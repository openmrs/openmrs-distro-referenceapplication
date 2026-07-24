import { type StockOperationFilter, useStockOperations } from '../stock-operations/stock-operations.resource';

export function useDisposalList(filter: StockOperationFilter) {
  const { items, isLoading, error } = useStockOperations(filter);

  const receivedItems = items?.results?.filter((item) => item?.operationType === 'disposed');

  return {
    items: receivedItems,
    isLoading,
    error,
  };
}
