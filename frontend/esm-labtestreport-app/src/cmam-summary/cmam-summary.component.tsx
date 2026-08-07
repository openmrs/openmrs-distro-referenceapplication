import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Modal, InlineLoading, Button } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import ReportsTabs from '../reports-shell/reports-tabs.component';
import ExportButtons from '../reports-shell/export-buttons.component';
import { type ExportSheet } from '../reports-shell/export-utils';
import pageStyles from '../reports-shell/reports-page.scss';
import {
  useCmamSummaryReport,
  useCmamDrilldown,
  type CmamDimension,
  type CmamSummaryRow,
} from './cmam-summary.resource';

interface Selection {
  dimension: CmamDimension;
  categoryConceptId: number;
  dimensionLabel: string;
  category: string;
}

function goToPatientChart(patientUuid: string) {
  navigate({ to: `\${openmrsSpaBase}/patient/${patientUuid}/chart/visits` });
}

export default function CmamSummaryReport() {
  const { t } = useTranslation();

  const [startDateInput, setStartDateInput] = useState('');
  const [endDateInput, setEndDateInput] = useState('');
  const [appliedDates, setAppliedDates] = useState<{ startDate?: string; endDate?: string }>({});

  const { rows, isLoading } = useCmamSummaryReport(appliedDates.startDate, appliedDates.endDate);

  const [selection, setSelection] = useState<Selection | null>(null);
  const { patients, isLoading: patientsLoading } = useCmamDrilldown(
    selection
      ? {
          dimension: selection.dimension,
          categoryConceptId: selection.categoryConceptId,
          startDate: appliedDates.startDate,
          endDate: appliedDates.endDate,
        }
      : null,
  );

  const visibleRows = useMemo(() => rows.filter((row) => row.total !== 0), [rows]);

  const rowsByDimension = useMemo(() => {
    const grouped: Record<CmamDimension, CmamSummaryRow[]> = {
      currentDiagnosis: [],
      childLastStatus: [],
      alertStatus: [],
    };
    visibleRows.forEach((row) => {
      grouped[row.dimension]?.push(row);
    });
    return grouped;
  }, [visibleRows]);

  const dimensionLabels: Record<CmamDimension, string> = {
    currentDiagnosis: t('currentDiagnosis', 'Current Diagnosis'),
    childLastStatus: t('childLastStatus', 'Child Last Status'),
    alertStatus: t('alertStatus', 'Alert Status'),
  };

  const mainExportSheet = useMemo<ExportSheet>(
    () => ({
      name: t('cmamFollowUpReportTitle', 'CMAM Follow-up Summary Report'),
      headers: [t('dimension', 'Dimension'), t('category', 'Category'), t('numberOfChildren', 'Number of Children')],
      rows: visibleRows.map((row) => [dimensionLabels[row.dimension] ?? row.dimension, row.category, row.total]),
    }),
    [t, visibleRows],
  );

  const exportExtraSheets = useMemo<Array<ExportSheet>>(
    () =>
      (Object.keys(rowsByDimension) as Array<CmamDimension>).map((dimension) => ({
        name: dimensionLabels[dimension],
        headers: [t('category', 'Category'), t('numberOfChildren', 'Number of Children')],
        rows: rowsByDimension[dimension].map((row) => [row.category, row.total]),
      })),
    [t, rowsByDimension],
  );

  const applyFilter = () => {
    setAppliedDates({ startDate: startDateInput || undefined, endDate: endDateInput || undefined });
  };

  const openDrilldown = (dimension: CmamDimension, dimensionLabel: string, row: CmamSummaryRow) => {
    setSelection({ dimension, dimensionLabel, categoryConceptId: row.categoryConceptId, category: row.category });
  };

  const renderDimensionTable = (dimension: CmamDimension, dimensionLabel: string) => {
    const dimensionRows = rowsByDimension[dimension];
    return (
      <div key={dimension} className={pageStyles.tableContainer} style={{ marginBottom: '1.5rem' }}>
        <h4 className={pageStyles.pageHeading} style={{ fontSize: '1rem' }}>
          {dimensionLabel}
        </h4>
        <table className={pageStyles.dataTable}>
          <thead>
            <tr>
              <th className="left">{t('category', 'Category')}</th>
              <th>{t('numberOfChildren', 'Number of Children')}</th>
            </tr>
          </thead>
          <tbody>
            {dimensionRows.map((row) => (
              <tr key={row.categoryConceptId}>
                <td className="left">{row.category}</td>
                <td>
                  <button className={pageStyles.linkCell} onClick={() => openDrilldown(dimension, dimensionLabel, row)}>
                    {row.total}
                  </button>
                </td>
              </tr>
            ))}
            {dimensionRows.length === 0 && (
              <tr>
                <td colSpan={2} className={pageStyles.emptyState}>
                  {t('noDataForSelection', 'No data found for this selection.')}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    );
  };

  return (
    <div>
      <ReportsTabs activeKey="cmam-follow-up" />
      <div className={pageStyles.pageBody}>
        <h2 className={pageStyles.pageHeading}>{t('cmamFollowUpReportTitle', 'CMAM Follow-up Summary Report')}</h2>

        <div className={pageStyles.filterTile}>
          <div className={pageStyles.filterField}>
            <label htmlFor="startDate">{t('startDate', 'Start Date')}</label>
            <input id="startDate" type="date" value={startDateInput} onChange={(e) => setStartDateInput(e.target.value)} />
          </div>
          <div className={pageStyles.filterField}>
            <label htmlFor="endDate">{t('endDate', 'End Date')}</label>
            <input id="endDate" type="date" value={endDateInput} onChange={(e) => setEndDateInput(e.target.value)} />
          </div>
          <Button size="md" onClick={applyFilter}>
            {t('filter', 'Filter')}
          </Button>
        </div>

        <ExportButtons
          filenameBase="cmam-follow-up-report"
          mainSheet={mainExportSheet}
          extraSheets={exportExtraSheets}
          disabled={isLoading}
        />

        {isLoading && <InlineLoading description={t('loadingReport', 'Loading report...')} />}

        {!isLoading && (
          <>
            {renderDimensionTable('currentDiagnosis', t('currentDiagnosis', 'Current Diagnosis'))}
            {renderDimensionTable('childLastStatus', t('childLastStatus', 'Child Last Status'))}
            {renderDimensionTable('alertStatus', t('alertStatus', 'Alert Status'))}
          </>
        )}

        {selection && (
          <Modal
            open
            modalHeading={`${selection.dimensionLabel} » ${selection.category}`}
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
                      <th className="left">{t('currentDiagnosis', 'Current Diagnosis')}</th>
                      <th className="left">{t('childLastStatus', 'Child Last Status')}</th>
                      <th className="left">{t('alertStatus', 'Alert Status')}</th>
                      <th className="left">{t('nextVisitDate', 'Next Visit Date')}</th>
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
                        <td className="left">{patient.currentDiagnosis}</td>
                        <td className="left">{patient.childLastStatus}</td>
                        <td className="left">{patient.alertStatus}</td>
                        <td className="left">{patient.nextVisitDate}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </Modal>
        )}
      </div>
    </div>
  );
}
