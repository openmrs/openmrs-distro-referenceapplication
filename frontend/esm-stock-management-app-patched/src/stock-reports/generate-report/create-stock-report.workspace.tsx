import React, { useEffect, useMemo, useState } from 'react';
import classNames from 'classnames';
import { debounce } from 'lodash-es';
import { useTranslation } from 'react-i18next';
import {
  Button,
  ButtonSet,
  Checkbox,
  ComboBox,
  DatePicker,
  DatePickerInput,
  Form,
  FormGroup,
  InlineLoading,
  NumberInput,
  RadioButton,
  RadioButtonGroup,
  Select,
  SelectItem,
  Stack,
} from '@carbon/react';
import {
  type ConfigObject,
  type DefaultWorkspaceProps,
  getCoreTranslation,
  openmrsFetch,
  restBaseUrl,
  showSnackbar,
  useConfig,
  useLayoutType,
} from '@openmrs/esm-framework';
import { Controller, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import {
  getParamDefaultLimit,
  getReportEndDateLabel,
  getReportLimitLabel,
  getReportStartDateLabel,
  ReportParameter,
} from '../ReportType';
import { DATE_PICKER_CONTROL_FORMAT, DATE_PICKER_FORMAT, formatForDatePicker, today } from '../../constants';
import { BatchJobTypeReport } from '../../core/api/types/BatchJob';
import { createBatchJob } from '../../stock-batch/stock-batch.resource';
import { formatDisplayDate } from '../../core/utils/datetimeUtils';
import { handleMutate } from '../../utils';
import { type Concept } from '../../core/api/types/concept/Concept';
import { type Patient } from '../../core/api/types/identity/Patient';
import { type StockItemDTO } from '../../core/api/types/stockItem/StockItem';
import { type StockReportSchema, reportSchema } from '../report-validation-schema';
import { useConcept, useStockTagLocations } from '../../stock-lookups/stock-lookups.resource';
import { ResourceRepresentation } from '../../core/api/api';
import { useReportTypes } from '../stock-reports.resource';
import styles from './create-stock-report.scss';

type CreateReportProps = DefaultWorkspaceProps & {
  model?: ReportModel;
};

export interface ReportModel {
  reportSystemName?: string;
  reportName?: string;
  parameters?: string[];
  date?: Date;
  startDate?: Date;
  endDate?: Date;
  stockItemCategory?: string;
  stockItemCategoryConceptUuid?: string;
  location?: string;
  locationUuid?: string;
  childLocations: boolean;
  stockSourceUuid?: string;
  stockSource?: string;
  stockSourceDestinationUuid?: string;
  stockSourceDestination?: string;
  inventoryGroupBy?: string;
  inventoryGroupByName?: string;
  maxReorderLevelRatio?: number;
  stockItemUuid?: string;
  stockItemName?: string;
  patientUuid?: string;
  patientName?: string;
  limit?: number | null;
  mostLeastMoving?: string;
  mostLeastMovingName?: string;
  fullFillment?: string[];
}

const CreateReport: React.FC<CreateReportProps> = ({ model, closeWorkspace }) => {
  const { t } = useTranslation();
  const { stockItemCategoryUUID } = useConfig<ConfigObject>();
  const isTablet = useLayoutType() === 'tablet';

  const { reportTypes, isLoading } = useReportTypes();
  const { stockLocations } = useStockTagLocations();
  const { items } = useConcept(stockItemCategoryUUID);
  const [displayDate, setDisplayDate] = useState<boolean>(false);
  const [displayStartDate, setDisplayStartDate] = useState<boolean>(false);
  const [displayEndDate, setDisplayEndDate] = useState<boolean>(false);
  const [displayStockItemCategory, setDisplayStockItemCategory] = useState<boolean>(false);
  const [displayLocation, setDisplayLocation] = useState<boolean>(false);
  const [displayChildLocations, setDisplayChildLocations] = useState<boolean>(false);
  const [displayStockSource, setDisplayStockSource] = useState<boolean>(false);
  const [displayStockSourceDestination, setDisplayStockSourceDestination] = useState<boolean>(false);
  const [displayInventoryGroupBy, setDisplayInventoryGroupBy] = useState<boolean>(false);
  const [displayMaxReorderLevelRatio, setDisplayMaxReorderLevelRatio] = useState<boolean>(false);
  const [displayPatient, setDisplayPatient] = useState<boolean>(false);
  const [displayStockItem, setDisplayStockItem] = useState<boolean>(false);
  const [displayLimit, setDisplayLimit] = useState<boolean>(false);
  const [displayMostLeastMoving, setDisplayMostLeastMoving] = useState<boolean>(false);
  const [displayFulfillment, setDisplayFulfillment] = useState<boolean>(false);
  const [selectedReportName, setSelectedReportName] = useState<string>('');

  const [stockItemSearchResults, setStockItemSearchResults] = useState<StockItemDTO[]>([]);
  const [patientSearchResults, setPatientSearchResults] = useState<Patient[]>([]);

  const handleReportNameChange = (name: string) => {
    setSelectedReportName(name);
  };

  useEffect(() => {
    let hasResetParameters = false;
    if (selectedReportName) {
      const reportType = Array.isArray(reportTypes)
        ? reportTypes.find((p) => p.name === selectedReportName)
        : undefined;
      if (reportType) {
        setDisplayDate(reportType.parameters?.some((p) => p === ReportParameter.Date));
        setDisplayStartDate(reportType.parameters?.some((p) => p === ReportParameter.StartDate));
        setDisplayEndDate(reportType.parameters?.some((p) => p === ReportParameter.EndDate));
        setDisplayStockItemCategory(reportType.parameters?.some((p) => p === ReportParameter.StockItemCategory));
        setDisplayLocation(reportType.parameters?.some((p) => p === ReportParameter.Location));
        setDisplayChildLocations(reportType.parameters?.some((p) => p === ReportParameter.ChildLocations));
        setDisplayStockSource(reportType.parameters?.some((p) => p === ReportParameter.StockSource));
        setDisplayStockSourceDestination(
          reportType.parameters?.some((p) => p === ReportParameter.StockSourceDestination),
        );
        setDisplayInventoryGroupBy(reportType.parameters?.some((p) => p === ReportParameter.InventoryGroupBy));
        setDisplayMaxReorderLevelRatio(reportType.parameters?.some((p) => p === ReportParameter.MaxReorderLevelRatio));
        setDisplayStockItem(reportType.parameters?.some((p) => p === ReportParameter.StockItem));
        setDisplayPatient(reportType.parameters?.some((p) => p === ReportParameter.Patient));
        setDisplayLimit(reportType.parameters?.some((p) => p === ReportParameter.Limit));
        setDisplayMostLeastMoving(reportType.parameters?.some((p) => p === ReportParameter.MostLeastMoving));
        setDisplayFulfillment(reportType.parameters?.some((p) => p === ReportParameter.Fullfillment));
        hasResetParameters = true;
      }
    }
    if (!hasResetParameters) {
      setDisplayDate(false);
      setDisplayStartDate(false);
      setDisplayEndDate(false);
      setDisplayStockItemCategory(false);
      setDisplayLocation(false);
      setDisplayChildLocations(false);
      setDisplayStockSource(false);
      setDisplayStockSourceDestination(false);
      setDisplayInventoryGroupBy(false);
      setDisplayMaxReorderLevelRatio(false);
      setDisplayStockItem(false);
      setDisplayPatient(false);
      setDisplayLimit(false);
      setDisplayMostLeastMoving(false);
      setDisplayFulfillment(false);
    }
  }, [selectedReportName, reportTypes]);

  const stockItemCategories = useMemo(() => {
    return [
      {
        display: 'All Categories',
        name: 'All Categories',
        uuid: '',
      } as unknown as Concept,
      ...((items && items?.answers?.length > 0 ? items?.answers : items?.setMembers) ?? []),
    ];
  }, [items]);

  const searchStockItems = useMemo(
    () =>
      debounce(async (searchTerm: string) => {
        if (!searchTerm || searchTerm.trim().length === 0) {
          setStockItemSearchResults([]);
          return;
        }

        try {
          const apiUrl = `${restBaseUrl}/stockmanagement/stockitem?q=${encodeURIComponent(searchTerm.trim())}&v=${
            ResourceRepresentation.Default
          }&limit=10&totalCount=true`;
          const response = await openmrsFetch<{ results: StockItemDTO[] }>(apiUrl);

          if (response.data?.results) {
            setStockItemSearchResults(response.data.results);
          } else {
            setStockItemSearchResults([]);
          }
        } catch (error) {
          showSnackbar({
            title: t('error', 'Error'),
            kind: 'error',
            subtitle: t('stockItemSearchFailed', 'Stock item search failed'),
          });
          setStockItemSearchResults([]);
        }
      }, 300),
    [t],
  );

  useEffect(() => {
    return () => searchStockItems.cancel();
  }, [searchStockItems]);

  const searchPatients = useMemo(
    () =>
      debounce(async (searchTerm: string) => {
        if (!searchTerm || searchTerm.trim().length === 0) {
          setPatientSearchResults([]);
          return;
        }

        try {
          const apiUrl = `${restBaseUrl}/patient?q=${encodeURIComponent(searchTerm.trim())}&v=${
            ResourceRepresentation.Default
          }&limit=10&totalCount=true`;
          const response = await openmrsFetch<{ results: Patient[] }>(apiUrl);

          if (response.data?.results) {
            setPatientSearchResults(response.data.results);
          } else {
            setPatientSearchResults([]);
          }
        } catch (error) {
          showSnackbar({
            title: t('error', 'Error'),
            kind: 'error',
            subtitle: t('patientSearchFailed', 'Patient search failed'),
          });
          setPatientSearchResults([]);
        }
      }, 300),
    [t],
  );

  useEffect(() => {
    return () => searchPatients.cancel();
  }, [searchPatients]);

  const {
    handleSubmit,
    control,
    formState: { errors, defaultValues, isDirty },
    setValue,
  } = useForm<StockReportSchema>({
    mode: 'all',
    resolver: zodResolver(reportSchema),
  });

  if (isLoading) {
    return <InlineLoading status="active" iconDescription="Loading" description="Loading data..." />;
  }

  const handleSave = async (report: StockReportSchema) => {
    const reportSystemName = Array.isArray(reportTypes)
      ? reportTypes.find((reportType) => reportType.name === report.reportName)?.systemName
      : undefined;

    if (!reportSystemName) {
      showSnackbar({
        title: t('error', 'Error'),
        kind: 'error',
        subtitle: t('reportTypeNotFound', 'Report type not found. Please select a valid report.'),
      });
      return;
    }

    const reportType = Array.isArray(reportTypes) ? reportTypes.find((p) => p.systemName === reportSystemName) : null;

    if (reportType?.parameters?.includes(ReportParameter.Location) && !report.locationUuid) {
      showSnackbar({
        title: t('error', 'Error'),
        kind: 'error',
        subtitle: t('locationRequired', 'Location is required for this report type.'),
      });
      return;
    }

    let hideSplash = true;
    try {
      const newLine = '\r\n';
      let parameters = `param.report=${reportSystemName}${newLine}`;
      if (displayFulfillment) {
        parameters += getReportParameter(
          ReportParameter.Fullfillment,
          (report.fullFillment ?? ['All']).join(','),
          (report.fullFillment ?? ['All']).join(', '),
          t('fulfillment', 'Fulfillment'),
          newLine,
        );
      }
      if (displayPatient) {
        parameters += getReportParameter(
          ReportParameter.Patient,
          report.patientUuid ?? '',
          report.patientName?.trim() ?? 'All Patients',
          t('patients', 'Patients'),
          newLine,
        );
      }
      if (displayStockItem) {
        parameters += getReportParameter(
          ReportParameter.StockItem,
          report.stockItemUuid ?? '',
          report.stockItemName?.trim() ?? 'All Stock Items',
          t('stockItem', 'Stock Item'),
          newLine,
        );
      }
      if (displayStockItemCategory) {
        parameters += getReportParameter(
          ReportParameter.StockItemCategory,
          report.stockItemCategoryConceptUuid ?? '',
          report.stockItemCategory?.trim() ?? 'All Categories',
          t('stockItemCategory', 'Stock Item Category'),
          newLine,
        );
      }
      if (displayInventoryGroupBy) {
        parameters += getReportParameter(
          ReportParameter.InventoryGroupBy,
          report.inventoryGroupBy ?? 'LocationStockItemBatchNo',
          report.inventoryGroupByName?.trim() ?? 'Stock Item Batch Number',
          t('inventoryGroupBy', 'Inventory group by'),
          newLine,
        );
      }
      if (displayLocation && report.locationUuid) {
        parameters += getReportParameter(
          ReportParameter.Location,
          report.locationUuid,
          report.location?.trim() ?? '',
          t('location', 'Location'),
          newLine,
        );
        if (displayChildLocations) {
          parameters += getReportParameter(
            ReportParameter.ChildLocations,
            report.childLocations ? 'true' : 'false',
            report.childLocations ? 'Yes' : 'No',
            t('includeChildLocations', 'Include Child Locations'),
            newLine,
          );
        }
      }
      if (displayMaxReorderLevelRatio) {
        parameters += getReportParameter(
          ReportParameter.MaxReorderLevelRatio,
          (report.maxReorderLevelRatio ?? 0).toString(),
          (report.maxReorderLevelRatio ?? 0).toString() + '%',
          t('maxReorderLevelRatio', 'Max reorder level ratio'),
          newLine,
        );
      }
      if (displayStockSource) {
        parameters += getReportParameter(
          ReportParameter.StockSource,
          report.stockSourceUuid ?? '',
          report.stockSource?.trim() ?? 'All Sources',
          t('stockSource', 'Stock source'),
          newLine,
        );
      }
      if (displayStockSourceDestination) {
        parameters += getReportParameter(
          ReportParameter.StockSourceDestination,
          report.stockSourceDestinationUuid ?? '',
          report.stockSourceDestination?.trim() ?? 'All Destinations',
          t('stockSourceDestination', 'Stock source destination'),
          newLine,
        );
      }
      if (displayMostLeastMoving) {
        parameters += getReportParameter(
          ReportParameter.MostLeastMoving,
          report.mostLeastMoving ?? 'MostMoving',
          report.mostLeastMovingName?.trim() ?? 'Most Moving',
          t('mostMoving', 'Most moving'),
          newLine,
        );
      }
      if (displayLimit) {
        parameters += getReportParameter(
          ReportParameter.Limit,
          (report.limit ?? getParamDefaultLimit(reportSystemName) ?? 20).toString(),
          (report.limit ?? getParamDefaultLimit(reportSystemName) ?? 20).toString(),
          t(getReportLimitLabel(reportSystemName)),
          newLine,
        );
      }
      if (displayDate) {
        parameters += getReportParameter(
          ReportParameter.Date,
          report.date ? JSON.stringify(report.date).replaceAll('"', '') : '',
          formatDisplayDate(report.date) ?? '',
          t('date', 'Date'),
          newLine,
        );
      }
      if (displayStartDate) {
        parameters += getReportParameter(
          ReportParameter.StartDate,
          report.startDate ? JSON.stringify(report.startDate).replaceAll('"', '') : '',
          formatDisplayDate(report.startDate) ?? '',
          t(getReportStartDateLabel(reportSystemName)),
          newLine,
        );
      }
      if (displayEndDate) {
        parameters += getReportParameter(
          ReportParameter.EndDate,
          report.endDate ? JSON.stringify(report.endDate).replaceAll('"', '') : '',
          formatDisplayDate(report.endDate) ?? '',
          t(getReportEndDateLabel(reportSystemName)),
          newLine,
        );
      }

      const newItem = {
        batchJobType: BatchJobTypeReport,
        description: report.reportName,
        parameters: parameters,
      };

      await createBatchJob(newItem)
        .then((response) => {
          if (response.status === 201) {
            showSnackbar({
              title: t('batchJob', 'Batch Job'),
              subtitle: t('batchJobSuccess', 'Batch job created successfully'),
              kind: 'success',
            });
            handleMutate(`${restBaseUrl}/stockmanagement/batchjob?batchJobType=Report&v=default`);
            closeWorkspace();
          } else {
            showSnackbar({
              title: t('batchJobErrorTitle', 'Batch job'),
              subtitle: t('batchJobErrorMessage', 'Error creating batch job'),
              kind: 'error',
            });
            closeWorkspace();
          }
        })
        .catch(() => {
          showSnackbar({
            title: t('batchJobErrorTitle', 'Batch job'),
            subtitle: t('batchJobErrorMessage', 'Error creating batch job'),
            kind: 'error',
          });
          closeWorkspace();
        });
      hideSplash = false;
    } finally {
      if (hideSplash) {
        // setShowSplash(false);
      }
    }
  };

  const getReportParameter = (
    name: string,
    value: string,
    valueDescription: string,
    description: string,
    newLine: string,
  ): string => {
    return `param.${name}.value=${value}${newLine}param.${name}.value.desc=${valueDescription}${newLine}param.${name}.description=${description}${newLine}`;
  };

  return (
    <Form className={styles.container} onSubmit={handleSubmit(handleSave)}>
      <Stack className={styles.form} gap={5}>
        <>
          <FormGroup legendText={t('reportName', 'Report name')}>
            <Controller
              control={control}
              name="reportName"
              render={({ field: { onChange } }) => (
                <ComboBox
                  id="report"
                  titleText={t('reportName', 'Report name')}
                  items={Array.isArray(reportTypes) ? reportTypes : [reportTypes]}
                  itemToString={(item) => `${item?.name ?? item?.name ?? ''}`}
                  placeholder={t('filter', 'Filter...')}
                  onChange={({ selectedItem }) => {
                    onChange(selectedItem.name);
                    handleReportNameChange(selectedItem.name);
                  }}
                />
              )}
            />
          </FormGroup>
        </>

        {displayStockItemCategory && (
          <>
            <FormGroup legendText={t('stockItemCategory', 'Stock Item Category')}>
              <Controller
                control={control}
                name="stockItemCategoryConceptUuid"
                render={({ field: { onChange } }) => (
                  <ComboBox
                    id="stockReportItem"
                    size="md"
                    titleText={t('stockItemCategory', 'Stock Item Category')}
                    items={stockItemCategories}
                    onChange={({ selectedItem }) => {
                      onChange(selectedItem?.uuid ?? '');
                      setValue('stockItemCategory', selectedItem?.display ?? '');
                    }}
                    itemToString={(item) => item?.display ?? ''}
                    placeholder={t('filter', 'Filter...')}
                  />
                )}
              />
            </FormGroup>
          </>
        )}
        {displayStartDate && (
          <Controller
            control={control}
            name="startDate"
            render={({ field: { onChange, value } }) => (
              <DatePicker
                datePickerType="single"
                maxDate={formatForDatePicker(today())}
                locale="en"
                dateFormat={DATE_PICKER_CONTROL_FORMAT}
                onChange={onChange}
                value={value}
              >
                <DatePickerInput
                  id="startDate"
                  placeholder={DATE_PICKER_FORMAT}
                  labelText={t('startDate', 'Start date')}
                  invalid={!!errors?.startDate?.message}
                  invalidText={errors?.startDate?.message}
                />
              </DatePicker>
            )}
          />
        )}
        {displayEndDate && (
          <Controller
            control={control}
            name="endDate"
            render={({ field: { onChange, value } }) => (
              <DatePicker
                datePickerType="single"
                maxDate={formatForDatePicker(today())}
                locale="en"
                dateFormat={DATE_PICKER_CONTROL_FORMAT}
                onChange={onChange}
                value={value}
              >
                <DatePickerInput
                  id="endDate"
                  placeholder={DATE_PICKER_FORMAT}
                  labelText={t('endDate', 'End date')}
                  invalid={!!errors?.endDate?.message}
                  invalidText={errors?.endDate?.message}
                />
              </DatePicker>
            )}
          />
        )}
        {displayInventoryGroupBy && (
          <Select
            id="inventoryGroupBy"
            defaultValue={model?.inventoryGroupBy}
            invalid={!!errors?.inventoryGroupBy?.message}
            invalidText={errors?.inventoryGroupBy?.message}
            labelText={t('inventoryBy', 'Inventory by')}
            onChange={(e) => setValue('inventoryGroupBy', e.target.value)}
          >
            <SelectItem value="" text={t('selectOption', 'Select an option')} />
            <SelectItem value="StockItemOnly" text={t('stockItem', 'Stock Item')} />
            <SelectItem value="LocationStockItem" text={t('locationAndStockItem', 'Location and stock item')} />
            <SelectItem value="LocationStockItemBatchNo" text={t('locationAndBatchNo', 'Location and batch')} />
          </Select>
        )}
        {displayPatient && (
          <Controller
            control={control}
            name="patientUuid"
            render={({ field: { onChange } }) => (
              <ComboBox
                id="patient"
                titleText={t('patient', 'Patient')}
                items={patientSearchResults}
                onChange={(data) => {
                  onChange(data.selectedItem?.uuid);
                  setValue('patientName', data.selectedItem?.display || '');
                }}
                onInputChange={(searchTerm) => {
                  searchPatients(searchTerm);
                }}
                itemToString={(item) => item?.display ?? ''}
                placeholder={t('filter', 'Filter...')}
                shouldFilterItem={() => true}
              />
            )}
          />
        )}
        {displayStockItem && (
          <Controller
            control={control}
            name="stockItemUuid"
            render={({ field: { onChange } }) => (
              <ComboBox
                id="stockItem"
                titleText={t('stockItem', 'Stock Item')}
                items={stockItemSearchResults}
                onChange={(data) => {
                  onChange(data.selectedItem?.uuid);
                  setValue(
                    'stockItemName',
                    data.selectedItem
                      ? `${data.selectedItem.drugName || ''}${
                          data.selectedItem.commonName || data.selectedItem.conceptName
                            ? ` (${data.selectedItem.commonName || data.selectedItem.conceptName})`
                            : ''
                        }`
                      : '',
                  );
                }}
                onInputChange={(searchTerm) => {
                  searchStockItems(searchTerm);
                }}
                itemToString={(item) =>
                  item
                    ? `${item?.drugName || ''}${
                        item?.commonName || item?.conceptName ? ` (${item?.commonName || item?.conceptName})` : ''
                      }`
                    : ''
                }
                placeholder={t('filter', 'Filter...')}
                shouldFilterItem={() => true}
              />
            )}
          />
        )}
        {displayLocation && (
          <Select
            name="locationUuid"
            className="select-field"
            labelText={t('location', 'Location')}
            id="location"
            onChange={(e) => {
              const selectedLocation = stockLocations?.find((loc) => loc.id === e.target.value);
              setValue('locationUuid', e.target.value);
              setValue('location', selectedLocation?.name || '');
            }}
            defaultValue=""
            invalid={!!errors?.location?.message}
            invalidText={errors?.location?.message}
          >
            <SelectItem disabled hidden value="" text={t('chooseALocation', 'Choose a location')} />
            {(stockLocations ?? [])?.map((loc) => {
              return <SelectItem key={loc.id} value={loc.id} text={loc.name} />;
            })}
          </Select>
        )}
        {displayChildLocations && (
          <Controller
            control={control}
            name="childLocations"
            render={({ field: { onChange, value } }) => (
              <Checkbox
                id="childLocations"
                onChange={onChange}
                checked={value}
                labelText={t('includeChildLocations', 'Include Child Locations')}
              />
            )}
          />
        )}
        {displayMostLeastMoving && (
          <Controller
            control={control}
            name="mostLeastMoving"
            render={({ field: { onChange, value } }) => (
              <RadioButtonGroup name="mostLeastMoving" legendText={t('rank', 'Rank')} onChange={onChange} value={value}>
                <RadioButton value="MostMoving" id="mostLeastMovingMost" labelText={t('mostMoving', 'Most moving')} />
                <RadioButton
                  value="LeastMoving"
                  id="mostLeastMovingLeast"
                  labelText={t('leastMoving', 'Least Moving')}
                />
              </RadioButtonGroup>
            )}
          />
        )}
        {displayLimit && (
          <Controller
            control={control}
            name="limit"
            render={({ field: { onChange, value } }) => (
              <NumberInput
                id="limitTop"
                allowEmpty
                disableWheel
                hideSteppers
                value={value}
                onChange={onChange}
                label={t('limit', 'Limit')}
              />
            )}
          />
        )}
        {displayFulfillment && (
          <div className={styles.flexRow}>
            <Controller
              control={control}
              name="fullFillment"
              render={({ field: { onChange, value } }) => (
                <>
                  <Checkbox
                    id="allFulfillment"
                    checked={value?.includes('All')}
                    onChange={(event) => {
                      const isChecked = event.target.checked;
                      if (isChecked) {
                        onChange(['All']);
                      } else {
                        onChange(value.filter((item) => item !== 'All'));
                      }
                    }}
                    labelText={t('all', 'All')}
                  />
                  <Checkbox
                    id="fullFulfillment"
                    checked={value?.includes('Full')}
                    onChange={(event) => {
                      const isChecked = event.target.checked;
                      onChange(
                        isChecked
                          ? [...value.filter((item) => item !== 'All'), 'Full']
                          : value.filter((item) => item !== 'Full'),
                      );
                    }}
                    labelText={t('fullFulfillment', 'Full Fulfillment')}
                  />
                  <Checkbox
                    id="partialFulfillment"
                    checked={value?.includes('Partial')}
                    onChange={(event) => {
                      const isChecked = event.target.checked;
                      onChange(
                        isChecked
                          ? [...value.filter((item) => item !== 'All'), 'Partial']
                          : value.filter((item) => item !== 'Partial'),
                      );
                    }}
                    labelText={t('partialFulfillment', 'Partial Fulfillment')}
                  />
                  <Checkbox
                    id="noneFulfillment"
                    checked={value?.includes('None')}
                    onChange={(event) => {
                      const isChecked = event.target.checked;
                      onChange(
                        isChecked
                          ? [...value.filter((item) => item !== 'All'), 'None']
                          : value.filter((item) => item !== 'None'),
                      );
                    }}
                    labelText={t('noneFulfillment', 'Non Fulfillment')}
                  />
                </>
              )}
            />
          </div>
        )}
        {displayDate && (
          <Controller
            control={control}
            name="date"
            render={({ field: { onChange, value } }) => (
              <DatePicker
                datePickerType="single"
                maxDate={formatForDatePicker(today())}
                locale="en"
                dateFormat={DATE_PICKER_CONTROL_FORMAT}
                onChange={onChange}
                value={value}
              >
                <DatePickerInput
                  id="date"
                  placeholder={DATE_PICKER_FORMAT}
                  labelText={t('date', 'Date')}
                  invalid={!!errors?.date?.message}
                  invalidText={errors?.date?.message}
                />
              </DatePicker>
            )}
          />
        )}
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
        <Button type="submit" className={styles.button}>
          {getCoreTranslation('save')}
        </Button>
      </ButtonSet>
    </Form>
  );
};

export default CreateReport;
