import { InlineLoading } from '@carbon/react';
import { ConfigurableLink, showSnackbar, useConfig } from '@openmrs/esm-framework';
import React, { useEffect, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { type ConfigObject } from '../../../config-schema';
import { URL_STOCK_ITEM } from '../../../constants';
import { useStockItem } from '../../../stock-items/stock-items.resource';

type StockOperationItemCellProps = {
  stockItemUuid: string;
};

const StockOperationItemCell: React.FC<StockOperationItemCellProps> = ({ stockItemUuid }) => {
  const { isLoading, error, item } = useStockItem(stockItemUuid);
  const { t } = useTranslation();
  const { useItemCommonNameAsDisplay } = useConfig<ConfigObject>();

  const commonName = useMemo(() => {
    if (!useItemCommonNameAsDisplay) return;
    const drugName = item?.drugName ? `(Drug name: ${item.drugName})` : undefined;
    return `${item?.commonName || t('noCommonNameAvailable', 'No common name available') + (drugName ?? '')}`;
  }, [item, useItemCommonNameAsDisplay, t]);

  const drugName = useMemo(() => {
    if (useItemCommonNameAsDisplay) return;
    const commonName = item?.commonName ? `(Common name: ${item.commonName})` : undefined;
    return `${item?.drugName || t('noDrugNameAvailable', 'No drug name available') + (commonName ?? '')}`;
  }, [item, useItemCommonNameAsDisplay, t]);
  useEffect(() => {
    if (error) {
      showSnackbar({
        kind: 'error',
        title: t('stockItemError', 'Error loading stock item'),
        subtitle: error?.message,
      });
    }
  }, [error, t]);

  if (isLoading) return <InlineLoading status="active" iconDescription="Loading" />;
  if (error) return <>--</>;

  return (
    <ConfigurableLink target={'_blank'} to={window.spaBase + URL_STOCK_ITEM(stockItemUuid)}>
      {useItemCommonNameAsDisplay ? commonName : drugName}
    </ConfigurableLink>
  );
};

export default StockOperationItemCell;
