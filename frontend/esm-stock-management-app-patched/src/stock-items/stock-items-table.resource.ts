import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { usePagination } from '@openmrs/esm-framework';
import { type StockItemFilter, useStockItems } from './stock-items.resource';
import { ResourceRepresentation } from '../core/api/api';

export function useStockItemsPages(v?: ResourceRepresentation) {
  const { t } = useTranslation();

  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPage, setCurrentPage] = useState(1);
  const [currentPageSize, setPageSize] = useState(10);
  const [searchString, setSearchString] = useState(null);

  // Drug filter type
  const [isDrug, setDrug] = useState('');

  const [stockItemFilter, setStockItemFilter] = useState<StockItemFilter>({
    startIndex: currentPage - 1,
    v: v || ResourceRepresentation.Default,
    limit: currentPageSize,
    q: null,
    totalCount: true,
  });

  const { items, isLoading, error } = useStockItems(stockItemFilter);
  const pagination = usePagination(items.results, currentPageSize);

  useEffect(() => {
    setStockItemFilter({
      startIndex: currentPage - 1,
      v: ResourceRepresentation.Default,
      limit: currentPageSize,
      q: searchString,
      totalCount: true,
      isDrug: isDrug,
    });
  }, [searchString, currentPage, currentPageSize, isDrug]);

  return {
    items: pagination.results,
    pagination,
    totalCount: items.totalCount,
    currentPageSize,
    currentPage,
    setCurrentPage,
    setPageSize,
    pageSizes,
    isLoading,
    error,
    isDrug,
    setDrug: (drug: string) => {
      setCurrentPage(1);
      setDrug(drug);
    },
    setSearchString,
  };
}
