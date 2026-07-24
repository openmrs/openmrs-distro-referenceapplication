import { type BaseOpenmrsData } from '../BaseOpenmrsData';

export interface UserRoleScopeOperationType extends BaseOpenmrsData {
  operationTypeUuid: string;
  operationTypeName: string;
}
