import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, InlineLoading, Search, Select, SelectItem, Tag } from '@carbon/react';
import { useLocations } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import type { ExportSheet } from '../reports-shell/export-utils';
import { getTodayDateString, clampToToday } from '../reports-shell/date-utils';
import { filterByItemAndSearch, distinctItemNames } from '../reports-shell/row-filter';
import SortableHeader from '../reports-shell/sortable-header.component';
import { useSortableRows } from '../reports-shell/use-sortable-rows';
import pageStyles from '../reports-shell/reports-page.scss';
import { useStockDaysRemainingReport, type StockDaysRemainingRow } from './stock-days-remaining.resource';

const STOCK_LOCATION_TAG = 'Login Location';
const LOW_DAYS_THRESHOLD = 14;

type DaysRemainingStatus = 'noConsumption' | 'low' | 'ok';

function daysRemainingStatus(daysRemaining: number | null): DaysRemainingStatus {
  if (daysRemaining === null) {
    return 'noConsumption';
  }
  return daysRemaining <= LOW_DAYS_THRESHOLD ? 'low' : 'ok';
}

export default function StockDaysRemainingReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [locationUuid, setLocationUuid] = useState('');
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState<'' | DaysRemainingStatus>('');

  const { rows: rawRows, isLoading } = useStockDaysRemainingReport(
    appliedDates.startDate,
    appliedDates.endDate,
    locationUuid || undefined,
  );
  const showLocationColumn = !locationUuid;

  const itemOptions = useMemo(() => distinctItemNames(rawRows), [rawRows]);
  const rows = useMemo(() => {
    const filtered = filterByItemAndSearch(rawRows, itemFilter, searchText);
    return statusFilter ? filtered.filter((row) => daysRemainingStatus(row.daysRemaining) === statusFilter) : filtered;
  }, [rawRows, itemFilter, searchText, statusFilter]);

  const sortAccessors = useMemo(
    () => ({
      item: (row: StockDaysRemainingRow) => row.itemName,
      location: (row: StockDaysRemainingRow) => row.locationName ?? '',
      onHandQty: (row: StockDaysRemainingRow) => row.onHandQty,
      avgDailyConsumption: (row: StockDaysRemainingRow) => row.avgDailyConsumption,
      daysRemaining: (row: StockDaysRemainingRow) => row.daysRemaining,
    }),
    [],
  );
  const {
    sortedRows,
    sortKey,
    direction,
    toggleSort,
  } = useSortableRows(rows, sortAccessors, 'daysRemaining', 'asc');

  const kpiItems = useMemo(() => {
    const lowStockCount = rows.filter((row) => row.daysRemaining !== null && row.daysRemaining <= LOW_DAYS_THRESHOLD)
      .length;
    const noConsumptionCount = rows.filter((row) => row.daysRemaining === null).length;
    return [
      { label: t('itemLocationPairs', 'Item/Location Pairs'), value: rows.length },
      { label: t('lowDaysRemaining', '≤14 Days Remaining'), value: lowStockCount },
      { label: t('noRecentConsumption', 'No Recent Consumption'), value: noConsumptionCount },
    ];
  }, [t, rows]);

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('daysOfStock', 'Days of Stock'),
      headers: [
        t('item', 'Item'),
        ...(showLocationColumn ? [t('location', 'Location')] : []),
        t('onHandQty', 'On-Hand Qty'),
        t('avgDailyConsumption', 'Avg Daily Consumption'),
        t('daysRemaining', 'Days Remaining'),
      ],
      rows: sortedRows.map((row) => {
        const base = showLocationColumn ? [row.itemName, row.locationName ?? ''] : [row.itemName];
        return [
          ...base,
          row.onHandQty,
          Number(row.avgDailyConsumption.toFixed(2)),
          row.daysRemaining === null ? '' : Number(row.daysRemaining.toFixed(1)),
        ];
      }),
    }),
    [t, sortedRows, showLocationColumn],
  );

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  return (
    <div>
      <BackToReportsLink to="stock-reports-home" label={t('stockReports', 'Stock Reports')} />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockDaysRemainingReportTitle', 'Days of Stock Remaining')}</h2>
        <p className={pageStyles.pageSubtitle}>
          {t(
            'stockDaysRemainingSubtitle',
            'Projects how many days of stock are left at each location, based on average daily consumption over the selected window (defaults to the last 30 days).',
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
              onChange={(e) => setStatusFilter(e.target.value as '' | DaysRemainingStatus)}
            >
              <SelectItem value="" text={t('allStatuses', 'All statuses')} />
              <SelectItem value="low" text={t('lowDaysRemainingShort', 'Low (≤14 days)')} />
              <SelectItem value="noConsumption" text={t('noRecentConsumptionShort', 'No consumption yet')} />
              <SelectItem value="ok" text={t('ok', 'OK')} />
            </Select>
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="startDate">{t('consumptionWindowStart', 'Consumption Window Start')}</label>
            <input
              id="startDate"
              type="date"
              value={startDateInput}
              max={getTodayDateString()}
              onChange={(e) => setStartDateInput(clampToToday(e.target.value))}
            />
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="endDate">{t('consumptionWindowEnd', 'Consumption Window End')}</label>
            <input
              id="endDate"
              type="date"
              value={endDateInput}
              max={getTodayDateString()}
              onChange={(e) => setEndDateInput(clampToToday(e.target.value))}
            />
          </div>
          <Button size="md" onClick={applyFilter}>
            {t('filter', 'Filter')}
          </Button>
        </div>

        <ExportButtons filenameBase="stock-days-remaining-report" mainSheet={mainExportSheet} disabled={isLoading} />

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
                    label={t('onHandQty', 'On-Hand Qty')}
                    sortKey="onHandQty"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <SortableHeader
                    label={t('avgDailyConsumption', 'Avg Daily Consumption')}
                    sortKey="avgDailyConsumption"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <SortableHeader
                    label={t('daysRemaining', 'Days Remaining')}
                    sortKey="daysRemaining"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                </tr>
              </thead>
              <tbody>
                {sortedRows.map((row) => (
                  <tr key={`${row.stockItemId}-${row.locationId}`}>
                    <td className="left">{row.itemName}</td>
                    {showLocationColumn && <td className="left">{row.locationName ?? '—'}</td>}
                    <td>{row.onHandQty}</td>
                    <td>{row.avgDailyConsumption.toFixed(2)}</td>
                    <td>
                      {row.daysRemaining === null ? (
                        <Tag type="gray" size="sm">
                          {t('noRecentConsumptionShort', 'No consumption yet')}
                        </Tag>
                      ) : row.daysRemaining <= LOW_DAYS_THRESHOLD ? (
                        <Tag type="red" size="sm">
                          {t('daysCount', '{{count}} days', { count: Math.round(row.daysRemaining) })}
                        </Tag>
                      ) : (
                        Math.round(row.daysRemaining)
                      )}
                    </td>
                  </tr>
                ))}
                {sortedRows.length === 0 && (
                  <tr>
                    <td colSpan={showLocationColumn ? 5 : 4} className={pageStyles.emptyState}>
                      {t('noDataForSelection', 'No data found for this selection.')}
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
