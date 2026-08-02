import React, { useState, useRef, useEffect, useMemo } from 'react';
import { useFormikContext } from 'formik';
import { useTranslation } from 'react-i18next';
import { Search } from '@carbon/react';
import { useAddressHierarchy } from './address-hierarchy.resource';
import styles from './address-search.scss';

interface AddressSearchComponentProps {
  addressLayout: Array<any>;
}

const AddressSearchComponent: React.FC<AddressSearchComponentProps> = ({ addressLayout }) => {
  const { t } = useTranslation();
  const separator = ' > ';
  const searchBox = useRef(null);
  const wrapper = useRef(null);
  const [searchString, setSearchString] = useState('');

  const { addresses, isLoading, error } = useAddressHierarchy(searchString, separator);

  const addressOptions: Array<string> = useMemo(() => {
    const options: Set<string> = new Set();
    addresses.forEach((address) => {
      const values = address.split(separator);
      values.forEach((val, index) => {
        if (val.toLowerCase().includes(searchString.toLowerCase())) {
          options.add(values.slice(0, index + 1).join(separator));
        }
      });
    });
    return [...options];
  }, [addresses, searchString]);

  const { setFieldValue } = useFormikContext();

  const handleInputChange = (e) => {
    setSearchString(e.target.value);
  };

  const handleChange = (address) => {
    if (address) {
      const values = address.split(separator);
      addressLayout.map(({ name }, index) => {
        setFieldValue(`address.${name}`, values?.[index] ?? '');
      });
      setSearchString('');
    }
  };

  const handleClickOutsideComponent = (e) => {
    if (wrapper.current && !wrapper.current.contains(e.target)) {
      setSearchString('');
    }
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutsideComponent);

    return () => {
      document.removeEventListener('mousedown', handleClickOutsideComponent);
    };
  }, [wrapper]);

  return (
    <div className={styles.autocomplete} ref={wrapper} style={{ marginBottom: '1rem' }}>
      <Search
        onChange={handleInputChange}
        onKeyDown={(e) => {
          if (e.key === 'Enter') {
            e.preventDefault();
          }
        }}
        labelText={t('searchAddress', 'Search address')}
        placeholder={t('searchAddress', 'Search address')}
        ref={searchBox}
        value={searchString}
      />
      {searchString && (
        <ul className={styles.suggestions}>
          {isLoading ? (
            <li className={styles.loading}>{t('searching', 'Searching...')}</li>
          ) : error ? (
            <li className={styles.noResults}>{t('errorFetchingAddresses', 'Error fetching address results')}</li>
          ) : addressOptions.length > 0 ? (
            addressOptions.map((address) => (
              <li key={address} onClick={() => handleChange(address)}>
                {address}
              </li>
            ))
          ) : (
            <li className={styles.noResults}>{t('noAddressResults', 'No matching addresses found')}</li>
          )}
        </ul>
      )}
    </div>
  );
};

export default AddressSearchComponent;
