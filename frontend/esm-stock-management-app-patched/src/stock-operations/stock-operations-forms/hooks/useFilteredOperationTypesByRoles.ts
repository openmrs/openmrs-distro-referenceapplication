import { useMemo } from 'react';
import { useStockOperationTypes, useUserRoles } from '../../../stock-lookups/stock-lookups.resource';

const useFilteredOperationTypesByRoles = () => {
  const {
    types: { results },
    isLoading: isStockOperationTypesLoading,
    error: stockOperationTypesError,
  } = useStockOperationTypes();

  const { userRoles, isLoading: isUserRolesLoading, error: userRolesError } = useUserRoles();

  const operationTypes = useMemo(() => {
    const applicablePrivilegeScopes = userRoles?.operationTypes?.map((p) => p.operationTypeUuid) || [];
    const uniqueApplicablePrivilegeScopes = [...new Set(applicablePrivilegeScopes)];

    return results?.filter((p) => uniqueApplicablePrivilegeScopes.includes(p.uuid)) || [];
  }, [results, userRoles]);

  const isLoading = isStockOperationTypesLoading || isUserRolesLoading;
  const error = stockOperationTypesError || userRolesError;

  return {
    operationTypes,
    isLoading,
    error,
  };
};

export default useFilteredOperationTypesByRoles;
