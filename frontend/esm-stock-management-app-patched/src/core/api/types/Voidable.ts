import { type User } from './identity/User';
import { type OpenmrsObject } from './OpenmrsObject';

export interface Voidable extends OpenmrsObject {
  dateVoided: Date;
  voidedBy: User;
  voidReason: string;
  voided: boolean;
}
