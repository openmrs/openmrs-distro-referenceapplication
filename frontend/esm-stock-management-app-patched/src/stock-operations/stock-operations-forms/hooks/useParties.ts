import { type FetchResponse, openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import React, { useCallback, useMemo } from 'react';
import useSWR from 'swr';
import { type Party } from '../../../core/api/types/Party';
import { LocationTypeLocation, LocationTypeOther } from '../../../core/api/types/stockOperation/LocationType';
import { type StockOperationType } from '../../../core/api/types/stockOperation/StockOperationType';

const useParties = (stockOperationType: StockOperationType) => {
  const apiUrl = `${restBaseUrl}/stockmanagement/party?v=default`;
  const { data, isLoading, mutate, error } = useSWR<FetchResponse<{ results: Array<Party> }>>(apiUrl, openmrsFetch);
  const sourceTags = useMemo(() => {
    return (
      stockOperationType?.stockOperationTypeLocationScopes
        ?.filter((p) => stockOperationType?.hasSource && p.isSource)
        .map((p) => p.locationTag) ?? []
    );
  }, [stockOperationType]);

  const destinationTags = useMemo(() => {
    return (
      stockOperationType?.stockOperationTypeLocationScopes
        ?.filter((p) => stockOperationType?.hasDestination && p.isDestination)
        .map((p) => p.locationTag) ?? []
    );
  }, [stockOperationType]);

  const sourcePartiesFilter = useCallback(
    (p: Party) => {
      return (
        (p.locationUuid &&
          stockOperationType?.sourceType === LocationTypeLocation &&
          (sourceTags.length === 0 || (p.tags && sourceTags.some((x) => p.tags.includes(x))))) ||
        (p.stockSourceUuid && stockOperationType?.sourceType === LocationTypeOther)
      );
    },
    [stockOperationType, sourceTags],
  );

  const destinationPartiesFilter = useCallback(
    (p: Party) => {
      return (
        (p.locationUuid &&
          stockOperationType?.destinationType === LocationTypeLocation &&
          (destinationTags.length === 0 || (p.tags && destinationTags.some((x) => p.tags.includes(x))))) ||
        (p.stockSourceUuid && stockOperationType?.destinationType === LocationTypeOther)
      );
    },
    [stockOperationType, destinationTags],
  );

  const sourceParties = useMemo(() => {
    return data?.data?.results?.filter(sourcePartiesFilter) ?? [];
  }, [data, sourcePartiesFilter]);

  const destinationParties = useMemo(() => {
    return data?.data?.results?.filter(destinationPartiesFilter) ?? [];
  }, [data, destinationPartiesFilter]);

  return {
    parties: data?.data?.results ?? [],
    isLoading,
    mutate,
    sourceParties,
    destinationParties,
    sourcePartiesFilter,
    destinationPartiesFilter,
    error,
    sourceTags,
    destinationTags,
  };
};

export default useParties;
