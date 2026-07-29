import React from 'react';
import { useTranslation } from 'react-i18next';
import { ClickableTile, Layer } from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';

export default function StockLedgerLink() {
  const { t } = useTranslation();
  return (
    <Layer>
      <ClickableTile href={`${window.getOpenmrsSpaBase().slice(0, -1)}/stock-ledger-report`}>
        <div>
          <div className="heading">{t('stockLedgerReportTitle', 'Stock Inventory Ledger Report')}</div>
          <div className="content">
            {t('stockLedgerReportShortDesc', 'Daily opening/incoming/outgoing/closing stock balances per item')}
          </div>
        </div>
        <div className="iconWrapper">
          <ArrowRight size={16} />
        </div>
      </ClickableTile>
    </Layer>
  );
}
