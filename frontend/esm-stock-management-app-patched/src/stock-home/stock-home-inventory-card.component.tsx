import React, { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { WarningHex, WarningAlt } from '@carbon/react/icons';
import { showModal } from '@openmrs/esm-framework';
import { useStockInventory } from './stock-home-inventory-expiry.resource';
import { useStockInventoryItems } from './stock-home-inventory-items.resource';
import { useStockBatchQuantities } from './stock-home-batch-quantities.resource';
import useStockList from './useStockList';
import useFilteredOperationTypesByRoles from '../stock-operations/stock-operations-forms/hooks/useFilteredOperationTypesByRoles';
import { findRequisitionOperationType, launchReorderOperation } from './launch-reorder-operation.utils';
import styles from './stock-home-detail-card.scss';

const StockHomeInventoryCard = () => {
  const { t } = useTranslation();
  const { items: expiryItems, isLoading: inventoryLoading } = useStockInventory();
  const { items: stockItems, isLoading } = useStockInventoryItems();
  const { outOfStockItems, understockedItems, isLoading: stockListLoading } = useStockList();
  const { operationTypes } = useFilteredOperationTypesByRoles();
  const requisitionOperationType = findRequisitionOperationType(operationTypes);

  const stockItemUuids = useMemo(
    () => Array.from(new Set(expiryItems.map((batch) => batch.stockItemUuid))),
    [expiryItems],
  );
  const { quantityByBatch, isLoading: quantityLoading } = useStockBatchQuantities(stockItemUuids);

  if (isLoading || inventoryLoading || quantityLoading || stockListLoading) {
    return null;
  }

  const currentDate = new Date();

  let mergedArray = expiryItems.map((batch) => {
    const matchingItem = stockItems?.find((item) => batch?.stockItemUuid === item.uuid);
    const batchQuantity = quantityByBatch.get(batch.uuid);
    return {
      ...batch,
      ...matchingItem,
      quantity: batchQuantity?.quantity ?? 0,
      quantityUoM: batchQuantity?.quantityUoM,
    };
  });

  mergedArray = mergedArray.filter((item) => item.hasExpiration);

  const expiringSoon = mergedArray.filter((item) => {
    const expirationDate = new Date(item.expiration);
    const differenceInDays = Math.ceil((expirationDate.getTime() - currentDate.getTime()) / (1000 * 60 * 60 * 24));
    return differenceInDays <= 180 && differenceInDays >= 0;
  });

  const filteredData = expiringSoon.slice(0, 3);
  const understockedToShow = understockedItems.slice(0, 3);

  const launchExpiredStockModal = () => {
    const dispose = showModal('expired-stock-modal', {
      closeModal: () => dispose(),
      expiredStock: expiringSoon,
    });
  };

  const launchOutOfStockModal = () => {
    const dispose = showModal('out-of-stock-modal', {
      closeModal: () => dispose(),
      outOfStockItems,
      understockedItems,
    });
  };

  const handleReorder = (displayName: string) => {
    launchReorderOperation(t, requisitionOperationType, displayName);
  };

  if (filteredData.length === 0 && understockedToShow.length === 0) {
    return <p className={styles.content}>{t('noInventoryAlerts', 'No inventory alerts to display')}</p>;
  }

  return (
    <>
      {filteredData.map((item, index) => (
        <div className={styles.card} key={`expiring-${index}`}>
          <div className={styles.colorLineRed} />
          <div className={styles.icon}>
            <WarningHex size={40} color={'#DA1E28'} />
          </div>
          <div className={styles.cardText}>
            <p>{t('expiringStock', 'Expiring stock')}</p>
            <p>
              <strong>{item?.drugName}</strong> {t('batchNumberLabel', 'Batch number:')} {item?.batchNo}{' '}
              {t('quantityLabel', 'Quantity:')} {item?.quantity} {item?.dispensingUnitName}
            </p>
          </div>
        </div>
      ))}
      {understockedToShow.map((item, index) => (
        <div className={styles.card} key={`understocked-${index}`}>
          <div className={styles.colorLineOrange} />
          <div className={styles.icon}>
            <WarningAlt size={40} color={'#FF832B'} />
          </div>
          <div className={styles.cardText}>
            <p>{t('understockedItem', 'Understocked')}</p>
            <p>
              <strong>{item?.displayName}</strong> {t('quantityLabel', 'Quantity:')} {item?.quantity}{' '}
              {t('reorderLevelLabel', '· Reorder level:')} {item?.reorderLevel}
            </p>
          </div>
          <Button kind="ghost" size="sm" onClick={() => handleReorder(item.displayName)}>
            {t('reorder', 'Reorder')}
          </Button>
        </div>
      ))}
      <div className={styles.action}>
        {filteredData.length > 0 && (
          <Button kind="ghost" onClick={launchExpiredStockModal} size="sm">
            {t('viewAllExpiring', 'View all expiring')}
          </Button>
        )}
        {understockedToShow.length > 0 && (
          <Button kind="ghost" onClick={launchOutOfStockModal} size="sm">
            {t('viewAllUnderstocked', 'View all understocked')}
          </Button>
        )}
      </div>
    </>
  );
};

export default StockHomeInventoryCard;
