import React, { useMemo, useState } from 'react';
import { InlineLoading, Button, ContentSwitcher, Switch, NumberInput } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import pageStyles from '../reports-shell/reports-page.scss';
import { usePatientEncounterSummary } from './patient-encounter-summary.resource';

export default function PatientEncounterSummaryReport() {
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [minAge, setMinAge] = useState<number | ''>('');
  const [maxAge, setMaxAge] = useState<number | ''>('');
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');

  const { rows, isLoading } = usePatientEncounterSummary(appliedDates.startDate, appliedDates.endDate);

  const filteredRows = useMemo(() => {
    return rows.filter((row) => {
      if (minAge !== '' && row.age < minAge) {
        return false;
      }
      if (maxAge !== '' && row.age > maxAge) {
        return false;
      }
      return true;
    });
  }, [rows, minAge, maxAge]);

  const chartData = useMemo(
    () =>
      filteredRows.map((row) => ({
        label: `${row.givenName} ${row.familyName}`,
        value: row.encounterCount,
      })),
    [filteredRows],
  );

  const kpiItems = useMemo(() => {
    const totalEncounters = filteredRows.reduce((sum, row) => sum + row.encounterCount, 0);
    const mostRecentDate = filteredRows.reduce<string | null>(
      (latest, row) => (!latest || row.mostRecentEncounterDate > latest ? row.mostRecentEncounterDate : latest),
      null,
    );
    return [
      { label: 'Total Patients', value: filteredRows.length },
      { label: 'Total Encounters', value: totalEncounters },
      {
        label: 'Avg Encounters / Patient',
        value: filteredRows.length > 0 ? (totalEncounters / filteredRows.length).toFixed(1) : '0',
      },
      { label: 'Most Recent Encounter', value: mostRecentDate ?? '—' },
    ];
  }, [filteredRows]);

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
        <h2 className={pageStyles.pageHeading}>Patient Encounter Summary Report</h2>

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

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField} style={{ minWidth: '10rem' }}>
            <NumberInput
              id="minAge"
              label="Min Age"
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
              label="Max Age"
              size="md"
              value={maxAge}
              min={0}
              allowEmpty
              onChange={(_e, { value }) => setMaxAge(value === '' ? '' : Number(value))}
            />
          </div>
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
                  <th className="left">Patient Name</th>
                  <th>Age</th>
                  <th>Number of Encounters</th>
                  <th>Most Recent Encounter Date</th>
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
                      No patients found for this selection.
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        )}

        {!isLoading && viewMode === 'graph' && (
          <SimpleBarChart data={chartData} emptyMessage="No patients found for this selection." />
        )}
      </div>
    </div>
  );
}
