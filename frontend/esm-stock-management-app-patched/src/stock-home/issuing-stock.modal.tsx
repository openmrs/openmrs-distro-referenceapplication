import React from 'react';
import {
  ModalBody,
  ModalHeader,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableHeader,
  TableRow,
} from '@carbon/react';
import { useTranslation } from 'react-i18next';

interface IssuingStockModalProps {
  issuingStock: any[];
  closeModal: () => void;
}

const IssuingStockModal = ({ issuingStock, closeModal }: IssuingStockModalProps) => {
  const { t } = useTranslation();

  const headers = [
    { key: 'status', header: 'Status' },
    { key: 'sourceName', header: 'Source' },
    { key: 'destinationName', header: 'Destination' },
    { key: 'stockItemName', header: 'Stock Item' },
    { key: 'stockItemPackagingUOMName', header: 'Unit' },
    { key: 'quantity', header: 'Quantity' },
  ];

  return (
    <>
      <ModalHeader closeModal={closeModal} title={t('issuedStock', 'Issued stock')} />
      <ModalBody>
        {issuingStock && issuingStock.length > 0 ? (
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  {headers.map((header) => (
                    <TableHeader key={header.key}>{header.header}</TableHeader>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {issuingStock.map((item, index) => (
                  <TableRow key={index}>
                    <TableCell>{item?.status || 'N/A'}</TableCell>
                    <TableCell>{item?.sourceName || 'N/A'}</TableCell>
                    <TableCell>{item?.destinationName || 'N/A'}</TableCell>
                    <TableCell>{item?.stockItemName || 'N/A'}</TableCell>
                    <TableCell>{item?.stockItemPackagingUOMName || 'N/A'}</TableCell>
                    <TableCell>{item?.quantity || 'N/A'}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        ) : (
          <p>{t('noIssuedStockDataAvailable', 'No issued stock data available.')}</p>
        )}
      </ModalBody>
    </>
  );
};

export default IssuingStockModal;
