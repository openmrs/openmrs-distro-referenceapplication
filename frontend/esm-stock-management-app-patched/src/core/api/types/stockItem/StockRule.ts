import { type RecordPermission } from '../RecordPermission';

export interface StockRule {
  id?: string;
  uuid: string;
  stockItemUuid: string;
  name: string;
  description: string;
  locationUuid: string;
  locationName: string;
  quantity?: number | null;
  stockItemPackagingUOMUuid: string;
  packagingUomName: string;
  enabled: boolean;
  evaluationFrequency?: number | null;
  lastEvaluation: Date;
  nextEvaluation: Date;
  lastActionDate: Date;
  actionFrequency?: number | null;
  alertRole?: string | null;
  mailRole?: string | null;
  permission: RecordPermission;
  enableDescendants: boolean;
  creatorGivenName?: string;
  creatorFamilyName?: string;
  dateCreated?: Date;
  nextActionDate?: Date;
}
