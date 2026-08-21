import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockLedgerRow {
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
  ledgerDate: string;
  actualQty: number;
  incomingQty: number;
  outgoingQty: number;
  remainingQty: number;
  unitName: string | null;
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

export function useStockLedgerReport(
  startDate?: string,
  endDate?: string,
  locationUuid?: string,
  enabled: boolean = true,
) {
  const url = enabled
    ? `/module/labtestreport/api/stock-ledger.json${buildQuery({ startDate, endDate, locationUuid })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: Array<StockLedgerRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
