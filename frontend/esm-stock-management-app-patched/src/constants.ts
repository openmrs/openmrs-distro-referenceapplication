import dayjs from 'dayjs';
import { formatDisplayDate } from './core/utils/datetimeUtils';

export const moduleName = '@openmrs/esm-stock-management-app';
export const spaRoot = `${window['getOpenmrsSpaBase']}`;
export const omrsDateFormat = 'YYYY-MM-DDTHH:mm:ss.SSSZZ';
export const startOfDay = dayjs(new Date().setUTCHours(0, 0, 0, 0)).format(omrsDateFormat);
export const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;

export const DATE_PICKER_FORMAT = 'DD/MM/YYYY';
export const DATE_PICKER_CONTROL_FORMAT = 'd/m/Y';

export const formatForDatePicker = (date: Date | null | undefined) => {
  return formatDisplayDate(date, DATE_PICKER_FORMAT);
};

export const today = () => {
  const date = new Date();
  return new Date(date.getFullYear(), date.getMonth(), date.getDate());
};

export const StockFilters = Object.freeze({
  SOURCES: 'Sources',
  OPERATION: 'Operation',
  STATUS: 'Status',
});

// privileges
/** @type {string}: App: stockmanagement.dashboard, Able to view stock management application dashboard*/
export const APP_STOCKMANAGEMENT_DASHBOARD = 'App: stockmanagement.dashboard';

/** @type {string}: App: stockmanagement.stockItems, Able to view stock items*/
export const APP_STOCKMANAGEMENT_STOCKITEMS = 'App: stockmanagement.stockItems';

/** @type {string}: Task: stockmanagement.stockItems.mutate, Able to create and update  stock items*/
export const TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE = 'Task: stockmanagement.stockItems.mutate';

/** @type {string}: Task: stockmanagement.stockItems.dispense.qty, Able to view stock item quantities at dispensing locations*/
export const TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY = 'Task: stockmanagement.stockItems.dispense.qty';

/** @type {string}: Task: stockmanagement.stockItems.dispense, Able to dispense stock items*/
export const TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE = 'Task: stockmanagement.stockItems.dispense';

/** @type {string}: App: stockmanagement.userRoleScopes, Able to view stock management user role scope*/
export const APP_STOCKMANAGEMENT_USERROLESCOPES = 'App: stockmanagement.userRoleScopes';

/** @type {string}: Task: stockmanagement.userRoleScopes.mutate, Able to create and update user role scopes*/
export const TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE = 'Task: stockmanagement.userRoleScopes.mutate';

/** @type {string}: App: stockmanagement.stockoperations, Able to view stock operations*/
export const APP_STOCKMANAGEMENT_STOCKOPERATIONS = 'App: stockmanagement.stockoperations';

/** @type {string}: Task: stockmanagement.stockoperations.mutate, Able to create and update stock operations*/
export const TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE = 'Task: stockmanagement.stockoperations.mutate';

/** @type {string}: Task: stockmanagement.stockoperations.approve, Able to approve stock operations*/
export const TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE = 'Task: stockmanagement.stockoperations.approve';

/** @type {string}: Task: stockmanagement.stockoperations.receiveitems, Able to receive dispatched stock items*/
export const TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS = 'Task: stockmanagement.stockoperations.receiveitems';

/** @type {string}: App: stockmanagement.stockSources, Able to view stock sources*/
export const APP_STOCKMANAGEMENT_STOCKSOURCES = 'App: stockmanagement.stockSources';

/** @type {string}: Task: stockmanagement.stockSources.mutate, Able to create and update stock sources*/
export const TASK_STOCKMANAGEMENT_STOCKSOURCES_MUTATE = 'Task: stockmanagement.stockSources.mutate';

/** @type {string}: App: stockmanagement.stockOperationType, Able to view stock operation types*/
export const APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE = 'App: stockmanagement.stockOperationType';

/** @type {string}: Task: stockmanagement.party.read, Able to read party information*/
export const TASK_STOCKMANAGEMENT_PARTY_READ = 'Task: stockmanagement.party.read';

/** @type {string}: App: stockmanagement.reports, Able to view stock reports*/
export const APP_STOCKMANAGEMENT_REPORTS_VIEW = 'App: stockmanagement.reports';

/** @type {string}: Task: stockmanagement.reports.mutate, Able to create stock reports*/
export const TASK_STOCKMANAGEMENT_REPORTS_MUTATE = 'Task: stockmanagement.reports.mutate';

/** @type {string}: Task: stockmanagement.reports.mutate, Able to create stock reports*/

export const INVENTORY_CLERK_ROLE_UUID = 'd210eb66-2188-11ed-9dff-507b9dea1806';
export const INVENTORY_MANAGER_ROLE_UUID = 'cca4be4b-2188-11ed-9dff-507b9dea1806';
export const INVENTORY_DISPENSING_ROLE_UUID = '84bdd876-4694-11ed-8109-00155dcc3fc0';
export const INVENTORY_REPORTING_ROLE_UUID = 'a49be648-6b0a-11ed-93a2-806d973f13a9';
export const INVENTORY_ADMINISTRATOR_ROLE_UUID = '2083fd40-3391-11ed-a667-507b9dea1806';

export const STOCK_OPERATION_PRINT_DISABLE_BALANCE_ON_HAND = true;
export const STOCK_OPERATION_PRINT_DISABLE_COSTS = true;
export const HEALTH_CENTER_NAME = 'Health Center';
export const PRINT_LOGO = 'moduleResources/stockmanagement/assets/print-logo.svg';
export const PRINT_LOGO_TEXT = 'Ministry of Health';

export const MAIN_STORE_LOCATION_TAG = 'Main Store';
export const BASE_OPENMRS_APP_URL = '/openmrs/';

export const STOCKMGMT_RESOURCE_URL = '/openmrs/stockmanagement/spa.page/';
export const STOCKMGMT_SPA_PAGE_URL = '/openmrs/stockmanagement/spa.page';
export const URL_PRINT_LOGO = () =>
  PRINT_LOGO
    ? `${BASE_OPENMRS_APP_URL}${PRINT_LOGO}`
    : 'data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7';
export const CLOSE_PRINT_AFTER_PRINT = true;
export const ALLOW_STOCK_ISSUE_WITHOUT_REQUISITION = false;

export const BASE_URL_CONFIGURED = BASE_OPENMRS_APP_URL;
export const LOGIN_URL = BASE_OPENMRS_APP_URL + 'login.htm';

export const ROUTING_BASE_URL = '/';
export const URL_STOCK_HOME = ROUTING_BASE_URL + 'home';

export const URL_STOCK_OPERATIONS = ROUTING_BASE_URL + 'stock-operations';
export const URL_STOCK_OPERATIONS_ROUTES = URL_STOCK_OPERATIONS + '/*';
export const URL_STOCK_OPERATIONS_NEW = URL_STOCK_OPERATIONS + '/new/*';
export const URL_STOCK_OPERATIONS_NEW_OPERATION = (name: string, requisitionUuid?: string): string =>
  `${URL_STOCK_OPERATIONS}/new/${name}${requisitionUuid ? `?requisition=${requisitionUuid}` : ''}`;
export const URL_STOCK_OPERATIONS_EDIT = URL_STOCK_OPERATIONS + '/:id';
export const URL_STOCK_OPERATIONS_REDIRECT = (uuid: string, tab?: string): string =>
  `${URL_STOCK_OPERATIONS}/redirect/${uuid}${tab ? `?tab=${tab}` : ''}`;
export const URL_STOCK_OPERATION = (uuid: string, tab?: string): string =>
  `${URL_STOCK_OPERATIONS}/${uuid}${tab ? `?tab=${tab}` : ''}`;

export const URL_USER_ROLE_SCOPES = ROUTING_BASE_URL + 'user-role-scopes';
export const URL_USER_ROLE_SCOPES_ROUTES = URL_USER_ROLE_SCOPES + '/*';
export const URL_USER_ROLE_SCOPES_NEW = URL_USER_ROLE_SCOPES + '/new';
export const URL_USER_ROLE_SCOPES_EDIT = URL_USER_ROLE_SCOPES + '/:id';
export const URL_USER_ROLE_SCOPE = (uuid: string): string => `${URL_USER_ROLE_SCOPES}/${uuid}`;

export const URL_STOCK_ITEMS = ROUTING_BASE_URL + 'stock-items';
export const URL_STOCK_ITEMS_ROUTES = ROUTING_BASE_URL + 'stock-items/*';
export const URL_STOCK_ITEMS_NEW = URL_STOCK_ITEMS + '/new';
export const URL_STOCK_ITEMS_EDIT = URL_STOCK_ITEMS + '/:id';
export const URL_STOCK_ITEM = (uuid: string, tab?: string): string =>
  `${URL_STOCK_ITEMS}/${uuid}${tab ? `?tab=${tab}` : ''}`;
export const URL_STOCK_ITEMS_REDIRECT = (uuid: string, tab?: string): string =>
  `${URL_STOCK_ITEMS}/redirect/${uuid}${tab ? `?tab=${tab}` : ''}`;
export const URL_IMPORT_ERROR_FILE = (importSessionId: string) =>
  `${BASE_OPENMRS_APP_URL}ws/rest/v1/stockmanagement/stockitemimport?id=${importSessionId}`;
export const URL_IMPORT_TEMPLATE_FILE = `${BASE_OPENMRS_APP_URL}moduleResources/stockmanagement/templates/Import_Stock_Items.xlsx`;

export const URL_STOCK_SOURCES = ROUTING_BASE_URL + 'stock-sources';
export const URL_STOCK_SOURCES_ROUTES = URL_STOCK_SOURCES + '/*';

export const URL_SIGN_IN = ROUTING_BASE_URL + 'sign-in';
export const URL_SIGN_OUT = ROUTING_BASE_URL + 'sign-out';

export const URL_ACCESS_DENIED = ROUTING_BASE_URL + 'access-denied';
export const URL_NOT_FOUND = '/*';
export const URL_WILDCARD = '*';

export const REACT_ROUTER_PREFIX = '#';

export const URL_LOCATIONS = ROUTING_BASE_URL + 'locations';
export const URL_LOCATIONS_ROUTES = ROUTING_BASE_URL + 'locations/*';
export const URL_LOCATIONS_NEW = () => `${BASE_OPENMRS_APP_URL}admin/locations/location.form`;
export const URL_LOCATIONS_EDIT = (id: number) =>
  `${BASE_OPENMRS_APP_URL}admin/locations/location.form?locationId=${id}`;

export const URL_STOCK_REPORTS = ROUTING_BASE_URL + 'stock-reports';
export const URL_STOCK_REPORTS_ROUTES = ROUTING_BASE_URL + 'stock-reports/*';
export const URL_STOCK_REPORT = (uuid: string): string => `${URL_STOCK_REPORTS}/${uuid}`;
export const URL_BATCH_JOB_ARTIFACT = (uuid: string, download: boolean): string =>
  `${BASE_OPENMRS_APP_URL}ws/rest/v1/stockmanagement/batchjobartifact?id=${uuid}${download ? '&download=1' : ''}`;

export function extractErrorMessagesFromResponse(errorObject) {
  const fieldErrors = errorObject?.responseBody?.error?.fieldErrors;
  if (!fieldErrors) {
    return [errorObject?.responseBody?.error?.message ?? errorObject?.message];
  }
  return Object.values(fieldErrors).flatMap((errors: Array<Error>) => errors.map((error) => error.message));
}
