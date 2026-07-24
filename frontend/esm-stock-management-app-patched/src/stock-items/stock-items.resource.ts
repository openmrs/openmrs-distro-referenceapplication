import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import useSWR from 'swr';
import { type ResourceFilterCriteria, toQueryParams } from '../core/api/api';
import { type PageableResult } from '../core/api/types/PageableResult';
import { type StockBatchDTO } from '../core/api/types/stockItem/StockBatchDTO';
import { type InventoryGroupBy, type StockItemDTO } from '../core/api/types/stockItem/StockItem';
import { type StockItemInventory } from '../core/api/types/stockItem/StockItemInventory';
import { type StockItemPackagingUOMDTO } from '../core/api/types/stockItem/StockItemPackagingUOM';
import { type StockItemReference, type StockItemReferenceDTO } from '../core/api/types/stockItem/StockItemReference';
import { type StockItemTransactionDTO } from '../core/api/types/stockItem/StockItemTransaction';
import { type StockRule } from '../core/api/types/stockItem/StockRule';
import { type StockOperationItemCost } from '../core/api/types/stockOperation/StockOperationItemCost';
import { type StockItemFormData } from './validationSchema';

export interface StockItemFilter extends ResourceFilterCriteria {
  isDrug?: string | null | undefined;
  drugUuid?: string | null;
  conceptUuid?: string | null;
}

export interface StockItemTransactionFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null;
  partyUuid?: string | null;
  stockOperationUuid?: string | null;
  includeBatchNo?: boolean | null;
  dateMin?: string | null;
  dateMax?: string | null;
  stockBatchUuid?: string | null | undefined;
}

export interface StockItemInventoryFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null;
  partyUuid?: string | null;
  locationUuid?: string | null;
  includeBatchNo?: boolean | null;
  stockBatchUuid?: string | null;
  groupBy?: InventoryGroupBy | null;
  totalBy?: InventoryGroupBy | null;
  stockOperationUuid?: string | null;
  date?: string | null;
  includeStockItemName?: 'true' | 'false' | '0' | '1';
  excludeExpired?: boolean | null;
  isPatientTransaction?: 'true' | 'false';
}

export interface StockItemPackagingUOMFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null | undefined;
}

export interface StockItemReferenceFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null | undefined;
}

export interface StockBatchFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null | undefined;
  excludeExpired?: boolean | null;
  includeStockItemName?: 'true' | 'false' | '0' | '1';
}

export interface StockInventoryResult extends PageableResult<StockItemInventory> {
  total: StockItemInventory[];
}

export interface StockRuleFilter extends ResourceFilterCriteria {
  stockItemUuid?: string | null;
  locationUuid?: string | null;
}

// getStockItems
export function useStockItems(filter: StockItemFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitem${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<
    {
      data: PageableResult<StockItemDTO>;
    },
    Error
  >(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockItemDTO>>{},
    isLoading,
    error,
  };
}

// fetch filtered stock item
export function fetchStockItem(drugUuid: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitem?drugUuid=${drugUuid}&limit=1`;
  return openmrsFetch(apiUrl).then(({ data }) => data);
}

// getStockItemTransactions
export function useStockItemTransactions(filter: StockItemTransactionFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitemtransaction${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<
    {
      data: PageableResult<StockItemTransactionDTO>;
    },
    Error
  >(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockItemTransactionDTO>>{},
    isLoading,
    error,
  };
}

// getStockItemInventory
export function useStockItemInventory(filter: StockItemInventoryFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockiteminventory${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<
    {
      data: StockInventoryResult;
    },
    Error
  >(apiUrl, openmrsFetch);

  return {
    items: data?.data || <StockInventoryResult>{},
    isLoading,
    error,
  };
}

// getStockOperationItemsCost
export function useStockOperationItemsCost(filter: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationitemcost?v=default&stockOperationUuid=${filter}`;
  const { data, error, isLoading } = useSWR<
    {
      data: PageableResult<StockOperationItemCost>;
    },
    Error
  >(apiUrl, openmrsFetch);
  return {
    items: data.data ? data.data : [],
    isLoading,
    error,
  };
}

// getStockBatches
export function useStockBatches(filter: StockBatchFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockbatch${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<
    {
      data: PageableResult<StockBatchDTO>;
    },
    Error
  >(apiUrl, openmrsFetch);
  return {
    items: data?.data || <PageableResult<StockBatchDTO>>{},
    isLoading,
    error,
  };
}

// getStockItemPackagingUOMs
export function useStockItemPackagingUOMs(filter: StockItemPackagingUOMFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitempackaginguom${toQueryParams(filter)}`;
  const { data, error, isLoading, mutate } = useSWR<
    {
      data: PageableResult<StockItemPackagingUOMDTO>;
    },
    Error
  >(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockItemPackagingUOMDTO>>{},
    isLoading,
    error,
    mutate,
  };
}

// getStockItem
export function useStockItem(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitem/${id}?v=full`;
  const { data, error, isLoading } = useSWR<
    {
      data: StockItemDTO;
    },
    Error
  >(apiUrl, openmrsFetch);
  return {
    item: data?.data || <StockItemDTO>{},
    isLoading,
    error,
  };
}

// deleteStockItems
export function deleteStockItems(ids: string[]) {
  let otherIds = ids.reduce((p, c, i) => {
    if (i === 0) return p;
    p += (p.length > 0 ? ',' : '') + encodeURIComponent(c);
    return p;
  }, '');
  if (otherIds.length > 0) {
    otherIds = '?ids=' + otherIds;
  }

  const apiUrl = `${restBaseUrl}/stockmanagement/stockitem/${ids[0]}${otherIds}`;

  const abortController = new AbortController();

  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// deleteStockItemPackagingUnit
export function deleteStockItemPackagingUnit(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitempackaginguom/${id}`;
  const abortController = new AbortController();

  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// createStockItem
export function createStockItem(item: StockItemFormData) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitem`;
  const abortController = new AbortController();
  delete item.isDrug;
  return openmrsFetch<StockItemDTO>(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// updateStockItem
export function updateStockItem(stockItemUuid: string, item: StockItemFormData) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitem/${stockItemUuid}`;
  const abortController = new AbortController();
  delete item.isDrug;
  delete item.dateCreated;
  return openmrsFetch<StockItemDTO>(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// createStockItemPackagingUnit
export function createStockItemPackagingUnit(item: StockItemPackagingUOMDTO) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitempackaginguom`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// updateStockItemPackagingUnit
export function updateStockItemPackagingUnit(item: StockItemPackagingUOMDTO, uuid: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitempackaginguom/${uuid}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// importStockItem
export function importStockItem(item: FormData) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitemimport`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// stock rules
// getStockRules
export function useStockRules(filter: StockRuleFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<
    {
      data: PageableResult<StockRule>;
    },
    Error
  >(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockRule>>{},
    isLoading,
    error,
  };
}

// createStockRule
export function createStockRule(item: StockRule) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// updateStockRule
export function updateStockRule(item: StockRule, uuid: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule/${uuid}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// deleteStockRule
export function deleteStockRule(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockrule/${id}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// stock references
// getStockItemReferences
export function useStockItemReferences(filter: StockItemReferenceFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitemreference${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<
    {
      data: PageableResult<StockItemReferenceDTO>;
    },
    Error
  >(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockItemReferenceDTO>>{},
    isLoading,
    error,
  };
}

// create stockItemReference
export function createStockItemReference(item: StockItemReferenceDTO) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitemreference`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}

// updateStockRule
export function updateStockItemReference(item: StockItemReference, uuid: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitemreference/${uuid}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: item,
  });
}
// deleteStockItemReferemce
export function deleteStockItemReference(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockitemreference/${id}`;
  const abortController = new AbortController();

  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}
