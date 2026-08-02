import { useMemo } from 'react';
import useSWR from 'swr';
import { type FetchResponse, fhirBaseUrl, openmrsFetch, useDebounce } from '@openmrs/esm-framework';
import { type LocationEntry, type LocationResponse } from '@openmrs/esm-service-queues-app/src/types';

interface UseLocationsResult {
  locations: Array<LocationEntry>;
  isLoading: boolean;
  loadingNewData: boolean;
}

export function useLocations(locationTag: string | null, searchQuery: string = ''): UseLocationsResult {
  const debouncedSearchQuery = useDebounce(searchQuery);

  const constructUrl = useMemo(() => {
    let url = `${fhirBaseUrl}/Location?`;
    let urlSearchParameters = new URLSearchParams();
    urlSearchParameters.append('_summary', 'data');

    if (!debouncedSearchQuery) {
      urlSearchParameters.append('_count', '10');
    }

    if (locationTag) {
      urlSearchParameters.append('_tag', locationTag);
    }

    if (typeof debouncedSearchQuery === 'string' && debouncedSearchQuery !== '') {
      urlSearchParameters.append('name:contains', debouncedSearchQuery);
    }

    return url + urlSearchParameters.toString();
  }, [locationTag, debouncedSearchQuery]);

  const { data, isLoading, isValidating } = useSWR<FetchResponse<LocationResponse>, Error>(constructUrl, openmrsFetch);

  return useMemo(
    () => ({
      locations: data?.data?.entry || [],
      isLoading,
      loadingNewData: isValidating,
    }),
    [data, isLoading, isValidating],
  );
}
