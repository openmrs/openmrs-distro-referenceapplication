import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface SummaryRow {
  categoryConceptId: number;
  category: string;
  categoryRowSpan: number;
  testConceptId: number;
  testLabel: string;
  totalTests: number;
  counts: Record<string, number>;
  total: number;
}

export interface PatientRow {
  patientId: number;
  patientUuid: string;
  givenName: string;
  familyName: string;
  identifier: string;
  sex: string;
  nationalId: string;
  phoneNumber: string;
}

export interface DrilldownParams {
  testConceptId: number;
  gender?: string;
  ageGroup?: string;
  startDate?: string;
  endDate?: string;
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

export function useSummaryReport(startDate?: string, endDate?: string, enabled: boolean = true) {
  const url = enabled ? `/module/labtestreport/api/summary.json${buildQuery({ startDate, endDate })}` : null;
  const { data, error, isLoading } = useSWR<{ data: SummaryRow[] }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}

export function useDrilldown(params: DrilldownParams | null) {
  const url = params
    ? `/module/labtestreport/api/drilldown.json${buildQuery({ ...params })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: PatientRow[] }, Error>(url, openmrsFetch);
  return { patients: data?.data ?? [], error, isLoading };
}
