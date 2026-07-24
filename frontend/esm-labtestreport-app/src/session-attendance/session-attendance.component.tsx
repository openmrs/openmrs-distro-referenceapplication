import React, { useMemo, useState } from 'react';
import { Modal, InlineLoading, Button, ContentSwitcher, Switch } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import pageStyles from '../reports-shell/reports-page.scss';
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
      blockRows.push(row);
      dailyTotal.totalAttendees += row.totalAttendees;
      dailyTotal.total += row.total;
      AGE_GENDER_COLUMNS.forEach((col) => {
        const key = `${col.ageGroup}_${col.gender}`;
        dailyTotal.counts[key] = (dailyTotal.counts[key] ?? 0) + (row.counts?.[key] ?? 0);
      });
    });
    blocks.push({ date, rows: blockRows, dailyTotal });
  });
  return blocks.sort((a, b) => a.date.localeCompare(b.date));
}

export default function SessionAttendanceReport() {
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [selection, setSelection] = useState<Selection | null>(null);
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');

  const { rows, isLoading } = useSessionAttendanceReport(appliedDates.startDate, appliedDates.endDate);
  const { patients, isLoading: patientsLoading } = useSessionAttendanceDrilldown(selection);

  const dayBlocks = useMemo(() => buildDayBlocks(rows), [rows]);

  const kpiItems = useMemo(() => {
    const totalAttendees = dayBlocks.reduce((sum, block) => sum + block.dailyTotal.totalAttendees, 0);
    const individualCount = rows
      .filter((row) => row.sessionType === 'Individual Sessions')
      .reduce((sum, row) => sum + row.totalAttendees, 0);
    const groupCount = rows
      .filter((row) => row.sessionType === 'Group Sessions')
      .reduce((sum, row) => sum + row.totalAttendees, 0);
    return [
      { label: 'Total Days', value: dayBlocks.length },
      { label: 'Total Attendees', value: totalAttendees },
      { label: 'Individual Sessions', value: individualCount },
      { label: 'Group Sessions', value: groupCount },
    ];
  }, [dayBlocks, rows]);

  const chartData = useMemo(
    () => dayBlocks.map((block) => ({ label: block.date, value: block.dailyTotal.totalAttendees })),
    [dayBlocks],
  );

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
      <ReportsTabs activeKey="session-attendance" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>Session Attendance Report</h2>

        {!isLoading && <KpiTiles items={kpiItems} />}

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField}>
            <label htmlFor="startDate">Start Date</label>
            <input
              id="startDate"
              type="date"
              value={startDateInput}
              onChange={(e) => setStartDateInput(e.target.value)}
            />
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="endDate">End Date</label>
            <input id="endDate" type="date" value={endDateInput} onChange={(e) => setEndDateInput(e.target.value)} />
          </div>
          <Button size="md" onClick={applyFilter}>
            Filter
          </Button>
        </div>

        <div className={pageStyles.viewSwitcher}>
          <ContentSwitcher
            size="md"
            selectedIndex={viewMode === 'table' ? 0 : 1}
            onChange={({ name }) => setViewMode(name as 'table' | 'graph')}
          >
            <Switch name="table" text="Table" />
            <Switch name="graph" text="Graph" />
          </ContentSwitcher>
        </div>

        {isLoading && <InlineLoading description="Loading report..." />}

        {!isLoading && viewMode === 'table' && (
          <div className={pageStyles.tableContainer}>
            <table className={pageStyles.dataTable}>
              <thead>
                <tr>
                  <th rowSpan={2} className="left">
                    Session Type
                  </th>
                  <th rowSpan={2} className="left">
                    Session Subject
                  </th>
                  <th rowSpan={2}>Total Attendees</th>
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
                      <td colSpan={16}>Day: {block.date}</td>
                    </tr>
                    {block.rows.map((row) => (
                      <tr key={row.sessionType}>
                        <td className="left">{row.sessionType}</td>
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
                        Total of the day
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
                      No data found for this selection.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}

        {!isLoading && viewMode === 'graph' && (
          <SimpleBarChart data={chartData} emptyMessage="No data found for this selection." />
        )}
      </div>

      {selection && (
        <Modal
          open
          modalHeading={`${selection.sessionDate} » ${selection.sessionType}${
            selection.ageGroupLabel ? ` » ${selection.ageGroupLabel}` : ''
          }`}
          passiveModal
          onRequestClose={() => setSelection(null)}
        >
          {patientsLoading && <InlineLoading description="Loading patients..." />}
          {!patientsLoading && patients.length === 0 && <p>No patients found for this selection.</p>}
          {!patientsLoading && patients.length > 0 && (
            <div className={pageStyles.tableContainer}>
              <table className={pageStyles.dataTable}>
                <thead>
                  <tr>
                    <th className="left">Name</th>
                    <th className="left">Identifier</th>
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
