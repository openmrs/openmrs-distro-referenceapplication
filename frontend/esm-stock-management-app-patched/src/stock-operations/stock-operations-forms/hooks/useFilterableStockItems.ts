import { useEffect, useState } from 'react';
import { type StockItemFilter, useStockItems as useStockItemsData } from '../../../stock-items/stock-items.resource';
import { type UserFilterCriteria } from '../../../stock-lookups/stock-lookups.resource';
import { ResourceRepresentation } from '../../../core/api/api';

export function useFilterableStockItems(filter?: StockItemFilter) {
  const [conceptFilter, setConceptFilter] = useState<UserFilterCriteria>(
    filter || {
      v: ResourceRepresentation.Default,
      limit: 10,
      startIndex: 0,
    },
  );

  const {
    items: { results: stockItemsList },
    isLoading,
  } = useStockItemsData(conceptFilter);

  const [searchString, setSearchString] = useState(null);

  // Drug filter type
  const [limit, setLimit] = useState(filter?.limit || 10);
  const [representation, setRepresentation] = useState(filter?.v || ResourceRepresentation.Default);

  useEffect(() => {
    setConceptFilter({
      startIndex: 0,
      v: representation,
      limit: limit,
      q: searchString,
    });
  }, [searchString, limit, representation]);

  return {
    stockItemsList,
    setLimit,
    setRepresentation,
    setSearchString,
    isLoading,
  };
}
