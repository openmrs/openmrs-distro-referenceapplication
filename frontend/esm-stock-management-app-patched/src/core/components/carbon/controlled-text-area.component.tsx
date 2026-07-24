import React, { type ChangeEvent } from 'react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { type TextAreaProps } from '@carbon/react/lib/components/TextArea/TextArea';
import { TextArea } from '@carbon/react';

interface ControlledTextAreaProps<T> extends TextAreaProps {
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const ControlledTextArea = <T,>(props: ControlledTextAreaProps<T>) => {
  return (
    <Controller
      name={props.controllerName}
      control={props.control}
      render={({ field: { onChange, value, ref } }) => (
        <TextArea
          {...props}
          onChange={(e: ChangeEvent<HTMLTextAreaElement>) => {
            onChange(e.target.value);

            // Fire prop change
            if (props.onChange) {
              props.onChange(e);
            }
          }}
          id={props.name}
          ref={ref}
          value={value}
        />
      )}
    />
  );
};

export default ControlledTextArea;
