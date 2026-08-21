import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { InlineLoading, Search, Select, SelectItem, Tag } from '@carbon/react';
import { useLocations, formatDate, parseDate } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import type { ExportSheet } from '../reports-shell/export-utils';
import { formatQuantity } from '../reports-shell/format-quantity';
import { filterByItemAndSearch, distinctItemNames } from '../reports-shell/row-filter';
import SortableHeader from '../reports-shell/sortable-header.component';
import { useSortableRows } from '../reports-shell/use-sortable-rows';
import pageStyles from '../reports-shell/reports-page.scss';
import { useStockExpiryRiskReport, type StockBatchExpiryRow } from './stock-expiry-risk.resource';

const STOCK_LOCATION_TAG = 'Login Location';

type ExpiryStatus = 'expired' | 'expiresSoon' | 'watch' | 'ok';

function expiryStatus(daysUntilExpiry: number): ExpiryStatus {
  if (daysUntilExpiry < 0) {
    return 'expired';
  }
  if (daysUntilExpiry <= 30) {
    return 'expiresSoon';
  }
  if (daysUntilExpiry <= 90) {
    return 'watch';
  }
  return 'ok';
}

function urgencyTag(daysUntilExpiry: number, t: (key: string, defaultValue: string) => string) {
  const status = expiryStatus(daysUntilExpiry);
  if (status === 'expired') {
    return (
      <Tag type="red" size="sm">
        {t('expired', 'Expired')}
      </Tag>
    );
  }
  if (status === 'expiresSoon') {
    return (
      <Tag type="red" size="sm">
        {t('expiresSoon', 'Expires soon')}
      </Tag>
    );
  }
  if (status === 'watch') {
    return (
      <Tag type="magenta" size="sm">
        {t('watch', 'Watch')}
      </Tag>
    );
  }
  return null;
}

export default function StockExpiryRiskReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [locationUuid, setLocationUuid] = useState('');
  const [daysAheadInput, setDaysAheadInput] = useState('180');
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState<'' | ExpiryStatus>('');

  const daysAhead = daysAheadInput === '' ? undefined : Number(daysAheadInput);
  const { rows: rawRows, isLoading } = useStockExpiryRiskReport(daysAhead, locationUuid || undefined);
  const showLocationColumn = !locationUuid;

  const itemOptions = useMemo(() => distinctItemNames(rawRows), [rawRows]);
  const rows = useMemo(() => {
    const filtered = filterByItemAndSearch(rawRows, itemFilter, searchText);
    return statusFilter ? filtered.filter((row) => expiryStatus(row.daysUntilExpiry) === statusFilter) : filtered;
  }, [rawRows, itemFilter, searchText, statusFilter]);

  const kpiItems = useMemo(() => {
    const expiredCount = rows.filter((row) => row.daysUntilExpiry < 0).length;
    const expiringSoonCount = rows.filter((row) => row.daysUntilExpiry >= 0 && row.daysUntilExpiry <= 30).length;
    return [
      { label: t('batchesAtRisk', 'Batches At Risk'), value: rows.length },
      { label: t('expiredBatches', 'Already Expired'), value: expiredCount },
      { label: t('expiringWithin30Days', 'Expiring Within 30 Days'), value: expiringSoonCount },
    ];
  }, [t, rows]);

  const sortAccessors = useMemo(
    () => ({
      item: (row: StockBatchExpiryRow) => row.itemName,
      location: (row: StockBatchExpiryRow) => row.locationName ?? '',
      batchNo: (row: StockBatchExpiryRow) => row.batchNo,
      expirationDate: (row: StockBatchExpiryRow) => row.expirationDate,
      daysUntilExpiry: (row: StockBatchExpiryRow) => row.daysUntilExpiry,
      remainingQty: (row: StockBatchExpiryRow) => row.remainingQty,
    }),
    [],
  );
  const { sortedRows, sortKey, direction, toggleSort } = useSortableRows(
    rows,
    sortAccessors,
    'daysUntilExpiry',
    'asc',
  );

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockExpiryRisk', 'Stock Expiry Risk'),
      headers: [
        t('item', 'Item'),
        ...(showLocationColumn ? [t('location', 'Location')] : []),
        t('batchNo', 'Batch No'),
        t('expirationDate', 'Expiration Date'),
        t('daysUntilExpiry', 'Days Until Expiry'),
        t('remainingQty', 'Remaining Qty'),
        t('unit', 'Unit'),
      ],
      rows: rows.map((row) =>
        showLocationColumn
          ? [
              row.itemName,
              row.locationName ?? '',
              row.batchNo,
              formatDate(parseDate(row.expirationDate), { mode: 'standard', time: false }),
              row.daysUntilExpiry,
              row.remainingQty,
              row.unitName ?? '',
            ]
          : [
              row.itemName,
              row.batchNo,
              formatDate(parseDate(row.expirationDate), { mode: 'standard', time: false }),
              row.daysUntilExpiry,
              row.remainingQty,
              row.unitName ?? '',
            ],
      ),
    }),
    [t, rows, showLocationColumn],
  );

  return (
    <div>
      <BackToReportsLink to="stock-reports-home" label={t('stockReports', 'Stock Reports')} />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockExpiryRiskReportTitle', 'Stock Expiry Risk by Location')}</h2>

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
              onChange={(e) => setStatusFilter(e.target.value as '' | ExpiryStatus)}
            >
              <SelectItem value="" text={t('allStatuses', 'All statuses')} />
              <SelectItem value="expired" text={t('expired', 'Expired')} />
              <SelectItem value="expiresSoon" text={t('expiresSoon', 'Expires soon')} />
              <SelectItem value="watch" text={t('watch', 'Watch')} />
              <SelectItem value="ok" text={t('ok', 'OK')} />
            </Select>
          </div>
          <div className={pageStyles.filterField}>
            <Select
              id="daysAheadFilter"
              labelText={t('expiringWithin', 'Expiring within')}
              value={daysAheadInput}
              onChange={(e) => setDaysAheadInput(e.target.value)}
            >
              <SelectItem value="30" text={t('30Days', '30 days')} />
              <SelectItem value="90" text={t('90Days', '90 days')} />
              <SelectItem value="180" text={t('180Days', '180 days')} />
              <SelectItem value="365" text={t('1Year', '1 year')} />
              <SelectItem value="" text={t('allBatches', 'All batches')} />
            </Select>
          </div>
        </div>

        <ExportButtons filenameBase="stock-expiry-risk-report" mainSheet={mainExportSheet} disabled={isLoading} />

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
                    label={t('batchNo', 'Batch No')}
                    sortKey="batchNo"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                    className="left"
                  />
                  <SortableHeader
                    label={t('expirationDate', 'Expiration Date')}
                    sortKey="expirationDate"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                    className="left"
                  />
                  <SortableHeader
                    label={t('daysUntilExpiry', 'Days Until Expiry')}
                    sortKey="daysUntilExpiry"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <SortableHeader
                    label={t('remainingQty', 'Remaining Qty')}
                    sortKey="remainingQty"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <th className="left">{t('status', 'Status')}</th>
                </tr>
              </thead>
              <tbody>
                {sortedRows.map((row) => (
                  <tr key={`${row.stockItemId}-${row.locationId}-${row.batchNo}`}>
                    <td className="left">{row.itemName}</td>
                    {showLocationColumn && <td className="left">{row.locationName ?? '—'}</td>}
                    <td className="left">{row.batchNo}</td>
                    <td className="left">{formatDate(parseDate(row.expirationDate), { mode: 'standard', time: false })}</td>
                    <td>{row.daysUntilExpiry}</td>
                    <td>{formatQuantity(row.remainingQty, row.unitName)}</td>
                    <td className="left">{urgencyTag(row.daysUntilExpiry, t)}</td>
                  </tr>
                ))}
                {rows.length === 0 && (
                  <tr>
                    <td colSpan={showLocationColumn ? 7 : 6} className={pageStyles.emptyState}>
                      {t('noBatchesAtRisk', 'No batches found for this selection.')}
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
