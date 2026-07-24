import { type StockItemDTO } from '../core/api/types/stockItem/StockItem';
import { type StockOperationDTO } from '../core/api/types/stockOperation/StockOperationDTO';
import { type KeyedMutator } from 'swr';

export type SaveStockItem = (item: StockItemDTO) => Promise<void>;
export type SaveStockOperation = (item: StockOperationDTO) => Promise<void>;
export type SaveStockOperationAction = (item: StockOperationDTO) => Promise<void>;

export type LocationMutator = KeyedMutator<{
  data: {
    results: Array<Location>;
  };
}>;

export type Location = {
  uuid: string;
  display: string;
  name: string;
  description: string | null;
  address1: string | null;
  address2: string | null;
  cityVillage: string | null;
  stateProvince: string | null;
  country: string | null;
  postalCode: string | null;
  latitude: string | null;
  longitude: string | null;
  countyDistrict: string | null;
  address3: string | null;
  address4: string | null;
  address5: string | null;
  address6: string | null;
  tags: Tag[];
  parentLocation: Location;
  childLocations: Location[];
  retired: boolean;
  auditInfo: {
    creator: {
      uuid: string;
      display: string;
      links: Array<{
        rel: string;
        uri: string;
        resourceAlias: string;
      }>;
    };
    dateCreated: string;
    changedBy: null;
    dateChanged: null;
  };
  address7: string | null;
  address8: string | null;
  address9: string | null;
  address10: string | null;
  address11: string | null;
  address12: string | null;
  address13: string | null;
  address14: string | null;
  address15: string | null;
  links: Array<{
    rel: string;
    uri: string;
    resourceAlias: string;
  }>;
  resourceVersion: string;
};

export type Tag = {
  uuid: string;
  display: string;
  name: string;
  description?: string;
  retired: boolean;
  links: Array<{
    rel: string;
    uri: string;
    resourceAlias: string;
  }>;
  resourceVersion: string;
};
export interface locationData {
  uuid?: string;
  name: string;
  tags: [];
}
