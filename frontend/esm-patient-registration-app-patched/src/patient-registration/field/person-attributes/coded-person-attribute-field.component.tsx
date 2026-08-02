import React, { useEffect, useMemo, useState } from 'react';
import classNames from 'classnames';
import { useTranslation } from 'react-i18next';
import { Field } from 'formik';
import { Layer, Select, SelectItem } from '@carbon/react';
import { reportError } from '@openmrs/esm-framework';
import { type PersonAttributeTypeResponse } from '../../patient-registration.types';
import { useConceptAnswers } from '../field.resource';
import styles from './../field.scss';

export interface CodedPersonAttributeFieldProps {
  id: string;
  personAttributeType: PersonAttributeTypeResponse;
  answerConceptSetUuid: string;
  label?: string;
  customConceptAnswers: Array<{ uuid: string; label?: string }>;
  required: boolean;
}

export function CodedPersonAttributeField({
  id,
  personAttributeType,
  answerConceptSetUuid,
  label,
  customConceptAnswers,
  required,
}: CodedPersonAttributeFieldProps) {
  const { data: conceptAnswers, isLoading: isLoadingConceptAnswers } = useConceptAnswers(
    customConceptAnswers.length ? '' : answerConceptSetUuid,
  );

  const { t } = useTranslation();
  const fieldName = `attributes.${personAttributeType.uuid}`;
  const [error, setError] = useState(false);

  useEffect(() => {
    if (!answerConceptSetUuid && !customConceptAnswers.length) {
      reportError(
        t(
          'codedPersonAttributeNoAnswerSet',
          `The person attribute field '{{codedPersonAttributeFieldId}}' is of type 'coded' but has been defined without an answer concept set UUID. The 'answerConceptSetUuid' key is required.`,
          { codedPersonAttributeFieldId: id },
        ),
      );
      setError(true);
    }
  }, [answerConceptSetUuid, customConceptAnswers, id, t]);

  useEffect(() => {
    if (!isLoadingConceptAnswers && !customConceptAnswers.length) {
      if (!conceptAnswers) {
        reportError(
          t(
            'codedPersonAttributeAnswerSetInvalid',
            `The coded person attribute field '{{codedPersonAttributeFieldId}}' has been defined with an invalid answer concept set UUID '{{answerConceptSetUuid}}'.`,
            { codedPersonAttributeFieldId: id, answerConceptSetUuid },
          ),
        );
        setError(true);
      }
      if (conceptAnswers?.length === 0) {
        reportError(
          t(
            'codedPersonAttributeAnswerSetEmpty',
            `The coded person attribute field '{{codedPersonAttributeFieldId}}' has been defined with an answer concept set UUID '{{answerConceptSetUuid}}' that does not have any concept answers.`,
            {
              codedPersonAttributeFieldId: id,
              answerConceptSetUuid,
            },
          ),
        );
        setError(true);
      }
    }
  }, [isLoadingConceptAnswers, conceptAnswers, customConceptAnswers, t, id, answerConceptSetUuid]);

  const answers = useMemo(() => {
    if (customConceptAnswers.length) {
      return customConceptAnswers;
    }
    return isLoadingConceptAnswers || !conceptAnswers
      ? []
      : conceptAnswers
          .map((answer) => ({ ...answer, label: answer.display }))
          .sort((a, b) => a.label.localeCompare(b.label));
  }, [customConceptAnswers, conceptAnswers, isLoadingConceptAnswers]);

  if (error) {
    return null;
  }

  return (
    <div className={classNames(styles.customField, styles.halfWidthInDesktopView)}>
      {!isLoadingConceptAnswers ? (
        <Layer>
          <Field name={fieldName}>
            {({ field, form: { touched, errors }, meta }) => {
              return (
                <>
                  <Select
                    id={id}
                    name={`person-attribute-${personAttributeType.uuid}`}
                    labelText={label ?? personAttributeType?.display}
                    invalid={errors[fieldName] && touched[fieldName]}
                    required={required}
                    {...field}>
                    <SelectItem value={''} text={t('selectAnOption', 'Select an option')} />
                    {answers.map((answer) => (
                      <SelectItem key={answer.uuid} value={answer.uuid} text={answer.label} />
                    ))}
                  </Select>
                </>
              );
            }}
          </Field>
        </Layer>
      ) : null}
    </div>
  );
}
