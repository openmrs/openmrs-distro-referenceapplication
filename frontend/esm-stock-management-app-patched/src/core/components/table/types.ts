import type React from 'react';

export interface DataTableRenderProps {
  onInputChange: (e: React.ChangeEvent<HTMLInputElement>, defaultValue?: string) => void;
}

export interface CustomTableHeader {
  key: string;
  header: string | { content: React.ReactNode };
  styles?: React.CSSProperties;
  isSortable?: boolean;
}

export interface CustomTableRow {
  id: string;
  [key: string]: unknown;
}
