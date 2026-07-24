import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type StockBatch } from '../stockItem/StockBatch';
import { type StockItem } from '../stockItem/StockItem';
import { type StockItemPackagingUOM } from '../stockItem/StockItemPackagingUOM';
import { type StockOperation } from './StockOperation';

export interface StockOperationItem extends BaseOpenmrsData {
  quantity: number;
  stockBatch: StockBatch;
  stockItemPackagingUOM: StockItemPackagingUOM;
  stockItem: StockItem;
  stockOperation: StockOperation;
}
