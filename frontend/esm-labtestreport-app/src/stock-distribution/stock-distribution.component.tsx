import React, { useEffect, useMemo, useState } from 'react';
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
import { useStockDistributionReport, type StockLocationQtyRow } from './stock-distribution.resource';

const STOCK_LOCATION_TAG = 'Login Location';

function rowLabel(row: StockLocationQtyRow): string {
  return `${row.itemName} — ${row.locationName ?? '?'}`;
}

export default function StockDistributionReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [sourceLocationUuid, setSourceLocationUuid] = useState('');
  const [hasDefaultedSource, setHasDefaultedSource] = useState(false);
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const compare = useMonthComparison();

  // Distribution is almost always "from the central Main Store", so default the filter to
  // whichever location is literally named that, once locations have loaded, rather than
  // making every user re-select it.
  useEffect(() => {
    if (hasDefaultedSource || !locations?.length) {
      return;
    }
    const mainStore = locations.find((location) => location.display.trim().toLowerCase() === 'main store');
    if (mainStore) {
      setSourceLocationUuid(mainStore.uuid);
    }
    setHasDefaultedSource(true);
  }, [locations, hasDefaultedSource]);

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows: rawRows, isLoading } = useStockDistributionReport(
    primaryStartDate,
    primaryEndDate,
    sourceLocationUuid || undefined,
  );
  const { rows: compareRawRows, isLoading: compareLoading } = useStockDistributionReport(
    compare.comparison.startDate,
    compare.comparison.endDate,
    sourceLocationUuid || undefined,
    compare.enabled,
  );
  const dataLoading = isLoading || (compare.enabled && compareLoading);

  const itemOptions = useMemo(() => distinctItemNames(rawRows), [rawRows]);
  const rows = useMemo(() => filterByItemAndSearch(rawRows, itemFilter, searchText), [rawRows, itemFilter, searchText]);
  const compareRows = useMemo(
    () => filterByItemAndSearch(compareRawRows, itemFilter, searchText),
    [compareRawRows, itemFilter, searchText],
  );

  const rankedRows = useMemo(
    () =>
      [...rows]
        .map((row) => ({ label: rowLabel(row), value: row.quantity }))
        .sort((a, b) => b.value - a.value),
    [rows],
  );

  const kpiItems = useMemo(() => {
    const totalDistributed = rows.reduce((sum, row) => sum + row.quantity, 0);
    const destinationCount = new Set(rows.map((row) => row.locationId)).size;
    const items_ = [
      { label: t('destinationsReached', 'Destinations Reached'), value: destinationCount },
      { label: t('totalDistributed', 'Total Quantity Distributed'), value: totalDistributed },
    ];
    if (!compare.enabled) {
      return items_;
    }
    const compareTotal = compareRows.reduce((sum, row) => sum + row.quantity, 0);
    const compareDestinationCount = new Set(compareRows.map((row) => row.locationId)).size;
    return items_.map((item, index) => ({
      ...item,
      compareValue: [compareDestinationCount, compareTotal][index],
      compareLabel: compare.comparison.label,
    }));
  }, [t, rows, compareRows, compare.enabled, compare.comparison.label]);

  const comparisonTableRows = useMemo<Array<ComparisonSummaryRow>>(() => {
    if (!compare.enabled) {
      return [];
    }
    const currentByLabel = new Map<string, number>(rows.map((row): [string, number] => [rowLabel(row), row.quantity]));
    const compareByLabel = new Map<string, number>(
      compareRows.map((row): [string, number] => [rowLabel(row), row.quantity]),
    );
    const labelUnion = new Set([...currentByLabel.keys(), ...compareByLabel.keys()]);
    return Array.from(labelUnion)
      .map((label) => ({
        label,
        current: currentByLabel.get(label) ?? 0,
        compare: compareByLabel.get(label) ?? 0,
      }))
      .sort((a, b) => b.current - b.compare - (a.current - a.compare));
  }, [rows, compareRows, compare.enabled]);

  const chartData = useMemo(() => rankedRows.slice(0, 10), [rankedRows]);

  const sortAccessors = useMemo(
    () => ({
      item: (row: StockLocationQtyRow) => row.itemName,
      source: (row: StockLocationQtyRow) => row.sourceLocationName ?? '',
      location: (row: StockLocationQtyRow) => row.locationName ?? '',
      quantity: (row: StockLocationQtyRow) => row.quantity,
    }),
    [],
  );
  const { sortedRows, sortKey, direction, toggleSort } = useSortableRows(rows, sortAccessors, 'quantity', 'desc');

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockDistribution', 'Stock Distribution'),
      headers: [
        t('item', 'Item'),
        t('sourceLocation', 'Source Location'),
        t('destinationLocation', 'Destination Location'),
        t('quantitySent', 'Quantity Sent'),
        t('unit', 'Unit'),
      ],
      rows: rows.map((row) => [
        row.itemName,
        row.sourceLocationName ?? '',
        row.locationName ?? '',
        row.quantity,
        row.unitName ?? '',
      ]),
    }),
    [t, rows],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(() => {
    if (!compare.enabled) {
      return [];
    }
    return [
      buildComparisonExportSheet(
        comparisonTableRows,
        t('itemAndDestination', 'Item / Destination'),
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
        <h2 className={pageStyles.pageHeading}>{t('stockDistributionReportTitle', 'Stock Distribution Report')}</h2>

        {!dataLoading && <KpiTiles items={kpiItems} />}

        {!dataLoading && compare.enabled && (
          <>
            <h3 className={pageStyles.pageHeading}>
              {t('distributionComparisonHeading', 'Distribution comparison: {{primary}} vs {{comparison}}', {
                primary: compare.primary.label,
                comparison: compare.comparison.label,
              })}
            </h3>
            <ComparisonSummaryTable
              rows={comparisonTableRows}
              rowLabel={t('itemAndDestination', 'Item / Destination')}
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
              id="sourceLocationFilter"
              labelText={t('sourceLocation', 'Source location')}
              value={sourceLocationUuid}
              onChange={(e) => setSourceLocationUuid(e.target.value)}
            >
              <SelectItem value="" text={t('allSources', 'All sources')} />
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
          filenameBase="stock-distribution-report"
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
                  <SortableHeader
                    label={t('sourceLocation', 'Source Location')}
                    sortKey="source"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                    className="left"
                  />
                  <SortableHeader
                    label={t('destinationLocation', 'Destination Location')}
                    sortKey="location"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                    className="left"
                  />
                  <SortableHeader
                    label={t('quantitySent', 'Quantity Sent')}
                    sortKey="quantity"
                    activeSortKey={sortKey}
                    direction={direction}
                    onSort={toggleSort}
                  />
                </tr>
              </thead>
              <tbody>
                {sortedRows.map((row) => (
                  <tr key={`${row.stockItemId}-${row.locationId}-${row.sourceLocationName ?? ''}`}>
                    <td className="left">{row.itemName}</td>
                    <td className="left">{row.sourceLocationName ?? '—'}</td>
                    <td className="left">{row.locationName ?? '—'}</td>
                    <td>{formatQuantity(row.quantity, row.unitName)}</td>
                  </tr>
                ))}
                {rows.length === 0 && (
                  <tr>
                    <td colSpan={4} className={pageStyles.emptyState}>
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
