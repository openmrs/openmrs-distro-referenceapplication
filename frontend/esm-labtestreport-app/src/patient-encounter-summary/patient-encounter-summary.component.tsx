import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { InlineLoading, Button, ContentSwitcher, Switch, NumberInput } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import MonthCompareControls from '../reports-shell/month-compare-controls.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { buildVisitDetailExportSheet, buildKpiExportSheet, type ExportSheet } from '../reports-shell/export-utils';
import { useMonthComparison } from '../reports-shell/month-compare';
import pageStyles from '../reports-shell/reports-page.scss';
import { getTodayDateString, clampToToday } from '../reports-shell/date-utils';
import {
  usePatientEncounterDetails,
  usePatientEncounterSummary,
  type PatientEncounterSummaryRow,
} from './patient-encounter-summary.resource';

// `location` and `serviceType` are comma-joined lists (a patient can visit more than one
// location, or be enrolled in more than one program, within the report period).
function splitCommaList(value: string | null | undefined): Array<string> {
  return (value ?? '')
    .split(',')
    .map((token) => token.trim())
    .filter(Boolean);
}

function uniqueSorted(values: Array<string>): Array<string> {
  return Array.from(new Set(values)).sort((a, b) => a.localeCompare(b));
}

function summarize(
  rows: Array<PatientEncounterSummaryRow>,
  minAge: number | '',
  maxAge: number | '',
  location: string,
  serviceType: string,
) {
  const filteredRows = rows.filter((row) => {
    if (row.visitCount === 0) {
      return false;
    }
    if (minAge !== '' && (row.age == null || row.age < minAge)) {
      return false;
    }
    if (maxAge !== '' && (row.age == null || row.age > maxAge)) {
      return false;
    }
    if (location && !splitCommaList(row.location).includes(location)) {
      return false;
    }
    if (serviceType && !splitCommaList(row.serviceType).includes(serviceType)) {
      return false;
    }
    return true;
  });
  const totalVisits = filteredRows.reduce((sum, row) => sum + row.visitCount, 0);
  const mostRecentDate = filteredRows.reduce<string | null>(
    (latest, row) => (!latest || row.mostRecentVisitDate > latest ? row.mostRecentVisitDate : latest),
    null,
  );
  return { filteredRows, totalVisits, mostRecentDate };
}

export default function PatientEncounterSummaryReport() {
  const { t } = useTranslation();
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [minAge, setMinAge] = useState<number | ''>('');
  const [maxAge, setMaxAge] = useState<number | ''>('');
  const [selectedLocation, setSelectedLocation] = useState('');
  const [selectedServiceType, setSelectedServiceType] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
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

  const { rows: detailRows, isLoading: detailsLoading } = usePatientEncounterDetails(
    primaryStartDate,
    primaryEndDate,
  );

  const primary = useMemo(
    () => summarize(rows, minAge, maxAge, selectedLocation, selectedServiceType),
    [rows, minAge, maxAge, selectedLocation, selectedServiceType],
  );
  const { filteredRows } = primary;

  const compareSummary = useMemo(
    () => (compare.enabled ? summarize(compareRowsRaw, minAge, maxAge, selectedLocation, selectedServiceType) : null),
    [compareRowsRaw, minAge, maxAge, selectedLocation, selectedServiceType, compare.enabled],
  );

  const locationOptions = useMemo(() => uniqueSorted(rows.flatMap((row) => splitCommaList(row.location))), [rows]);
  const serviceTypeOptions = useMemo(
    () => uniqueSorted(rows.flatMap((row) => splitCommaList(row.serviceType))),
    [rows],
  );

  const searchedRows = useMemo(() => {
    const term = searchTerm.trim().toLowerCase();
    if (!term) {
      return filteredRows;
    }
    return filteredRows.filter((row) =>
      [row.givenName, row.familyName, row.nationalId, row.phoneNumber, row.location, row.serviceType]
        .filter(Boolean)
        .some((field) => field.toLowerCase().includes(term)),
    );
  }, [filteredRows, searchTerm]);

  const chartData = useMemo(
    () =>
      filteredRows.map((row) => ({
        label: `${row.givenName} ${row.familyName}`,
        value: row.visitCount,
      })),
    [filteredRows],
  );

  const kpiItems = useMemo(() => {
    const items = [
      { label: t('totalPatients', 'Total Patients'), value: filteredRows.length },
      { label: t('totalVisits', 'Total Visits'), value: primary.totalVisits },
      {
        label: t('avgVisitsPerPatient', 'Avg Visits / Patient'),
        value: filteredRows.length > 0 ? (primary.totalVisits / filteredRows.length).toFixed(1) : '0',
      },
      { label: t('mostRecentVisit', 'Most Recent Visit'), value: primary.mostRecentDate ?? '—' },
    ];
    if (!compare.enabled || !compareSummary) {
      return items;
    }
    const compareValues: Array<React.ReactNode> = [
      compareSummary.filteredRows.length,
      compareSummary.totalVisits,
      compareSummary.filteredRows.length > 0
        ? (compareSummary.totalVisits / compareSummary.filteredRows.length).toFixed(1)
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
      name: t('patientVisitSummary', 'Patient Visit Summary'),
      headers: [
        t('givenName', 'Given Name'),
        t('familyName', 'Family Name'),
        t('age', 'Age'),
        t('numberOfVisits', 'Number of Visits'),
        t('mostRecentVisitDate', 'Most Recent Visit Date'),
        t('location', 'Location'),
        t('serviceType', 'Service Type'),
      ],
      rows: filteredRows.map((row) => [
        row.givenName,
        row.familyName,
        row.age,
        row.visitCount,
        row.mostRecentVisitDate,
        row.location ?? '',
        row.serviceType ?? '',
      ]),
    }),
    [t, filteredRows],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(() => {
    const filteredPatientIds = new Set(filteredRows.map((row) => row.patientId));
    const visitDetailSheet = buildVisitDetailExportSheet(
      detailRows.filter((detail) => filteredPatientIds.has(detail.patientId)),
      t,
    );
    return compare.enabled ? [buildKpiExportSheet(kpiItems, t), visitDetailSheet] : [visitDetailSheet];
  }, [t, compare.enabled, kpiItems, detailRows, filteredRows]);

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
          {t('patientVisitSummaryReportTitle', 'Patient Visit Summary Report')}
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
          <div className={pageStyles.filterField}>
            <label htmlFor="locationFilter">{t('location', 'Location')}</label>
            <select
              id="locationFilter"
              value={selectedLocation}
              onChange={(e: React.ChangeEvent<HTMLSelectElement>) => setSelectedLocation(e.target.value)}
            >
              <option value="">{t('all', 'All')}</option>
              {locationOptions.map((location: string) => (
                <option key={location} value={location}>
                  {location}
                </option>
              ))}
            </select>
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="serviceTypeFilter">{t('serviceType', 'Service Type')}</label>
            <select
              id="serviceTypeFilter"
              value={selectedServiceType}
              onChange={(e: React.ChangeEvent<HTMLSelectElement>) => setSelectedServiceType(e.target.value)}
            >
              <option value="">{t('all', 'All')}</option>
              {serviceTypeOptions.map((serviceType: string) => (
                <option key={serviceType} value={serviceType}>
                  {serviceType}
                </option>
              ))}
            </select>
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="searchTerm">{t('search', 'Search')}</label>
            <input
              id="searchTerm"
              type="search"
              placeholder={t('searchAllColumns', 'Search name, ID, phone, location...')}
              value={searchTerm}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>

        <ExportButtons
          filenameBase="patient-visit-summary-report"
          mainSheet={mainExportSheet}
          extraSheets={exportExtraSheets}
          disabled={dataLoading || detailsLoading}
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
                  <th className="left">{t('sex', 'Sex')}</th>
                  <th className="left">{t('nationalId', 'National ID')}</th>
                  <th className="left">{t('phoneNumber', 'Phone Number')}</th>
                  <th>{t('age', 'Age')}</th>
                  <th>{t('numberOfVisits', 'Number of Visits')}</th>
                  <th>{t('mostRecentVisitDate', 'Most Recent Visit Date')}</th>
                  <th className="left">{t('location', 'Location')}</th>
                  <th className="left">{t('serviceType', 'Service Type')}</th>
                </tr>
              </thead>
              <tbody>
                {searchedRows.map((row) => (
                  <tr
                    key={row.patientId}
                    className={pageStyles.clickableRow}
                    onClick={() => goToPatientChart(row.patientUuid)}
                  >
                    <td className="left">
                      {row.givenName} {row.familyName}
                    </td>
                    <td className="left">{row.sex}</td>
                    <td className="left">{row.nationalId}</td>
                    <td className="left">{row.phoneNumber}</td>
                    <td>{row.age}</td>
                    <td>{row.visitCount}</td>
                    <td>{row.mostRecentVisitDate}</td>
                    <td className="left">{row.location || '--'}</td>
                    <td className="left">{row.serviceType || '--'}</td>
                  </tr>
                ))}
                {searchedRows.length === 0 && (
                  <tr>
                    <td colSpan={9} className={pageStyles.emptyState}>
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
