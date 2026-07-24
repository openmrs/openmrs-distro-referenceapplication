export const adjustmentOpeationTypeMock = {
  uuid: '11111111-1111-1111-1111-111111111111',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Adjustment',
  description: 'Physical item quantities that do not match the current system quantity.',
  operationType: 'adjustment',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: false,
  destinationType: null,
  availableWhenReserved: true,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: 'f958ad0a-2a55-11ed-9cab-507b9dea1806',
      dateCreated: '2024-03-13T15:41:25.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/f958ad0a-2a55-11ed-9cab-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '019f2d4b-2a56-11ed-9cab-507b9dea1806',
      dateCreated: '2024-03-13T15:41:25.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/019f2d4b-2a56-11ed-9cab-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '7228d114-2abc-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:25.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/7228d114-2abc-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const disposalOperationTypeMock = {
  uuid: '22222222-2222-2222-2222-222222222222',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Disposal',
  description: 'Item stock quantities that have expired and must be removed from circulation.',
  operationType: 'disposed',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: false,
  destinationType: null,
  availableWhenReserved: true,
  allowExpiredBatchNumbers: true,
  stockOperationTypeLocationScopes: [
    {
      uuid: '12de41c9-28a1-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:26.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/12de41c9-28a1-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '8bee3005-2ae6-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:26.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/8bee3005-2ae6-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '83fbf8ca-2ae6-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:27.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/83fbf8ca-2ae6-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const tranferOutOperationTypeMock = {
  uuid: '33333333-3333-3333-3333-333333333333',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Transfer Out',
  description: 'Items that are distributed to other outside destination.',
  operationType: 'transferout',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: true,
  destinationType: 'Other',
  availableWhenReserved: true,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: '5b341a26-28a2-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:27.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/5b341a26-28a2-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const receiptOperationTypeMock = {
  uuid: '44444444-4444-4444-4444-444444444444',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Receipt',
  description: 'Items that are added into the inventory system from an outside provider.',
  operationType: 'receipt',
  hasSource: true,
  sourceType: 'Other',
  hasDestination: true,
  destinationType: 'Location',
  availableWhenReserved: false,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: '01c9285a-28a3-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:28.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: false,
      isDestination: true,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/01c9285a-28a3-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const returnOperationTypeMock = {
  uuid: '55555555-5555-5555-5555-555555555555',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Return',
  description: 'Items that are returned to the main store from other stock holding areas.',
  operationType: 'return',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: true,
  destinationType: 'Location',
  availableWhenReserved: true,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: '2c5772e7-28a4-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:28.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/2c5772e7-28a4-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '6e26757a-2abe-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:29.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/6e26757a-2abe-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '27ff4a05-28a4-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:29.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: false,
      isDestination: true,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/27ff4a05-28a4-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const stockIssueOperationtypeMock = {
  uuid: '66666666-6666-6666-6666-666666666666',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Stock Issue',
  description: 'Items that are transferred between two locations.',
  operationType: 'stockissue',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: true,
  destinationType: 'Location',
  availableWhenReserved: true,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: '20fac1fa-28a4-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:29.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: false,
      isDestination: true,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/20fac1fa-28a4-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '11ebab13-2abf-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:30.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: false,
      isDestination: true,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/11ebab13-2abf-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '1c984ba0-28a4-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:30.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/1c984ba0-28a4-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const requisitionOperationTypeMock = {
  uuid: '77777777-7777-7777-7777-777777777777',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Requisition',
  description: 'Request for items from another location.',
  operationType: 'requisition',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: true,
  destinationType: 'Location',
  availableWhenReserved: false,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: 'dfba06ad-2abe-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:31.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/dfba06ad-2abe-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '161ccea9-28a4-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:30.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/161ccea9-28a4-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '11ba3575-28a4-11ed-bdcb-507b9dea1806',
      dateCreated: '2024-03-13T15:41:31.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: false,
      isDestination: true,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/11ba3575-28a4-11ed-bdcb-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const stockTakeOperationTypeMock = {
  uuid: '88888888-8888-8888-8888-888888888888',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Stock Take',
  description: 'Physical verification of item quantities.',
  operationType: 'stocktake',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: false,
  destinationType: null,
  availableWhenReserved: true,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: '86fad5b3-2abf-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:32.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/86fad5b3-2abf-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '90b9f544-2abf-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:31.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/90b9f544-2abf-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '8ab5bb1d-2abf-11ed-ba4a-507b9dea1806',
      dateCreated: '2024-03-13T15:41:32.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/8ab5bb1d-2abf-11ed-ba4a-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const openingStockOperationTypeMock = {
  uuid: '99999999-9999-9999-9999-999999999999',
  dateCreated: '2022-07-31T00:00:00.000+0300',
  dateChanged: null,
  name: 'Opening Stock',
  description: 'Initial physical item quantities.',
  operationType: 'initial',
  hasSource: true,
  sourceType: 'Location',
  hasDestination: false,
  destinationType: null,
  availableWhenReserved: false,
  allowExpiredBatchNumbers: false,
  stockOperationTypeLocationScopes: [
    {
      uuid: '99e64f69-2e90-11ed-a5a0-507b9dea1806',
      dateCreated: '2024-03-13T15:41:33.000+0300',
      dateChanged: null,
      locationTag: 'Main Pharmacy',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/99e64f69-2e90-11ed-a5a0-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: '886b6f2d-2e90-11ed-a5a0-507b9dea1806',
      dateCreated: '2024-03-13T15:41:32.000+0300',
      dateChanged: null,
      locationTag: 'Main Store',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/886b6f2d-2e90-11ed-a5a0-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
    {
      uuid: 'a24c5c90-2e90-11ed-a5a0-507b9dea1806',
      dateCreated: '2024-03-13T15:41:33.000+0300',
      dateChanged: null,
      locationTag: 'Dispensary',
      isSource: true,
      isDestination: false,
      links: [
        {
          rel: 'full',
          uri: 'http://hie.kenyahmis.org/openmrs/ws/rest/v1/stockmanagement/stockoperationtypelocationscope/a24c5c90-2e90-11ed-a5a0-507b9dea1806?v=full',
          resourceAlias: 'stockoperationtypelocationscope',
        },
      ],
      resourceVersion: '1.8',
    },
  ],
};

export const operationTypesMock = [
  adjustmentOpeationTypeMock,
  disposalOperationTypeMock,
  tranferOutOperationTypeMock,
  receiptOperationTypeMock,
  returnOperationTypeMock,
  stockIssueOperationtypeMock,
  requisitionOperationTypeMock,
  stockTakeOperationTypeMock,
  openingStockOperationTypeMock,
];
