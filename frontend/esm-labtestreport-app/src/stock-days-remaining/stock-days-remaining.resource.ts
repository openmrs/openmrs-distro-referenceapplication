import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockDaysRemainingRow {
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
  onHandQty: number;
  avgDailyConsumption: number;
  daysRemaining: number | null;
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

export function useStockDaysRemainingReport(startDate?: string, endDate?: string, locationUuid?: string) {
  const url = `/module/labtestreport/api/stock-days-remaining.json${buildQuery({ startDate, endDate, locationUuid })}`;
  const { data, error, isLoading } = useSWR<{ data: Array<StockDaysRemainingRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
