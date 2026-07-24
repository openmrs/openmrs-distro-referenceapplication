import React from 'react';
import { ComboBox, InlineNotification, SelectSkeleton } from '@carbon/react';
import { Controller, useFormContext } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useConfig } from '@openmrs/esm-framework';
import { type ConfigObject } from '../../../config-schema';
import { type Concept } from '../../../core/api/types/concept/Concept';
import { useConcept } from '../../../stock-lookups/stock-lookups.resource';
import { OperationType } from '../../../core/api/types/stockOperation/StockOperationType';

type StockOperationReasonSelectorProps = {
  stockOperationType: string;
};

const StockOperationReasonSelector: React.FC<StockOperationReasonSelectorProps> = ({ stockOperationType }) => {
  const { stockAdjustmentReasonUUID, stockTakeReasonUUID } = useConfig<ConfigObject>();
  const operationReason =
    stockOperationType === OperationType.STOCK_TAKE_OPERATION_TYPE ? stockTakeReasonUUID : stockAdjustmentReasonUUID;

  const form = useFormContext<{ reasonUuid: string }>();
  const {
    isLoading,
    error,
    items: { answers: reasons },
  } = useConcept(operationReason);
  const { t } = useTranslation();
  if (isLoading) return <SelectSkeleton role="progressbar" />;

  if (error)
    return (
      <InlineNotification
        lowContrast
        kind="error"
        title={t('reasonError', 'Error loading reasons concepts')}
        subtitle={error?.message}
      />
    );

  return (
    <Controller
      control={form.control}
      name={'reasonUuid'}
      render={({ field, fieldState: { error } }) => (
        <ComboBox
          readOnly={field.disabled}
          titleText={t('reason', 'Reason')}
          placeholder={t('chooseAReason', 'Choose a reason')}
          name={'reasonUuid'}
          id={'reasonUuid'}
          size="lg"
          items={reasons}
          initialSelectedItem={reasons?.find((p) => p.uuid === field.value)}
          selectedItem={reasons.find((p) => p.uuid === field.value)}
          itemToString={(item?: Concept) => (item && item?.display ? `${item?.display}` : '')}
          onChange={(data: { selectedItem?: Concept }) => {
            field.onChange(data?.selectedItem?.uuid);
          }}
          ref={field.ref}
          invalid={!!error?.message}
          invalidText={error?.message}
        />
      )}
    />
  );
};

export default StockOperationReasonSelector;
