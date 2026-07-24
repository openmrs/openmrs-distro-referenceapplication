import React, { useCallback } from 'react';
import { Button } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { launchWorkspace } from '@openmrs/esm-framework';

const AddStockUserRoleScopeActionButton: React.FC = () => {
  const { t } = useTranslation();

  const handleClick = useCallback(() => {
    launchWorkspace('stock-user-role-scopes-form-workspace', {
      workspaceTitle: t('addNewUserRoleScope', 'Add new user role scope'),
    });
  }, [t]);

  return (
    <Button kind="primary" onClick={handleClick} size="md">
      {t('addNewUserRoleScope', 'Add new user role scope')}
    </Button>
  );
};

export default AddStockUserRoleScopeActionButton;
