import React from 'react';
import { PatientPhoto, type PatientPhotoProps } from '@openmrs/esm-framework';

function PatientPhotoExtension(props: PatientPhotoProps) {
  console.warn(
    'Using the patient-photo extension (or patient-photo-slot slot) is deprecated. Please use the PatientPhoto component from @openmrs/esm-framework.',
  );
  return <PatientPhoto {...props} />;
}

export default PatientPhotoExtension;
