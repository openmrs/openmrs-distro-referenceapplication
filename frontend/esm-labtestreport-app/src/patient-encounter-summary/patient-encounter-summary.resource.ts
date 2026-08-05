import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface PatientEncounterSummaryRow {
  patientId: number;
  patientUuid: string;
  givenName: string;
  familyName: string;
  age: number;
  visitCount: number;
  mostRecentVisitDate: string;
}

export function usePatientEncounterSummary(startDate?: string, endDate?: string, enabled: boolean = true) {
  const search = new URLSearchParams();
  if (startDate) {
    search.set('startDate', startDate);
  }
  if (endDate) {
    search.set('endDate', endDate);
  }
  const query = search.toString();
  const url = enabled ? `/module/labtestreport/api/encounters.json${query ? `?${query}` : ''}` : null;
  const { data, error, isLoading } = useSWR<{ data: PatientEncounterSummaryRow[] }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}

export interface PatientEncounterDetailRow {
  patientId: number;
  patientUuid: string;
  givenName: string;
  familyName: string;
  visitId: number;
  visitDate: string;
  locationName: string;
  providerName: string;
}

export function usePatientEncounterDetails(startDate?: string, endDate?: string, enabled: boolean = true) {
  const search = new URLSearchParams();
  if (startDate) {
    search.set('startDate', startDate);
  }
  if (endDate) {
    search.set('endDate', endDate);
  }
  const query = search.toString();
  const url = enabled ? `/module/labtestreport/api/encounter-details.json${query ? `?${query}` : ''}` : null;
  const { data, error, isLoading } = useSWR<{ data: PatientEncounterDetailRow[] }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}
