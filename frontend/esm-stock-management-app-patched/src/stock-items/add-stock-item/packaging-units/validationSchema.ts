import { z } from 'zod';

export const packageUnitSchema = z.object({
  id: z.string().nullish(),
  uuid: z.string().nullish(),
  stockItemUuid: z.string().nullish(),
  packagingUomName: z.string().nullish(),
  packagingUomUuid: z.string().nullish(),
  factor: z.coerce.number().nullish(),
  isDefaultStockOperationsUoM: z.boolean().nullish(),
  isDispensingUnit: z.boolean().nullish(),
});

export type PackageUnitFormData = z.infer<typeof packageUnitSchema>;
