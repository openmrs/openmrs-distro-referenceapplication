import React from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { v4 } from 'uuid';
import { type FormValues } from '../../patient-registration.types';
import styles from './../input.scss';

interface DummyDataInputProps {
  setValues(values: FormValues, shouldValidate?: boolean): void;
}

export const dummyFormValues: FormValues = {
  patientUuid: v4(),
  givenName: 'John',
  middleName: '',
  familyName: 'Smith',
  additionalGivenName: 'Joey',
  additionalMiddleName: '',
  additionalFamilyName: 'Smitty',
  addNameInLocalLanguage: true,
  gender: 'male',
  birthdate: new Date(2020, 1, 1),
  yearsEstimated: 1,
  monthsEstimated: 2,
  birthdateEstimated: true,
  telephoneNumber: '0800001066',
  isDead: false,
  deathDate: '',
  deathTime: '',
  deathTimeFormat: 'AM',
  deathCause: '',
  nonCodedCauseOfDeath: '',
  relationships: [],
  address: {
    address1: 'Bom Jesus Street',
    address2: '',
    cityVillage: 'Recife',
    stateProvince: 'Pernambuco',
    country: 'Brazil',
    postalCode: '50030-310',
  },
  identifiers: {},
};

export const DummyDataInput: React.FC<DummyDataInputProps> = ({ setValues }) => {
  const { t } = useTranslation();
  return (
    <Button
      kind="tertiary"
      onClick={() => setValues(dummyFormValues)}
      className={styles.dummyData}
      aria-label={t('inputDummyData', 'Input Dummy Data')}>
      {t('inputDummyData', 'Input Dummy Data')}
    </Button>
  );
};
