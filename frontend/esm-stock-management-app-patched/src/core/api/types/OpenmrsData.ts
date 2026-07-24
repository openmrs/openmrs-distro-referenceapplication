import { type OpenmrsObject } from './OpenmrsObject';
import { type Auditable } from './Auditable';
import { type Voidable } from './Voidable';

export interface OpenmrsData extends OpenmrsObject, Auditable, Voidable {}
