import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import classNames from 'classnames';
import { Button, InlineLoading, Link } from '@carbon/react';
import { XAxis } from '@carbon/react/icons';
import { useLocation, useParams } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { Form, Formik, type FormikHelpers, type FormikErrors } from 'formik';
import {
  createErrorHandler,
  interpolateUrl,
  showSnackbar,
  useConfig,
  usePatient,
  usePatientPhoto,
} from '@openmrs/esm-framework';
import { builtInSections, type RegistrationConfig, type SectionDefinition } from '../config-schema';
import { cancelRegistration, filterOutUndefinedPatientIdentifiers, scrollIntoView } from './patient-registration-utils';
import { getValidationSchema } from './validation/patient-registration-validation';
import { DummyDataInput } from './input/dummy-data/dummy-data-input.component';
import { PatientRegistrationContextProvider } from './patient-registration-context';
import { useResourcesContext } from '../resources-context';
import { SectionWrapper } from './section/section-wrapper.component';
import { type CapturePhotoProps, type FormValues } from './patient-registration.types';
import { type SavePatientForm, SavePatientTransactionManager } from './form-manager';
import { useInitialAddressFieldValues, useInitialFormValues, usePatientUuidMap } from './patient-registration-hooks';
import BeforeSavePrompt from './before-save-prompt.component';
import styles from './patient-registration.scss';

let exportedInitialFormValuesForTesting = {} as FormValues;

export interface PatientRegistrationProps {
  savePatientForm: SavePatientForm;
  isOffline: boolean;
}

export const PatientRegistration: React.FC<PatientRegistrationProps> = ({ savePatientForm, isOffline }) => {
  const { t } = useTranslation();
  const { currentSession, identifierTypes, addressTemplate } = useResourcesContext();
  const { patientUuid: uuidOfPatientToEdit } = useParams();
  const { search } = useLocation();
  const { isLoading: isLoadingPatientToEdit, patient: patientToEdit } = usePatient(uuidOfPatientToEdit);
  const config = useConfig<RegistrationConfig>();

  const [initialFormValues, setInitialFormValues] = useInitialFormValues(
    isLoadingPatientToEdit,
    patientToEdit,
    uuidOfPatientToEdit,
  );
  const [initialAddressFieldValues] = useInitialAddressFieldValues(
    {},
    isLoadingPatientToEdit,
    patientToEdit,
    uuidOfPatientToEdit,
  );

  const [patientUuidMap] = usePatientUuidMap({}, isLoadingPatientToEdit, patientToEdit, uuidOfPatientToEdit);

  const [target, setTarget] = useState<undefined | string>();
  const [capturePhotoProps, setCapturePhotoProps] = useState<CapturePhotoProps | null>(null);

  const location = currentSession?.sessionLocation?.uuid;
  const inEditMode = isLoadingPatientToEdit ? undefined : !!(uuidOfPatientToEdit && patientToEdit);
  const showDummyDataInput = useMemo(
    () => localStorage.getItem('openmrs:devtools') === 'true' && !inEditMode,
    [inEditMode],
  );
  const { data: photo } = usePatientPhoto(patientToEdit?.id);
  const savePatientTransactionManager = useRef(new SavePatientTransactionManager());
  const validationSchema = getValidationSchema(config, t, addressTemplate);

  useEffect(() => {
    exportedInitialFormValuesForTesting = initialFormValues;
  }, [initialFormValues]);

  const sections: Array<SectionDefinition> = useMemo(() => {
    return config.sections
      .map(
        (sectionName) =>
          config.sectionDefinitions.filter((s) => s.id === sectionName)[0] ??
          builtInSections.filter((s) => s.id === sectionName)[0],
      )
      .filter((s) => s);
  }, [config.sections, config.sectionDefinitions]);

  const onFormSubmit = async (values: FormValues, helpers: FormikHelpers<FormValues>) => {
    const abortController = new AbortController();
    helpers.setSubmitting(true);

    const updatedFormValues = { ...values, identifiers: filterOutUndefinedPatientIdentifiers(values.identifiers) };
    try {
      await savePatientForm(
        !inEditMode,
        updatedFormValues,
        patientUuidMap,
        initialAddressFieldValues,
        capturePhotoProps,
        location,
        initialFormValues['identifiers'],
        currentSession,
        config,
        savePatientTransactionManager.current,
        abortController,
      );

      showSnackbar({
        subtitle: inEditMode
          ? t('updatePatientSuccessSnackbarSubtitle', "The patient's information has been successfully updated")
          : t(
              'registerPatientSuccessSnackbarSubtitle',
              'The patient can now be found by searching for them using their name or ID number',
            ),
        title: inEditMode
          ? t('updatePatientSuccessSnackbarTitle', 'Patient Details Updated')
          : t('registerPatientSuccessSnackbarTitle', 'New Patient Created'),
        kind: 'success',
        isLowContrast: true,
      });

      const afterUrl = new URLSearchParams(search).get('afterUrl');
      const redirectUrl = interpolateUrl(afterUrl || config.links.submitButton, { patientUuid: values.patientUuid });

      setTarget(redirectUrl);
    } catch (error) {
      if (error.responseBody?.error?.globalErrors) {
        error.responseBody.error.globalErrors.forEach((error) => {
          showSnackbar({
            title: inEditMode
              ? t('updatePatientErrorSnackbarTitle', 'Patient Details Update Failed')
              : t('registrationErrorSnackbarTitle', 'Patient Registration Failed'),
            subtitle: error.message,
            kind: 'error',
          });
        });
      } else if (error.responseBody?.error?.message) {
        showSnackbar({
          title: inEditMode
            ? t('updatePatientErrorSnackbarTitle', 'Patient Details Update Failed')
            : t('registrationErrorSnackbarTitle', 'Patient Registration Failed'),
          subtitle: error.responseBody.error.message,
          kind: 'error',
        });
      } else {
        createErrorHandler()(error);
      }

      helpers.setSubmitting(false);
    }
  };

  const getDescription = (errors: FormikErrors<FormValues>): JSX.Element => {
    return (
      <ul style={{ listStyle: 'inside' }}>
        {Object.keys(errors).map((error, index) => {
          return <li key={index}>{t(`${error}LabelText`, error)}</li>;
        })}
      </ul>
    );
  };

  const displayErrors = (errors: FormikErrors<FormValues>): void => {
    if (errors && typeof errors === 'object' && !!Object.keys(errors).length) {
      showSnackbar({
        isLowContrast: true,
        kind: 'warning',
        title: t('fieldsWithErrors', 'The following fields have errors:'),
        subtitle: <>{getDescription(errors)}</>,
      });
    }
  };

  const createContextValue = useCallback(
    (formikProps) => ({
      identifierTypes,
      validationSchema,
      values: formikProps.values,
      inEditMode,
      setFieldValue: formikProps.setFieldValue,
      setFieldTouched: formikProps.setFieldTouched,
      setCapturePhotoProps,
      currentPhoto: photo?.imageSrc,
      isOffline,
      initialFormValues: formikProps.initialValues,
      setInitialFormValues,
    }),
    [
      identifierTypes,
      validationSchema,
      inEditMode,
      setCapturePhotoProps,
      photo?.imageSrc,
      isOffline,
      setInitialFormValues,
    ],
  );

  return (
    <Formik
      enableReinitialize
      initialValues={initialFormValues}
      onSubmit={onFormSubmit}
      validationSchema={validationSchema}>
      {(props) => (
        <Form className={styles.form}>
          <BeforeSavePrompt when={Object.keys(props.touched).length > 0} redirect={target} />
          <div className={styles.formContainer}>
            {/* Navigation Sidebar */}
            <div className={styles.stickyColumn}>
              <h4>
                {inEditMode
                  ? t('editPatientDetails', 'Edit patient details')
                  : t('createNewPatient', 'Create new patient')}
              </h4>
              {showDummyDataInput && <DummyDataInput setValues={props.setValues} />}
              <p className={styles.label01}>{t('jumpTo', 'Jump to')}</p>
              {sections.map((section) => (
                <div className={classNames(styles.space05, styles.touchTarget)} key={section.name}>
                  <Link className={styles.linkName} onClick={() => scrollIntoView(section.id)}>
                    <XAxis size={16} /> {t(`${section.id}Section`, section.name)}
                  </Link>
                </div>
              ))}
              <hr className={styles.divider} />
              <Button
                className={styles.submitButton}
                type="submit"
                onClick={() => props.validateForm().then((errors) => displayErrors(errors))}
                // Current session and identifiers are required for patient registration.
                // If currentSession or identifierTypes are not available, then the
                // user should be blocked to register the patient.
                disabled={!currentSession || !identifierTypes || props.isSubmitting}>
                {props.isSubmitting ? (
                  <InlineLoading
                    className={styles.spinner}
                    description={`${t('submitting', 'Submitting')} ...`}
                    iconDescription="submitting"
                  />
                ) : inEditMode ? (
                  t('updatePatient', 'Update patient')
                ) : (
                  t('registerPatient', 'Register patient')
                )}
              </Button>
              <Button className={styles.cancelButton} kind="secondary" onClick={cancelRegistration}>
                {t('cancel', 'Cancel')}
              </Button>
            </div>
            {/* Registration Form */}
            <div className={styles.infoGrid}>
              <PatientRegistrationContextProvider value={createContextValue(props)}>
                {sections.map((section, index) => (
                  <SectionWrapper
                    key={`registration-section-${section.id}`}
                    sectionDefinition={section}
                    index={index}
                  />
                ))}
              </PatientRegistrationContextProvider>
            </div>
          </div>
        </Form>
      )}
    </Formik>
  );
};

/**
 * @internal
 * Just exported for testing
 */
export { exportedInitialFormValuesForTesting as initialFormValues };
