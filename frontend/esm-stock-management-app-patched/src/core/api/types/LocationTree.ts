import { type BaseOpenmrsObject } from './BaseOpenmrsObject';

export interface LocationTree extends BaseOpenmrsObject {
  parentLocationId: number;
  childLocationId: number;
  depth: number;
}
