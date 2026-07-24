import { type FetchResponse, openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import useSWR from 'swr';
import { type ResourceFilterCriteria, toQueryParams } from '../core/api/api';
import { type PageableResult } from '../core/api/types/PageableResult';
import { type InventoryGroupBy } from '../core/api/types/stockItem/StockItem';
import { type StopOperationAction } from '../core/api/types/stockOperation/StockOperationAction';
import { type StockOperationDTO } from '../core/api/types/stockOperation/StockOperationDTO';
import { type StockOperationItemDtoSchema } from './validation-schema';
import { type StockOperationItemCost } from '../core/api/types/stockOperation/StockOperationItemCost';
import { type StockItemInventory } from '../core/api/types/stockItem/StockItemInventory';

export interface StockOperationFilter extends ResourceFilterCriteria {
  status?: string | null | undefined;
  operationTypeUuid?: string | null | undefined;
  locationUuid?: string | null | undefined;
  isLocationOther?: boolean | null | undefined;
  stockItemUuid?: string | null | undefined;
  operationDateMin?: string | null | undefined;
  operationDateMax?: string | null | undefined;
  sourceTypeUuid?: string | null | undefined;
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
}

// getStockOperations
export function useStockOperations(filter: StockOperationFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperation${toQueryParams(filter)}`;
  const { data, error, isLoading } = useSWR<{ data: PageableResult<StockOperationDTO> }, Error>(apiUrl, openmrsFetch);

  return {
    items: data?.data || <PageableResult<StockOperationDTO>>{},
    isLoading,
    error,
  };
}

// getStockOperationLinks
export function getStockOperationLinks(filter: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationlink?v=default&q=${filter}`;
  const abortController = new AbortController();

  return openmrsFetch(apiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// getStockOperation

export function useStockOperation(id: string | null) {
  const apiUrl = id ? `${restBaseUrl}/stockmanagement/stockoperation/${id}` : null;
  const { data, error, isLoading } = useSWR<{ data: StockOperationDTO }, Error>(apiUrl, apiUrl ? openmrsFetch : null);
  return {
    items: data?.data,
    isLoading,
    error,
  };
}

// getStockOperation
export function getStockOperation(id: string): Promise<FetchResponse<StockOperationDTO>> {
  if (!id) {
    return;
  }
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperation/${id}?v=full`;
  return openmrsFetch(apiUrl);
}

// getStockOperationAndItems
export function useStockOperationAndItems(id = '') {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperation/${id}?v=full`;
  const { data, error, isLoading } = useSWR<{ data: StockOperationDTO }, Error>(id ? apiUrl : null, openmrsFetch);
  return {
    items: data?.data,
    isLoading,
    error,
  };
}

// deleteStockOperations
export function deleteStockOperations(ids: string[]) {
  let otherIds = ids.reduce((p, c, i) => {
    if (i === 0) return p;
    p += (p.length > 0 ? ',' : '') + encodeURIComponent(c);
    return p;
  }, '');
  if (otherIds.length > 0) {
    otherIds = '?ids=' + otherIds;
  }
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperation/${ids[0]}${otherIds}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// deleteStockOperationItem
export function deleteStockOperationItem(id: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationitem/${id}`;
  const abortController = new AbortController();
  return openmrsFetch(apiUrl, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

// createStockOperation
export function createStockOperation(data: StockOperationItemDtoSchema) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperation`;
  const abortController = new AbortController();
  return openmrsFetch<StockOperationDTO>(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: data,
  });
}

// updateStockOperation
export function updateStockOperation(stockOperation: StockOperationDTO, data: StockOperationItemDtoSchema) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperation/${stockOperation.uuid}`;
  const abortController = new AbortController();
  return openmrsFetch<StockOperationDTO>(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: data,
  });
}

// executeStockOperationAction
export function executeStockOperationAction(item: StopOperationAction) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationaction`;
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

// updateStockOperationBatchNumbers
export function updateStockOperationBatchNumbers(item: StockOperationDTO, uuid: string) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationbatchnumbers/${uuid}`;
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

// get stock operation itemcosts
export function getStockOperationItemsCost(filter: StockOperationFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationitemcost?v=default&stockOperationUuid=${filter}`;
  const abortController = new AbortController();
  return openmrsFetch<{ results: Array<StockOperationItemCost> }>(apiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}
// get stockiteminvoentory
export function getStockItemInventory(filter: StockItemInventoryFilter) {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockiteminventory${toQueryParams(filter)}&v=default`;
  const abortController = new AbortController();
  return openmrsFetch<{ results: Array<StockItemInventory> }>(apiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

export const operationStatusColor = (status: string) => {
  switch (status) {
    case 'NEW':
      return '#0f62fe';
    case 'SUBMITTED':
      return '#4589ff';
    case 'DISPATCHED':
      return '#8a3ffc';
    case 'COMPLETED':
      return '#24a148';
    case 'CANCELLED':
      return '#da1e28';
    case 'RETURNED':
      return '#eb6200';
    default:
      break;
  }
};
