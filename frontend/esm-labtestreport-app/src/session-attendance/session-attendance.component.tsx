import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import type { TFunction } from 'i18next';
import { Modal, InlineLoading, Button, ContentSwitcher, Select, SelectItem, Switch } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import BackToReportsLink from '../reports-shell/back-to-reports-link.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import MonthCompareControls from '../reports-shell/month-compare-controls.component';
import ComparisonSummaryTable from '../reports-shell/comparison-summary-table.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { buildKpiExportSheet, buildComparisonExportSheet, type ExportSheet } from '../reports-shell/export-utils';
import { useMonthComparison } from '../reports-shell/month-compare';
import pageStyles from '../reports-shell/reports-page.scss';
import { getTodayDateString, clampToToday } from '../reports-shell/date-utils';
import {
  useSessionAttendanceReport,
  useSessionAttendanceDrilldown,
  type SessionAttendanceRow,
  type SessionDrilldownParams,
} from './session-attendance.resource';

const AGE_GENDER_COLUMNS: Array<{ ageGroup: string; gender: string; label: string }> = [
  { ageGroup: '0-4', gender: 'M', label: '0-4 M' },
  { ageGroup: '0-4', gender: 'F', label: '0-4 F' },
  { ageGroup: '5-14', gender: 'M', label: '5-14 M' },
  { ageGroup: '5-14', gender: 'F', label: '5-14 F' },
  { ageGroup: '15-18', gender: 'M', label: '15-18 M' },
  { ageGroup: '15-18', gender: 'F', label: '15-18 F' },
  { ageGroup: '19-49', gender: 'M', label: '19-49 M' },
  { ageGroup: '19-49', gender: 'F', label: '19-49 F' },
  { ageGroup: '50-65', gender: 'M', label: '50-65 M' },
  { ageGroup: '50-65', gender: 'F', label: '50-65 F' },
  { ageGroup: '65+', gender: 'M', label: '65+ M' },
  { ageGroup: '65+', gender: 'F', label: '65+ F' },
];

const SESSION_TYPES_IN_ORDER = ['Individual Sessions', 'Group Sessions', 'Case follow up'];

interface DayBlock {
  date: string;
  rows: Array<SessionAttendanceRow>;
  dailyTotal: SessionAttendanceRow;
}

interface Selection extends SessionDrilldownParams {
  ageGroupLabel?: string;
}

function sessionTypeLabel(t: TFunction, sessionType: string): string {
  switch (sessionType) {
    case 'Individual Sessions':
      return t('individualSessions', 'Individual Sessions');
    case 'Group Sessions':
      return t('groupSessions', 'Group Sessions');
    case 'Case follow up':
      return t('caseFollowUp', 'Case follow up');
    default:
      return sessionType;
  }
}

function zeroRow(date: string, type: string): SessionAttendanceRow {
  const counts: Record<string, number> = {};
  AGE_GENDER_COLUMNS.forEach((col) => (counts[`${col.ageGroup}_${col.gender}`] = 0));
  return { sessionDate: date, sessionType: type, sessionSubject: null, totalAttendees: 0, counts, total: 0 };
}

function buildDayBlocks(rows: Array<SessionAttendanceRow>): Array<DayBlock> {
  const byDate = new Map<string, Map<string, SessionAttendanceRow>>();
  rows.forEach((row) => {
    if (!byDate.has(row.sessionDate)) {
      byDate.set(row.sessionDate, new Map());
    }
    byDate.get(row.sessionDate)!.set(row.sessionType, row);
  });

  const blocks: Array<DayBlock> = [];
  byDate.forEach((typeMap, date) => {
    const blockRows: Array<SessionAttendanceRow> = [];
    const dailyTotal = zeroRow(date, '');
    SESSION_TYPES_IN_ORDER.forEach((type) => {
      const row = typeMap.get(type) ?? zeroRow(date, type);
      dailyTotal.totalAttendees += row.totalAttendees;
      dailyTotal.total += row.total;
      AGE_GENDER_COLUMNS.forEach((col) => {
        const key = `${col.ageGroup}_${col.gender}`;
        dailyTotal.counts[key] = (dailyTotal.counts[key] ?? 0) + (row.counts?.[key] ?? 0);
      });
      if (row.total !== 0) {
        blockRows.push(row);
      }
    });
    if (blockRows.length > 0) {
      blocks.push({ date, rows: blockRows, dailyTotal });
    }
  });
  return blocks.sort((a, b) => a.date.localeCompare(b.date));
}

export default function SessionAttendanceReport() {
  const { t } = useTranslation();
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [selection, setSelection] = useState<Selection | null>(null);
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');
  const [sessionTypeFilter, setSessionTypeFilter] = useState('');
  const compare = useMonthComparison();

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows: rawRows, isLoading } = useSessionAttendanceReport(primaryStartDate, primaryEndDate);
  const { rows: compareRawRows, isLoading: compareLoading } = useSessionAttendanceReport(
    compare.comparison.startDate,
    compare.comparison.endDate,
    compare.enabled,
  );
  const { patients, isLoading: patientsLoading } = useSessionAttendanceDrilldown(selection);
  const dataLoading = isLoading || (compare.enabled && compareLoading);

  const rows = useMemo(
    () => (sessionTypeFilter ? rawRows.filter((row) => row.sessionType === sessionTypeFilter) : rawRows),
    [rawRows, sessionTypeFilter],
  );
  const compareRows = useMemo(
    () => (sessionTypeFilter ? compareRawRows.filter((row) => row.sessionType === sessionTypeFilter) : compareRawRows),
    [compareRawRows, sessionTypeFilter],
  );

  const dayBlocks = useMemo(() => buildDayBlocks(rows), [rows]);

  function attendeesByType(sourceRows: Array<SessionAttendanceRow>, sessionType: string): number {
    return sourceRows
      .filter((row) => row.sessionType === sessionType)
      .reduce((sum, row) => sum + row.totalAttendees, 0);
  }

  const kpiItems = useMemo(() => {
    const totalAttendees = dayBlocks.reduce((sum, block) => sum + block.dailyTotal.totalAttendees, 0);
    const individualCount = attendeesByType(rows, 'Individual Sessions');
    const groupCount = attendeesByType(rows, 'Group Sessions');
    const items = [
      { label: t('totalDays', 'Total Days'), value: dayBlocks.length },
      { label: t('totalAttendees', 'Total Attendees'), value: totalAttendees },
      { label: sessionTypeLabel(t, 'Individual Sessions'), value: individualCount },
      { label: sessionTypeLabel(t, 'Group Sessions'), value: groupCount },
    ];
    if (!compare.enabled) {
      return items;
    }
    const compareValues = [
      new Set(compareRows.map((row) => row.sessionDate)).size,
      compareRows.reduce((sum, row) => sum + row.totalAttendees, 0),
      attendeesByType(compareRows, 'Individual Sessions'),
      attendeesByType(compareRows, 'Group Sessions'),
    ];
    return items.map((item, index) => ({
      ...item,
      compareValue: compareValues[index],
      compareLabel: compare.comparison.label,
    }));
  }, [t, dayBlocks, rows, compareRows, compare.enabled, compare.comparison.label]);

  const comparisonTableRows = useMemo(() => {
    if (!compare.enabled) {
      return [];
    }
    return SESSION_TYPES_IN_ORDER.map((sessionType) => ({
      label: sessionTypeLabel(t, sessionType),
      current: attendeesByType(rows, sessionType),
      compare: attendeesByType(compareRows, sessionType),
    }));
  }, [t, rows, compareRows, compare.enabled]);

  const chartData = useMemo(
    () => dayBlocks.map((block) => ({ label: block.date, value: block.dailyTotal.totalAttendees })),
    [dayBlocks],
  );

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('sessionAttendance', 'Session Attendance'),
      headers: [
        t('date', 'Date'),
        t('sessionType', 'Session Type'),
        t('sessionSubject', 'Session Subject'),
        t('totalAttendees', 'Total Attendees'),
        ...AGE_GENDER_COLUMNS.map((col) => col.label),
        t('total', 'Total'),
      ],
      rows: [...rows]
        .filter((row) => row.total !== 0)
        .sort((a, b) => a.sessionDate.localeCompare(b.sessionDate))
        .map((row) => [
          row.sessionDate,
          sessionTypeLabel(t, row.sessionType),
          row.sessionSubject ?? '',
          row.totalAttendees,
          ...AGE_GENDER_COLUMNS.map((col) => row.counts?.[`${col.ageGroup}_${col.gender}`] ?? 0),
          row.total,
        ]),
    }),
    [t, rows],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(() => {
    if (!compare.enabled) {
      return [];
    }
    return [
      buildComparisonExportSheet(
        comparisonTableRows,
        t('sessionType', 'Session Type'),
        compare.primary.label,
        compare.comparison.label,
        t,
      ),
      buildKpiExportSheet(kpiItems, t),
    ];
  }, [t, compare.enabled, comparisonTableRows, compare.primary.label, compare.comparison.label, kpiItems]);

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  function openDrilldown(
    sessionDate: string,
    sessionType: string,
    ageGroup?: string,
    gender?: string,
    ageGroupLabel?: string,
  ) {
    setSelection({ sessionDate, sessionType, ageGroup, gender, ageGroupLabel });
  }

  function goToPatientChart(patientUuid: string) {
    navigate({ to: `\${openmrsSpaBase}/patient/${patientUuid}/chart/visits` });
  }

  return (
    <div>
      <BackToReportsLink />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('sessionAttendanceReportTitle', 'Session Attendance Report')}</h2>

        {!dataLoading && <KpiTiles items={kpiItems} />}

        {!dataLoading && compare.enabled && (
          <>
            <h3 className={pageStyles.pageHeading}>
              {t('sessionTypeComparisonHeading', 'Session type comparison: {{primary}} vs {{comparison}}', {
                primary: compare.primary.label,
                comparison: compare.comparison.label,
              })}
            </h3>
            <ComparisonSummaryTable
              rows={comparisonTableRows}
              rowLabel={t('sessionType', 'Session Type')}
              currentLabel={compare.primary.label}
              compareLabel={compare.comparison.label}
              emptyMessage={t('noDataForEitherPeriod', 'No data found for either period.')}
            />
          </>
        )}

        <MonthCompareControls {...compare} />

        {!compare.enabled && (
          <div className={pageStyles.filterTile}>
            <div className={pageStyles.filterField}>
              <label htmlFor="startDate">{t('startDate', 'Start Date')}</label>
              <input
                id="startDate"
                type="date"
                value={startDateInput}
                max={getTodayDateString()}
                onChange={(e) => setStartDateInput(clampToToday(e.target.value))}
              />
            </div>
            <div className={pageStyles.filterField}>
              <label htmlFor="endDate">{t('endDate', 'End Date')}</label>
              <input
                id="endDate"
                type="date"
                value={endDateInput}
                max={getTodayDateString()}
                onChange={(e) => setEndDateInput(clampToToday(e.target.value))}
              />
            </div>
            <Button size="md" onClick={applyFilter}>
              {t('filter', 'Filter')}
            </Button>
          </div>
        )}

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField}>
            <Select
              id="sessionTypeFilter"
              labelText={t('sessionType', 'Session Type')}
              value={sessionTypeFilter}
              onChange={(e) => setSessionTypeFilter(e.target.value)}
            >
              <SelectItem value="" text={t('allSessionTypes', 'All session types')} />
              {SESSION_TYPES_IN_ORDER.map((type) => (
                <React.Fragment key={type}>
                  <SelectItem value={type} text={sessionTypeLabel(t, type)} />
                </React.Fragment>
              ))}
            </Select>
          </div>
        </div>

        <ExportButtons
          filenameBase="session-attendance-report"
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
                  <th rowSpan={2} className="left">
                    {t('sessionType', 'Session Type')}
                  </th>
                  <th rowSpan={2} className="left">
                    {t('sessionSubject', 'Session Subject')}
                  </th>
                  <th rowSpan={2}>{t('totalAttendees', 'Total Attendees')}</th>
                  <th colSpan={2}>0-4</th>
                  <th colSpan={2}>5-14</th>
                  <th colSpan={2}>15-18</th>
                  <th colSpan={2}>19-49</th>
                  <th colSpan={2}>50-65</th>
                  <th colSpan={2}>65+</th>
                  <th rowSpan={2}>Total</th>
                </tr>
                <tr>
                  {AGE_GENDER_COLUMNS.map((col) => (
                    <th key={col.label}>{col.gender}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {dayBlocks.map((block) => (
                  <React.Fragment key={block.date}>
                    <tr className={pageStyles.dayHeaderRow}>
                      <td colSpan={16}>{t('dayLabel', 'Day: {{date}}', { date: block.date })}</td>
                    </tr>
                    {block.rows.map((row) => (
                      <tr key={row.sessionType}>
                        <td className="left">{sessionTypeLabel(t, row.sessionType)}</td>
                        <td className="left">{row.sessionSubject ?? ''}</td>
                        <td>
                          {row.totalAttendees > 0 ? (
                            <button
                              className={pageStyles.linkCell}
                              onClick={() => openDrilldown(row.sessionDate, row.sessionType)}
                            >
                              {row.totalAttendees}
                            </button>
                          ) : (
                            row.totalAttendees
                          )}
                        </td>
                        {AGE_GENDER_COLUMNS.map((col) => {
                          const key = `${col.ageGroup}_${col.gender}`;
                          const count = row.counts?.[key] ?? 0;
                          return (
                            <td key={key}>
                              {count > 0 ? (
                                <button
                                  className={pageStyles.linkCell}
                                  onClick={() =>
                                    openDrilldown(row.sessionDate, row.sessionType, col.ageGroup, col.gender, col.label)
                                  }
                                >
                                  {count}
                                </button>
                              ) : (
                                count
                              )}
                            </td>
                          );
                        })}
                        <td>
                          {row.total > 0 ? (
                            <button
                              className={pageStyles.linkCell}
                              onClick={() => openDrilldown(row.sessionDate, row.sessionType)}
                            >
                              {row.total}
                            </button>
                          ) : (
                            row.total
                          )}
                        </td>
                      </tr>
                    ))}
                    <tr className={pageStyles.dailyTotalRow}>
                      <td className="left" colSpan={2}>
                        {t('totalOfTheDay', 'Total of the day')}
                      </td>
                      <td>{block.dailyTotal.totalAttendees}</td>
                      {AGE_GENDER_COLUMNS.map((col) => (
                        <td key={col.label}>{block.dailyTotal.counts[`${col.ageGroup}_${col.gender}`]}</td>
                      ))}
                      <td>{block.dailyTotal.total}</td>
                    </tr>
                  </React.Fragment>
                ))}
                {dayBlocks.length === 0 && (
                  <tr>
                    <td colSpan={16} className={pageStyles.emptyState}>
                      {t('noDataForSelection', 'No data found for this selection.')}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}

        {!dataLoading && viewMode === 'graph' && (
          <SimpleBarChart data={chartData} emptyMessage={t('noDataForSelection', 'No data found for this selection.')} />
        )}
      </div>

      {selection && (
        <Modal
          open
          modalHeading={`${selection.sessionDate} » ${sessionTypeLabel(t, selection.sessionType)}${
            selection.ageGroupLabel ? ` » ${selection.ageGroupLabel}` : ''
          }`}
          passiveModal
          onRequestClose={() => setSelection(null)}
        >
          {patientsLoading && <InlineLoading description={t('loadingPatients', 'Loading patients...')} />}
          {!patientsLoading && patients.length === 0 && (
            <p>{t('noPatientsForSelection', 'No patients found for this selection.')}</p>
          )}
          {!patientsLoading && patients.length > 0 && (
            <div className={pageStyles.tableContainer}>
              <table className={pageStyles.dataTable}>
                <thead>
                  <tr>
                    <th className="left">{t('name', 'Name')}</th>
                    <th className="left">{t('identifier', 'Identifier')}</th>
                    <th className="left">{t('sex', 'Sex')}</th>
                    <th className="left">{t('nationalId', 'National ID')}</th>
                    <th className="left">{t('phoneNumber', 'Phone Number')}</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map((patient) => (
                    <tr
                      key={patient.patientId}
                      className={pageStyles.clickableRow}
                      onClick={() => goToPatientChart(patient.patientUuid)}
                    >
                      <td className="left">
                        {patient.givenName} {patient.familyName}
                      </td>
                      <td className="left">{patient.identifier}</td>
                      <td className="left">{patient.sex}</td>
                      <td className="left">{patient.nationalId}</td>
                      <td className="left">{patient.phoneNumber}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </Modal>
      )}
    </div>
  );
}
