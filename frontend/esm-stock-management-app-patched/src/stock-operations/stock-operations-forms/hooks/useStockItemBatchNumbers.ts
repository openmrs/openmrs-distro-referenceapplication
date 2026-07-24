import { useEffect, useState } from 'react';
import { ResourceRepresentation } from '../../../core/api/api';
import { type StockBatchFilter, useStockBatches } from '../../../stock-items/stock-items.resource';

export function useStockItemBatchNumbers(stockItemUuid: string) {
  const [conceptFilter, setConceptFilter] = useState<StockBatchFilter>({
    v: ResourceRepresentation.Default,
    limit: 10,
    startIndex: 0,
    stockItemUuid,
  });

  const {
    items: { results: stockItemBatchNos },
    isLoading,
  } = useStockBatches(conceptFilter);

  const [searchString, setSearchString] = useState(null);

  // Drug filter type
  const [limit, setLimit] = useState(10);
  const [representation, setRepresentation] = useState(ResourceRepresentation.Default);

  useEffect(() => {
    setConceptFilter({
      startIndex: 0,
      v: representation,
      limit: limit,
      q: searchString,
      stockItemUuid,
    });
  }, [searchString, limit, representation, stockItemUuid]);

  return {
    stockItemBatchNos,
    setLimit,
    setRepresentation,
    setSearchString,
    isLoading,
  };
}
