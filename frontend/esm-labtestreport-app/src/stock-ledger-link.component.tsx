import React from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function StockLedgerLink() {
  const { t } = useTranslation();
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/stock-reports-home`}>
        <div>
          <div className="heading">{t('stockReportsTitle', 'Stock Reports')}</div>
          <div className="content">
            {t(
              'stockReportsShortDesc',
              'Inventory ledger, consumption by location, and distribution reports for stock management',
            )}
          </div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
