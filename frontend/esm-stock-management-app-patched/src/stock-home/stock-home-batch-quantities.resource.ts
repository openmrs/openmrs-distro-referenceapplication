import useSWR from 'swr';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { ResourceRepresentation, toQueryParams } from '../core/api/api';
import { LocationStockItemBatchNo } from '../core/api/types/stockItem/StockItem';
import { type StockItemInventory } from '../core/api/types/stockItem/StockItemInventory';
import { type StockItemInventoryFilter } from '../stock-items/stock-items.resource';

export interface BatchQuantity {
  quantity: number;
  quantityUoM?: string;
}

async function fetchBatchQuantities(stockItemUuids: Array<string>) {
  const quantityByBatch = new Map<string, BatchQuantity>();
  await Promise.all(
    stockItemUuids.map(async (stockItemUuid) => {
      const url = `${restBaseUrl}/stockmanagement/stockiteminventory${toQueryParams<StockItemInventoryFilter>({
        v: ResourceRepresentation.Default,
        stockItemUuid,
        groupBy: LocationStockItemBatchNo,
        totalCount: true,
      })}`;
      const { data } = await openmrsFetch<{ results: Array<StockItemInventory> }>(url);
      (data.results ?? []).forEach((row) => {
        if (!row.stockBatchUuid) {
          return;
        }
        const existing = quantityByBatch.get(row.stockBatchUuid);
        quantityByBatch.set(row.stockBatchUuid, {
          quantity: (existing?.quantity ?? 0) + row.quantity,
          quantityUoM: row.quantityUoM,
        });
      });
    }),
  );
  return quantityByBatch;
}

export function useStockBatchQuantities(stockItemUuids: Array<string>) {
  // String key starting with the stockiteminventory REST path, not an array tuple, so
  // handleMutate's prefix-based invalidation (called after operations complete) reaches
  // this cache - see the matching comment in stock-item-quantities.resource.ts.
  const key =
    stockItemUuids.length > 0
      ? `${restBaseUrl}/stockmanagement/stockiteminventory?batchQuantities=${stockItemUuids.join(',')}`
      : null;
  const { data, error, isLoading } = useSWR(key, () => fetchBatchQuantities(stockItemUuids));
  return { quantityByBatch: data ?? new Map<string, BatchQuantity>(), isLoading, error };
}
