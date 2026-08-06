import React from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function CmamSummaryLink() {
  const { t } = useTranslation();
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/cmam-follow-up-report`}>
        <div>
          <div className="heading">{t('cmamFollowUpReportTitle', 'CMAM Follow-up Summary Report')}</div>
          <div className="content">
            {t(
              'cmamFollowUpReportShortDesc',
              'Children by Current Diagnosis, Child Last Status and Alert Status, with drill-down',
            )}
          </div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
