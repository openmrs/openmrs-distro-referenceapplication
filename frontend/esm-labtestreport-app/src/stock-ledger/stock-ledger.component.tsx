import React, { useMemo, useState } from 'react';
import { Button, ContentSwitcher, InlineLoading, Switch } from '@carbon/react';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
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
    });
}

export default function StockLedgerReport() {
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');

  const { rows, isLoading } = useStockLedgerReport(appliedDates.startDate, appliedDates.endDate);

  const items = useMemo(() => buildItemList(rows), [rows]);
  const dayBlocks = useMemo(() => buildDayBlocks(rows, items), [rows, items]);

  const kpiItems = useMemo(() => {
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    const totalRemaining = latestBlock ? latestBlock.cells.reduce((sum, cell) => sum + cell.remainingQty, 0) : 0;
    return [
      { label: 'Items Tracked', value: items.length },
      { label: 'Days in Range', value: dayBlocks.length },
      { label: 'Total Remaining (latest)', value: totalRemaining },
    ];
  }, [items, dayBlocks]);

  const chartData = useMemo(() => {
    const latestBlock = dayBlocks[dayBlocks.length - 1];
    if (!latestBlock) {
      return [];
    }
    return latestBlock.cells.map((cell) => ({ label: cell.itemName, value: cell.remainingQty }));
  }, [dayBlocks]);

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  return (
    <div>
      <ReportsTabs activeKey="stock-ledger" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>Stock Inventory Ledger Report</h2>

        {!isLoading && <KpiTiles items={kpiItems} />}

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField}>
            <label htmlFor="startDate">Start Date</label>
            <input
              id="startDate"
              type="date"
              value={startDateInput}
              onChange={(e) => setStartDateInput(e.target.value)}
            />
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="endDate">End Date</label>
            <input id="endDate" type="date" value={endDateInput} onChange={(e) => setEndDateInput(e.target.value)} />
          </div>
          <Button size="md" onClick={applyFilter}>
            Filter
          </Button>
        </div>

        <div className={pageStyles.viewSwitcher}>
          <ContentSwitcher
            size="md"
            selectedIndex={viewMode === 'table' ? 0 : 1}
            onChange={({ name }) => setViewMode(name as 'table' | 'graph')}
          >
            <Switch name="table" text="Table" />
            <Switch name="graph" text="Graph (latest remaining)" />
          </ContentSwitcher>
        </div>

        {isLoading && <InlineLoading description="Loading report..." />}

        {!isLoading && viewMode === 'table' && (
          <div className={pageStyles.tableContainer}>
            <table className={pageStyles.dataTable}>
              <thead>
                <tr>
                  <th rowSpan={2} className="left">
                    Date
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
                      <th>Actual Qty</th>
                      <th>Incoming</th>
                      <th>Outgoing</th>
                      <th>Remaining</th>
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
                      No data found for this selection.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}

        {!isLoading && viewMode === 'graph' && (
          <SimpleBarChart data={chartData} emptyMessage="No data found for this selection." />
        )}
      </div>
    </div>
  );
}
