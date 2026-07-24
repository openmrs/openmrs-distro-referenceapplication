import React, { useCallback } from 'react';
import { useTranslation } from 'react-i18next';
import { Button } from '@carbon/react';
import { showModal } from '@openmrs/esm-framework';

const AddStockItemsBulktImportActionButton: React.FC = () => {
  const { t } = useTranslation();

  const handleLaunchImportBulkStockItemsModal = useCallback(() => {
    const dispose = showModal('import-bulk-stock-items', {
      closeModal: () => dispose(),
    });
  }, []);

  return (
    <Button iconDescription={t('import', 'Import')} kind="ghost" onClick={handleLaunchImportBulkStockItemsModal}>
      {t('import', 'Import')}
    </Button>
  );
};

export default AddStockItemsBulktImportActionButton;
