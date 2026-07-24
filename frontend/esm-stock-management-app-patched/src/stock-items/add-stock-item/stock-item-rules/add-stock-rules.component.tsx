import React, { type ChangeEvent, useCallback, useEffect, useState } from 'react';
import classNames from 'classnames';
import { useTranslation } from 'react-i18next';
import {
  Button,
  ButtonSet,
  Checkbox,
  CheckboxGroup,
  Form,
  FormGroup,
  Select,
  SelectItem,
  TextInput,
} from '@carbon/react';
import { type DefaultWorkspaceProps, getCoreTranslation, showSnackbar, useLayoutType } from '@openmrs/esm-framework';
import { createOrUpdateStockRule } from './stock-rules.resource';
import { ResourceRepresentation } from '../../../core/api/api';
import { type StockItemInventoryFilter, useStockItemPackagingUOMs } from '../../stock-items.resource';
import { type StockRule } from '../../../core/api/types/stockItem/StockRule';
import { useRoles, useStockTagLocations } from '../../../stock-lookups/stock-lookups.resource';
import styles from './add-stock-rules.scss';

interface AddStockRuleProps extends Partial<DefaultWorkspaceProps> {
  model?: StockRule;
  stockItemUuid?: string;
}

const StockRulesAddOrUpdate: React.FC<AddStockRuleProps> = ({ model, stockItemUuid, closeWorkspace }) => {
  const { t } = useTranslation();
  const isTablet = useLayoutType() === 'tablet';
  const [stockItemFilter, setStockItemFilter] = useState<StockItemInventoryFilter>({
    startIndex: 0,
    v: ResourceRepresentation.Default,
    q: null,
    totalCount: true,
  });

  useEffect(() => {
    setStockItemFilter({
      startIndex: 0,
      v: ResourceRepresentation.Default,
      totalCount: true,
      stockItemUuid: stockItemUuid,
    });
  }, [stockItemUuid]);

  const { items: dispensingUnits } = useStockItemPackagingUOMs(stockItemFilter);

  const { stockLocations } = useStockTagLocations();

  //Roles
  const { items: rolesData } = useRoles({ v: ResourceRepresentation.Default });

  const [formModel, setFormModel] = useState<StockRule>({
    enabled: true,
    ...model,
  });

  useEffect(() => {
    if (model != null && Object.keys(model).length != 0) {
      // To prevent editing properties like date created
      const { ...rest } = model;
      const tmpFormModel = { ...rest };
      setFormModel(tmpFormModel);
    }
  }, [model]);

  const onNameChanged = (evt: React.ChangeEvent<HTMLInputElement>): void => {
    if (model) {
      model.name = evt.target.value;
    }
    setFormModel({ ...formModel, name: evt.target.value });
  };

  const onQuantityChanged = (evt: React.ChangeEvent<HTMLInputElement>): void => {
    if (model) {
      model.quantity = Number(evt.target.value);
    }
    setFormModel({ ...formModel, quantity: Number(evt.target.value) });
  };

  const onEvaluationFrequencyChanged = (evt: React.ChangeEvent<HTMLInputElement>): void => {
    if (model) {
      model.evaluationFrequency = Number(evt.target.value);
    }
    setFormModel({
      ...formModel,
      evaluationFrequency: Number(evt.target.value),
    });
  };

  const onActionFrequencyChanged = (evt: React.ChangeEvent<HTMLInputElement>): void => {
    if (model) {
      model.actionFrequency = Number(evt.target.value);
    }
    setFormModel({ ...formModel, actionFrequency: Number(evt.target.value) });
  };

  const onLocationChange = (evt: ChangeEvent<HTMLSelectElement>) => {
    const selectedLocation = stockLocations.find((x) => x.id === evt.target.value);
    setFormModel({ ...formModel, locationUuid: selectedLocation.id });
  };

  const onQuantityUnitChange = (evt: ChangeEvent<HTMLSelectElement>) => {
    const selectedQuantityUnit = dispensingUnits?.results.find((x) => x.uuid === evt.target.value);
    setFormModel({
      ...formModel,
      stockItemPackagingUOMUuid: selectedQuantityUnit?.uuid,
    });
  };

  const onAlertRoleChange = (evt: ChangeEvent<HTMLSelectElement>) => {
    const selectedAlertRole = rolesData?.results.find((x) => x.display === evt.target.value);
    setFormModel({ ...formModel, alertRole: selectedAlertRole.display });
  };

  const onMailRoleChange = (evt: ChangeEvent<HTMLSelectElement>) => {
    const selectedMailRole = rolesData?.results.find((x) => x.display === evt.target.value);
    console.warn('Setting mail role to: ', selectedMailRole);
    setFormModel({ ...formModel, mailRole: selectedMailRole.display });
  };

  const onEnabledChanged = (): void => {
    const isEnabled = !formModel?.enabled;
    setFormModel({ ...formModel, enabled: isEnabled });
  };

  const onAppliesToChildrenChanged = (): void => {
    const enableDescendants = !formModel?.enableDescendants;
    setFormModel({ ...formModel, enableDescendants: enableDescendants });
  };

  const onFormSubmit = useCallback(
    (event) => {
      event.preventDefault();
      if (model) {
        formModel.uuid = model.uuid;
      }
      if (stockItemUuid) {
        formModel.stockItemUuid = stockItemUuid;
      }
      createOrUpdateStockRule(formModel)
        .then(
          () => {
            showSnackbar({
              isLowContrast: true,
              title: t('addedRule', 'Add rule'),
              kind: 'success',
              subtitle: t('stockRuleAddedSuccessfully', 'Stock rule added successfully'),
            });
            closeWorkspace?.();
          },
          (error) => {
            showSnackbar({
              title: t('errorAddingRule', 'Error adding a rule'),
              kind: 'error',
              isLowContrast: true,
              subtitle: error?.message,
            });
          },
        )
        .catch();
    },
    [formModel, model, t, stockItemUuid, closeWorkspace],
  );

  return (
    <Form onSubmit={onFormSubmit} className={styles.formContainer}>
      <div>
        <FormGroup legendText={t('ruleConfiguration', 'Rule configuration')}>
          <section className={styles.section}>
            <section className={styles.section}>
              <Select
                name="location"
                className="select-field"
                labelText={t('location', 'Location')}
                id="location"
                value={formModel?.locationUuid ? formModel.locationUuid : ''}
                onChange={onLocationChange}
              >
                <SelectItem disabled hidden value="" text={t('chooseLocation', 'Choose the location')} />
                {stockLocations?.map((location) => {
                  return <SelectItem key={location.id} value={location.id} text={location.name} />;
                })}
              </Select>
            </section>

            <section className={styles.section}>
              <TextInput
                id="name"
                type="text"
                labelText={t('ruleName', 'Rule name')}
                size="md"
                onChange={onNameChanged}
                value={model?.name}
                placeholder="e.g Panado Alert"
              />
            </section>

            <section className={styles.section}>
              <Select
                name="quantityUnit"
                className="select-field"
                labelText={t('quantityUnit', 'Quantity unit')}
                id="quantityUnit"
                value={formModel?.stockItemPackagingUOMUuid ? formModel.stockItemPackagingUOMUuid : ''}
                onChange={onQuantityUnitChange}
              >
                <SelectItem disabled hidden value="" text={t('chooseQuantityUnit', 'Choose the Unit of Quantity')} />
                {dispensingUnits?.results?.map((stockItemPackagingUOMUuid) => {
                  return (
                    <SelectItem
                      key={stockItemPackagingUOMUuid.uuid}
                      value={stockItemPackagingUOMUuid.uuid}
                      text={stockItemPackagingUOMUuid.packagingUomName}
                    />
                  );
                })}
              </Select>
            </section>

            <section className={styles.section}>
              <TextInput
                id="quantity"
                type="number"
                labelText={t('quantityThreshold', 'Quantity threshold')}
                size="md"
                onChange={onQuantityChanged}
                value={model?.quantity}
                placeholder="e.g 30 Boxes"
              />
            </section>
          </section>
        </FormGroup>

        <FormGroup legendText={t('notifications', 'Notifications')}>
          <section className={styles.section}>
            <Select
              name="alertRole"
              className="select-field"
              labelText={t('alertRole', 'Alert role')}
              id="alertRole"
              value={formModel?.alertRole ? formModel.alertRole : ''}
              onChange={onAlertRoleChange}
            >
              <SelectItem disabled hidden value="" text={t('chooseAlertRole', 'Choose an Alert Role')} />
              {rolesData?.results?.map((alertRole) => {
                return <SelectItem key={alertRole.display} value={alertRole.display} text={alertRole.display} />;
              })}
            </Select>
          </section>
          <section className={styles.section}>
            <Select
              name="mailRole"
              className="select-field"
              labelText={t('mailRole', 'Mail role')}
              id="mailRole"
              value={formModel?.mailRole ? formModel.mailRole : ''}
              onChange={onMailRoleChange}
            >
              <SelectItem disabled hidden value="" text={t('chooseMailRole', 'Choose a Mail Role')} />
              {rolesData?.results?.map((mailRole) => {
                return <SelectItem key={mailRole.display} value={mailRole.display} text={mailRole.display} />;
              })}
            </Select>
          </section>
          <section className={styles.section}>
            <TextInput
              id="evaluationFrequency"
              type="number"
              labelText={t('evaluationFrequency', 'Frequency Check (Minutes)')}
              size="md"
              onChange={onEvaluationFrequencyChanged}
              value={model?.evaluationFrequency}
              placeholder="e.g 30 Minutes"
            />
            <TextInput
              id="actionFrequency"
              type="number"
              labelText={t('actionFrequency', 'Notification Frequency (Minutes)')}
              size="md"
              onChange={onActionFrequencyChanged}
              value={model?.actionFrequency}
              placeholder="e.g 3600 Minutes"
            />
          </section>
        </FormGroup>

        <div
          style={{
            display: 'grid',
            gridTemplateColumns: '1fr 1fr',
            justifyContent: 'center',
          }}
        >
          <FormGroup className="clear-margin-bottom" legendText={t('enabled', 'Enabled')}>
            <CheckboxGroup className={styles.checkboxGrid} legendText="">
              <Checkbox
                onChange={onEnabledChanged}
                checked={formModel?.enabled}
                labelText={`Enabled ?`}
                value={model?.enabled ? 'true' : 'false'}
                id="chk-ruleEnabled"
              />
            </CheckboxGroup>
          </FormGroup>
          <FormGroup className="clear-margin-bottom" legendText={t('scope', 'Scope')}>
            <CheckboxGroup className={styles.checkboxGrid} legendText="">
              <Checkbox
                onChange={onAppliesToChildrenChanged}
                name="appliesToChildren"
                checked={formModel?.enableDescendants}
                value={model?.enableDescendants ? 'true' : 'false'}
                labelText={`Applies to child locations?`}
                id="chk-ruleAppliesToChildren"
              />
            </CheckboxGroup>
          </FormGroup>
        </div>

        <div>
          This stock rule will be evaluated by checking if the stock quantities have lowered to the threshold or below
          and a notification will be sent to the personnel with the specified role in the given location. The
          notification will only be sent once per specified notification frequency.
        </div>
      </div>
      <ButtonSet
        className={classNames(styles.buttonSet, {
          [styles.tablet]: isTablet,
          [styles.desktop]: !isTablet,
        })}
      >
        <Button kind="secondary" onClick={() => closeWorkspace()} className={styles.button}>
          {getCoreTranslation('cancel')}
        </Button>
        <Button type="submit" className={styles.button}>
          {getCoreTranslation('save')}
        </Button>
      </ButtonSet>
    </Form>
  );
};

export default StockRulesAddOrUpdate;
