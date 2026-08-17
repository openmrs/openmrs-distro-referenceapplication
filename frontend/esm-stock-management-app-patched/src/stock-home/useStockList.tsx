import useSWR from 'swr';
import { openmrsFetch, restBaseUrl, useSession } from '@openmrs/esm-framework';
import { useStockItemQuantities } from '../stock-items/stock-item-quantities.resource';

interface StockListItem {
  uuid: string;
  hasExpiration: boolean;
  expiryNotice: number;
  reorderLevel: number | null | undefined;
  commonName?: string | null;
  drugName?: string | null;
  conceptName?: string | null;
}

const useStockList = () => {
  const { sessionLocation } = useSession();

  const stockItemsUrl = `${restBaseUrl}/stockmanagement/stockitem?v=default&totalCount=true`;
  const {
    data: stockItemsData,
    error: stockItemsError,
    isLoading: stockItemsLoading,
  } = useSWR<{ data: { results: Array<StockListItem> } }>(stockItemsUrl, openmrsFetch);

  const stockItems = stockItemsData?.data.results ?? [];
  const stockItemUuids = stockItems.map((item) => item.uuid);

  const {
    quantityByItem,
    error: quantityError,
    isLoading: quantityLoading,
  } = useStockItemQuantities(stockItemUuids, sessionLocation?.uuid);

  const itemDisplayName = (item: StockListItem) => item.commonName || item.drugName || item.conceptName || item.uuid;

  const outOfStockItems = stockItems
    .filter((item) => (quantityByItem?.get(item.uuid) ?? 0) <= 0)
    .map((item) => ({ ...item, displayName: itemDisplayName(item), quantity: quantityByItem?.get(item.uuid) ?? 0 }));

  const understockedItems = stockItems
    .filter((item) => {
      const quantity = quantityByItem?.get(item.uuid) ?? 0;
      return quantity > 0 && !!item.reorderLevel && quantity < item.reorderLevel;
    })
    .map((item) => ({ ...item, displayName: itemDisplayName(item), quantity: quantityByItem?.get(item.uuid) ?? 0 }));

  return {
    stockList: stockItems,
    outOfStockItems,
    understockedItems,
    isLoading: stockItemsLoading || quantityLoading,
    error: stockItemsError || quantityError,
  };
};

export default useStockList;
