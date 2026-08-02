import { type OpenmrsResource, type Session } from '@openmrs/esm-framework';
import { type RegistrationConfig } from '../config-schema';
import { type SavePatientTransactionManager } from './form-manager';

interface NameValue {
  uuid: string;
  preferred: boolean;
  givenName: string;
  middleName: string;
  familyName: string;
}

export interface AttributeValue {
  attributeType: string;
  value: string;
}

/**
 * Patient Identifier data as it is fetched and composed from the APIs.
 */
export interface FetchedPatientIdentifierType {
  name: string;
  required: boolean;
  uuid: string;
  fieldName: string;
  format: string;
  formatDescription?: string;
  isPrimary: boolean;
  /** See: https://github.com/openmrs/openmrs-core/blob/e3fb1ac0a052aeff0f957a150731757dd319693b/api/src/main/java/org/openmrs/PatientIdentifierType.java#L41 */
  uniquenessBehavior: undefined | null | 'UNIQUE' | 'NON_UNIQUE' | 'LOCATION';
}

export interface PatientIdentifierValue {
  identifierUuid?: string;
  identifierTypeUuid: string;
  initialValue: string;
  identifierValue: string;
  identifierName: string;
  selectedSource: IdentifierSource;
  autoGeneration?: boolean;
  preferred: boolean;
  required: boolean;
}

/**
 * Extends the `FetchedPatientIdentifierType` with aggregated data.
 */
export interface PatientIdentifierType extends FetchedPatientIdentifierType {
  identifierSources: Array<IdentifierSource>;
  autoGenerationSource?: IdentifierSource;
  checked?: boolean;
  source?: IdentifierSource;
}

export interface IdentifierSource {
  uuid: string;
  name: string;
  autoGenerationOption?: IdentifierSourceAutoGenerationOption;
}

export interface IdentifierSourceAutoGenerationOption {
  manualEntryEnabled: boolean;
  automaticGenerationEnabled: boolean;
}

export interface PatientIdentifier {
  uuid?: string;
  identifier: string;
  identifierType?: string;
  location?: string;
  preferred?: boolean;
}

export interface PatientRegistration {
  id?: number;
  /**
   * The preliminary patient in the FHIR format.
   */
  fhirPatient: fhir.Patient;
  /**
   * Internal data collected by patient-registration. Required for later syncing and editing.
   * Not supposed to be used outside of this module.
   */
  _patientRegistrationData: {
    isNewPatient: boolean;
    formValues: FormValues;
    patientUuidMap: PatientUuidMapType;
    initialAddressFieldValues: Partial<Record<AddressProperties, string>>;
    capturePhotoProps: CapturePhotoProps;
    currentLocation: string;
    initialIdentifierValues: FormValues['identifiers'];
    currentUser: Session;
    config: RegistrationConfig;
    savePatientTransactionManager: SavePatientTransactionManager;
  };
}

export type Relationship = {
  relationshipType: string;
  personA: string;
  personB: string;
};

export type Patient = {
  uuid: string;
  identifiers: Array<PatientIdentifier>;
  person: {
    uuid: string;
    names: Array<NameValue>;
    gender: string;
    birthdate: string;
    birthdateEstimated: boolean;
    attributes: Array<AttributeValue>;
    addresses: Array<Record<string, string>>;
    dead: boolean;
    deathDate?: string;
    causeOfDeath?: string;
  };
};

export interface Encounter {
  encounterDatetime: Date;
  patient: string;
  encounterType: string;
  location: string;
  encounterProviders: Array<{
    provider: string;
    encounterRole: string;
  }>;
  form: string;
  obs: Array<{
    concept: string | OpenmrsResource;
    value: string | number | OpenmrsResource;
  }>;
}

export interface RelationshipValue {
  relatedPersonName?: string;
  relatedPersonUuid: string;
  relation?: string;
  relationshipType: string;
  /**
   * Defines the action to be taken on the existing relationship
   * @kind ADD -> adds a new relationship
   * @kind UPDATE -> updates an existing relationship
   * @kind DELETE -> deletes an existing relationship
   * @kind undefined -> no operation on the existing relationship
   */
  action?: 'ADD' | 'UPDATE' | 'DELETE';
  /**
   * Value kept for restoring initial relationshipType value
   */
  initialrelationshipTypeValue?: string;
  uuid?: string;
}

export interface FormValues {
  additionalFamilyName: string;
  additionalGivenName: string;
  additionalMiddleName: string;
  addNameInLocalLanguage: boolean;
  address: {
    [addressField: string]: string;
  };
  attributes?: {
    [attributeTypeUuid: string]: string;
  };
  birthdate: Date | string;
  birthdateEstimated: boolean;
  deathCause: string;
  deathDate: string | Date;
  deathTime: string;
  deathTimeFormat: 'AM' | 'PM';
  familyName: string;
  gender: string;
  givenName: string;
  identifiers: {
    [identifierFieldName: string]: PatientIdentifierValue;
  };
  isDead: boolean;
  middleName: string;
  monthsEstimated: number;
  nonCodedCauseOfDeath: string;
  obs?: {
    [conceptUuid: string]: string;
  };
  patientUuid: string;
  relationships: Array<RelationshipValue>;
  telephoneNumber: string;
  yearsEstimated: number;
}

export interface PatientUuidMapType {
  additionalNameUuid?: string;
  preferredNameUuid?: string;
  preferredAddressUuid?: string;
}

export interface CapturePhotoProps {
  imageData: string;
  dateTime: string;
}

export interface PatientIdentifierResponse {
  uuid: string;
  identifier: string;
  preferred: boolean;
  identifierType: {
    uuid: string;
    required: boolean;
    name: string;
  };
}
export interface PersonAttributeTypeResponse {
  uuid: string;
  display: string;
  name: string;
  description: string;
  format: string;
}

export interface PersonAttributeResponse {
  display: string;
  uuid: string;
  value:
    | string
    | {
        uuid: string;
        display: string;
      };
  attributeType: {
    display: string;
    uuid: string;
    format: 'org.openmrs.Concept' | string;
  };
}

export interface ConceptAnswers {
  display: string;
  uuid: string;
}

export interface ConceptResponse {
  uuid: string;
  display: string;
  datatype: {
    uuid: string;
    display: string;
  };
  answers: Array<ConceptAnswers>;
  setMembers: Array<ConceptAnswers>;
}

export interface RelationshipType {
  uuid: string;
  display: string;
  aIsToB: string;
  bIsToA: string;
  displayAIsToB?: string;
  displayBIsToA?: string;
  description?: string;
}

export interface RelationshipTypesResponse {
  results: Array<RelationshipType>;
}

export type AddressProperties =
  | 'cityVillage'
  | 'stateProvince'
  | 'countyDistrict'
  | 'postalCode'
  | 'country'
  | 'address1'
  | 'address2'
  | 'address3'
  | 'address4'
  | 'address5'
  | 'address6'
  | 'address7'
  | 'address8'
  | 'address9'
  | 'address10'
  | 'address11'
  | 'address12'
  | 'address13'
  | 'address14'
  | 'address15';

export type ExtensibleAddressProperties = { [p in AddressProperties]?: string } | null;

export interface AddressTemplate {
  displayName: string | null;
  codeName: string | null;
  country: string | null;
  lines: Array<
    Array<{
      isToken: 'IS_NOT_ADDR_TOKEN' | 'IS_ADDR_TOKEN';
      displayText: string;
      codeName?: AddressProperties;
      displaySize?: string;
    }>
  > | null;
  lineByLineFormat: Array<string> | null;
  nameMappings: ExtensibleAddressProperties;
  sizeMappings: ExtensibleAddressProperties;
  elementDefaults: ExtensibleAddressProperties;
  elementRegex: ExtensibleAddressProperties;
  elementRegexFormats: ExtensibleAddressProperties;
  requiredElements: Array<AddressProperties> | null;
}
