import React from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function PatientEncounterSummaryLink() {
  const { t } = useTranslation();
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/patient-encounter-summary-report`}>
        <div>
          <div className="heading">{t('patientVisitSummaryReportTitle', 'Patient Visit Summary Report')}</div>
          <div className="content">
            {t('patientVisitSummaryReportShortDesc', 'Patients by visit count, click a row to open their chart')}
          </div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
