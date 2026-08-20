import React, { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Grid, Column, InlineLoading, Tag } from '@carbon/react';
import { ArrowRight, WarningAltFilled, CheckmarkFilled } from '@carbon/react/icons';
import { navigate } from '@openmrs/esm-framework';
import { useCmamSummaryReport } from '../cmam-summary/cmam-summary.resource';
import pageStyles from '../reports-shell/reports-page.scss';
import styles from './reports-home.scss';

interface ReportTile {
  key: string;
  title: string;
  description: string;
  route: string;
  alertCount?: number;
  alertLoading?: boolean;
}

interface ReportCategory {
  key: string;
  label: string;
  tiles: Array<ReportTile>;
}

/**
 * Groups every report into the same handful of categories a clinic manager would think in
 * (clinical/diagnostic activity, patient/session attendance, nutrition, stock), mirroring how the
 * Stock Reports tab itself groups its own sub-reports - so growing the report count doesn't mean
 * growing an undifferentiated wall of tiles.
 */
export default function ReportsHome() {
  const { t } = useTranslation();

  const { rows: cmamAlertRows, isLoading: cmamAlertLoading } = useCmamSummaryReport();
  const cmamAlertCount = useMemo(
    () =>
      cmamAlertRows
        .filter((row) => row.dimension === 'alertStatus' && row.category !== 'OK')
        .reduce((sum, row) => sum + row.total, 0),
    [cmamAlertRows],
  );

  const categories: Array<ReportCategory> = [
    {
      key: 'clinical',
      label: t('clinicalReports', 'Clinical Reports'),
      tiles: [
        {
          key: 'lab-test-summary',
          title: t('labTestSummaryReportTitle', 'Lab Test Summary Report'),
          description: t(
            'labTestSummaryReportTileDesc',
            'Lab test orders by category, test, age group and gender, with drill-down to the patients behind each count.',
          ),
          route: 'lab-test-summary-report',
        },
        {
          key: 'disease-summary',
          title: t('diseaseSummaryReportTitle', 'Disease Surveillance Summary Report'),
          description: t(
            'diseaseSummaryReportTileDesc',
            'Diagnoses by category, age group and gender, with drill-down to the patients behind each count.',
          ),
          route: 'disease-summary-report',
        },
      ],
    },
    {
      key: 'patient-activity',
      label: t('patientActivityReports', 'Patient Activity Reports'),
      tiles: [
        {
          key: 'patient-encounter-summary',
          title: t('patientVisitSummaryReportTitle', 'Patient Visit Summary Report'),
          description: t(
            'patientVisitSummaryReportTileDesc',
            'Patients by number of visits and most recent visit date. Click a patient to open their chart.',
          ),
          route: 'patient-encounter-summary-report',
        },
        {
          key: 'session-attendance',
          title: t('sessionAttendanceReportTitle', 'Session Attendance Report'),
          description: t(
            'sessionAttendanceReportTileDesc',
            'Individual and Group session attendance by day, age group and gender, with drill-down to the patients behind each count.',
          ),
          route: 'session-attendance-report',
        },
      ],
    },
    {
      key: 'nutrition',
      label: t('nutritionReports', 'Nutrition Reports'),
      tiles: [
        {
          key: 'cmam-follow-up',
          title: t('cmamFollowUpReportTitle', 'CMAM Follow-up Summary Report'),
          description: t(
            'cmamFollowUpReportTileDesc',
            'Children by Current Diagnosis, Child Last Status and Alert Status, with drill-down to the children behind each count.',
          ),
          route: 'cmam-follow-up-report',
          alertCount: cmamAlertCount,
          alertLoading: cmamAlertLoading,
        },
      ],
    },
    {
      key: 'stock',
      label: t('stockReports', 'Stock Reports'),
      tiles: [
        {
          key: 'stock-reports-home',
          title: t('stockReportsTitle', 'Stock Reports'),
          description: t(
            'stockReportsTileDesc',
            'Inventory ledger, consumption by location, and distribution reports for stock management.',
          ),
          route: 'stock-reports-home',
        },
      ],
    },
  ];

  return (
    <div>
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('reports', 'Reports')}</h2>
        {categories.map((category) => (
          <div key={category.key} className={styles.categorySection}>
            <h3 className={styles.categoryLabel}>{category.label}</h3>
            <Grid className={styles.tileGrid}>
              {category.tiles.map((tile) => (
                <Column sm={4} md={4} lg={5} key={tile.key}>
                  <ClickableTile
                    className={styles.tile}
                    onClick={() => navigate({ to: `\${openmrsSpaBase}/${tile.route}` })}
                  >
                    <div className={styles.tileTitleRow}>
                      <div className={styles.tileTitle}>{tile.title}</div>
                      {tile.alertLoading && <InlineLoading />}
                      {!tile.alertLoading && tile.alertCount !== undefined && tile.alertCount > 0 && (
                        <Tag type="red" size="sm" renderIcon={WarningAltFilled} className={styles.tileBadge}>
                          {t('nNeedAttention', '{{count}} need attention', { count: tile.alertCount })}
                        </Tag>
                      )}
                      {!tile.alertLoading && tile.alertCount === 0 && (
                        <Tag type="green" size="sm" renderIcon={CheckmarkFilled} className={styles.tileBadge}>
                          {t('allClear', 'All clear')}
                        </Tag>
                      )}
                    </div>
                    <p className={styles.tileDescription}>{tile.description}</p>
                    <ArrowRight size={20} className={styles.tileIcon} />
                  </ClickableTile>
                </Column>
              ))}
            </Grid>
          </div>
        ))}
      </div>
    </div>
  );
}
