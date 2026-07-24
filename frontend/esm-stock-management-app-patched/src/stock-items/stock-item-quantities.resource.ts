import useSWR from 'swr';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { ResourceRepresentation, toQueryParams } from '../core/api/api';
import { StockItemOnly } from '../core/api/types/stockItem/StockItem';
import { type StockItemInventory } from '../core/api/types/stockItem/StockItemInventory';
import { type StockItemInventoryFilter } from './stock-items.resource';

export async function fetchQuantitiesByItem(stockItemUuids: Array<string>, locationUuid: string) {
  const quantityByItem = new Map<string, number>();
  await Promise.all(
    stockItemUuids.map(async (stockItemUuid) => {
      const url = `${restBaseUrl}/stockmanagement/stockiteminventory${toQueryParams<StockItemInventoryFilter>({
        v: ResourceRepresentation.Default,
        stockItemUuid,
        locationUuid,
        groupBy: StockItemOnly,
      })}`;
      const { data } = await openmrsFetch<{ results: Array<StockItemInventory> }>(url);
      const quantity = data.results?.[0]?.quantity ?? 0;
      quantityByItem.set(stockItemUuid, quantity);
    }),
  );
  return quantityByItem;
}

export function useStockItemQuantities(stockItemUuids: Array<string>, locationUuid: string | undefined) {
  const key =
    locationUuid && stockItemUuids.length > 0
      ? (['stock-item-quantities', locationUuid, stockItemUuids.join(',')] as const)
      : null;
  const { data, error, isLoading } = useSWR(key, ([, itemLocationUuid, itemUuidsKey]) =>
    fetchQuantitiesByItem(itemUuidsKey.split(','), itemLocationUuid),
  );
  return { quantityByItem: data ?? new Map<string, number>(), isLoading, error };
}
