import React, { useCallback } from 'react';

import { Button } from '@carbon/react';
import { showModal } from '@openmrs/esm-framework';
import { useTranslation } from 'react-i18next';
import { Departure } from '@carbon/react/icons';
import { type StockOperationDTO } from '../../core/api/types/stockOperation/StockOperationDTO';

interface StockOperationApproveDispatchButtonProps {
  operation: StockOperationDTO;
}

const StockOperationApproveDispatchButton: React.FC<StockOperationApproveDispatchButtonProps> = ({ operation }) => {
  const { t } = useTranslation();
  const launchApproveDispatchModal = useCallback(() => {
    const dispose = showModal('stock-operations-modal', {
      title: 'Dispatch',
      operation: operation,
      requireReason: false,
      closeModal: () => dispose(),
    });
  }, [operation]);

  return (
    <Button onClick={launchApproveDispatchModal} renderIcon={(props) => <Departure size={16} {...props} />}>
      {t('dispatch', 'Dispatch')}
    </Button>
  );
};

export default StockOperationApproveDispatchButton;
