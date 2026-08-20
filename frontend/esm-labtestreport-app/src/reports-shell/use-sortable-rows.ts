import { useMemo, useState } from 'react';

export type SortDirection = 'asc' | 'desc';

export type SortAccessors<T> = Record<string, (row: T) => string | number | null>;

/**
 * Generic click-to-sort helper for flat report tables. Pass one accessor per sortable column;
 * clicking the same column again flips direction, clicking a different column starts descending
 * (most report tables are ranked "biggest/most urgent first" by default).
 */
export function useSortableRows<T>(
  rows: Array<T>,
  accessors: SortAccessors<T>,
  defaultSortKey: string | null = null,
  defaultDirection: SortDirection = 'desc',
) {
  const [sortKey, setSortKey] = useState<string | null>(defaultSortKey);
  const [direction, setDirection] = useState<SortDirection>(defaultDirection);

  function toggleSort(key: string) {
    if (sortKey === key) {
      setDirection((prev) => (prev === 'asc' ? 'desc' : 'asc'));
    } else {
      setSortKey(key);
      setDirection('desc');
    }
  }

  const sortedRows = useMemo(() => {
    const accessor = sortKey ? accessors[sortKey] : null;
    if (!accessor) {
      return rows;
    }
    const withIndex = rows.map((row, index) => ({ row, index, value: accessor(row) }));
    withIndex.sort((a, b) => {
      if (a.value === null && b.value === null) return a.index - b.index;
      if (a.value === null) return 1;
      if (b.value === null) return -1;
      const comparison =
        typeof a.value === 'number' && typeof b.value === 'number'
          ? a.value - b.value
          : String(a.value).localeCompare(String(b.value));
      return direction === 'asc' ? comparison : -comparison;
    });
    return withIndex.map((entry) => entry.row);
  }, [rows, accessors, sortKey, direction]);

  return { sortedRows, sortKey, direction, toggleSort };
}
