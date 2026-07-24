import React from 'react';
import { Dropdown, DropdownSkeleton } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { type ConfigObject } from '../../config-schema';
import { useConcept } from '../../stock-lookups/stock-lookups.resource';
import { useConfig } from '@openmrs/esm-framework';
import styles from './stock-operation-sources-filter.scss';

const StockOperationSourcesFilter: React.FC = () => {
  const { t } = useTranslation();
  const { stockSourceTypeUUID } = useConfig<ConfigObject>();

  // get stock sources
  const { items, isLoading, error } = useConcept(stockSourceTypeUUID);
  if (isLoading || error) {
    return <DropdownSkeleton />;
  }
  return (
    <>
      <div className={styles.filterContainer}>
        <Dropdown
          id="stockOperationSourcesFiter"
          items={[...items.answers]}
          itemToString={(item) => (item ? item.display : t('notSet', 'Not Set'))}
          type="inline"
          size="sm"
          label={t('filterBySource', 'Filter by source')}
          titleText={t('source', 'Source')}
        />
      </div>
    </>
  );
};

export default StockOperationSourcesFilter;
