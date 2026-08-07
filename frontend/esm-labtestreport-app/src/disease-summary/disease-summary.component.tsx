import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import {
  Modal,
  InlineLoading,
  Button,
  Search,
  Select,
  SelectItem,
  ContentSwitcher,
  Switch,
  NumberInput,
  InlineNotification,
} from '@carbon/react';
import { ChevronDown, ChevronRight } from '@carbon/react/icons';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
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
  useDiseaseSummaryReport,
  useDiseaseDrilldown,
  type DiseaseDrilldownParams,
  type DiseaseSummaryRow,
} from './disease-summary.resource';

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

interface Selection extends DiseaseDrilldownParams {
  category: string;
  diagnosisLabel: string;
  ageGroupLabel?: string;
}

interface CategoryGroup {
  category: string;
  categoryConceptId: number;
  rows: DiseaseSummaryRow[];
  totalCases: number;
  grandTotal: number;
  columnTotals: Record<string, number>;
}

function summarize(rows: DiseaseSummaryRow[], searchText: string, categoryFilter: string) {
  const search = searchText.trim().toLowerCase();
  const filteredRows = rows.filter((row) => {
    if (row.total === 0) {
      return false;
    }
    if (categoryFilter && row.category !== categoryFilter) {
      return false;
    }
    if (!search) {
      return true;
    }
    return row.category.toLowerCase().includes(search) || row.diagnosisLabel.toLowerCase().includes(search);
  });
  const groupedRows = groupByCategory(filteredRows);
  const columnTotals: Record<string, number> = {};
  AGE_GENDER_COLUMNS.forEach((col) => (columnTotals[`${col.ageGroup}_${col.gender}`] = 0));
  let totalCases = 0;
  let grandTotal = 0;
  filteredRows.forEach((row) => {
    totalCases += row.totalCases;
    grandTotal += row.total;
    AGE_GENDER_COLUMNS.forEach((col) => {
      const key = `${col.ageGroup}_${col.gender}`;
      columnTotals[key] += row.counts?.[key] ?? 0;
    });
  });
  const topCategory = groupedRows.reduce<CategoryGroup | null>(
    (top, group) => (!top || group.totalCases > top.totalCases ? group : top),
    null,
  );
  return { filteredRows, groupedRows, totals: { totalCases, grandTotal, columnTotals }, topCategory };
}

function groupByCategory(list: DiseaseSummaryRow[]): CategoryGroup[] {
  const groups: CategoryGroup[] = [];
  let current: CategoryGroup | null = null;
  list.forEach((row) => {
    if (!current || current.categoryConceptId !== row.categoryConceptId) {
      current = {
        category: row.category,
        categoryConceptId: row.categoryConceptId,
        rows: [],
        totalCases: 0,
        grandTotal: 0,
        columnTotals: {},
      };
      AGE_GENDER_COLUMNS.forEach((col) => (current!.columnTotals[`${col.ageGroup}_${col.gender}`] = 0));
      groups.push(current);
    }
    current.rows.push(row);
    current.totalCases += row.totalCases;
    current.grandTotal += row.total;
    AGE_GENDER_COLUMNS.forEach((col) => {
      const key = `${col.ageGroup}_${col.gender}`;
      current!.columnTotals[key] += row.counts?.[key] ?? 0;
    });
  });
  return groups;
}

export default function DiseaseSummaryReport() {
  const { t } = useTranslation();
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [selection, setSelection] = useState<Selection | null>(null);
  const [searchText, setSearchText] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [collapsedCategories, setCollapsedCategories] = useState<Set<number>>(new Set());
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');
  const [alertThreshold, setAlertThreshold] = useState<number>(10);
  const compare = useMonthComparison();

  const primaryStartDate = compare.enabled ? compare.primary.startDate : appliedDates.startDate;
  const primaryEndDate = compare.enabled ? compare.primary.endDate : appliedDates.endDate;

  const { rows, isLoading } = useDiseaseSummaryReport(primaryStartDate, primaryEndDate);
  const { rows: compareRowsRaw, isLoading: compareLoading } = useDiseaseSummaryReport(
    compare.comparison.startDate,
    compare.comparison.endDate,
    compare.enabled,
  );
  const { patients, isLoading: patientsLoading } = useDiseaseDrilldown(selection);
  const dataLoading = isLoading || (compare.enabled && compareLoading);

  const categories = useMemo(
    () => Array.from(new Set(rows.map((row) => row.category))).sort((a, b) => a.localeCompare(b)),
    [rows],
  );

  const primary = useMemo(() => summarize(rows, searchText, categoryFilter), [rows, searchText, categoryFilter]);
  const { filteredRows, groupedRows, totals, topCategory } = primary;

  const compareSummary = useMemo(
    () => (compare.enabled ? summarize(compareRowsRaw, searchText, categoryFilter) : null),
    [compareRowsRaw, searchText, categoryFilter, compare.enabled],
  );

  const chartData = useMemo(() => {
    const compareByCategory = new Map(
      (compareSummary?.groupedRows ?? []).map((group) => [group.category, group.totalCases]),
    );
    return groupedRows.map((group) => ({
      label: group.category,
      value: group.totalCases,
      compareValue: compare.enabled ? compareByCategory.get(group.category) ?? 0 : undefined,
    }));
  }, [groupedRows, compareSummary, compare.enabled]);

  const kpiItems = useMemo(() => {
    const items = [
      { label: t('totalCases', 'Total Cases'), value: totals.totalCases },
      { label: t('categories', 'Categories'), value: groupedRows.length },
      { label: t('diagnosesTracked', 'Diagnoses Tracked'), value: filteredRows.length },
      { label: t('topCategory', 'Top Category'), value: topCategory ? topCategory.category : '—' },
    ];
    if (!compare.enabled || !compareSummary) {
      return items;
    }
    const compareValues: Array<React.ReactNode> = [
      compareSummary.totals.totalCases,
      compareSummary.groupedRows.length,
      compareSummary.filteredRows.length,
      compareSummary.topCategory ? compareSummary.topCategory.category : '—',
    ];
    return items.map((item, index) => ({
      ...item,
      compareValue: compareValues[index],
      compareLabel: compare.comparison.label,
    }));
  }, [t, totals, groupedRows, filteredRows, topCategory, compare.enabled, compareSummary, compare.comparison.label]);

  const comparisonTableRows = useMemo(() => {
    if (!compare.enabled || !compareSummary) {
      return [];
    }
    const compareByCategory = new Map(compareSummary.groupedRows.map((group) => [group.category, group.totalCases]));
    const categoryUnion = new Set([
      ...groupedRows.map((group) => group.category),
      ...compareSummary.groupedRows.map((group) => group.category),
    ]);
    return Array.from(categoryUnion)
      .sort((a, b) => a.localeCompare(b))
      .map((category) => ({
        label: category,
        current: groupedRows.find((group) => group.category === category)?.totalCases ?? 0,
        compare: compareByCategory.get(category) ?? 0,
      }));
  }, [groupedRows, compareSummary, compare.enabled]);

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('diseaseSummary', 'Disease Summary'),
      headers: [
        t('category', 'Category'),
        t('diagnosis', 'Diagnosis'),
        t('totalCases', 'Total Cases'),
        ...AGE_GENDER_COLUMNS.map((col) => col.label),
        t('total', 'Total'),
      ],
      rows: filteredRows.map((row) => [
        row.category,
        row.diagnosisLabel,
        row.totalCases,
        ...AGE_GENDER_COLUMNS.map((col) => row.counts?.[`${col.ageGroup}_${col.gender}`] ?? 0),
        row.total,
      ]),
    }),
    [t, filteredRows],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(() => {
    if (!compare.enabled) {
      return [];
    }
    return [
      buildComparisonExportSheet(
        comparisonTableRows,
        t('category', 'Category'),
        compare.primary.label,
        compare.comparison.label,
        t,
      ),
      buildKpiExportSheet(kpiItems, t),
    ];
  }, [t, compare.enabled, comparisonTableRows, compare.primary.label, compare.comparison.label, kpiItems]);

  const outbreakAlerts = useMemo(
    () => groupedRows.filter((group) => group.totalCases >= alertThreshold),
    [groupedRows, alertThreshold],
  );

  function toggleCategory(categoryConceptId: number) {
    setCollapsedCategories((prev) => {
      const next = new Set(prev);
      if (next.has(categoryConceptId)) {
        next.delete(categoryConceptId);
      } else {
        next.add(categoryConceptId);
      }
      return next;
    });
  }

  function applyFilter() {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  }

  function openDrilldown(
    diagnosisConceptId: number,
    category: string,
    diagnosisLabel: string,
    ageGroup?: string,
    gender?: string,
    ageGroupLabel?: string,
  ) {
    setSelection({
      diagnosisConceptId,
      ageGroup,
      gender,
      startDate: primaryStartDate,
      endDate: primaryEndDate,
      category,
      diagnosisLabel,
      ageGroupLabel,
    });
  }

  function goToPatientChart(patientUuid: string) {
    navigate({ to: `\${openmrsSpaBase}/patient/${patientUuid}/chart/visits` });
  }

  return (
    <div>
      <ReportsTabs activeKey="disease-summary" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('diseaseSummaryReportTitle', 'Disease Surveillance Summary Report')}</h2>

        {!dataLoading && <KpiTiles items={kpiItems} />}

        {!dataLoading && outbreakAlerts.length > 0 && (
          <div className={pageStyles.alertStack}>
            {outbreakAlerts.map((group) => (
              <React.Fragment key={group.categoryConceptId}>
                <InlineNotification
                  kind="error"
                  lowContrast
                  hideCloseButton
                  title={t('outbreakAlert', 'Outbreak alert')}
                  subtitle={t(
                    'outbreakAlertSubtitle',
                    '{{category}} has {{count}} {{caseWord}} (threshold: {{threshold}})',
                    {
                      category: group.category,
                      count: group.totalCases,
                      caseWord: group.totalCases === 1 ? t('case', 'case') : t('casesPlural', 'cases'),
                      threshold: alertThreshold,
                    },
                  )}
                />
              </React.Fragment>
            ))}
          </div>
        )}

        {!dataLoading && compare.enabled && (
          <>
            <h3 className={pageStyles.pageHeading}>
              {t('categoryComparisonHeading', 'Category comparison: {{primary}} vs {{comparison}}', {
                primary: compare.primary.label,
                comparison: compare.comparison.label,
              })}
            </h3>
            <ComparisonSummaryTable
              rows={comparisonTableRows}
              rowLabel={t('category', 'Category')}
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
          <div className={pageStyles.filterField} style={{ minWidth: '16rem' }}>
            <Search
              size="md"
              labelText={t('searchByCategoryOrDiagnosis', 'Search by category or diagnosis')}
              placeholder={t('searchByCategoryOrDiagnosisPlaceholder', 'Search by category or diagnosis...')}
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              onClear={() => setSearchText('')}
            />
          </div>
          <div className={pageStyles.filterField} style={{ minWidth: '14rem' }}>
            <Select
              id="categoryFilter"
              labelText={t('category', 'Category')}
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
            >
              <SelectItem value="" text={t('allCategories', 'All categories')} />
              {categories.map((category) => (
                <React.Fragment key={category}>
                  <SelectItem value={category} text={category} />
                </React.Fragment>
              ))}
            </Select>
          </div>
          <div className={pageStyles.filterField} style={{ minWidth: '10rem' }}>
            <NumberInput
              id="alertThreshold"
              label={t('outbreakAlertThreshold', 'Outbreak alert threshold')}
              size="md"
              value={alertThreshold}
              min={1}
              onChange={(_e, { value }) => setAlertThreshold(Number(value) || 1)}
            />
          </div>
        </div>

        <ExportButtons
          filenameBase="disease-summary-report"
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
                    {t('category', 'Category')}
                  </th>
                  <th rowSpan={2} className="left">
                    {t('diagnosis', 'Diagnosis')}
                  </th>
                  <th rowSpan={2}>{t('totalCases', 'Total Cases')}</th>
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
                {groupedRows.map((group) => {
                  const collapsed = collapsedCategories.has(group.categoryConceptId);
                  return (
                    <React.Fragment key={group.categoryConceptId}>
                      <tr className={pageStyles.categoryHeaderRow} onClick={() => toggleCategory(group.categoryConceptId)}>
                        <td colSpan={2} className="left">
                          <button className={pageStyles.collapseToggle} aria-expanded={!collapsed}>
                            {collapsed ? <ChevronRight size={16} /> : <ChevronDown size={16} />}
                            {group.category} ({group.rows.length})
                          </button>
                        </td>
                        <td>{group.totalCases}</td>
                        {AGE_GENDER_COLUMNS.map((col) => (
                          <td key={col.label}>{group.columnTotals[`${col.ageGroup}_${col.gender}`]}</td>
                        ))}
                        <td>{group.grandTotal}</td>
                      </tr>
                      {!collapsed &&
                        group.rows.map((row, index) => (
                          <tr key={`${row.diagnosisConceptId}-${index}`}>
                            <td className="left" />
                            <td className="left">{row.diagnosisLabel}</td>
                            <td>
                              {row.totalCases > 0 ? (
                                <button
                                  className={pageStyles.linkCell}
                                  onClick={() => openDrilldown(row.diagnosisConceptId, row.category, row.diagnosisLabel)}
                                >
                                  {row.totalCases}
                                </button>
                              ) : (
                                row.totalCases
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
                                        openDrilldown(
                                          row.diagnosisConceptId,
                                          row.category,
                                          row.diagnosisLabel,
                                          col.ageGroup,
                                          col.gender,
                                          col.label,
                                        )
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
                                  onClick={() => openDrilldown(row.diagnosisConceptId, row.category, row.diagnosisLabel)}
                                >
                                  {row.total}
                                </button>
                              ) : (
                                row.total
                              )}
                            </td>
                          </tr>
                        ))}
                    </React.Fragment>
                  );
                })}
                {groupedRows.length === 0 && (
                  <tr>
                    <td colSpan={16} className={pageStyles.emptyState}>
                      {t('noDataForSelection', 'No data found for this selection.')}
                    </td>
                  </tr>
                )}
              </tbody>
              {groupedRows.length > 0 && (
                <tfoot>
                  <tr>
                    <td className="left" colSpan={2}>
                      <strong>
                        {t('totalsDiagnosesCount', 'Totals ({{count}} diagnoses)', { count: filteredRows.length })}
                      </strong>
                    </td>
                    <td>
                      <strong>{totals.totalCases}</strong>
                    </td>
                    {AGE_GENDER_COLUMNS.map((col) => (
                      <td key={col.label}>
                        <strong>{totals.columnTotals[`${col.ageGroup}_${col.gender}`]}</strong>
                      </td>
                    ))}
                    <td>
                      <strong>{totals.grandTotal}</strong>
                    </td>
                  </tr>
                </tfoot>
              )}
            </table>
          </div>
        )}

        {!dataLoading && viewMode === 'graph' && (
          <SimpleBarChart
            data={chartData}
            emptyMessage={t('noDataForSelection', 'No data found for this selection.')}
            currentLabel={compare.enabled ? compare.primary.label : undefined}
            compareLabel={compare.enabled ? compare.comparison.label : undefined}
          />
        )}
      </div>

      {selection && (
        <Modal
          open
          modalHeading={`${selection.category} » ${selection.diagnosisLabel}${
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
