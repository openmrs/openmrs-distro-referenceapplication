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
import { useStockoutFrequencyReport, type StockoutFrequencyRow } from './stock-stockout-frequency.resource';

const STOCK_LOCATION_TAG = 'Login Location';

type StockoutStatus = 'hasStockouts' | 'noStockouts';

function stockoutStatus(stockoutDays: number): StockoutStatus {
  return stockoutDays > 0 ? 'hasStockouts' : 'noStockouts';
}

export default function StockoutFrequencyReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [locationUuid, setLocationUuid] = useState('');
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const [statusFilter, setStatusFilter] = useState<'' | StockoutStatus>('');

  const { rows: rawRows, isLoading } = useStockoutFrequencyReport(
    appliedDates.startDate,
    appliedDates.endDate,
    locationUuid || undefined,
  );
  const showLocationColumn = !locationUuid;

  const itemOptions = useMemo(() => distinctItemNames(rawRows), [rawRows]);
  const rows = useMemo(() => {
    const filtered = filterByItemAndSearch(rawRows, itemFilter, searchText);
    return statusFilter ? filtered.filter((row) => stockoutStatus(row.stockoutDays) === statusFilter) : filtered;
  }, [rawRows, itemFilter, searchText, statusFilter]);

  const kpiItems = useMemo(() => {
    const withStockouts = rows.filter((row) => row.stockoutDays > 0).length;
    const totalStockoutDays = rows.reduce((sum, row) => sum + row.stockoutDays, 0);
    return [
      { label: t('itemLocationPairsWithStockouts', 'Item/Location Pairs With Stockouts'), value: withStockouts },
      { label: t('totalStockoutDays', 'Total Stockout Days'), value: totalStockoutDays },
    ];
  }, [t, rows]);

  const sortAccessors = useMemo(
    () => ({
      item: (row: StockoutFrequencyRow) => row.itemName,
      location: (row: StockoutFrequencyRow) => row.locationName ?? '',
      stockoutDays: (row: StockoutFrequencyRow) => row.stockoutDays,
      activeDays: (row: StockoutFrequencyRow) => row.activeDays,
    }),
    [],
  );
  const { sortedRows, sortKey, direction, toggleSort } = useSortableRows(rows, sortAccessors, 'stockoutDays', 'desc');

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockoutFrequency', 'Stockout Frequency'),
      headers: [
        t('item', 'Item'),
        ...(showLocationColumn ? [t('location', 'Location')] : []),
        t('stockoutDays', 'Stockout Days'),
        t('activeDays', 'Active Days'),
      ],
      rows: rows.map((row) => {
        const base = showLocationColumn ? [row.itemName, row.locationName ?? ''] : [row.itemName];
        return [...base, row.stockoutDays, row.activeDays];
      }),
    }),
    [t, rows, showLocationColumn],
  );

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  return (
    <div>
      <BackToReportsLink to="stock-reports-home" label={t('stockReports', 'Stock Reports')} />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockoutFrequencyReportTitle', 'Stockout Frequency by Location')}</h2>
        <p className={pageStyles.pageSubtitle}>
          {t(
            'stockoutFrequencySubtitle',
            'How often each item ran out at each location over the selected period, to spot chronic shortages. Counts days with recorded stock activity that ended at zero, not full calendar coverage.',
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
              onChange={(e) => setStatusFilter(e.target.value as '' | StockoutStatus)}
            >
              <SelectItem value="" text={t('allStatuses', 'All statuses')} />
              <SelectItem value="hasStockouts" text={t('hasStockouts', 'Has stockouts')} />
              <SelectItem value="noStockouts" text={t('noStockouts', 'No stockouts')} />
            </Select>
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="startDate">{t('startDate', 'Start Date')}</label>
            <input
              id="startDate"
              type="date"
              value={startDateInput}
              max={getTodayDateString()}
              onChange={(e) => setStartDateInput(clampToToday(e.target.value))}
            />
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="endDate">{t('endDate', 'End Date')}</label>
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

        <ExportButtons filenameBase="stock-stockout-frequency-report" mainSheet={mainExportSheet} disabled={isLoading} />

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
                    label={t('stockoutDays', 'Stockout Days')}
                    sortKey="stockoutDays"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                  <SortableHeader
                    label={t('activeDays', 'Active Days')}
                    sortKey="activeDays"
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
                    <td>
                      {row.stockoutDays > 0 ? (
                        <Tag type="red" size="sm">
                          {row.stockoutDays}
                        </Tag>
                      ) : (
                        0
                      )}
                    </td>
                    <td>{row.activeDays}</td>
                  </tr>
                ))}
                {rows.length === 0 && (
                  <tr>
                    <td colSpan={showLocationColumn ? 4 : 3} className={pageStyles.emptyState}>
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
