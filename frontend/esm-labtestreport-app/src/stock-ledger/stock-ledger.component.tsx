import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, ContentSwitcher, InlineLoading, Search, Select, SelectItem, Switch } from '@carbon/react';
import { ChevronDown, ChevronRight } from '@carbon/react/icons';
import { useLocations } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import SimpleLineChart from '../reports-shell/simple-line-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import MonthCompareControls from '../reports-shell/month-compare-controls.component';
import ComparisonSummaryTable from '../reports-shell/comparison-summary-table.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { buildKpiExportSheet, buildComparisonExportSheet, type ExportSheet } from '../reports-shell/export-utils';
import { useMonthComparison } from '../reports-shell/month-compare';
import { getTodayDateString, clampToToday } from '../reports-shell/date-utils';
import { formatQuantity } from '../reports-shell/format-quantity';
import pageStyles from '../reports-shell/reports-page.scss';
import { useStockLedgerReport, type StockLedgerRow } from './stock-ledger.resource';

interface LedgerItem {
  key: string;
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
}

interface DayBlock {
  date: string;
  cells: Array<StockLedgerRow>;
}

function itemLocationKey(stockItemId: number, locationId: number): string {
  return `${stockItemId}-${locationId}`;
}

function itemLocationLabel(itemName: string, locationName: string | null, showLocation: boolean): string {
  return showLocation ? `${itemName} — ${locationName ?? '?'}` : itemName;
}

function buildItemList(rows: Array<StockLedgerRow>): Array<LedgerItem> {
  const byKey = new Map<string, LedgerItem>();
  rows.forEach((row) => {
    const key = itemLocationKey(row.stockItemId, row.locationId);
    if (!byKey.has(key)) {
      byKey.set(key, {
        key,
        stockItemId: row.stockItemId,
        itemName: row.itemName,
        locationId: row.locationId,
        locationName: row.locationName,
      });
    }
  });
  return Array.from(byKey.values()).sort(
    (a, b) => a.itemName.localeCompare(b.itemName) || (a.locationName ?? '').localeCompare(b.locationName ?? ''),
  );
}

function buildDayBlocks(rows: Array<StockLedgerRow>, items: Array<LedgerItem>): Array<DayBlock> {
  const byItemAndDate = new Map<string, Map<string, StockLedgerRow>>();
  const unitNameByItem = new Map<string, string | null>();
  const allDates = new Set<string>();
  rows.forEach((row) => {
    const key = itemLocationKey(row.stockItemId, row.locationId);
    if (!byItemAndDate.has(key)) {
      byItemAndDate.set(key, new Map());
    }
    byItemAndDate.get(key)!.set(row.ledgerDate, row);
    unitNameByItem.set(key, row.unitName);
    allDates.add(row.ledgerDate);
  });

  const lastRemaining = new Map<string, number>();
  items.forEach((item) => lastRemaining.set(item.key, 0));

  return Array.from(allDates)
    .sort()
    .map((date) => {
      const cells: Array<StockLedgerRow> = items.map((item) => {
        const byDate = byItemAndDate.get(item.key);
        const actualRow = byDate?.get(date);
        const opening = lastRemaining.get(item.key) ?? 0;
        if (actualRow) {
          lastRemaining.set(item.key, actualRow.remainingQty);
          return { ...actualRow, actualQty: opening };
        }
        return {
          stockItemId: item.stockItemId,
          itemName: item.itemName,
          locationId: item.locationId,
          locationName: item.locationName,
          ledgerDate: date,
          actualQty: opening,
          incomingQty: 0,
          outgoingQty: 0,
          remainingQty: opening,
          unitName: unitNameByItem.get(item.key) ?? null,
        };
      });
      return { date, cells };
    })
    .filter((block) =>
      block.cells.some((cell) => cell.actualQty || cell.incomingQty || cell.outgoingQty || cell.remainingQty),
    );
}

function filterRows(rows: Array<StockLedgerRow>, itemFilter: string, searchText: string): Array<StockLedgerRow> {
  const search = searchText.trim().toLowerCase();
  return rows.filter((row) => {
    if (itemFilter && row.itemName !== itemFilter) {
      return false;
    }
    if (!search) {
      return true;
    }
    return row.itemName.toLowerCase().includes(search) || (row.locationName ?? '').toLowerCase().includes(search);
  });
}

function buildFlatRows(dayBlocks: Array<DayBlock>): Array<StockLedgerRow> {
  return [...dayBlocks]
    .flatMap((block) => block.cells)
    .sort(
      (a, b) =>
        a.itemName.localeCompare(b.itemName) ||
        (a.locationName ?? '').localeCompare(b.locationName ?? '') ||
        a.ledgerDate.localeCompare(b.ledgerDate),
    );
}

interface LedgerGroup {
  key: string;
  itemName: string;
  locationName: string | null;
  rows: Array<StockLedgerRow>;
  totalIncoming: number;
  totalOutgoing: number;
  latestRemaining: number;
  unitName: string | null;
}

function buildGroupedRows(items: Array<LedgerItem>, flatRows: Array<StockLedgerRow>): Array<LedgerGroup> {
  return items.map((item) => {
    const rows = flatRows.filter((row) => itemLocationKey(row.stockItemId, row.locationId) === item.key);
    return {
      key: item.key,
      itemName: item.itemName,
      locationName: item.locationName,
      rows,
      totalIncoming: rows.reduce((sum, row) => sum + row.incomingQty, 0),
      totalOutgoing: rows.reduce((sum, row) => sum + row.outgoingQty, 0),
      latestRemaining: rows.length > 0 ? rows[rows.length - 1].remainingQty : 0,
      unitName: rows.length > 0 ? rows[0].unitName : null,
    };
  });
}

const STOCK_LOCATION_TAG = 'Login Location';

export default function StockLedgerReport() {
  const { t } = useTranslation();
  const locations = useLocations(STOCK_LOCATION_TAG);
  const [locationUuid, setLocationUuid] = useState('');
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [viewMode, setViewMode] = useState<'table' | 'graph' | 'trend'>('table');
  const [trendItemKey, setTrendItemKey] = useState<string | null>(null);
  const [itemFilter, setItemFilter] = useState('');
  const [searchText, setSearchText] = useState('');
  const [expandedGroups, setExpandedGroups] = useState<Set<string>>(new Set());
  const compare = useMonthComparison();
  const showLocationColumn = !locationUuid;

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows: rawRows, isLoading } = useStockLedgerReport(primaryStartDate, primaryEndDate, locationUuid || undefined);
  const { rows: compareRawRows, isLoading: compareLoading } = useStockLedgerReport(
    compare.comparison.startDate,
    compare.comparison.endDate,
    locationUuid || undefined,
    compare.enabled,
  );
  const dataLoading = isLoading || (compare.enabled && compareLoading);

  const itemOptions = useMemo(
    () => Array.from(new Set(rawRows.map((row) => row.itemName))).sort((a, b) => a.localeCompare(b)),
    [rawRows],
  );

  const rows = useMemo(() => filterRows(rawRows, itemFilter, searchText), [rawRows, itemFilter, searchText]);
  const compareRows = useMemo(
    () => filterRows(compareRawRows, itemFilter, searchText),
    [compareRawRows, itemFilter, searchText],
  );

  const items = useMemo(() => buildItemList(rows), [rows]);
  const dayBlocks = useMemo(() => buildDayBlocks(rows, items), [rows, items]);

  const compareItems = useMemo(() => buildItemList(compareRows), [compareRows]);
  const compareDayBlocks = useMemo(
    () => buildDayBlocks(compareRows, compareItems),
    [compareRows, compareItems],
  );

  const kpiItems = useMemo(() => {
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    const totalRemaining = latestBlock ? latestBlock.cells.reduce((sum, cell) => sum + cell.remainingQty, 0) : 0;
    const items_ = [
      {
        label: showLocationColumn ? t('itemLocationPairs', 'Item/Location Pairs') : t('itemsTracked', 'Items Tracked'),
        value: items.length,
      },
      { label: t('daysInRange', 'Days in Range'), value: dayBlocks.length },
      { label: t('totalRemainingLatest', 'Total Remaining (latest)'), value: totalRemaining },
    ];
    if (!compare.enabled) {
      return items_;
    }
    const compareLatestBlock = compareDayBlocks[compareDayBlocks.length - 1];
    const compareTotalRemaining = compareLatestBlock
      ? compareLatestBlock.cells.reduce((sum, cell) => sum + cell.remainingQty, 0)
      : 0;
    const compareValues = [compareItems.length, compareDayBlocks.length, compareTotalRemaining];
    return items_.map((item, index) => ({
      ...item,
      compareValue: compareValues[index],
      compareLabel: compare.comparison.label,
    }));
  }, [t, items, dayBlocks, compareItems, compareDayBlocks, compare.enabled, compare.comparison.label, showLocationColumn]);

  const comparisonTableRows = useMemo(() => {
    if (!compare.enabled) {
      return [];
    }
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    const compareLatestBlock = compareDayBlocks[compareDayBlocks.length - 1];
    const currentByKey = new Map(
      (latestBlock?.cells ?? []).map((cell): [string, number] => [
        itemLocationKey(cell.stockItemId, cell.locationId),
        cell.remainingQty,
      ]),
    );
    const compareByKey = new Map(
      (compareLatestBlock?.cells ?? []).map((cell): [string, number] => [
        itemLocationKey(cell.stockItemId, cell.locationId),
        cell.remainingQty,
      ]),
    );
    const itemUnion = new Map<string, string>();
    items.forEach((item) => itemUnion.set(item.key, itemLocationLabel(item.itemName, item.locationName, showLocationColumn)));
    compareItems.forEach((item) =>
      itemUnion.set(item.key, itemLocationLabel(item.itemName, item.locationName, showLocationColumn)),
    );
    return Array.from(itemUnion.entries())
      .sort((a, b) => a[1].localeCompare(b[1]))
      .map(([key, label]) => ({
        label,
        current: currentByKey.get(key) ?? 0,
        compare: compareByKey.get(key) ?? 0,
      }));
  }, [items, compareItems, dayBlocks, compareDayBlocks, compare.enabled, showLocationColumn]);

  const chartData = useMemo(() => {
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    if (!latestBlock) {
      return [];
    }
    return latestBlock.cells.map((cell) => ({
      label: itemLocationLabel(cell.itemName, cell.locationName, showLocationColumn),
      value: cell.remainingQty,
    }));
  }, [dayBlocks, showLocationColumn]);

  const flatRows = useMemo(() => buildFlatRows(dayBlocks), [dayBlocks]);
  const groupedRows = useMemo(() => buildGroupedRows(items, flatRows), [items, flatRows]);

  const effectiveTrendItemKey = trendItemKey ?? items[0]?.key ?? null;

  const trendData = useMemo(() => {
    if (effectiveTrendItemKey === null) {
      return [];
    }
    return dayBlocks
      .map((block) => {
        const cell = block.cells.find((c) => itemLocationKey(c.stockItemId, c.locationId) === effectiveTrendItemKey);
        return cell ? { date: block.date, value: cell.remainingQty } : null;
      })
      .filter((point): point is { date: string; value: number } => point !== null);
  }, [dayBlocks, effectiveTrendItemKey]);

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockLedger', 'Stock Ledger'),
      headers: [
        t('item', 'Item'),
        ...(showLocationColumn ? [t('location', 'Location')] : []),
        t('date', 'Date'),
        t('openingBalance', 'Opening Balance'),
        t('incoming', 'Incoming'),
        t('outgoing', 'Outgoing'),
        t('balanceOnStock', 'Balance on Stock'),
        t('unit', 'Unit'),
      ],
      rows: flatRows.map((row) => [
        row.itemName,
        ...(showLocationColumn ? [row.locationName ?? ''] : []),
        row.ledgerDate,
        row.actualQty,
        row.incomingQty,
        row.outgoingQty,
        row.remainingQty,
        row.unitName ?? '',
      ]),
    }),
    [t, flatRows, showLocationColumn],
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

  function toggleGroup(key: string) {
    setExpandedGroups((prev) => {
      const next = new Set(prev);
      if (next.has(key)) {
        next.delete(key);
      } else {
        next.add(key);
      }
      return next;
    });
  }

  return (
    <div>
      <BackToReportsLink to="stock-reports-home" label={t('stockReports', 'Stock Reports')} />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('stockLedgerReportTitle', 'Stock Inventory Ledger Report')}</h2>

        {!dataLoading && <KpiTiles items={kpiItems} />}

        {!dataLoading && compare.enabled && (
          <>
            <h3 className={pageStyles.pageHeading}>
              {t(
                'itemComparisonHeading',
                'Item comparison (latest remaining): {{primary}} vs {{comparison}}',
                { primary: compare.primary.label, comparison: compare.comparison.label },
              )}
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
        </div>

        {!compare.enabled && (
          <div className={pageStyles.filterTile}>
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
        )}

        <ExportButtons
          filenameBase="stock-ledger-report"
          mainSheet={mainExportSheet}
          extraSheets={exportExtraSheets}
          disabled={dataLoading}
        />

        <div className={pageStyles.viewSwitcher}>
          <ContentSwitcher
            size="md"
            selectedIndex={viewMode === 'table' ? 0 : viewMode === 'graph' ? 1 : 2}
            onChange={({ name }) => setViewMode(name as 'table' | 'graph' | 'trend')}
          >
            <Switch name="table" text={t('table', 'Table')} />
            <Switch name="graph" text={t('graphLatestRemaining', 'Graph (latest remaining)')} />
            <Switch name="trend" text={t('trendByItem', 'Trend (by item)')} />
          </ContentSwitcher>
        </div>

        {dataLoading && <InlineLoading description={t('loadingReport', 'Loading report...')} />}

        {!dataLoading && viewMode === 'table' && (
          <div className={pageStyles.tableContainer}>
            <table className={pageStyles.dataTable}>
              <thead>
                <tr>
                  <th className="left">{t('item', 'Item')}</th>
                  {showLocationColumn && <th className="left">{t('location', 'Location')}</th>}
                  <th className="left">{t('date', 'Date')}</th>
                  <th>{t('openingBalance', 'Opening Balance')}</th>
                  <th>{t('incoming', 'Incoming')}</th>
                  <th>{t('outgoing', 'Outgoing')}</th>
                  <th>{t('balanceOnStock', 'Balance on Stock')}</th>
                </tr>
              </thead>
              <tbody>
                {groupedRows.map((group) => {
                  const expanded = expandedGroups.has(group.key);
                  const unitName = group.unitName;
                  return (
                    <React.Fragment key={group.key}>
                      <tr className={pageStyles.categoryHeaderRow} onClick={() => toggleGroup(group.key)}>
                        <td colSpan={showLocationColumn ? 3 : 2} className="left">
                          <button className={pageStyles.collapseToggle} aria-expanded={expanded}>
                            {expanded ? <ChevronDown size={16} /> : <ChevronRight size={16} />}
                            {itemLocationLabel(group.itemName, group.locationName, showLocationColumn)} (
                            {t('nDays', '{{count}} days', { count: group.rows.length })})
                          </button>
                        </td>
                        <td>{'—'}</td>
                        <td>{formatQuantity(group.totalIncoming, unitName)}</td>
                        <td>{formatQuantity(group.totalOutgoing, unitName)}</td>
                        <td>
                          <strong>{formatQuantity(group.latestRemaining, unitName)}</strong>
                        </td>
                      </tr>
                      {expanded &&
                        group.rows.map((row) => (
                          <tr key={`${row.stockItemId}-${row.locationId}-${row.ledgerDate}`}>
                            <td className="left" />
                            {showLocationColumn && <td className="left" />}
                            <td className="left">{row.ledgerDate}</td>
                            <td>{formatQuantity(row.actualQty, row.unitName)}</td>
                            <td>{formatQuantity(row.incomingQty, row.unitName)}</td>
                            <td>{formatQuantity(row.outgoingQty, row.unitName)}</td>
                            <td>{formatQuantity(row.remainingQty, row.unitName)}</td>
                          </tr>
                        ))}
                    </React.Fragment>
                  );
                })}
                {groupedRows.length === 0 && (
                  <tr>
                    <td colSpan={showLocationColumn ? 7 : 6} className={pageStyles.emptyState}>
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

        {!dataLoading && viewMode === 'trend' && (
          <div>
            <div className={pageStyles.filterTile}>
              <div className={pageStyles.filterField}>
                <Select
                  id="trendItemFilter"
                  labelText={t('item', 'Item')}
                  value={effectiveTrendItemKey ?? ''}
                  onChange={(e) => setTrendItemKey(e.target.value)}
                >
                  {items.map((item) => (
                    <React.Fragment key={item.key}>
                      <SelectItem value={item.key} text={itemLocationLabel(item.itemName, item.locationName, showLocationColumn)} />
                    </React.Fragment>
                  ))}
                </Select>
              </div>
            </div>
            <SimpleLineChart data={trendData} emptyMessage={t('noDataForSelection', 'No data found for this selection.')} />
          </div>
        )}
      </div>
    </div>
  );
}
