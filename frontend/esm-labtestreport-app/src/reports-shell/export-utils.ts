import type { ReactNode } from 'react';
import type { KpiTileDatum } from './kpi-tiles.component';
import type { ComparisonSummaryRow } from './comparison-summary-table.component';

export interface ExportSheet {
  name: string;
  headers: Array<string>;
  rows: Array<Array<string | number>>;
}

function toCell(value: ReactNode): string | number {
  return typeof value === 'number' ? value : String(value ?? '');
}

function triggerDownload(blob: Blob, filename: string) {
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

function escapeCsvCell(value: string | number): string {
  const text = String(value);
  return /[",\r\n]/.test(text) ? `"${text.replace(/"/g, '""')}"` : text;
}

export function downloadCsv(filenameBase: string, sheet: ExportSheet) {
  const lines = [sheet.headers, ...sheet.rows].map((row) => row.map(escapeCsvCell).join(','));
  // Leading BOM so Excel opens the CSV as UTF-8 rather than the system codepage.
  const blob = new Blob([`﻿${lines.join('\r\n')}`], { type: 'text/csv;charset=utf-8;' });
  triggerDownload(blob, `${filenameBase}.csv`);
}

function escapeHtml(value: string | number): string {
  return String(value).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function sheetToHtmlTable(sheet: ExportSheet): string {
  const headerRow = `<tr>${sheet.headers.map((header) => `<th>${escapeHtml(header)}</th>`).join('')}</tr>`;
  const bodyRows = sheet.rows
    .map((row) => `<tr>${row.map((cell) => `<td>${escapeHtml(cell)}</td>`).join('')}</tr>`)
    .join('');
  return `<h3>${escapeHtml(sheet.name)}</h3><table border="1">${headerRow}${bodyRows}</table>`;
}

export function downloadExcel(filenameBase: string, sheets: Array<ExportSheet>) {
  // Excel opens this legacy HTML-table format natively; no binary spreadsheet library required.
  const html = `<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><meta charset="utf-8"></head><body>${sheets
    .map(sheetToHtmlTable)
    .join('<br/>')}</body></html>`;
  const blob = new Blob([html], { type: 'application/vnd.ms-excel' });
  triggerDownload(blob, `${filenameBase}.xls`);
}

export type Translate = (key: string, defaultValue: string, options?: Record<string, unknown>) => string;

export function buildKpiExportSheet(items: Array<KpiTileDatum>, t: Translate): ExportSheet {
  return {
    name: t('kpiComparison', 'KPI Comparison'),
    headers: [t('metric', 'Metric'), t('current', 'Current'), t('comparison', 'Comparison'), t('delta', 'Δ')],
    rows: items.map((item) => {
      const delta =
        typeof item.value === 'number' && typeof item.compareValue === 'number' ? item.value - item.compareValue : '';
      return [item.label, toCell(item.value), item.compareValue !== undefined ? toCell(item.compareValue) : '', delta];
    }),
  };
}

export function buildComparisonExportSheet(
  rows: Array<ComparisonSummaryRow>,
  rowLabel: string,
  currentLabel: string,
  compareLabel: string,
  t: Translate,
): ExportSheet {
  return {
    name: t('rowLabelComparison', '{{rowLabel}} Comparison', { rowLabel }).slice(0, 31),
    headers: [rowLabel, currentLabel, compareLabel, t('delta', 'Δ'), t('deltaPercent', 'Δ %')],
    rows: rows.map((row) => {
      const delta = row.current - row.compare;
      const percent = row.compare !== 0 ? (delta / Math.abs(row.compare)) * 100 : null;
      return [row.label, row.current, row.compare, delta, percent === null ? '' : `${percent.toFixed(1)}%`];
    }),
  };
}
