import React, { useMemo } from 'react';
import {
  DataTable,
  TableContainer,
  Table,
  TableHead,
  TableRow,
  TableHeader,
  TableBody,
  TableCell,
} from '@carbon/react';
import { useStockItem } from '../../../stock-items.resource';
import PrintableBincardTransactionHeader from './printable-bincard-transaction-header.component';
import PrintableTransactionFooter from './printable-transaction-footer.component';
import styles from './printable-transaction.scss';

type Props = {
  title: string;
  columns: any;
  data: any;
};

const TransactionsBincardPrintout: React.FC<Props> = ({ columns, data, title }) => {
  return (
    <div>
      <PrintableBincardTransactionHeader itemName={title} />

      <div className={styles.itemsContainer}>
        <div className={styles.tableContainer}>
          <DataTable data-floating-menu-container rows={data} headers={columns} useZebraStyles>
            {({ rows, headers, getHeaderProps, getTableProps, onInputChange }) => (
              <div>
                <TableContainer>
                  <Table {...getTableProps()}>
                    <TableHead>
                      <TableRow>
                        {headers.map((header) => (
                          <TableHeader {...getHeaderProps({ header })}>{header.header}</TableHeader>
                        ))}
                      </TableRow>
                    </TableHead>
                    <TableBody style={{ fontSize: '8px' }}>
                      {rows.map((row) => (
                        <TableRow key={row.id}>
                          {row.cells.map((cell) => (
                            <TableCell key={cell.id}>{cell.value}</TableCell>
                          ))}
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </TableContainer>
              </div>
            )}
          </DataTable>
        </div>
      </div>

      <PrintableTransactionFooter title={''} />
    </div>
  );
};

export default TransactionsBincardPrintout;
