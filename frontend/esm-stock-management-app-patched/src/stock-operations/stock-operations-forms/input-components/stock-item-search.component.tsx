import React, { useCallback, useEffect, useState } from 'react';
import { ClickableTile, Search } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { useConfig, useDebounce } from '@openmrs/esm-framework';
import { type StockItemDTO } from '../../../core/api/types/stockItem/StockItem';
import { useFilterableStockItems } from '../hooks/useFilterableStockItems';
import { type ConfigObject } from '../../../config-schema';
import styles from './input-components-styles.scss';

type StockItemSearchProps = {
  onSelectedItem?: (stockItem: StockItemDTO) => void;
};

const StockItemSearch: React.FC<StockItemSearchProps> = ({ onSelectedItem }) => {
  const { t } = useTranslation();
  const { isLoading, stockItemsList, setSearchString } = useFilterableStockItems({});
  const [searchTerm, setSearchTerm] = useState('');
  const debouncedSearchTerm = useDebounce(searchTerm);
  const { useItemCommonNameAsDisplay } = useConfig<ConfigObject>();
  const getDrugName = useCallback(
    (item: StockItemDTO) => {
      if (useItemCommonNameAsDisplay) return;
      const commonName = item?.commonName ? `(Common name: ${item.commonName})` : undefined;
      return `${item?.drugName || t('noDrugNameAvailable', 'No drug name available') + (commonName ?? '')}`;
    },
    [useItemCommonNameAsDisplay, t],
  );

  const getCommonName = useCallback(
    (item: StockItemDTO) => {
      if (!useItemCommonNameAsDisplay) return;
      const drugName = item?.drugName ? `(Drug name: ${item.drugName})` : undefined;
      return `${item?.commonName || t('noCommonNameAvailable', 'No common name available') + (drugName ?? '')}`;
    },
    [useItemCommonNameAsDisplay, t],
  );
  useEffect(() => {
    if (debouncedSearchTerm?.length !== 0) {
      setSearchString(debouncedSearchTerm);
    }
  }, [debouncedSearchTerm, setSearchString]);

  const handleOnSearchResultClick = (stockItem: StockItemDTO) => {
    onSelectedItem?.(stockItem);
    setSearchTerm('');
  };

  return (
    <div className={styles.stockItemSearchContainer}>
      <div style={{ display: 'flex' }}>
        <Search
          size="lg"
          placeholder={t('findItems', 'Find your items')}
          labelText={t('search', 'Search')}
          closeButtonLabelText={t('clearSearch', 'Clear search input')}
          value={searchTerm}
          id="search-stock-operation-item"
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      {searchTerm && stockItemsList?.length > 0 && (
        <div className={styles.searchResults}>
          {stockItemsList?.slice(0, 5).map((stockItem) => {
            const commonName = getCommonName(stockItem);
            const drugName = getDrugName(stockItem);
            return (
              <ClickableTile onClick={() => handleOnSearchResultClick(stockItem)} key={stockItem?.uuid}>
                {useItemCommonNameAsDisplay ? commonName : drugName}
              </ClickableTile>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default StockItemSearch;
