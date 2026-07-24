import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type LocationType } from './LocationType';

export interface StockOperationType extends BaseOpenmrsData {
  name: string;
  description: string;
  operationType: string;
  hasSource: boolean;
  sourceType: LocationType;
  hasDestination: boolean;
  destinationType: LocationType;
  hasRecipient: boolean;
  recipientRequired: boolean;
  availableWhenReserved: boolean;
  allowExpiredBatchNumbers: boolean;
  stockOperationTypeLocationScopes: StockOperationTypeLocationScope[];
}

export interface StockOperationTypeLocationScope {
  uuid: string;
  locationTag: string;
  isSource: string;
  isDestination: string;
}

export enum OperationType {
  TRANSFER_OUT_OPERATION_TYPE = 'transferout',
  DISPOSED_OPERATION_TYPE = 'disposed',
  STOCK_ISSUE_OPERATION_TYPE = 'stockissue',
  STOCK_TAKE_OPERATION_TYPE = 'stocktake',
  REQUISITION_OPERATION_TYPE = 'requisition',
  OPENING_STOCK_OPERATION_TYPE = 'initial',
  RECEIPT_OPERATION_TYPE = 'receipt',
  RETURN_OPERATION_TYPE = 'return',
  ADJUSTMENT_OPERATION_TYPE = 'adjustment',
}

export function operationFromString(str: string): OperationType | undefined {
  str = str?.toLowerCase();
  if (str === OperationType.TRANSFER_OUT_OPERATION_TYPE) return OperationType.TRANSFER_OUT_OPERATION_TYPE;
  if (str === OperationType.DISPOSED_OPERATION_TYPE) return OperationType.DISPOSED_OPERATION_TYPE;
  if (str === OperationType.STOCK_ISSUE_OPERATION_TYPE) return OperationType.STOCK_ISSUE_OPERATION_TYPE;
  if (str === OperationType.STOCK_TAKE_OPERATION_TYPE) return OperationType.STOCK_TAKE_OPERATION_TYPE;
  if (str === OperationType.REQUISITION_OPERATION_TYPE) return OperationType.REQUISITION_OPERATION_TYPE;
  if (str === OperationType.RECEIPT_OPERATION_TYPE) return OperationType.RECEIPT_OPERATION_TYPE;
  if (str === OperationType.RETURN_OPERATION_TYPE) return OperationType.RETURN_OPERATION_TYPE;
  if (str === OperationType.ADJUSTMENT_OPERATION_TYPE) return OperationType.ADJUSTMENT_OPERATION_TYPE;
  if (str === OperationType.OPENING_STOCK_OPERATION_TYPE) return OperationType.OPENING_STOCK_OPERATION_TYPE;
}

export const StockOperationTypeRequiresStockAdjustmentReason = (operationType: OperationType) => {
  return (
    operationType === OperationType.ADJUSTMENT_OPERATION_TYPE ||
    operationType === OperationType.STOCK_TAKE_OPERATION_TYPE ||
    operationType === OperationType.DISPOSED_OPERATION_TYPE
  );
};

export const StockOperationTypeIsNegativeQtyAllowed = (operationType: OperationType) => {
  return operationType === OperationType.ADJUSTMENT_OPERATION_TYPE;
};

export const StockOperationTypeRequiresBatchUuid = (operationType: OperationType) => {
  return (
    !StockOperationTypeRequiresActualBatchInformation(operationType) &&
    operationType !== OperationType.REQUISITION_OPERATION_TYPE
  );
};

export const StockOperationTypeRequiresActualBatchInformation = (operationType: OperationType) => {
  return (
    operationType === OperationType.RECEIPT_OPERATION_TYPE ||
    operationType === OperationType.OPENING_STOCK_OPERATION_TYPE
  );
};

export const StockOperationTypeIsQuantityOptional = (operationType: OperationType) => {
  return operationType === OperationType.REQUISITION_OPERATION_TYPE;
};

export const StockOperationTypeCanCapturePurchasePrice = (operationType: OperationType) => {
  return (
    operationType === OperationType.RECEIPT_OPERATION_TYPE ||
    operationType === OperationType.OPENING_STOCK_OPERATION_TYPE
  );
};

export const StockOperationTypeCanBeRelatedToRequisition = (operationType: OperationType) => {
  return operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE;
};

export const StockOperationTypeRequiresDispatchAcknowledgement = (operationType: OperationType) => {
  return (
    operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE || operationType === OperationType.RETURN_OPERATION_TYPE
  );
};

export const StockOperationTypeHasPrint = (operationType: OperationType) => {
  return (
    operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE ||
    StockOperationTypeIsRequistion(operationType) ||
    StockOperationTypeIsReceipt(operationType) ||
    StockOperationTypeIsTransferOut(operationType)
  );
};

export const StockOperationTypeIsRequistion = (operationType: OperationType) => {
  return operationType === OperationType.REQUISITION_OPERATION_TYPE;
};

export const StockOperationTypeIsReceipt = (operationType: OperationType) => {
  return operationType === OperationType.RECEIPT_OPERATION_TYPE;
};

export const StockOperationTypeIsTransferOut = (operationType: OperationType) => {
  return operationType === OperationType.TRANSFER_OUT_OPERATION_TYPE;
};

export const StockOperationTypeIsStockIssue = (operationType: OperationType) => {
  return operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE;
};

export const StockOperationPrintHasItemCosts = (operationType: OperationType) => {
  return StockOperationTypeIsStockIssue(operationType) || StockOperationTypeIsTransferOut(operationType);
};
