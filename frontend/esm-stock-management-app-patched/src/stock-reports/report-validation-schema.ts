import { z } from 'zod';

export const reportSchema = z
  .object({
    startDate: z.coerce.date().optional(),
    endDate: z.coerce.date().optional(),
    location: z.string().optional(),
    reportName: z.string({ required_error: 'Report Name Required' }).min(1, {
      message: 'Report Name Required',
    }),
    mostLeastMoving: z.string().optional(),
    mostLeastMovingName: z.string().optional(),
    stockItemUuid: z.string().optional(),
    stockItemName: z.string().optional(),
    patientUuid: z.string().optional(),
    patientName: z.string().optional(),
    locationUuid: z.string().optional(),
    childLocations: z.boolean().optional(),
    stockSourceUuid: z.string().optional(),
    stockSource: z.string().optional(),
    stockSourceDestinationUuid: z.string().optional(),
    stockSourceDestination: z.string().optional(),
    inventoryGroupBy: z.string().optional(),
    stockItemCategory: z.string().optional(),
    inventoryGroupByName: z.string().optional(),
    stockItemCategoryConceptUuid: z.string().optional(),
    reportSystemName: z.string().optional(),
    maxReorderLevelRatio: z.number().optional(),
    fullFillment: z.string().array().optional(),
    limit: z.string().optional(),
    date: z.coerce.date().optional(),
  })
  .superRefine((data, ctx) => {
    if (data.reportName !== 'Stock Status Report' && data.reportName !== 'Stock-Out Report') {
      if (!data.startDate) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'Start Date is required ',
          path: ['startDate'],
        });
      }
      if (!data.endDate) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'End Date is required',
          path: ['endDate'],
        });
      }
    }
    if (data.reportName === 'Stock Status Report' || data.reportName === 'Stock-Out Report') {
      if (!data.date) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'Date is required ',
          path: ['date'],
        });
      }
      if (!data.inventoryGroupBy) {
        ctx.addIssue({
          code: z.ZodIssueCode.custom,
          message: 'Inventory by is required ',
          path: ['inventoryGroupBy'],
        });
      }
    }
  });

export type StockReportSchema = z.infer<typeof reportSchema>;
