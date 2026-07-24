import React from 'react';
import { useTranslation } from 'react-i18next';
import { RadioButton, RadioButtonGroup } from '@carbon/react';
import styles from './filter-stock-items.scss';

interface FilterStockItemsProps {
  filterType: string;
  changeFilterType: React.Dispatch<React.SetStateAction<string>>;
}

const FilterStockItems: React.FC<FilterStockItemsProps> = ({ filterType, changeFilterType }) => {
  const { t } = useTranslation();
  return (
    <RadioButtonGroup
      name="is-drug"
      defaultSelected={filterType}
      onChange={(value) => changeFilterType(value as string)}
      className={styles.spacing}
    >
      <RadioButton labelText={t('all', 'All')} value="" id="is-drug-all" />
      <RadioButton labelText={t('pharmaceuticals', 'Pharmaceuticals')} value="true" id="is-drug-drug" />
      <RadioButton labelText={t('nonPharmaceuticals', 'Non Pharmaceuticals')} value="false" id="is-drug-other" />
    </RadioButtonGroup>
  );
};

export default FilterStockItems;
