import {
  fhirBaseUrl,
  makeUrl,
  messageOmrsServiceWorker,
  navigate,
  restBaseUrl,
  setupDynamicOfflineDataHandler,
  setupOfflineSync,
  type SyncProcessOptions,
} from '@openmrs/esm-framework';
import { patientRegistration, personRelationshipRepresentation } from './constants';
import {
  fetchAddressTemplate,
  fetchAllFieldDefinitionTypes,
  fetchAllRelationshipTypes,
  fetchCurrentSession,
  fetchPatientIdentifierTypesWithSources,
} from './offline.resources';
import { FormManager } from './patient-registration/form-manager';
import { type PatientRegistration } from './patient-registration/patient-registration.types';

export function setupOffline() {
  setupOfflineSync(patientRegistration, [], syncPatientRegistration, {
    onBeginEditSyncItem(syncItem) {
      navigate({ to: `\${openmrsSpaBase}/patient/${syncItem.content.fhirPatient.id}/edit` });
    },
  });

  precacheStaticAssets();

  setupDynamicOfflineDataHandler({
    id: 'esm-patient-registration-app:patient',
    type: 'patient',
    displayName: 'Patient registration',
    async isSynced(patientUuid) {
      const expectedUrls = getPatientUrlsToBeCached(patientUuid);
      const cache = await caches.open('omrs-spa-cache-v1');
      const keys = (await cache.keys()).map((key) => key.url);
      return expectedUrls.every((url) => keys.includes(url));
    },
    async sync(patientUuid) {
      const urlsToCache = getPatientUrlsToBeCached(patientUuid);
      await Promise.allSettled(
        urlsToCache.map(async (url) => {
          await messageOmrsServiceWorker({
            type: 'registerDynamicRoute',
            url,
          });

          await fetch(url);
        }),
      );
    },
  });
}

function getPatientUrlsToBeCached(patientUuid: string) {
  return [
    `${fhirBaseUrl}/Patient/${patientUuid}`,
    `${restBaseUrl}/relationship?v=${personRelationshipRepresentation}&person=${patientUuid}`,
    `${restBaseUrl}/person/${patientUuid}/attribute`,
    `${restBaseUrl}/patient/${patientUuid}/identifier?v=custom:(uuid,identifier,identifierType:(uuid,required,name),preferred)`,
  ].map((url) => window.origin + makeUrl(url));
}

async function precacheStaticAssets() {
  await Promise.all([
    fetchCurrentSession(),
    fetchAddressTemplate(),
    fetchAllRelationshipTypes(),
    fetchAllFieldDefinitionTypes(),
    fetchPatientIdentifierTypesWithSources(),
  ]);
}

export async function syncPatientRegistration(
  queuedPatient: PatientRegistration,
  options: SyncProcessOptions<PatientRegistration>,
) {
  await FormManager.savePatientFormOnline(
    queuedPatient._patientRegistrationData.isNewPatient,
    queuedPatient._patientRegistrationData.formValues,
    queuedPatient._patientRegistrationData.patientUuidMap,
    queuedPatient._patientRegistrationData.initialAddressFieldValues,
    queuedPatient._patientRegistrationData.capturePhotoProps,
    queuedPatient._patientRegistrationData.currentLocation,
    queuedPatient._patientRegistrationData.initialIdentifierValues,
    queuedPatient._patientRegistrationData.currentUser,
    queuedPatient._patientRegistrationData.config,
    queuedPatient._patientRegistrationData.savePatientTransactionManager,
    options.abort,
  );
}
