import React from 'react';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function LabTestSummaryLink() {
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/lab-test-summary-report`}>
        <div>
          <div className="heading">Lab Test Summary Report</div>
          <div className="content">Category / test / age / gender breakdown with drill-down</div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
