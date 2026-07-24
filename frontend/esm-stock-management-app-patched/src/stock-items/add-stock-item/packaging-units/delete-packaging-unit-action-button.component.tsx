import React, { useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { TrashCan } from '@carbon/react/icons';
import { showModal } from '@openmrs/esm-framework';
import { type StockItemPackagingUOMDTO } from '../../../core/api/types/stockItem/StockItemPackagingUOM';

interface DeletePackagingUnitActionButtonProps {
  row?: StockItemPackagingUOMDTO;
}

const DeletePackagingUnitActionButton: React.FC<DeletePackagingUnitActionButtonProps> = ({ row }) => {
  const { t } = useTranslation();
  const launchDeleteModal = useCallback(() => {
    const dispose = showModal('delete-packaging-unit-modal', {
      closeModal: () => dispose(),
      row,
    });
  }, [row]);

  return (
    <Button
      type="button"
      size="sm"
      className="submitButton clear-padding-margin"
      iconDescription={t('delete', 'Delete')}
      kind="ghost"
      renderIcon={TrashCan}
      onClick={launchDeleteModal}
    />
  );
};

export default DeletePackagingUnitActionButton;
