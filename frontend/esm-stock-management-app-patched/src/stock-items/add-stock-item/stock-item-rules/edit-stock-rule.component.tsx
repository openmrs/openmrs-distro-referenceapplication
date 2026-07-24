import { Button } from '@carbon/react';
import { Edit } from '@carbon/react/icons';
import React, { useCallback } from 'react';

import { launchWorkspace } from '@openmrs/esm-framework';
import { useTranslation } from 'react-i18next';
import { type StockRule } from '../../../core/api/types/stockItem/StockRule';

interface EditStockRulesActionMenuProps {
  data?: StockRule;
  stockItemUuid?: string;
}

const EditStockRuleActionsMenu: React.FC<EditStockRulesActionMenuProps> = ({ data, stockItemUuid }) => {
  const { t } = useTranslation();
  const handleClick = useCallback(() => {
    launchWorkspace('stock-item-rules-form-workspace', {
      workspaceTitle: t('editStockRule', 'Edit Stock Rule'),
      stockItemUuid,
      model: data,
    });
  }, [data, t, stockItemUuid]);

  return (
    <Button
      kind="ghost"
      size="md"
      onClick={() => handleClick()}
      iconDescription={t('editStockRule', 'Edit Stock Rule')}
      renderIcon={(props) => <Edit size={16} {...props} />}
    />
  );
};
export default EditStockRuleActionsMenu;
