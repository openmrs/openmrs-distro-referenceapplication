import React, { useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { launchAddOrEditStockItemWorkspace } from '../stock-item.utils';

const AddStockItemActionButton: React.FC = () => {
  const { t } = useTranslation();

  const handleAddOrLaunchStockItemWorkspace = useCallback(() => {
    launchAddOrEditStockItemWorkspace(t);
  }, [t]);

  return (
    <Button onClick={handleAddOrLaunchStockItemWorkspace} size="md" kind="primary">
      {t('addStockItem', 'Add stock item')}
    </Button>
  );
};

export default AddStockItemActionButton;
