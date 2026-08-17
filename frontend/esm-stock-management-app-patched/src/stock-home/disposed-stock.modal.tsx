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

const DisposedStockModal = ({ closeModal, disposedStock }) => {
  const { t } = useTranslation();

  const headers = [
    { key: 'reasonName', header: t('reason', 'Reason') },
    { key: 'sourceName', header: t('location', 'Location') },
    { key: 'stockItemName', header: t('stockItem', 'Stock Item') },
    { key: 'stockItemPackagingUOMName', header: t('unit', 'Unit') },
    { key: 'quantity', header: t('quantity', 'Quantity') },
  ];

  return (
    <>
      <ModalHeader closeModal={closeModal} title={t('disposedStock', 'Disposed stock')} />
      <ModalBody>
        {disposedStock && disposedStock.length > 0 ? (
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
                {disposedStock.map((item, index) =>
                  item?.stockOperationItems?.map((stock, stockIndex) => (
                    <TableRow key={`${index}-${stockIndex}`}>
                      <TableCell>{item?.reasonName || 'N/A'}</TableCell>
                      <TableCell>{item?.sourceName || 'N/A'}</TableCell>
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
          <p>{t('noDisposedStockDataAvailable', 'No disposed stock data available.')}</p>
        )}
      </ModalBody>
    </>
  );
};

export default DisposedStockModal;
