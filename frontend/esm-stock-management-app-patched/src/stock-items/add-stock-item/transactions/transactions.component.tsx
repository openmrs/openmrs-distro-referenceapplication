import { DataTableSkeleton } from '@carbon/react';
import { ArrowLeft } from '@carbon/react/icons';
import React, { useEffect, useMemo, useState } from 'react';
import { useForm } from 'react-hook-form';
import { useTranslation } from 'react-i18next';
import DataList from '../../../core/components/table/table.component';
import { formatDisplayDate } from '../../../core/utils/datetimeUtils';
import { type StockItemInventoryFilter } from '../../stock-items.resource';
import TransactionsPrintAction from './printout/transactions-print-action.component';
import TransactionsLocationsFilter from './transaction-filters/transaction-locations-filter.component';
import { useStockItemsTransactions } from './transactions.resource';
import StockOperationReference from '../../../stock-operations/stock-operation-reference.component';

interface TransactionsProps {
  onSubmit?: () => void;
  stockItemUuid: string;
}

const Transactions: React.FC<TransactionsProps> = ({ stockItemUuid }) => {
  const { t } = useTranslation();

  const [stockItemFilter, setStockItemFilter] = useState<StockItemInventoryFilter>();
  const {
    isLoading,
    items,
    tableHeaders,
    totalCount,
    setCurrentPage,
    setStockItemUuid,
    setLocationUuid,
    binCardHeaders,
    inventory,
  } = useStockItemsTransactions(stockItemFilter);

  useEffect(() => {
    setStockItemUuid(stockItemUuid);
  }, [stockItemUuid, setStockItemUuid]);

  const { control } = useForm({});

  const tableRows = useMemo(() => {
    return items?.map((stockItemTransaction) => {
      const balance = inventory?.total ?? '';

      return {
        ...stockItemTransaction,
        id: stockItemTransaction?.uuid,
        key: `key-${stockItemTransaction?.uuid}`,
        uuid: `${stockItemTransaction?.uuid}`,
        date: formatDisplayDate(stockItemTransaction?.dateCreated),
        location:
          stockItemTransaction.operationSourcePartyName && stockItemTransaction.operationDestinationPartyName ? (
            stockItemTransaction.operationSourcePartyName === stockItemTransaction?.partyName ? (
              stockItemTransaction.quantity > 0 ? (
                <>
                  <span className="transaction-location">{stockItemTransaction.operationSourcePartyName}</span>
                  <ArrowLeft size={16} /> {stockItemTransaction.operationDestinationPartyName}
                </>
              ) : (
                <>
                  <span className="transaction-location">{stockItemTransaction.operationSourcePartyName}</span>
                  <ArrowLeft size={16} /> {stockItemTransaction.operationDestinationPartyName}
                </>
              )
            ) : stockItemTransaction.operationDestinationPartyName === stockItemTransaction?.partyName ? (
              stockItemTransaction.quantity > 0 ? (
                <>
                  <span className="transaction-location">{stockItemTransaction.operationDestinationPartyName}</span>
                  <ArrowLeft size={16} /> {stockItemTransaction.operationSourcePartyName}
                </>
              ) : (
                <>
                  <span className="transaction-location">{stockItemTransaction.operationDestinationPartyName}</span>
                  <ArrowLeft size={16} /> {stockItemTransaction.operationSourcePartyName}
                </>
              )
            ) : (
              stockItemTransaction?.partyName
            )
          ) : (
            stockItemTransaction?.partyName
          ),
        transaction: stockItemTransaction?.isPatientTransaction
          ? 'Patient Dispense'
          : stockItemTransaction.stockOperationTypeName,
        quantity: `${stockItemTransaction?.quantity?.toLocaleString()} ${stockItemTransaction?.packagingUomName ?? ''}`,
        batch: stockItemTransaction.stockBatchNo
          ? `${stockItemTransaction.stockBatchNo}${
              stockItemTransaction.expiration ? ` (${formatDisplayDate(stockItemTransaction.expiration)})` : ''
            }`
          : '',
        reference: (
          <StockOperationReference
            operationUuid={stockItemTransaction?.stockOperationUuid}
            operationNumber={stockItemTransaction?.stockOperationNumber}
          />
        ),
        status: stockItemTransaction?.stockOperationStatus ?? '',
        in:
          stockItemTransaction?.quantity >= 0
            ? `${stockItemTransaction?.quantity?.toLocaleString()} ${stockItemTransaction?.packagingUomName ?? ''} of ${
                stockItemTransaction.packagingUomFactor
              }`
            : '',
        out:
          stockItemTransaction?.quantity < 0
            ? `${(-1 * stockItemTransaction?.quantity)?.toLocaleString()} ${
                stockItemTransaction?.packagingUomName ?? ''
              } of ${stockItemTransaction.packagingUomFactor}`
            : '',
        totalin:
          stockItemTransaction?.quantity >= 0
            ? `${stockItemTransaction?.quantity * Number(stockItemTransaction.packagingUomFactor)}`
            : '',
        totalout:
          stockItemTransaction?.quantity < 0
            ? `${-1 * stockItemTransaction?.quantity * Number(stockItemTransaction.packagingUomFactor)}`
            : '',
        balance: `${balance} ${stockItemTransaction?.packagingUomName ?? ''}`,
      };
    });
  }, [items, inventory]);

  if (isLoading) {
    return <DataTableSkeleton role="progressbar" />;
  }

  return (
    <DataList
      children={() => (
        <>
          <TransactionsPrintAction
            columns={binCardHeaders}
            data={tableRows}
            itemUuid={stockItemUuid}
            filter={stockItemFilter}
          />
          <TransactionsLocationsFilter
            onLocationIdChange={(q) => {
              setLocationUuid(q);
              setStockItemFilter({ ...stockItemFilter, locationUuid: q });
            }}
            name={'TransactionLocationUuid'}
            placeholder={t('filterByLocation', 'Filter by Location')}
            control={control}
            controllerName="TransactionLocationUuid"
          />
        </>
      )}
      columns={tableHeaders}
      data={tableRows}
      totalItems={totalCount}
      goToPage={setCurrentPage}
      hasToolbar={true}
    />
  );
};

export default Transactions;
