import React, { useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { OverflowMenuItem } from '@carbon/react';
import { navigate } from '@openmrs/esm-framework';
import styles from './edit-patient-details-button.scss';

interface EditPatientDetailsButtonProps {
  patientUuid: string;
  closeMenu?: () => void;
}

const EditPatientDetailsButton: React.FC<EditPatientDetailsButtonProps> = ({ patientUuid, closeMenu }) => {
  const { t } = useTranslation();

  const handleClick = useCallback(() => {
    navigate({ to: `\${openmrsSpaBase}/patient/${patientUuid}/edit` });
  }, [patientUuid]);

  return (
    <OverflowMenuItem
      className={styles.menuitem}
      closeMenu={closeMenu}
      itemText={t('editPatientDetails', 'Edit patient details')}
      onClick={handleClick}
    />
  );
};

export default EditPatientDetailsButton;
