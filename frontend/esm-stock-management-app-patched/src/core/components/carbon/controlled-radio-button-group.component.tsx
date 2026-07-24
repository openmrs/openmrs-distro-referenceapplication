import React from 'react';
import { type RadioButtonGroupProps } from '@carbon/react/lib/components/RadioButtonGroup/RadioButtonGroup';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { RadioButtonGroup, RadioButton } from '@carbon/react';
import { type RadioOption } from '../../../stock-items/add-stock-item/stock-item-details/stock-item-details.resource';

interface ControlledRadioButtonGroupProps<T>
  extends Omit<RadioButtonGroupProps, 'onChange' | 'id' | 'ref' | 'value' | 'defaultSelected'> {
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
  options: RadioOption[];
  id?: string;
  onChange?: (selectedValue: string, name: string, event: React.ChangeEvent<HTMLInputElement>) => void;
}

const ControlledRadioButtonGroup = <T,>(props: ControlledRadioButtonGroupProps<T>) => {
  const { controllerName, name, control, options, onChange: onChangeProp, id: propsId, ...radioGroupProps } = props;

  return (
    <Controller
      name={controllerName}
      control={control}
      render={({ field: { onChange, value, ref } }) => (
        <RadioButtonGroup
          {...radioGroupProps}
          name={name}
          onChange={(selectedValue: string, name: string, event: React.ChangeEvent<HTMLInputElement>) => {
            const matchedOption = options.find((option) => String(option.value) === selectedValue);
            onChange(matchedOption ? matchedOption.value : selectedValue);

            // Fire prop change
            if (onChangeProp) {
              onChangeProp(selectedValue, name, event);
            }
          }}
          id={name}
          ref={ref}
          defaultSelected={propsId}
          valueSelected={value != null ? String(value) : value}
        >
          {options.map((option, index) => (
            <RadioButton
              key={`${index}-${name}-${option.value}`}
              id={`${name}-${option.value}`}
              labelText={option.label}
              value={String(option.value)}
            />
          ))}
        </RadioButtonGroup>
      )}
    />
  );
};

export default ControlledRadioButtonGroup;
