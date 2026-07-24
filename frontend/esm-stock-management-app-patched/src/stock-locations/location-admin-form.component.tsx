import React, { useState } from 'react';
import { z } from 'zod';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  Button,
  ComposedModal,
  FilterableMultiSelect,
  FormGroup,
  InlineNotification,
  ModalBody,
  ModalFooter,
  ModalHeader,
  Stack,
  TextInput,
} from '@carbon/react';
import { getCoreTranslation } from '@openmrs/esm-framework';
import { useTranslation } from 'react-i18next';
import { type locationData } from '../stock-items/types';
import { useLocationTags } from './stock-locations-table.resource';
import styles from './stock-locations-table.scss';

const LocationAdministrationSchema = z.object({
  name: z.string().max(255),
});

interface LocationAdministrationFormProps {
  showModal: boolean;
  onModalChange: (showModal: boolean) => void;
  handleCreateQuestion?: (formData: locationData) => void;
  handleDeleteBedTag?: () => void;
  headerTitle: string;
  initialData: locationData;
}

interface ErrorType {
  message: string;
}

const LocationAdministrationForm: React.FC<LocationAdministrationFormProps> = ({
  showModal,
  onModalChange,
  handleCreateQuestion,
  headerTitle,
  initialData,
}) => {
  const { t } = useTranslation();

  const [showErrorNotification, setShowErrorNotification] = useState(false);
  const [formStateError, setFormStateError] = useState('');

  // Location tag types
  const { locationTagList: Tags } = useLocationTags();

  const {
    handleSubmit,
    control,
    getValues,
    formState: { isDirty },
  } = useForm<locationData>({
    mode: 'all',
    resolver: zodResolver(LocationAdministrationSchema),
    defaultValues: {
      name: initialData.name || '',
      tags: initialData.tags || [],
    },
  });

  const onSubmit = (formData: locationData) => {
    const formDataFormSubmission = getValues();
    const locationTagsUuid = formDataFormSubmission?.tags?.['selectedItems']?.map((tag) => tag.uuid) ?? [];

    const payload = {
      name: formDataFormSubmission.name,
      tags: locationTagsUuid,
    };
    const result = LocationAdministrationSchema.safeParse(formData);
    if (result.success) {
      setShowErrorNotification(false);
      handleCreateQuestion(payload);
    }
  };

  const onError = (error: { [key: string]: ErrorType }) => {
    setFormStateError(Object.entries(error)[0][1].message);
    setShowErrorNotification(true);
  };

  return (
    <ComposedModal open={showModal} onClose={() => onModalChange(false)} preventCloseOnClickOutside size={'md'}>
      <ModalHeader title={headerTitle} />
      <form onSubmit={handleSubmit(onSubmit, onError)}>
        <ModalBody hasScrollingContent>
          <Stack gap={3}>
            <FormGroup legendText={''}>
              <Controller
                name="name"
                control={control}
                rules={{ required: true }}
                render={({ field, fieldState }) => (
                  <>
                    <TextInput
                      id="location"
                      labelText={t('locationName', 'Location name')}
                      placeholder={t('locationPlaceholder', 'Add a location')}
                      invalidText={fieldState.error?.message}
                      {...field}
                    />
                  </>
                )}
              />
              <div className={styles.loaderContainer}>
                <Controller
                  name="tags"
                  control={control}
                  render={({ field }) => (
                    <FilterableMultiSelect
                      id="tag"
                      titleText={t('selectTags', 'Select tags')}
                      helperText={t('selectTagsHelper', 'Select one or more tags for this location')}
                      items={Tags ?? []}
                      {...field}
                      itemToString={(item) => {
                        if (item && typeof item === 'object' && 'display' in item) {
                          return (item as { display: string }).display;
                        }
                        return '';
                      }}
                      selectionFeedback="top-after-reopen"
                    />
                  )}
                />
              </div>
            </FormGroup>

            {showErrorNotification && (
              <InlineNotification
                lowContrast
                title={t('error', 'Error')}
                style={{ minWidth: '100%', margin: '0rem', padding: '0rem' }}
                role="alert"
                kind="error"
                subtitle={
                  t('pleaseFillField', '{{message}}', {
                    message: formStateError,
                  }) + '.'
                }
                onClose={() => setShowErrorNotification(false)}
              />
            )}
          </Stack>
        </ModalBody>
        <ModalFooter>
          <Button onClick={() => onModalChange(false)} kind="secondary">
            {getCoreTranslation('cancel')}
          </Button>
          <Button disabled={!isDirty} type="submit">
            <span>{t('save', 'Save')}</span>
          </Button>
        </ModalFooter>
      </form>
    </ComposedModal>
  );
};

export default LocationAdministrationForm;
