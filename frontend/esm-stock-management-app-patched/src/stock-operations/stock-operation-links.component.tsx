import React, { useMemo } from 'react';
import { type StockOperationDTO } from '../core/api/types/stockOperation/StockOperationDTO';
import { OperationType, type StockOperationType } from '../core/api/types/stockOperation/StockOperationType';
import useStockOperationLinks from './stock-operations-forms/hooks/useStockOperationLinks';
import styles from './stock-operations-table.scss';
import StockOperationRelatedLink from './stock-operations-forms/stock-operation-related-link.component';

type Props = {
  stockOperation: StockOperationDTO;
  stockOperationType: StockOperationType;
};

const StockOperationLinks: React.FC<Props> = ({ stockOperation, stockOperationType }) => {
  const requisitionOperationUuid = useMemo(() => {
    if (
      stockOperationType?.operationType === OperationType.REQUISITION_OPERATION_TYPE ||
      stockOperation?.operationType === OperationType.REQUISITION_OPERATION_TYPE ||
      stockOperationType?.operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE ||
      stockOperation?.operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE
    ) {
      return stockOperation.uuid;
    }
    return null;
  }, [stockOperationType, stockOperation]);
  const { error, isLoading, operationLinks } = useStockOperationLinks(requisitionOperationUuid);
  if (isLoading || error) return null;
  return (
    <div>
      {' '}
      {operationLinks && operationLinks.length > 0 && (
        <div>
          <h6 className={styles.relatedTransactionHeader}>Related Transactions:</h6>
          {operationLinks.map(
            (item) =>
              (stockOperation.uuid === item?.parentUuid || stockOperationType?.uuid === item?.parentUuid) && (
                <React.Fragment key={item.uuid}>
                  <span>{item?.childOperationTypeName}</span>
                  <span className={item?.childVoided ? 'voided' : ''}>
                    {' '}
                    {item?.childVoided && item?.childOperationNumber}
                    {!item?.childVoided && (
                      <span className={styles.relatedLink}>
                        <StockOperationRelatedLink
                          stockOperationUuid={item?.childUuid}
                          operationNumber={item?.childOperationNumber}
                        />
                      </span>
                    )}
                  </span>{' '}
                  <span>[{item?.childStatus}]</span>
                </React.Fragment>
              ),
          )}
          <span> </span>
          {operationLinks.map(
            (item) =>
              (stockOperation.uuid === item?.childUuid || stockOperationType.uuid === item?.childUuid) && (
                <React.Fragment key={item.uuid}>
                  <span>{item?.parentOperationTypeName}</span>
                  <span className={item?.parentVoided ? 'voided' : ''}>
                    {' '}
                    {item?.parentVoided && item?.parentOperationNumber}
                    {!item?.parentVoided && (
                      <span className={styles.relatedLink}>
                        <StockOperationRelatedLink
                          stockOperationUuid={item?.parentUuid}
                          operationNumber={item?.parentOperationNumber}
                        />
                      </span>
                    )}
                  </span>{' '}
                  <span>[{item?.parentStatus}]</span>
                </React.Fragment>
              ),
          )}
        </div>
      )}
    </div>
  );
};

export default StockOperationLinks;
