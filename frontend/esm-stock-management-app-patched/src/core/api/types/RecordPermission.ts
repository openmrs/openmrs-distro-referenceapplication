export interface RecordPermission {
  canView: boolean;
  canEdit: boolean;
  canApprove: boolean | undefined | null;
  canReceiveItems: boolean | undefined | null;
  canDisplayReceivedItems: boolean | undefined | null;
  isRequisitionAndCanIssueStock: boolean | undefined | null;
  canUpdateBatchInformation: boolean | undefined | null;
}
