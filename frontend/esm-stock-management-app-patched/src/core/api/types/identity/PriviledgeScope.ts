export interface PrivilegeScope {
  privilege: string;
  locationUuid: string | null;
  operationTypeUuid: string | null;
  partyUuid: string | null;
  isPermanent: boolean;
  activeFrom: Date | null;
  activeTo: Date | null;
}
