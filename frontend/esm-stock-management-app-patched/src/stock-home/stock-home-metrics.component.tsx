import React from 'react';
import { useTranslation } from 'react-i18next';
import { ErrorState } from '@openmrs/esm-framework';
import { ResourceRepresentation } from '../core/api/api';
import { useDisposalList } from './useDisposalList';
import { useStockInventory } from './stock-home-inventory-expiry.resource';
import { useStockInventoryItems } from './stock-home-inventory-items.resource';
import { type StockOperationFilter } from '../stock-operations/stock-operations.resource';
import useStockList from './useStockList';
import MetricsCard from '../core/components/card/metrics-card-component';
import styles from './stock-home.scss';

const StockManagementMetrics: React.FC = (filter: StockOperationFilter) => {
  const { t } = useTranslation();
  const { outOfStockItems, understockedItems, error } = useStockList();
  const { items: expiryItems } = useStockInventory();
  const { items: stockItems } = useStockInventoryItems();

  const currentDate = new Date();

  let mergedArray = expiryItems.map((batch) => {
    const matchingItem = stockItems?.find((item) => batch?.stockItemUuid === item.uuid);
    return { ...batch, ...matchingItem };
  });

  mergedArray = mergedArray.filter((item) => item.hasExpiration);

  const filteredData = mergedArray.filter((item) => {
    const expiryNotice = item.expiryNotice || 0;
    const expirationDate = new Date(item.expiration);
    const differenceInDays = Math.ceil((expirationDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24));
    return differenceInDays <= expiryNotice || differenceInDays < 0;
  });

  const sixMonthsExpiryStocks = filteredData.filter((stock) => stock.hasExpiration && stock.expiryNotice <= 180);

  const { items } = useDisposalList({
    v: ResourceRepresentation.Full,
    totalCount: true,
  });

  if (error) {
    return <ErrorState headerTitle={t('errorStockMetric', 'Error fetching stock metrics')} error={error} />;
  }

  const filteredItems =
    items && items.filter((item) => item.reasonName === 'Drug not available due to expired medication');
  const poorQualityItems = items && items.filter((item) => item.reasonName === 'Poor Quality');

  return (
    <div className={styles.cardContainer}>
      <MetricsCard
        count={{
          expiry6months: sixMonthsExpiryStocks,
        }}
        headerLabel={t('expiringStock', 'Expiring stock')}
        label={t('expiringStock', 'Expiring stock')}
        value={filteredData?.length || 0}
      />
      <MetricsCard
        label={t('outOfStock', 'Out of stock')}
        headerLabel={t('outOfStock', 'Out of stock')}
        outOfStockCount={{
          itemsBelowMin: understockedItems,
          itemsAboveMax: [],
        }}
        value={outOfStockItems?.length ?? 0}
      />
      <MetricsCard
        disposedCount={{
          expired: filteredItems,
          poorQuality: poorQualityItems,
        }}
        headerLabel={t('disposedStock', 'Disposed stock')}
        label={t('disposedStock', 'Disposed stock')}
        value={items?.length || 0}
      />
    </div>
  );
};
export default StockManagementMetrics;
