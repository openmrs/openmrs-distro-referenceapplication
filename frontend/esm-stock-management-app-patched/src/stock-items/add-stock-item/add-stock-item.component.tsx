import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { type DefaultWorkspaceProps } from '@openmrs/esm-framework';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import { type TabItem } from '../../core/components/tabs/types';
import PackagingUnits from './packaging-units/packaging-units.component';
import StockItemDetails from './stock-item-details/stock-item-details.component';
import StockOperationStepper from '../../stock-operations/stock-operations-forms/stock-operation-stepper/stock-operation-stepper.component';

interface AddStockItemProps extends Partial<DefaultWorkspaceProps> {
  stockItem?: StockItemDTO;
}

const AddEditStockItem: React.FC<AddStockItemProps> = ({ stockItem, closeWorkspace }) => {
  const { t } = useTranslation();
  const [selectedTab, setSelectedTab] = useState(0);
  const isEditing = Boolean(stockItem);

  const handleTabChange = (index: number) => {
    setSelectedTab(index);
  };

  const tabs: TabItem[] = [
    {
      name: t('stockItemDetails', 'Stock Item Details'),
      component: (
        <StockItemDetails
          key={stockItem?.uuid}
          handleTabChange={handleTabChange}
          stockItem={stockItem}
          onCloseWorkspace={closeWorkspace}
        />
      ),
    },
    {
      name: t('packagingUnits', 'Packaging Units'),
      component: <PackagingUnits isEditing handleTabChange={handleTabChange} stockItemUuid={stockItem?.uuid} />,
      disabled: !isEditing,
    },
  ];

  return (
    <StockOperationStepper
      steps={tabs.map((tab) => ({
        title: tab.name,
        component: tab.component,
        disabled: tab.disabled,
      }))}
      selectedIndex={selectedTab}
      onChange={handleTabChange}
    />
  );
};

export default AddEditStockItem;
