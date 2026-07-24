import React, { useEffect, useMemo, useState } from 'react';
import {
  Button,
  DataTable,
  DataTableSkeleton,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
} from '@carbon/react';
import { Save } from '@carbon/react/icons';
import { zodResolver } from '@hookform/resolvers/zod';
import { FormProvider, useForm, useFormContext } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import { getCoreTranslation, restBaseUrl, showSnackbar } from '@openmrs/esm-framework';
import { createStockItemPackagingUnit, updateStockItemPackagingUnit } from '../../stock-items.resource';
import { handleMutate } from '../../../utils';
import { type PackageUnitFormData, packageUnitSchema } from './validationSchema';
import { type StockItemPackagingUOMDTO } from '../../../core/api/types/stockItem/StockItemPackagingUOM';
import { useStockItemPackageUnitsHook } from './packaging-units.resource';
import ControlledNumberInput from '../../../core/components/carbon/controlled-number-input.component';
import DeletePackagingUnitActionButton from './delete-packaging-unit-action-button.component';
import PackagingUnitsConceptSelector from '../packaging-units-concept-selector/packaging-units-concept-selector.component';
import { type CustomTableHeader, type CustomTableRow } from '../../../core/components/table/types';
import styles from './packaging-units.scss';

interface PackagingUnitsProps {
  isEditing?: boolean;
  onSubmit?: () => void;
  stockItemUuid: string;
  handleTabChange: (index) => void;
}

const PackagingUnits: React.FC<PackagingUnitsProps> = ({ stockItemUuid, handleTabChange }) => {
  const { items, isLoading, setStockItemUuid, mutate } = useStockItemPackageUnitsHook();

  const [packagingUnits, setPackagingUnits] = useState<StockItemPackagingUOMDTO[]>(items);

  const [newUnit, setNewUnit] = useState<{
    factor: number;
    packagingUomUuid: string;
    packagingUomName: string;
  }>({
    packagingUomUuid: undefined,
    factor: 0,
    packagingUomName: '',
  });

  useEffect(() => {
    setStockItemUuid(stockItemUuid);
  }, [stockItemUuid, setStockItemUuid]);

  useEffect(() => {
    setPackagingUnits(items);
  }, [items]);

  const { t } = useTranslation();
  const tableHeaders = useMemo<CustomTableHeader[]>(
    () => [
      {
        key: 'packaging',
        header: t('packagingUnit', 'Packaging Unit'),
        styles: { width: '50%' },
      },
      {
        key: 'quantity',
        header: t('packSize', 'Pack Size'),
        styles: { width: '50%' },
      },
      {
        key: 'action',
        header: t('action', 'Actions'),
        styles: { width: '50%' },
      },
    ],
    [t],
  );

  const packageUnitForm = useForm<PackageUnitFormData>({
    defaultValues: {},
    mode: 'all',
    resolver: zodResolver(packageUnitSchema),
  });

  const handleSavePackageUnits = () => {
    const { reset } = packageUnitForm;

    const newPayload = newUnit
      ? {
          factor: newUnit.factor,
          packagingUomUuid: newUnit.packagingUomUuid,
          stockItemUuid,
        }
      : null;

    // Filter changed units
    const updatedUnits = packagingUnits.filter((unit) => {
      const originalUnit = items.find((item) => item.uuid === unit.uuid);
      return originalUnit && originalUnit.factor !== unit.factor;
    });

    // Create new unit
    const createPromises = newPayload.packagingUomUuid
      ? createStockItemPackagingUnit(newPayload).then(
          () => {
            showSnackbar({
              title: t('savePackingUnitTitle', 'Package Unit'),
              subtitle: t('savePackingUnitMessage', 'Package Unit saved successfully'),
              kind: 'success',
            });
            setNewUnit({
              factor: 0,
              packagingUomUuid: undefined,
              packagingUomName: '',
            }); // Reset new unit
          },
          () => {
            showSnackbar({
              title: t('savePackagingUnitErrorTitle', 'Package Unit'),
              subtitle: t('savePackagingUnitErrorMessage', 'Error saving package unit'),
              kind: 'error',
            });
          },
        )
      : Promise.resolve({ status: 'no-create' });

    // Update existing units
    const updatePromises = updatedUnits.map((unit) =>
      updateStockItemPackagingUnit(unit, unit.uuid).then(
        () => {
          showSnackbar({
            title: t('updatePackingUnitTitle', 'Package Unit'),
            subtitle: t('updatePackingUnitMessage', 'Package Unit {{ name }} updated successfully', {
              name: unit.packagingUomName,
            }),
            kind: 'success',
          });
        },
        () => {
          showSnackbar({
            title: t('updatePackagingUnitErrorTitle', 'Package Unit'),
            subtitle: t('updatePackagingUnitErrorMessage', 'Error updating package unit {{name}}', {
              name: unit.packagingUomName,
            }),
            kind: 'error',
          });
        },
      ),
    );

    // Wait for all requests to complete
    Promise.all([createPromises, ...updatePromises]).then(() => {
      mutate();
      handleMutate(`${restBaseUrl}/stockmanagement/stockitem`);
      reset();
      handleTabChange(0);
    });
  };
  const handleCancelPackagingUnits = () => {
    handleTabChange(0);
  };
  const handleNewUnitFactorChange = (value: string | number) => {
    setNewUnit({
      ...newUnit,
      factor: Number(value),
    });
  };

  const handleNewUnitPackageUnitChange = (unit: { uuid: string; display: string }) => {
    setNewUnit({
      ...newUnit,
      packagingUomUuid: unit.uuid,
      packagingUomName: unit.display,
    });
  };

  const onFactorFieldUpdate = (row: StockItemPackagingUOMDTO, value: string | number) => {
    const qtyValue = typeof value === 'number' ? value : parseFloat(value);

    setPackagingUnits((prevState) =>
      prevState.map((item) => (item.uuid === row.uuid ? { ...item, factor: qtyValue } : item)),
    );
  };

  if (isLoading) return <DataTableSkeleton showHeader={false} rowCount={5} columnCount={5} zebra />;

  return (
    <FormProvider {...packageUnitForm}>
      <DataTable
        rows={
          [
            ...items.map((item, idx) => ({ ...item, id: item.uuid || `item-${idx}` })),
            { id: 'new-row' },
          ] as CustomTableRow[]
        }
        headers={tableHeaders as any}
        isSortable={false}
        useZebraStyles
      >
        {({ headers, getHeaderProps, getTableProps }) => (
          <TableContainer className={styles.packagingTableContainer}>
            <Table {...getTableProps()} className={styles.packingTable}>
              <TableHead>
                <TableRow>
                  {headers.map((header) => {
                    const customHeader = header as CustomTableHeader;
                    return (
                      <TableHeader
                        {...getHeaderProps({
                          header,
                          isSortable: false,
                        })}
                        style={customHeader.styles}
                        key={header.key}
                      >
                        {typeof customHeader.header === 'object' &&
                        customHeader.header !== null &&
                        'content' in customHeader.header
                          ? (customHeader.header.content as React.ReactNode)
                          : (customHeader.header as React.ReactNode)}
                      </TableHeader>
                    );
                  })}
                  <TableHeader style={{ width: '70%' }} />
                </TableRow>
              </TableHead>
              <TableBody className={styles.packingTableBody}>
                {items?.map((row: StockItemPackagingUOMDTO, index) => (
                  <PackagingUnitRow
                    row={row}
                    id={`${index}-${row?.uuid}`}
                    onChange={(value) => onFactorFieldUpdate(row, value)}
                  />
                ))}
                <PackagingUnitRow
                  row={newUnit || {}}
                  id="new-package-unit"
                  isEditing
                  onChangePackageUnit={(value) => handleNewUnitPackageUnitChange(value)}
                  onChange={(value) => handleNewUnitFactorChange(value)}
                />
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </DataTable>
      <div className={styles.packageUnitsBtn}>
        <Button kind="secondary" onClick={handleCancelPackagingUnits}>
          {getCoreTranslation('cancel')}
        </Button>
        <Button
          name="save"
          type="submit"
          className="submitButton"
          onClick={handleSavePackageUnits}
          kind="primary"
          renderIcon={Save}
        >
          {getCoreTranslation('save')}
        </Button>
      </div>
    </FormProvider>
  );
};

export default PackagingUnits;

const PackagingUnitRow: React.FC<{
  isEditing?: boolean;
  row: StockItemPackagingUOMDTO;
  id?: string;
  onChange?: (value: string | number) => void;
  onChangePackageUnit?: (value: { uuid: string; display: string }) => void;
}> = ({ isEditing, row, id, onChange, onChangePackageUnit }) => {
  const {
    control,
    formState: { errors },
  } = useFormContext();
  const minPackagingQuantity = 0;

  return (
    <>
      <TableRow>
        <TableCell>
          {isEditing ? (
            <PackagingUnitsConceptSelector
              row={row}
              controllerName={'packagingUomUuid'}
              name="packagingUomUuid"
              placeholder="Filter"
              control={control}
              onPackageUnitChange={(concept) => onChangePackageUnit(concept)}
              invalid={!!errors.packagingUomUuid}
            />
          ) : (
            (!isEditing || !row.uuid.startsWith('new-item')) && row?.packagingUomName
          )}
        </TableCell>
        <TableCell>
          <ControlledNumberInput
            row={row}
            controllerName="factor"
            name="factor"
            min={minPackagingQuantity}
            control={control}
            id={id}
            invalid={!!errors.factor}
            hideSteppers={true}
            onChange={(e, state) => onChange(state.value)}
          />
        </TableCell>
        <TableCell>
          <DeletePackagingUnitActionButton row={row} />
        </TableCell>
      </TableRow>
    </>
  );
};
