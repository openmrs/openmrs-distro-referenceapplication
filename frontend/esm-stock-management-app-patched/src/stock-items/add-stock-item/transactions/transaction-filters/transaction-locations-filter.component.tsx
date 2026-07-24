import React, { type ReactNode } from 'react';
import { ComboBox } from '@carbon/react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { useStockTagLocations } from '../../../../stock-lookups/stock-lookups.resource';

interface TransactionsLocationsFilterProps<T> {
  onLocationIdChange?: (location: string) => void;
  title?: string;
  placeholder?: string;
  invalid?: boolean;
  invalidText?: ReactNode;

  // Control
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const TransactionsLocationsFilter = <T,>(props: TransactionsLocationsFilterProps<T>) => {
  const { t } = useTranslation();
  const { stockLocations } = useStockTagLocations();
  return (
    <Controller
      name={props.controllerName}
      control={props.control}
      render={({ field: { onChange, value, ref } }) => (
        <ComboBox
          titleText={props.title}
          id={props.name}
          size={'md'}
          items={stockLocations ?? []}
          onChange={(data: { selectedItem }) => {
            props.onLocationIdChange?.(data?.selectedItem?.id ?? '');
            onChange(data?.selectedItem?.name || '');
          }}
          initialSelectedItem={`${stockLocations[0]?.name}`}
          itemToString={(item) => (item ? item.name : '')}
          shouldFilterItem={() => true}
          placeholder={props.placeholder}
          ref={ref}
          value={value}
          invalid={props.invalid}
          invalidText={props.invalidText}
        />
      )}
    />
  );
};

export default TransactionsLocationsFilter;
