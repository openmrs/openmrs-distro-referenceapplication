import React, { useMemo } from 'react';
import classNames from 'classnames';
import useSWRImmutable from 'swr/immutable';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Grid } from '@carbon/react';
import { useConnectivity, useSession } from '@openmrs/esm-framework';
import {
  fetchAddressTemplate,
  fetchAllRelationshipTypes,
  fetchPatientIdentifierTypesWithSources,
} from './offline.resources';
import { ResourcesContextProvider } from './resources-context';
import { FormManager } from './patient-registration/form-manager';
import { PatientRegistration } from './patient-registration/patient-registration.component';
import styles from './root.scss';

export default function Root() {
  const isOnline = useConnectivity();
  const currentSession = useSession();
  const { data: addressTemplate } = useSWRImmutable('patientRegistrationAddressTemplate', fetchAddressTemplate);
  const { data: relationshipTypes } = useSWRImmutable(
    'patientRegistrationRelationshipTypes',
    fetchAllRelationshipTypes,
  );
  const { data: identifierTypes } = useSWRImmutable(
    'patientRegistrationPatientIdentifiers',
    fetchPatientIdentifierTypesWithSources,
  );
  const savePatientForm = useMemo(
    () => (isOnline ? FormManager.savePatientFormOnline : FormManager.savePatientFormOffline),
    [isOnline],
  );

  return (
    <main className={classNames('omrs-main-content', styles.root)}>
      <Grid className={styles.grid}>
        <ResourcesContextProvider
          value={{
            addressTemplate,
            relationshipTypes,
            identifierTypes,
            currentSession,
          }}>
          <BrowserRouter basename={window.getOpenmrsSpaBase()}>
            <Routes>
              <Route
                path="patient-registration"
                element={<PatientRegistration savePatientForm={savePatientForm} isOffline={!isOnline} />}
              />
              <Route
                path="patient/:patientUuid/edit"
                element={<PatientRegistration savePatientForm={savePatientForm} isOffline={!isOnline} />}
              />
            </Routes>
          </BrowserRouter>
        </ResourcesContextProvider>
      </Grid>
    </main>
  );
}
