import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface PatientEncounterSummaryRow {
  patientId: number;
  patientUuid: string;
  givenName: string;
  familyName: string;
  age: number;
  encounterCount: number;
  mostRecentEncounterDate: string;
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
