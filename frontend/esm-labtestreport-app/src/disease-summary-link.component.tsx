import React from 'react';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function DiseaseSummaryLink() {
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/disease-summary-report`}>
        <div>
          <div className="heading">Disease Surveillance Summary Report</div>
          <div className="content">Category / diagnosis / age / gender breakdown with drill-down</div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
