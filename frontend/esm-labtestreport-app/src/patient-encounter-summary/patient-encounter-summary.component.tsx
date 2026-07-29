import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { InlineLoading, Button, ContentSwitcher, Switch, NumberInput } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import MonthCompareControls from '../reports-shell/month-compare-controls.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { buildKpiExportSheet, type ExportSheet } from '../reports-shell/export-utils';
import { useMonthComparison } from '../reports-shell/month-compare';
import pageStyles from '../reports-shell/reports-page.scss';
import { usePatientEncounterSummary, type PatientEncounterSummaryRow } from './patient-encounter-summary.resource';

function summarize(rows: Array<PatientEncounterSummaryRow>, minAge: number | '', maxAge: number | '') {
  const filteredRows = rows.filter((row) => {
    if (minAge !== '' && row.age < minAge) {
      return false;
    }
    if (maxAge !== '' && row.age > maxAge) {
      return false;
    }
    return true;
  });
  const totalEncounters = filteredRows.reduce((sum, row) => sum + row.encounterCount, 0);
  const mostRecentDate = filteredRows.reduce<string | null>(
    (latest, row) => (!latest || row.mostRecentEncounterDate > latest ? row.mostRecentEncounterDate : latest),
    null,
  );
  return { filteredRows, totalEncounters, mostRecentDate };
}

export default function PatientEncounterSummaryReport() {
  const { t } = useTranslation();
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [minAge, setMinAge] = useState<number | ''>('');
  const [maxAge, setMaxAge] = useState<number | ''>('');
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');
  const compare = useMonthComparison();

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows, isLoading } = usePatientEncounterSummary(primaryStartDate, primaryEndDate);
  const { rows: compareRowsRaw, isLoading: compareLoading } = usePatientEncounterSummary(
    compare.comparison.startDate,
    compare.comparison.endDate,
    compare.enabled,
  );
  const dataLoading = isLoading || (compare.enabled && compareLoading);

  const primary = useMemo(() => summarize(rows, minAge, maxAge), [rows, minAge, maxAge]);
  const { filteredRows } = primary;

  const compareSummary = useMemo(
    () => (compare.enabled ? summarize(compareRowsRaw, minAge, maxAge) : null),
    [compareRowsRaw, minAge, maxAge, compare.enabled],
  );

  const chartData = useMemo(
    () =>
      filteredRows.map((row) => ({
        label: `${row.givenName} ${row.familyName}`,
        value: row.encounterCount,
      })),
    [filteredRows],
  );

  const kpiItems = useMemo(() => {
    const items = [
      { label: t('totalPatients', 'Total Patients'), value: filteredRows.length },
      { label: t('totalEncounters', 'Total Encounters'), value: primary.totalEncounters },
      {
        label: t('avgEncountersPerPatient', 'Avg Encounters / Patient'),
        value: filteredRows.length > 0 ? (primary.totalEncounters / filteredRows.length).toFixed(1) : '0',
      },
      { label: t('mostRecentEncounter', 'Most Recent Encounter'), value: primary.mostRecentDate ?? '—' },
    ];
    if (!compare.enabled || !compareSummary) {
      return items;
    }
    const compareValues: Array<React.ReactNode> = [
      compareSummary.filteredRows.length,
      compareSummary.totalEncounters,
      compareSummary.filteredRows.length > 0
        ? (compareSummary.totalEncounters / compareSummary.filteredRows.length).toFixed(1)
        : '0',
      compareSummary.mostRecentDate ?? '—',
    ];
    return items.map((item, index) => ({
      ...item,
      compareValue: compareValues[index],
      compareLabel: compare.comparison.label,
    }));
  }, [t, filteredRows, primary, compare.enabled, compareSummary, compare.comparison.label]);

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('patientEncounterSummary', 'Patient Encounter Summary'),
      headers: [
        t('givenName', 'Given Name'),
        t('familyName', 'Family Name'),
        t('age', 'Age'),
        t('numberOfEncounters', 'Number of Encounters'),
        t('mostRecentEncounterDate', 'Most Recent Encounter Date'),
      ],
      rows: filteredRows.map((row) => [
        row.givenName,
        row.familyName,
        row.age,
        row.encounterCount,
        row.mostRecentEncounterDate,
      ]),
    }),
    [t, filteredRows],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(
    () => (compare.enabled ? [buildKpiExportSheet(kpiItems, t)] : []),
    [t, compare.enabled, kpiItems],
  );

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  function goToPatientChart(patientUuid: string) {
    navigate({ to: `\${openmrsSpaBase}/patient/${patientUuid}/chart/visits` });
  }

  return (
    <div>
      <ReportsTabs activeKey="patient-encounter-summary" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>
          {t('patientEncounterSummaryReportTitle', 'Patient Encounter Summary Report')}
        </h2>

        {!dataLoading && <KpiTiles items={kpiItems} />}

        <MonthCompareControls {...compare} />

        {!compare.enabled && (
          <div className={pageStyles.filterTile}>
            <div className={pageStyles.filterField}>
              <label htmlFor="startDate">{t('startDate', 'Start Date')}</label>
              <input
                id="startDate"
                type="date"
                value={startDateInput}
                onChange={(e) => setStartDateInput(e.target.value)}
              />
            </div>
            <div className={pageStyles.filterField}>
              <label htmlFor="endDate">{t('endDate', 'End Date')}</label>
              <input
                id="endDate"
                type="date"
                value={endDateInput}
                onChange={(e) => setEndDateInput(e.target.value)}
              />
            </div>
            <Button size="md" onClick={applyFilter}>
              {t('filter', 'Filter')}
            </Button>
          </div>
        )}

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField} style={{ minWidth: '10rem' }}>
            <NumberInput
              id="minAge"
              label={t('minAge', 'Min Age')}
              size="md"
              value={minAge}
              min={0}
              allowEmpty
              onChange={(_e, { value }) => setMinAge(value === '' ? '' : Number(value))}
            />
          </div>
          <div className={pageStyles.filterField} style={{ minWidth: '10rem' }}>
            <NumberInput
              id="maxAge"
              label={t('maxAge', 'Max Age')}
              size="md"
              value={maxAge}
              min={0}
              allowEmpty
              onChange={(_e, { value }) => setMaxAge(value === '' ? '' : Number(value))}
            />
          </div>
        </div>

        <ExportButtons
          filenameBase="patient-encounter-summary-report"
          mainSheet={mainExportSheet}
          extraSheets={exportExtraSheets}
          disabled={dataLoading}
        />

        <div className={pageStyles.viewSwitcher}>
          <ContentSwitcher
            size="md"
            selectedIndex={viewMode === 'table' ? 0 : 1}
            onChange={({ name }) => setViewMode(name as 'table' | 'graph')}
          >
            <Switch name="table" text={t('table', 'Table')} />
            <Switch name="graph" text={t('graph', 'Graph')} />
          </ContentSwitcher>
        </div>

        {dataLoading && <InlineLoading description={t('loadingReport', 'Loading report...')} />}

        {!dataLoading && viewMode === 'table' && (
          <div className={pageStyles.tableContainer}>
            <table className={pageStyles.dataTable}>
              <thead>
                <tr>
                  <th className="left">{t('patientName', 'Patient Name')}</th>
                  <th>{t('age', 'Age')}</th>
                  <th>{t('numberOfEncounters', 'Number of Encounters')}</th>
                  <th>{t('mostRecentEncounterDate', 'Most Recent Encounter Date')}</th>
                </tr>
              </thead>
              <tbody>
                {filteredRows.map((row) => (
                  <tr
                    key={row.patientId}
                    className={pageStyles.clickableRow}
                    onClick={() => goToPatientChart(row.patientUuid)}
                  >
                    <td className="left">
                      {row.givenName} {row.familyName}
                    </td>
                    <td>{row.age}</td>
                    <td>{row.encounterCount}</td>
                    <td>{row.mostRecentEncounterDate}</td>
                  </tr>
                ))}
                {filteredRows.length === 0 && (
                  <tr>
                    <td colSpan={4} className={pageStyles.emptyState}>
                      {t('noPatientsForSelection', 'No patients found for this selection.')}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}

        {!dataLoading && viewMode === 'graph' && (
          <SimpleBarChart
            data={chartData}
            emptyMessage={t('noPatientsForSelection', 'No patients found for this selection.')}
          />
        )}
      </div>
    </div>
  );
}
