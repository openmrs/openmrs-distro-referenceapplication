export interface StopOperationAction {
  name: StopOperationActionType;
  reason?: string | null;
  uuid: string;
  lineItems?: StockOperationActionLineItem[];
}

export interface StockOperationActionLineItem {
  uuid: string;
  amount: number;
  packagingUoMUuId: string;
}

export const StopOperationActionTypes = [
  'SUBMIT',
  'DISPATCH',
  'APPROVE',
  'RETURN',
  'REJECT',
  'COMPLETE',
  'CANCEL',
  'QUANTITY_RECEIVED',
] as const;
export type StopOperationActionType = (typeof StopOperationActionTypes)[number];
