import camelCase from 'lodash-es/camelCase';
import { parseDate } from '@openmrs/esm-framework';
import {
  type Encounter,
  type FormValues,
  type PatientIdentifierValue,
  type PatientUuidMapType,
} from './patient-registration.types';

export function scrollIntoView(viewId: string) {
  document.getElementById(viewId)?.scrollIntoView({
    behavior: 'smooth',
    block: 'start',
    inline: 'center',
  });
}

export function cancelRegistration() {
  window.history.back();
}

export function getFormValuesFromFhirPatient(patient: fhir.Patient) {
  const result = {} as FormValues;
  const patientName = patient.name[0];
  const additionalPatientName = patient.name[1];

  result.patientUuid = patient.id;
  result.givenName = patientName?.given[0];
  result.middleName = patientName?.given[1];
  result.familyName = patientName?.family;
  result.addNameInLocalLanguage = !!additionalPatientName ? true : undefined;
  result.additionalGivenName = additionalPatientName?.given[0];
  result.additionalMiddleName = additionalPatientName?.given[1];
  result.additionalFamilyName = additionalPatientName?.family;

  result.gender = patient.gender;
  result.birthdate = patient.birthDate ? parseDate(patient.birthDate) : undefined;
  result.telephoneNumber = patient.telecom ? patient.telecom[0].value : '';

  return {
    ...result,
    ...patient.identifier.map((identifier) => {
      const key = camelCase(identifier.system || identifier.type.text);
      return { [key]: identifier.value };
    }),
  };
}

export function getAddressFieldValuesFromFhirPatient(patient: fhir.Patient) {
  const result = {};
  const address = patient.address?.[0];

  if (address) {
    for (const key of Object.keys(address)) {
      switch (key) {
        case 'city':
          result['cityVillage'] = address[key];
          break;
        case 'state':
          result['stateProvince'] = address[key];
          break;
        case 'district':
          result['countyDistrict'] = address[key];
          break;
        case 'extension':
          address[key].forEach((ext) => {
            ext.extension.forEach((extension) => {
              result[extension.url.split('#')[1]] = extension.valueString;
            });
          });
          break;
        default:
          if (key === 'country' || key === 'postalCode') {
            result[key] = address[key];
          }
      }
    }
  }

  return result;
}

export function getPatientUuidMapFromFhirPatient(patient: fhir.Patient): PatientUuidMapType {
  const patientName = patient.name[0];
  const additionalPatientName = patient.name[1];
  const address = patient.address?.[0];

  return {
    preferredNameUuid: patientName?.id,
    additionalNameUuid: additionalPatientName?.id,
    preferredAddressUuid: address?.id,
    ...patient.identifier.map((identifier) => {
      const key = camelCase(identifier.system || identifier.type.text);
      return { [key]: { uuid: identifier.id, value: identifier.value } };
    }),
  };
}

export function getPhonePersonAttributeValueFromFhirPatient(patient: fhir.Patient) {
  const result = {};
  if (patient.telecom) {
    result['phone'] = patient.telecom[0].value;
  }
  return result;
}

type IdentifierMap = { [identifierFieldName: string]: PatientIdentifierValue };
export const filterOutUndefinedPatientIdentifiers = (patientIdentifiers: IdentifierMap): IdentifierMap =>
  Object.fromEntries(
    Object.entries(patientIdentifiers).filter(
      ([key, value]) =>
        (value.autoGeneration && value.selectedSource.autoGenerationOption.manualEntryEnabled) ||
        value.identifierValue !== undefined,
    ),
  );

export const latestFirstEncounter = (a: Encounter, b: Encounter) =>
  new Date(b.encounterDatetime).getTime() - new Date(a.encounterDatetime).getTime();
