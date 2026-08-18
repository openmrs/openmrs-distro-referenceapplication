import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Button, InlineNotification, ModalHeader, ModalBody, ModalFooter, TextArea } from '@carbon/react';
import { getCoreTranslation } from '@openmrs/esm-framework';

interface DeleteStockItemModalProps {
  displayName?: string;
  /** Current quantity on hand across all locations; deletion is blocked while this is > 0. */
  quantityOnHand?: number;
  close: () => void;
  onConfirmation: (reason: string) => void;
}

const DeleteStockItemModal: React.FC<DeleteStockItemModalProps> = ({
  close,
  onConfirmation,
  displayName,
  quantityOnHand = 0,
}) => {
  const { t } = useTranslation();
  const [reason, setReason] = useState('');
  const hasStockOnHand = quantityOnHand > 0;
  const handleCancel = () => close();
  const handleDelete = () => onConfirmation?.(reason);

  return (
    <>
      <ModalHeader closeModal={close}>{t('deleteStockItem', 'Delete stock item')}?</ModalHeader>
      <ModalBody>
        {hasStockOnHand ? (
          <InlineNotification
            kind="error"
            lowContrast
            hideCloseButton
            title={t('cannotDeleteStockItem', "Can't delete this item")}
            subtitle={t(
              'cannotDeleteStockItemWithStock',
              '{{displayName}} still has {{quantityOnHand}} unit(s) in stock. Issue or dispose of the remaining stock first, then delete the item.',
              { displayName, quantityOnHand },
            )}
          />
        ) : (
          <>
            <p>
              {t(
                'deleteStockItemConfirmationText',
                `Are you sure you want to delete {{displayName}}? This action can't be undone.`,
                { displayName },
              )}
            </p>
            <TextArea
              id="delete-stock-item-reason"
              labelText={t('reasonLabel', 'Please explain the reason:')}
              onChange={(e) => setReason(e.target.value)}
              maxLength={500}
              placeholder={t('reasonPlaceholder', 'Enter reason here')}
            />
          </>
        )}
      </ModalBody>
      <ModalFooter>
        <Button size="lg" kind="secondary" onClick={handleCancel}>
          {hasStockOnHand ? t('close', 'Close') : getCoreTranslation('cancel')}
        </Button>
        {!hasStockOnHand && (
          <Button autoFocus kind="danger" onClick={handleDelete} size="lg" disabled={reason.trim().length === 0}>
            {t('delete', 'Delete')}
          </Button>
        )}
      </ModalFooter>
    </>
  );
};

export default DeleteStockItemModal;
