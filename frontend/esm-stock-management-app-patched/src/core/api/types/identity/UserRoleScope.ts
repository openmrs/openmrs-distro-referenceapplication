import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type UserRoleScopeLocation } from './UserRoleScopeLocation';
import { type UserRoleScopeOperationType } from './UserRoleScopeOperationType';

export interface UserRoleScope extends BaseOpenmrsData {
  userUuid?: string;
  role?: string;
  userName?: string;
  userGivenName?: string;
  userFamilyName?: string;
  permanent: boolean;
  activeFrom?: Date;
  activeTo?: Date;
  enabled: boolean;
  locations: UserRoleScopeLocation[];
  operationTypes: UserRoleScopeOperationType[];
}
