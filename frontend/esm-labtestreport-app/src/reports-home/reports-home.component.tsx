import React from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Grid, Column } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import pageStyles from '../reports-shell/reports-page.scss';
import styles from './reports-home.scss';

export default function ReportsHome() {
  const { t } = useTranslation();

  return (
    <div>
      <ReportsTabs activeKey="overview" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('reports', 'Reports')}</h2>
        <Grid className={styles.tileGrid}>
          <Column sm={4} md={4} lg={5}>
            <ClickableTile
              className={styles.tile}
              onClick={() => navigate({ to: '${openmrsSpaBase}/lab-test-summary-report' })}
            >
              <div className={styles.tileTitle}>{t('labTestSummaryReportTitle', 'Lab Test Summary Report')}</div>
              <p className={styles.tileDescription}>
                {t(
                  'labTestSummaryReportTileDesc',
                  'Lab test orders by category, test, age group and gender, with drill-down to the patients behind each count.',
                )}
              </p>
              <ArrowRight size={20} className={styles.tileIcon} />
            </ClickableTile>
          </Column>
          <Column sm={4} md={4} lg={5}>
            <ClickableTile
              className={styles.tile}
              onClick={() => navigate({ to: '${openmrsSpaBase}/patient-encounter-summary-report' })}
            >
              <div className={styles.tileTitle}>
                {t('patientVisitSummaryReportTitle', 'Patient Visit Summary Report')}
              </div>
              <p className={styles.tileDescription}>
                {t(
                  'patientVisitSummaryReportTileDesc',
                  'Patients by number of visits and most recent visit date. Click a patient to open their chart.',
                )}
              </p>
              <ArrowRight size={20} className={styles.tileIcon} />
            </ClickableTile>
          </Column>
          <Column sm={4} md={4} lg={5}>
            <ClickableTile
              className={styles.tile}
              onClick={() => navigate({ to: '${openmrsSpaBase}/disease-summary-report' })}
            >
              <div className={styles.tileTitle}>{t('diseaseSummaryReportTitle', 'Disease Surveillance Summary Report')}</div>
              <p className={styles.tileDescription}>
                {t(
                  'diseaseSummaryReportTileDesc',
                  'Diagnoses by category, age group and gender, with drill-down to the patients behind each count.',
                )}
              </p>
              <ArrowRight size={20} className={styles.tileIcon} />
            </ClickableTile>
          </Column>
          <Column sm={4} md={4} lg={5}>
            <ClickableTile
              className={styles.tile}
              onClick={() => navigate({ to: '${openmrsSpaBase}/session-attendance-report' })}
            >
              <div className={styles.tileTitle}>{t('sessionAttendanceReportTitle', 'Session Attendance Report')}</div>
              <p className={styles.tileDescription}>
                {t(
                  'sessionAttendanceReportTileDesc',
                  'Individual and Group session attendance by day, age group and gender, with drill-down to the patients behind each count.',
                )}
              </p>
              <ArrowRight size={20} className={styles.tileIcon} />
            </ClickableTile>
          </Column>
          <Column sm={4} md={4} lg={5}>
            <ClickableTile
              className={styles.tile}
              onClick={() => navigate({ to: '${openmrsSpaBase}/stock-ledger-report' })}
            >
              <div className={styles.tileTitle}>{t('stockLedgerReportTitle', 'Stock Inventory Ledger Report')}</div>
              <p className={styles.tileDescription}>
                {t('stockLedgerReportTileDesc', 'Daily opening, incoming, outgoing and closing stock balances per item.')}
              </p>
              <ArrowRight size={20} className={styles.tileIcon} />
            </ClickableTile>
          </Column>
        </Grid>
      </div>
    </div>
  );
}
