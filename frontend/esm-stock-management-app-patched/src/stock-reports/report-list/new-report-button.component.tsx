import { Button } from '@carbon/react';
import React, { useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { launchWorkspace } from '@openmrs/esm-framework';

const NewReportActionButton: React.FC = () => {
  const { t } = useTranslation();

  const handleClick = useCallback(() => {
    launchWorkspace('stock-reports-form-workspace', {
      workspaceTitle: t('newReport', 'New Report'),
    });
  }, [t]);

  return (
    <Button onClick={handleClick} size="md" kind="primary">
      {t('newReport', 'New Report')}
    </Button>
  );
};

export default NewReportActionButton;
