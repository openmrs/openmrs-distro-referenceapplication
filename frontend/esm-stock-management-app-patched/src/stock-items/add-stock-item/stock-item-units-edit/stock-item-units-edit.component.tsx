import React from 'react';
import { useTranslation } from 'react-i18next';
import { NumberInputSkeleton } from '@carbon/react';
import { useStockItem } from '../../stock-items.resource';
import { type Control, type FormState } from 'react-hook-form';
import { type StockItemFormData } from '../../validationSchema';
import ControlledNumberInput from '../../../core/components/carbon/controlled-number-input.component';
import DispensingPackageMeasurement from '../dispensing-package-measurement/dispensing-package-measurement.component';

interface StockItemUnitsEditProps {
  stockItemUuid: string;
  control: Control<StockItemFormData>;
  formState: FormState<StockItemFormData>;
}

const StockItemUnitsEdit: React.FC<StockItemUnitsEditProps> = ({ stockItemUuid, control, formState: { errors } }) => {
  const { t } = useTranslation();

  const { item: stockItem, isLoading } = useStockItem(stockItemUuid);

  if (!(stockItem && stockItem.packagingUnits && stockItem.packagingUnits.length > 0)) return <></>;

  return (
    <>
      <DispensingPackageMeasurement
        dispensingUnitPackagingUoMUuid={stockItem?.dispensingUnitPackagingUoMUuid}
        name="dispensingUnitPackagingUoMUuid"
        controllerName="dispensingUnitPackagingUoMUuid"
        control={control}
        title={t('dispensingUnitOfMeasurement', 'Dispensing packaging unit of measurement') + ':'}
        placeholder={t('chooseAPackagingUoM', 'Choose a packaging UoM')}
        isLoading={isLoading}
        packagingUnits={stockItem.packagingUnits}
        invalid={!!errors.dispensingUnitPackagingUoMUuid}
        invalidText={errors.dispensingUnitPackagingUoMUuid && errors?.dispensingUnitPackagingUoMUuid?.message}
      />
      <DispensingPackageMeasurement
        dispensingUnitPackagingUoMUuid={stockItem?.defaultStockOperationsUoMUuid}
        name="defaultStockOperationsUoMUuid"
        controllerName="defaultStockOperationsUoMUuid"
        control={control}
        title={t('defaultStockOperationsPackagingUnit', 'Default stock operations packaging unit') + ':'}
        placeholder={t('chooseAPackagingUoM', 'Choose a packaging UoM')}
        isLoading={isLoading}
        packagingUnits={stockItem.packagingUnits}
        invalid={!!errors.defaultStockOperationsUoMUuid}
        invalidText={errors.defaultStockOperationsUoMUuid && errors?.defaultStockOperationsUoMUuid?.message}
      />
      {isLoading ? (
        <NumberInputSkeleton />
      ) : (
        <ControlledNumberInput
          id="reorderLevel"
          name="reorderLevel"
          control={control}
          controllerName="reorderLevel"
          size={'md'}
          allowEmpty={true}
          label={t('orderLevel', 'Reorder level') + ':'}
          invalid={!!errors.reorderLevel}
          value={stockItem.reorderLevel ?? ''}
          hideSteppers={true}
          invalidText={errors.reorderLevel && errors?.reorderLevel?.message}
        />
      )}
      <DispensingPackageMeasurement
        dispensingUnitPackagingUoMUuid={stockItem?.purchasePriceUoMUuid}
        name="reorderLevelUoMUuid"
        controllerName="reorderLevelUoMUuid"
        control={control}
        title={t('reorderLevelPackagingUnit', 'Reorder level packaging unit') + ':'}
        placeholder={t('notSet', 'Not Set')}
        isLoading={isLoading}
        packagingUnits={stockItem.packagingUnits}
        invalid={!!errors.reorderLevelUoMUuid}
        invalidText={errors.reorderLevelUoMUuid && errors?.reorderLevelUoMUuid?.message}
      />
      {isLoading ? (
        <NumberInputSkeleton />
      ) : (
        <ControlledNumberInput
          id="purchasePrice"
          name="purchasePrice"
          control={control}
          controllerName="purchasePrice"
          size={'md'}
          allowEmpty={true}
          label={t('purchasePrice', 'Purchase Price') + ':'}
          value={stockItem.purchasePrice ?? ''}
          invalid={!!errors.purchasePrice}
          hideSteppers={true}
          invalidText={errors.purchasePrice && errors?.purchasePrice?.message}
        />
      )}
      <DispensingPackageMeasurement
        dispensingUnitPackagingUoMUuid={stockItem?.purchasePriceUoMUuid}
        name="purchasePriceUoMUuid"
        controllerName="purchasePriceUoMUuid"
        control={control}
        title={t('purchasePricePackagingUnit', 'Purchase price packaging unit') + ':'}
        placeholder={t('notSet', 'Not Set')}
        isLoading={isLoading}
        packagingUnits={stockItem.packagingUnits}
        invalid={!!errors.purchasePriceUoMUuid}
        invalidText={errors.purchasePriceUoMUuid && errors?.purchasePriceUoMUuid?.message}
      />
    </>
  );
};

export default StockItemUnitsEdit;
