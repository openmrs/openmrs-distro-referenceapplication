import React, { useMemo, useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import {
  DataTable,
  DataTableSkeleton,
  IconButton,
  Pagination,
  Table,
  TableBatchActions,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
  TableToolbar,
  TableToolbarAction,
  TableToolbarContent,
  TableToolbarMenu,
  TableToolbarSearch,
  Tile,
} from '@carbon/react';
import { Edit } from '@carbon/react/icons';
import { ErrorState, isDesktop, restBaseUrl, useSession } from '@openmrs/esm-framework';
import { handleMutate } from '../utils';
import { launchAddOrEditStockItemWorkspace } from './stock-item.utils';
import { ResourceRepresentation } from '../core/api/api';
import { useDebounce } from '../core/hooks/debounce-hook';
import { useStockItemsPages } from './stock-items-table.resource';
import { useStockItemQuantities } from './stock-item-quantities.resource';
import AddStockItemActionButton from './add-stock-item/add-stock-action-button.component';
import AddStockItemsBulktImportActionButton from './add-bulk-stock-item/add-stock-items-bulk-import-action-button.component';
import DeleteStockItemActionButton from './delete-stock-item-action-button.component';
import EditStockItemActionsMenu from './edit-stock-item/edit-stock-item-action-menu.component';
import FilterStockItems from './components/filter-stock-items/filter-stock-items.component';
import StockItemsColumnFilter, {
  EMPTY_COLUMN_FILTERS,
  type StockItemColumnFilters,
  type StockStatus,
} from './components/stock-items-column-filter/stock-items-column-filter.component';
import { type CustomTableHeader } from '../core/components/table/types';
import styles from './stock-items-table.scss';

interface StockItemsTableProps {
  from?: string;
}

const StockItemsTableComponent: React.FC<StockItemsTableProps> = () => {
  const { t } = useTranslation();
  const { sessionLocation } = useSession();
  const [searchInput, setSearchInput] = useState('');

  const handleRefresh = () => {
    handleMutate(`${restBaseUrl}/stockmanagement/stockitem`);
    handleMutate(`${restBaseUrl}/stockmanagement/stockiteminventory`);
  };

  const { error, isDrug, isLoading, items, setDrug, setSearchString } = useStockItemsPages(ResourceRepresentation.Full);

  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPage, setCurrentPage] = useState(1);
  const [currentPageSize, setPageSize] = useState(10);

  const stockItemUuids = useMemo(() => items?.map((item) => item.uuid) ?? [], [items]);
  const { quantityByItem, quantityMetaByItem } = useStockItemQuantities(stockItemUuids, sessionLocation?.uuid);

  const [columnFilters, setColumnFilters] = useState<StockItemColumnFilters>(EMPTY_COLUMN_FILTERS);

  const getStockStatus = (stockItem: { uuid: string | null | undefined; reorderLevel?: number | null }): StockStatus => {
    const quantity = quantityByItem.get(stockItem.uuid ?? '') ?? 0;
    if (quantity <= 0) return 'outOfStock';
    if (stockItem.reorderLevel && quantity < stockItem.reorderLevel) return 'understocked';
    return 'inStock';
  };

  const dispensingUoMOptions = useMemo(
    () =>
      Array.from(new Set(items?.map((item) => item.dispensingUnitName).filter((v): v is string => Boolean(v)))).sort(),
    [items],
  );
  const packagingUoMOptions = useMemo(
    () =>
      Array.from(
        new Set(items?.map((item) => item.defaultStockOperationsUoMName).filter((v): v is string => Boolean(v))),
      ).sort(),
    [items],
  );

  const filteredItems = useMemo(() => {
    const { dispensingUoM, packagingUoM, stockStatus } = columnFilters;
    if (dispensingUoM.length === 0 && packagingUoM.length === 0 && stockStatus.length === 0) {
      return items;
    }
    return items?.filter((item) => {
      if (dispensingUoM.length > 0 && !dispensingUoM.includes(item.dispensingUnitName ?? '')) return false;
      if (packagingUoM.length > 0 && !packagingUoM.includes(item.defaultStockOperationsUoMName ?? '')) return false;
      if (stockStatus.length > 0 && !stockStatus.includes(getStockStatus(item))) return false;
      return true;
    });
  }, [items, columnFilters, quantityByItem]);

  // Column filters/search/type now narrow the full fetched set (see stock-items-table.resource.ts),
  // so pagination is applied client-side, after filtering, over that full set.
  const pageItems = useMemo(() => {
    const start = (currentPage - 1) * currentPageSize;
    return filteredItems?.slice(start, start + currentPageSize) ?? [];
  }, [filteredItems, currentPage, currentPageSize]);

  // Land back on page 1 whenever the filtered set changes shape, so the user
  // never lands on a page number that no longer has any rows.
  useEffect(() => {
    setCurrentPage(1);
  }, [columnFilters, isDrug]);

  const handleSearch = (query: string) => {
    setSearchInput(query);
  };

  const debouncedSearch = useDebounce((query: string) => {
    setSearchString(query);
    setCurrentPage(1);
  }, 1000);

  useEffect(() => {
    debouncedSearch(searchInput);
  }, [searchInput, debouncedSearch]);

  const tableHeaders = useMemo(
    () => [
      {
        id: 0,
        header: t('type', 'Type'),
        key: 'type',
      },
      {
        id: 1,
        header: t('genericName', 'Generic name'),
        key: 'genericName',
      },
      {
        id: 2,
        header: t('commonName', 'Common name'),
        key: 'commonName',
      },
      {
        id: 3,
        header: t('tradeName', 'Trade name'),
        key: 'tradeName',
      },
      {
        id: 4,
        header: t('quantity', 'Quantity'),
        key: 'quantity',
      },
      {
        id: 5,
        header: t('dispensingUnitName', 'Dispensing UoM'),
        key: 'dispensingUnitName',
      },
      {
        id: 6,
        header: t('defaultStockOperationsUoMName', 'Bulk packaging'),
        key: 'defaultStockOperationsUoMName',
      },
      {
        id: 7,
        header: t('reorderLevel', 'Reorder level'),
        key: 'reorderLevel',
      },
      {
        id: 8,
        header: t('actions', 'Actions'),
        key: 'actions',
      },
    ],
    [t],
  );

  const tableRows = useMemo(() => {
    return pageItems?.map((stockItem) => ({
      ...stockItem,
      id: stockItem?.uuid,
      key: `key-${stockItem?.uuid}`,
      uuid: `${stockItem?.uuid}`,
      type: stockItem?.drugUuid ? t('drug', 'Drug') : t('other', 'Other'),
      genericName: <EditStockItemActionsMenu data={stockItem} />,
      commonName: stockItem?.commonName,
      tradeName: stockItem?.drugUuid ? stockItem?.conceptName : '',
      preferredVendorName: stockItem?.preferredVendorName,
      dispensingUoM: stockItem?.defaultStockOperationsUoMName,
      // Inventory is recorded in the packaging unit stock operations use (e.g. Box), not the
      // dispensing unit (e.g. Strip) - show both when they differ so neither figure is mislabeled.
      quantity: (() => {
        const rawQuantity = quantityByItem.get(stockItem?.uuid) ?? 0;
        const meta = quantityMetaByItem.get(stockItem?.uuid);
        const packagingUoM = meta?.quantityUoM ?? stockItem?.dispensingUnitName ?? '';
        const packagingPart = `${rawQuantity.toLocaleString()} ${packagingUoM}`;
        if (meta && stockItem?.dispensingUnitName && meta.quantityUoM !== stockItem.dispensingUnitName) {
          const dispensingQuantity = rawQuantity * meta.quantityFactor;
          return `${packagingPart} (${dispensingQuantity.toLocaleString()} ${stockItem.dispensingUnitName})`;
        }
        return packagingPart;
      })(),
      dispensingUnitName: stockItem?.dispensingUnitName,
      defaultStockOperationsUoMName: stockItem?.defaultStockOperationsUoMName,
      reorderLevel:
        stockItem?.reorderLevelUoMName && stockItem?.reorderLevel
          ? `${stockItem?.reorderLevel?.toLocaleString()} ${stockItem?.reorderLevelUoMName}`
          : '',
      actions: (
        <>
          <IconButton
            kind="ghost"
            label={t('editStockItem', 'Edit stock item')}
            onClick={() => {
              stockItem.isDrug = !!stockItem.drugUuid;
              launchAddOrEditStockItemWorkspace(t, stockItem);
            }}
          >
            <Edit size={16} />
          </IconButton>
          <DeleteStockItemActionButton
            uuid={stockItem?.uuid}
            displayName={stockItem?.drugName ?? stockItem?.conceptName ?? stockItem?.commonName}
            quantityOnHand={quantityByItem.get(stockItem?.uuid ?? '') ?? 0}
          />
        </>
      ),
    }));
  }, [pageItems, t, quantityByItem, quantityMetaByItem]);

  if (isLoading) {
    return <DataTableSkeleton role="progressbar" />;
  }

  if (error) {
    return <ErrorState headerTitle={t('errorLoadingStockItems', 'Error loading stock items')} error={error} />;
  }

  return (
    <>
      <h2 className={styles.tableHeader}>
        {t('stockItemsTableHeader', 'Drugs and other stock items managed by the system.')}
      </h2>
      <DataTable rows={tableRows} headers={tableHeaders} isSortable useZebraStyles>
        {({ rows, headers, getHeaderProps, getTableProps, getRowProps, getBatchActionProps }) => (
          <TableContainer>
            <TableToolbar
              style={{
                position: 'static',
                overflow: 'visible',
                backgroundColor: 'color',
              }}
            >
              <TableBatchActions {...getBatchActionProps()} />
              <TableToolbarContent
                style={{
                  display: 'flex',
                  alignItems: 'center',
                }}
              >
                <TableToolbarSearch
                  onChange={(e) =>
                    handleSearch(typeof e === 'string' ? e : (e as React.ChangeEvent<HTMLInputElement>).target.value)
                  }
                  persistent
                  placeholder={t('searchStockItems', 'Search stock items')}
                  value={searchInput}
                />
                <FilterStockItems filterType={isDrug} changeFilterType={setDrug} />
                <StockItemsColumnFilter
                  dispensingUoMOptions={dispensingUoMOptions}
                  packagingUoMOptions={packagingUoMOptions}
                  filters={columnFilters}
                  onChange={setColumnFilters}
                />
                <AddStockItemsBulktImportActionButton />
                <TableToolbarMenu data-testid="stock-items-menu">
                  <TableToolbarAction className={styles.toolbarAction} onClick={handleRefresh}>
                    {t('refresh', 'Refresh')}
                  </TableToolbarAction>
                </TableToolbarMenu>
                <AddStockItemActionButton />
              </TableToolbarContent>
            </TableToolbar>
            <Table {...getTableProps()}>
              <TableHead>
                <TableRow>
                  {headers.map(
                    (header) =>
                      header.key !== 'details' && (
                        <TableHeader
                          {...getHeaderProps({
                            header,
                            isSortable: (header as CustomTableHeader).isSortable,
                          })}
                          className={isDesktop ? styles.desktopHeader : styles.tabletHeader}
                          key={`${header.key}`}
                          isSortable={header.key !== 'name'}
                        >
                          {(() => {
                            const customHeader = header as CustomTableHeader;
                            return typeof customHeader.header === 'object' &&
                              customHeader.header !== null &&
                              'content' in customHeader.header
                              ? (customHeader.header.content as React.ReactNode)
                              : (customHeader.header as React.ReactNode);
                          })()}
                        </TableHeader>
                      ),
                  )}
                  <TableHeader />
                </TableRow>
              </TableHead>
              <TableBody>
                {rows.map((row) => {
                  return (
                    <React.Fragment key={row.id}>
                      <TableRow
                        {...getRowProps({ row })}
                        className={isDesktop ? styles.desktopRow : styles.tabletRow}
                        key={row.id}
                      >
                        {row.cells.map(
                          (cell) =>
                            cell?.info?.header !== 'details' && <TableCell key={cell.id}>{cell.value}</TableCell>,
                        )}
                      </TableRow>
                    </React.Fragment>
                  );
                })}
              </TableBody>
            </Table>
            {rows.length === 0 ? (
              <div className={styles.tileContainer}>
                <Tile className={styles.tile}>
                  <div className={styles.tileContent}>
                    <p className={styles.content}>{t('noStockItemsToDisplay', 'No stock items to display')}</p>
                    <p className={styles.helper}>{t('checkFilters', 'Check the filters above')}</p>
                  </div>
                </Tile>
              </div>
            ) : null}
          </TableContainer>
        )}
      </DataTable>
      <Pagination
        className={styles.paginationOverride}
        onChange={({ page, pageSize }) => {
          setCurrentPage(page);
          setPageSize(pageSize);
        }}
        page={currentPage}
        pageSize={currentPageSize}
        pageSizes={pageSizes}
        totalItems={filteredItems?.length ?? 0}
      />
    </>
  );
};

export default StockItemsTableComponent;
