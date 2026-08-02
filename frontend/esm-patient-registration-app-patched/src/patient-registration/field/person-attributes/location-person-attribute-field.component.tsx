import React, { useCallback, useMemo, useRef, useState } from 'react';
import classNames from 'classnames';
import { ComboBox, InlineLoading, Layer } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { Field, useField } from 'formik';
import { type PersonAttributeTypeResponse } from '../../patient-registration.types';
import { useLocations } from './location-person-attribute-field.resource';
import styles from './../field.scss';

export interface LocationPersonAttributeFieldProps {
  id: string;
  personAttributeType: PersonAttributeTypeResponse;
  label?: string;
  locationTag: string;
  required?: boolean;
}

export function LocationPersonAttributeField({
  personAttributeType,
  id,
  label,
  locationTag,
  required,
}: LocationPersonAttributeFieldProps) {
  const { t } = useTranslation();
  const fieldName = `attributes.${personAttributeType.uuid}`;
  const [field, meta, { setValue }] = useField(`attributes.${personAttributeType.uuid}`);
  const [searchQuery, setSearchQuery] = useState('');
  const { locations, isLoading, loadingNewData } = useLocations(locationTag || null, searchQuery);
  const prevLocationOptions = useRef([]);

  const locationOptions = useMemo(() => {
    if (!(isLoading && loadingNewData)) {
      const newOptions = locations.map(({ resource: { id, name } }) => ({ value: id, label: name }));
      prevLocationOptions.current = newOptions;
      return newOptions;
    }
    return prevLocationOptions.current;
  }, [locations, isLoading, loadingNewData]);

  const selectedItem = useMemo(() => {
    if (typeof meta.value === 'string') {
      return locationOptions.find(({ value }) => value === meta.value) || null;
    }
    if (typeof meta.value === 'object' && meta.value) {
      return locationOptions.find(({ value }) => value === meta.value.uuid) || null;
    }
    return null;
  }, [locationOptions, meta.value]);

  // Callback for when updating the combobox input
  const handleInputChange = useCallback(
    (value: string | null) => {
      if (value) {
        // If the value exists in the locationOptions (i.e. a label matches the input), exit the function
        if (locationOptions.find(({ label }) => label === value)) return;
        // If the input is a new value, set the search query
        setSearchQuery(value);
        // Clear the current selected value since the input doesn't match any existing options
        setValue(null);
      }
    },
    [locationOptions, setValue],
  );
  const handleSelect = useCallback(
    ({ selectedItem }) => {
      if (selectedItem) {
        setValue(selectedItem.value);
      }
    },
    [setValue],
  );

  return (
    <div
      className={classNames(styles.customField, styles.halfWidthInDesktopView, styles.locationAttributeFieldContainer)}>
      <Layer>
        <Field name={fieldName}>
          {({ field, form: { touched, errors } }) => {
            return (
              <ComboBox
                id={id}
                name={`person-attribute-${personAttributeType.uuid}`}
                titleText={label}
                items={locationOptions}
                placeholder={t('searchLocationPersonAttribute', 'Search location')}
                onInputChange={handleInputChange}
                required={required}
                onChange={handleSelect}
                selectedItem={selectedItem}
                invalid={errors[fieldName] && touched[fieldName]}
                typeahead
              />
            );
          }}
        </Field>
      </Layer>
      {loadingNewData && (
        <div className={styles.loadingContainer}>
          <InlineLoading />
        </div>
      )}
    </div>
  );
}
