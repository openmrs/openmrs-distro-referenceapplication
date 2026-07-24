import { type BaseOpenmrsData } from '../BaseOpenmrsData';
import { type Patient } from '../identity/Patient';
import { type OpenMRSLocation } from '../Location';
import { type User } from '../identity/User';
import { type StockOperationItem } from './StockOperationItem';
import { type StockOperationStatus } from './StockOperationStatus';
import { type StockOperationType } from './StockOperationType';
import { type Concept } from '../concept/Concept';

export interface StockOperation extends BaseOpenmrsData {
  cancelReason: string;
  cancelledBy: User;
  cancelledDate: Date;
  completedBy: User;
  completedDate: Date;
  destination: OpenMRSLocation;
  externalReference: string;
  location: OpenMRSLocation;
  operationDate: Date;
  locked: boolean;
  operationNumber: string;
  operationOrder: number;
  patient: Patient;
  remarks: string;
  source: OpenMRSLocation;
  sourceOther: string;
  status: StockOperationStatus;
  returnReason: string;
  rejectionReason: string;
  workflowId: number;
  responsiblePerson: User;
  responsiblePersonOther: string;
  consignmentCategory: Concept;
  stockOperationType: StockOperationType;
  stockOperationItems: StockOperationItem[];
}
