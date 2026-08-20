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

export function useStockDistributionReport(
  startDate?: string,
  endDate?: string,
  sourceLocationUuid?: string,
  enabled: boolean = true,
) {
  const url = enabled
    ? `/module/labtestreport/api/stock-distribution.json${buildQuery({ startDate, endDate, sourceLocationUuid })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: Array<StockLocationQtyRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
