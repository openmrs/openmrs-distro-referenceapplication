import React, { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import useStockList from './useStockList';
import useFilteredOperationTypesByRoles from '../stock-operations/stock-operations-forms/hooks/useFilteredOperationTypesByRoles';
import { findRequisitionOperationType, launchReorderOperation } from './launch-reorder-operation.utils';
import styles from './stock-home-detail-card.scss';

const MAX_ITEMS = 5;

const StockHomeNearingStockoutCard = () => {
  const { t } = useTranslation();
  const { understockedItems, isLoading } = useStockList();
  const { operationTypes } = useFilteredOperationTypesByRoles();
  const requisitionOperationType = findRequisitionOperationType(operationTypes);

  const nearingStockout = useMemo(
    () =>
      [...understockedItems]
        .sort((a, b) => a.quantity / (a.reorderLevel || 1) - b.quantity / (b.reorderLevel || 1))
        .slice(0, MAX_ITEMS),
    [understockedItems],
  );

  if (isLoading) {
    return null;
  }

  if (nearingStockout.length === 0) {
    return <p className={styles.content}>{t('noItemsNearingStockout', 'No items nearing stockout')}</p>;
  }

  const handleReorder = (displayName: string) => {
    launchReorderOperation(t, requisitionOperationType, displayName);
  };

  return (
    <>
      {nearingStockout.map((item, index) => {
        const percentRemaining = item.reorderLevel ? Math.round((item.quantity / item.reorderLevel) * 100) : 0;
        return (
          <div className={styles.card} key={index}>
            <div className={styles.colorLineOrange} />
            <div className={styles.cardText}>
              <p>
                <strong>{item?.displayName}</strong>
              </p>
              <p>
                {t('quantityLabel', 'Quantity:')} {item?.quantity} {t('reorderLevelLabel', '· Reorder level:')}{' '}
                {item?.reorderLevel} ({percentRemaining}%)
              </p>
            </div>
            <Button kind="ghost" size="sm" onClick={() => handleReorder(item.displayName)}>
              {t('reorder', 'Reorder')}
            </Button>
          </div>
        );
      })}
    </>
  );
};

export default StockHomeNearingStockoutCard;
