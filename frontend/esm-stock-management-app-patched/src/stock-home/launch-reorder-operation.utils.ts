import { type TFunction } from 'i18next';
import { showSnackbar } from '@openmrs/esm-framework';
import { OperationType, type StockOperationType } from '../core/api/types/stockOperation/StockOperationType';
import { launchStockoperationAddOrEditWorkSpace } from '../stock-operations/stock-operation.utils';

/**
 * Opens a new Requisition operation - the actual "order more stock" workflow - instead
 * of the stock item edit workspace. There's no safe way to pre-fill a brand-new
 * operation's item list (the form's other tabs key off `stockOperation` being truthy to
 * mean "this operation already exists on the backend"), so this opens a blank
 * requisition and tells the user which item to add to it.
 */
export function launchReorderOperation(
  t: TFunction,
  requisitionOperationType: StockOperationType | undefined,
  itemDisplayName?: string,
) {
  if (!requisitionOperationType) {
    showSnackbar({
      kind: 'error',
      isLowContrast: true,
      title: t('reorder', 'Reorder'),
      subtitle: t(
        'noRequisitionPermission',
        "You don't have permission to create a requisition. Contact an administrator to update your role scopes.",
      ),
    });
    return;
  }

  launchStockoperationAddOrEditWorkSpace(t, requisitionOperationType);

  if (itemDisplayName) {
    showSnackbar({
      kind: 'info',
      isLowContrast: true,
      title: t('reorder', 'Reorder'),
      subtitle: t('addItemToRequisition', 'Add {{itemDisplayName}} to this requisition.', { itemDisplayName }),
    });
  }
}

export function findRequisitionOperationType(operationTypes: Array<StockOperationType> | undefined) {
  return operationTypes?.find((type) => type.operationType === OperationType.REQUISITION_OPERATION_TYPE);
}
