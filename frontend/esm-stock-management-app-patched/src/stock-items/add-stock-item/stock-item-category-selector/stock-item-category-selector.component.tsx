import React, { type ReactNode, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { type Control, type FieldValues, useController } from 'react-hook-form';
import { type Concept } from '../../../core/api/types/concept/Concept';
import { ComboBox, TextInputSkeleton } from '@carbon/react';
import { useConfig } from '@openmrs/esm-framework';
import { useConcept } from '../../../stock-lookups/stock-lookups.resource';
import { type ConfigObject } from '../../../config-schema';

interface StockItemCategorySelectorProps<T> {
  onCategoryUuidChange?: (unit: Concept | null | undefined) => void;
  title?: string;
  placeholder?: string;
  invalid?: boolean;
  invalidText?: ReactNode;
  itemType?: string;

  // Control
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const StockItemCategorySelector = <T,>(props: StockItemCategorySelectorProps<T>) => {
  const { t } = useTranslation();

  const { stockItemCategoryUUID } = useConfig<ConfigObject>();
  const {
    items: { answers: categories },
    isLoading,
  } = useConcept(stockItemCategoryUUID);

  const filteredCategories = props.itemType ? categories?.filter((c) => c.display === props?.itemType) : categories;

  const {
    field: { onChange, value, ref },
  } = useController({
    name: props.controllerName,
    control: props.control,
  });

  const prevItemTypeRef = useRef(props.itemType);

  useEffect(() => {
    if (prevItemTypeRef.current !== props.itemType && filteredCategories) {
      prevItemTypeRef.current = props.itemType;
      if (value && !filteredCategories.some((c) => c.uuid === value)) {
        onChange('');
      }
    }
  }, [props.itemType, filteredCategories, value, onChange]);

  if (isLoading) {
    return <TextInputSkeleton />;
  }

  return (
    <ComboBox
      titleText={props.title}
      id={props.name}
      size={'md'}
      items={filteredCategories || []}
      onChange={(data: { selectedItem: Concept | null | undefined }) => {
        props.onCategoryUuidChange?.(data.selectedItem);
        onChange(data.selectedItem?.uuid);
      }}
      selectedItem={filteredCategories?.find((p) => p.uuid === value) ?? null}
      itemToString={(item?: Concept) => (item && item?.display ? `${t(item?.display)}` : '')}
      shouldFilterItem={() => true}
      placeholder={props.placeholder}
      ref={ref}
      invalid={props.invalid}
      invalidText={props.invalidText}
    />
  );
};

export default StockItemCategorySelector;
