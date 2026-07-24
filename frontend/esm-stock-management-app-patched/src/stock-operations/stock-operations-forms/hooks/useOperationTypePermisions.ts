import {
  operationFromString,
  type StockOperationType,
  StockOperationTypeCanCapturePurchasePrice,
  StockOperationTypeHasPrint,
  StockOperationTypeIsNegativeQtyAllowed,
  StockOperationTypeIsQuantityOptional,
  StockOperationTypeRequiresActualBatchInformation,
  StockOperationTypeRequiresBatchUuid,
  StockOperationTypeRequiresDispatchAcknowledgement,
  StockOperationTypeRequiresStockAdjustmentReason,
} from '../../../core/api/types/stockOperation/StockOperationType';

const useOperationTypePermisions = (stockoperationType: StockOperationType) => {
  const opType = operationFromString(stockoperationType.operationType);
  return {
    isNegativeQuantityAllowed: StockOperationTypeIsNegativeQtyAllowed(opType),
    requiresBatchUuid: StockOperationTypeRequiresBatchUuid(opType),
    requiresActualBatchInfo: StockOperationTypeRequiresActualBatchInformation(opType),
    isQuantityOptional: StockOperationTypeIsQuantityOptional(opType),
    canCaptureQuantityPrice: StockOperationTypeCanCapturePurchasePrice(opType),
    requiresStockAdjustmentReason: StockOperationTypeRequiresStockAdjustmentReason(opType),
    requiresDispatchAcknowledgement: StockOperationTypeRequiresDispatchAcknowledgement(opType),
    allowExpiredBatchNumbers: stockoperationType?.allowExpiredBatchNumbers ?? false,
    allowPrinting: StockOperationTypeHasPrint(opType),
  };
};

export default useOperationTypePermisions;
