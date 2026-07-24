export interface StockItemInventory {
  partyUuid: string;
  partyName: string;
  stockItemUuid: string;
  stockBatchUuid: string;
  batchNumber: string;
  quantity: number;
  quantityUoM: string;
  quantityFactor: string;
  expiration: Date;
  quantityUoMUuid?: string;
}
