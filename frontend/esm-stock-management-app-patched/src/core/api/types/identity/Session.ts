import { type SessionLocation } from '@openmrs/esm-framework';
import { type PrivilegeScope } from './PriviledgeScope';
import { type User } from './User';

export interface GetSessionResponse {
  sessionId: string;
  authenticated: boolean;
  user: User;
  locale: string;
  allowedLocales: string[];
  sessionLocation?: SessionLocation;
}

export interface StockManagementSession {
  privileges: PrivilegeScope[];
}
