import { type OpenmrsObject } from './OpenmrsObject';
import { type User } from './identity/User';

export interface Auditable extends OpenmrsObject {
  creator: User;
  dateCreated: Date;
  changedBy: User;
  dateChanged: Date;
}
