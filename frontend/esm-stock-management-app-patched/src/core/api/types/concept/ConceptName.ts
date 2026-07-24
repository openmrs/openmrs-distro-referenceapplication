import { type BaseOpenmrsObject } from '../BaseOpenmrsObject';
import { type Concept } from './Concept';

export interface ConceptName extends BaseOpenmrsObject {
  conceptNameId: number;
  concept: Concept;
  name: string;
  localePreferred: boolean;
  short: boolean;
  preferred: boolean;
  indexTerm: boolean;
  synonym: boolean;
  fullySpecifiedName: boolean;
}
