import { type StockOperationFilter, useStockOperations } from '../stock-operations/stock-operations.resource';

export function useStockReceiving(filter: StockOperationFilter) {
  const { items, isLoading, error } = useStockOperations(filter);

  const receivedItems = items?.results?.filter(
    (item) => item?.operationType === 'receipt' && item?.status !== 'COMPLETED',
  );
  return {
    items: receivedItems,
    isLoading,
    error,
  };
}
