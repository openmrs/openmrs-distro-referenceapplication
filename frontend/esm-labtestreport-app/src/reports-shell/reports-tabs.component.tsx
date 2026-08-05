import React from 'react';
import { useTranslation } from 'react-i18next';
import { Tabs, TabList, Tab } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import styles from './reports-tabs.scss';

export type ReportsTabKey =
  | 'overview'
  | 'lab-test-summary'
  | 'patient-encounter-summary'
  | 'disease-summary'
  | 'session-attendance'
  | 'stock-ledger';

const TAB_ROUTES: Record<ReportsTabKey, string> = {
  overview: 'labtestreport-reports',
  'lab-test-summary': 'lab-test-summary-report',
  'patient-encounter-summary': 'patient-encounter-summary-report',
  'disease-summary': 'disease-summary-report',
  'session-attendance': 'session-attendance-report',
  'stock-ledger': 'stock-ledger-report',
};

const TAB_ORDER: ReportsTabKey[] = [
  'overview',
  'lab-test-summary',
  'patient-encounter-summary',
  'disease-summary',
  'session-attendance',
  'stock-ledger',
];

export default function ReportsTabs({ activeKey }: { activeKey: ReportsTabKey }) {
  const { t } = useTranslation();
  const selectedIndex = TAB_ORDER.indexOf(activeKey);

  const tabLabels: Record<ReportsTabKey, string> = {
    overview: t('reportsOverview', 'Reports overview'),
    'lab-test-summary': t('labTestSummary', 'Lab Test Summary'),
    'patient-encounter-summary': t('patientVisits', 'Patient Visits'),
    'disease-summary': t('diseaseSurveillance', 'Disease Surveillance'),
    'session-attendance': t('sessionAttendance', 'Session Attendance'),
    'stock-ledger': t('stockLedger', 'Stock Ledger'),
  };

  return (
    <div className={styles.tabsWrapper}>
      <p className={styles.sectionLabel}>{t('reports', 'Reports')}</p>
      <Tabs
        selectedIndex={selectedIndex}
        onChange={({ selectedIndex: index }: { selectedIndex: number }) => {
          const key = TAB_ORDER[index];
          navigate({ to: `\${openmrsSpaBase}/${TAB_ROUTES[key]}` });
        }}
      >
        <TabList aria-label={t('reportsNavigation', 'Reports navigation')} contained>
          {TAB_ORDER.map((key) => (
            <Tab key={key}>{tabLabels[key]}</Tab>
          ))}
        </TabList>
      </Tabs>
    </div>
  );
}
