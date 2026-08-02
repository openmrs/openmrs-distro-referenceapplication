import { defineConfigSchema, getAsyncLifecycle, getSyncLifecycle } from '@openmrs/esm-framework';
import { esmPatientRegistrationSchema } from './config-schema';
import { moduleName } from './constants';
import { setupOffline } from './offline';
import addPatientLinkComponent from './add-patient-link.extension';

export const importTranslation = require.context('../translations', false, /.json$/, 'lazy');

const options = {
  featureName: 'Patient Registration',
  moduleName,
};

export function startupApp() {
  defineConfigSchema(moduleName, esmPatientRegistrationSchema);
  setupOffline();
}

export const root = getAsyncLifecycle(() => import('./root.component'), options);

export const editPatient = getAsyncLifecycle(() => import('./root.component'), options);

export const addPatientLink = getSyncLifecycle(addPatientLinkComponent, options);

export const cancelPatientEditModal = getAsyncLifecycle(() => import('./widgets/cancel-patient-edit.modal'), options);

export const patientPhotoExtension = getAsyncLifecycle(() => import('./patient-photo.extension'), options);

export const editPatientDetailsButton = getAsyncLifecycle(
  () => import('./widgets/edit-patient-details-button.component'),
  options,
);

export const deleteIdentifierConfirmationModal = getAsyncLifecycle(
  () => import('./widgets/delete-identifier-confirmation.modal'),
  options,
);
