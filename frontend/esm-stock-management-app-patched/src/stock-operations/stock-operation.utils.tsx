import { launchWorkspace, showModal } from '@openmrs/esm-framework';
import { type TFunction } from 'i18next';
import { useLocation } from 'react-router-dom';
import { type StockOperationDTO } from '../core/api/types/stockOperation/StockOperationDTO';
import { type StockOperationType } from '../core/api/types/stockOperation/StockOperationType';

export const launchStockoperationAddOrEditWorkSpace = (
  t: TFunction,
  operationType: StockOperationType,
  stockOperation?: StockOperationDTO,
  stockRequisitionUuid?: string, // Only supplied on stock issue (when workspace is launched for stock issue)
) => {
  launchWorkspace('stock-operation-form-workspace', {
    workspaceTitle: stockOperation
      ? t('editOperationTitle', 'Edit {{operationType}}', {
          operationType: stockOperation?.operationTypeName,
        })
      : t('newOperationTitle', 'New: {{operationName}}', {
          operationName: operationType?.name,
        }),
    stockOperationType: operationType,
    stockOperation: stockOperation,
    stockRequisitionUuid: stockRequisitionUuid,
  });
};

export const useUrlQueryParams = () => {
  return new URLSearchParams(useLocation().search);
};

export function getStockOperationUniqueId() {
  return `${new Date().getTime()}-${Math.random().toString(36).substring(2, 16)}`;
}

export const launchStockOperationsModal = (title: string, requireReason: boolean, operation: StockOperationDTO) => {
  const dispose = showModal('stock-operations-modal', {
    title: title,
    operation: operation,
    requireReason: requireReason,
    closeModal: () => dispose(),
  });
};
