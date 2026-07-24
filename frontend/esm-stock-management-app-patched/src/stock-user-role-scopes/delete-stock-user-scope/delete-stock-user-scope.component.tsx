import React, { useState } from 'react';
import { IconButton, InlineLoading } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { TrashCan } from '@carbon/react/icons';
import { restBaseUrl, showModal, showSnackbar } from '@openmrs/esm-framework';
import { deleteUserRoleScopes } from '../stock-user-role-scopes.resource';
import { handleMutate } from '../../utils';

interface StockUserScopDeleteActionMenuProps {
  uuid: string;
}

const StockUserScopeDeleteActionMenu: React.FC<StockUserScopDeleteActionMenuProps> = ({ uuid }) => {
  const { t } = useTranslation();

  const [deletingUserScope, setDeletingUserScope] = useState(false);

  const handleDeleteStockUserScope = React.useCallback(() => {
    const close = showModal('delete-stock-user-scope-modal', {
      close: () => close(),
      uuid: uuid,
      onConfirmation: () => {
        const ids = [];
        ids.push(uuid);
        deleteUserRoleScopes(ids)
          .then(
            () => {
              handleMutate(`${restBaseUrl}/stockmanagement/userrolescope`);
              setDeletingUserScope(false);
              showSnackbar({
                isLowContrast: true,
                title: t('deletingStockUserScope', 'Delete Stock User Scope'),
                kind: 'success',
                subtitle: t('stockUserScopeDeletedSuccessfully', 'Stock User Scope Deleted Successfully'),
              });
            },
            (error) => {
              setDeletingUserScope(false);
              showSnackbar({
                title: t('errorDeletingUserScope', 'Error deleting a user scope'),
                kind: 'error',
                isLowContrast: true,
                subtitle: error?.message,
              });
            },
          )
          .catch();
        close();
      },
    });
  }, [t, uuid]);

  const deleteButton = (
    <IconButton
      autoAlign
      kind="ghost"
      label={t('deleteUserRoleScope', 'Delete user role scope')}
      onClick={handleDeleteStockUserScope}
    >
      <TrashCan size={16} />
    </IconButton>
  );

  return deletingUserScope ? <InlineLoading /> : deleteButton;
};

export default StockUserScopeDeleteActionMenu;
