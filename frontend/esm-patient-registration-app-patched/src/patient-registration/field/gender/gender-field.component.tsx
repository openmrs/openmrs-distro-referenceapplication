import React from 'react';
import { RadioButton, RadioButtonGroup } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { useField } from 'formik';
import { type RegistrationConfig } from '../../../config-schema';
import { usePatientRegistrationContext } from '../../patient-registration-context';
import { useConfig } from '@openmrs/esm-framework';
import styles from '../field.scss';

export const GenderField: React.FC = () => {
  const { t } = useTranslation();
  const { fieldConfigurations } = useConfig<RegistrationConfig>();
  const { setFieldValue, setFieldTouched } = usePatientRegistrationContext();
  const [field, meta] = useField('gender');
  const fieldConfigs = fieldConfigurations?.gender;

  const setGender = (gender: string) => {
    setFieldValue('gender', gender);
    setFieldTouched('gender', true, false);
  };
  /**
   * DO NOT REMOVE THIS COMMENT HERE, ADDS TRANSLATION FOR SEX OPTIONS
   * t('male', 'Male')
   * t('female', 'Female')
   * t('other', 'Other')
   * t('unknown', 'Unknown')
   */

  return (
    <div className={styles.halfWidthInDesktopView}>
      <h4 className={styles.productiveHeading02Light}>{t('sexFieldLabelText', 'Sex')}</h4>
      <div className={styles.sexField}>
        <RadioButtonGroup
          name="gender"
          legendText={t('genderLabelText', 'Sex')}
          orientation="vertical"
          onChange={setGender}
          valueSelected={field.value}>
          {fieldConfigs.map((option) => {
            const label = option.label || option.value;
            return (
              <RadioButton
                id={`gender-option-${option.value}`}
                key={label}
                labelText={t(label, label)}
                value={option.value}
              />
            );
          })}
        </RadioButtonGroup>
        {meta.touched && meta.error && (
          <div className={styles.radioFieldError}>{t(meta.error, 'Gender is required')}</div>
        )}
      </div>
    </div>
  );
};
