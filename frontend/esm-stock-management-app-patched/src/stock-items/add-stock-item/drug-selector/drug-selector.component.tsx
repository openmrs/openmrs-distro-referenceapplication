import React, { type ReactNode, useCallback, useEffect, useRef, useState } from 'react';
import { ComboBox, InlineLoading } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { type Control, Controller, type FieldValues } from 'react-hook-form';
import { type Drug } from '../../../core/api/types/concept/Drug';
import { fetchStockItem } from '../../stock-items.resource';
import { useDrugsHook } from './drug-selector.resource';

interface DrugSelectorProps<T> {
  placeholder?: string;
  onDrugChanged?: (drug: Drug | null | undefined) => void;
  title?: string;
  invalid?: boolean;
  invalidText?: ReactNode;
  initialDrugName?: string;
  readOnly?: boolean;

  // Control
  controllerName: string;
  name: string;
  control: Control<FieldValues, T>;
}

const DrugSelector = <T,>(props: DrugSelectorProps<T>) => {
  const [inputValue, setInputValue] = useState(props.initialDrugName ?? '');
  const { t } = useTranslation();
  const { isLoading, drugList } = useDrugsHook(inputValue, undefined, !props.readOnly);
  const [selectedDrug, setSelectedDrug] = useState<Drug | null>(null);
  const [showExistenceError, setShowExistenceError] = useState(false);
  const existenceCheckUuidRef = useRef<string | null>(null);

  const handleInputChange = (value: string) => {
    setInputValue(value);
  };

  const checkDrugExistence = useCallback(
    (drugUuid: string) => {
      if (props.readOnly) {
        return;
      }

      existenceCheckUuidRef.current = drugUuid;

      fetchStockItem(drugUuid).then((result: any) => {
        if (existenceCheckUuidRef.current !== drugUuid) {
          return;
        }
        const itemExists = (result?.results?.length ?? 0) !== 0;
        setShowExistenceError(itemExists);
      });
    },
    [props.readOnly],
  );

  useEffect(() => {
    if (props.readOnly) {
      setShowExistenceError(false);
      existenceCheckUuidRef.current = null;
    }
  }, [props.readOnly]);

  // Build the items list, ensuring the selected drug is always included
  // even if the current search results don't contain it
  const items = drugList || [];
  const resolvedItems =
    selectedDrug && !items.some((d) => d.uuid === selectedDrug.uuid) ? [selectedDrug, ...items] : items;

  return (
    <div>
      <Controller
        name={props.controllerName}
        control={props.control}
        render={({ field: { onChange, value, ref } }) => {
          // When the drug hasn't been fetched yet (e.g. initial edit load),
          // create a placeholder so Carbon can display the name
          const placeholder =
            value && !resolvedItems.some((d) => d.uuid === value) && props.initialDrugName
              ? ({ uuid: value, name: props.initialDrugName } as Drug)
              : null;
          const comboItems = placeholder ? [placeholder, ...resolvedItems] : resolvedItems;

          return (
            <ComboBox
              titleText={props.title}
              id={props.name}
              size={'md'}
              items={comboItems}
              selectedItem={resolvedItems.find((p) => p.uuid === value) ?? placeholder}
              itemToString={drugName}
              onChange={(data: { selectedItem: Drug | null | undefined }) => {
                setShowExistenceError(false);
                setSelectedDrug(data.selectedItem ?? null);
                props.onDrugChanged?.(data.selectedItem);
                onChange(data.selectedItem?.uuid ?? '');
                if (!props.readOnly && data.selectedItem?.uuid) {
                  checkDrugExistence(data.selectedItem.uuid);
                } else {
                  existenceCheckUuidRef.current = null;
                }
              }}
              onInputChange={(event) => handleInputChange(event)}
              placeholder={props.placeholder}
              invalid={props.invalid}
              invalidText={props.invalidText}
              disabled={props.readOnly}
              ref={ref}
            />
          );
        }}
      />
      {isLoading && <InlineLoading status="active" iconDescription="Searching" description="Searching..." />}
      {!props.readOnly && showExistenceError && (
        <div style={{ color: '#da1e28' }}>{t('itemAlreadyExists', 'Item already exists')}</div>
      )}
    </div>
  );
};

function drugName(item: Drug): string {
  return item ? `${item.name}${item.concept ? ` (${item.concept.display})` : ''}` : '';
}

export default DrugSelector;
