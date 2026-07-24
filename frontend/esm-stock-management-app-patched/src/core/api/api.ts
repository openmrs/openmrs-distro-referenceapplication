import { type PagingCriteria } from './types/PageableResult';

export enum ResourceRepresentation {
  Default = 'default',
  Full = 'full',
  REF = 'ref',
}

export interface ResourceFilterCriteria extends PagingCriteria {
  v?: ResourceRepresentation | null;
  q?: string | null;
  totalCount?: boolean | null;
  limit?: number | null;
}

export function toQueryParams<T extends ResourceFilterCriteria>(
  filterCriteria?: T | null,
  skipEmptyString = true,
): string {
  if (!filterCriteria) return '';
  const queryParams: string = Object.keys(filterCriteria)
    ?.map((key) => {
      const value = filterCriteria[key];
      return (skipEmptyString && (value === false || value === true ? true : value)) ||
        (!skipEmptyString && (value === '' || (value === false || value === true ? true : value)))
        ? `${encodeURIComponent(key)}=${encodeURIComponent(value.toString())}`
        : null;
    })
    .filter((o) => o != null)
    .join('&');
  return queryParams.length > 0 ? '?' + queryParams : '';
}
