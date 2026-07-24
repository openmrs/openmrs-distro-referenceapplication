import React, { useCallback } from 'react';

import { Button } from '@carbon/react';
import { showModal } from '@openmrs/esm-framework';
import { useTranslation } from 'react-i18next';
import { CheckmarkOutline } from '@carbon/react/icons';
import { type StockOperationDTO } from '../../core/api/types/stockOperation/StockOperationDTO';

interface StockOperationApprovalButtonProps {
  operation: StockOperationDTO;
}

const StockOperationApprovalButton: React.FC<StockOperationApprovalButtonProps> = ({ operation }) => {
  const { t } = useTranslation();
  const launchApprovalModal = useCallback(() => {
    const dispose = showModal('stock-operations-modal', {
      title: 'Approve',
      operation: operation,
      requireReason: false,
      closeModal: () => dispose(),
    });
  }, [operation]);

  return (
    <Button onClick={launchApprovalModal} renderIcon={(props) => <CheckmarkOutline size={16} {...props} />}>
      {t('approve', 'Approve')}
    </Button>
  );
};

export default StockOperationApprovalButton;
