import React, { useCallback } from 'react';
import classNames from 'classnames';
import dayjs from 'dayjs';
import { Layer, SelectItem, TimePicker, TimePickerSelect } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { useField } from 'formik';
import { OpenmrsDatePicker } from '@openmrs/esm-framework';
import { usePatientRegistrationContext } from '../../patient-registration-context';
import type { FormValues } from '../../patient-registration.types';
import styles from '../field.scss';

export const DateAndTimeOfDeathField: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className={classNames(styles.dodField, styles.halfWidthInDesktopView)}>
      <h4 className={styles.productiveHeading02Light}>{t('deathDateInputLabel', 'Date of death')}</h4>
      <span>
        <DeathDateField />
        <DeathTimeField />
      </span>
    </div>
  );
};

function DeathDateField() {
  const { values, setFieldValue } = usePatientRegistrationContext();
  const [deathDate, deathDateMeta] = useField<keyof FormValues>('deathDate');
  const { t } = useTranslation();
  const today = dayjs().hour(23).minute(59).second(59).toDate();
  const onDateChange = useCallback(
    (selectedDate: Date) => {
      setFieldValue(
        'deathDate',
        selectedDate ? dayjs(selectedDate).hour(0).minute(0).second(0).millisecond(0).toDate() : undefined,
      );
    },
    [setFieldValue],
  );

  return (
    <Layer>
      <OpenmrsDatePicker
        {...deathDate}
        id="deathDate"
        invalidText={t(deathDateMeta.error)}
        invalid={!!(deathDateMeta.touched && deathDateMeta.error)}
        isRequired={values.isDead}
        labelText={t('deathDateInputLabel', 'Date of death')}
        maxDate={today}
        onChange={onDateChange}
      />
    </Layer>
  );
}

function DeathTimeField() {
  const { t } = useTranslation();
  const [deathTimeField, deathTimeMeta] = useField<keyof FormValues>('deathTime');
  const [deathTimeFormatField, deathTimeFormatMeta] = useField<keyof FormValues>('deathTimeFormat');

  return (
    <Layer>
      <TimePicker
        {...deathTimeField}
        id="time-picker"
        labelText={t('timeOfDeathInputLabel', 'Time of death (hh:mm)')}
        className={styles.timeOfDeathField}
        pattern="^(1[0-2]|0?[1-9]):([0-5]?[0-9])$"
        invalid={!!(deathTimeMeta.touched && deathTimeMeta.error)}
        invalidText={t(deathTimeMeta.error)}>
        <TimePickerSelect {...deathTimeFormatField} id="time-format-picker" aria-label={t('timeFormat', 'Time Format')}>
          <SelectItem value="AM" text="AM" />
          <SelectItem value="PM" text="PM" />
        </TimePickerSelect>
      </TimePicker>
    </Layer>
  );
}
