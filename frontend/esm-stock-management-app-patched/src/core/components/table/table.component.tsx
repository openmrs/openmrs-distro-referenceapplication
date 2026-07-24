import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { saveAs } from 'file-saver';
import {
  DataTable,
  OverflowMenu,
  OverflowMenuItem,
  Pagination,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
  TableToolbar,
  TableToolbarContent,
  TableToolbarSearch,
  Tile,
} from '@carbon/react';
import { DocumentDownload } from '@carbon/react/icons';
import { isDesktop, useLayoutType, usePagination } from '@openmrs/esm-framework';
import { type DataTableRenderProps } from './types';
import styles from './table.scss';

type FilterProps = {
  rowIds: Array<string>;
  headers: any;
  cellsById: any;
  inputValue: string;
  getCellId: (row, key) => string;
};

interface ListProps {
  columns: any;
  data: any;
  children?: (renderProps: DataTableRenderProps) => React.ReactElement;
  totalItems?: number;
  goToPage?: (page: number) => void;
  hasToolbar?: boolean;
}

const DataList: React.FC<ListProps> = ({ columns, data, children, totalItems, goToPage, hasToolbar = true }) => {
  const { t } = useTranslation();
  const layout = useLayoutType();
  const [allRows, setAllRows] = useState([]);
  const isTablet = useLayoutType() === 'tablet';
  const [list] = useState(data);
  const pageSizes = [10, 20, 30, 40, 50];
  const [currentPageSize, setPageSize] = useState(10);
  const { goTo, results: paginatedList, currentPage } = usePagination(list, currentPageSize);

  useEffect(() => {
    const rows: Array<Record<string, string>> = [];

    paginatedList.map((item: any, index) => {
      return rows.push({ ...item, id: index++ });
    });
    setAllRows(rows);
  }, [paginatedList, allRows]);

  const handleFilter = ({ rowIds, headers, cellsById, inputValue, getCellId }: FilterProps): Array<string> => {
    return rowIds.filter((rowId) =>
      headers.some(({ key }) => {
        const cellId = getCellId(rowId, key);
        const filterableValue = cellsById[cellId].value;
        const filterTerm = inputValue?.toLowerCase();

        if (typeof filterableValue === 'boolean') {
          return false;
        }

        return ('' + filterableValue)?.toLowerCase().includes(filterTerm);
      }),
    );
  };
  const handleExportCSV = () => {
    const csvString = convertToCSV(list, columns);
    const blob = new Blob([csvString], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'data.csv');
  };

  const handleExportJSON = () => {
    const jsonString = JSON.stringify(list, null, 2);
    const jsonBlob = new Blob([jsonString], { type: 'application/json' });
    saveAs(jsonBlob, 'data.json');
  };

  const convertToCSV = (data, columns) => {
    const header = columns.map((col) => col.header).join(',');
    const rows = data.map((row) => columns.map((col) => JSON.stringify(row[col.key])).join(','));
    return [header, ...rows].join('\n');
  };

  return (
    <>
      <DataTable
        data-floating-menu-container
        rows={allRows}
        headers={columns}
        filterRows={handleFilter}
        overflowMenuOnHover={isDesktop(layout)}
        size={isTablet ? 'lg' : 'md'}
        useZebraStyles
      >
        {({ rows, headers, getHeaderProps, getTableProps, onInputChange }) => (
          <div>
            <TableContainer className={styles.tableContainer}>
              {hasToolbar && (
                <TableToolbar
                  style={{
                    position: 'static',
                    overflow: 'visible',
                    backgroundColor: 'color',
                  }}
                >
                  <TableToolbarContent className={styles.toolbarContent}>
                    {children ? (
                      children({
                        onInputChange,
                      })
                    ) : (
                      <>
                        <OverflowMenu
                          focusTrap={false}
                          iconDescription={t('downloadOptions', 'Download options')}
                          renderIcon={DocumentDownload}
                          size="sm"
                        >
                          <OverflowMenuItem
                            className={styles.menuItem}
                            itemText={t('downloadAsCSV', 'Download As CSV')}
                            onClick={handleExportCSV}
                          />
                          <OverflowMenuItem
                            className={styles.menuItem}
                            itemText={t('downloadAsJson', 'Download As JSON')}
                            onClick={handleExportJSON}
                          />
                        </OverflowMenu>
                        <TableToolbarSearch
                          className={styles.itemListSearch}
                          expanded
                          onChange={onInputChange}
                          placeholder={t('searchThisList', 'Search this list')}
                          size="sm"
                        />
                      </>
                    )}
                  </TableToolbarContent>
                </TableToolbar>
              )}
              <Table {...getTableProps()}>
                <TableHead>
                  <TableRow>
                    {headers.map((header) => (
                      <TableHeader {...getHeaderProps({ header })} key={header.key}>
                        {header.header}
                      </TableHeader>
                    ))}
                  </TableRow>
                </TableHead>
                <TableBody>
                  {rows.map((row) => (
                    <TableRow key={row.id}>
                      {row.cells.map((cell) => (
                        <TableCell key={cell.id}>{cell.value}</TableCell>
                      ))}
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              {rows.length === 0 ? (
                <div className={styles.tileContainer}>
                  <Tile className={styles.tile}>
                    <div className={styles.tileContent}>
                      <p className={styles.content}>{t('noData', 'No data to display')}</p>
                      <p className={styles.helper}>{t('checkFilters', 'Check the filters above')}</p>
                    </div>
                  </Tile>
                </div>
              ) : null}
              <Pagination
                forwardText={t('nextPage', 'Next page')}
                backwardText={t('previousPage', 'Previous page')}
                page={currentPage}
                pageSize={currentPageSize}
                pageSizes={pageSizes}
                totalItems={totalItems || list?.length}
                className={styles.pagination}
                onChange={({ pageSize, page }) => {
                  if (pageSize !== currentPageSize) {
                    setPageSize(pageSize);
                  }
                  if (page !== currentPage) {
                    if (goToPage) {
                      goToPage(page);
                      return;
                    }
                    goTo(page);
                  }
                }}
              />
            </TableContainer>
          </div>
        )}
      </DataTable>
    </>
  );
};

export default DataList;
