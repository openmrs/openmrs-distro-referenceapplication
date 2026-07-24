import { InlineLoading, Layer, Row } from '@carbon/react';
import React, { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { type StockOperationDTO } from '../../../core/api/types/stockOperation/StockOperationDTO';
import { useStockOperationTypes } from '../../../stock-lookups/stock-lookups.resource';
import StockoperationActions from '../../stock-operation-actions.component';
import StockOperationLinks from '../../stock-operation-links.component';
import StockItemsTable from './stock-items-table';
import styles from './stock-operation-expanded-row.scss';
import StockOpertationStatus from './stock-operations-status';

interface StockOperationExpandedRowProps {
  model: StockOperationDTO;
}

const StockOperationExpandedRow: React.FC<StockOperationExpandedRowProps> = (props) => {
  const { t } = useTranslation();
  const { types, isLoading, error } = useStockOperationTypes();
  const currentOperationType = useMemo(() => {
    return types?.results?.find(({ uuid }) => props.model.operationTypeUuid === uuid);
  }, [props.model.operationTypeUuid, types?.results]);
  return (
    <>
      <Layer className={styles.statusContainer}>
        <Row className={styles.statusContainerRow}>
          {props.model?.dateCreated && (
            <StockOpertationStatus
              status={t('started', 'Started')}
              statusFilledDate={props.model?.dateCreated.toString()}
              statusFillerFamilyName={props.model?.creatorFamilyName}
              statusFillerGivenName={props.model?.creatorGivenName}
            />
          )}
          {props.model.submittedDate && (
            <StockOpertationStatus
              status={t('submitted', 'Submitted')}
              statusFilledDate={props.model?.submittedDate.toString()}
              statusFillerFamilyName={props.model?.submittedByFamilyName}
              statusFillerGivenName={props.model?.submittedByGivenName}
            />
          )}
          {props.model?.dispatchedDate && (
            <StockOpertationStatus
              status={t('dispatched', 'Dispatched')}
              statusFilledDate={props.model?.dispatchedDate.toString()}
              statusFillerFamilyName={props.model?.dispatchedByFamilyName}
              statusFillerGivenName={props.model?.dispatchedByGivenName}
            />
          )}
          {props.model?.returnedDate && (
            <StockOpertationStatus
              status={t('returned', 'Returned')}
              statusFilledDate={props.model?.returnedDate.toString()}
              statusFillerFamilyName={props.model?.returnedByFamilyName}
              statusFillerGivenName={props.model?.returnedByGivenName}
              extraStatusinfo={<span className={styles.text}>{props.model?.returnReason}</span>}
            />
          )}
          {props.model?.completedDate && (
            <StockOpertationStatus
              status={t('completed', 'Completed')}
              statusFilledDate={props.model?.completedDate.toString()}
              statusFillerFamilyName={props.model?.completedByFamilyName}
              statusFillerGivenName={props.model?.completedByGivenName}
            />
          )}
          {props.model?.status === 'CANCELLED' && (
            <StockOpertationStatus
              status={t('cancelled', 'Cancelled')}
              statusFilledDate={props.model?.cancelledDate.toString()}
              statusFillerFamilyName={props.model?.cancelledByFamilyName}
              statusFillerGivenName={props.model?.cancelledByGivenName}
              extraStatusinfo={<span className={styles.text}>{props.model?.cancelReason}</span>}
            />
          )}
          {props.model?.status === 'REJECTED' && (
            <StockOpertationStatus
              status={t('rejected', 'Rejected')}
              statusFilledDate={props.model?.rejectedDate.toString()}
              statusFillerFamilyName={props.model?.rejectedByFamilyName}
              statusFillerGivenName={props.model?.rejectedByGivenName}
              extraStatusinfo={<span>{props.model?.rejectionReason}</span>}
            />
          )}
        </Row>
        <Row className={styles.statusContainerRow}>
          {isLoading && (
            <InlineLoading
              description={t('loadingStockOperationLinks', 'Loading stock operation links...')}
              iconDescription={t('loadingStockOperationLinksIcon', 'Loading stock operation links')}
            />
          )}
          {currentOperationType && (
            <StockOperationLinks stockOperationType={currentOperationType} stockOperation={props.model} />
          )}
        </Row>
        <Row className={styles.statusContainerRow}>
          <StockItemsTable items={props.model.stockOperationItems} />
        </Row>
        <Row className={styles.statusContainerRow}>
          {isLoading && (
            <InlineLoading
              description={t('loadingOperationActions', 'Loading stock operation actions...')}
              iconDescription={t('loadingOperationActionsIcon', 'Loading stock operation actions')}
            />
          )}
          {currentOperationType && (
            <StockoperationActions stockOperationType={currentOperationType} stockOperation={props.model} />
          )}
        </Row>
      </Layer>
    </>
  );
};

export default StockOperationExpandedRow;
