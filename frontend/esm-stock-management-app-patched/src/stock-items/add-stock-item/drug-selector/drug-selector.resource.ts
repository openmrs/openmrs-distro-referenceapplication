import { ResourceRepresentation } from '../../../core/api/api';
import { type DrugFilterCriteria, useDrugs } from '../../../stock-lookups/stock-lookups.resource';

export function useDrugsHook(searchTerm?: string, filter?: DrugFilterCriteria, enabled = true) {
  const defaultFilters: DrugFilterCriteria = {
    v: ResourceRepresentation.Default,
    q: searchTerm,
    limit: 20,
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
