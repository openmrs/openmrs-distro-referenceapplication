export interface StockOperationItemCost {
  uuid: string;
  stockItemUuid: string;
  stockItemPackagingUOMUuid: string;
  stockItemPackagingUOMName: string;
  stockBatchId: number;
  stockBatchUuid: string;
  batchNo: string;
  quantity: number;
  unitCost: number;
  unitCostUOMUuid: string;
  unitCostUOMName: string;
  totalCost: number;
}
