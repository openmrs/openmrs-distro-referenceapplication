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

interface ExpiredStockModalProps {
  closeModal: () => void;
  expiredStock: any[];
}

const ExpiredStockModal = ({ closeModal, expiredStock }: ExpiredStockModalProps) => {
  const { t } = useTranslation();

  const headers = [
    { key: 'drugName', header: 'Drug Name' },
    { key: 'batchNo', header: 'Batch No' },
    { key: 'quantity', header: 'Quantity' },
    { key: 'dispensingUnitName', header: 'Unit' },
    { key: 'expiration', header: 'Expiration Date' },
  ];

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-GB', {
      year: 'numeric',
      month: 'short',
      day: '2-digit',
    }).format(date);
  };

  return (
    <>
      <ModalHeader closeModal={closeModal} title={t('expiredStock', 'Expired stock')} />
      <ModalBody>
        {expiredStock.length > 0 ? (
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
                {expiredStock.map((item, index) => (
                  <TableRow key={index}>
                    <TableCell>{item?.drugName || 'N/A'}</TableCell>
                    <TableCell>{item?.batchNo || 'N/A'}</TableCell>
                    <TableCell>{item?.quantity || 'N/A'}</TableCell>
                    <TableCell>{item?.dispensingUnitName || 'N/A'}</TableCell>
                    <TableCell>{formatDate(item?.expiration)}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        ) : (
          <p>{t('noExpiredStockDataAvailable', 'No expired stock data available.')}</p>
        )}
      </ModalBody>
    </>
  );
};

export default ExpiredStockModal;
