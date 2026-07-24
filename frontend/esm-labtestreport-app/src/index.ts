/**
 * Entry point of this microfrontend. Registers a "Reports" landing page plus the three
 * interactive report pages (Lab Test Summary, Patient Encounter Summary, Disease Surveillance
 * Summary), and a card link for each on the System Administration page.
 */
import { getAsyncLifecycle } from '@openmrs/esm-framework';
import { moduleName } from './constants';

const options = {
  featureName: 'labtestreport',
  moduleName,
};

export const importTranslation = (require as any).context('../translations', false, /.json$/, 'lazy');

export function startupApp() {
  // no config schema needed
}

export const reportsHome = getAsyncLifecycle(() => import('./reports-home/reports-home.component'), options);

export const labTestSummaryReport = getAsyncLifecycle(
  () => import('./lab-test-summary/lab-test-summary.component'),
  options,
);

export const patientEncounterSummaryReport = getAsyncLifecycle(
  () => import('./patient-encounter-summary/patient-encounter-summary.component'),
  options,
);

export const diseaseSummaryReport = getAsyncLifecycle(
  () => import('./disease-summary/disease-summary.component'),
  options,
);

export const sessionAttendanceReport = getAsyncLifecycle(
  () => import('./session-attendance/session-attendance.component'),
  options,
);

export const stockLedgerReport = getAsyncLifecycle(() => import('./stock-ledger/stock-ledger.component'), options);

export const labTestSummaryLink = getAsyncLifecycle(() => import('./lab-test-summary-link.component'), options);

export const patientEncounterSummaryLink = getAsyncLifecycle(
  () => import('./patient-encounter-summary-link.component'),
  options,
);

export const diseaseSummaryLink = getAsyncLifecycle(() => import('./disease-summary-link.component'), options);

export const sessionAttendanceLink = getAsyncLifecycle(() => import('./session-attendance-link.component'), options);

export const stockLedgerLink = getAsyncLifecycle(() => import('./stock-ledger-link.component'), options);
