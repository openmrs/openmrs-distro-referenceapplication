import {
  type FetchResponse,
  getConfig,
  openmrsFetch,
  queueSynchronizationItem,
  restBaseUrl,
  type Session,
  type StyleguideConfigObject,
  toOmrsIsoString,
} from '@openmrs/esm-framework';
import { patientRegistration } from '../constants';
import {
  type AddressProperties,
  type AttributeValue,
  type CapturePhotoProps,
  type Encounter,
  type FormValues,
  type Patient,
  type PatientIdentifier,
  type PatientRegistration,
  type PatientUuidMapType,
  type RelationshipValue,
} from './patient-registration.types';
import {
  addPatientIdentifier,
  deletePatientIdentifier,
  deletePersonName,
  deleteRelationship,
  generateIdentifier,
  getDatetime,
  saveEncounter,
  savePatient,
  savePatientPhoto,
  saveRelationship,
  updatePatientIdentifier,
  updateRelationship,
} from './patient-registration.resource';
import { type RegistrationConfig } from '../config-schema';

type AddressFieldValues = Partial<Record<AddressProperties, string>>;

export type SavePatientForm = (
  isNewPatient: boolean,
  values: FormValues,
  patientUuidMap: PatientUuidMapType,
  initialAddressFieldValues: AddressFieldValues,
  capturePhotoProps: CapturePhotoProps,
  currentLocation: string,
  initialIdentifierValues: FormValues['identifiers'],
  currentUser: Session,
  config: RegistrationConfig,
  savePatientTransactionManager: SavePatientTransactionManager,
  abortController?: AbortController,
) => Promise<string | void>;

export class FormManager {
  static savePatientFormOffline: SavePatientForm = async (
    isNewPatient,
    values,
    patientUuidMap,
    initialAddressFieldValues,
    capturePhotoProps,
    currentLocation,
    initialIdentifierValues,
    currentUser,
    config,
  ) => {
    const syncItem: PatientRegistration = {
      fhirPatient: FormManager.mapPatientToFhirPatient(
        FormManager.getPatientToCreate(isNewPatient, values, patientUuidMap, initialAddressFieldValues, [], config),
      ),
      _patientRegistrationData: {
        isNewPatient,
        formValues: values,
        patientUuidMap,
        initialAddressFieldValues,
        capturePhotoProps,
        currentLocation,
        initialIdentifierValues,
        currentUser,
        config,
        savePatientTransactionManager: new SavePatientTransactionManager(),
      },
    };

    await queueSynchronizationItem(patientRegistration, syncItem, {
      id: values.patientUuid,
      displayName: 'Patient registration',
      patientUuid: syncItem.fhirPatient.id,
      dependencies: [],
    });

    return null;
  };

  static savePatientFormOnline: SavePatientForm = async (
    isNewPatient,
    values,
    patientUuidMap,
    initialAddressFieldValues,
    capturePhotoProps,
    currentLocation,
    initialIdentifierValues,
    currentUser,
    config,
    savePatientTransactionManager,
    abortController,
  ) => {
    const patientIdentifiers: Array<PatientIdentifier> = await FormManager.savePatientIdentifiers(
      isNewPatient,
      values.patientUuid,
      values.identifiers,
      initialIdentifierValues,
      currentLocation,
    );

    const createdPatient = FormManager.getPatientToCreate(
      isNewPatient,
      values,
      patientUuidMap,
      initialAddressFieldValues,
      patientIdentifiers,
      config,
    );

    FormManager.getDeletedNames(values.patientUuid, patientUuidMap).forEach(async (name) => {
      await deletePersonName(name.nameUuid, name.personUuid);
    });

    const savePatientResponse = await savePatient(
      createdPatient,
      isNewPatient && !savePatientTransactionManager.patientSaved ? undefined : values.patientUuid,
    );

    if (savePatientResponse.ok) {
      savePatientTransactionManager.patientSaved = true;
      await this.saveRelationships(values.relationships, savePatientResponse);

      await this.saveObservations(values.obs, savePatientResponse, currentLocation, currentUser, config);

      const { patientPhotoConceptUuid } = await getConfig<StyleguideConfigObject>('@openmrs/esm-styleguide');

      if (patientPhotoConceptUuid && capturePhotoProps?.imageData) {
        await savePatientPhoto(
          savePatientResponse.data.uuid,
          capturePhotoProps.imageData,
          `${restBaseUrl}/obs`,
          capturePhotoProps.dateTime || new Date().toISOString(),
          patientPhotoConceptUuid,
        );
      }
    }

    return savePatientResponse.data.uuid;
  };

  static async saveRelationships(relationships: Array<RelationshipValue>, savePatientResponse: FetchResponse) {
    return Promise.all(
      relationships
        .filter((m) => m.relationshipType)
        .filter((relationship) => !!relationship.action)
        .map(({ relatedPersonUuid, relationshipType, uuid: relationshipUuid, action }) => {
          const [type, direction] = relationshipType.split('/');
          const thisPatientUuid = savePatientResponse.data.uuid;
          const isAToB = direction === 'aIsToB';
          const relationshipToSave = {
            personA: isAToB ? relatedPersonUuid : thisPatientUuid,
            personB: isAToB ? thisPatientUuid : relatedPersonUuid,
            relationshipType: type,
          };

          switch (action) {
            case 'ADD':
              return saveRelationship(relationshipToSave);
            case 'UPDATE':
              return updateRelationship(relationshipUuid, relationshipToSave);
            case 'DELETE':
              return deleteRelationship(relationshipUuid);
          }
        }),
    );
  }

  static async saveObservations(
    obss: { [conceptUuid: string]: string },
    savePatientResponse: FetchResponse,
    currentLocation: string,
    currentUser: Session,
    config: RegistrationConfig,
  ) {
    if (obss && Object.keys(obss).length > 0) {
      if (!config.registrationObs.encounterTypeUuid) {
        console.error(
          'The registration form has been configured to have obs fields, ' +
            'but no registration encounter type has been configured. Obs field values ' +
            'will not be saved.',
        );
      } else {
        const encounterToSave: Encounter = {
          encounterDatetime: new Date(),
          patient: savePatientResponse.data.uuid,
          encounterType: config.registrationObs.encounterTypeUuid,
          location: currentLocation,
          encounterProviders: [
            {
              provider: currentUser.currentProvider.uuid,
              encounterRole: config.registrationObs.encounterProviderRoleUuid,
            },
          ],
          form: config.registrationObs.registrationFormUuid,
          obs: Object.entries(obss)
            .filter(([, value]) => value !== '')
            .map(([conceptUuid, value]) => ({ concept: conceptUuid, value })),
        };
        return saveEncounter(encounterToSave);
      }
    }
  }

  static async savePatientIdentifiers(
    isNewPatient: boolean,
    patientUuid: string,
    patientIdentifiers: FormValues['identifiers'], // values.identifiers
    initialIdentifierValues: FormValues['identifiers'], // Initial identifiers assigned to the patient
    location: string,
  ): Promise<Array<PatientIdentifier>> {
    let identifierTypeRequests = Object.values(patientIdentifiers)
      /* Since default identifier-types will be present on the form and are also in the not-required state,
        therefore we might be running into situations when there's no value and no source associated,
        hence filtering these fields out.
      */
      .filter(
        ({ identifierValue, autoGeneration, selectedSource }) => identifierValue || (autoGeneration && selectedSource),
      )
      .map(async (patientIdentifier) => {
        const {
          identifierTypeUuid,
          identifierValue,
          identifierUuid,
          selectedSource,
          preferred,
          autoGeneration,
          initialValue,
        } = patientIdentifier;

        const autoGenerationManualEntry =
          autoGeneration && selectedSource?.autoGenerationOption?.manualEntryEnabled && !!identifierValue;

        const identifier =
          !autoGeneration || autoGenerationManualEntry
            ? identifierValue
            : await (
                await generateIdentifier(selectedSource.uuid)
              ).data.identifier;

        const identifierToCreate = {
          uuid: identifierUuid,
          identifier,
          identifierType: identifierTypeUuid,
          location,
          preferred,
        };

        if (!isNewPatient) {
          if (!initialValue) {
            await addPatientIdentifier(patientUuid, identifierToCreate);
          } else if (initialValue !== identifier) {
            await updatePatientIdentifier(patientUuid, identifierUuid, identifierToCreate.identifier);
          }
        }

        return identifierToCreate;
      });

    /*
      If there was initially an identifier assigned to the patient,
      which is now not present in the patientIdentifiers(values.identifiers),
      this means that the identifier is meant to be deleted, hence we need
      to delete the respective identifiers.
    */

    if (patientUuid) {
      Object.keys(initialIdentifierValues)
        .filter((identifierFieldName) => !patientIdentifiers[identifierFieldName])
        .forEach(async (identifierFieldName) => {
          await deletePatientIdentifier(patientUuid, initialIdentifierValues[identifierFieldName].identifierUuid);
        });
    }

    return Promise.all(identifierTypeRequests);
  }

  static getDeletedNames(patientUuid: string, patientUuidMap: PatientUuidMapType) {
    if (patientUuidMap?.additionalNameUuid) {
      return [
        {
          nameUuid: patientUuidMap.additionalNameUuid,
          personUuid: patientUuid,
        },
      ];
    }
    return [];
  }

  static getPatientToCreate(
    isNewPatient: boolean,
    values: FormValues,
    patientUuidMap: PatientUuidMapType,
    initialAddressFieldValues: AddressFieldValues,
    identifiers: Array<PatientIdentifier>,
    config?: RegistrationConfig,
  ): Patient {
    let birthdate;
    if (values.birthdate instanceof Date) {
      birthdate = [values.birthdate.getFullYear(), values.birthdate.getMonth() + 1, values.birthdate.getDate()].join(
        '-',
      );
    } else {
      birthdate = values.birthdate;
    }

    return {
      uuid: values.patientUuid,
      person: {
        uuid: values.patientUuid,
        names: FormManager.getNames(values, patientUuidMap),
        gender: values.gender.charAt(0).toUpperCase(),
        birthdate,
        birthdateEstimated: values.birthdateEstimated,
        attributes: FormManager.getPatientAttributes(isNewPatient, values, patientUuidMap),
        addresses: [values.address],
        ...FormManager.getPatientDeathInfo(values, config),
      },
      identifiers,
    };
  }

  static getNames(values: FormValues, patientUuidMap: PatientUuidMapType) {
    const names = [
      {
        uuid: patientUuidMap.preferredNameUuid,
        preferred: true,
        givenName: values.givenName,
        middleName: values.middleName,
        familyName: values.familyName,
      },
    ];

    if (values.addNameInLocalLanguage) {
      names.push({
        uuid: patientUuidMap.additionalNameUuid,
        preferred: false,
        givenName: values.additionalGivenName,
        middleName: values.additionalMiddleName,
        familyName: values.additionalFamilyName,
      });
    }

    return names;
  }

  static getPatientAttributes(isNewPatient: boolean, values: FormValues, patientUuidMap: PatientUuidMapType) {
    const attributes: Array<AttributeValue> = [];
    if (values.attributes) {
      Object.entries(values.attributes)
        .filter(([, value]) => !!value)
        .forEach(([key, value]) => {
          attributes.push({
            attributeType: key,
            value,
          });
        });

      if (!isNewPatient && values.patientUuid) {
        Object.entries(values.attributes)
          .filter(([, value]) => !value)
          .forEach(async ([key]) => {
            const attributeUuid = patientUuidMap[`attribute.${key}`];
            await openmrsFetch(`${restBaseUrl}/person/${values.patientUuid}/attribute/${attributeUuid}`, {
              method: 'DELETE',
            }).catch((err) => {
              console.error(err);
            });
          });
      }
    }

    return attributes;
  }

  static getPatientDeathInfo(values: FormValues, config?: RegistrationConfig) {
    const { isDead, deathDate, deathTime, deathTimeFormat, deathCause, nonCodedCauseOfDeath } = values;

    if (!isDead) {
      return {
        dead: false,
      };
    }
    const dateTimeOfDeath = toOmrsIsoString(getDatetime(deathDate, deathTime, deathTimeFormat));

    return {
      dead: true,
      deathDate: dateTimeOfDeath,
      ...(deathCause === config?.freeTextFieldConceptUuid
        ? { causeOfDeathNonCoded: nonCodedCauseOfDeath, causeOfDeath: null }
        : { causeOfDeath: deathCause, causeOfDeathNonCoded: null }),
    };
  }

  static mapPatientToFhirPatient(patient: Partial<Patient>): fhir.Patient {
    // Important:
    // When changing this code, ideally assume that `patient` can be missing any attribute.
    // The `fhir.Patient` provides us with the benefit that all properties are nullable and thus
    // not required (technically, at least). -> Even if we cannot map some props here, we still
    // provide a valid fhir.Patient object. The various patient chart modules should be able to handle
    // such missing props correctly (and should be updated if they don't).

    // Mapping inspired by:
    // https://github.com/openmrs/openmrs-module-fhir/blob/669b3c52220bb9abc622f815f4dc0d8523687a57/api/src/main/java/org/openmrs/module/fhir/api/util/FHIRPatientUtil.java#L36
    // https://github.com/openmrs/openmrs-esm-patient-management/blob/94e6f637fb37cf4984163c355c5981ea6b8ca38c/packages/esm-patient-search-app/src/patient-search-result/patient-search-result.component.tsx#L21
    // Update as required.
    return {
      id: patient.uuid,
      gender: patient.person?.gender,
      birthDate: patient.person?.birthdate,
      deceasedBoolean: patient.person.dead,
      deceasedDateTime: patient.person.deathDate,
      name: patient.person?.names?.map((name) => ({
        given: [name.givenName, name.middleName].filter(Boolean),
        family: name.familyName,
      })),
      address: patient.person?.addresses.map((address) => ({
        city: address.cityVillage,
        country: address.country,
        postalCode: address.postalCode,
        state: address.stateProvince,
        use: 'home',
      })),
      telecom: patient.person.attributes?.filter((attribute) => attribute.attributeType === 'Telephone Number'),
    };
  }
}

export class SavePatientTransactionManager {
  patientSaved = false;
}
