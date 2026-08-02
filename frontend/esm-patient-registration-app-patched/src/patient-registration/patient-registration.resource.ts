import dayjs from 'dayjs';
import { openmrsFetch, restBaseUrl } from '@openmrs/esm-framework';
import { type Patient, type Relationship, type PatientIdentifier, type Encounter } from './patient-registration.types';

function dataURItoFile(dataURI: string) {
  const byteString = window.atob(dataURI.split(',')[1]);
  const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
  // write the bytes of the string to a typed array
  const buffer = new Uint8Array(byteString.length);

  for (let i = 0; i < byteString.length; i++) {
    buffer[i] = byteString.charCodeAt(i);
  }

  const blob = new Blob([buffer], { type: mimeString });
  return new File([blob], 'patient-photo.png');
}

export function savePatient(patient: Patient | null, updatePatientUuid?: string) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/patient/${updatePatientUuid || ''}`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: patient,
    signal: abortController.signal,
  });
}

export function saveEncounter(encounter: Encounter) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/encounter`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: encounter,
    signal: abortController.signal,
  });
}

export function generateIdentifier(source: string) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/idgen/identifiersource/${source}/identifier`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: {},
    signal: abortController.signal,
  });
}

export function deletePersonName(nameUuid: string, personUuid: string) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/person/${personUuid}/name/${nameUuid}`, {
    method: 'DELETE',
    signal: abortController.signal,
  });
}

export function saveRelationship(relationship: Relationship) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/relationship`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: relationship,
    signal: abortController.signal,
  });
}

export function updateRelationship(relationshipUuid, relationship: { relationshipType: string }) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/relationship/${relationshipUuid}`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'POST',
    body: { relationshipType: relationship.relationshipType },
    signal: abortController.signal,
  });
}

export function deleteRelationship(relationshipUuid) {
  const abortController = new AbortController();

  return openmrsFetch(`${restBaseUrl}/relationship/${relationshipUuid}`, {
    headers: {
      'Content-Type': 'application/json',
    },
    method: 'DELETE',
    signal: abortController.signal,
  });
}

export async function savePatientPhoto(
  patientUuid: string,
  content: string,
  url: string,
  date: string,
  conceptUuid: string,
) {
  const abortController = new AbortController();

  const formData = new FormData();
  formData.append('patient', patientUuid);
  formData.append('file', dataURItoFile(content));
  formData.append(
    'json',
    JSON.stringify({
      person: patientUuid,
      concept: conceptUuid,
      groupMembers: [],
      obsDatetime: date,
    }),
  );

  return openmrsFetch(url, {
    method: 'POST',
    signal: abortController.signal,
    body: formData,
  });
}

export async function fetchPerson(query: string, abortController: AbortController) {
  const [patientsRes, personsRes] = await Promise.all([
    openmrsFetch(`${restBaseUrl}/patient?q=${query}`, {
      signal: abortController.signal,
    }),
    openmrsFetch(`${restBaseUrl}/person?q=${query}`, {
      signal: abortController.signal,
    }),
  ]);

  const results = [...patientsRes.data.results];

  personsRes.data.results.forEach((person) => {
    if (!results.some((patient) => patient.uuid === person.uuid)) {
      results.push(person);
    }
  });

  return results;
}

export async function addPatientIdentifier(patientUuid: string, patientIdentifier: PatientIdentifier) {
  const abortController = new AbortController();
  return openmrsFetch(`${restBaseUrl}/patient/${patientUuid}/identifier/`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: patientIdentifier,
  });
}

export async function updatePatientIdentifier(patientUuid: string, identifierUuid: string, identifier: string) {
  const abortController = new AbortController();
  return openmrsFetch(`${restBaseUrl}/patient/${patientUuid}/identifier/${identifierUuid}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
    body: { identifier },
  });
}

export async function deletePatientIdentifier(patientUuid: string, patientIdentifierUuid: string) {
  const abortController = new AbortController();
  return openmrsFetch(`${restBaseUrl}/patient/${patientUuid}/identifier/${patientIdentifierUuid}?purge`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
    },
    signal: abortController.signal,
  });
}

export function getDatetime(date: Date | string, time: string, timeFormat: 'AM' | 'PM') {
  const datetime = new Date(date);
  const [hours, minutes] = time.split(':').map(Number);
  const fullHours = timeFormat === 'PM' ? (hours % 12) + 12 : hours % 12;
  return dayjs(datetime).hour(fullHours).minute(minutes).second(0).millisecond(0).toDate();
}
