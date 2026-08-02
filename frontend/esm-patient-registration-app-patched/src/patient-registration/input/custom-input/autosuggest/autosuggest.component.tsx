import React, { useEffect, useRef, useState } from 'react';
import { Layer, Search } from '@carbon/react';
import classNames from 'classnames';
import styles from './autosuggest.scss';

type SearchProps = React.ComponentProps<typeof Search>;

interface AutosuggestProps<Suggestion = unknown> extends SearchProps {
  getDisplayValue: (suggestion: Suggestion) => string;
  getFieldValue: (suggestion: Suggestion) => string;
  getSearchResults: (query: string) => Promise<Array<Suggestion>>;
  onSuggestionSelected: (field: string, value: string) => void;
  invalid?: boolean | undefined;
  invalidText?: string | undefined;
}

export const Autosuggest = <Suggestion = unknown,>({
  getDisplayValue,
  getFieldValue,
  getSearchResults,
  onSuggestionSelected,
  invalid,
  invalidText,
  ...searchProps
}: AutosuggestProps<Suggestion>) => {
  const [suggestions, setSuggestions] = useState<Array<Suggestion>>([]);
  const searchBox = useRef(null);
  const wrapper = useRef(null);
  const { id: name, labelText } = searchProps;

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutsideComponent);

    return () => {
      document.removeEventListener('mousedown', handleClickOutsideComponent);
    };
  }, [wrapper]);

  const handleClickOutsideComponent = (e: MouseEvent) => {
    if (wrapper.current && !wrapper.current.contains(e.target as Node)) {
      setSuggestions([]);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const query = e.target.value;
    onSuggestionSelected(name, undefined);

    if (query) {
      getSearchResults(query).then((suggestions) => {
        setSuggestions(suggestions);
      });
    } else {
      setSuggestions([]);
    }
  };

  const handleClear = () => {
    onSuggestionSelected(name, undefined);
  };

  const handleClick = (index: number) => {
    const display = getDisplayValue(suggestions[index]);
    const value = getFieldValue(suggestions[index]);
    searchBox.current.value = display;
    onSuggestionSelected(name, value);
    setSuggestions([]);
  };

  return (
    <div className={styles.autocomplete} ref={wrapper}>
      <label className="cds--label">{labelText}</label>
      <Layer className={classNames({ [styles.invalid]: invalid })}>
        <Search
          id="autosuggest"
          onChange={handleChange}
          onClear={handleClear}
          ref={searchBox}
          className={styles.autocompleteSearch}
          labelText={labelText}
          {...searchProps}
        />
      </Layer>
      {suggestions.length > 0 && (
        <ul className={styles.suggestions}>
          {suggestions.map((suggestion, index) => (
            <li key={index} onClick={(e) => handleClick(index)}>
              {getDisplayValue(suggestion)}
            </li>
          ))}
        </ul>
      )}
      {invalid ? <label className={classNames(styles.invalidMsg)}>{invalidText}</label> : <></>}
    </div>
  );
};
