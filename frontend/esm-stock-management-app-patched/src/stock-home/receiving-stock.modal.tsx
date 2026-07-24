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

const ReceivingStockModal = ({ closeModal, receivingStock }) => {
  const { t } = useTranslation();

  const headers = [
    { key: 'status', header: t('status', 'Status') },
    { key: 'sourceName', header: t('source', 'Source') },
    { key: 'destinationName', header: t('destination', 'Destination') },
    { key: 'stockItemName', header: t('stockItem', 'Stock Item') },
    { key: 'stockItemPackagingUOMName', header: t('unit', 'Unit') },
    { key: 'quantity', header: t('quantity', 'Quantity') },
  ];

  return (
    <>
      <ModalHeader closeModal={closeModal} title={t('receivedStock', 'Received stock')} />
      <ModalBody>
        {receivingStock && receivingStock.length > 0 ? (
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
                {receivingStock.map((item, index) =>
                  item?.stockOperationItems.map((stock, stockIndex) => (
                    <TableRow key={`${index}-${stockIndex}`}>
                      <TableCell>{item?.status || 'N/A'}</TableCell>
                      <TableCell>{item?.sourceName || 'N/A'}</TableCell>
                      <TableCell>{item?.destinationName || 'N/A'}</TableCell>
                      <TableCell>{stock?.stockItemName || 'N/A'}</TableCell>
                      <TableCell>{stock?.stockItemPackagingUOMName || 'N/A'}</TableCell>
                      <TableCell>{stock?.quantity || 'N/A'}</TableCell>
                    </TableRow>
                  )),
                )}
              </TableBody>
            </Table>
          </TableContainer>
        ) : (
          <p>{t('noReceivedStockDataAvailable', 'No received stock data available.')}</p>
        )}
      </ModalBody>
    </>
  );
};

export default ReceivingStockModal;
