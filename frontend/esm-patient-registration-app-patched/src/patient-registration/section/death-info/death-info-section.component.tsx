import React from 'react';
import { useTranslation } from 'react-i18next';
import { Checkbox, Layer } from '@carbon/react';
import { useField } from 'formik';
import { usePatientRegistrationContext } from '../../patient-registration-context';
import { Field } from '../../field/field.component';
import styles from './../section.scss';

export interface DeathInfoSectionProps {
  fields: Array<string>;
}

export const DeathInfoSection: React.FC<DeathInfoSectionProps> = ({ fields }) => {
  const { t } = useTranslation();
  const { values, setFieldValue } = usePatientRegistrationContext();
  const [deathDate, deathDateMeta] = useField('deathDate');
  const today = new Date();

  return (
    <section className={styles.formSection} aria-label="Death Info Section">
      <section className={styles.fieldGroup}>
        <Layer>
          <div className={styles.isDeadFieldContainer}>
            <Checkbox
              checked={values.isDead}
              id="isDead"
              labelText={t('isDeadInputLabel', 'Is dead')}
              onChange={(event, { checked, id }) => setFieldValue(id, checked)}
            />
          </div>
        </Layer>
        {values.isDead ? fields.map((field) => <Field key={`death-info-${field}`} name={field} />) : null}
      </section>
    </section>
  );
};
