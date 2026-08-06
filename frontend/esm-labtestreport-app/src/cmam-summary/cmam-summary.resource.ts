import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export type CmamDimension = 'currentDiagnosis' | 'childLastStatus' | 'alertStatus';

export interface CmamSummaryRow {
  dimension: CmamDimension;
  categoryConceptId: number;
  category: string;
  total: number;
}

export interface CmamPatientRow {
  patientId: number;
  patientUuid: string;
  givenName: string;
  familyName: string;
  identifier: string;
  sex: string;
  nationalId: string;
  phoneNumber: string;
  currentDiagnosis: string;
  childLastStatus: string;
  alertStatus: string;
  nextVisitDate: string;
}

export interface CmamDrilldownParams {
  dimension: CmamDimension;
  categoryConceptId: number;
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

export function useCmamSummaryReport(startDate?: string, endDate?: string, enabled: boolean = true) {
  const url = enabled ? `/module/labtestreport/api/cmam-summary.json${buildQuery({ startDate, endDate })}` : null;
  const { data, error, isLoading } = useSWR<{ data: CmamSummaryRow[] }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}

export function useCmamDrilldown(params: CmamDrilldownParams | null) {
  const url = params ? `/module/labtestreport/api/cmam-drilldown.json${buildQuery({ ...params })}` : null;
  const { data, error, isLoading } = useSWR<{ data: CmamPatientRow[] }, Error>(url, openmrsFetch);
  return { patients: data?.data ?? [], error, isLoading };
}
