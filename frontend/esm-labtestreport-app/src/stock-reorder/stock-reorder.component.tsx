import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { InlineLoading, Search, Select, SelectItem, Tag } from '@carbon/react';
import { useLocations } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import type { ExportSheet } from '../reports-shell/export-utils';
import { formatQuantity } from '../reports-shell/format-quantity';
import { filterByItemAndSearch, distinctItemNames } from '../reports-shell/row-filter';
import SortableHeader from '../reports-shell/sortable-header.component';
import { useSortableRows } from '../reports-shell/use-sortable-rows';
import pageStyles from '../reports-shell/reports-page.scss';
import { useStockReorderReport, type StockReorderRow } from './stock-reorder.resource';

const STOCK_LOCATION_TAG = 'Login Location';

type ReorderStatus = 'outOfStock' | 'lowStock';

function reorderStatus(onHandQty: number): ReorderStatus {
  return onHandQty <= 0 ? 'outOfStock' : 'lowStock';
}

export default function StockReorderReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [locationUuid, setLocationUuid] = useState('');
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState<'' | ReorderStatus>('');

  const { rows: rawRows, isLoading } = useStockReorderReport(locationUuid || undefined);
  const showLocationColumn = !locationUuid;

  const itemOptions = useMemo(() => distinctItemNames(rawRows), [rawRows]);
  const rows = useMemo(() => {
    const filtered = filterByItemAndSearch(rawRows, itemFilter, searchText);
    return statusFilter ? filtered.filter((row) => reorderStatus(row.onHandQty) === statusFilter) : filtered;
  }, [rawRows, itemFilter, searchText, statusFilter]);

  const kpiItems = useMemo(
    () => [
      { label: t('itemsBelowReorderLevel', 'Items Below Reorder Level'), value: rows.length },
      {
        label: t('totalDeficitUnits', 'Total Deficit (Units)'),
        value: rows.reduce((sum, row) => sum + (row.reorderLevel - row.onHandQty), 0),
      },
    ],
    [t, rows],
  );

  const sortAccessors = useMemo(
    () => ({
      item: (row: StockReorderRow) => row.itemName,
      location: (row: StockReorderRow) => row.locationName ?? '',
      reorderLevel: (row: StockReorderRow) => row.reorderLevel,
      onHandQty: (row: StockReorderRow) => row.onHandQty,
      deficit: (row: StockReorderRow) => row.reorderLevel - row.onHandQty,
    }),
    [],
  );
  const { sortedRows, sortKey, direction, toggleSort } = useSortableRows(rows, sortAccessors, 'deficit', 'desc');

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockReorder', 'Stock Reorder'),
      headers: [
        t('item', 'Item'),
        ...(showLocationColumn ? [t('location', 'Location')] : []),
        t('reorderLevel', 'Reorder Level'),
        t('onHandQty', 'On-Hand Qty'),
        t('deficit', 'Deficit'),
        t('unit', 'Unit'),
      ],
      rows: rows.map((row) => {
        const base = showLocationColumn ? [row.itemName, row.locationName ?? ''] : [row.itemName];
        return [...base, row.reorderLevel, row.onHandQty, row.reorderLevel - row.onHandQty, row.unitName ?? ''];
      }),
    }),
    [t, rows, showLocationColumn],
  );

  return (
    <div>
      <BackToReportsLink to="stock-reports-home" label={t('stockReports', 'Stock Reports')} />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockReorderReportTitle', 'Reorder / Low-Stock Report')}</h2>
        <p className={pageStyles.pageSubtitle}>
          {t(
            'stockReorderSubtitle',
            'Items currently below the reorder threshold configured for them at a location (Stock Rules). Configure reorder rules in the Stock Management app to add items here.',
          )}
        </p>

        {!isLoading && <KpiTiles items={kpiItems} />}

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField} style={{ minWidth: '16rem' }}>
            <Search
              size="md"
              labelText={t('searchByItemOrLocation', 'Search by item or location')}
              placeholder={t('searchByItemOrLocationPlaceholder', 'Search by item or location...')}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              onClear={() => setSearchText('')}
            />
          </div>
          <div className={pageStyles.filterField}>
            <Select
              id="itemFilter"
              labelText={t('item', 'Item')}
              value={itemFilter}
              onChange={(e) => setItemFilter(e.target.value)}
            >
              <SelectItem value="" text={t('allItems', 'All items')} />
              {itemOptions.map((itemName) => (
                <React.Fragment key={itemName}>
                  <SelectItem value={itemName} text={itemName} />
                </React.Fragment>
              ))}
            </Select>
          </div>
          <div className={pageStyles.filterField}>
            <Select
              id="locationFilter"
              labelText={t('location', 'Location')}
              value={locationUuid}
              onChange={(e) => setLocationUuid(e.target.value)}
            >
              <SelectItem value="" text={t('allLocations', 'All locations')} />
              {locations?.map((location) => (
                <React.Fragment key={location.uuid}>
                  <SelectItem value={location.uuid} text={location.display} />
                </React.Fragment>
              ))}
            </Select>
          </div>
          <div className={pageStyles.filterField}>
            <Select
              id="statusFilter"
              labelText={t('status', 'Status')}
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value as '' | ReorderStatus)}
            >
              <SelectItem value="" text={t('allStatuses', 'All statuses')} />
              <SelectItem value="outOfStock" text={t('outOfStock', 'Out of stock')} />
              <SelectItem value="lowStock" text={t('lowStock', 'Low stock')} />
            </Select>
          </div>
        </div>

        <ExportButtons filenameBase="stock-reorder-report" mainSheet={mainExportSheet} disabled={isLoading} />

        {isLoading && <InlineLoading description={t('loadingReport', 'Loading report...')} />}

        {!isLoading && (
          <div className={pageStyles.tableContainer}>
            <table className={pageStyles.dataTable}>
              <thead>
                <tr>
                  <SortableHeader
                    label={t('item', 'Item')}
                    sortKey="item"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                    className="left"
                  />
                  {showLocationColumn && (
                    <SortableHeader
                      label={t('location', 'Location')}
                      sortKey="location"
                      activeSortKey={sortKey}
                      direction={direction}
                      onSort={toggleSort}
                      className="left"
                    />
                  )}
                  <SortableHeader
                    label={t('reorderLevel', 'Reorder Level')}
                    sortKey="reorderLevel"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <SortableHeader
                    label={t('onHandQty', 'On-Hand Qty')}
                    sortKey="onHandQty"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <SortableHeader
                    label={t('deficit', 'Deficit')}
                    sortKey="deficit"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <th className="left">{t('status', 'Status')}</th>
                </tr>
              </thead>
              <tbody>
                {sortedRows.map((row) => (
                  <tr key={`${row.stockItemId}-${row.locationId}`}>
                    <td className="left">{row.itemName}</td>
                    {showLocationColumn && <td className="left">{row.locationName ?? '—'}</td>}
                    <td>{formatQuantity(row.reorderLevel, row.unitName)}</td>
                    <td>{formatQuantity(row.onHandQty, row.unitName)}</td>
                    <td>{formatQuantity(row.reorderLevel - row.onHandQty, row.unitName)}</td>
                    <td className="left">
                      <Tag type={reorderStatus(row.onHandQty) === 'outOfStock' ? 'red' : 'magenta'} size="sm">
                        {reorderStatus(row.onHandQty) === 'outOfStock'
                          ? t('outOfStock', 'Out of stock')
                          : t('lowStock', 'Low stock')}
                      </Tag>
                    </td>
                  </tr>
                ))}
                {rows.length === 0 && (
                  <tr>
                    <td colSpan={showLocationColumn ? 6 : 5} className={pageStyles.emptyState}>
                      {t(
                        'noItemsBelowReorderLevel',
                        'No items are currently below their reorder level for this selection.',
                      )}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
