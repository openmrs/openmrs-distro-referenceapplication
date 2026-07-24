import React, { type ChangeEvent } from 'react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { TextInput } from '@carbon/react';
import { type TextInputProps } from '@carbon/react/lib/components/TextInput/TextInput';

interface ControlledTextInputProps<T> extends TextInputProps {
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const ControlledTextInput = <T,>(props: ControlledTextInputProps<T>) => {
  const { controllerName, control, ...textInputProps } = props;

  return (
    <Controller
      name={controllerName}
      control={control}
      render={({ field: { onChange, value, ref } }) => (
        <TextInput
          {...textInputProps}
          onChange={(e: ChangeEvent<HTMLInputElement>) => {
            onChange(e.target.value);

            // Fire prop change
            if (props.onChange) {
              props.onChange(e);
            }
          }}
          id={props.id}
          ref={ref}
          value={value ?? props.value}
        />
      )}
    />
  );
};

export default ControlledTextInput;
