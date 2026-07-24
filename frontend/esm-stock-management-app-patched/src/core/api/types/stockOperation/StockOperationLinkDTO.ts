export interface StockOperationLinkDTO {
  uuid: string;
  parentUuid: string;
  parentOperationNumber: string;
  parentOperationTypeName: string;
  parentStatus: string;
  parentVoided: boolean;
  childUuid: string;
  childOperationNumber: string;
  childOperationTypeName: string;
  childStatus: string;
  childVoided: boolean;
}
