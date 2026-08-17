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

interface OutOfStockItem {
  displayName: string;
  quantity: number;
  reorderLevel?: number | null;
}

interface OutOfStockModalProps {
  closeModal: () => void;
  outOfStockItems: Array<OutOfStockItem>;
  understockedItems: Array<OutOfStockItem>;
}

const OutOfStockModal = ({ closeModal, outOfStockItems, understockedItems }: OutOfStockModalProps) => {
  const { t } = useTranslation();

  const renderTable = (title: string, items: Array<OutOfStockItem>, showReorderLevel: boolean) => (
    <>
      <h5>{title}</h5>
      {items.length > 0 ? (
        <TableContainer>
          <Table size="sm">
            <TableHead>
              <TableRow>
                <TableHeader>{t('itemName', 'Item name')}</TableHeader>
                <TableHeader>{t('quantity', 'Quantity')}</TableHeader>
                {showReorderLevel && <TableHeader>{t('reorderLevel', 'Reorder level')}</TableHeader>}
              </TableRow>
            </TableHead>
            <TableBody>
              {items.map((item, index) => (
                <TableRow key={index}>
                  <TableCell>{item.displayName}</TableCell>
                  <TableCell>{item.quantity}</TableCell>
                  {showReorderLevel && <TableCell>{item.reorderLevel}</TableCell>}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : (
        <p>{t('noItemsToDisplay', 'No items to display')}</p>
      )}
    </>
  );

  return (
    <>
      <ModalHeader closeModal={closeModal} title={t('outOfStock', 'Out of stock')} />
      <ModalBody>
        {renderTable(t('outOfStockItems', 'Out of stock items'), outOfStockItems, false)}
        {renderTable(t('understockedItems', 'Understocked items'), understockedItems, true)}
      </ModalBody>
    </>
  );
};

export default OutOfStockModal;
