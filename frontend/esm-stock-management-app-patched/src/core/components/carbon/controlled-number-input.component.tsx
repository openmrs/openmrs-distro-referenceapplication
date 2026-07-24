import React from 'react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { type NumberInputProps } from '@carbon/react/lib/components/NumberInput/NumberInput';
import { NumberInput } from '@carbon/react';
import { type StockItemPackagingUOMDTO } from '../../api/types/stockItem/StockItemPackagingUOM';

interface ControlledNumberInputProps<T> extends NumberInputProps {
  row?: StockItemPackagingUOMDTO;
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const ControlledNumberInput = <T,>(props: ControlledNumberInputProps<T>) => {
  const { controllerName, name, control, row, onChange: onChangeProp, id, ...numberInputProps } = props;

  return (
    <Controller
      name={controllerName}
      control={control}
      render={({ field: { onChange, value, ref } }) => (
        <NumberInput
          disableWheel
          {...numberInputProps}
          id={`${name}-${id}-${row?.uuid}`}
          value={row?.factor ?? value}
          ref={ref}
          onChange={(
            event: React.MouseEvent<HTMLButtonElement>,
            state: {
              value: number | string;
              direction: string;
            },
          ) => {
            onChange(state.value);

            if (onChangeProp) {
              onChangeProp(event, state);
            }
          }}
        />
      )}
    />
  );
};

export default ControlledNumberInput;
