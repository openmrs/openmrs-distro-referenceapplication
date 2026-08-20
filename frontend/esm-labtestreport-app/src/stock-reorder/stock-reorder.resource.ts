import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface StockReorderRow {
  stockItemId: number;
  itemName: string;
  locationId: number;
  locationName: string | null;
  ruleName: string;
  reorderLevel: number;
  onHandQty: number;
}

export function useStockReorderReport(locationUuid?: string) {
  const url = `/module/labtestreport/api/stock-reorder.json${locationUuid ? `?locationUuid=${locationUuid}` : ''}`;
  const { data, error, isLoading } = useSWR<{ data: Array<StockReorderRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
