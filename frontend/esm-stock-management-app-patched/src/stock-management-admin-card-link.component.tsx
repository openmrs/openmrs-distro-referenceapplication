import React from 'react';
import { useTranslation } from 'react-i18next';
import { Layer, ClickableTile } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

const StockManagementCardLink: React.FC = () => {
  const { t } = useTranslation();
  const header = t('manageStock', 'Manage Stock');
  return (
    <Layer>
      <ClickableTile href={window.getOpenmrsSpaBase() + 'stock-management'} rel="noopener noreferrer">
        <div>
          <div className="heading">{header}</div>
          <div className="content">{t('stockManagement', 'Stock management')}</div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
};

export default StockManagementCardLink;
