import { useMemo, useState } from 'react';

export interface MonthRange {
  month: string;
  label: string;
  startDate: string;
  endDate: string;
}

function pad(value: number): string {
  return String(value).padStart(2, '0');
}

export function monthToValue(year: number, monthIndex: number): string {
  return `${year}-${pad(monthIndex + 1)}`;
}

export function currentMonthValue(): string {
  const now = new Date();
  return monthToValue(now.getFullYear(), now.getMonth());
}

export function previousMonthValue(monthValue: string): string {
  const [year, month] = monthValue.split('-').map(Number);
  const date = new Date(year, month - 1, 1);
  date.setMonth(date.getMonth() - 1);
  return monthToValue(date.getFullYear(), date.getMonth());
}

export function toMonthRange(monthValue: string): MonthRange {
  const [year, month] = monthValue.split('-').map(Number);
  const lastDay = new Date(year, month, 0).getDate();
  const label = new Date(year, month - 1, 1).toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
  return {
    month: monthValue,
    label,
    startDate: `${monthValue}-01`,
    endDate: `${monthValue}-${pad(lastDay)}`,
  };
}

export interface MonthComparison {
  enabled: boolean;
  setEnabled: (enabled: boolean) => void;
  primaryMonth: string;
  setPrimaryMonth: (month: string) => void;
  comparisonMonth: string;
  setComparisonMonth: (month: string) => void;
  primary: MonthRange;
  comparison: MonthRange;
}

export function useMonthComparison(): MonthComparison {
  const [enabled, setEnabled] = useState(false);
  const [primaryMonth, setPrimaryMonth] = useState<string>(currentMonthValue());
  const [comparisonMonth, setComparisonMonth] = useState<string>(() => previousMonthValue(currentMonthValue()));

  const primary = useMemo(() => toMonthRange(primaryMonth), [primaryMonth]);
  const comparison = useMemo(() => toMonthRange(comparisonMonth), [comparisonMonth]);

  return {
    enabled,
    setEnabled,
    primaryMonth,
    setPrimaryMonth,
    comparisonMonth,
    setComparisonMonth,
    primary,
    comparison,
  };
}
