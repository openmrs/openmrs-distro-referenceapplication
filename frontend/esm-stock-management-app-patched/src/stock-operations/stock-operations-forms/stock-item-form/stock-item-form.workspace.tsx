import React, { useMemo } from 'react';
import {
  Button,
  ButtonSet,
  Column,
  DatePicker,
  DatePickerInput,
  Form,
  NumberInput,
  Stack,
  TextInput,
} from '@carbon/react';
import classNames from 'classnames';
import { Controller, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { zodResolver } from '@hookform/resolvers/zod';
import { useConfig, useLayoutType } from '@openmrs/esm-framework';
import { type z } from 'zod';
import { type ConfigObject } from '../../../config-schema';
import { DATE_PICKER_CONTROL_FORMAT, DATE_PICKER_FORMAT, formatForDatePicker, today } from '../../../constants';
import {
  operationFromString,
  type StockOperationType,
} from '../../../core/api/types/stockOperation/StockOperationType';
import { useStockItem } from '../../../stock-items/stock-items.resource';
import { type BaseStockOperationItemFormData, getStockOperationItemFormSchema } from '../../validation-schema';
import useOperationTypePermisions from '../hooks/useOperationTypePermisions';
import BatchNoSelector from '../input-components/batch-no-selector.component';
import QtyUomSelector from '../input-components/quantity-uom-selector.component';
import styles from './stock-item-form.scss';

export interface StockItemFormProps {
  stockOperationType: StockOperationType;
  stockOperationItem: BaseStockOperationItemFormData;
  onSave?: (data: BaseStockOperationItemFormData) => void;
  onBack?: () => void;
}

const StockItemForm: React.FC<StockItemFormProps> = ({ stockOperationType, stockOperationItem, onSave, onBack }) => {
  const isTablet = useLayoutType() === 'tablet';
  const operationType = useMemo(() => {
    return operationFromString(stockOperationType.operationType);
  }, [stockOperationType]);
  const formSchema = useMemo(() => {
    return getStockOperationItemFormSchema(operationType);
  }, [operationType]);
  const operationTypePermision = useOperationTypePermisions(stockOperationType);
  const { useItemCommonNameAsDisplay } = useConfig<ConfigObject>();

  const fields = formSchema.keyof().options;
  type FormData = z.infer<typeof formSchema>;
  const form = useForm<FormData>({
    resolver: zodResolver(formSchema),
    defaultValues: stockOperationItem,
    mode: 'all',
  });
  const { t } = useTranslation();
  const { item } = useStockItem(form.getValues('stockItemUuid'));
  const commonName = useMemo(() => {
    if (!useItemCommonNameAsDisplay) return;
    const drugName = item?.drugName ? `(Drug name: ${item.drugName})` : undefined;
    return `${item?.commonName || t('noCommonNameAvailable', 'No common name available') + (drugName ?? '')}`;
  }, [item, useItemCommonNameAsDisplay, t]);

  const drugName = useMemo(() => {
    if (useItemCommonNameAsDisplay) return;
    const commonName = item?.commonName ? `(Common name: ${item.commonName})` : undefined;
    return `${item?.drugName || t('noDrugNameAvailable', 'No drug name available') + (commonName ?? '')}`;
  }, [item, useItemCommonNameAsDisplay, t]);

  const onSubmit = (data: z.infer<typeof formSchema>) => {
    onSave?.(data);
  };

  return (
    <Form onSubmit={form.handleSubmit(onSubmit)} className={styles.form}>
      <Stack gap={4} className={styles.grid}>
        <p className={styles.title}>{useItemCommonNameAsDisplay ? commonName : drugName}</p>

        {(operationTypePermision.requiresActualBatchInfo || operationTypePermision.requiresBatchUuid) &&
          fields.includes('batchNo' as keyof FormData) && (
            <Column>
              <Controller
                control={form.control}
                defaultValue={stockOperationItem?.batchNo}
                name={'batchNo' as keyof FormData}
                render={({ field, fieldState: { error } }) => (
                  <TextInput
                    maxLength={50}
                    {...field}
                    value={String(field.value ?? '')}
                    invalidText={error?.message}
                    invalid={!!error?.message}
                    placeholder={t('batchNumber', 'Batch number')}
                    labelText={t('batchNumber', 'Batch number')}
                    id="batchNumber"
                  />
                )}
              />
            </Column>
          )}

        {operationTypePermision.requiresBatchUuid && !operationTypePermision.requiresActualBatchInfo && (
          <Column>
            <Controller
              control={form.control}
              name={'stockBatchUuid' as keyof FormData}
              render={({ field, fieldState: { error } }) => (
                <BatchNoSelector
                  initialValue={stockOperationItem?.stockBatchUuid}
                  onValueChange={field.onChange}
                  stockItemUuid={stockOperationItem.stockItemUuid}
                  error={error?.message}
                />
              )}
            />
          </Column>
        )}
        {(operationTypePermision.requiresActualBatchInfo || operationTypePermision.requiresBatchUuid) &&
          fields.includes('expiration' as keyof FormData) && (
            <Column>
              <Controller
                control={form.control}
                name={'expiration' as keyof FormData}
                render={({ field, fieldState: { error } }) => {
                  const { value, onChange, onBlur, name, ref } = field;
                  return (
                    <DatePicker
                      datePickerType="single"
                      minDate={formatForDatePicker(today())}
                      locale="en"
                      className={styles.datePickerInput}
                      dateFormat={DATE_PICKER_CONTROL_FORMAT}
                      value={(value as unknown as Date) || null}
                      onChange={([newDate]) => {
                        onChange(newDate);
                      }}
                    >
                      <DatePickerInput
                        id={`expiration-input`}
                        placeholder={DATE_PICKER_FORMAT}
                        labelText={t('expiration', 'Expiration date')}
                        invalid={!!error?.message}
                        invalidText={error?.message}
                      />
                    </DatePicker>
                  );
                }}
              />
            </Column>
          )}

        <Column>
          <Controller
            control={form.control}
            name="quantity"
            render={({ field, fieldState: { error } }) => (
              <NumberInput
                allowEmpty
                className="small-placeholder-text"
                disableWheel
                hideSteppers
                id={`qty`}
                {...field}
                label={t('quantity', 'Quantity')}
                invalidText={error?.message}
                invalid={!!error?.message}
              />
            )}
          />
        </Column>
        <Column>
          <Controller
            control={form.control}
            name={'stockItemPackagingUOMUuid'}
            render={({ field, fieldState: { error } }) => (
              <QtyUomSelector
                stockItemUuid={stockOperationItem?.stockItemUuid}
                intiallvalue={field.value}
                error={error?.message}
                onValueChange={field.onChange}
              />
            )}
          />
        </Column>

        {operationTypePermision?.canCaptureQuantityPrice && fields.includes('purchasePrice' as keyof FormData) && (
          <Column>
            <Controller
              control={form.control}
              name={'purchasePrice' as keyof FormData}
              render={({ field, fieldState: { error } }) => {
                const { value, onChange, onBlur, disabled, name, ref } = field;
                return (
                  <TextInput
                    value={String(value ?? '')}
                    onChange={onChange}
                    onBlur={onBlur}
                    disabled={disabled}
                    name={name}
                    ref={ref}
                    labelText={t('purchasePrice', 'Purchase Price')}
                    invalid={!!error?.message}
                    invalidText={error?.message}
                    id={`purchaseprice`}
                    placeholder={t('purchasePrice', 'Purchase Price')}
                  />
                );
              }}
            />
          </Column>
        )}
      </Stack>

      <ButtonSet
        className={classNames(styles.buttonSet, {
          [styles.tablet]: isTablet,
          [styles.desktop]: !isTablet,
        })}
      >
        <Button className={styles.button} kind="secondary" onClick={onBack}>
          {t('discard', 'Discard')}
        </Button>
        <Button className={styles.button} kind="primary" type="submit" disabled={form.formState.isSubmitting}>
          {t('save', 'Save')}
        </Button>
      </ButtonSet>
    </Form>
  );
};

export default StockItemForm;
