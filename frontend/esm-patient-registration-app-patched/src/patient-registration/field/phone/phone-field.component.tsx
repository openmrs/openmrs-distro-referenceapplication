import React from 'react';
import { PersonAttributeField } from '../person-attributes/person-attribute-field.component';
import { useConfig } from '@openmrs/esm-framework';
import { type RegistrationConfig } from '../../../config-schema';

export function PhoneField() {
  const config = useConfig<RegistrationConfig>();

  const fieldDefinition = {
    id: 'phone',
    type: 'person attribute',
    uuid: config.fieldConfigurations.phone.personAttributeUuid,
    validation: config.fieldConfigurations.phone.validation,
    showHeading: false,
  };
  return <PersonAttributeField fieldDefinition={fieldDefinition} />;
}
