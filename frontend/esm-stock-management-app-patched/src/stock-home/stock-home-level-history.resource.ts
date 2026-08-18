import useSWR from 'swr';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { ResourceRepresentation, toQueryParams } from '../core/api/api';
import { StockItemOnly } from '../core/api/types/stockItem/StockItem';
import { type StockItemInventory } from '../core/api/types/stockItem/StockItemInventory';
import { type StockItemInventoryFilter } from '../stock-items/stock-items.resource';

interface StockItemForHistory {
  uuid: string | null | undefined;
  reorderLevel?: number | null;
}

interface SnapshotPoint {
  daysAgo: number;
  outOfStockCount: number;
  understockedCount: number;
}

const SNAPSHOT_DAYS_AGO = [28, 21, 14, 7, 0];

function isoDaysAgo(daysAgo: number) {
  const date = new Date();
  date.setDate(date.getDate() - daysAgo);
  return date.toISOString().split('T')[0];
}

async function fetchSnapshot(
  locationUuid: string,
  daysAgo: number,
  stockItems: Array<StockItemForHistory>,
): Promise<SnapshotPoint> {
  const filter: StockItemInventoryFilter = {
    v: ResourceRepresentation.Default,
    locationUuid,
    groupBy: StockItemOnly,
  };
  if (daysAgo > 0) {
    filter.date = isoDaysAgo(daysAgo);
  }

  const url = `${restBaseUrl}/stockmanagement/stockiteminventory${toQueryParams(filter)}`;
  const { data } = await openmrsFetch<{ results: Array<StockItemInventory> }>(url);

  const quantityByItem = new Map<string, number>();
  (data.results ?? []).forEach((row) => quantityByItem.set(row.stockItemUuid, row.quantity));

  let outOfStockCount = 0;
  let understockedCount = 0;
  stockItems.forEach((item) => {
    if (!item.uuid) {
      return;
    }
    const quantity = quantityByItem.get(item.uuid) ?? 0;
    if (quantity <= 0) {
      outOfStockCount++;
    } else if (item.reorderLevel && quantity < item.reorderLevel) {
      understockedCount++;
    }
  });

  return { daysAgo, outOfStockCount, understockedCount };
}

/**
 * Derives weekly out-of-stock/understocked snapshots for the last 4 weeks from the
 * stockiteminventory as-of-date filter, so the overview can show a trend arrow and
 * sparkline instead of a bare count. Any snapshot that fails to load is dropped
 * rather than plotted as zero, since the trend/sparkline degrade gracefully when
 * fewer points are available.
 */
export function useStockLevelHistory(locationUuid: string | undefined, stockItems: Array<StockItemForHistory>) {
  // String key starting with the stockiteminventory REST path, not an array tuple, so
  // handleMutate's prefix-based invalidation reaches this cache too - see the matching
  // comment in stock-item-quantities.resource.ts.
  const key =
    locationUuid && stockItems.length > 0
      ? `${restBaseUrl}/stockmanagement/stockiteminventory?levelHistory=${locationUuid}:${stockItems.length}`
      : null;

  const { data, isLoading } = useSWR(key, async () => {
    const settled = await Promise.allSettled(
      SNAPSHOT_DAYS_AGO.map((daysAgo) => fetchSnapshot(locationUuid as string, daysAgo, stockItems)),
    );
    return settled
      .filter((result): result is PromiseFulfilledResult<SnapshotPoint> => result.status === 'fulfilled')
      .map((result) => result.value)
      .sort((a, b) => b.daysAgo - a.daysAgo);
  });

  const points = data ?? [];
  const latest = points[points.length - 1];
  const weekAgo = points.find((point) => point.daysAgo === 7);

  return {
    isLoading,
    outOfStockTrend: latest && weekAgo ? latest.outOfStockCount - weekAgo.outOfStockCount : null,
    outOfStockSparkline: points.map((point) => point.outOfStockCount),
  };
}
