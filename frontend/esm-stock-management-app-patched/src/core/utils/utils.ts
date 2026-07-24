import { type User } from '../api/types/identity/User';

export const initialStockOperationValue = () => {
  return {
    atLocationName: '',
    atLocationUuid: '',
    cancelReason: '',
    cancelledBy: 0,
    cancelledByFamilyName: '',
    cancelledByGivenName: '',
    cancelledDate: undefined,
    completedBy: 0,
    completedByFamilyName: '',
    completedByGivenName: '',
    completedDate: undefined,
    creator: 0,
    creatorFamilyName: '',
    creatorGivenName: '',
    dateCreated: undefined,
    destinationName: '',
    destinationUuid: '',
    dispatchedByFamilyName: '',
    dispatchedByGivenName: '',
    dispatchedDate: undefined,
    externalReference: '',
    locked: false,
    operationDate: new Date(),
    operationNumber: '',
    operationOrder: 0,
    operationType: '',
    operationTypeName: '',
    operationTypeUuid: '',
    permission: undefined,
    reasonName: '',
    reasonUuid: '',
    rejectedByFamilyName: '',
    rejectedByGivenName: '',
    rejectedDate: undefined,
    rejectionReason: '',
    remarks: '',
    requisitionStockOperationUuid: '',
    responsiblePerson: 0,
    responsiblePersonFamilyName: '',
    responsiblePersonGivenName: '',
    responsiblePersonOther: '',
    responsiblePersonUuid: '',
    returnReason: '',
    returnedByFamilyName: '',
    returnedByGivenName: '',
    returnedDate: undefined,
    sourceName: '',
    sourceUuid: '',
    status: undefined,
    stockOperationItems: [],
    submitted: false,
    submittedBy: '',
    submittedByFamilyName: '',
    submittedByGivenName: '',
    submittedDate: undefined,
    uuid: '',
  };
};

export const pick = <T extends object>(obj: T, fields: string[]): Partial<T> => {
  if (!obj || typeof obj !== 'object') {
    throw new TypeError('First argument must be an object');
  }

  if (!Array.isArray(fields)) {
    throw new TypeError('Second argument must be an array');
  }

  const getNestedValue = (obj: any, path: string): any => {
    return path.split('.').reduce((acc, key) => (acc && acc[key] !== undefined ? acc[key] : undefined), obj);
  };

  return fields.reduce((result, field) => {
    const value = getNestedValue(obj, field);
    if (value !== undefined) {
      const keys = field.split('.');
      const lastKey = keys.pop()!;

      // Create nested structure in result
      keys.reduce((nested, key) => {
        if (!nested[key]) nested[key] = {};
        return nested[key];
      }, result as any)[lastKey] = value;
    }
    return result;
  }, {} as Partial<T>);
};

export const otherUser: User = {
  uuid: 'Other',
  display: 'Other',
  person: {
    display: 'Other',
  },
} as unknown as User;
