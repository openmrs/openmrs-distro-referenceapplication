import { openmrsFetch } from '@openmrs/esm-framework';
import useSWR from 'swr';

export interface SessionAttendanceRow {
  sessionDate: string;
  sessionType: string;
  sessionSubject: string | null;
  totalAttendees: number;
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

export interface SessionDrilldownParams {
  sessionDate: string;
  sessionType: string;
  gender?: string;
  ageGroup?: string;
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

export function useSessionAttendanceReport(startDate?: string, endDate?: string, enabled: boolean = true) {
  const url = enabled
    ? `/module/labtestreport/api/session-attendance.json${buildQuery({ startDate, endDate })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: Array<SessionAttendanceRow> }, Error>(url, openmrsFetch);
  return { rows: data?.data ?? [], error, isLoading };
}

export function useSessionAttendanceDrilldown(params: SessionDrilldownParams | null) {
  const url = params
    ? `/module/labtestreport/api/session-attendance-drilldown.json${buildQuery({ ...params })}`
    : null;
  const { data, error, isLoading } = useSWR<{ data: Array<PatientRow> }, Error>(url, openmrsFetch);
  return { patients: data?.data ?? [], error, isLoading };
}
