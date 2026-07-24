import React, { useCallback, useMemo, useState } from 'react';
import {
  DataTable,
  DataTableSkeleton,
  DatePicker,
  DatePickerInput,
  InlineLoading,
  Link,
  Pagination,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableExpandedRow,
  TableExpandHeader,
  TableExpandRow,
  TableHead,
  TableHeader,
  TableRow,
  TableToolbar,
  TableToolbarAction,
  TableToolbarContent,
  TableToolbarMenu,
  TableToolbarSearch,
  Tile,
} from '@carbon/react';
import { ArrowRight } from '@carbon/react/icons';
import { useTranslation } from 'react-i18next';
import { isDesktop, restBaseUrl } from '@openmrs/esm-framework';
import { DATE_PICKER_CONTROL_FORMAT, DATE_PICKER_FORMAT, StockFilters } from '../constants';
import { formatDisplayDate } from '../core/utils/datetimeUtils';
import { handleMutate } from '../utils';
import { ResourceRepresentation } from '../core/api/api';
import { useStockOperationPages } from './stock-operations-table.resource';
import EditStockOperationActionMenu from './edit-stock-operation/edit-stock-operation-action-menu.component';
import StockOperationTypesSelector from './stock-operation-types-selector/stock-operation-types-selector.component';
import StockOperationsFilters from './stock-operations-filters.component';
import StockOperationExpandedRow from './add-stock-operation/stock-operations-expanded-row/stock-operation-expanded-row.component';
import styles from './stock-operations-table.scss';

interface StockOperationsTableProps {
  status?: string;
}

const StockOperations: React.FC<StockOperationsTableProps> = () => {
  const { t } = useTranslation();

  const handleRefresh = () => {
    handleMutate(`${restBaseUrl}/stockmanagement/stockoperation`);
  };

  const [selectedFromDate, setSelectedFromDate] = useState(null);
  const [selectedToDate, setSelectedToDate] = useState(null);
  const [selectedSources, setSelectedSources] = useState<string[]>([]);
  const [selectedStatus, setSelectedStatus] = useState<string[]>([]);
  const [selectedOperations, setSelectedOperations] = useState<string[]>([]);

  const { items, tableHeaders, currentPage, pageSizes, totalItems, goTo, currentPageSize, setPageSize, isLoading } =
    useStockOperationPages({
      v: ResourceRepresentation.Full,
      totalCount: true,
      operationDateMin: selectedFromDate?.toISOString(),
      operationDateMax: selectedToDate?.toISOString(),
      status: selectedStatus.join(','),
      sourceTypeUuid: selectedSources.join(','),
      operationTypeUuid: selectedOperations.join(','),
    });

  const filterApplied =
    selectedFromDate || selectedToDate || selectedSources.length || selectedStatus.length || selectedOperations.length;

  const handleOnFilterChange = useCallback((selectedItems, filterType) => {
    if (filterType === StockFilters.SOURCES) {
      setSelectedSources(selectedItems);
    } else if (filterType === StockFilters.OPERATION) {
      setSelectedOperations(selectedItems);
    } else {
      setSelectedStatus(selectedItems);
    }
  }, []);

  const handleDateFilterChange = ([startDate, endDate]) => {
    if (startDate) {
      setSelectedFromDate(startDate);
      if (selectedToDate && startDate && selectedToDate < startDate) {
        setSelectedToDate(startDate);
      }
    }
    if (endDate) {
      setSelectedToDate(endDate);
      if (selectedFromDate && endDate && selectedFromDate > endDate) {
        setSelectedFromDate(endDate);
      }
    }
  };

  const tableRows = useMemo(
    () =>
      items?.map((stockOperation, index) => {
        const threshHold = 1;
        const itemCountGreaterThanThreshhold = (stockOperation?.stockOperationItems?.length ?? 0) > threshHold;
        const commonNames =
          stockOperation?.stockOperationItems
            ?.slice(0, itemCountGreaterThanThreshhold ? threshHold : undefined)
            .map((item) => item.commonName)
            .join(', ') ?? '';

        return {
          ...stockOperation,
          id: stockOperation?.uuid,
          key: `key-${stockOperation?.uuid}`,
          operationTypeName: `${stockOperation?.operationTypeName}`,
          operationNumber: (
            <EditStockOperationActionMenu stockOperation={stockOperation} showIcon={false} showprops={true} />
          ),
          stockOperationItems: {
            commonNames,
            more: itemCountGreaterThanThreshhold ? stockOperation?.stockOperationItems?.length - threshHold : 0,
          },
          status: `${stockOperation?.status}`,
          source: `${stockOperation?.sourceName ?? ''}`,
          destination: `${stockOperation?.destinationName ?? ''}`,
          location: (
            <>
              {stockOperation?.sourceName ?? ''}
              {stockOperation?.sourceName && stockOperation?.destinationName ? (
                <ArrowRight className={styles.arrowIcon} key={`${index}-0`} size={12} />
              ) : (
                ''
              )}{' '}
              {stockOperation?.destinationName ?? ''}
            </>
          ),
          responsiblePerson: `${
            stockOperation?.responsiblePersonFamilyName ?? stockOperation?.responsiblePersonOther ?? ''
          } ${stockOperation?.responsiblePersonGivenName ?? ''}`,
          operationDate: formatDisplayDate(stockOperation?.operationDate),
          actions: <EditStockOperationActionMenu stockOperation={stockOperation} showIcon={true} showprops={false} />,
        };
      }),
    [items],
  );

  if (isLoading && !filterApplied) {
    return (
      <DataTableSkeleton className={styles.dataTableSkeleton} showHeader={false} rowCount={5} columnCount={5} zebra />
    );
  }

  return (
    <>
      <h2 className={styles.tableHeader}>
        {t('stockOperationsTableHeader', 'Stock operations to track movement of stock.')}
      </h2>
      <DataTable headers={tableHeaders} isSortable rows={tableRows} useZebraStyles>
        {({
          expandRow,
          getExpandedRowProps,
          getHeaderProps,
          getRowProps,
          getTableProps,
          headers,
          onInputChange,
          rows,
        }) => (
          <TableContainer>
            <TableToolbar
              style={{
                position: 'static',
                overflow: 'visible',
                backgroundColor: 'color',
              }}
            >
              <TableToolbarContent className={styles.toolbarContent}>
                <TableToolbarSearch
                  expanded
                  labelText={t('searchStockOperations', 'Search stock operations')}
                  onChange={onInputChange}
                  placeholder={t('searchStockOperations', 'Search stock operations')}
                />
                <div className={styles.container}>
                  <DatePicker
                    className={styles.datePicker}
                    datePickerType="range"
                    dateFormat={DATE_PICKER_CONTROL_FORMAT}
                    onChange={([startDate, endDate]) => handleDateFilterChange([startDate, endDate])}
                    value={[selectedFromDate, selectedToDate]}
                  >
                    <DatePickerInput
                      id="stock-operations-start-date"
                      labelText={t('startDate', 'Start date')}
                      placeholder={DATE_PICKER_FORMAT}
                    />
                    <DatePickerInput
                      id="stock-operations-end-date"
                      labelText={t('endDate', 'End date')}
                      placeholder={DATE_PICKER_FORMAT}
                    />
                  </DatePicker>
                  <StockOperationsFilters filterName={StockFilters.SOURCES} onFilterChange={handleOnFilterChange} />
                  <StockOperationsFilters filterName={StockFilters.STATUS} onFilterChange={handleOnFilterChange} />
                  <StockOperationsFilters filterName={StockFilters.OPERATION} onFilterChange={handleOnFilterChange} />
                </div>
                <TableToolbarMenu>
                  <TableToolbarAction className={styles.toolbarMenuAction} onClick={handleRefresh}>
                    {t('refresh', 'Refresh')}
                  </TableToolbarAction>
                </TableToolbarMenu>

                <StockOperationTypesSelector />
              </TableToolbarContent>
            </TableToolbar>
            <Table {...getTableProps()}>
              <TableHead>
                <TableRow>
                  <TableExpandHeader />
                  {headers.map(
                    (header: any) =>
                      header.key !== 'details' && (
                        <TableHeader
                          {...getHeaderProps({
                            header,
                            isSortable: header.isSortable,
                          })}
                          className={isDesktop ? styles.desktopHeader : styles.tabletHeader}
                          key={`${header.key}`}
                        >
                          {header.header?.content ?? header.header}
                        </TableHeader>
                      ),
                  )}
                  <TableHeader></TableHeader>
                </TableRow>
              </TableHead>
              <TableBody>
                {rows?.map((row: any, index) => {
                  const props = getRowProps({ row });
                  const expandedRowProps = getExpandedRowProps({ row });
                  return (
                    <React.Fragment key={row.id}>
                      <TableExpandRow className={isDesktop ? styles.desktopRow : styles.tabletRow} {...props}>
                        {row.cells.map((cell) => (
                          <TableCell key={cell.id}>
                            {cell?.info?.header === 'stockOperationItems' ? (
                              <span>
                                <span>{cell.value.commonNames}</span>
                                {cell.value.more > 0 && (
                                  <Link onClick={() => expandRow(row.id)}>{`...(${cell.value.more} more)`}</Link>
                                )}
                              </span>
                            ) : (
                              cell.value
                            )}
                          </TableCell>
                        ))}
                      </TableExpandRow>
                      {row.isExpanded ? (
                        <TableExpandedRow colSpan={headers.length + 2}>
                          <StockOperationExpandedRow model={items[index]} />
                        </TableExpandedRow>
                      ) : (
                        <TableExpandedRow className={styles.hiddenRow} colSpan={headers.length + 2} />
                      )}
                    </React.Fragment>
                  );
                })}
              </TableBody>
            </Table>
            {rows.length === 0 && !isLoading ? (
              <div className={styles.tileContainer}>
                <Tile className={styles.tile}>
                  <div className={styles.tileContent}>
                    <p className={styles.content}>{t('noOperationsToDisplay', 'No stock operations to display')}</p>
                    <p className={styles.helper}>{t('checkFilters', 'Check the filters above')}</p>
                  </div>
                </Tile>
              </div>
            ) : null}
            {Boolean(filterApplied && isLoading) && (
              <div className={styles.rowLoadingContainer}>
                <InlineLoading description={t('loadingPlaceholder', 'Loading...')} />
              </div>
            )}
          </TableContainer>
        )}
      </DataTable>
      {items.length > 0 && (
        <Pagination
          page={currentPage}
          pageSize={currentPageSize}
          pageSizes={pageSizes}
          totalItems={totalItems}
          onChange={({ pageSize, page }) => {
            if (pageSize !== currentPageSize) {
              setPageSize(pageSize);
            }
            if (page !== currentPage) {
              goTo(page);
            }
          }}
          className={styles.paginationOverride}
        />
      )}
    </>
  );
};

export default StockOperations;
