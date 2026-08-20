import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockoutFrequencyRow {
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
  stockoutDays: number;
  activeDays: number;
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

export function useStockoutFrequencyReport(startDate?: string, endDate?: string, locationUuid?: string) {
  const url = `/module/labtestreport/api/stock-stockout-frequency.json${buildQuery({ startDate, endDate, locationUuid })}`;
  const { data, error, isLoading } = useSWR<{ data: Array<StockoutFrequencyRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
