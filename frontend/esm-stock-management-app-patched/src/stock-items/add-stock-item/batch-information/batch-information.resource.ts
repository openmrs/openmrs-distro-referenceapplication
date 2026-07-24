import { ResourceRepresentation } from '../../../core/api/api';
import { useEffect, useMemo, useState } from 'react';
import { type StockItemInventoryFilter, useStockItemInventory } from '../../stock-items.resource';

export function useStockItemBatchInformationHook(filter?: StockItemInventoryFilter) {
  const [stockItemFilter, setStockItemFilter] = useState<StockItemInventoryFilter>({
    startIndex: 0,
    v: filter?.v || ResourceRepresentation.Default,
    limit: 10,
    q: filter?.q,
    totalCount: true,
    includeBatchNo: true,
  });

  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPageSize, setPageSize] = useState(10);
  const [searchString, setSearchString] = useState(null);

  const [currentPage, setCurrentPage] = useState(1);

  const [stockItemUuid, setStockItemUuid] = useState<string | null>(filter?.stockItemUuid);
  const [partyUuid, setPartyUuid] = useState<string | null>(filter?.partyUuid);
  const [locationUuid, setLocationUuid] = useState<string | null>(filter?.locationUuid);
  const [stockBatchUuid, setStockBatchUuid] = useState<string | null>(filter?.stockBatchUuid);

  useEffect(() => {
    setStockItemFilter({
      startIndex: currentPage - 1,
      v: ResourceRepresentation.Default,
      limit: currentPageSize,
      q: searchString,
      totalCount: true,
      stockItemUuid: stockItemUuid,
      partyUuid: partyUuid,
      locationUuid: locationUuid,
      stockBatchUuid: stockBatchUuid,
      includeBatchNo: true,
    });
  }, [searchString, currentPage, currentPageSize, stockItemUuid, partyUuid, locationUuid, stockBatchUuid]);

  const { items, isLoading, error } = useStockItemInventory(stockItemFilter);

  return {
    items: items.results ?? [],
    totalCount: items.totalCount,
    currentPage,
    currentPageSize,
    setCurrentPage,
    setPageSize,
    pageSizes,
    isLoading,
    error,
    setSearchString,
    setStockItemUuid,
    setLocationUuid,
    setPartyUuid,
    setStockBatchUuid,
  };
}
