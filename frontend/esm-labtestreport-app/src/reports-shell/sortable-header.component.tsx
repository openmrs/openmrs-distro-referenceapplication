import React from 'react';
import { ArrowUp, ArrowDown, ChevronSort } from '@carbon/react/icons';
import type { SortDirection } from './use-sortable-rows';
import styles from './sortable-header.scss';

interface SortableHeaderProps {
  label: string;
  sortKey: string;
  activeSortKey: string | null;
  direction: SortDirection;
  onSort: (key: string) => void;
  className?: string;
}

export default function SortableHeader({ label, sortKey, activeSortKey, direction, onSort, className }: SortableHeaderProps) {
  const isActive = activeSortKey === sortKey;
  return (
    <th className={className}>
      <button type="button" className={styles.sortButton} onClick={() => onSort(sortKey)}>
        <span>{label}</span>
        {isActive ? (
          direction === 'asc' ? (
            <ArrowUp size={14} />
          ) : (
            <ArrowDown size={14} />
          )
        ) : (
          <ChevronSort size={14} className={styles.sortIconInactive} />
        )}
      </button>
    </th>
  );
}
