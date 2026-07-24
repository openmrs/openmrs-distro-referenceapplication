import React from 'react';
import { Dropdown, DropdownSkeleton } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { useStockRules } from './stock-rules.resource';
import { ResourceRepresentation } from '../../../core/api/api';
import styles from './stock-rules-filter.scss';

interface StockRulesFilterProps {
  stockItemUuid: string;
}

const StockRulesFilter: React.FC<StockRulesFilterProps> = ({ stockItemUuid }) => {
  const { t } = useTranslation();
  const { items, isLoading, error } = useStockRules({
    v: ResourceRepresentation.Default,
    totalCount: true,
    stockItemUuid: stockItemUuid,
  });

  if (isLoading || error) {
    return <DropdownSkeleton />;
  }

  return (
    <>
      <div className={styles.filterContainer}>
        <Dropdown
          id="stockRulesFiter"
          items={[...items.results]}
          itemToString={(item) => (item ? item.name : t('notSet', 'Not Set'))}
          type="inline"
          size="sm"
          label={t('filterStockRules', 'Filter stock rules')}
          titleText={t('stockRules', 'Stock rules')}
        />
      </div>
    </>
  );
};

export default StockRulesFilter;
