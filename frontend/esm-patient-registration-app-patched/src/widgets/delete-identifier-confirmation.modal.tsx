import React from 'react';
import { useTranslation } from 'react-i18next';
import { Button, ModalBody, ModalHeader, ModalFooter } from '@carbon/react';

interface DeleteIdentifierConfirmationModalProps {
  closeModal: () => void;
  deleteIdentifier: (x: boolean) => void;
  identifierName: string;
  identifierValue: string;
}

const DeleteIdentifierConfirmationModal: React.FC<DeleteIdentifierConfirmationModalProps> = ({
  closeModal,
  deleteIdentifier,
  identifierName,
  identifierValue,
}) => {
  const { t } = useTranslation();

  return (
    <>
      <ModalHeader
        closeModal={closeModal}
        title={t('deleteIdentifierModalHeading', 'Delete identifier?')}></ModalHeader>
      <ModalBody>
        <p>
          {identifierName && identifierValue && (
            <span>
              <strong>{identifierName}</strong>
              {t('deleteIdentifierModalText', ' has a value of ')} <strong>{identifierValue}</strong>.{' '}
            </span>
          )}
          {t('confirmIdentifierDeletionText', 'Are you sure you want to delete this identifier?')}
        </p>
      </ModalBody>
      <ModalFooter>
        <Button kind="secondary" size="lg" onClick={closeModal}>
          {t('cancel', 'Cancel')}
        </Button>
        <Button kind="danger" size="lg" onClick={() => deleteIdentifier(true)}>
          {t('removeIdentifierButton', 'Remove identifier')}
        </Button>
      </ModalFooter>
    </>
  );
};

export default DeleteIdentifierConfirmationModal;
