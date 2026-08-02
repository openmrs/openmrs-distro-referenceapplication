import { Type, validator, validators } from '@openmrs/esm-framework';
import _default from 'yup/lib/locale';

export interface SectionDefinition {
  id: string;
  name?: string;
  fields: Array<string>;
}

export interface FieldDefinition {
  id: string;
  type: string;
  label?: string;
  uuid: string;
  placeholder?: string;
  allowFutureDates?: boolean;
  allowPastDates?: boolean;
  showHeading: boolean;
  validation?: {
    required: boolean;
    matches?: string;
  };
  locationTag?: string;
  answerConceptSetUuid?: string;
  customConceptAnswers?: Array<CustomConceptAnswer>;
}

export interface CustomConceptAnswer {
  uuid: string;
  label?: string;
}

export interface Gender {
  label?: string;
  value: string;
}

export interface RegistrationConfig {
  sections: Array<string>;
  sectionDefinitions: Array<SectionDefinition>;
  fieldDefinitions: Array<FieldDefinition>;
  fieldConfigurations: {
    causeOfDeath: {
      conceptUuid: string;
      required?: boolean;
    };
    name: {
      displayMiddleName: boolean;
      allowUnidentifiedPatients: boolean;
      defaultUnknownGivenName: string;
      defaultUnknownFamilyName: string;
      displayCapturePhoto: boolean;
      displayReverseFieldOrder: boolean;
    };
    gender: Array<Gender>;
    address: {
      useAddressHierarchy: {
        enabled: boolean;
        useQuickSearch: boolean;
        searchAddressByLevel: boolean;
      };
      selectFields: Array<{ id: string; options: Array<string> }>;
      fieldValidation: Array<{ id: string; validateLuhn?: boolean }>;
    };
    dateOfBirth: {
      allowEstimatedDateOfBirth: boolean;
      useEstimatedDateOfBirth: {
        enabled: boolean;
        dayOfMonth: number;
        month: number;
      };
    };
    phone: {
      personAttributeUuid: string;
      validation?: {
        required: boolean;
        matches?: string;
      };
    };
  };
  links: {
    submitButton: string;
  };
  defaultPatientIdentifierTypes: Array<string>;
  registrationObs: {
    encounterTypeUuid: string | null;
    encounterProviderRoleUuid: string;
    registrationFormUuid: string | null;
  };
  freeTextFieldConceptUuid: string;
}

export const builtInSections: Array<SectionDefinition> = [
  {
    id: 'demographics',
    name: 'Basic Info',
    fields: ['name', 'gender', 'dob', 'id'],
  },
  { id: 'contact', name: 'Contact Details', fields: ['address', 'phone'] },
  { id: 'death', name: 'Death Info', fields: ['dateAndTimeOfDeath', 'causeOfDeath'] },
  { id: 'relationships', name: 'Relationships', fields: [] },
];

// These fields are handled specially in field.component.tsx
export const builtInFields = [
  'name',
  'gender',
  'dob',
  'id',
  'address',
  'phone',
  'causeOfDeath',
  'dateAndTimeOfDeath',
] as const;

export const esmPatientRegistrationSchema = {
  sections: {
    _type: Type.Array,
    _default: ['demographics', 'contact', 'relationships'],
    _description: `An array of strings which are the keys from 'sectionDefinitions' or any of the following built-in sections: '${builtInSections
      .map((s) => s.id)
      .join("', '")}'.`,
    _elements: {
      _type: Type.String,
    },
  },
  sectionDefinitions: {
    _type: Type.Array,
    _default: [],
    _elements: {
      id: {
        _type: Type.String,
        _description: `How this section will be referred to in the \`sections\` configuration. To override a built-in section, use that section's id. The built in section ids are '${builtInSections
          .map((s) => s.id)
          .join("', '")}'.`,
      },
      name: {
        _type: Type.String,
        _description: 'The title to display at the top of the section.',
      },
      fields: {
        _type: Type.Array,
        _default: [],
        _description: `The parts to include in the section. Can be any of the following built-in fields: ${builtInFields.join(
          ', ',
        )}. Can also be an id from an object in the \`fieldDefinitions\` array, which you can use to define custom fields.`,
        _elements: {
          _type: Type.String,
        },
      },
    },
  },
  fieldDefinitions: {
    _type: Type.Array,
    _default: [],
    _description:
      'Definitions for custom fields that can be used in sectionDefinitions. Can also be used to override built-in fields.',
    _elements: {
      id: {
        _type: Type.String,
        _description:
          'How this field will be referred to in the `fields` element of the `sectionDefinitions` configuration.',
      },
      type: {
        _type: Type.String,
        _description: "How this field's data will be stored—a person attribute or an obs.",
        _validators: [validators.oneOf(['person attribute', 'obs'])],
      },
      uuid: {
        _type: Type.UUID,
        _description: "Person attribute type UUID that this field's data should be saved to.",
      },
      showHeading: {
        _type: Type.Boolean,
        _default: false,
        _description: 'Whether to show a heading above the person attribute field.',
      },
      label: {
        _type: Type.String,
        _default: '',
        _description: 'The label of the input. By default, uses the metadata `display` attribute.',
      },
      placeholder: {
        _type: Type.String,
        _default: '',
        _description: 'Placeholder that will appear in the input.',
      },
      allowFutureDates: {
        _type: Type.Boolean,
        _default: true,
        _description: 'Indicates whether the date input field should allow the selection of future dates or not.',
      },
      allowPastDates: {
        _type: Type.Boolean,
        _default: true,
        _description: 'Indicates whether the date input field should allow the selection of past dates or not.',
      },
      validation: {
        required: {
          _type: Type.Boolean,
          _default: false,
        },
        matches: {
          _type: Type.String,
          _default: '',
          _description: 'Optional RegEx for testing the validity of the input.',
        },
      },
      locationTag: {
        _type: Type.String,
        _default: '',
        _description:
          'Only for fields with "person attribute" type `org.openmrs.Location`. This filters the list of location options in the dropdown based on their location tag. By default, all locations are shown.',
      },
      answerConceptSetUuid: {
        _type: Type.ConceptUuid,
        _default: '',
        _description:
          'For coded questions only. A concept which has the possible responses either as answers or as set members.',
      },
      customConceptAnswers: {
        _type: Type.Array,
        _default: [],
        _description:
          'For coded questions only (obs or person attrbute). A list of custom concept answers. Overrides answers that come from the obs concept or from `answerSetConceptUuid`.',
        _elements: {
          uuid: {
            _type: Type.UUID,
            _description: 'Answer concept UUID',
          },
          label: {
            _type: Type.String,
            _default: '',
            _description: 'The custom label for the answer concept.',
          },
        },
      },
    },
    // Do not add fields here. If you want to add a field in code, add it to built-in fields above.
  },
  fieldConfigurations: {
    causeOfDeath: {
      conceptUuid: {
        _type: Type.ConceptUuid,
        _default: '9272a14b-7260-4353-9e5b-5787b5dead9d',
        _description: 'The concept UUID to get cause of death answers',
      },
      required: {
        _type: Type.Boolean,
        _default: false,
      },
    },
    name: {
      displayMiddleName: {
        _type: Type.Boolean,
        _default: true,
      },
      allowUnidentifiedPatients: {
        _type: Type.Boolean,
        _default: true,
        _description: 'Whether to allow registering unidentified patients.',
      },
      defaultUnknownGivenName: {
        _type: Type.String,
        _default: 'UNKNOWN',
        _description: 'The given/first name to record for unidentified patients.',
      },
      defaultUnknownFamilyName: {
        _type: Type.String,
        _default: 'UNKNOWN',
        _description: 'The family/last name to record for unidentified patients.',
      },
      displayCapturePhoto: {
        _type: Type.Boolean,
        _default: true,
        _description: 'Whether to display capture patient photo slot on name field',
      },
      displayReverseFieldOrder: {
        _type: Type.Boolean,
        _default: false,
        _description: "Whether to display the name fields in the order 'Family name' -> 'Middle name' -> 'First name'",
      },
    },
    gender: {
      _type: Type.Array,
      _default: [
        {
          value: 'male',
        },
        {
          value: 'female',
        },
        {
          value: 'other',
        },
        {
          value: 'unknown',
        },
      ],
      _description:
        'The options for sex selection during patient registration. This is Administrative Gender as it is called by FHIR (Possible options are limited to those defined in FHIR Administrative Gender, see https://hl7.org/fhir/R4/valueset-administrative-gender.html).',
      _elements: {
        value: {
          _type: Type.String,
          _description:
            'Value that will be sent to the server. Limited to FHIR-supported values for Administrative Gender',
          _validators: [validators.oneOf(['male', 'female', 'other', 'unknown'])],
        },
        label: {
          _type: Type.String,
          _default: '',
          _description:
            'The label displayed for the sex option, if it should be different from the value (the value will be translated; the English "translation" is upper-case).',
        },
      },
    },
    address: {
      useAddressHierarchy: {
        enabled: {
          _type: Type.Boolean,
          _default: true,
          _description: 'Whether to use the Address hierarchy in the registration form or not',
        },
        useQuickSearch: {
          _type: Type.Boolean,
          _default: true,
          _description:
            'Whether to use the quick searching through the address saved in the database pre-fill the form.',
        },
        searchAddressByLevel: {
          _type: Type.Boolean,
          _default: false,
          _description:
            "Whether to fill the addresses by levels, i.e. County => subCounty, the current field is dependent on it's previous field.",
        },
      },
      selectFields: {
        _type: Type.Array,
        _default: [],
        _description:
          'Address fields (matched by address template codeName, e.g. stateProvince) that should render as a fixed dropdown instead of free text.',
        _elements: {
          id: {
            _type: Type.String,
            _description: 'The address template codeName (e.g. stateProvince, cityVillage) this applies to.',
          },
          options: {
            _type: Type.Array,
            _default: [],
            _elements: { _type: Type.String },
          },
        },
      },
      fieldValidation: {
        _type: Type.Array,
        _default: [],
        _description:
          "Address fields (matched by address template codeName) opted into having the address template's elementRegex/elementRegexFormats enforced client-side, optionally with an added Luhn checksum.",
        _elements: {
          id: {
            _type: Type.String,
            _description: 'The address template codeName (e.g. address3) this applies to.',
          },
          validateLuhn: {
            _type: Type.Boolean,
            _default: false,
            _description: 'Whether to additionally require the value to pass a Luhn checksum.',
          },
        },
      },
    },
    dateOfBirth: {
      allowEstimatedDateOfBirth: {
        _type: Type.Boolean,
        _default: true,
        _description: 'Whether to allow estimated date of birth for a patient during registration',
      },
      useEstimatedDateOfBirth: {
        enabled: {
          _type: Type.Boolean,
          _default: false,
          _description: 'Whether to use a fixed day and month for estimated date of birth',
        },
        dayOfMonth: {
          _type: Type.Number,
          _default: 0,
          _description:
            'The custom day of the month use on the estimated date of birth  (0 = last day of previous month, 1-31 = specific day)',
          _validators: [validators.inRange(0, 31)],
        },
        month: {
          _type: Type.Number,
          _default: 0,
          _description: 'The custom month to use on the estimated date of birth i.e 0 = Jan & 11 = Dec',
          _validators: [validators.inRange(0, 11)],
        },
      },
    },
    phone: {
      personAttributeUuid: {
        _type: Type.UUID,
        _default: '14d4f066-15f5-102d-96e4-000c29c2a5d7',
        _description: 'The UUID of the phone number person attribute type',
      },
      validation: {
        required: {
          _type: Type.Boolean,
          _default: false,
        },
        matches: {
          _type: Type.String,
          _default: '',
          _description: 'Optional RegEx for testing the validity of the input.',
        },
      },
    },
  },
  links: {
    submitButton: {
      _type: Type.String,
      _default: '${openmrsSpaBase}/patient/${patientUuid}/chart',
      _validators: [validators.isUrlWithTemplateParameters(['patientUuid'])],
    },
  },
  defaultPatientIdentifierTypes: {
    _type: Type.Array,
    _default: [],
    _elements: {
      _type: Type.PatientIdentifierTypeUuid,
    },
  },
  registrationObs: {
    encounterTypeUuid: {
      _type: Type.UUID,
      _default: '',
      _description:
        'Obs created during registration will be associated with an encounter of this type. This must be set in order to use fields of type `obs`.',
    },
    encounterProviderRoleUuid: {
      _type: Type.UUID,
      _default: 'a0b03050-c99b-11e0-9572-0800200c9a66',
      _description: "The provider role to use for the registration encounter. Default is 'Unkown'.",
    },
    registrationFormUuid: {
      _type: Type.UUID,
      _default: '',
      _description:
        'The form UUID to associate with the registration encounter. By default no form will be associated.',
    },
  },
  freeTextFieldConceptUuid: {
    _type: Type.ConceptUuid,
    _default: '5622AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA',
  },
  _validators: [
    validator(
      (config: RegistrationConfig) =>
        !config.fieldDefinitions.some((d) => d.type === 'obs') || config.registrationObs.encounterTypeUuid !== null,
      "If fieldDefinitions contains any fields of type 'obs', `registrationObs.encounterTypeUuid` must be specified.",
    ),
    validator(
      (config: RegistrationConfig) =>
        config.sections.every((s) =>
          [...builtInSections, ...config.sectionDefinitions].map((sDef) => sDef.id).includes(s),
        ),
      (config: RegistrationConfig) => {
        const allowedSections = [...builtInSections, ...config.sectionDefinitions].map((sDef) => sDef.id);
        const badSection = config.sections.find((s) => !allowedSections.includes(s));
        return (
          `'${badSection}' is not a valid section ID. Valid section IDs include the built-in sections ${stringifyDefinitions(
            builtInSections,
          )}` +
          (config.sectionDefinitions.length
            ? `; and the defined sections ${stringifyDefinitions(config.sectionDefinitions)}.`
            : '.')
        );
      },
    ),
    validator(
      (config: RegistrationConfig) =>
        config.sectionDefinitions.every((sectionDefinition) =>
          sectionDefinition.fields.every((f) =>
            [...builtInFields, ...config.fieldDefinitions.map((fDef) => fDef.id)].includes(f),
          ),
        ),
      (config: RegistrationConfig) => {
        const allowedFields = [...builtInFields, ...config.fieldDefinitions.map((fDef) => fDef.id)];
        const badSection = config.sectionDefinitions.find((sectionDefinition) =>
          sectionDefinition.fields.some((f) => !allowedFields.includes(f)),
        );
        const badField = badSection.fields.find((f) => !allowedFields.includes(f));
        return (
          `The section definition '${
            badSection.id
          }' contains an invalid field '${badField}'. 'fields' can only contain the built-in fields '${builtInFields.join(
            "', '",
          )}'` +
          (config.fieldDefinitions.length
            ? `; or the defined fields ${stringifyDefinitions(config.fieldDefinitions)}.`
            : '.')
        );
      },
    ),
  ],
};

function stringifyDefinitions(sectionDefinitions: Array<SectionDefinition | FieldDefinition>) {
  return `'${sectionDefinitions.map((s) => s.id).join("', '")}'`;
}
