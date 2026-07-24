import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type StockItem } from './StockItem';

export interface StockBatch extends BaseOpenmrsData {
  batchNo: string;
  expiration: Date;
  stockItem: StockItem;
}
