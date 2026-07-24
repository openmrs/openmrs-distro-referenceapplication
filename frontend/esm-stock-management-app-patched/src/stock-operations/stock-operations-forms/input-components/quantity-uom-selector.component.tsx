import React, { useEffect, useMemo } from 'react';
import { ComboBox, InlineNotification, SkeletonText } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { type StockItemPackagingUOMDTO } from '../../../core/api/types/stockItem/StockItemPackagingUOM';
import { useStockItem } from '../../../stock-items/stock-items.resource';

interface QtyUomSelectorProps {
  stockItemUuid: string;
  intiallvalue?: string;
  onValueChange?: (value: string) => void;
  error?: string;
}

const QtyUomSelector: React.FC<QtyUomSelectorProps> = ({ stockItemUuid, error, intiallvalue, onValueChange }) => {
  const { t } = useTranslation();
  const { isLoading, error: stockItemError, item } = useStockItem(stockItemUuid);
  const initialSelectedItem = useMemo<StockItemPackagingUOMDTO | null>(
    () => (item?.packagingUnits ?? []).find((u) => u.uuid === intiallvalue) ?? item?.packagingUnits?.[0],
    [item?.packagingUnits, intiallvalue],
  );
  useEffect(() => {
    if (initialSelectedItem) {
      onValueChange?.(initialSelectedItem?.uuid);
    }
  }, [initialSelectedItem, onValueChange]);

  if (isLoading) return <SkeletonText />;

  if (stockItemError)
    return (
      <InlineNotification
        kind="error"
        title={t('packagingUomError', 'Error loading Stock item')}
        subtitle={stockItemError?.message}
      />
    );

  return (
    <ComboBox
      id={'stockItemPackagingUOMUuid'}
      initialSelectedItem={initialSelectedItem}
      invalid={!!error}
      invalidText={error}
      items={item?.packagingUnits ?? []}
      itemToString={(s: StockItemPackagingUOMDTO) =>
        s?.packagingUomName ? `${s?.packagingUomName} - ${s?.factor} ` : ''
      }
      name={'stockItemPackagingUOMUuid'}
      onChange={(data: { selectedItem?: StockItemPackagingUOMDTO }) => {
        onValueChange?.(data.selectedItem?.uuid);
      }}
      placeholder={t('filterPlaceholder', 'Filter...')}
      titleText={t('quantityUom', 'Quantity unit of measurement')}
    />
  );
};

export default QtyUomSelector;
