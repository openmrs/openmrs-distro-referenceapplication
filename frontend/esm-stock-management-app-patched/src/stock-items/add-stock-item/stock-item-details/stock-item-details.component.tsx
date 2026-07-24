import React, { useMemo } from 'react';
import classNames from 'classnames';
import { Button, ButtonSet, FormGroup, InlineLoading, Stack } from '@carbon/react';
import { Save } from '@carbon/react/icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { type SubmitHandler, useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { getCoreTranslation, restBaseUrl, showSnackbar, useLayoutType } from '@openmrs/esm-framework';
import { createStockItem, updateStockItem } from '../../stock-items.resource';
import { expirationOptions, radioOptions, StockItemType } from './stock-item-details.resource';
import { handleMutate } from '../../../utils';
import { launchAddOrEditStockItemWorkspace } from '../../stock-item.utils';
import { createStockItemDetailsSchema, type StockItemFormData } from '../../validationSchema';
import { type StockItemDTO } from '../../../core/api/types/stockItem/StockItem';
import ConceptsSelector from '../concepts-selector/concepts-selector.component';
import ControlledNumberInput from '../../../core/components/carbon/controlled-number-input.component';
import ControlledRadioButtonGroup from '../../../core/components/carbon/controlled-radio-button-group.component';
import ControlledTextInput from '../../../core/components/carbon/controlled-text-input.component';
import DispensingUnitSelector from '../dispensing-unit-selector/dispensing-unit-selector.component';
import DrugSelector from '../drug-selector/drug-selector.component';
import PreferredVendorSelector from '../preferred-vendor-selector/preferred-vendor-selector.component';
import StockItemCategorySelector from '../stock-item-category-selector/stock-item-category-selector.component';
import StockItemUnitsEdit from '../stock-item-units-edit/stock-item-units-edit.component';
import styles from '../../add-stock-item/add-stock-item.scss';

interface StockItemDetailsProps {
  stockItem?: StockItemDTO;
  handleTabChange: (index) => void;
  onCloseWorkspace?: () => void;
}

const StockItemDetails = ({ stockItem, handleTabChange, onCloseWorkspace }: StockItemDetailsProps) => {
  const { t } = useTranslation();
  const isTablet = useLayoutType() === 'tablet';

  const { handleSubmit, control, formState, watch } = useForm<StockItemFormData>({
    defaultValues: stockItem ?? {},
    mode: 'all',
    resolver: zodResolver(createStockItemDetailsSchema(t)),
  });

  const { errors } = formState;
  const handleSave: SubmitHandler<StockItemFormData> = async (formValues) => {
    try {
      const response = stockItem
        ? await updateStockItem(stockItem?.uuid, formValues)
        : await createStockItem(formValues);
      if (response?.data) {
        showSnackbar({
          isLowContrast: true,
          title: stockItem ? `${t('editStockItem', 'Edit stock item')}` : `${t('addStockItem', 'Add stock item')}`,
          kind: 'success',
          subtitle: stockItem
            ? `${t('stockItemEdited', 'Stock item edited successfully')}`
            : `${t('stockItemAdded', 'Stock item added successfully')}`,
        });
        if (!stockItem) {
          onCloseWorkspace?.();
          // launch edit stock item workspace
          const item = response.data;
          item.isDrug = !!item.drugUuid;
          launchAddOrEditStockItemWorkspace(t, item);
        }
      }

      handleTabChange(1);
      handleMutate(`${restBaseUrl}/stockmanagement/stockitem`);
    } catch (e) {
      // Show notification
      showSnackbar({
        title: stockItem
          ? t('errorEditingStockItem', 'Error editing a stock Item')
          : t('errorAddingStockItem', 'Error adding a stock Item'),
        kind: 'error',
        isLowContrast: true,
        subtitle: e?.responseBody?.error?.message,
      });
    }
  };
  const [observableIsDrug, observableHasExpiration] = watch(['isDrug', 'hasExpiration']);
  const selectedItemType = useMemo<StockItemType | null>(() => {
    if (observableIsDrug === true) return StockItemType.PHARMACEUTICALS;
    else if (observableIsDrug === false) return StockItemType.NONE_PHARMACEUTICALS;
    return null;
  }, [observableIsDrug]);

  return (
    <form className={styles.formContainer}>
      <Stack className={styles.stack} gap={5}>
        {!stockItem && (
          <FormGroup
            className="clear-margin-bottom"
            legendText={t('itemType', 'Item type')}
            title={t('itemType', 'Item type')}
          >
            <ControlledRadioButtonGroup
              control={control}
              name="isDrug"
              controllerName="isDrug"
              legendText=""
              invalid={!!errors.isDrug}
              invalidText={errors.isDrug && errors?.isDrug?.message}
              options={radioOptions} // Pass radioOptions directly
            />
          </FormGroup>
        )}
        {selectedItemType === StockItemType.PHARMACEUTICALS ? (
          <DrugSelector
            name="drugUuid"
            controllerName="drugUuid"
            control={control}
            title={t('pleaseSpecify', 'Please specify')}
            placeholder={t('chooseADrug', 'Choose a drug')}
            initialDrugName={stockItem?.drugName ?? stockItem?.conceptName ?? undefined}
            readOnly={!!stockItem}
            invalid={!!errors.drugUuid}
            invalidText={errors.drugUuid && errors?.drugUuid?.message}
          />
        ) : selectedItemType === StockItemType.NONE_PHARMACEUTICALS ? (
          <ConceptsSelector
            name="conceptUuid"
            controllerName="conceptUuid"
            control={control}
            title={t('pleaseSpecify', 'Please specify')}
            placeholder={t('chooseAnItem', 'Choose an item')}
            invalid={!!errors.drugUuid}
            invalidText={errors.drugUuid && errors?.drugUuid?.message}
          />
        ) : null}
        <ControlledTextInput
          id="commonName"
          name="commonName"
          control={control}
          controllerName="commonName"
          maxLength={255}
          size={'md'}
          value={`${stockItem?.commonName ?? ''}`}
          labelText={t('commonName', 'Common name') + ':'}
          invalid={!!errors.commonName}
          invalidText={errors.commonName && errors?.commonName?.message}
        />
        <ControlledTextInput
          id="acronym"
          maxLength={255}
          name="acronym"
          control={control}
          controllerName="acronym"
          size={'md'}
          labelText={t('abbreviation', 'Abbreviation') + ':'}
          invalid={!!errors.acronym}
          invalidText={errors.acronym && errors?.acronym?.message}
        />
        <div
          style={{
            display: 'grid',
            gridTemplateColumns: '1fr 1fr',
            justifyContent: 'center',
          }}
        >
          <FormGroup
            className="clear-margin-bottom"
            legendText={t('hasExpiration', 'Does the item expire?')}
            title={t('hasExpiration', 'Does the item expire?')}
          >
            <ControlledRadioButtonGroup
              name="hasExpiration"
              controllerName="hasExpiration"
              control={control}
              legendText=""
              invalid={!!errors.hasExpiration}
              invalidText={errors.hasExpiration && errors?.hasExpiration?.message}
              options={expirationOptions} // Pass expirationOptions directly
            />
          </FormGroup>

          {observableHasExpiration && (
            <FormGroup className="clear-margin-bottom" legendText={t('expirationNotice', 'Expiration Notice (days)')}>
              <ControlledNumberInput
                id="expiryNotice"
                name="expiryNotice"
                control={control}
                controllerName="expiryNotice"
                min={0}
                hideSteppers
                size="md"
                allowEmpty
                label=""
                invalid={!!errors.expiryNotice}
                invalidText={errors.expiryNotice && errors?.expiryNotice?.message}
              />
            </FormGroup>
          )}
        </div>
        <PreferredVendorSelector
          name="preferredVendorUuid"
          controllerName="preferredVendorUuid"
          control={control}
          title={t('whoIsThePreferredVendor', 'Who is the preferred vendor?')}
          placeholder={t('chooseVendor', 'Choose vendor')}
          invalid={!!errors.preferredVendorUuid}
          invalidText={errors.preferredVendorUuid && errors?.preferredVendorUuid?.message}
        />
        <StockItemCategorySelector
          name="categoryUuid"
          controllerName="categoryUuid"
          control={control}
          itemType={
            selectedItemType === StockItemType.PHARMACEUTICALS
              ? 'Drugs'
              : selectedItemType === StockItemType.NONE_PHARMACEUTICALS
              ? 'Non Drugs'
              : undefined
          }
          title={t('category', 'Category') + ':'}
          placeholder={t('chooseACategory', 'Choose a category')}
          invalid={!!errors.categoryUuid}
          invalidText={errors.categoryUuid && errors?.categoryUuid?.message}
        />
        {observableIsDrug && (
          <DispensingUnitSelector
            name="dispensingUnitUuid"
            controllerName="dispensingUnitUuid"
            control={control}
            title={t('dispensingUnit', 'Dispensing unit') + ':'}
            placeholder={t('dispensingUnitHolder', 'Choose a dispensing unit')}
            invalid={!!errors.dispensingUnitUuid}
            invalidText={errors.dispensingUnitUuid && errors?.dispensingUnitUuid?.message}
          />
        )}
        {observableIsDrug && stockItem && (
          <StockItemUnitsEdit control={control} formState={formState} stockItemUuid={stockItem?.uuid} />
        )}
      </Stack>
      <ButtonSet
        className={classNames(styles.buttonSet, {
          [styles.tablet]: isTablet,
          [styles.desktop]: !isTablet,
        })}
      >
        <Button kind="secondary" onClick={onCloseWorkspace} className={styles.button}>
          {getCoreTranslation('cancel')}
        </Button>
        <Button
          className={styles.button}
          kind="primary"
          name="save"
          onClick={handleSubmit(handleSave)}
          renderIcon={Save}
          type="button"
        >
          {formState.isSubmitting ? <InlineLoading /> : getCoreTranslation('save')}
        </Button>
      </ButtonSet>
    </form>
  );
};

export default StockItemDetails;
