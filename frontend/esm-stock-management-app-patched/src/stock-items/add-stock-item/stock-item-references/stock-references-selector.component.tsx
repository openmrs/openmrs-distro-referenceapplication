import React from 'react';
import { ComboBox, TextInputSkeleton } from '@carbon/react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { type StockSource } from '../../../core/api/types/stockOperation/StockSource';
import { useStockSources } from '../../../stock-sources/stock-sources.resource';
import { type Concept } from '../../../core/api/types/concept/Concept';
import { type StockItemReferenceDTO } from '../../../core/api/types/stockItem/StockItemReference';
import { ResourceRepresentation } from '../../../core/api/api';

interface StockSourceSelectorProps<T> {
  row?: StockItemReferenceDTO;
  onSourceChange?: (unit: StockSource) => void;
  title?: string;
  placeholder?: string;
  invalid?: boolean;

  // Control
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const StockSourceSelector = <T,>(props: StockSourceSelectorProps<T>) => {
  const {
    items: { results: sourcesList },
    isLoading,
  } = useStockSources({
    v: ResourceRepresentation.Default,
  });
  if (isLoading) return <TextInputSkeleton />;

  return (
    <Controller
      name={props.controllerName}
      control={props.control}
      render={({ field: { onChange, value, ref } }) => (
        <ComboBox
          titleText={props.title}
          id={props.name}
          size={'md'}
          items={sourcesList || []}
          onChange={(data: { selectedItem: StockSource }) => {
            props.onSourceChange?.(data.selectedItem);
            onChange(data.selectedItem?.uuid);
          }}
          initialSelectedItem={sourcesList?.find((p) => p.uuid === props.row?.uuid) || {}}
          itemToString={(item?: Concept) => (item && item?.name ? `${item?.name}` : '')}
          shouldFilterItem={() => true}
          value={sourcesList?.find((p) => p.uuid === value)?.name ?? ''}
          placeholder={props.placeholder}
          ref={ref}
          invalid={props.invalid}
        />
      )}
    />
  );
};

export default StockSourceSelector;
