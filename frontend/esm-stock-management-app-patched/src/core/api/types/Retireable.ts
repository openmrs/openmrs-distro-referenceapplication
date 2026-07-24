import { type User } from './identity/User';
import { type OpenmrsObject } from './OpenmrsObject';

export interface Retireable extends OpenmrsObject {
  retired: boolean;
  dateRetired: Date;
  retiredBy: User;
  retireReason: string;
}
