import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { IconButton, InlineLoading } from '@carbon/react';
import { TrashCan } from '@carbon/react/icons';
import { restBaseUrl, showModal, showSnackbar } from '@openmrs/esm-framework';
import { deleteStockItems } from './stock-items.resource';
import { handleMutate } from '../utils';

interface DeleteStockItemActionButtonProps {
  uuid: string | null | undefined;
  displayName?: string | null;
  /** Current quantity on hand across all locations; deletion is blocked while this is > 0. */
  quantityOnHand?: number;
}

const DeleteStockItemActionButton: React.FC<DeleteStockItemActionButtonProps> = ({
  uuid,
  displayName,
  quantityOnHand,
}) => {
  const { t } = useTranslation();
  const [isDeleting, setIsDeleting] = useState(false);

  const handleDeleteStockItem = React.useCallback(() => {
    if (!uuid) {
      return;
    }
    const close = showModal('delete-stock-item-modal', {
      close: () => close(),
      displayName,
      quantityOnHand,
      onConfirmation: (reason: string) => {
        setIsDeleting(true);
        deleteStockItems([uuid], reason).then(
          () => {
            setIsDeleting(false);
            handleMutate(`${restBaseUrl}/stockmanagement/stockitem`);
            handleMutate(`${restBaseUrl}/stockmanagement/stockiteminventory`);
            showSnackbar({
              isLowContrast: true,
              title: t('deleteStockItem', 'Delete stock item'),
              kind: 'success',
              subtitle: t('stockItemDeletedSuccessfully', 'Stock item deleted successfully'),
            });
          },
          (error) => {
            setIsDeleting(false);
            showSnackbar({
              title: t('errorDeletingStockItem', 'Error deleting stock item'),
              kind: 'error',
              isLowContrast: true,
              subtitle: error?.message,
            });
          },
        );
        close();
      },
    });
  }, [t, uuid, displayName, quantityOnHand]);

  if (isDeleting) {
    return <InlineLoading />;
  }

  return (
    <IconButton kind="ghost" label={t('deleteStockItem', 'Delete stock item')} onClick={handleDeleteStockItem}>
      <TrashCan size={16} />
    </IconButton>
  );
};

export default DeleteStockItemActionButton;
