import { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { usePagination } from '@openmrs/esm-framework';
import { useStockSources } from './stock-sources.resource';
import { type StockOperationFilter } from '../stock-operations/stock-operations.resource';

export default function useStockSourcesPage(filter: StockOperationFilter) {
  const { t } = useTranslation();
  const { items, isLoading, error } = useStockSources(filter);

  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPageSize, setPageSize] = useState(10);
  const { goTo, results: paginatedItems, currentPage } = usePagination(items.results, currentPageSize);

  const tableHeaders = useMemo(
    () => [
      {
        id: 1,
        header: t('name', 'Name'),
        key: 'name',
      },
      {
        id: 2,
        header: t('acronym', 'Acronym'),
        key: 'acronym',
      },
      {
        id: 3,
        header: t('sourceType', 'Source Type'),
        key: 'sourceType',
      },
      {
        id: 4,
        header: t('actions', 'Actions'),
        key: 'actions',
      },
    ],
    [t],
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
