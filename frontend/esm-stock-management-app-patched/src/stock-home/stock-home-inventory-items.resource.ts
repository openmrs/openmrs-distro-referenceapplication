import { useEffect, useState } from 'react';
import { type StockItemFilter, useStockItems } from '../stock-items/stock-items.resource';
import { ResourceRepresentation } from '../core/api/api';

export function useStockInventoryItems(v?: ResourceRepresentation) {
  const [stockItemFilter, setStockItemFilter] = useState<StockItemFilter>({
    v: v || ResourceRepresentation.Default,
    q: null,
    totalCount: true,
  });

  const { items, isLoading, error } = useStockItems(stockItemFilter);

  useEffect(() => {
    setStockItemFilter({
      v: ResourceRepresentation.Default,
      totalCount: true,
    });
  }, []);

  return {
    items: items?.results ?? [],
    isLoading,
    error,
  };
}
