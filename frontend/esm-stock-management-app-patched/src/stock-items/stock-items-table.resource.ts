import { useEffect, useState } from 'react';
import { type StockItemFilter, useStockItems } from './stock-items.resource';
import { ResourceRepresentation } from '../core/api/api';

// Upper bound on how many items we'll fetch client-side so column filters and
// pagination can operate over the whole matching set instead of just one server
// page. Kept at the OpenMRS REST module's common default max-results-absolute (100)
// - asking for more than the server's configured absolute max causes the request to
// fail outright, which previously showed up as a silent "no items" empty table. A
// catalog larger than this would need the filters pushed down to the backend query,
// or the backend's webservices.rest.maxResultsAbsolute setting raised, instead.
const MAX_FETCH_SIZE = 100;

export function useStockItemsPages(v?: ResourceRepresentation) {
  const [searchString, setSearchString] = useState(null);

  // Drug filter type
  const [isDrug, setDrug] = useState('');

  const [stockItemFilter, setStockItemFilter] = useState<StockItemFilter>({
    startIndex: 0,
    v: v || ResourceRepresentation.Default,
    limit: MAX_FETCH_SIZE,
    q: null,
    totalCount: true,
  });

  const { items, isLoading, error } = useStockItems(stockItemFilter);

  useEffect(() => {
    setStockItemFilter({
      startIndex: 0,
      v: ResourceRepresentation.Default,
      limit: MAX_FETCH_SIZE,
      q: searchString,
      totalCount: true,
      isDrug: isDrug,
    });
  }, [searchString, isDrug]);

  return {
    items: items.results,
    isLoading,
    error,
    isDrug,
    setDrug,
    setSearchString,
  };
}
