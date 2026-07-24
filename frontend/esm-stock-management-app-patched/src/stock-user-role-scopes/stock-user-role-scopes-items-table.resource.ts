import { type StockOperationFilter } from '../stock-operations/stock-operations.resource';
import { useUserRoleScopes } from './stock-user-role-scopes.resource';
import { useState } from 'react';
import { usePagination } from '@openmrs/esm-framework';

export default function useStockUserRoleScopesPage(filter: StockOperationFilter) {
  const { items, isLoading, error } = useUserRoleScopes(filter);

  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPageSize, setPageSize] = useState(10);
  const { goTo, results: paginatedItems, currentPage } = usePagination(items.results, currentPageSize);

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
  };
}
