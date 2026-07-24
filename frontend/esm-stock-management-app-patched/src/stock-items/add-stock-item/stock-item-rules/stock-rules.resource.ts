import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { type ResourceFilterCriteria, toQueryParams } from '../../../core/api/api';
import useSWR from 'swr';
import { type PageableResult } from '../../../core/api/types/PageableResult';
import { type StockRule } from '../../../core/api/types/stockItem/StockRule';

export interface StockSourceFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null;
}

// Get Stock Rules
export function useStockRules(filter: StockSourceFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule${toQueryParams(filter)}`;

  const { data, error, isLoading } = useSWR<{ data: PageableResult<StockRule> }, Error>(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockRule>>{},
    isLoading,
    error,
  };
}

// Get Stock Rule
export function useStockRule(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule/${id}`;
  const { data, error, isLoading } = useSWR<{ data: StockRule }, Error>(apiUrl, openmrsFetch);
  return {
    items: data.data ? data.data : [],
    isLoading,
    error,
  };
}

// Delete Stock Rule
export function deleteStockRule(ids: string[]) {
  let otherIds = ids.reduce((p, c, i) => {
    if (i === 0) return p;
    p += (p.length > 0 ? ',' : '') + encodeURIComponent(c);
    return p;
  }, '');
  if (otherIds.length > 0) {
    otherIds = '?ids=' + otherIds;
  }
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule/${ids[0]}${otherIds}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// Create Or Update Stock Rule
export function createOrUpdateStockRule(item: StockRule) {
  const isNew = item.uuid != null;

  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule${isNew ? '/' + item.uuid : ''}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    // body: { ...item, sourceType: item?.sourceType?.uuid },
    body: { ...item },
  });
}
