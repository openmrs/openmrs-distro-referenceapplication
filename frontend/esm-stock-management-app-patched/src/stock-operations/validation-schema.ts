import { z } from 'zod';
import { StockOperationStatusTypes } from '../core/api/types/stockOperation/StockOperationStatus';
import { OperationType } from '../core/api/types/stockOperation/StockOperationType';

export const stockItemPackagingUOMDTOSchema = z.object({
  id: z.string().nullish(),
  uuid: z.string().nullish(),
  stockItemUuid: z.string().nullish(),
  packagingUomName: z.string().nullish(),
  packagingUomUuid: z.string().nullish(),
  factor: z.number().nullish(),
  isDefaultStockOperationsUoM: z.boolean().nullish(),
  isDispensingUnit: z.boolean().nullish(),
});

export type StockItemPackagingUOMDTOFormData = z.infer<typeof stockItemPackagingUOMDTOSchema>;

export const recordPermissionSchema = z.object({
  canView: z.boolean(),
  canEdit: z.boolean(),
  canApprove: z.boolean().nullish(),
  canReceiveItems: z.boolean().nullish(),
  canDisplayReceivedItems: z.boolean().nullish(),
  isRequisitionAndCanIssueStock: z.boolean().nullish(),
  canUpdateBatchInformation: z.boolean().nullish(),
});

export type RecordPermissionFormData = z.infer<typeof recordPermissionSchema>;

export const stockOperationItemSchema = z.object({
  itemIndex: z.number().nullish(),
  id: z.string().nullish(),
  uuid: z.string().nullish(),
  stockItemUuid: z.string().nullish(),
  stockItemName: z.string().nullish(),
  stockItemPackagingUOMUuid: z.string().nullish(),
  stockItemPackagingUOMName: z.string().nullish(),
  stockBatchUuid: z.string().nullish(),
  batchNo: z.string().nullish(),
  expiration: z.coerce.date().nullish(),
  quantity: z.number().nullish(),
  purchasePrice: z.number().nullish(),
  permission: recordPermissionSchema.nullish(),
  edit: z.boolean().nullish(),
  hasExpiration: z.boolean().nullish(),
  packagingUnits: stockItemPackagingUOMDTOSchema.array(),
  quantityReceived: z.number().nullish(),
  quantityReceivedPackagingUOMName: z.string().nullish(),
  quantityReceivedPackagingUOMUuid: z.string().nullish(),
  quantityRequested: z.number().nullish(),
  quantityRequestedPackagingUOMUuid: z.string().nullish(),
  quantityRequestedPackagingUOMName: z.string().nullish(),
  commonName: z.string().nullish(),
  acronym: z.string().nullish(),
});

export type StockOperationItemFormData = z.infer<typeof stockOperationItemSchema>;

export const stockOperationSchema = z.object({
  uuid: z.string().nullish(),
  cancelReason: z.string().nullish(),
  cancelledBy: z.coerce.number(),
  cancelledByGivenName: z.string().nullish(),
  cancelledByFamilyName: z.string().nullish(),
  cancelledDate: z.coerce.date(),
  completedBy: z.coerce.number(),
  completedByGivenName: z.string().nullish(),
  completedByFamilyName: z.string().nullish(),
  completedDate: z.coerce.date(),
  destinationUuid: z.string().nullish(),
  destinationName: z.string().nullish(),
  externalReference: z.string().nullish(),
  atLocationUuid: z.string().nullish(),
  atLocationName: z.string().nullish(),
  operationDate: z.coerce.date(),
  submitted: z.boolean(),
  submittedBy: z.string().nullish(),
  submittedByGivenName: z.string().nullish(),
  submittedByFamilyName: z.string().nullish(),
  locked: z.boolean(),
  operationNumber: z.string().nullish(),
  operationOrder: z.coerce.number(),
  remarks: z.string().nullish(),
  sourceUuid: z.string().nullish(),
  sourceName: z.string().nullish(),
  status: z.enum(StockOperationStatusTypes),
  returnReason: z.string().nullish(),
  rejectionReason: z.string().nullish(),
  operationTypeUuid: z.string().nullish(),
  operationType: z.string().nullish(),
  operationTypeName: z.string().nullish(),
  responsiblePerson: z.coerce.number(),
  responsiblePersonUuid: z.string().nullish(),
  responsiblePersonGivenName: z.string().nullish(),
  responsiblePersonFamilyName: z.string().nullish(),
  responsiblePersonOther: z.string().nullish(),
  creator: z.coerce.number(),
  dateCreated: z.coerce.date(),
  creatorGivenName: z.string().nullish(),
  creatorFamilyName: z.string().nullish(),
  permission: recordPermissionSchema.nullish(),
  reasonUuid: z.string().nullish(),
  reasonName: z.string().nullish(),
  approvalRequired: z.boolean().nullish(),
  stockOperationItems: stockOperationItemSchema.array(),

  submittedDate: z.coerce.date(),
  returnedByGivenName: z.string().nullish(),
  returnedByFamilyName: z.string().nullish(),
  returnedDate: z.coerce.date(),
  rejectedByGivenName: z.string().nullish(),
  rejectedByFamilyName: z.string().nullish(),
  rejectedDate: z.coerce.date(),
  dispatchedByGivenName: z.string().nullish(),
  dispatchedByFamilyName: z.string().nullish(),
  dispatchedDate: z.coerce.date(),
  requisitionStockOperationUuid: z.string().uuid().nullish(),
});
export const baseStockOperationItemSchema = z.object({
  uuid: z.string().min(1, 'Required'),
  stockItemUuid: z.string().min(1, { message: 'Required' }),
  stockItemPackagingUOMUuid: z.string().min(1, { message: 'Required' }),
  batchNo: z.string().min(1, { message: 'Required' }),
  stockBatchUuid: z.string().optional(),
  expiration: z.coerce.date({ required_error: 'Required' }),
  quantity: z.coerce.number().min(1, { message: 'Required' }),
  purchasePrice: z.coerce.number().nullish(),
  hasExpiration: z.boolean().nullish(),
});

export type BaseStockOperationItemFormData = z.infer<typeof baseStockOperationItemSchema>;

export const getStockOperationItemFormSchema = (operationType: OperationType) => {
  switch (operationType) {
    case OperationType.RECEIPT_OPERATION_TYPE:
    case OperationType.OPENING_STOCK_OPERATION_TYPE:
      return baseStockOperationItemSchema.omit({ stockBatchUuid: true });
    case OperationType.REQUISITION_OPERATION_TYPE:
      return baseStockOperationItemSchema.omit({
        batchNo: true,
        stockBatchUuid: true,
        expiration: true,
        purchasePrice: true,
      });
    case OperationType.ADJUSTMENT_OPERATION_TYPE:
      return baseStockOperationItemSchema
        .omit({
          batchNo: true,
          expiration: true,
          purchasePrice: true,
        })
        .extend({
          // Override quantity to allow negative values for negative adjustment operation
          quantity: z.coerce.number().refine((value) => value !== 0, {
            message: 'Quantity cannot be zero.',
          }),
        });
    case OperationType.DISPOSED_OPERATION_TYPE:
    case OperationType.RETURN_OPERATION_TYPE:
    case OperationType.STOCK_ISSUE_OPERATION_TYPE:
    case OperationType.STOCK_TAKE_OPERATION_TYPE:
    case OperationType.TRANSFER_OUT_OPERATION_TYPE:
      return baseStockOperationItemSchema.omit({
        batchNo: true,
        expiration: true,
        purchasePrice: true,
      });
    default:
      return baseStockOperationItemSchema;
  }
};
export const stockOperationItemDtoSchema = z.object({
  operationDate: z.coerce.date(),
  sourceUuid: z.string({ required_error: 'Location Required' }).min(1, {
    message: 'Location Required',
  }),
  destinationUuid: z.string({ required_error: 'Location Required' }).min(1, {
    message: 'Location Required',
  }),
  reasonUuid: z.string({ required_error: 'Reason Required' }).min(1, {
    message: 'Reason Required',
  }),
  responsiblePersonUuid: z
    .string({
      required_error: 'Responsible Person Required',
    })
    .min(1, {
      message: 'Responsible Person Required',
    }),
  responsiblePersonOther: z.string().nullish(),
  remarks: z.string().nullish(),
  operationTypeUuid: z.string().min(1, 'Operation type required').uuid('Invalid operation type'),
  stockOperationItems: baseStockOperationItemSchema.array().nonempty('You must add atleast one stock item'),
  requisitionStockOperationUuid: z.string().uuid().optional(), // Suplied only for stock issue operation
});

export type StockOperationItemDtoSchema = z.infer<typeof stockOperationItemDtoSchema>;

export type StockOperationFormData = z.infer<typeof stockOperationSchema>;

export const getStockOperationFormSchema = (operation: OperationType): z.Schema => {
  switch (operation) {
    case OperationType.OPENING_STOCK_OPERATION_TYPE:
      return stockOperationItemDtoSchema
        .omit({
          destinationUuid: true,
          reasonUuid: true,
        })
        .merge(
          z.object({
            stockOperationItems: getStockOperationItemFormSchema(operation)
              .array()
              .nonempty('You must add atleast one stock item'),
          }),
        );
    case OperationType.STOCK_TAKE_OPERATION_TYPE:
    case OperationType.ADJUSTMENT_OPERATION_TYPE:
    case OperationType.DISPOSED_OPERATION_TYPE:
      return stockOperationItemDtoSchema.omit({ destinationUuid: true }).merge(
        z.object({
          stockOperationItems: getStockOperationItemFormSchema(operation)
            .array()
            .nonempty('You must add atleast one stock item'),
        }),
      );
    case OperationType.TRANSFER_OUT_OPERATION_TYPE:
    case OperationType.STOCK_ISSUE_OPERATION_TYPE:
      return stockOperationItemDtoSchema.omit({ reasonUuid: true }).merge(
        z.object({
          // Merged to overid initial one with error message having  location instead of destination
          destinationUuid: z.string({ required_error: 'Destination Required' }).min(1, {
            message: 'Destination Required',
          }),
          stockOperationItems: getStockOperationItemFormSchema(operation)
            .array()
            .nonempty('You must add atleast one stock item'),
        }),
      );
    case OperationType.RETURN_OPERATION_TYPE:
    case OperationType.REQUISITION_OPERATION_TYPE:
    case OperationType.RECEIPT_OPERATION_TYPE:
      return stockOperationItemDtoSchema.omit({ reasonUuid: true }).merge(
        z.object({
          // Merged to overid initial one with error message having location instead of source
          sourceUuid: z.string({ required_error: 'Source Required' }).min(1, {
            message: 'Source Required',
          }),
          stockOperationItems: getStockOperationItemFormSchema(operation)
            .array()
            .nonempty('You must add atleast one stock item'),
        }),
      );
  }
};
