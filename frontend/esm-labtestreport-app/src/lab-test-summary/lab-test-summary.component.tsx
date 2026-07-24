import React, { useMemo, useState } from 'react';
import { Modal, InlineLoading, Button, Search, Select, SelectItem, ContentSwitcher, Switch } from '@carbon/react';
import { ChevronDown, ChevronRight } from '@carbon/react/icons';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import SimpleBarChart from '../reports-shell/simple-bar-chart.component';
import KpiTiles from '../reports-shell/kpi-tiles.component';
import pageStyles from '../reports-shell/reports-page.scss';
import { useSummaryReport, useDrilldown, type DrilldownParams, type SummaryRow } from './lab-test-summary.resource';

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

interface Selection extends DrilldownParams {
  category: string;
  testLabel: string;
  ageGroupLabel?: string;
}

interface CategoryGroup {
  category: string;
  categoryConceptId: number;
  rows: SummaryRow[];
  totalTests: number;
  grandTotal: number;
  columnTotals: Record<string, number>;
}

function groupByCategory(list: SummaryRow[]): CategoryGroup[] {
  const groups: CategoryGroup[] = [];
  let current: CategoryGroup | null = null;
  list.forEach((row) => {
    if (!current || current.categoryConceptId !== row.categoryConceptId) {
      current = {
        category: row.category,
        categoryConceptId: row.categoryConceptId,
        rows: [],
        totalTests: 0,
        grandTotal: 0,
        columnTotals: {},
      };
      AGE_GENDER_COLUMNS.forEach((col) => (current!.columnTotals[`${col.ageGroup}_${col.gender}`] = 0));
      groups.push(current);
    }
    current.rows.push(row);
    current.totalTests += row.totalTests;
    current.grandTotal += row.total;
    AGE_GENDER_COLUMNS.forEach((col) => {
      const key = `${col.ageGroup}_${col.gender}`;
      current!.columnTotals[key] += row.counts?.[key] ?? 0;
    });
  });
  return groups;
}

export default function LabTestSummaryReport() {
  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});
  const [selection, setSelection] = useState<Selection | null>(null);
  const [searchText, setSearchText] = useState('');
  const [categoryFilter, setCategoryFilter] = useState('');
  const [collapsedCategories, setCollapsedCategories] = useState<Set<number>>(new Set());
  const [viewMode, setViewMode] = useState<'table' | 'graph'>('table');

  const { rows, isLoading } = useSummaryReport(appliedDates.startDate, appliedDates.endDate);
  const { patients, isLoading: patientsLoading } = useDrilldown(selection);

  const categories = useMemo(
    () => Array.from(new Set(rows.map((row) => row.category))).sort((a, b) => a.localeCompare(b)),
    [rows],
  );

  const filteredRows = useMemo(() => {
    const search = searchText.trim().toLowerCase();
    return rows.filter((row) => {
      if (categoryFilter && row.category !== categoryFilter) {
        return false;
      }
      if (!search) {
        return true;
      }
      return row.category.toLowerCase().includes(search) || row.testLabel.toLowerCase().includes(search);
    });
  }, [rows, searchText, categoryFilter]);

  const groupedRows = useMemo(() => groupByCategory(filteredRows), [filteredRows]);

  const totals = useMemo(() => {
    const columnTotals: Record<string, number> = {};
    AGE_GENDER_COLUMNS.forEach((col) => (columnTotals[`${col.ageGroup}_${col.gender}`] = 0));
    let totalTests = 0;
    let grandTotal = 0;
    filteredRows.forEach((row) => {
      totalTests += row.totalTests;
      grandTotal += row.total;
      AGE_GENDER_COLUMNS.forEach((col) => {
        const key = `${col.ageGroup}_${col.gender}`;
        columnTotals[key] += row.counts?.[key] ?? 0;
      });
    });
    return { totalTests, grandTotal, columnTotals };
  }, [filteredRows]);

  const chartData = useMemo(
    () => groupedRows.map((group) => ({ label: group.category, value: group.totalTests })),
    [groupedRows],
  );

  const topCategory = useMemo(
    () => groupedRows.reduce<CategoryGroup | null>((top, group) => (!top || group.totalTests > top.totalTests ? group : top), null),
    [groupedRows],
  );

  const kpiItems = useMemo(
    () => [
      { label: 'Total Tests', value: totals.totalTests },
      { label: 'Categories', value: groupedRows.length },
      { label: 'Lab Tests Tracked', value: filteredRows.length },
      { label: 'Top Category', value: topCategory ? topCategory.category : '—' },
    ],
    [totals, groupedRows, filteredRows, topCategory],
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
    testConceptId: number,
    category: string,
    testLabel: string,
    ageGroup?: string,
    gender?: string,
    ageGroupLabel?: string,
  ) {
    setSelection({
      testConceptId,
      ageGroup,
      gender,
      startDate: appliedDates.startDate,
      endDate: appliedDates.endDate,
      category,
      testLabel,
      ageGroupLabel,
    });
  }

  function goToPatientChart(patientUuid: string) {
    navigate({ to: `\${openmrsSpaBase}/patient/${patientUuid}/chart/visits` });
  }

  return (
    <div>
      <ReportsTabs activeKey="lab-test-summary" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>Lab Test Summary Report</h2>

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
          <div className={pageStyles.filterField} style={{ minWidth: '16rem' }}>
            <Search
              size="md"
              labelText="Search by category or lab test"
              placeholder="Search by category or lab test..."
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              onClear={() => setSearchText('')}
            />
          </div>
          <div className={pageStyles.filterField} style={{ minWidth: '14rem' }}>
            <Select
              id="categoryFilter"
              labelText="Category"
              value={categoryFilter}
              onChange={(e) => setCategoryFilter(e.target.value)}
            >
              <SelectItem value="" text="All categories" />
              {categories.map((category) => (
                <React.Fragment key={category}>
                  <SelectItem value={category} text={category} />
                </React.Fragment>
              ))}
            </Select>
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
                  <th rowSpan={2} className="left">
                    Category
                  </th>
                  <th rowSpan={2} className="left">
                    Lab Test
                  </th>
                  <th rowSpan={2}>Total Tests</th>
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
                        <td>{group.totalTests}</td>
                        {AGE_GENDER_COLUMNS.map((col) => (
                          <td key={col.label}>{group.columnTotals[`${col.ageGroup}_${col.gender}`]}</td>
                        ))}
                        <td>{group.grandTotal}</td>
                      </tr>
                      {!collapsed &&
                        group.rows.map((row, index) => (
                          <tr key={`${row.testConceptId}-${index}`}>
                            <td className="left" />
                            <td className="left">{row.testLabel}</td>
                            <td>
                              {row.totalTests > 0 ? (
                                <button
                                  className={pageStyles.linkCell}
                                  onClick={() => openDrilldown(row.testConceptId, row.category, row.testLabel)}
                                >
                                  {row.totalTests}
                                </button>
                              ) : (
                                row.totalTests
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
                                          row.testConceptId,
                                          row.category,
                                          row.testLabel,
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
                                  onClick={() => openDrilldown(row.testConceptId, row.category, row.testLabel)}
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
                      No data found for this selection.
                    </td>
                  </tr>
                )}
              </tbody>
              {groupedRows.length > 0 && (
                <tfoot>
                  <tr>
                    <td className="left" colSpan={2}>
                      <strong>Totals ({filteredRows.length} tests)</strong>
                    </td>
                    <td>
                      <strong>{totals.totalTests}</strong>
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

        {!isLoading && viewMode === 'graph' && (
          <SimpleBarChart data={chartData} emptyMessage="No data found for this selection." />
        )}
      </div>

      {selection && (
        <Modal
          open
          modalHeading={`${selection.category} » ${selection.testLabel}${
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
