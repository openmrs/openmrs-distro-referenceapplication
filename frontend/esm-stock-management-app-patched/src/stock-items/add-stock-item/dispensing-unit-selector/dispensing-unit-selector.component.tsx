import React, { type ReactNode } from 'react';
import { ComboBox, TextInputSkeleton } from '@carbon/react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { useConfig } from '@openmrs/esm-framework';
import { type Concept } from '../../../core/api/types/concept/Concept';
import { type ConfigObject } from '../../../config-schema';
import { useConcept } from '../../../stock-lookups/stock-lookups.resource';

interface DispensingUnitSelectorProps<T> {
  control: Control<FieldValues, T>;
  controllerName: string;
  dispensingUnitUuid?: string;
  invalid?: boolean;
  invalidText?: ReactNode;
  name: string;
  onDispensingUnitChange?: (unit: Concept) => void;
  placeholder?: string;
  title?: string;
}

const DispensingUnitSelector = <T,>(props: DispensingUnitSelectorProps<T>) => {
  const { packingUnitsUUID } = useConfig<ConfigObject>();
  const {
    items: { answers: packingUnits },
    isLoading,
  } = useConcept(packingUnitsUUID);

  if (isLoading) {
    return <TextInputSkeleton />;
  }

  return (
    <Controller
      control={props.control}
      name={props.controllerName}
      render={({ field: { onChange, value, ref } }) => (
        <ComboBox
          id={props.name}
          name={props.name}
          items={packingUnits || []}
          selectedItem={packingUnits?.find((p) => p.uuid === value) ?? null}
          invalid={props.invalid}
          invalidText={props.invalidText}
          itemToString={(item?: Concept) => item?.display ?? ''}
          onChange={(data: { selectedItem: Concept | null }) => {
            if (data.selectedItem) {
              props.onDispensingUnitChange?.(data.selectedItem);
              onChange(data.selectedItem.uuid);
            } else {
              onChange('');
            }
          }}
          placeholder={props.placeholder}
          ref={ref}
          size="md"
          titleText={props.title}
        />
      )}
    />
  );
};

export default DispensingUnitSelector;
