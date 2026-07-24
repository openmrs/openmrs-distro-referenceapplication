import { InlineLoading } from '@carbon/react';
import { showSnackbar } from '@openmrs/esm-framework';
import React, { useEffect, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { useStockItemBatchInformationHook } from '../../../stock-items/add-stock-item/batch-information/batch-information.resource';
import styles from './stock-operation-items-form-step.scc.scss';
const StockAvailability: React.FC<{ stockItemUuid: string }> = ({ stockItemUuid }) => {
  const { items, isLoading, error } = useStockItemBatchInformationHook({
    stockItemUuid: stockItemUuid,
    includeBatchNo: true,
  });
  const { t } = useTranslation();

  const totalQuantity = useMemo(() => {
    if (!items?.length) return 0;
    return items.reduce((total, batch) => {
      return total + (Number(batch.quantity) || 0);
    }, 0);
  }, [items]);
  const commonUOM = useMemo(() => {
    if (!items?.length) return '';
    return items[0]?.quantityUoM || '';
  }, [items]);

  useEffect(() => {
    if (error) {
      showSnackbar({
        kind: 'error',
        title: t('stockAvailabilityError', 'Error loading stock availability'),
        subtitle: error?.message,
      });
    }
  }, [error, t]);

  if (isLoading) return <InlineLoading status="active" iconDescription="Loading" />;
  if (error) return <>--</>;

  return (
    <div className={styles.availability}>
      {totalQuantity > 0 ? (
        <span>
          Available: {totalQuantity.toLocaleString()} {commonUOM}
        </span>
      ) : (
        <span className={styles.outOfStock}>Out of Stock</span>
      )}
    </div>
  );
};

export default StockAvailability;
