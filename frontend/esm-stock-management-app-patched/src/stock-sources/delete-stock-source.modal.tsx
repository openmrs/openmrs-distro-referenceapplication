import React from 'react';
import { useTranslation } from 'react-i18next';
import { Button, ModalHeader, ModalBody, ModalFooter } from '@carbon/react';
import { getCoreTranslation } from '@openmrs/esm-framework';
import styles from './delete-stock-modal.scss';

interface DeleteStockSourceModalProps {
  uuid?: string;
  close: () => void;
  onConfirmation: () => void;
}

const DeleteStockSourceModal: React.FC<DeleteStockSourceModalProps> = ({ close, onConfirmation, uuid }) => {
  const { t } = useTranslation();
  const handleCancel = () => close();
  const handleDelete = () => onConfirmation?.();

  return (
    <>
      <ModalHeader closeModal={close} className={styles.modalHeader}>
        {t('deleteStock', 'Delete Stock source')}?
      </ModalHeader>
      <ModalBody>
        <p className={styles.bodyText}>
          {t('deleteConfirmationText', `Are you sure you want to delete this source? This action can't be undone.`, {
            encounter: uuid,
          })}
        </p>
      </ModalBody>
      <ModalFooter>
        <Button size="lg" kind="secondary" onClick={handleCancel}>
          {getCoreTranslation('cancel')}
        </Button>
        <Button autoFocus kind="danger" onClick={handleDelete} size="lg">
          {t('delete', 'Delete')}
        </Button>
      </ModalFooter>
    </>
  );
};

export default DeleteStockSourceModal;
