import React from 'react';
import { Dropdown, DropdownSkeleton } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { useStockOperationTypes } from '../../stock-lookups/stock-lookups.resource';
import styles from './stock-operation-operations-filter.scss';

const StockOperationOperationsFilter: React.FC = () => {
  const { t } = useTranslation();
  // get stock sources
  const { types, isLoading, error } = useStockOperationTypes();
  if (isLoading || error) {
    return <DropdownSkeleton />;
  }
  return (
    <>
      <div className={styles.filterContainer}>
        <Dropdown
          id="stockOperationOperationsFiter"
          items={types.results}
          itemToString={(item) => (item ? item.name : t('notSet', 'Not Set'))}
          type="inline"
          size="sm"
          label={t('filterByOperationType', 'Filter by operation type')}
          titleText={t('operationType', 'Operation Type')}
        />
      </div>
    </>
  );
};

export default StockOperationOperationsFilter;
