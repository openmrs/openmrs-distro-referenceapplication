import { camelCase, escapeRegExp, find } from 'lodash-es';
import {
  getConfig,
  messageOmrsServiceWorker,
  openmrsFetch,
  restBaseUrl,
  type FetchResponse,
  type Session,
} from '@openmrs/esm-framework';
import type {
  AddressTemplate,
  ConceptResponse,
  FetchedPatientIdentifierType,
  IdentifierSourceAutoGenerationOption,
  PatientIdentifierType,
  PersonAttributeTypeResponse,
  RelationshipTypesResponse,
} from './patient-registration/patient-registration.types';
import { cacheForOfflineHeaders, moduleName } from './constants';
import type { FieldDefinition } from './config-schema';

export interface Resources {
  addressTemplate: AddressTemplate;
  currentSession: Session;
  relationshipTypes: RelationshipTypesResponse;
  identifierTypes: Array<PatientIdentifierType>;
}

export async function fetchCurrentSession(): Promise<Session> {
  const { data } = await cacheAndFetch<Session>(`${restBaseUrl}/session`);
  return data;
}

export async function fetchAddressTemplate() {
  const { data } = await cacheAndFetch<AddressTemplate>(`${restBaseUrl}/addresstemplate`);
  return data;
}

export async function fetchAllRelationshipTypes(): Promise<RelationshipTypesResponse> {
  const { data } = await cacheAndFetch<RelationshipTypesResponse>(`${restBaseUrl}/relationshiptype?v=default`);
  return data;
}

export async function fetchAllFieldDefinitionTypes() {
  const config = await getConfig(moduleName);

  if (!config.fieldDefinitions) {
    return;
  }

  const fieldDefinitionPromises = config.fieldDefinitions.map((def) => fetchFieldDefinitionType(def));

  const fieldDefinitionResults = await Promise.all(fieldDefinitionPromises);

  const mergedData = fieldDefinitionResults.reduce((merged, result) => {
    if (result) {
      merged.push(result);
    }
    return merged;
  }, []);

  return mergedData;
}

async function fetchFieldDefinitionType(
  fieldDefinition: FieldDefinition,
): Promise<PersonAttributeTypeResponse | undefined> {
  let apiUrl = '';

  if (fieldDefinition.type === 'person attribute') {
    apiUrl = `${restBaseUrl}/personattributetype/${fieldDefinition.uuid}`;
  }

  // Pre-fetch the concept for offline caching if answerConceptSetUuid is provided.
  // This ensures the concept answers are available offline even though we don't use the result here.
  // The actual concept data is fetched later via useConceptAnswers hook when rendering the field.
  if (fieldDefinition.answerConceptSetUuid) {
    await cacheAndFetch<ConceptResponse>(`${restBaseUrl}/concept/${fieldDefinition.answerConceptSetUuid}`);
  }

  if (!apiUrl) {
    return undefined;
  }

  const { data } = await cacheAndFetch<PersonAttributeTypeResponse>(apiUrl);
  return data;
}

function isValidFetchedIdentifierType(
  type: FetchedPatientIdentifierType | null | undefined,
): type is FetchedPatientIdentifierType {
  return type !== null && type !== undefined;
}

export async function fetchPatientIdentifierTypesWithSources(): Promise<Array<PatientIdentifierType>> {
  const patientIdentifierTypes = await fetchPatientIdentifierTypes();

  const validIdentifierTypes = patientIdentifierTypes.filter(isValidFetchedIdentifierType);
  // Convert FetchedPatientIdentifierType to PatientIdentifierType
  const identifierTypes: Array<PatientIdentifierType> = validIdentifierTypes.map((type) => ({
    ...type,
    identifierSources: [],
  }));

  const [autoGenOptions, identifierSourcesResponse] = await Promise.all([
    fetchAutoGenerationOptions(),
    fetchIdentifierSources(),
  ]);

  const allIdentifierSources = identifierSourcesResponse.data?.results || [];

  for (let i = 0; i < identifierTypes.length; i++) {
    identifierTypes[i].identifierSources = allIdentifierSources
      .filter((source) => source.identifierType.uuid === identifierTypes[i].uuid)
      .map((source) => {
        const option = find(autoGenOptions.data?.results || [], { source: { uuid: source.uuid } });
        if (option && 'manualEntryEnabled' in option && 'automaticGenerationEnabled' in option) {
          source.autoGenerationOption = option as IdentifierSourceAutoGenerationOption;
        }
        return source;
      });
  }

  return identifierTypes;
}

interface ApiPatientIdentifierType {
  display: string;
  uuid: string;
  name: string;
  format: string;
  formatDescription?: string;
  required: boolean;
  uniquenessBehavior: undefined | null | 'UNIQUE' | 'NON_UNIQUE' | 'LOCATION';
}

async function fetchPatientIdentifierTypes(): Promise<Array<FetchedPatientIdentifierType | null>> {
  const [patientIdentifierTypesResponse, primaryIdentifierTypeResponse] = await Promise.all([
    cacheAndFetch<{ results: Array<ApiPatientIdentifierType> }>(
      `${restBaseUrl}/patientidentifiertype?v=custom:(display,uuid,name,format,formatDescription,required,uniquenessBehavior)`,
    ),
    cacheAndFetch<{ results: Array<{ metadataUuid: string }> }>(
      `${restBaseUrl}/metadatamapping/termmapping?v=full&code=emr.primaryIdentifierType`,
    ),
  ]);

  if (patientIdentifierTypesResponse.ok) {
    // Primary identifier type is to be kept at the top of the list.
    const patientIdentifierTypes = patientIdentifierTypesResponse?.data?.results || [];

    const primaryIdentifierTypeUuid = primaryIdentifierTypeResponse?.data?.results?.[0]?.metadataUuid;

    const identifierTypes: Array<FetchedPatientIdentifierType | null> = [];

    if (primaryIdentifierTypeResponse?.ok && primaryIdentifierTypeUuid) {
      const primaryType = patientIdentifierTypes.find((type) => type.uuid === primaryIdentifierTypeUuid);
      if (primaryType) {
        identifierTypes.push(mapPatientIdentifierType(primaryType, true));
      }
    }

    patientIdentifierTypes.forEach((type) => {
      if (type.uuid !== primaryIdentifierTypeUuid) {
        identifierTypes.push(mapPatientIdentifierType(type, false));
      }
    });
    return identifierTypes;
  }

  return [];
}

interface IdentifierSourceResponse {
  results: Array<{
    uuid: string;
    name: string;
    identifierType: { uuid: string };
    autoGenerationOption?: IdentifierSourceAutoGenerationOption;
  }>;
}

async function fetchIdentifierSources() {
  return await cacheAndFetch<IdentifierSourceResponse>(`${restBaseUrl}/idgen/identifiersource?v=default`);
}

interface AutoGenerationOptionResponse {
  results: Array<IdentifierSourceAutoGenerationOption & { source: { uuid: string } }>;
}

async function fetchAutoGenerationOptions(abortController?: AbortController) {
  return await cacheAndFetch<AutoGenerationOptionResponse>(`${restBaseUrl}/idgen/autogenerationoption?v=full`);
}

async function cacheAndFetch<T = unknown>(url?: string): Promise<FetchResponse<T>> {
  if (!url) {
    throw new Error('URL is required for cacheAndFetch');
  }

  const abortController = new AbortController();

  await messageOmrsServiceWorker({
    type: 'registerDynamicRoute',
    pattern: escapeRegExp(url),
  });

  return await openmrsFetch<T>(url, { headers: cacheForOfflineHeaders, signal: abortController?.signal });
}

function mapPatientIdentifierType(
  patientIdentifierType: ApiPatientIdentifierType,
  isPrimary: boolean,
): FetchedPatientIdentifierType {
  return {
    name: patientIdentifierType.display,
    fieldName: camelCase(patientIdentifierType.name),
    required: patientIdentifierType.required,
    uuid: patientIdentifierType.uuid,
    format: patientIdentifierType.format,
    formatDescription: patientIdentifierType.formatDescription,
    isPrimary,
    uniquenessBehavior: patientIdentifierType.uniquenessBehavior,
  };
}
