import React from 'react';
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

const TAB_LABELS: Record<ReportsTabKey, string> = {
  overview: 'Reports overview',
  'lab-test-summary': 'Lab Test Summary',
  'patient-encounter-summary': 'Patient Encounters',
  'disease-summary': 'Disease Surveillance',
  'session-attendance': 'Session Attendance',
  'stock-ledger': 'Stock Ledger',
};

export default function ReportsTabs({ activeKey }: { activeKey: ReportsTabKey }) {
  const selectedIndex = TAB_ORDER.indexOf(activeKey);

  return (
    <div className={styles.tabsWrapper}>
      <p className={styles.sectionLabel}>Reports</p>
      <Tabs
        selectedIndex={selectedIndex}
        onChange={({ selectedIndex: index }: { selectedIndex: number }) => {
          const key = TAB_ORDER[index];
          navigate({ to: `\${openmrsSpaBase}/${TAB_ROUTES[key]}` });
        }}
      >
        <TabList aria-label="Reports navigation" contained>
          {TAB_ORDER.map((key) => (
            <Tab key={key}>{TAB_LABELS[key]}</Tab>
          ))}
        </TabList>
      </Tabs>
    </div>
  );
}
