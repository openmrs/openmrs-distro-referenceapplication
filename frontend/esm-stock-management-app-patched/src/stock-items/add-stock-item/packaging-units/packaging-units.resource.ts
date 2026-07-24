import { ResourceRepresentation } from '../../../core/api/api';
import { useEffect, useState } from 'react';
import { type StockItemInventoryFilter, useStockItemPackagingUOMs } from '../../stock-items.resource';

export function useStockItemPackageUnitsHook(v?: ResourceRepresentation) {
  const [stockItemFilter, setStockItemFilter] = useState<StockItemInventoryFilter>({
    startIndex: 0,
    v: v || ResourceRepresentation.Default,
    q: null,
    totalCount: true,
  });

  const [stockItemUuid, setStockItemUuid] = useState<string | null>();

  useEffect(() => {
    setStockItemFilter({
      startIndex: 0,
      v: ResourceRepresentation.Default,
      totalCount: true,
      stockItemUuid: stockItemUuid,
    });
  }, [stockItemUuid]);

  const { items, isLoading, error, mutate } = useStockItemPackagingUOMs(stockItemFilter);

  return {
    items: items.results ?? [],
    totalCount: items.totalCount,
    isLoading,
    error,
    setStockItemUuid,
    mutate,
  };
}
