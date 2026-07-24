import React, { useRef } from 'react';
import { ModalBody, ModalHeader, ModalFooter, Button } from '@carbon/react';
import { useTranslation } from 'react-i18next';
import { useReactToPrint } from 'react-to-print';
import { getCoreTranslation } from '@openmrs/esm-framework';
import TransactionsBincardPrintout from './transactions-bincard-printout.component';

type TransactionsBincardPrintPreviewModalProps = {
  onClose?: () => void;
  title?: string;
  columns: any;
  data: any;
  binOrStockCard: number;
};

const TransactionsBincardPrintPreviewModal: React.FC<TransactionsBincardPrintPreviewModalProps> = ({
  onClose,
  title,
  columns,
  data,
  binOrStockCard,
}) => {
  const { t } = useTranslation();
  const ref = useRef<HTMLDivElement>(null);

  const handlePrint = useReactToPrint({
    content: () => ref.current,
  });
  return (
    <>
      <ModalHeader closeModal={onClose} title={t('printBinCard', 'Print Bin Card')} />
      <ModalBody>
        <div ref={ref}>
          <TransactionsBincardPrintout title={title} columns={columns} data={data} />
        </div>
      </ModalBody>
      <ModalFooter>
        <Button kind="secondary" onClick={onClose}>
          {getCoreTranslation('cancel')}
        </Button>
        <Button type="button" onClick={handlePrint}>
          {getCoreTranslation('print')}
        </Button>
      </ModalFooter>
    </>
  );
};

export default TransactionsBincardPrintPreviewModal;
