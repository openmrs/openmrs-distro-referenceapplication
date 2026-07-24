import { type FetchResponse, openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import useSWR from 'swr';
import { type StockOperationLinkDTO } from '../../../core/api/types/stockOperation/StockOperationLinkDTO';

const useStockOperationLinks = (stockOperationUuid?: string) => {
  const apiUrl = `${restBaseUrl}/stockmanagement/stockoperationlink?v=default&q=${stockOperationUuid}`;
  const { data, error, isLoading, mutate } = useSWR<
    FetchResponse<{
      results: Array<StockOperationLinkDTO>;
    }>
  >(stockOperationUuid ? apiUrl : null, openmrsFetch);
  return {
    error,
    isLoading,
    mutate,
    operationLinks: data?.data?.results ?? [],
  };
};

export default useStockOperationLinks;
