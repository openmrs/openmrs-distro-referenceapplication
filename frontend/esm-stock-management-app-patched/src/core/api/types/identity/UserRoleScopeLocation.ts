import { type BaseOpenmrsData } from '../BaseOpenmrsData';

export interface UserRoleScopeLocation extends BaseOpenmrsData {
  locationUuid: string;
  locationName: string;
  enableDescendants: boolean;
}
