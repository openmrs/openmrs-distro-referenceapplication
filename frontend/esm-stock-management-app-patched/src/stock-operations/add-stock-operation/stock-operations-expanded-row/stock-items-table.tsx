import React, { useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import {
  DataTable,
  Pagination,
  Search,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
  Tile,
} from '@carbon/react';
import { formatDate, parseDate, usePagination } from '@openmrs/esm-framework';
import { type StockOperationItemDTO } from '../../../core/api/types/stockOperation/StockOperationItemDTO';
import styles from './stock-items-table.scss';

type Props = {
  items: Array<StockOperationItemDTO>;
};
const StockItemsTable: React.FC<Props> = ({ items }) => {
  const { t } = useTranslation();
  const [pageSize, setPageSize] = useState(10);
  const pageSizesOptions = useMemo(() => [5, 10, 20, 50, 100], []);
  const [searchText, setSearchText] = useState<string>('');

  const handleSearch = (item: StockOperationItemDTO) => {
    if (!searchText) return true;
    return item.commonName.toLowerCase().includes(searchText);
  };
  const filtered = items.filter(handleSearch);
  const { results, totalPages, currentPage, goTo } = usePagination(filtered, pageSize);

  const headers = useMemo(
    () => [
      {
        header: t('item', 'Item'),
        key: 'commonName',
      },
      {
        header: t('batchNo', 'Batch number'),
        key: 'batchNo',
      },
      {
        header: t('expiry', 'Expiry'),
        key: 'expiration',
      },
      {
        header: t('quantity', 'Quantity'),
        key: 'quantity',
      },
      {
        header: t('uom', 'Unit of Measurement'),
        key: 'stockItemPackagingUOMName',
      },
    ],
    [t],
  );

  const tableRows = useMemo(
    () =>
      results.map((item, index) => ({
        id: index,
        ...item,
        expiration: item.expiration ? formatDate(parseDate(`${item.expiration}`)) : '--',
      })),
    [results],
  );

  return (
    <Tile className={styles.container}>
      <span className={styles.title}>{t('stockItems', 'Stock items')}</span>
      <Search
        labelText={t('search', 'Search')}
        value={searchText}
        onChange={({ target: { value } }) => setSearchText(value as string)}
      />
      <DataTable useZebraStyles rows={tableRows} headers={headers}>
        {({ rows, headers, getTableProps, getHeaderProps, getRowProps }) => (
          <Table {...getTableProps()}>
            <TableHead>
              <TableRow>
                {headers.map((header) => (
                  <TableHeader {...getHeaderProps({ header })}>{header.header}</TableHeader>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => (
                <TableRow {...getRowProps({ row })}>
                  {row.cells.map((cell) => (
                    <TableCell key={cell.id}>{cell.value}</TableCell>
                  ))}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </DataTable>
      <Pagination
        page={currentPage}
        pageSize={pageSize}
        pageSizes={pageSizesOptions}
        totalItems={filtered.length}
        onChange={({ page, pageSize }) => {
          goTo(page);
          setPageSize(pageSize);
        }}
      />
    </Tile>
  );
};

export default StockItemsTable;
