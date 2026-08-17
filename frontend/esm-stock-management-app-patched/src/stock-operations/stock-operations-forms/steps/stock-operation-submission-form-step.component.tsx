import React, { useCallback, useMemo, useState } from 'react';
import { Button, Column, InlineLoading, RadioButton, RadioButtonGroup, Stack } from '@carbon/react';
import { ArrowLeft, ArrowRight, Departure, ListChecked, Save, SendFilled } from '@carbon/react/icons';
import { useFormContext } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { restBaseUrl, showSnackbar, useSession } from '@openmrs/esm-framework';
import { createStockOperation, deleteStockOperationItem, updateStockOperation } from '../../stock-operations.resource';
import { extractErrorMessagesFromResponse } from '../../../constants';
import { handleMutate } from '../../../utils';
import {
  OperationType,
  StockOperationTypeRequiresApproval,
  type StockOperationType,
} from '../../../core/api/types/stockOperation/StockOperationType';
import { otherUser } from '../../../core/utils/utils';
import { launchStockOperationsModal } from '../../stock-operation.utils';
import { type StockOperationDTO } from '../../../core/api/types/stockOperation/StockOperationDTO';
import { type StockOperationItemDTO } from '../../../core/api/types/stockOperation/StockOperationItemDTO';
import { type StockOperationItemDtoSchema } from '../../validation-schema';
import { buildExternalReference } from '../../external-reference.utils';
import useOperationTypePermisions from '../hooks/useOperationTypePermisions';
import styles from '../stock-operation-form.scss';

type StockOperationSubmissionFormStepProps = {
  onPrevious?: () => void;
  stockOperation?: StockOperationDTO;
  stockOperationType: StockOperationType;
  onNext?: () => void;
  dismissWorkspace?: () => void;
};

const StockOperationSubmissionFormStep: React.FC<StockOperationSubmissionFormStepProps> = ({
  onPrevious,
  stockOperationType,
  stockOperation,
  onNext,
  dismissWorkspace,
}) => {
  const { t } = useTranslation();
  const { sessionLocation } = useSession();
  const operationTypePermision = useOperationTypePermisions(stockOperationType);
  const editable = useMemo(() => !stockOperation || stockOperation.status === 'NEW', [stockOperation]);
  const form = useFormContext<StockOperationItemDtoSchema>();
  const isOpeningStockOperation = useMemo(
    () => StockOperationTypeRequiresApproval(stockOperationType.operationType as OperationType),
    [stockOperationType],
  );
  const [approvalRequired, setApprovalRequired] = useState<boolean | null>(
    isOpeningStockOperation ? true : stockOperation?.approvalRequired,
  );
  const isStockIssueOperation = useMemo(
    () => OperationType.STOCK_ISSUE_OPERATION_TYPE === stockOperationType.operationType,
    [stockOperationType],
  );
  const handleRadioButtonChange = (selectedItem: boolean) => {
    if (isOpeningStockOperation) {
      // Opening Stock always requires approval - the choice isn't editable.
      return;
    }
    setApprovalRequired(selectedItem);
  };

  const handleSave = useCallback(async () => {
    let result: StockOperationDTO; // To store the result for returning
    await form.handleSubmit(async (formData) => {
      try {
        // Get deleted items (items in stock operation bt not i form data)
        const itemsToDelete =
          stockOperation?.stockOperationItems?.reduce<Array<StockOperationItemDTO>>((prev, curr) => {
            const itemDoNotExistInFormData =
              formData.stockOperationItems.findIndex((item) => item.uuid === curr.uuid) === -1;
            if (itemDoNotExistInFormData) {
              return [...prev, curr];
            }
            return prev;
          }, []) ?? [];
        // Delete them from backend asynchronosely
        const deleted = await Promise.allSettled(itemsToDelete.map((item) => deleteStockOperationItem(item.uuid)));
        // Give delete status on completion
        deleted.forEach((del, index) => {
          showSnackbar({
            kind: del.status === 'rejected' ? 'error' : 'success',
            title:
              del.status === 'rejected'
                ? t('stockOperationItemDeleteError', 'Error deleting stock operation item {{item}}', {
                    item: itemsToDelete[index].commonName,
                  })
                : t('success', 'Success'),
            subtitle:
              del.status === 'rejected'
                ? del.reason?.message
                : t('stockOperationItemDeleteSuccess', 'Stock operation item {{item}} deleted successfully', {
                    item: itemsToDelete[index].commonName,
                  }),
          });
        });
        // construct update payload
        const payload = {
          ...formData,
          // Remove other uuid if responsible person is set to other
          responsiblePersonUuid:
            formData.responsiblePersonUuid === otherUser.uuid ? undefined : formData.responsiblePersonUuid,
          // This form never collects a location itself; always send the current UI session's location
          atLocationUuid: sessionLocation?.uuid,
          approvalRequired: approvalRequired ? true : false,
          // The backend has no dedicated columns for these three - pack them into the one
          // free-text field it does have (externalReference) instead of sending them raw.
          externalReference: buildExternalReference({
            purchaseOrderNo: formData.purchaseOrderNo,
            purchaseRequestNo: formData.purchaseRequestNo,
            projectFundCode: formData.projectFundCode,
          }),
          purchaseOrderNo: undefined,
          purchaseRequestNo: undefined,
          projectFundCode: undefined,
          stockOperationItems: [
            ...formData.stockOperationItems.map((item) => ({
              ...item,
              uuid:
                item.uuid.startsWith('new-item-') || (!stockOperation && isStockIssueOperation) ? undefined : item.uuid, // Remove uuid for newly inserted items and stock issue items derived from requisition to avoid foreign key constraint lookup error
            })),
          ],
        };
        const resp = await (stockOperation
          ? updateStockOperation(stockOperation, payload as any)
          : createStockOperation(payload as any));
        result = resp.data; // Store the response data
        handleMutate(`${restBaseUrl}/stockmanagement/stockoperation`);
        dismissWorkspace?.();
        showSnackbar({
          isLowContrast: true,
          title: stockOperation
            ? t('editStockOperation', 'Edit stock operation')
            : t('addStockOperation', 'Add stock operation'),
          kind: 'success',
          subtitle: stockOperation
            ? t('stockOperationEdited', 'Stock operation edited successfully')
            : t('stockOperationAdded', 'Stock operation added successfully'),
        });
      } catch (error) {
        const errorMessages = extractErrorMessagesFromResponse(error);
        showSnackbar({
          subtitle: errorMessages.join(', '),
          title: t('errorSavingForm', 'Error on saving form'),
          kind: 'error',
          isLowContrast: true,
        });
        throw error;
      }
    })(); // Call handleSubmit to trigger validation and submission
    return result; // Return the result after handleSubmit completes
  }, [form, stockOperation, t, approvalRequired, isStockIssueOperation, dismissWorkspace]);

  const handleComplete = useCallback(() => {
    handleSave().then((operation) => {
      if (!operation) return;
      launchStockOperationsModal('Complete', false, { ...operation, status: 'COMPLETED' });
    });
  }, [handleSave]);
  const handleSubmitForReview = useCallback(() => {
    handleSave().then((operation) => {
      if (!operation) return;
      launchStockOperationsModal('Submit', false, { ...operation, status: 'SUBMITTED' });
    });
  }, [handleSave]);
  const handleDispatch = useCallback(() => {
    handleSave().then((operation) => {
      if (!operation) return;
      launchStockOperationsModal('Dispatch', false, { ...operation, status: 'DISPATCHED' });
    });
  }, [handleSave]);

  return (
    <Stack gap={4} className={styles.grid}>
      <div className={styles.heading}>
        <h4>
          {operationTypePermision?.requiresDispatchAcknowledgement
            ? t('submitAndDispatch', 'Submit/Dispatch')
            : t('submitAndComplete', 'Submit/Complete')}
        </h4>
      </div>

      <Column>
        <RadioButtonGroup
          name="rbgApprovelRequired"
          legendText={t('doesThisTransactionRequireApproval', 'Does the transaction require approval ?')}
          onChange={(value) => handleRadioButtonChange(value === 'true')}
          readOnly={!editable || isOpeningStockOperation}
          valueSelected={approvalRequired === true ? 'true' : approvalRequired === false ? 'false' : null}
        >
          <RadioButton value="true" id="rbgApprovelRequired-true" labelText={t('yes', 'Yes')} />
          <RadioButton value="false" id="rbgApprovelRequired-false" labelText={t('no', 'No')} />
        </RadioButtonGroup>
      </Column>
      {editable && (
        <Column>
          {approvalRequired != null && (
            <>
              {!operationTypePermision.requiresDispatchAcknowledgement && !approvalRequired && (
                <Button
                  name="complete"
                  data-testid="complete-button"
                  type="button"
                  style={{ margin: '4px' }}
                  className="submitButton"
                  kind="primary"
                  onClick={handleComplete}
                  renderIcon={ListChecked}
                >
                  {t('complete', 'Complete')}
                </Button>
              )}
              {operationTypePermision.requiresDispatchAcknowledgement && !approvalRequired && (
                <Button
                  name="dispatch"
                  type="button"
                  style={{ margin: '4px' }}
                  data-testid="dipatch-button"
                  className="submitButton"
                  kind="primary"
                  onClick={handleDispatch}
                  renderIcon={Departure}
                >
                  {form.formState.isSubmitting ? (
                    <InlineLoading description={t('dispatching', 'Dispatching')} />
                  ) : (
                    t('dispatch', 'Dispatch')
                  )}
                </Button>
              )}
              {approvalRequired && (
                <Button
                  name="submit"
                  type="button"
                  style={{ margin: '4px' }}
                  className="submitButton"
                  kind="primary"
                  onClick={handleSubmitForReview}
                  renderIcon={SendFilled}
                >
                  {form.formState.isSubmitting ? (
                    <InlineLoading description={t('submittingForReview', 'Submitting for review')} />
                  ) : (
                    t('submitForReview', 'Submit For Review')
                  )}
                </Button>
              )}
            </>
          )}
          <Button
            name="save"
            type="button"
            className="submitButton"
            style={{ margin: '4px' }}
            disabled={form.formState.isSubmitting}
            kind="secondary"
            onClick={handleSave}
            renderIcon={Save}
          >
            {form.formState.isSubmitting ? <InlineLoading /> : t('save', 'Save')}
          </Button>
        </Column>
      )}
      <div className={styles.btnSet}>
        {typeof onNext === 'function' && (
          <Button kind="tertiary" onClick={onNext} renderIcon={ArrowRight}>
            {t('nextButton', 'Next')}
          </Button>
        )}
        {typeof onPrevious === 'function' && (
          <Button
            kind="tertiary"
            onClick={onPrevious}
            renderIcon={ArrowLeft}
            hasIconOnly
            data-testid="previous-btn"
            iconDescription={t('previousButton', 'Previous')}
          />
        )}
      </div>
    </Stack>
  );
};

export default StockOperationSubmissionFormStep;
