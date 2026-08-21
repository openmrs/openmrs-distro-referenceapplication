import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockLocationQtyRow {
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
  quantity: number;
  unitName: string | null;
  /** Only populated for the Distribution report - null for Consumption and Wastage. */
  sourceLocationName: string | null;
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

export function useStockConsumptionReport(
  startDate?: string,
  endDate?: string,
  locationUuid?: string,
  enabled: boolean = true,
) {
  const url = enabled
    ? `/module/labtestreport/api/stock-consumption.json${buildQuery({ startDate, endDate, locationUuid })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: Array<StockLocationQtyRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
