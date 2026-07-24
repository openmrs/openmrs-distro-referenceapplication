<% ui.includeFragment("appui", "standardEmrIncludes")
    ui.includeCss("appui","bootstrap.min.css")
    ui.includeCss("appui","bootstrap.min.js") %>
<% ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("stockmanagement.title") ]) %>
<script>
    window.STOCKMGMT_BASE_URL = '${stockmgmtBaseUrl}/';
    window.STOCKMGMT_RESOURCE_URL = '${stockmgmtResourcesUrl}/';
    window.STOCKMGMT_SPA_PAGE_URL = '${stockmgmtBaseUrl}/stockmanagement/spa.page';
    window.STOCK_SOURCE_TYPE_CODED_CONCEPT_ID = '${stockSourceTypeConceptId}';
    window.STOCK_ADJUSTMENT_REASON_CODED_CONCEPT_ID='${stockAdjustmentReasonConceptId}';
    window.DISPENSING_UNITS_CONCEPT_ID='${dispensingUnitsConceptId}';
    window.PACKAGING_UNITS_CODED_CONCEPT_ID='${packagingUnitsConceptId}';
    window.STOCK_ITEM_CATEGORY_CONCEPT_ID='${stockItemCategoryConceptId}';
    window.STOCK_OPERATION_PRINT_DISABLE_BALANCE_ON_HAND=${stockOperationPrintDisableBalanceOnHand};
    window.STOCK_OPERATION_PRINT_DISABLE_COSTS=${stockOperationPrintDisableCosts};
    window.PRINT_LOGO_TEXT = '${printLogoText}';
    window.PRINT_LOGO = '${printLogo}';
    window.HEALTH_CENTER_NAME='${healthCenterName}';
    window.CLOSE_PRINT_AFTER_PRINT = ${closePrintAfterPrint};
    window.ALLOW_STOCK_ISSUE_WITHOUT_REQUISITION = ${allowStockIssueWithoutRequisition};

</script>
<% if(isDevEnv){ %>
<style type="text/css">
    body{
        width: 90%;
        max-width: 100%;
    }
</style>
<% } %>
${ spaHead }
${ spaBody }