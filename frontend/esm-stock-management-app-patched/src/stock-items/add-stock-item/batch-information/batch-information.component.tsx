import React, { useEffect, useMemo, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { useForm } from 'react-hook-form';
import { formatDisplayDate } from '../../../core/utils/datetimeUtils';
import { DataTableSkeleton } from '@carbon/react';
import { type StockItemInventoryFilter } from '../../stock-items.resource';
import { useStockItemBatchInformationHook } from './batch-information.resource';
import BatchInformationLocationsFilter from './batch-information-locations/batch-information-locations-filter.component';
import DataList from '../../../core/components/table/table.component';

interface BatchInformationProps {
  onSubmit?: () => void;
  stockItemUuid: string;
}

const BatchInformation: React.FC<BatchInformationProps> = ({ stockItemUuid }) => {
  const [stockItemFilter, setStockItemFilter] = useState<StockItemInventoryFilter>();

  const { isLoading, items, totalCount, setCurrentPage, setStockItemUuid, setLocationUuid } =
    useStockItemBatchInformationHook(stockItemFilter);
  const { t } = useTranslation();
  const { control } = useForm({});

  useEffect(() => {
    setStockItemUuid(stockItemUuid);
  }, [stockItemUuid, setStockItemUuid]);
  const tableHeaders = useMemo(
    () => [
      {
        key: 'location',
        header: t('location', 'Location'),
      },
      {
        key: 'batch',
        header: t('batchNumber', 'Batch number'),
      },
      {
        key: 'quantity',
        header: t('quantity', 'Quantity'),
      },
      {
        key: 'packaging',
        header: t('packagingUnit', 'Packaging Unit'),
      },
      {
        key: 'expires',
        header: t('expires', 'Expires'),
      },
    ],
    [t],
  );

  const tableRows = useMemo(() => {
    return items?.map((row, index) => ({
      ...row,
      id: `${row.partyUuid}${row.stockBatchUuid}${index}`,
      key: `${row.partyUuid}${row.stockBatchUuid}${index}`,
      uuid: `${row.partyUuid}${row.stockBatchUuid}${index}`,
      expires: formatDisplayDate(row?.expiration),
      location: row?.partyName,
      quantity: row?.quantity?.toLocaleString() ?? '',
      batch: row.batchNumber ?? '',
      packaging: `${row.quantityUoM ?? ''} of ${row.quantityFactor ?? ''}`,
    }));
  }, [items]);

  if (isLoading) {
    return <DataTableSkeleton role="progressbar" />;
  }

  return (
    <DataList
      children={() => (
        <>
          <BatchInformationLocationsFilter
            control={control}
            onLocationIdChange={(q) => {
              setLocationUuid(q);
              setStockItemFilter({
                ...stockItemFilter,
                locationUuid: q,
              });
            }}
            placeholder={t('filterByLocation', 'Filter by Location')}
            name="BatchLocationUuid"
            controllerName="BatchLocationUuid"
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

export default BatchInformation;
