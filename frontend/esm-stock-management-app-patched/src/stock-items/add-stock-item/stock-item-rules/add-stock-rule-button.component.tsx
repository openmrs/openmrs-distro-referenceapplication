import { Button } from '@carbon/react';
import { launchWorkspace } from '@openmrs/esm-framework';
import React, { useCallback } from 'react';
import { useTranslation } from 'react-i18next';

interface AddStockRuleActionButtonProps {
  stockItemUuid: string;
}

const AddStockRuleActionButton: React.FC<AddStockRuleActionButtonProps> = ({ stockItemUuid }) => {
  const { t } = useTranslation();

  const handleClick = useCallback(() => {
    launchWorkspace('stock-item-rules-form-workspace', {
      workspaceTitle: t('addStockRule', 'Add stock rule'),
      stockItemUuid,
    });
  }, [stockItemUuid, t]);

  return (
    <Button onClick={handleClick} size="md" kind="primary">
      {t('addNewStockRule', 'Add New Rule')}
    </Button>
  );
};

export default AddStockRuleActionButton;
