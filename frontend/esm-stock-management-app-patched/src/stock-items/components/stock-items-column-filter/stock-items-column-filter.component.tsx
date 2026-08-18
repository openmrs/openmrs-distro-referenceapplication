import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, Checkbox, CheckboxGroup, Popover, PopoverContent } from '@carbon/react';
import { Filter } from '@carbon/react/icons';
import styles from './stock-items-column-filter.scss';

export type StockStatus = 'inStock' | 'understocked' | 'outOfStock';

export interface StockItemColumnFilters {
  dispensingUoM: Array<string>;
  packagingUoM: Array<string>;
  stockStatus: Array<StockStatus>;
}

export const EMPTY_COLUMN_FILTERS: StockItemColumnFilters = {
  dispensingUoM: [],
  packagingUoM: [],
  stockStatus: [],
};

interface StockItemsColumnFilterProps {
  dispensingUoMOptions: Array<string>;
  packagingUoMOptions: Array<string>;
  filters: StockItemColumnFilters;
  onChange: (filters: StockItemColumnFilters) => void;
}

const STOCK_STATUS_OPTIONS: Array<{ value: StockStatus; labelKey: string; defaultLabel: string }> = [
  { value: 'inStock', labelKey: 'inStock', defaultLabel: 'In stock' },
  { value: 'understocked', labelKey: 'understockedItem', defaultLabel: 'Understocked' },
  { value: 'outOfStock', labelKey: 'outOfStock', defaultLabel: 'Out of stock' },
];

const StockItemsColumnFilter: React.FC<StockItemsColumnFilterProps> = ({
  dispensingUoMOptions,
  packagingUoMOptions,
  filters,
  onChange,
}) => {
  const { t } = useTranslation();
  const [open, setOpen] = useState(false);

  const activeCount = filters.dispensingUoM.length + filters.packagingUoM.length + filters.stockStatus.length;

  const toggleValue = (key: keyof StockItemColumnFilters, value: string) => {
    const current = filters[key] as Array<string>;
    const next = current.includes(value) ? current.filter((v) => v !== value) : [...current, value];
    onChange({ ...filters, [key]: next });
  };

  const clearAll = () => onChange(EMPTY_COLUMN_FILTERS);

  const dispensingLabel = useMemo(() => t('dispensingUnitName', 'Dispensing UoM'), [t]);
  const packagingLabel = useMemo(() => t('defaultStockOperationsUoMName', 'Bulk packaging'), [t]);

  return (
    <Popover open={open} onRequestClose={() => setOpen(false)} align="bottom-right" isTabTip>
      <Button
        kind="ghost"
        size="md"
        hasIconOnly
        iconDescription={t('filters', 'Filters')}
        renderIcon={Filter}
        onClick={() => setOpen((prev) => !prev)}
        className={styles.filterTrigger}
      >
        {activeCount > 0 ? `${t('filters', 'Filters')} (${activeCount})` : t('filters', 'Filters')}
      </Button>
      <PopoverContent className={styles.popoverContent}>
        <div className={styles.filterPanel}>
          <CheckboxGroup legendText={t('stockStatus', 'Stock status')}>
            {STOCK_STATUS_OPTIONS.map((option) => (
              <Checkbox
                key={option.value}
                id={`filter-stock-status-${option.value}`}
                labelText={t(option.labelKey, option.defaultLabel)}
                checked={filters.stockStatus.includes(option.value)}
                onChange={() => toggleValue('stockStatus', option.value)}
              />
            ))}
          </CheckboxGroup>
          {dispensingUoMOptions.length > 0 && (
            <CheckboxGroup legendText={dispensingLabel}>
              {dispensingUoMOptions.map((uom) => (
                <Checkbox
                  key={uom}
                  id={`filter-dispensing-${uom}`}
                  labelText={uom}
                  checked={filters.dispensingUoM.includes(uom)}
                  onChange={() => toggleValue('dispensingUoM', uom)}
                />
              ))}
            </CheckboxGroup>
          )}
          {packagingUoMOptions.length > 0 && (
            <CheckboxGroup legendText={packagingLabel}>
              {packagingUoMOptions.map((uom) => (
                <Checkbox
                  key={uom}
                  id={`filter-packaging-${uom}`}
                  labelText={uom}
                  checked={filters.packagingUoM.includes(uom)}
                  onChange={() => toggleValue('packagingUoM', uom)}
                />
              ))}
            </CheckboxGroup>
          )}
          <Button kind="ghost" size="sm" onClick={clearAll} disabled={activeCount === 0}>
            {t('clearFilters', 'Clear filters')}
          </Button>
        </div>
      </PopoverContent>
    </Popover>
  );
};

export default StockItemsColumnFilter;
