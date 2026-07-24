import { useMemo, useState } from 'react';
import { type StockRuleFilter, useStockRules } from '../../stock-items.resource';
import { usePagination } from '@openmrs/esm-framework';

export function useStockItemRules(filter: StockRuleFilter) {
  const { items, isLoading, error } = useStockRules(filter);

  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPageSize, setPageSize] = useState(10);
  const { goTo, results: paginatedItems, currentPage } = usePagination(items.results, currentPageSize);

  const tableHeaders = useMemo(
    () => [
      {
        key: 'location',
        header: 'Location',
      },
      {
        key: 'name',
        header: 'Name',
      },
      {
        key: 'quantity',
        header: 'Quantity Threshold',
      },
      {
        key: 'evaluationFrequency',
        header: 'Frequency Check',
      },
      {
        key: 'actionFrequency',
        header: 'Notification Frequency',
      },
      {
        key: 'alertRole',
        header: 'Alert Role',
      },
      {
        key: 'mailRole',
        header: 'Mail Role',
      },
      {
        key: 'enabled',
        header: 'Enabled?',
      },
      {
        // id: 4,
        header: 'actions',
        key: 'actions',
      },
    ],
    [],
  );

  return {
    items: paginatedItems,
    totalItems: items?.totalCount,
    currentPage,
    currentPageSize,
    paginatedItems,
    goTo,
    pageSizes,
    isLoading,
    error,
    setPageSize,
    tableHeaders,
  };
}
