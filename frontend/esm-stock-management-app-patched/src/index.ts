import { defineConfigSchema, getAsyncLifecycle, getSyncLifecycle } from '@openmrs/esm-framework';
import { configSchema } from './config-schema';
import { createDashboardLink } from './createDashboardLink';
import appMenu from './stock-app-menu-item/item.component';
import deletePackagingUnitModalButtonComponent from './stock-items/add-stock-item/packaging-units/delete-packaging-unit-action-button.component';
import Root from './root.component';
import SideMenu from './side-menu/side-menu.component';
import StockHomeLandingPage from './stock-home/stock-home-landing-page-component';
import StockItems from './stock-items/stock-items.component';
import StockLocations from './stock-locations/stock-locations.component';
import stockManagementAdminCardLinkComponent from './stock-management-admin-card-link.component';
import stockManagementComponent from './stock-management.component';
import StockOperationsComponent from './stock-operations/stock-operations.component';
import StockReports from './stock-reports/report-list/stock-reports.component';
import StockSettings from './stock-settings/stock-settings.component';
import StockSources from './stock-sources/stock-sources.component';
import StockUserScopes from './stock-user-role-scopes/stock-user-role-scopes.component';
import TransactionsBincardPrintPreviewModal from './stock-items/add-stock-item/transactions/printout/transactions-print-bincard-preview.modal';
import TransactionsStockcardPrintPreviewModal from './stock-items/add-stock-item/transactions/printout/transactions-print-stockcard-preview.modal';

const moduleName = '@openmrs/esm-stock-management-app';

const options = {
  featureName: 'stock-management',
  moduleName,
};

export const importTranslation = require.context('../translations', false, /.json$/, 'lazy');

export const deleteStockModal = getAsyncLifecycle(() => import('./stock-sources/delete-stock-source.modal'), {
  featureName: 'delete-stock-modal',
  moduleName,
});

export const deleteUserScopeModal = getAsyncLifecycle(
  () => import('./stock-user-role-scopes/delete-stock-user-scope.modal'),
  {
    featureName: 'delete-stock-user-scope-modal',
    moduleName,
  },
);

export const deletePackagingUnitModal = getAsyncLifecycle(
  () => import('./stock-items/add-stock-item/packaging-units/packaging-units-delete.modal'),
  {
    featureName: 'delete-packaging-unit-modal',
    moduleName,
  },
);

export const deletePackagingUnitButton = getSyncLifecycle(deletePackagingUnitModalButtonComponent, {
  featureName: 'delete-packaging-unit-button',
  moduleName,
});

export const expiredStockModal = getAsyncLifecycle(() => import('./stock-home/expired-stock.modal'), {
  featureName: 'expired-stock-modal',
  moduleName,
});

export const importBulkStockItemsModal = getAsyncLifecycle(
  () => import('./stock-items/add-bulk-stock-item/stock-items-bulk-import.modal'),
  {
    featureName: 'import-bulk-stock-items-modal',
    moduleName,
  },
);

export const issuingStockModal = getAsyncLifecycle(() => import('./stock-home/issuing-stock.modal'), {
  featureName: 'issuing-stock-modal',
  moduleName,
});

export const root = getSyncLifecycle(Root, options);

export const receivingStockModal = getAsyncLifecycle(() => import('./stock-home/receiving-stock.modal'), {
  featureName: 'receiving-stock-modal',
  moduleName,
});

export const stockManagementAdminCardLink = getSyncLifecycle(stockManagementAdminCardLinkComponent, options);

export const stockNavMenu = getSyncLifecycle(SideMenu, options);

// t("overview","Overview")
export const stockOverview = getSyncLifecycle(StockHomeLandingPage, options);
export const stockOverviewLink = getSyncLifecycle(
  createDashboardLink({ title: 'Overview', name: 'stock-management' }),
  options,
);

// t("operations","Operations")
export const stockOperations = getSyncLifecycle(StockOperationsComponent, options);
export const stockOperationsLink = getSyncLifecycle(
  createDashboardLink({ title: 'Operations', name: 'operations' }),
  options,
);

// t("items","Items")
export const stockItems = getSyncLifecycle(StockItems, options);

export const stockItemsLink = getSyncLifecycle(createDashboardLink({ title: 'Items', name: 'items' }), options);

// t("useScopes","User role scopes")
export const stockUserScopes = getSyncLifecycle(StockUserScopes, options);
export const stockUserScopesLink = getSyncLifecycle(
  createDashboardLink({ title: 'User role scopes', name: 'user-scopes' }),
  options,
);

// t("sources","Sources")
export const stockSources = getSyncLifecycle(StockSources, options);
export const stockSourcesLink = getSyncLifecycle(createDashboardLink({ title: 'Sources', name: 'sources' }), options);

// t("locations","Locations")
export const stockLocations = getSyncLifecycle(StockLocations, options);
export const stockLocationsLink = getSyncLifecycle(
  createDashboardLink({ title: 'Locations', name: 'locations' }),
  options,
);

// t("reports","Reports")
export const stockReports = getSyncLifecycle(StockReports, options);
export const stockReportsLink = getSyncLifecycle(createDashboardLink({ title: 'Reports', name: 'reports' }), options);

// t("settings","Settings")
export const stockSettings = getSyncLifecycle(StockSettings, options);
export const stockSettingsLink = getSyncLifecycle(
  createDashboardLink({ title: 'Settings', name: 'settings' }),
  options,
);

export const stockManagement = getSyncLifecycle(stockManagementComponent, options);

export const stockOperationsModal = getAsyncLifecycle(
  () => import('./stock-operations/stock-operations-modal/stock-operations.modal'),
  {
    featureName: 'stock-operation-modal',
    moduleName,
  },
);

export const stockManagementAppMenuItem = getSyncLifecycle(appMenu, options);

export const stockOperationFormWorkspace = getAsyncLifecycle(
  () => import('./stock-operations/stock-operations-forms/stock-operation-form.component'),
  options,
);
export const stockItemFormWorkspace = getAsyncLifecycle(
  () => import('./stock-items/add-stock-item/add-stock-item.component'),
  options,
);
export const stockItemRulesFormWorkspace = getAsyncLifecycle(
  () => import('./stock-items/add-stock-item/stock-item-rules/add-stock-rules.component'),
  options,
);

export const stockSourcesFormWorkspace = getAsyncLifecycle(
  () => import('./stock-sources/add-stock-sources/add-stock-sources.workspace'),
  options,
);

export const stockLocationsFormWorkspace = getAsyncLifecycle(
  () => import('./stock-locations/add-locations-form.workspace'),
  options,
);

export const stockReportsFormWorkspace = getAsyncLifecycle(
  () => import('./stock-reports/generate-report/create-stock-report.workspace'),
  options,
);

export const stockUserScopesFormWorkspace = getAsyncLifecycle(
  () => import('./stock-user-role-scopes/add-stock-user-scope/add-stock-user-role-scope.workspace'),
  options,
);

export const transactionBincardPrintPreviewModal = getSyncLifecycle(TransactionsBincardPrintPreviewModal, options);

export const transactionStockcardPrintPreviewModal = getSyncLifecycle(TransactionsStockcardPrintPreviewModal, options);

export function startupApp() {
  defineConfigSchema(moduleName, configSchema);
}
