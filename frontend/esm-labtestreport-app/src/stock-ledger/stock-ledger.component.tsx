import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, ContentSwitcher, InlineLoading, Switch } from '@carbon/react';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import MonthCompareControls from '../reports-shell/month-compare-controls.component';
import ComparisonSummaryTable from '../reports-shell/comparison-summary-table.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { buildKpiExportSheet, buildComparisonExportSheet, type ExportSheet } from '../reports-shell/export-utils';
import { useMonthComparison } from '../reports-shell/month-compare';
import { getTodayDateString, clampToToday } from '../reports-shell/date-utils';
import pageStyles from '../reports-shell/reports-page.scss';
import { useStockLedgerReport, type StockLedgerRow } from './stock-ledger.resource';

interface LedgerItem {
  stockItemId: number;
  itemName: string;
}

interface DayBlock {
  date: string;
  cells: Array<StockLedgerRow>;
}

function buildItemList(rows: Array<StockLedgerRow>): Array<LedgerItem> {
  const byId = new Map<number, string>();
  rows.forEach((row) => {
    if (!byId.has(row.stockItemId)) {
      byId.set(row.stockItemId, row.itemName);
    }
  });
  return Array.from(byId.entries())
    .map(([stockItemId, itemName]) => ({ stockItemId, itemName }))
    .sort((a, b) => a.itemName.localeCompare(b.itemName));
}

function buildDayBlocks(rows: Array<StockLedgerRow>, items: Array<LedgerItem>): Array<DayBlock> {
  const byItemAndDate = new Map<number, Map<string, StockLedgerRow>>();
  const allDates = new Set<string>();
  rows.forEach((row) => {
    if (!byItemAndDate.has(row.stockItemId)) {
      byItemAndDate.set(row.stockItemId, new Map());
    }
    byItemAndDate.get(row.stockItemId)!.set(row.ledgerDate, row);
    allDates.add(row.ledgerDate);
  });

  const lastRemaining = new Map<number, number>();
  items.forEach((item) => lastRemaining.set(item.stockItemId, 0));

  return Array.from(allDates)
    .sort()
    .map((date) => {
      const cells: Array<StockLedgerRow> = items.map((item) => {
        const byDate = byItemAndDate.get(item.stockItemId);
        const actualRow = byDate?.get(date);
        const opening = lastRemaining.get(item.stockItemId) ?? 0;
        if (actualRow) {
          lastRemaining.set(item.stockItemId, actualRow.remainingQty);
          return { ...actualRow, actualQty: opening };
        }
        return {
          stockItemId: item.stockItemId,
          itemName: item.itemName,
          ledgerDate: date,
          actualQty: opening,
          incomingQty: 0,
          outgoingQty: 0,
          remainingQty: opening,
        };
      });
      return { date, cells };
    })
    .filter((block) =>
      block.cells.some((cell) => cell.actualQty || cell.incomingQty || cell.outgoingQty || cell.remainingQty),
    );
}

export default function StockLedgerReport() {
  const { t } = useTranslation();
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');
  const compare = useMonthComparison();

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows, isLoading } = useStockLedgerReport(primaryStartDate, primaryEndDate);
  const { rows: compareRows, isLoading: compareLoading } = useStockLedgerReport(
    compare.comparison.startDate,
    compare.comparison.endDate,
    compare.enabled,
  );
  const dataLoading = isLoading || (compare.enabled && compareLoading);

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
      { label: t('itemsTracked', 'Items Tracked'), value: items.length },
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
  }, [t, items, dayBlocks, compareItems, compareDayBlocks, compare.enabled, compare.comparison.label]);

  const comparisonTableRows = useMemo(() => {
    if (!compare.enabled) {
      return [];
    }
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    const compareLatestBlock = compareDayBlocks[compareDayBlocks.length - 1];
    const currentByItem = new Map((latestBlock?.cells ?? []).map((cell) => [cell.stockItemId, cell.remainingQty]));
    const compareByItem = new Map(
      (compareLatestBlock?.cells ?? []).map((cell) => [cell.stockItemId, cell.remainingQty]),
    );
    const itemUnion = new Map<number, string>();
    items.forEach((item) => itemUnion.set(item.stockItemId, item.itemName));
    compareItems.forEach((item) => itemUnion.set(item.stockItemId, item.itemName));
    return Array.from(itemUnion.entries())
      .sort((a, b) => a[1].localeCompare(b[1]))
      .map(([stockItemId, itemName]) => ({
        label: itemName,
        current: currentByItem.get(stockItemId) ?? 0,
        compare: compareByItem.get(stockItemId) ?? 0,
      }));
  }, [items, compareItems, dayBlocks, compareDayBlocks, compare.enabled]);

  const chartData = useMemo(() => {
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    if (!latestBlock) {
      return [];
    }
    return latestBlock.cells.map((cell) => ({ label: cell.itemName, value: cell.remainingQty }));
  }, [dayBlocks]);

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('stockLedger', 'Stock Ledger'),
      headers: [
        t('date', 'Date'),
        ...items.flatMap((item) => [
          t('itemActualQty', '{{item}} - Actual Qty', { item: item.itemName }),
          t('itemIncoming', '{{item}} - Incoming', { item: item.itemName }),
          t('itemOutgoing', '{{item}} - Outgoing', { item: item.itemName }),
          t('itemRemaining', '{{item}} - Remaining', { item: item.itemName }),
        ]),
      ],
      rows: dayBlocks.map((block) => [
        block.date,
        ...block.cells.flatMap((cell) => [cell.actualQty, cell.incomingQty, cell.outgoingQty, cell.remainingQty]),
      ]),
    }),
    [t, items, dayBlocks],
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
      <ReportsTabs activeKey="stock-ledger" />
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
            selectedIndex={viewMode === 'table' ? 0 : 1}
            onChange={({ name }) => setViewMode(name as 'table' | 'graph')}
          >
            <Switch name="table" text={t('table', 'Table')} />
            <Switch name="graph" text={t('graphLatestRemaining', 'Graph (latest remaining)')} />
          </ContentSwitcher>
        </div>

        {dataLoading && <InlineLoading description={t('loadingReport', 'Loading report...')} />}

        {!dataLoading && viewMode === 'table' && (
          <div className={pageStyles.tableContainer}>
            <table className={pageStyles.dataTable}>
              <thead>
                <tr>
                  <th rowSpan={2} className="left">
                    {t('date', 'Date')}
                  </th>
                  {items.map((item) => (
                    <th key={item.stockItemId} colSpan={4}>
                      {item.itemName}
                    </th>
                  ))}
                </tr>
                <tr>
                  {items.map((item) => (
                    <React.Fragment key={item.stockItemId}>
                      <th>{t('actualQty', 'Actual Qty')}</th>
                      <th>{t('incoming', 'Incoming')}</th>
                      <th>{t('outgoing', 'Outgoing')}</th>
                      <th>{t('remaining', 'Remaining')}</th>
                    </React.Fragment>
                  ))}
                </tr>
              </thead>
              <tbody>
                {dayBlocks.map((block) => (
                  <tr key={block.date}>
                    <td className="left">{block.date}</td>
                    {block.cells.map((cell) => (
                      <React.Fragment key={cell.stockItemId}>
                        <td>{cell.actualQty}</td>
                        <td>{cell.incomingQty}</td>
                        <td>{cell.outgoingQty}</td>
                        <td>{cell.remainingQty}</td>
                      </React.Fragment>
                    ))}
                  </tr>
                ))}
                {dayBlocks.length === 0 && (
                  <tr>
                    <td colSpan={1 + items.length * 4} className={pageStyles.emptyState}>
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
