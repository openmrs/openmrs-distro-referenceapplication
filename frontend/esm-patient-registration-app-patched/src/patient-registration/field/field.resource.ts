import { useMemo } from 'react';
import useSWRImmutable from 'swr/immutable';
import { type FetchResponse, openmrsFetch, restBaseUrl, showSnackbar } from '@openmrs/esm-framework';
import { type ConceptAnswers, type ConceptResponse } from '../patient-registration.types';

export function useConcept(conceptUuid: string): { data: ConceptResponse; isLoading: boolean } {
  const shouldFetch = typeof conceptUuid === 'string' && conceptUuid !== '';
  const { data, error, isLoading } = useSWRImmutable<FetchResponse<ConceptResponse>, Error>(
    shouldFetch ? `${restBaseUrl}/concept/${conceptUuid}` : null,
    openmrsFetch,
  );
  if (error) {
    showSnackbar({
      title: error.name,
      subtitle: error.message,
      kind: 'error',
    });
  }
  const results = useMemo(() => ({ data: data?.data, isLoading }), [data, isLoading]);
  return results;
}

export function useConceptAnswers(conceptUuid: string): {
  data: Array<ConceptAnswers>;
  isLoading: boolean;
  error: Error;
} {
  const shouldFetch = typeof conceptUuid === 'string' && conceptUuid !== '';
  const { data, error, isLoading } = useSWRImmutable<FetchResponse<ConceptResponse>, Error>(
    shouldFetch ? `${restBaseUrl}/concept/${conceptUuid}?v=custom:(uuid,display,answers:(uuid,display))` : null,
    openmrsFetch,
  );
  if (error) {
    showSnackbar({
      title: error.name,
      subtitle: error.message,
      kind: 'error',
    });
  }
  const results = useMemo(() => ({ data: data?.data?.answers, isLoading, error }), [isLoading, error, data]);
  return results;
}
