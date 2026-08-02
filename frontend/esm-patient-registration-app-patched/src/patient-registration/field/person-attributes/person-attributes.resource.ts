import { type FetchResponse, openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import useSWRImmutable from 'swr/immutable';
import { type PersonAttributeTypeResponse } from '../../patient-registration.types';

export function usePersonAttributeType(personAttributeTypeUuid: string): {
  data: PersonAttributeTypeResponse;
  isLoading: boolean;
  error: Error | undefined;
} {
  const { data, error, isLoading } = useSWRImmutable<FetchResponse<PersonAttributeTypeResponse>>(
    `${restBaseUrl}/personattributetype/${personAttributeTypeUuid}`,
    openmrsFetch,
  );

  return {
    data: data?.data,
    isLoading,
    error,
  };
}
