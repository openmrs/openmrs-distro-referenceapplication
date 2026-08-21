import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockBatchExpiryRow {
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
  batchNo: string;
  expirationDate: string;
  remainingQty: number;
  daysUntilExpiry: number;
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

export function useStockExpiryRiskReport(daysAhead?: number, locationUuid?: string) {
  const url = `/module/labtestreport/api/stock-expiry-risk.json${buildQuery({ daysAhead, locationUuid })}`;
  const { data, error, isLoading } = useSWR<{ data: Array<StockBatchExpiryRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
