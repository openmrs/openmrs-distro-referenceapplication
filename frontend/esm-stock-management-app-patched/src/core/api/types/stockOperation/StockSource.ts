import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type Concept } from '../concept/Concept';

export interface StockSource extends BaseOpenmrsData {
  name: string;
  acronym: string;
  sourceType: Concept | undefined;
}
