import { type StockOperationStatus } from '../stockOperation/StockOperationStatus';

export interface StockItemTransactionDTO {
  uuid: string;
  dateCreated: Date;
  partyUuid: string;
  partyName: string;
  isPatientTransaction: boolean;
  quantity: number;
  stockBatchUuid: string;
  stockBatchNo: string;
  expiration: Date;
  stockItemUuid: string;
  stockOperationUuid: string;
  stockOperationStatus: StockOperationStatus;
  stockOperationNumber: string;
  packagingUomFactor: string;
  stockOperationTypeName: string;
  stockItemPackagingUOMUuid: string;
  packagingUomName: string;
  operationSourcePartyName: string;
  operationDestinationPartyName: string;
  patientId: number;
  patientUuid: string;
}
