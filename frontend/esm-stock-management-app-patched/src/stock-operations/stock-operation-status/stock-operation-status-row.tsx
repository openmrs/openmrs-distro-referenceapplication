import React from 'react';
import { type StockOperationDTO } from '../../core/api/types/stockOperation/StockOperationDTO';
import styles from './stock-operation-status.scss';
import StockOpertationStatus from './stock-operation-status';
import { useTranslation } from 'react-i18next';
import { Row } from '@carbon/react';
type Props = {
  stockOperation: StockOperationDTO;
};
const StockOperationStatusRow: React.FC<Props> = ({ stockOperation }) => {
  const { t } = useTranslation();
  return (
    <Row className={styles.statusContainerRow}>
      {stockOperation.dateCreated && (
        <StockOpertationStatus
          status={t('started', 'Started')}
          statusFilledDate={stockOperation.dateCreated.toString()}
          statusFillerFamilyName={stockOperation.creatorFamilyName}
          statusFillerGivenName={stockOperation.creatorGivenName}
        />
      )}
      {stockOperation?.submittedDate && (
        <StockOpertationStatus
          status={t('submitted', 'Submitted')}
          statusFilledDate={stockOperation.submittedDate.toString()}
          statusFillerFamilyName={stockOperation.submittedByFamilyName}
          statusFillerGivenName={stockOperation.submittedByGivenName}
        />
      )}
      {stockOperation.dispatchedDate && (
        <StockOpertationStatus
          status={t('dispatched', 'Dispatched')}
          statusFilledDate={stockOperation.dispatchedDate.toString()}
          statusFillerFamilyName={stockOperation.dispatchedByFamilyName}
          statusFillerGivenName={stockOperation.dispatchedByGivenName}
        />
      )}
      {stockOperation.returnedDate && (
        <StockOpertationStatus
          status={t('returned', 'Returned')}
          statusFilledDate={stockOperation.returnedDate.toString()}
          statusFillerFamilyName={stockOperation.returnedByFamilyName}
          statusFillerGivenName={stockOperation.returnedByGivenName}
          extraStatusinfo={<span className={styles.text}>{stockOperation.returnReason}</span>}
        />
      )}
      {stockOperation.completedDate && (
        <StockOpertationStatus
          status={t('completed', 'Completed')}
          statusFilledDate={stockOperation.completedDate.toString()}
          statusFillerFamilyName={stockOperation.completedByFamilyName}
          statusFillerGivenName={stockOperation.completedByGivenName}
        />
      )}
      {stockOperation.status === 'CANCELLED' && (
        <StockOpertationStatus
          status={t('cancelled', 'Cancelled')}
          statusFilledDate={stockOperation.cancelledDate.toString()}
          statusFillerFamilyName={stockOperation.cancelledByFamilyName}
          statusFillerGivenName={stockOperation.cancelledByGivenName}
          extraStatusinfo={<span className={styles.text}>{stockOperation.cancelReason}</span>}
        />
      )}
      {stockOperation.status === 'REJECTED' && (
        <StockOpertationStatus
          status={t('rejected', 'Rejected')}
          statusFilledDate={stockOperation.rejectedDate.toString()}
          statusFillerFamilyName={stockOperation.rejectedByFamilyName}
          statusFillerGivenName={stockOperation.rejectedByGivenName}
          extraStatusinfo={<span>{stockOperation.rejectionReason}</span>}
        />
      )}
    </Row>
  );
};

export default StockOperationStatusRow;
