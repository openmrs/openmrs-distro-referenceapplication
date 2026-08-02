import React, { useEffect } from 'react';
import { useField } from 'formik';
import { Field } from '../../field/field.component';
import { usePatientRegistrationContext } from '../../patient-registration-context';
import styles from './../section.scss';

export interface DemographicsSectionProps {
  fields: Array<string>;
}

export const DemographicsSection: React.FC<DemographicsSectionProps> = ({ fields }) => {
  const [field, meta] = useField('addNameInLocalLanguage');
  const { setFieldValue } = usePatientRegistrationContext();

  useEffect(() => {
    if (!field.value && meta.touched) {
      setFieldValue('additionalGivenName', '');
      setFieldValue('additionalMiddleName', '');
      setFieldValue('additionalFamilyName', '');
    }
  }, [field.value, meta.touched, setFieldValue]);

  return (
    <section className={styles.formSection} aria-label="Demographics Section">
      {fields.map((field) => (
        <Field key={`demographics-${field}`} name={field} />
      ))}
    </section>
  );
};
