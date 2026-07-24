import React, { useEffect, useState } from 'react';
import {
  type ConceptFilterCriteria,
  type UserFilterCriteria,
  useUsers,
} from '../../../stock-lookups/stock-lookups.resource';
import { ResourceRepresentation } from '../../../core/api/api';

const useSearchUser = (filter?: ConceptFilterCriteria) => {
  const [conceptFilter, setConceptFilter] = useState<UserFilterCriteria>(
    filter || {
      v: ResourceRepresentation.Default,
      limit: 10,
      startIndex: 0,
    },
  );

  const {
    items: { results: userList },
    isLoading,
  } = useUsers(conceptFilter);

  const [searchString, setSearchString] = useState(null);

  // Drug filter type
  const [limit, setLimit] = useState(filter?.limit || 10);
  const [representation, setRepresentation] = useState<string>(filter?.v || ResourceRepresentation.Default);

  useEffect(() => {
    setConceptFilter({
      startIndex: 0,
      v: representation as ResourceRepresentation,
      limit: limit,
      q: searchString,
    });
  }, [searchString, limit, representation]);

  return {
    userList,
    setLimit,
    setRepresentation,
    setSearchString,
    isLoading,
  };
};

export default useSearchUser;
