import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';
import type { StockLocationQtyRow } from '../stock-consumption/stock-consumption.resource';

export type { StockLocationQtyRow };

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

export function useStockWastageReport(startDate?: string, endDate?: string, locationUuid?: string, enabled: boolean = true) {
  const url = enabled
    ? `/module/labtestreport/api/stock-wastage.json${buildQuery({ startDate, endDate, locationUuid })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: Array<StockLocationQtyRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
