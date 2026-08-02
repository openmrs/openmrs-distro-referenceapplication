import React, { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { Layer, TextInput } from '@carbon/react';
import { useField } from 'formik';

export type TextInputProps = React.ComponentProps<typeof TextInput>;

interface InputProps extends TextInputProps {
  checkWarning?(value: string): string;
}

export const Input: React.FC<InputProps> = ({ checkWarning, ...props }) => {
  const [field, meta] = useField(props.name);
  const { t } = useTranslation();

  /*
    Do not remove these comments
    t('givenNameRequired')
    t('familyNameRequired')
    t('genderUnspecified')
    t('genderRequired')
    t('birthdayRequired')
    t('birthdayNotInTheFuture')
    t('negativeYears')
    t('negativeMonths')
    t('deathdayNotInTheFuture')
    t('invalidEmail')
    t('numberInNameDubious')
    t('yearsEstimateRequired')
    t('deathdayIsRequired', 'Death date is required when the patient is marked as deceased.')
    t('deathdayBeforeBirthday', 'Death date and time cannot be before the birthday')
    t('deathCauseRequired', 'Cause of death is required')
    t('nonCodedCauseOfDeathRequiredWhenSelected', 'Non-coded cause of death is required')
  */

  const value = field.value || '';
  const invalidText = meta.error || '';
  const warnText = useMemo(() => {
    if (!invalidText && typeof checkWarning === 'function') {
      const warning = checkWarning(value);
      return warning && t(warning);
    }

    return undefined;
  }, [checkWarning, invalidText, value, t]);

  const labelText = props.required ? props.labelText : `${props.labelText} (${t('optional', 'optional')})`;

  return (
    <div style={{ marginBottom: '1rem' }}>
      <Layer>
        <TextInput
          {...props}
          {...field}
          labelText={labelText}
          invalid={!!(meta.touched && meta.error)}
          invalidText={invalidText}
          warn={!!warnText}
          warnText={warnText}
          value={value}
        />
      </Layer>
    </div>
  );
};
