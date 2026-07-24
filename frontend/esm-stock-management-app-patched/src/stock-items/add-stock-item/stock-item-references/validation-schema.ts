import { z } from 'zod';

export const stockItemReferenceschema = z.object({
  code: z.string().nullish(),
  references: z.string().nullish(),
});

export type StockItemReferenceData = z.infer<typeof stockItemReferenceschema>;
