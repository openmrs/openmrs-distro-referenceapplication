import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { type ResourceFilterCriteria, toQueryParams } from '../core/api/api';
import useSWR from 'swr';
import { type PageableResult } from '../core/api/types/PageableResult';
import { type StockSource } from '../core/api/types/stockOperation/StockSource';

export interface StockSourceFilter extends ResourceFilterCriteria {
  sourceTypeUuid?: string | null;
}

// getStockSources
export function useStockSources(filter: StockSourceFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stocksource${toQueryParams(filter)}`;

  const { data, error, isLoading } = useSWR<{ data: PageableResult<StockSource> }, Error>(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockSource>>{},
    isLoading,
    error,
  };
}

// getStockSource
export function useStockSource(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stocksource/${id}`;
  const { data, error, isLoading } = useSWR<{ data: StockSource }, Error>(apiUrl, openmrsFetch);
  return {
    items: data.data ? data.data : [],
    isLoading,
    error,
  };
}

// deleteStockSources
export function deleteStockSource(ids: string[]) {
  let otherIds = ids.reduce((p, c, i) => {
    if (i === 0) return p;
    p += (p.length > 0 ? ',' : '') + encodeURIComponent(c);
    return p;
  }, '');
  if (otherIds.length > 0) {
    otherIds = '?ids=' + otherIds;
  }
  const apiUrl = `${restBaseUrl}/stockmanagement/stocksource/${ids[0]}${otherIds}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// createOrUpdateStockSource
export function createOrUpdateStockSource(item: StockSource) {
  const isNew = item.uuid != null;

  const apiUrl = `${restBaseUrl}/stockmanagement/stocksource${isNew ? '/' + item.uuid : ''}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: { ...item, sourceType: item?.sourceType?.uuid },
  });
}
