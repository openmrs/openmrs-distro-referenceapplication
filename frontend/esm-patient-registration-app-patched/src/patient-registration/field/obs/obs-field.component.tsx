import React, { useMemo } from 'react';
import classNames from 'classnames';
import { Field } from 'formik';
import { useTranslation } from 'react-i18next';
import { InlineNotification, Layer, Select, SelectItem } from '@carbon/react';
import { OpenmrsDatePicker, useConfig } from '@openmrs/esm-framework';
import { type ConceptResponse } from '../../patient-registration.types';
import { type FieldDefinition, type RegistrationConfig } from '../../../config-schema';
import { Input } from '../../input/basic-input/input/input.component';
import { useConcept, useConceptAnswers } from '../field.resource';
import { usePatientRegistrationContext } from '../../patient-registration-context';
import styles from './../field.scss';

export interface ObsFieldProps {
  fieldDefinition: FieldDefinition;
}

export function ObsField({ fieldDefinition }: ObsFieldProps) {
  const { t } = useTranslation();
  const { data: concept, isLoading } = useConcept(fieldDefinition.uuid);
  const config = useConfig<RegistrationConfig>();

  if (!config.registrationObs.encounterTypeUuid) {
    console.error(
      'The registration form has been configured to have obs fields, ' +
        'but no registration encounter type has been configured. Obs fields ' +
        'will not be displayed.',
    );
    return null;
  }

  if (isLoading) {
    return null;
  }

  switch (concept.datatype.display) {
    case 'Text':
      return (
        <TextObsField
          concept={concept}
          validationRegex={fieldDefinition.validation.matches}
          label={fieldDefinition.label}
          required={fieldDefinition.validation.required}
        />
      );
    case 'Numeric':
      return (
        <NumericObsField
          concept={concept}
          label={fieldDefinition.label}
          required={fieldDefinition.validation.required}
        />
      );
    case 'Date':
      return (
        <DateObsField
          concept={concept}
          label={fieldDefinition.label}
          required={fieldDefinition.validation.required}
          allowPastDates={fieldDefinition.allowPastDates}
          allowFutureDates={fieldDefinition.allowFutureDates}
        />
      );
    case 'Coded':
      return (
        <CodedObsField
          concept={concept}
          answerConceptSetUuid={fieldDefinition.answerConceptSetUuid}
          label={fieldDefinition.label}
          required={fieldDefinition.validation.required}
          customConceptAnswers={fieldDefinition.customConceptAnswers}
        />
      );
    default:
      return (
        <InlineNotification kind="error" title="Error">
          {t(
            'obsFieldUnknownDatatype',
            `Concept for obs field '{{fieldDefinitionId}}' has unknown datatype '{{datatypeName}}'`,
            { fieldDefinitionId: fieldDefinition.id, datatypeName: concept.datatype.display },
          )}
        </InlineNotification>
      );
  }
}

interface TextObsFieldProps {
  concept: ConceptResponse;
  validationRegex: string;
  label: string;
  required?: boolean;
}

function TextObsField({ concept, validationRegex, label, required }: TextObsFieldProps) {
  const { t } = useTranslation();

  const validateInput = (value: string) => {
    if (!value || !validationRegex || validationRegex === '' || typeof validationRegex !== 'string' || value === '') {
      return;
    }
    const regex = new RegExp(validationRegex);
    if (regex.test(value)) {
      return;
    } else {
      return t('invalidInput', 'Invalid Input');
    }
  };

  const fieldName = `obs.${concept.uuid}`;
  return (
    <div className={classNames(styles.customField, styles.halfWidthInDesktopView)}>
      <Field name={fieldName} validate={validateInput}>
        {({ field, form: { touched, errors }, meta }) => {
          return (
            <Input
              id={fieldName}
              labelText={label ?? concept.display}
              required={required}
              invalid={errors[fieldName] && touched[fieldName]}
              {...field}
            />
          );
        }}
      </Field>
    </div>
  );
}

interface NumericObsFieldProps {
  concept: ConceptResponse;
  label: string;
  required?: boolean;
}

function NumericObsField({ concept, label, required }: NumericObsFieldProps) {
  const fieldName = `obs.${concept.uuid}`;

  return (
    <div className={classNames(styles.customField, styles.halfWidthInDesktopView)}>
      <Field name={fieldName}>
        {({ field, form: { touched, errors }, meta }) => {
          return (
            <Input
              id={fieldName}
              labelText={label ?? concept.display}
              required={required}
              invalid={errors[fieldName] && touched[fieldName]}
              type="number"
              {...field}
            />
          );
        }}
      </Field>
    </div>
  );
}

interface DateObsFieldProps {
  concept: ConceptResponse;
  label: string;
  required?: boolean;
  allowPastDates?: boolean;
  allowFutureDates?: boolean;
}

function DateObsField({ concept, label, required, allowPastDates, allowFutureDates }: DateObsFieldProps) {
  const { t } = useTranslation();
  const fieldName = `obs.${concept.uuid}`;
  const { setFieldValue } = usePatientRegistrationContext();
  const futureDatesAllowed = allowFutureDates ?? true;
  const pastDatesAllowed = allowPastDates ?? true;

  const onDateChange = (date: Date) => {
    setFieldValue(fieldName, date);
  };

  return (
    <Layer>
      <div className={styles.dobField}>
        <Field name={fieldName}>
          {({ field, form: { touched, errors }, meta }) => {
            return (
              <>
                <OpenmrsDatePicker
                  id={fieldName}
                  {...field}
                  isRequired={required}
                  onChange={onDateChange}
                  labelText={label ?? concept.display}
                  isInvalid={errors[fieldName] && touched[fieldName]}
                  invalidText={t(meta.error)}
                  value={field.value}
                  minDate={!pastDatesAllowed ? new Date() : undefined}
                  maxDate={!futureDatesAllowed ? new Date() : undefined}
                />
              </>
            );
          }}
        </Field>
      </div>
    </Layer>
  );
}

interface CodedObsFieldProps {
  concept: ConceptResponse;
  answerConceptSetUuid?: string;
  label?: string;
  required?: boolean;
  customConceptAnswers: Array<{ uuid: string; label?: string }>;
}

function CodedObsField({ concept, answerConceptSetUuid, label, required, customConceptAnswers }: CodedObsFieldProps) {
  const { t } = useTranslation();
  const fieldName = `obs.${concept.uuid}`;

  const { data: conceptAnswers, isLoading: isLoadingConceptAnswers } = useConceptAnswers(
    customConceptAnswers.length ? '' : answerConceptSetUuid ?? concept.uuid,
  );

  const answers = useMemo(
    () =>
      customConceptAnswers.length
        ? customConceptAnswers
        : isLoadingConceptAnswers
          ? []
          : conceptAnswers.map((answer) => ({ ...answer, label: answer.display })),
    [customConceptAnswers, conceptAnswers, isLoadingConceptAnswers],
  );

  return (
    <div className={classNames(styles.customField, styles.halfWidthInDesktopView)}>
      {!isLoadingConceptAnswers ? (
        <Field name={fieldName}>
          {({ field, form: { touched, errors }, meta }) => {
            return (
              <Layer>
                <Select
                  id={fieldName}
                  name={fieldName}
                  labelText={label ?? concept?.display}
                  required={required}
                  invalid={errors[fieldName] && touched[fieldName]}
                  {...field}>
                  <SelectItem
                    key={`no-answer-select-item-${fieldName}`}
                    value={''}
                    text={t('selectAnOption', 'Select an option')}
                  />
                  {answers.map((answer) => (
                    <SelectItem key={answer.uuid} value={answer.uuid} text={answer.label} />
                  ))}
                </Select>
              </Layer>
            );
          }}
        </Field>
      ) : null}
    </div>
  );
}
