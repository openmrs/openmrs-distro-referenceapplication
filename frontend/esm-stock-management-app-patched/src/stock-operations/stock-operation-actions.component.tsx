import React, { useMemo } from 'react';
import { type StockOperationDTO } from '../core/api/types/stockOperation/StockOperationDTO';
import {
  operationFromString,
  OperationType,
  type StockOperationType,
  StockOperationTypeHasPrint,
} from '../core/api/types/stockOperation/StockOperationType';
import StockOperationApprovalButton from './stock-operations-modal/stock-operations-approve-button.component';
import StockOperationApproveDispatchButton from './stock-operations-modal/stock-operations-approve-dispatch-button.component';
import StockOperationCancelButton from './stock-operations-modal/stock-operations-cancel-button.component';
import StockOperationCompleteDispatchButton from './stock-operations-modal/stock-operations-completed-dispatch-button.component';
import StockOperationIssueStockButton from './stock-operations-modal/stock-operations-issue-stock-button.component';
import StockOperationPrintButton from './stock-operations-modal/stock-operations-print-button.component';
import StockOperationRejectButton from './stock-operations-modal/stock-operations-reject-button.component';
import StockOperationReturnButton from './stock-operations-modal/stock-operations-return-button.component';
import useOperationTypePermisions from './stock-operations-forms/hooks/useOperationTypePermisions';
import styles from './stock-operations-table.scss';

type Props = {
  stockOperation: StockOperationDTO;
  stockOperationType: StockOperationType;
};

const StockoperationActions: React.FC<Props> = ({ stockOperation, stockOperationType }) => {
  const operationTypePermision = useOperationTypePermisions(stockOperationType);
  const operationType = useMemo(() => {
    return operationFromString(stockOperationType.operationType);
  }, [stockOperationType]);
  return (
    <>
      {((!stockOperation.permission?.canEdit &&
        (stockOperation.permission?.canApprove || stockOperation.permission?.canReceiveItems)) ||
        stockOperation.permission?.canEdit ||
        StockOperationTypeHasPrint(operationType) ||
        (stockOperation?.permission?.isRequisitionAndCanIssueStock ?? false) ||
        stockOperation.permission?.isRequisitionAndCanIssueStock) && (
        <div className={styles.actionBtns}>
          <>
            {!stockOperation.permission?.canEdit && stockOperation.permission?.canApprove && (
              <>
                {!operationTypePermision.requiresDispatchAcknowledgement && (
                  <StockOperationApprovalButton operation={stockOperation} />
                )}

                {operationTypePermision.requiresDispatchAcknowledgement && (
                  <StockOperationApproveDispatchButton operation={stockOperation} />
                )}

                <StockOperationRejectButton operation={stockOperation} />
                <StockOperationReturnButton operation={stockOperation} />
                <StockOperationCancelButton operation={stockOperation} />
              </>
            )}

            {!stockOperation.permission?.canEdit && stockOperation.permission?.canReceiveItems && (
              <>
                <StockOperationCompleteDispatchButton operation={stockOperation} reason={false} />
                <StockOperationReturnButton operation={stockOperation} />
              </>
            )}

            {stockOperation.permission?.canEdit && <StockOperationCancelButton operation={stockOperation} />}
            {stockOperation.permission?.isRequisitionAndCanIssueStock && (
              <StockOperationIssueStockButton operation={stockOperation} />
            )}
            {(stockOperation.permission?.isRequisitionAndCanIssueStock ||
              stockOperation.operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE ||
              stockOperation.operationType === OperationType.REQUISITION_OPERATION_TYPE ||
              stockOperation.operationType === OperationType.RECEIPT_OPERATION_TYPE ||
              stockOperation.operationType === OperationType.TRANSFER_OUT_OPERATION_TYPE) && (
              <StockOperationPrintButton operation={stockOperation} />
            )}
          </>
        </div>
      )}
    </>
  );
};

export default StockoperationActions;
