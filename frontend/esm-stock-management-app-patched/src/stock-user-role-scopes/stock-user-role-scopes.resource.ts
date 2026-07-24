import useSWR from 'swr';
import { type ResourceFilterCriteria, toQueryParams } from '../core/api/api';
import { type PageableResult } from '../core/api/types/PageableResult';
import { type UserRoleScope } from '../core/api/types/identity/UserRoleScope';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';

export type UserRoleScopeFilter = ResourceFilterCriteria;

// getUserRoleScopes
export function useUserRoleScopes(filter: UserRoleScopeFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/userrolescope${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<{ data: PageableResult<UserRoleScope> }, Error>(apiUrl, openmrsFetch);
  return {
    items: data?.data || <PageableResult<UserRoleScope>>{},
    isLoading,
    error,
  };
}

// getUserRoleScope
export function useUserRoleScope(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/userrolescope/${id}`;
  const { data, error, isLoading } = useSWR<{ data: UserRoleScope }, Error>(apiUrl, openmrsFetch);
  return {
    items: data.data ? data.data : {},
    isLoading,
    error,
  };
}

// deleteUserRoleScopes
export function deleteUserRoleScopes(ids: string[]) {
  let otherIds = ids.reduce((p, c, i) => {
    if (i === 0) return p;
    p += (p.length > 0 ? ',' : '') + encodeURIComponent(c);
    return p;
  }, '');
  if (otherIds.length > 0) {
    otherIds = '?ids=' + otherIds;
  }
  const apiUrl = `${restBaseUrl}/stockmanagement/userrolescope/${ids[0]}${otherIds}`;
  const abortController = new AbortController();

  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// createOrUpdateUserRoleScope
export function createOrUpdateUserRoleScope(item: UserRoleScope) {
  const abortController = new AbortController();
  const hasUuid = item.uuid != null;
  const apiUrl = `${restBaseUrl}/stockmanagement/userrolescope${hasUuid ? '/' + item.uuid : ''}`;
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}
