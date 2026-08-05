import type { ReactNode } from 'react';
import * as XLSX from 'xlsx';
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

// Excel sheet names can't exceed 31 characters or contain : \ / ? * [ ], and must be unique
// within the workbook.
function sanitizeSheetName(name: string, usedNames: Set<string>): string {
  const stripped = name.replace(/[:\\/?*[\]]/g, '').slice(0, 31) || 'Sheet';
  let candidate = stripped;
  let suffix = 2;
  while (usedNames.has(candidate)) {
    const suffixText = ` (${suffix})`;
    candidate = stripped.slice(0, 31 - suffixText.length) + suffixText;
    suffix++;
  }
  usedNames.add(candidate);
  return candidate;
}

export function downloadExcel(filenameBase: string, sheets: Array<ExportSheet>) {
  const workbook = XLSX.utils.book_new();
  const usedSheetNames = new Set<string>();
  for (const sheet of sheets) {
    const worksheet = XLSX.utils.aoa_to_sheet([sheet.headers, ...sheet.rows]);
    XLSX.utils.book_append_sheet(workbook, worksheet, sanitizeSheetName(sheet.name, usedSheetNames));
  }
  const buffer: ArrayBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
  const blob = new Blob([buffer], {
    type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
  });
  triggerDownload(blob, `${filenameBase}.xlsx`);
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

export interface VisitDetailForExport {
  patientId: number;
  givenName: string;
  familyName: string;
  visitDate: string;
  locationName: string;
  providerName: string;
}

/**
 * Wide-format detail sheet: one row per patient, with a Date/Location/Provider column triplet
 * for each visit, padded with blanks up to the most visits any one patient has.
 */
export function buildVisitDetailExportSheet(details: Array<VisitDetailForExport>, t: Translate): ExportSheet {
  const byPatient = new Map<number, Array<VisitDetailForExport>>();
  for (const detail of details) {
    const existing = byPatient.get(detail.patientId);
    if (existing) {
      existing.push(detail);
    } else {
      byPatient.set(detail.patientId, [detail]);
    }
  }

  const maxVisits = Math.max(0, ...Array.from(byPatient.values(), (visits) => visits.length));

  const headers = [t('givenName', 'Given Name'), t('familyName', 'Family Name')];
  for (let i = 1; i <= maxVisits; i++) {
    headers.push(
      t('visitNDate', 'Visit {{n}} Date', { n: i }),
      t('visitNLocation', 'Visit {{n}} Location', { n: i }),
      t('visitNProvider', 'Visit {{n}} Provider', { n: i }),
    );
  }

  const rows = Array.from(byPatient.values()).map((visits) => {
    const row: Array<string | number> = [visits[0].givenName, visits[0].familyName];
    for (let i = 0; i < maxVisits; i++) {
      const visit = visits[i];
      row.push(visit?.visitDate ?? '', visit?.locationName ?? '', visit?.providerName ?? '');
    }
    return row;
  });

  return {
    name: t('visitDetails', 'Visit Details'),
    headers,
    rows,
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
