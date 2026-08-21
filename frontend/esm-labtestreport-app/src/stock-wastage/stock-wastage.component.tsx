import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, ContentSwitcher, InlineLoading, Search, Select, SelectItem, Switch } from '@carbon/react';
import { useLocations } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import MonthCompareControls from '../reports-shell/month-compare-controls.component';
import ComparisonSummaryTable, { type ComparisonSummaryRow } from '../reports-shell/comparison-summary-table.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { buildKpiExportSheet, buildComparisonExportSheet, type ExportSheet } from '../reports-shell/export-utils';
import { useMonthComparison } from '../reports-shell/month-compare';
import { getTodayDateString, clampToToday } from '../reports-shell/date-utils';
import { filterByItemAndSearch, distinctItemNames } from '../reports-shell/row-filter';
import { formatQuantity } from '../reports-shell/format-quantity';
import SortableHeader from '../reports-shell/sortable-header.component';
import { useSortableRows } from '../reports-shell/use-sortable-rows';
import pageStyles from '../reports-shell/reports-page.scss';
import { useStockWastageReport, type StockLocationQtyRow } from './stock-wastage.resource';

const STOCK_LOCATION_TAG = 'Login Location';

function rowLabel(row: StockLocationQtyRow, showLocation: boolean): string {
  return showLocation ? `${row.itemName} — ${row.locationName ?? '?'}` : row.itemName;
}

function toRankedRows(rows: Array<StockLocationQtyRow>, showLocation: boolean) {
  return [...rows]
    .map((row) => ({ label: rowLabel(row, showLocation), value: row.quantity }))
    .sort((a, b) => b.value - a.value);
}

export default function StockWastageReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [locationUuid, setLocationUuid] = useState('');
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const compare = useMonthComparison();

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows: rawRows, isLoading } = useStockWastageReport(primaryStartDate, primaryEndDate, locationUuid || undefined);
  const { rows: compareRawRows, isLoading: compareLoading } = useStockWastageReport(
    compare.comparison.startDate,
    compare.comparison.endDate,
    locationUuid || undefined,
    compare.enabled,
  );
  const dataLoading = isLoading || (compare.enabled && compareLoading);
  const showLocationInLabel = !locationUuid;

  const itemOptions = useMemo(() => distinctItemNames(rawRows), [rawRows]);
  const rows = useMemo(() => filterByItemAndSearch(rawRows, itemFilter, searchText), [rawRows, itemFilter, searchText]);
  const compareRows = useMemo(
    () => filterByItemAndSearch(compareRawRows, itemFilter, searchText),
    [compareRawRows, itemFilter, searchText],
  );

  const rankedRows = useMemo(() => toRankedRows(rows, showLocationInLabel), [rows, showLocationInLabel]);

  const kpiItems = useMemo(() => {
    const totalDisposed = rows.reduce((sum, row) => sum + row.quantity, 0);
    const distinctItems = new Set(rows.map((row) => row.stockItemId)).size;
    const items_ = [
      { label: t('itemsDisposed', 'Items Disposed'), value: distinctItems },
      { label: t('totalDisposed', 'Total Quantity Disposed'), value: totalDisposed },
    ];
    if (!compare.enabled) {
      return items_;
    }
    const compareTotal = compareRows.reduce((sum, row) => sum + row.quantity, 0);
    const compareDistinctItems = new Set(compareRows.map((row) => row.stockItemId)).size;
    return items_.map((item, index) => ({
      ...item,
      compareValue: [compareDistinctItems, compareTotal][index],
      compareLabel: compare.comparison.label,
    }));
  }, [t, rows, compareRows, compare.enabled, compare.comparison.label]);

  const comparisonTableRows = useMemo<Array<ComparisonSummaryRow>>(() => {
    if (!compare.enabled) {
      return [];
    }
    const currentByLabel = new Map<string, number>(
      rows.map((row): [string, number] => [rowLabel(row, showLocationInLabel), row.quantity]),
    );
    const compareByLabel = new Map<string, number>(
      compareRows.map((row): [string, number] => [rowLabel(row, showLocationInLabel), row.quantity]),
    );
    const labelUnion = new Set([...currentByLabel.keys(), ...compareByLabel.keys()]);
    return Array.from(labelUnion)
      .map((label) => ({
        label,
        current: currentByLabel.get(label) ?? 0,
        compare: compareByLabel.get(label) ?? 0,
      }))
      .sort((a, b) => b.current - b.compare - (a.current - a.compare));
  }, [rows, compareRows, compare.enabled, showLocationInLabel]);

  const chartData = useMemo(() => rankedRows.slice(0, 10), [rankedRows]);

  const sortAccessors = useMemo(
    () => ({
      item: (row: StockLocationQtyRow) => row.itemName,
      location: (row: StockLocationQtyRow) => row.locationName ?? '',
      quantity: (row: StockLocationQtyRow) => row.quantity,
    }),
    [],
  );
  const { sortedRows, sortKey, direction, toggleSort } = useSortableRows(rows, sortAccessors, 'quantity', 'desc');

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockWastage', 'Stock Wastage'),
      headers: [
        t('item', 'Item'),
        ...(showLocationInLabel ? [t('location', 'Location')] : []),
        t('quantityDisposed', 'Quantity Disposed'),
        t('unit', 'Unit'),
      ],
      rows: rows.map((row) =>
        showLocationInLabel
          ? [row.itemName, row.locationName ?? '', row.quantity, row.unitName ?? '']
          : [row.itemName, row.quantity, row.unitName ?? ''],
      ),
    }),
    [t, rows, showLocationInLabel],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(() => {
    if (!compare.enabled) {
      return [];
    }
    return [
      buildComparisonExportSheet(
        comparisonTableRows,
        t('item', 'Item'),
        compare.primary.label,
        compare.comparison.label,
        t,
      ),
      buildKpiExportSheet(kpiItems, t),
    ];
  }, [t, compare.enabled, comparisonTableRows, compare.primary.label, compare.comparison.label, kpiItems]);

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  return (
    <div>
      <BackToReportsLink to="stock-reports-home" label={t('stockReports', 'Stock Reports')} />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockWastageReportTitle', 'Stock Wastage / Disposal Report')}</h2>

        {!dataLoading && <KpiTiles items={kpiItems} />}

        {!dataLoading && compare.enabled && (
          <>
            <h3 className={pageStyles.pageHeading}>
              {t('wastageComparisonHeading', 'Item disposal comparison: {{primary}} vs {{comparison}}', {
                primary: compare.primary.label,
                comparison: compare.comparison.label,
              })}
            </h3>
            <ComparisonSummaryTable
              rows={comparisonTableRows}
              rowLabel={t('item', 'Item')}
              currentLabel={compare.primary.label}
              compareLabel={compare.comparison.label}
              emptyMessage={t('noDataForEitherPeriod', 'No data found for either period.')}
            />
          </>
        )}

        <MonthCompareControls {...compare} />

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
          {!compare.enabled && (
            <>
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
            </>
          )}
        </div>

        <ExportButtons
          filenameBase="stock-wastage-report"
          mainSheet={mainExportSheet}
          extraSheets={exportExtraSheets}
          disabled={dataLoading}
        />

        <div className={pageStyles.viewSwitcher}>
          <ContentSwitcher
            size="md"
            selectedIndex={viewMode === 'table' ? 0 : 1}
            onChange={({ name }) => setViewMode(name as 'table' | 'graph')}
          >
            <Switch name="table" text={t('table', 'Table')} />
            <Switch name="graph" text={t('graphTop10', 'Graph (top 10)')} />
          </ContentSwitcher>
        </div>

        {dataLoading && <InlineLoading description={t('loadingReport', 'Loading report...')} />}

        {!dataLoading && viewMode === 'table' && (
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
                  {showLocationInLabel && (
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
                    label={t('quantityDisposed', 'Quantity Disposed')}
                    sortKey="quantity"
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
                    {showLocationInLabel && <td className="left">{row.locationName ?? '—'}</td>}
                    <td>{formatQuantity(row.quantity, row.unitName)}</td>
                  </tr>
                ))}
                {rows.length === 0 && (
                  <tr>
                    <td colSpan={showLocationInLabel ? 3 : 2} className={pageStyles.emptyState}>
                      {t('noDataForSelection', 'No data found for this selection.')}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}

        {!dataLoading && viewMode === 'graph' && (
          <SimpleBarChart data={chartData} emptyMessage={t('noDataForSelection', 'No data found for this selection.')} />
        )}
      </div>
    </div>
  );
}
