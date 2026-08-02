import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import classNames from 'classnames';
import { useTranslation } from 'react-i18next';
import { TextInput, Layer } from '@carbon/react';
import SelectionTick from './selection-tick.component';
import styles from '../input.scss';

type TextInputProps = React.ComponentProps<typeof TextInput>;

interface ComboInputProps {
  entries: Array<string>;
  error?: Error;
  isLoading?: boolean;
  name: string;
  fieldProps: {
    value: string;
    labelText: string;
    id?: string;
  } & Omit<TextInputProps, 'value' | 'labelText' | 'id' | 'onChange' | 'onFocus' | 'autoComplete' | 'onKeyDown'>;
  handleInputChange: (newValue: string) => void;
  handleSelection: (newSelection: string) => void;
}

const ComboInput: React.FC<ComboInputProps> = ({
  entries,
  error,
  isLoading,
  name,
  fieldProps,
  handleInputChange,
  handleSelection,
}) => {
  const { t } = useTranslation();
  const [highlightedEntry, setHighlightedEntry] = useState(-1);
  const { value = '' } = fieldProps;
  const [showEntries, setShowEntries] = useState(false);
  const comboInputRef = useRef(null);

  const handleFocus = useCallback(() => {
    setShowEntries(true);
    setHighlightedEntry(-1);
  }, [setShowEntries, setHighlightedEntry]);

  const filteredEntries = useMemo(() => {
    if (!entries) {
      return [];
    }
    if (!value) {
      return entries;
    }
    return entries.filter((entry) => entry.toLowerCase().includes(value.toLowerCase()));
  }, [entries, value]);

  const handleOptionClick = useCallback(
    (newSelection: string, e: React.KeyboardEvent<HTMLInputElement> | null = null) => {
      e?.preventDefault();
      handleSelection(newSelection);
      setShowEntries(false);
    },
    [handleSelection, setShowEntries],
  );

  const handleKeyPress = useCallback(
    (e: React.KeyboardEvent<HTMLInputElement>) => {
      const totalResults = filteredEntries.length ?? 0;

      if (e.key === 'Tab') {
        setShowEntries(false);
        setHighlightedEntry(-1);
      }

      if (e.key === 'ArrowUp') {
        setHighlightedEntry((prev) => Math.max(-1, prev - 1));
      } else if (e.key === 'ArrowDown') {
        setHighlightedEntry((prev) => Math.min(totalResults - 1, prev + 1));
      } else if (e.key === 'Enter') {
        e.preventDefault();
        if (highlightedEntry > -1) {
          handleOptionClick(filteredEntries[highlightedEntry]);
        }
      }
    },
    [highlightedEntry, handleOptionClick, filteredEntries, setHighlightedEntry, setShowEntries],
  );

  useEffect(() => {
    const listener = (e: MouseEvent) => {
      if (!comboInputRef.current.contains(e.target as Node)) {
        setShowEntries(false);
        setHighlightedEntry(-1);
      }
    };
    window.addEventListener('click', listener);
    return () => {
      window.removeEventListener('click', listener);
    };
  }, []);

  return (
    <div className={styles.comboInput} ref={comboInputRef}>
      <Layer>
        <TextInput
          {...fieldProps}
          id={fieldProps.id || name}
          onChange={(e) => {
            setHighlightedEntry(-1);
            handleInputChange(e.target.value);
          }}
          onFocus={handleFocus}
          autoComplete={'off'}
          onKeyDown={handleKeyPress}
        />
      </Layer>
      <div className={styles.comboInputEntries}>
        {showEntries && (
          <div className="cds--combo-box cds--list-box cds--list-box--expanded">
            <div id="downshift-1-menu" className="cds--list-box__menu" role="listbox">
              {isLoading ? (
                <div className="cds--list-box__menu-item">
                  <div className={classNames('cds--list-box__menu-item__option', styles.comboInputItemOption)}>
                    {t('searching', 'Searching...')}
                  </div>
                </div>
              ) : error ? (
                <div className="cds--list-box__menu-item">
                  <div className={classNames('cds--list-box__menu-item__option', styles.comboInputItemOption)}>
                    {t('errorFetchingResults', 'Error fetching results')}
                  </div>
                </div>
              ) : filteredEntries.length > 0 ? (
                filteredEntries.map((entry, indx) => (
                  <div
                    className={classNames('cds--list-box__menu-item', {
                      'cds--list-box__menu-item--highlighted': indx === highlightedEntry,
                    })}
                    key={indx}
                    id="downshift-1-item-0"
                    role="option"
                    tabIndex={-1}
                    aria-selected="true"
                    onClick={() => handleOptionClick(entry)}>
                    <div
                      className={classNames('cds--list-box__menu-item__option', styles.comboInputItemOption, {
                        'cds--list-box__menu-item--active': entry === value,
                      })}>
                      {entry}
                      {entry === value && <SelectionTick />}
                    </div>
                  </div>
                ))
              ) : value ? (
                <div className="cds--list-box__menu-item">
                  <div className={classNames('cds--list-box__menu-item__option', styles.comboInputItemOption)}>
                    {t('noMatchingResults', 'No matching results')}
                  </div>
                </div>
              ) : null}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default ComboInput;
