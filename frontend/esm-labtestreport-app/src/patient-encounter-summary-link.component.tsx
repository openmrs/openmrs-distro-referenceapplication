import React from 'react';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function PatientEncounterSummaryLink() {
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/patient-encounter-summary-report`}>
        <div>
          <div className="heading">Patient Encounter Summary Report</div>
          <div className="content">Patients by encounter count, click a row to open their chart</div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
