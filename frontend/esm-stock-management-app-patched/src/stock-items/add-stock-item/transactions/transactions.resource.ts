import { ResourceRepresentation } from '../../../core/api/api';
import { useEffect, useMemo, useState } from 'react';
import {
  type StockItemInventoryFilter,
  useStockItemInventory,
  useStockItemTransactions,
} from '../../stock-items.resource';

export function useStockItemsTransactions(filter?: StockItemInventoryFilter) {
  const [stockItemFilter, setStockItemFilter] = useState<StockItemInventoryFilter>({
    startIndex: 0,
    v: filter?.v || ResourceRepresentation.Default,
    limit: 10,
    q: filter?.q,
    totalCount: true,
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
    });
  }, [searchString, currentPage, currentPageSize, stockItemUuid, partyUuid, locationUuid, stockBatchUuid]);

  const { items, isLoading, error } = useStockItemTransactions(stockItemFilter);
  const { items: inventory } = useStockItemInventory(stockItemFilter);

  const tableHeaders = useMemo(
    () => [
      {
        key: 'date',
        header: 'Date',
      },
      {
        key: 'location',
        header: 'Location',
      },
      {
        key: 'transaction',
        header: 'Transaction',
      },
      {
        key: 'in',
        header: 'IN',
      },
      {
        key: 'out',
        header: 'OUT',
      },
      {
        key: 'batch',
        header: 'Batch',
      },
      {
        key: 'reference',
        header: 'Reference',
      },
      {
        key: 'status',
        header: 'Status',
      },
    ],
    [],
  );

  const binCardHeaders = useMemo(
    () => [
      {
        key: 'date',
        header: 'Date',
      },
      {
        key: 'location',
        header: 'Location',
      },
      {
        key: 'transaction',
        header: 'Transaction',
      },
      {
        key: 'totalin',
        header: 'IN',
      },
      {
        key: 'totalout',
        header: 'OUT',
      },
      {
        key: 'batch',
        header: 'Batch',
      },
      {
        key: 'balance',
        header: 'Balance',
      },
      {
        key: 'reference',
        header: 'Reference',
      },
      {
        key: 'status',
        header: 'Status',
      },
    ],
    [],
  );

  return {
    items: items.results,
    totalCount: items.totalCount,
    currentPage,
    currentPageSize,
    setCurrentPage,
    setPageSize,
    pageSizes,
    isLoading,
    error,
    setSearchString,
    tableHeaders,
    setStockItemUuid,
    setLocationUuid,
    setPartyUuid,
    setStockBatchUuid,
    binCardHeaders,
    inventory: inventory,
  };
}
