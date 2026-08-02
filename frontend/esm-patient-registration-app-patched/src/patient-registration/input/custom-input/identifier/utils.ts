import { type FetchedPatientIdentifierType, type PatientIdentifierType } from '../../../patient-registration.types';

export function shouldBlockPatientIdentifierInOfflineMode(identifierType: PatientIdentifierType) {
  // Patient Identifiers which are unique and can be manually entered are prohibited while offline because
  // of the chance of generating conflicts when syncing later.
  return (
    isUniqueIdentifierTypeForOffline(identifierType) &&
    !identifierType.identifierSources.some(
      (source) =>
        !source.autoGenerationOption?.manualEntryEnabled && source.autoGenerationOption?.automaticGenerationEnabled,
    )
  );
}

export function isUniqueIdentifierTypeForOffline(identifierType: FetchedPatientIdentifierType) {
  // In offline mode we consider each uniqueness behavior which could cause conflicts during syncing as 'unique'.
  // Syncing conflicts can appear for the following behaviors:
  return identifierType.uniquenessBehavior === 'UNIQUE' || identifierType.uniquenessBehavior === 'LOCATION';
}
