import React, { useEffect, useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { ResourceRepresentation } from '../../../core/api/api';
import { formatDisplayDate } from '../../../core/utils/datetimeUtils';
import { DataTableSkeleton } from '@carbon/react';
import { useStockItemQuantitiesHook } from './quantities.resource';
import DataList from '../../../core/components/table/table.component';

interface StockQuantitiesProps {
  onSubmit?: () => void;
  stockItemUuid: string;
}

const StockQuantities: React.FC<StockQuantitiesProps> = ({ stockItemUuid }) => {
  const { isLoading, items, totalCount, setCurrentPage, setStockItemUuid } = useStockItemQuantitiesHook(
    ResourceRepresentation.Default,
  );
  const { t } = useTranslation();

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
        key: 'quantity',
        header: t('quantity', 'Quantity'),
      },
      {
        key: 'packaging',
        header: t('packagingUnit', 'Packaging Unit'),
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
      columns={tableHeaders}
      data={tableRows}
      totalItems={totalCount}
      goToPage={setCurrentPage}
      hasToolbar={false}
    />
  );
};

export default StockQuantities;
