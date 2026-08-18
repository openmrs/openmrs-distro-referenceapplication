import useSWR from 'swr';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { ResourceRepresentation, toQueryParams } from '../core/api/api';
import { StockItemOnly } from '../core/api/types/stockItem/StockItem';
import { type StockItemInventory } from '../core/api/types/stockItem/StockItemInventory';
import { type StockItemInventoryFilter } from './stock-items.resource';

// The inventory quantity is recorded in whatever packaging unit (e.g. Box) the stock
// operations used, not in the item's dispensing unit (e.g. Strip) - quantityUoM/quantityFactor
// carry the packaging unit's name and its conversion factor to the dispensing unit, so callers
// that need to *display* the quantity can also show the dispensing-unit equivalent.
export interface StockItemQuantityMeta {
  quantityUoM: string;
  quantityFactor: number;
}

export async function fetchQuantitiesByItem(stockItemUuids: Array<string>, locationUuid: string) {
  const quantityByItem = new Map<string, number>();
  const quantityMetaByItem = new Map<string, StockItemQuantityMeta>();
  await Promise.all(
    stockItemUuids.map(async (stockItemUuid) => {
      const url = `${restBaseUrl}/stockmanagement/stockiteminventory${toQueryParams<StockItemInventoryFilter>({
        v: ResourceRepresentation.Default,
        stockItemUuid,
        locationUuid,
        groupBy: StockItemOnly,
      })}`;
      const { data } = await openmrsFetch<{ results: Array<StockItemInventory> }>(url);
      const result = data.results?.[0];
      const quantity = result?.quantity ?? 0;
      quantityByItem.set(stockItemUuid, quantity);
      if (result?.quantityUoM) {
        quantityMetaByItem.set(stockItemUuid, {
          quantityUoM: result.quantityUoM,
          quantityFactor: Number(result.quantityFactor) || 1,
        });
      }
    }),
  );
  return { quantityByItem, quantityMetaByItem };
}

export function useStockItemQuantities(stockItemUuids: Array<string>, locationUuid: string | undefined) {
  // Keyed as a string starting with the stockiteminventory REST path (rather than an
  // array tuple) so that handleMutate's prefix-based cache invalidation - already
  // called after adding/editing items and after operations complete - actually
  // reaches this cache. An array key here would silently never be invalidated,
  // since handleMutate only matches string keys.
  const key =
    locationUuid && stockItemUuids.length > 0
      ? `${restBaseUrl}/stockmanagement/stockiteminventory?stockItemQuantities=${locationUuid}:${stockItemUuids.join(',')}`
      : null;
  const { data, error, isLoading } = useSWR(key, () => fetchQuantitiesByItem(stockItemUuids, locationUuid as string));
  return {
    quantityByItem: data?.quantityByItem ?? new Map<string, number>(),
    quantityMetaByItem: data?.quantityMetaByItem ?? new Map<string, StockItemQuantityMeta>(),
    isLoading,
    error,
  };
}
