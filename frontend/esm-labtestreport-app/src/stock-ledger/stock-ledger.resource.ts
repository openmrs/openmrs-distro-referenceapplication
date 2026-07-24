import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockLedgerRow {
  stockItemId: number;
  itemName: string;
  ledgerDate: string;
  actualQty: number;
  incomingQty: number;
  outgoingQty: number;
  remainingQty: number;
}

function buildQuery(params: Record<string, string | number | undefined>): string {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      search.set(key, String(value));
    }
  });
  const query = search.toString();
  return query ? `?${query}` : '';
}

export function useStockLedgerReport(startDate?: string, endDate?: string) {
  const url = `/module/labtestreport/api/stock-ledger.json${buildQuery({ startDate, endDate })}`;
  const { data, error, isLoading } = useSWR<{ data: Array<StockLedgerRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
