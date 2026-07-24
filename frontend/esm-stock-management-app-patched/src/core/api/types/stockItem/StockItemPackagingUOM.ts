import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type Concept } from '../concept/Concept';
import { type StockItem } from './StockItem';

export interface StockItemPackagingUOM extends BaseOpenmrsData {
  factor: number;
  packagingUom: Concept;
  stockItem: StockItem;
}

export interface StockItemPackagingUOMDTO {
  id?: string;
  uuid?: string;
  stockItemUuid?: string;
  packagingUomName?: string;
  packagingUomUuid?: string;
  factor?: number | null;
  isDefaultStockOperationsUoM?: boolean;
  isDispensingUnit?: boolean;
}
