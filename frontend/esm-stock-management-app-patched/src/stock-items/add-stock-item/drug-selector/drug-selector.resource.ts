import { ResourceRepresentation } from '../../../core/api/api';
import { type DrugFilterCriteria, useDrugs } from '../../../stock-lookups/stock-lookups.resource';

export function useDrugsHook(searchTerm?: string, filter?: DrugFilterCriteria, enabled = true) {
  const defaultFilters: DrugFilterCriteria = {
    v: ResourceRepresentation.Default,
    q: searchTerm,
    // High enough to return the full drug dictionary in one page rather than a truncated
    // first-20 slice - relies on webservices.rest.maxResultsAbsolute being raised to match.
    limit: 500,
  };
  const drugsFilter: DrugFilterCriteria = filter || defaultFilters;

  const {
    items: { results: drugList },
    isLoading,
  } = useDrugs(drugsFilter, enabled);

  return {
    drugList,
    isLoading,
  };
}
