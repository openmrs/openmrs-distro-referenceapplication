export interface PageableResult<ResultType> {
  results: ResultType[];
  links: ResultLink[] | null;
  totalCount: number | null;
}

export interface ResultLink {
  rel: string;
  uri: string;
}

export interface PagingCriteria {
  startIndex?: number | null;
  limit?: number | null;
}
