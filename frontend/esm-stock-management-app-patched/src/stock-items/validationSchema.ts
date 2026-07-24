import { type TFunction } from 'i18next';
import { z } from 'zod';

const nullableString = z.string().max(255).nullish();

// Stock item details
export const createStockItemDetailsSchema = (t: TFunction) =>
  z
    .object({
      isDrug: z.boolean({ required_error: t('selectItemType', 'Please select an item type') }),
      drugUuid: z.string().nullish(),
      drugName: z.string().nullish(),
      commonName: nullableString,
      acronym: nullableString,
      hasExpiration: z.boolean({
        required_error: t('indicateWhetherItemExpires', 'Please indicate whether the item expires'),
      }),
      expiryNotice: z.coerce.number().nullish(),
      uuid: z.string().nullish(),
      conceptUuid: z.string().nullish(),
      conceptName: z.string().nullish(),
      preferredVendorUuid: z.string().nullish(),
      preferredVendorName: z.string().nullish(),
      purchasePrice: z.coerce.number().nullish(),
      purchasePriceUoMUuid: z.string().nullish(),
      purchasePriceUoMName: z.string().nullish(),
      categoryUuid: z.string().nullish(),
      categoryName: z.string().nullish(),
      dispensingUnitUuid: z.string().nullish(),
      dispensingUnitName: z.string().nullish(),
      dispensingUnitPackagingUoMUuid: z.string().nullish(),
      dispensingUnitPackagingUoMName: z.string().nullish(),
      defaultStockOperationsUoMUuid: z.string().nullish(),
      defaultStockOperationsUoMName: z.string().nullish(),
      reorderLevel: z.coerce.number().nullish(),
      reorderLevelUoMUuid: z.string().nullish(),
      reorderLevelUoMName: z.string().nullish(),
      dateCreated: z.coerce.date().nullish(),
      creatorGivenName: z.string().nullish(),
      creatorFamilyName: z.string().nullish(),
      voided: z.boolean().nullish(),
    })
    .refine(
      ({ isDrug, drugUuid }) => {
        return isDrug ? !!drugUuid : true;
      },
      {
        message: t('drugRequired', 'Drug required'),
        path: ['drugUuid'],
      },
    )
    .refine(
      ({ isDrug, dispensingUnitUuid }) => {
        return isDrug ? !!dispensingUnitUuid : true;
      },
      {
        message: t('dispensingUnitRequired', 'Dispensing unit required'),
        path: ['dispensingUnitUuid'],
      },
    )
    .refine(
      ({ hasExpiration, expiryNotice }) => {
        return hasExpiration ? expiryNotice != null : true;
      },
      {
        message: t('expiryNoticeRequired', 'Expiry notice required'),
        path: ['expiryNotice'],
      },
    )
    .refine(
      ({ purchasePrice, purchasePriceUoMUuid }) => {
        return purchasePrice != null && purchasePrice > 0 ? !!purchasePriceUoMUuid : true;
      },
      {
        message: t('purchasePriceUoMRequired', 'Purchase price packaging unit is required when purchase price is set'),
        path: ['purchasePriceUoMUuid'],
      },
    );

export type StockItemFormData = z.infer<ReturnType<typeof createStockItemDetailsSchema>>;
