import React from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { Delivery } from '@carbon/react/icons';
import { showModal } from '@openmrs/esm-framework';
import { ResourceRepresentation } from '../core/api/api';
import { useStockReceiving } from './stock-home-receiving.resource';
import styles from './stock-home-detail-card.scss';

const StockHomeReceivingCard = () => {
  const { t } = useTranslation();

  const { items, isLoading } = useStockReceiving({
    v: ResourceRepresentation.Full,
    totalCount: true,
  });

  if (isLoading) {
    return null;
  }

  if (items?.length === 0) {
    return <p className={styles.content}>{t('noReceivedStock', 'No received stock to display')}</p>;
  }

  const launchReceivingStockModal = () => {
    const dispose = showModal('receiving-stock-modal', {
      closeModal: () => dispose(),
      receivingStock: items,
    });
  };

  return (
    <>
      {items?.map((item, index) =>
        item?.stockOperationItems.map((stock) => (
          <div className={styles.card} key={index}>
            <div className={styles.colorLineBlue} />
            <div className={styles.icon}>
              <Delivery size={40} color={'#0F62FE'} />
            </div>
            <div className={styles.cardText}>
              <p>
                {item?.status} · {item?.sourceName} · {item?.destinationName}
              </p>
              <p>
                <strong>{stock?.stockItemName}</strong> {stock?.stockItemPackagingUOMName}, {stock?.quantity}
              </p>
            </div>
          </div>
        )),
      )}
      <Button kind="ghost" onClick={launchReceivingStockModal} size="sm">
        {t('viewAll', 'View All')}
      </Button>
    </>
  );
};

export default StockHomeReceivingCard;
