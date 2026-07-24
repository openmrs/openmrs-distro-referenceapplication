import React, { type ReactNode, useState } from 'react';
import { ComboBox, TextInputSkeleton } from '@carbon/react';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import useSWR from 'swr';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { useDebounce } from '../../../core/hooks/debounce-hook';

// This selector is only ever used for Non Pharmaceuticals stock items (Pharmaceuticals use
// DrugSelector, hitting /drug, instead). It hits the custom /non-drug resource, which mirrors
// /drug's shape but lists the answers of the "Non-drug" bucket concept under "Stock item
// category" (8ccf6066-9297-4d76-aaf3-00aa3714d198) - a curated non-drug item list.
interface NonDrugItem {
  uuid: string;
  display: string;
}

interface ConceptsSelectorProps<T> {
  conceptUuid?: string;
  control: Control<FieldValues, T>;
  controllerName: string;
  invalid?: boolean;
  invalidText?: ReactNode;
  name: string;
  onConceptUuidChange?: (unit: NonDrugItem) => void;
  placeholder?: string;
  title?: string;
}

const ConceptsSelector = <T,>(props: ConceptsSelectorProps<T>) => {
  const [searchQuery, setSearchQuery] = useState('');

  const url = `${restBaseUrl}/non-drug?v=default&limit=20${
    searchQuery ? `&q=${encodeURIComponent(searchQuery)}` : ''
  }`;
  const { data, isLoading } = useSWR<{ data: { results: Array<NonDrugItem> } }>(url, openmrsFetch);
  const items = data?.data.results ?? [];

  const handleInputChange = useDebounce((query: string) => setSearchQuery(query), 500);

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
          invalid={props.invalid}
          invalidText={props.invalidText}
          items={items}
          itemToString={(item?: NonDrugItem) => item?.display ?? ''}
          name={props.name}
          onChange={(data: { selectedItem: NonDrugItem | null | undefined }) => {
            if (data.selectedItem) {
              props.onConceptUuidChange?.(data.selectedItem);
              onChange(data.selectedItem.uuid);
            } else {
              onChange('');
            }
          }}
          onInputChange={handleInputChange}
          placeholder={props.placeholder}
          ref={ref}
          selectedItem={items?.find((p) => p.uuid === value) ?? null}
          size="md"
          titleText={props.title}
        />
      )}
    />
  );
};

export default ConceptsSelector;
