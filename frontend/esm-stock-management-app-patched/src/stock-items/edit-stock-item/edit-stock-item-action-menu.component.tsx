import React from 'react';
import { Button } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import { launchAddOrEditStockItemWorkspace } from '../stock-item.utils';

interface EditStockItemActionsMenuProps {
  data: StockItemDTO;
}

const EditStockItemActionsMenu: React.FC<EditStockItemActionsMenuProps> = ({ data }) => {
  const { t } = useTranslation();

  return (
    <Button
      kind="ghost"
      size="md"
      onClick={() => {
        data.isDrug = !!data.drugUuid;
        launchAddOrEditStockItemWorkspace(t, data);
      }}
      iconDescription={t('editStockItem', 'Edit stock item')}
    >
      {`${data?.drugName ?? data.conceptName}`}
    </Button>
  );
};
export default EditStockItemActionsMenu;
