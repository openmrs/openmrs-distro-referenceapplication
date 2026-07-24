import React, { type ChangeEvent, useEffect, useMemo, useState } from 'react';
import classNames from 'classnames';
import {
  Button,
  ButtonSet,
  Checkbox,
  CheckboxGroup,
  ComboBox,
  DatePicker,
  DatePickerInput,
  Form,
  FormGroup,
  InlineLoading,
  Select,
  SelectItem,
  Stack,
  Toggle,
} from '@carbon/react';
import { Save } from '@carbon/react/icons';
import { useTranslation } from 'react-i18next';
import {
  type DefaultWorkspaceProps,
  getCoreTranslation,
  restBaseUrl,
  showSnackbar,
  useLayoutType,
  useSession,
} from '@openmrs/esm-framework';
import {
  useRoles,
  useStockOperationTypes,
  useStockTagLocations,
  useUser,
} from '../../stock-lookups/stock-lookups.resource';
import { ResourceRepresentation } from '../../core/api/api';
import { type UserRoleScope } from '../../core/api/types/identity/UserRoleScope';
import { createOrUpdateUserRoleScope } from '../stock-user-role-scopes.resource';
import { type UserRoleScopeOperationType } from '../../core/api/types/identity/UserRoleScopeOperationType';
import { type UserRoleScopeLocation } from '../../core/api/types/identity/UserRoleScopeLocation';
import {
  DATE_PICKER_CONTROL_FORMAT,
  DATE_PICKER_FORMAT,
  formatForDatePicker,
  INVENTORY_ADMINISTRATOR_ROLE_UUID,
  INVENTORY_CLERK_ROLE_UUID,
  INVENTORY_DISPENSING_ROLE_UUID,
  INVENTORY_MANAGER_ROLE_UUID,
  INVENTORY_REPORTING_ROLE_UUID,
  today,
} from '../../constants';
import { type Role } from '../../core/api/types/identity/Role';
import { type StockOperationType } from '../../core/api/types/stockOperation/StockOperationType';
import { type User } from '../../core/api/types/identity/User';
import { handleMutate } from '../../utils';
import useSearchUser from '../../stock-operations/stock-operations-forms/hooks/useSearchUser';
import { useDebounce } from '../../core/hooks/debounce-hook';
import styles from './add-stock-user-role-scope.scss';

const MinDate: Date = today();

type AddStockUserRoleScopeProps = DefaultWorkspaceProps & {
  model?: UserRoleScope;
  editMode?: boolean;
};

const AddStockUserRoleScope: React.FC<AddStockUserRoleScopeProps> = ({ model, editMode, closeWorkspace }) => {
  const { t } = useTranslation();
  const currentUser = useSession();
  const [formModel, setFormModel] = useState<UserRoleScope>({ ...model });
  const isTablet = useLayoutType() === 'tablet';

  const [roles, setRoles] = useState<Role[]>([]);

  const loggedInUserUuid = currentUser?.user?.uuid;
  const [selectedUserUuid, setSelectedUserUuid] = useState<string | null>(null);
  const { data: user } = useUser(selectedUserUuid);

  // operation types
  const {
    types: { results: stockOperations },
    isLoading,
  } = useStockOperationTypes();

  // Server-side user search (fixes O3-4518: systems with many users)
  const { userList, setSearchString, isLoading: loadingUsers } = useSearchUser();

  const debouncedSearch = useDebounce((query: string) => {
    setSearchString(query?.trim() || null);
  }, 1000);

  // Stabilize the items list — only update when not loading to prevent flicker
  const [stableUserList, setStableUserList] = useState(userList ?? []);

  useEffect(() => {
    if (!loadingUsers && userList) {
      setStableUserList(userList);
    }
  }, [userList, loadingUsers]);

  const usersResults = useMemo(
    () => stableUserList.filter((item) => item.uuid !== loggedInUserUuid),
    [stableUserList, loggedInUserUuid],
  );

  // get roles
  const { isLoading: loadingRoles } = useRoles({
    v: ResourceRepresentation.Default,
  });

  /* Only load locations tagged to perform stock related activities.
     Unless a location is tag as main store, main pharmacy or dispensing, it will not be listed here.
   */
  const { stockLocations, isLoading: isLoadingStockLocations } = useStockTagLocations();
  const onEnabledChanged = (): void => {
    const isEnabled = !formModel?.enabled;
    setFormModel({ ...formModel, enabled: isEnabled });
  };

  const onPermanentChanged = (): void => {
    const isPermanent = !formModel?.permanent;
    setFormModel({
      ...formModel,
      permanent: isPermanent,
      activeFrom: undefined,
      activeTo: undefined,
    });
  };

  useEffect(() => {
    if (model?.userUuid) {
      setSelectedUserUuid(model.userUuid);
    }
  }, [model]);

  const onStockOperationTypeChanged = (event: React.ChangeEvent<HTMLInputElement>): void => {
    const operationType = formModel?.operationTypes?.find((x) => x.operationTypeUuid === event?.target?.value);
    if (operationType) {
      const newOperationTypes = [
        ...formModel.operationTypes.filter((x) => x.operationTypeUuid !== operationType?.operationTypeUuid),
      ];
      setFormModel({ ...formModel, operationTypes: newOperationTypes });
    } else {
      const stockOperationType = stockOperations?.find((x) => x.uuid === event?.target?.value);
      const operationType: UserRoleScopeOperationType = {
        operationTypeName: stockOperationType?.name,
        operationTypeUuid: stockOperationType?.uuid,
      } as unknown as UserRoleScopeOperationType;
      setFormModel({
        ...formModel,
        operationTypes: [...(formModel?.operationTypes ?? []), operationType],
      });
    }
  };

  const onLocationCheckBoxChanged = (event: ChangeEvent<HTMLInputElement>): void => {
    const selectedLocation = formModel?.locations?.find((x) => x.locationUuid === event?.target?.value);
    if (selectedLocation) {
      const newLocations = [
        ...(formModel?.locations?.filter((x) => x.locationUuid !== selectedLocation?.locationUuid) ?? []),
      ];
      setFormModel({ ...formModel, locations: newLocations });
    } else {
      const loc = stockLocations?.find((x) => x.id === event?.target?.value);
      const newLocation: UserRoleScopeLocation = {
        locationName: loc?.name,
        locationUuid: loc?.id,
        // The backend's permission check (StockManagementDao#getFlattenedUserRoleScopesByUser)
        // unconditionally requires enableDescendants = true for a location to match at all, even
        // for the exact same location with no children, so this must default to true rather than
        // false or the scope can never grant access via the UI.
        enableDescendants: true,
      } as unknown as UserRoleScopeLocation;
      const newLocations = [...(formModel?.locations ?? []), newLocation];
      setFormModel({ ...formModel, locations: newLocations });
    }
  };

  const onLocationDescendantsToggleChanged = (locationUuid: string, toggled: boolean): void => {
    const newLocations = (formModel?.locations ?? []).map((loc) =>
      loc.locationUuid === locationUuid ? { ...loc, enableDescendants: toggled } : loc,
    );
    setFormModel({ ...formModel, locations: newLocations });
  };

  const findCheckedLocation = (location: fhir.Location): UserRoleScopeLocation | null => {
    const result = formModel?.locations?.filter((x) => x.locationUuid === location.id);
    return result && result.length > 0 ? result[0] : null;
  };

  const onActiveDatesChange = (dates: Date[]): void => {
    setFormModel({ ...formModel, activeFrom: dates[0], activeTo: dates[1] });
  };

  const onUserChanged = (data: { selectedItem: User }) => {
    const stockRolesUUIDs = [
      INVENTORY_CLERK_ROLE_UUID,
      INVENTORY_MANAGER_ROLE_UUID,
      INVENTORY_DISPENSING_ROLE_UUID,
      INVENTORY_REPORTING_ROLE_UUID,
      INVENTORY_ADMINISTRATOR_ROLE_UUID,
    ];

    const filteredStockRoles = data.selectedItem?.roles
      .filter((role) => stockRolesUUIDs.includes(role.uuid))
      .filter((role) => role.uuid !== loggedInUserUuid);
    setFormModel({ ...formModel, userUuid: data.selectedItem?.uuid });
    setRoles(filteredStockRoles ?? []);
    setSelectedUserUuid(data?.selectedItem?.uuid);
  };

  const onRoleChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const rootLocations = stockLocations?.filter((x) => !x.id)?.map((x) => x.id);
    const filteredLocations =
      formModel?.locations?.filter(
        (x) => !rootLocations || rootLocations.length === 0 || !rootLocations.some((p) => p === x.locationUuid),
      ) ?? [];

    setFormModel({
      ...formModel,
      role: e.target.value,
      locations: filteredLocations,
    });
  };

  const isOperationChecked = (operationType: StockOperationType) => {
    return formModel?.operationTypes?.filter((x) => x.operationTypeUuid === operationType.uuid)?.length > 0;
  };

  const addStockUserRole = async (e) => {
    e.preventDefault();

    // Validation: ensure required fields are present
    if (!formModel?.userUuid) {
      showSnackbar({
        title: t('errorSavingUserRoleScope', 'Error Saving user role scope'),
        kind: 'error',
        isLowContrast: true,
        subtitle: t('userRequired', 'User is required'),
      });
      return;
    }

    if (!formModel?.role) {
      showSnackbar({
        title: t('errorSavingUserRoleScope', 'Error Saving user role scope'),
        kind: 'error',
        isLowContrast: true,
        subtitle: t('roleRequired', 'Role is required'),
      });
      return;
    }

    createOrUpdateUserRoleScope(formModel).then(
      (response) => {
        handleMutate(`${restBaseUrl}/stockmanagement/userrolescope`);
        showSnackbar({
          isLowContrast: true,
          title: t('addUserRole', 'Add User role'),
          kind: 'success',
          subtitle: t('successfullySaved', 'You have successfully saved user role scope'),
        });
        closeWorkspace();
      },
      (err) => {
        showSnackbar({
          title: t('errorSavingUserRoleScope', 'Error Saving user role scope'),
          kind: 'error',
          isLowContrast: true,
          subtitle: err?.message ?? err?.cause ?? t('unknownError', 'An unknown error occurred'),
        });
      },
    );
  };

  if (isLoading || loadingRoles || isLoadingStockLocations) {
    return (
      <InlineLoading status="active" iconDescription="Loading" description={t('loadingData', 'Loading data...')} />
    );
  }

  return (
    <Form className={styles.container} onSubmit={addStockUserRole}>
      <Stack className={styles.form} gap={5}>
        <div>
          <FormGroup legendText={t('user', 'User')}>
            <ComboBox
              id="userName"
              initialSelectedItem={usersResults.find((user) => user.uuid === model?.userUuid) ?? null}
              items={usersResults}
              itemToString={(item) => {
                if (!item || typeof item !== 'object') return '';
                const itemWithPerson = item as { person?: { display?: string }; display?: string };
                return `${itemWithPerson?.person?.display ?? itemWithPerson?.display ?? ''}`;
              }}
              titleText={t('user', 'User')}
              onChange={onUserChanged}
              onInputChange={debouncedSearch}
              placeholder={t('searchUsers', 'Search users')}
              shouldFilterItem={() => true}
              size="md"
            />
          </FormGroup>
        </div>
        <Select
          id="select-role"
          labelText={t('role', 'Role')}
          name="role"
          onChange={onRoleChange}
          value={formModel.role}
        >
          <SelectItem value={''} text={t('chooseARole', 'Choose a role')} />
          {editMode ? (
            <SelectItem key={formModel?.role} value={formModel?.role} text={formModel?.role} />
          ) : (
            (user?.roles ?? roles)?.map((role) => {
              return <SelectItem key={role.display} value={role.display} text={role.display} />;
            })
          )}
        </Select>
        <CheckboxGroup className={styles.checkboxGrid} legendText="">
          <Checkbox
            checked={formModel?.enabled}
            id="chk-userEnabled"
            labelText={t('enabled', 'Enabled')}
            onChange={onEnabledChanged}
            value={model?.enabled ? 'true' : 'false'}
          />
          <Checkbox
            checked={formModel?.permanent}
            id="chk-userPermanent"
            labelText={t('permanent', 'Permanent')}
            name="isPermanent"
            onChange={onPermanentChanged}
            value={model?.permanent ? 'true' : 'false'}
          />

          {!formModel?.permanent && (
            <DatePicker
              dateFormat={DATE_PICKER_CONTROL_FORMAT}
              datePickerType="range"
              light
              locale="en"
              minDate={formatForDatePicker(MinDate)}
              onChange={onActiveDatesChange}
            >
              <DatePickerInput
                id="date-picker-input-id-start"
                labelText={t('activeFrom', 'Active From')}
                placeholder={DATE_PICKER_FORMAT}
              />
              <DatePickerInput
                id="date-picker-input-id-finish"
                labelText={t('activeTo', 'Active To')}
                placeholder={DATE_PICKER_FORMAT}
              />
            </DatePicker>
          )}
        </CheckboxGroup>
        <FormGroup legendText={t('stockOperations', 'Stock operations')}>
          <span className={styles.subTitle}>
            {t('roleDescription', 'The role will be applicable to only selected stock operations.')}
          </span>
        </FormGroup>
        <CheckboxGroup className={styles.checkboxGrid} legendText="">
          {stockOperations?.length > 0 &&
            stockOperations.map((type) => {
              return (
                <div className={styles.flexRow}>
                  <Checkbox
                    checked={isOperationChecked(type)}
                    className={styles.checkbox}
                    id={type.uuid}
                    labelText={type.name}
                    onChange={(event) => onStockOperationTypeChanged(event)}
                    value={type.uuid}
                  />
                </div>
              );
            })}
        </CheckboxGroup>
        <FormGroup legendText={t('locations', 'Locations')}>
          <span className={styles.subTitle}>
            {t('toggleMessage', 'Use the toggle to apply this scope to the locations under the selected location.')}
          </span>
        </FormGroup>
        <CheckboxGroup className={styles.checkboxGrid} legendText="">
          {stockLocations?.length > 0 &&
            stockLocations.map((type) => {
              const checkedLocation = findCheckedLocation(type);

              const getToggledValue = (locationUuid) => {
                const location = checkedLocation?.locationUuid === locationUuid ? checkedLocation : null;
                return location?.enableDescendants === true;
              };

              return (
                <div className={styles.flexRow}>
                  <Checkbox
                    checked={checkedLocation != null}
                    className={styles.checkbox}
                    id={`chk-loc-child-${type.id}`}
                    key={`chk-loc-child-key-${type.id}`}
                    labelText={type.name}
                    name="location"
                    onChange={(event) => onLocationCheckBoxChanged(event)}
                    value={type.id}
                  />
                  {checkedLocation && (
                    <Toggle
                      className={styles.toggle}
                      hideLabel
                      id={`tg-loc-child-${type.id}`}
                      key={`tg-loc-child-key-${type.id}`}
                      toggled={getToggledValue(type.id)}
                      onToggle={(toggled) => onLocationDescendantsToggleChanged(type.id, toggled)}
                      size="sm"
                    />
                  )}
                </div>
              );
            })}
        </CheckboxGroup>
      </Stack>
      <ButtonSet
        className={classNames(styles.buttonSet, {
          [styles.tablet]: isTablet,
          [styles.desktop]: !isTablet,
        })}
      >
        <Button kind="secondary" onClick={() => closeWorkspace()} className={styles.button}>
          {getCoreTranslation('cancel')}
        </Button>
        <Button type="submit" className={styles.button} renderIcon={Save}>
          {getCoreTranslation('save')}
        </Button>
      </ButtonSet>
    </Form>
  );
};

export default AddStockUserRoleScope;
