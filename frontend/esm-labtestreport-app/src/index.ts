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

export const stockReportsHome = getAsyncLifecycle(
  () => import('./stock-reports-home/stock-reports-home.component'),
  options,
);

export const stockLedgerReport = getAsyncLifecycle(() => import('./stock-ledger/stock-ledger.component'), options);

export const stockConsumptionReport = getAsyncLifecycle(
  () => import('./stock-consumption/stock-consumption.component'),
  options,
);

export const stockDistributionReport = getAsyncLifecycle(
  () => import('./stock-distribution/stock-distribution.component'),
  options,
);

export const stockWastageReport = getAsyncLifecycle(() => import('./stock-wastage/stock-wastage.component'), options);

export const stockExpiryRiskReport = getAsyncLifecycle(
  () => import('./stock-expiry-risk/stock-expiry-risk.component'),
  options,
);

export const stockDaysRemainingReport = getAsyncLifecycle(
  () => import('./stock-days-remaining/stock-days-remaining.component'),
  options,
);

export const stockReorderReport = getAsyncLifecycle(() => import('./stock-reorder/stock-reorder.component'), options);

export const stockStockoutFrequencyReport = getAsyncLifecycle(
  () => import('./stock-stockout-frequency/stock-stockout-frequency.component'),
  options,
);

export const cmamSummaryReport = getAsyncLifecycle(() => import('./cmam-summary/cmam-summary.component'), options);

export const labTestSummaryLink = getAsyncLifecycle(() => import('./lab-test-summary-link.component'), options);

export const patientEncounterSummaryLink = getAsyncLifecycle(
  () => import('./patient-encounter-summary-link.component'),
  options,
);

export const diseaseSummaryLink = getAsyncLifecycle(() => import('./disease-summary-link.component'), options);

export const sessionAttendanceLink = getAsyncLifecycle(() => import('./session-attendance-link.component'), options);

export const stockLedgerLink = getAsyncLifecycle(() => import('./stock-ledger-link.component'), options);

export const cmamSummaryLink = getAsyncLifecycle(() => import('./cmam-summary-link.component'), options);

export const reportsOverviewLink = getAsyncLifecycle(() => import('./reports-overview-link.component'), options);
