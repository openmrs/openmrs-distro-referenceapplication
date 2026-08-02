import React, { useMemo } from 'react';
import classNames from 'classnames';
import { Field, useField } from 'formik';
import { useTranslation } from 'react-i18next';
import { InlineNotification, Layer, Select, SelectItem, SelectSkeleton, TextInput } from '@carbon/react';
import { useConfig } from '@openmrs/esm-framework';
import { type RegistrationConfig } from '../../../config-schema';
import { useConceptAnswers } from '../field.resource';
import styles from '../field.scss';

export const CauseOfDeathField: React.FC = () => {
  const { t } = useTranslation();
  const { fieldConfigurations, freeTextFieldConceptUuid } = useConfig<RegistrationConfig>();
  const [deathCause, deathCauseMeta] = useField('deathCause');

  const conceptUuid = fieldConfigurations?.causeOfDeath?.conceptUuid;
  const required = fieldConfigurations?.causeOfDeath?.required;

  const {
    data: conceptAnswers,
    isLoading: isLoadingConceptAnswers,
    error: errorLoadingConceptAnswers,
  } = useConceptAnswers(conceptUuid);

  const answers = useMemo(() => {
    if (!isLoadingConceptAnswers && conceptAnswers) {
      return conceptAnswers.map((answer) => ({ ...answer, label: answer.display }));
    }
    return [];
  }, [conceptAnswers, isLoadingConceptAnswers]);

  if (isLoadingConceptAnswers) {
    return (
      <div className={classNames(styles.customField, styles.halfWidthInDesktopView)}>
        <h4 className={styles.productiveHeading02Light}>{t('causeOfDeathInputLabel', 'Cause of death')}</h4>
        <SelectSkeleton />
      </div>
    );
  }

  return (
    <div className={classNames(styles.customField, styles.halfWidthInDesktopView)}>
      <h4 className={styles.productiveHeading02Light}>{t('causeOfDeathInputLabel', 'Cause of death')}</h4>
      {errorLoadingConceptAnswers || !conceptUuid ? (
        <InlineNotification
          hideCloseButton
          kind="error"
          title={t('errorFetchingCodedCausesOfDeath', 'Error fetching coded causes of death')}
          subtitle={t('refreshOrContactAdmin', 'Try refreshing the page or contact your system administrator')}
        />
      ) : (
        <>
          <Field name="deathCause">
            {({ field, form: { touched, errors }, meta }) => {
              return (
                <Layer>
                  <Select
                    {...field}
                    id="deathCause"
                    invalid={errors.deathCause && touched.deathCause}
                    invalidText={errors.deathCause?.message}
                    labelText={t('causeOfDeathInputLabel', 'Cause of death')}
                    name="deathCause"
                    required={required}>
                    <SelectItem id="empty-default-option" value={null} text={t('selectAnOption', 'Select an option')} />
                    {answers.map((answer) => (
                      <SelectItem id={answer.uuid} key={answer.uuid} text={answer.label} value={answer.uuid} />
                    ))}
                  </Select>
                </Layer>
              );
            }}
          </Field>
          {deathCause.value === freeTextFieldConceptUuid && (
            <div className={styles.nonCodedCauseOfDeath}>
              <Field name="nonCodedCauseOfDeath">
                {({ field, form: { touched, errors }, meta }) => {
                  return (
                    <Layer>
                      <TextInput
                        {...field}
                        id="nonCodedCauseOfDeath"
                        invalid={errors?.nonCodedCauseOfDeath && touched.nonCodedCauseOfDeath}
                        invalidText={errors?.nonCodedCauseOfDeath?.message}
                        labelText={t('nonCodedCauseOfDeath', 'Non-coded cause of death')}
                        placeholder={t('enterNonCodedCauseOfDeath', 'Enter non-coded cause of death')}
                      />
                    </Layer>
                  );
                }}
              </Field>
            </div>
          )}
        </>
      )}
    </div>
  );
};
