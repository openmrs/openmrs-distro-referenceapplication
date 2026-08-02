import { vi } from 'vitest';
import { type ConceptResponse } from '../../patient-registration.types';

export const useConcept = vi.fn(function mockUseConceptImplementation(uuid: string): {
  data: ConceptResponse;
  isLoading: boolean;
} {
  let data;
  if (uuid === 'weight-uuid') {
    data = {
      uuid: 'weight-uuid',
      display: 'Weight (kg)',
      datatype: { display: 'Numeric', uuid: 'num' },
      answers: [],
      setMembers: [],
    };
  } else if (uuid === 'chief-complaint-uuid') {
    data = {
      uuid: 'chief-complaint-uuid',
      display: 'Chief Complaint',
      datatype: { display: 'Text', uuid: 'txt' },
      answers: [],
      setMembers: [],
    };
  } else if (uuid === 'nationality-uuid') {
    data = {
      uuid: 'nationality-uuid',
      display: 'Nationality',
      datatype: { display: 'Coded', uuid: 'cdd' },
      answers: [
        { display: 'USA', uuid: 'usa' },
        { display: 'Mexico', uuid: 'mex' },
      ],
      setMembers: [],
    };
  }
  return {
    data: data ?? null,
    isLoading: !data,
  };
});

export const useConceptAnswers = vi.fn((uuid: string) => {
  if (uuid === 'nationality-uuid') {
    return {
      data: [
        { display: 'USA', uuid: 'usa' },
        { display: 'Mexico', uuid: 'mex' },
      ],
      isLoading: false,
    };
  } else if (uuid === 'other-countries-uuid') {
    return {
      data: [
        { display: 'Kenya', uuid: 'ke' },
        { display: 'Uganda', uuid: 'ug' },
      ],
      isLoading: false,
    };
  }
});
