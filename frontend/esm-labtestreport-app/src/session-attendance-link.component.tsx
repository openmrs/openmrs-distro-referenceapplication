import React from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function SessionAttendanceLink() {
  const { t } = useTranslation();
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/session-attendance-report`}>
        <div>
          <div className="heading">{t('sessionAttendanceReportTitle', 'Session Attendance Report')}</div>
          <div className="content">
            {t('sessionAttendanceReportShortDesc', 'Individual/Group session attendance by day, age and gender')}
          </div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
