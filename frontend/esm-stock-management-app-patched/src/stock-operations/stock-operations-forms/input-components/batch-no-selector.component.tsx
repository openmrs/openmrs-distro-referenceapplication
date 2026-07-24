import React, { useCallback, useEffect, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { ComboBox, SelectSkeleton } from '@carbon/react';
import { type StockBatchWithUoM } from '../../../core/api/types/stockItem/StockBatchDTO';
import { formatForDatePicker } from '../../../constants';
import { useStockItemBatchInformationHook } from '../../../stock-items/add-stock-item/batch-information/batch-information.resource';
import { useStockItemBatchNumbers } from '../hooks/useStockItemBatchNumbers';

interface BatchNoSelectorProps {
  stockItemUuid: string;
  initialValue?: string;
  onValueChange?: (value: string) => void;
  error?: string;
}

const BatchNoSelector: React.FC<BatchNoSelectorProps> = ({ stockItemUuid, error, initialValue, onValueChange }) => {
  const { isLoading, stockItemBatchNos } = useStockItemBatchNumbers(stockItemUuid);
  const { t } = useTranslation();
  const { items, setStockItemUuid, isLoading: isLoadingBatchinfo } = useStockItemBatchInformationHook();

  useEffect(() => {
    setStockItemUuid(stockItemUuid);
  }, [stockItemUuid, setStockItemUuid]);

  const stockItemBatchesInfo = useMemo(() => {
    if (!stockItemBatchNos) return [];

    return stockItemBatchNos.map((item) => {
      const matchingBatch = items?.find((batch) => batch.batchNumber === item.batchNo);

      return matchingBatch
        ? ({
            ...item,
            ...matchingBatch,
            quantity: String(matchingBatch.quantity),
          } as StockBatchWithUoM)
        : (item as StockBatchWithUoM);
    });
  }, [stockItemBatchNos, items]);

  const filteredBatches = useMemo(() => {
    if (!stockItemBatchesInfo) return [];

    return stockItemBatchesInfo.filter((batch) => {
      const quantity = typeof batch.quantity === 'string' ? parseFloat(batch.quantity) : batch.quantity;

      return !isNaN(quantity) && quantity > 0;
    });
  }, [stockItemBatchesInfo]);

  const initialSelectedItem = useMemo(
    () => filteredBatches.find((batch) => batch.uuid === initialValue) ?? null,
    [filteredBatches, initialValue],
  );

  const formatQuantityDisplay = useCallback(
    (batch: StockBatchWithUoM): string => {
      if (batch.quantity === undefined) return t('unknown', 'Unknown');

      const quantity = typeof batch.quantity === 'string' ? parseFloat(batch.quantity) : batch.quantity;

      if (isNaN(quantity)) return t('unknown', 'Unknown');

      const baseQuantity = quantity.toString();

      if (!batch.quantityUoM) return baseQuantity;

      const withUnit = `${baseQuantity} ${batch.quantityUoM}`;

      if (!batch.quantityFactor) return withUnit;

      const factor = parseFloat(batch.quantityFactor);
      return !isNaN(factor) && factor > 1 ? `${withUnit} (${factor} units each)` : withUnit;
    },
    [t],
  );

  const itemToString = useCallback(
    (batch: StockBatchWithUoM | null): string => {
      if (!batch?.batchNo) return '';

      const quantityDisplay = formatQuantityDisplay(batch);
      const expiryDate = batch.expiration ? formatForDatePicker(batch.expiration) : t('noExpiry', 'No expiry');

      return `${batch.batchNo} | Qty: ${quantityDisplay} | Expiry: ${expiryDate}`;
    },
    [formatQuantityDisplay, t],
  );

  const handleChange = useCallback(
    (data: { selectedItem?: StockBatchWithUoM | null }) => {
      onValueChange?.(data.selectedItem?.uuid ?? '');
    },
    [onValueChange],
  );

  if (isLoading || isLoadingBatchinfo) {
    return <SelectSkeleton role="progressbar" />;
  }

  return (
    <ComboBox
      id="stockBatchUuid"
      invalid={!!error}
      invalidText={error}
      items={filteredBatches}
      itemToString={itemToString}
      name="stockBatchUuid"
      onChange={handleChange}
      placeholder={t('filterPlaceholder', 'Filter...')}
      selectedItem={initialSelectedItem}
      style={{ flexGrow: 1 }}
      titleText={t('batchNo', 'Batch number')}
    />
  );
};

export default BatchNoSelector;
