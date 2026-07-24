export interface RadioOption {
  label: string;
  value: boolean;
}

export enum StockItemType {
  PHARMACEUTICALS = 'Pharmaceuticals',
  NONE_PHARMACEUTICALS = 'Non Pharmaceuticals',
}

export const radioOptions: RadioOption[] = [
  { label: StockItemType.PHARMACEUTICALS, value: true },
  { label: StockItemType.NONE_PHARMACEUTICALS, value: false },
];

export const expirationOptions: RadioOption[] = [
  { label: 'Yes', value: true },
  { label: 'No', value: false },
];
