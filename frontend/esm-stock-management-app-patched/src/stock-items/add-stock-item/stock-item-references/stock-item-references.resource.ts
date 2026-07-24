import { ResourceRepresentation } from '../../../core/api/api';
import { type StockItemReferenceFilter, useStockItemReferences } from '../../stock-items.resource';
import { useEffect, useState } from 'react';

export function useStockItemReferencesHook(v?: ResourceRepresentation) {
  const [stockItemReferenceFilter, setStockItemReferenceFilter] = useState<StockItemReferenceFilter>({
    startIndex: 0,
    v: v || ResourceRepresentation.Default,
    q: null,
    totalCount: true,
  });

  const [stockItemUuid, setStockItemUuid] = useState<string | null>();

  useEffect(() => {
    setStockItemReferenceFilter({
      startIndex: 0,
      v: ResourceRepresentation.Default,
      totalCount: true,
      stockItemUuid: stockItemUuid,
    });
  }, [stockItemUuid]);

  const { items, isLoading, error } = useStockItemReferences(stockItemReferenceFilter);

  return {
    items: items.results,
    totalCount: items.totalCount,
    isLoading,
    error,
    setStockItemUuid,
  };
}
