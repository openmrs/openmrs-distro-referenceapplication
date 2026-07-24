import React, { useCallback, useEffect } from 'react';
import { ButtonSkeleton, OverflowMenu, OverflowMenuItem } from '@carbon/react';
import { OverflowMenuVertical } from '@carbon/react/icons';
import { useTranslation } from 'react-i18next';
import { showSnackbar } from '@openmrs/esm-framework';
import { OperationType, type StockOperationType } from '../../core/api/types/stockOperation/StockOperationType';
import { launchStockoperationAddOrEditWorkSpace } from '../stock-operation.utils';
import useFilteredOperationTypesByRoles from '../stock-operations-forms/hooks/useFilteredOperationTypesByRoles';

const StockOperationTypesSelector = () => {
  const { t } = useTranslation();
  const { error, isLoading, operationTypes } = useFilteredOperationTypesByRoles();

  const handleSelect = useCallback(
    (stockOperationType: StockOperationType) => {
      const isStockIssueOperation = stockOperationType.operationType === OperationType.STOCK_ISSUE_OPERATION_TYPE;

      launchStockoperationAddOrEditWorkSpace(t, stockOperationType, undefined);
    },
    [t],
  );

  useEffect(() => {
    if (error) {
      showSnackbar({
        kind: 'error',
        title: t('stockOperationTypesError', 'Error loading stock operation types'),
        subtitle: error?.message,
      });
    }
  }, [error, t]);

  if (isLoading) return <ButtonSkeleton />;

  if (error) return null;

  return operationTypes && operationTypes.length ? (
    <OverflowMenu
      renderIcon={() => (
        <>
          Start New&nbsp;&nbsp;
          <OverflowMenuVertical size={16} />
        </>
      )}
      menuOffset={{ top: 0, left: -100 }}
      style={{
        backgroundColor: '#007d79',
        backgroundImage: 'none',
        color: '#fff',
        minHeight: '1rem',
        padding: '.95rem !important',
        width: '8rem',
        marginRight: '0.5rem',
        whiteSpace: 'nowrap',
      }}
    >
      {operationTypes
        .sort((a, b) => a.name.localeCompare(b.name))
        .map((operation) => (
          <OverflowMenuItem
            key={operation.uuid}
            itemText={operation.name}
            onClick={() => {
              handleSelect(operation);
            }}
          />
        ))}
    </OverflowMenu>
  ) : null;
};

export default StockOperationTypesSelector;
