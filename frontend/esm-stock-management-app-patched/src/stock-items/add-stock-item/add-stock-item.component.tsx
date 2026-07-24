import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { type DefaultWorkspaceProps } from '@openmrs/esm-framework';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import { type TabItem } from '../../core/components/tabs/types';
import BatchInformation from './batch-information/batch-information.component';
import PackagingUnits from './packaging-units/packaging-units.component';
import StockItemDetails from './stock-item-details/stock-item-details.component';
import StockItemRules from './stock-item-rules/stock-item-rules.component';
import StockOperationStepper from '../../stock-operations/stock-operations-forms/stock-operation-stepper/stock-operation-stepper.component';
import StockQuantities from './quantities/quantities.component';
import StockReferences from './stock-item-references/stock-item-references.component';
import Transactions from './transactions/transactions.component';

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
    {
      name: t('transactions', 'Transactions'),
      component: <Transactions stockItemUuid={stockItem?.uuid} />,
      disabled: !isEditing,
    },
    {
      name: t('batchInformation', 'Batch Information'),
      component: <BatchInformation stockItemUuid={stockItem?.uuid} />,
      disabled: !isEditing,
    },
    {
      name: t('quantities', 'Quantities'),
      component: <StockQuantities stockItemUuid={stockItem?.uuid} />,
      disabled: !isEditing,
    },
    {
      name: t('rules', 'Rules'),
      component: <StockItemRules stockItemUuid={stockItem?.uuid} />,
      disabled: !isEditing,
    },
    {
      name: t('references', 'References'),
      component: <StockReferences stockItemUuid={stockItem?.uuid} isEditing={isEditing} />,
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
