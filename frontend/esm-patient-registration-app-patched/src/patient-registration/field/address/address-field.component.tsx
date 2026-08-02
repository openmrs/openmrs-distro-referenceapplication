import React, { useEffect, useState, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { SkeletonText, InlineNotification } from '@carbon/react';
import { type Session, useConfig, useConnectivity } from '@openmrs/esm-framework';
import { useOrderedAddressHierarchyLevels } from './address-hierarchy.resource';
import {
  type PatientRegistrationContextProps,
  PatientRegistrationContextProvider,
  usePatientRegistrationContext,
} from '../../patient-registration-context';
import { ResourcesContextProvider, useResourcesContext } from '../../../resources-context';
import { type AddressTemplate } from '../../patient-registration.types';
import { Input } from '../../input/basic-input/input/input.component';
import { SelectInput } from '../../input/basic-input/select/select-input.component';
import AddressHierarchyLevels from './address-hierarchy-levels.component';
import AddressSearchComponent from './address-search.component';
import styles from '../field.scss';

export const AddressComponent: React.FC = () => {
  const config = useConfig();
  const { t } = useTranslation();
  const { setFieldValue } = usePatientRegistrationContext();
  const { orderedFields, isLoadingFieldOrder, errorFetchingFieldOrder } = useOrderedAddressHierarchyLevels();

  const isOnline = useConnectivity();
  const {
    fieldConfigurations: {
      address: {
        useAddressHierarchy: { enabled: addressHierarchyEnabled, useQuickSearch, searchAddressByLevel },
        selectFields,
      },
    },
  } = config;

  const selectFieldOptionsByCodeName = useMemo(
    () => Object.fromEntries((selectFields ?? []).map(({ id, options }) => [id, options])),
    [selectFields],
  );

  const { addressTemplate } = useResourcesContext();
  const addressLayout = useMemo(() => {
    if (!addressTemplate?.lines) {
      return [];
    }

    const allFields = addressTemplate?.lines?.flat();
    const fields = allFields?.filter(({ isToken }) => isToken === 'IS_ADDR_TOKEN');
    const allRequiredFields = Object.fromEntries(addressTemplate?.requiredElements?.map((curr) => [curr, curr]) || []);
    return fields.map(({ displayText, codeName }) => {
      return {
        id: codeName,
        name: codeName,
        label: displayText,
        required: Boolean(allRequiredFields[codeName]),
      };
    });
  }, [addressTemplate]);

  const [selected, setSelected] = useState('');

  useEffect(() => {
    if (addressTemplate?.elementDefaults) {
      Object.entries(addressTemplate.elementDefaults).forEach(([name, defaultValue]) => {
        setFieldValue(`address.${name}`, defaultValue);
      });
    }
  }, [addressTemplate, setFieldValue]);

  const orderedAddressFields = useMemo(() => {
    if (isLoadingFieldOrder || errorFetchingFieldOrder) {
      return [];
    }

    const orderMap = Object.fromEntries(orderedFields.map((field, indx) => [field, indx]));

    return [...addressLayout].sort(
      (existingField1, existingField2) => orderMap[existingField1.name] - orderMap[existingField2.name],
    );
  }, [isLoadingFieldOrder, errorFetchingFieldOrder, orderedFields, addressLayout]);

  const renderAddressField = (attributes: (typeof addressLayout)[number], index: number) => {
    const options = selectFieldOptionsByCodeName[attributes.name];
    if (options) {
      return (
        <SelectInput
          key={`combo_input_${index}`}
          name={`address.${attributes.name}`}
          label={t(attributes.label)}
          options={options}
          required={attributes.required}
        />
      );
    }

    return (
      <Input
        key={`combo_input_${index}`}
        name={`address.${attributes.name}`}
        labelText={t(attributes.label)}
        id={attributes.name}
        value={selected}
        required={attributes.required}
      />
    );
  };

  if (addressTemplate && !Object.keys(addressTemplate)?.length) {
    return (
      <AddressComponentContainer>
        <div role="progressbar" aria-label={t('loading', 'Loading')}>
          <SkeletonText />
        </div>
      </AddressComponentContainer>
    );
  }

  if (!addressHierarchyEnabled || !isOnline) {
    return <AddressComponentContainer>{addressLayout.map(renderAddressField)}</AddressComponentContainer>;
  }

  if (isLoadingFieldOrder) {
    return (
      <AddressComponentContainer>
        <div role="progressbar" aria-label={t('loading', 'Loading')}>
          <SkeletonText />
        </div>
      </AddressComponentContainer>
    );
  }

  if (errorFetchingFieldOrder) {
    return (
      <AddressComponentContainer>
        <InlineNotification
          style={{ margin: '0', minWidth: '100%' }}
          kind="error"
          lowContrast={true}
          title={t('errorFetchingOrderedFields', 'Error occurred fetching ordered fields for address hierarchy')}
        />
      </AddressComponentContainer>
    );
  }

  return (
    <AddressComponentContainer>
      {useQuickSearch && <AddressSearchComponent addressLayout={orderedAddressFields} />}
      {searchAddressByLevel ? (
        <AddressHierarchyLevels orderedAddressFields={orderedAddressFields} />
      ) : (
        orderedAddressFields.map(renderAddressField)
      )}
    </AddressComponentContainer>
  );
};

const AddressComponentContainer = ({ children }) => {
  const { t } = useTranslation();
  const contextValue = useMemo(
    () =>
      ({
        fieldConfigurations: {},
        setFieldValue: async () => {},
        values: {},
      }) as unknown as PatientRegistrationContextProps,
    [],
  );

  return (
    <ResourcesContextProvider
      value={{
        addressTemplate: {} as AddressTemplate,
        currentSession: {} as Session,
        identifierTypes: [],
        relationshipTypes: { results: [] },
      }}>
      <PatientRegistrationContextProvider value={contextValue}>
        <div>
          <h4 className={styles.productiveHeading02Light}>{t('addressHeader', 'Address')}</h4>
          <div
            style={{
              paddingBottom: '5%',
            }}>
            {children}
          </div>
        </div>
      </PatientRegistrationContextProvider>
    </ResourcesContextProvider>
  );
};
