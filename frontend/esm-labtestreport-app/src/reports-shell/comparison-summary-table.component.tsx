import React from 'react';
import { useTranslation } from 'react-i18next';
import pageStyles from './reports-page.scss';

export interface ComparisonSummaryRow {
  label: string;
  current: number;
  compare: number;
}

interface ComparisonSummaryTableProps {
  rows: Array<ComparisonSummaryRow>;
  rowLabel: string;
  currentLabel: string;
  compareLabel: string;
  emptyMessage?: string;
}

function formatDelta(delta: number): string {
  return delta > 0 ? `+${delta}` : String(delta);
}

export default function ComparisonSummaryTable({
  rows,
  rowLabel,
  currentLabel,
  compareLabel,
  emptyMessage,
}: ComparisonSummaryTableProps) {
  const { t } = useTranslation();
  const resolvedEmptyMessage = emptyMessage ?? t('noDataToCompare', 'No data to compare.');
  const totalCurrent = rows.reduce((sum, row) => sum + row.current, 0);
  const totalCompare = rows.reduce((sum, row) => sum + row.compare, 0);
  const totalDelta = totalCurrent - totalCompare;

  return (
    <div className={pageStyles.tableContainer}>
      <table className={pageStyles.dataTable}>
        <thead>
          <tr>
            <th className="left">{rowLabel}</th>
            <th>{currentLabel}</th>
            <th>{compareLabel}</th>
            <th>{t('delta', 'Δ')}</th>
            <th>{t('deltaPercent', 'Δ %')}</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row) => {
            const delta = row.current - row.compare;
            const percent = row.compare !== 0 ? (delta / Math.abs(row.compare)) * 100 : null;
            return (
              <tr key={row.label}>
                <td className="left">{row.label}</td>
                <td>{row.current}</td>
                <td>{row.compare}</td>
                <td>{formatDelta(delta)}</td>
                <td>{percent === null ? '—' : `${percent > 0 ? '+' : ''}${percent.toFixed(1)}%`}</td>
              </tr>
            );
          })}
          {rows.length === 0 && (
            <tr>
              <td colSpan={5} className={pageStyles.emptyState}>
                {resolvedEmptyMessage}
              </td>
            </tr>
          )}
        </tbody>
        {rows.length > 0 && (
          <tfoot>
            <tr>
              <td className="left">
                <strong>{t('total', 'Total')}</strong>
              </td>
              <td>
                <strong>{totalCurrent}</strong>
              </td>
              <td>
                <strong>{totalCompare}</strong>
              </td>
              <td>
                <strong>{formatDelta(totalDelta)}</strong>
              </td>
              <td />
            </tr>
          </tfoot>
        )}
      </table>
    </div>
  );
}
