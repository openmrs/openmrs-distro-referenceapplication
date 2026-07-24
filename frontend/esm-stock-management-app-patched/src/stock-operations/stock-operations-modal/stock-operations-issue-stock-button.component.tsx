import React, { useEffect, useMemo } from 'react';

import { Button, InlineLoading } from '@carbon/react';
import { DeliveryTruck } from '@carbon/react/icons';
import { useTranslation } from 'react-i18next';
import { type StockOperationDTO } from '../../core/api/types/stockOperation/StockOperationDTO';
import { OperationType } from '../../core/api/types/stockOperation/StockOperationType';
import { useStockOperationTypes } from '../../stock-lookups/stock-lookups.resource';
import { launchStockoperationAddOrEditWorkSpace } from '../stock-operation.utils';
import { showSnackbar } from '@openmrs/esm-framework';

interface StockOperationIssueStockButtonProps {
  operation: StockOperationDTO;
}

const StockOperationIssueStockButton: React.FC<StockOperationIssueStockButtonProps> = ({ operation }) => {
  const { t } = useTranslation();
  const { error, isLoading, types } = useStockOperationTypes();
  const stockIssueOperationType = useMemo(
    () => types?.results?.find((type) => type.operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE),
    [types],
  );

  const handleButtonClick = () => {
    launchStockoperationAddOrEditWorkSpace(t, stockIssueOperationType, undefined, operation.uuid);
  };

  useEffect(() => {
    if (error) {
      showSnackbar({
        kind: 'error',
        title: t('stockOperationTypesError', 'Error loading stock operation types'),
        subtitle: error?.message,
      });
    }
  }, [error, t]);

  if (isLoading)
    return <InlineLoading description="" iconDescription={t('loadingOperationTypes', 'Loading operation types')} />;

  return (
    <Button onClick={handleButtonClick} kind="tertiary" renderIcon={(props) => <DeliveryTruck size={16} {...props} />}>
      {t('issueStock', 'Issue Stock ')}
    </Button>
  );
};

export default StockOperationIssueStockButton;
