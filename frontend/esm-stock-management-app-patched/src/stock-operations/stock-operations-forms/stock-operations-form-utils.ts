import { type StockOperationDTO } from '../../core/api/types/stockOperation/StockOperationDTO';
import { OperationType } from '../../core/api/types/stockOperation/StockOperationType';

export function mapIssueStockLocations(stockOperation) {
  /** Since we are using requisition information to issue stock,
      please note that the locations will be inverted: the destination listed on the requisition will become the issuing location.
  */
  const { sourceUuid, sourceName, destinationUuid, destinationName } = stockOperation;
  return {
    ...stockOperation,
    sourceUuid: destinationUuid,
    sourceName: destinationName,
    destinationUuid: sourceUuid,
    destinationName: sourceName,
  };
}

export const getStockOperationLocationByOperationType = (
  operationType: OperationType,
  stockOperation?: StockOperationDTO,
) => {
  if (operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE)
    return {
      sourceUuid: stockOperation?.destinationUuid ?? '',
      destinationUuid: stockOperation?.sourceUuid ?? '',
    };

  return {
    sourceUuid: stockOperation?.sourceUuid ?? '',
    destinationUuid: stockOperation?.destinationUuid ?? '',
  };
};
