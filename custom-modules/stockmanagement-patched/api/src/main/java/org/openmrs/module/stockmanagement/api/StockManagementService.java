/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement.api;

import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.dto.reporting.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.api.reporting.Report;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * The main service of this module, which is exposed for other modules. See
 * moduleApplicationContext.xml on how it is wired up.
 */
public interface StockManagementService extends OpenmrsService {
	
	@Transactional(readOnly = true)
	List<LocationTree> getCompleteLocationTree();
	
	@Transactional
	void deleteLocationTreeNodes(List<LocationTree> nodes);
	
	@Transactional
	void saveLocationTreeNodes(List<LocationTree> nodes);
	
	@Transactional(readOnly = true)
	List<LocationTree> getCompleteLocationTree(Integer atLocationId);
	
	@Transactional(readOnly = true)
	List<PartyDTO> getCompletePartyList(Integer atLocationId);
	
	@Transactional(readOnly = true)
	List<PartyDTO> getCompleteStockDispensingLocationPartyList(Integer atLocationId);
	
	@Transactional(readOnly = true)
	List<Location> getMainPharmacyLocations();
	
	@Transactional(readOnly = true)
	List<PartyDTO> getMainPharmacyPartyList();
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_USERROLESCOPES)
	Result<UserRoleScopeDTO> findUserRoleScopes(UserRoleScopeSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	StockOperation getStockOperationByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	List<StockOperationItem> getStockOperationItemsByStockOperation(Integer stockOperationId);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	Result<StockOperationItemDTO> findStockOperationItems(StockOperationItemSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	Result<StockOperationItemCost> getStockOperationItemCosts(StockOperationItemSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	Result<StockOperationLinkDTO> findStockOperationLinks(String stockOperationUuid);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
	StockOperation saveStockOperation(StockOperationDTO stockOperationDTO);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE)
	StockOperationType getStockOperationTypeByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE)
	StockOperationType getStockOperationTypeByType(String type);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE)
	List<StockOperationType> getAllStockOperationTypes();
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE)
	StockOperationTypeLocationScope getStockOperationTypeLocationScopeByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE)
	List<StockOperationTypeLocationScope> getAllStockOperationTypeLocationScopes();
	
	@Transactional(readOnly = true)
	UserRoleScopeOperationType getUserRoleScopeOperationTypeByUuid(String uuid);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	void voidUserRoleScopes(List<String> userRoleScopeIds, String reason);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_USERROLESCOPES)
	UserRoleScope getUserRoleScopeByUuid(String uuid);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	UserRoleScope saveUserRoleScope(UserRoleScope userRoleScope);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	UserRoleScope saveUserRoleScope(UserRoleScopeDTO userRoleScopeDTO);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	UserRoleScopeLocation saveUserRoleScopeLocation(UserRoleScopeLocation userRoleScopeLocation);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	UserRoleScopeOperationType saveUserRoleScopeOperationType(UserRoleScopeOperationType userRoleScopeOperationType);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	void voidUserRoleScopeLocations(List<String> userRoleScopeLocationIds, String reason, int voidedBy);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE)
	void voidUserRoleScopeOperationTypes(List<String> userRoleScopeOperationTypeIds, String reason, int voidedBy);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	Result<StockItem> findStockItemEntities(StockItemSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	List<Integer> searchStockItemCommonName(String text, Boolean isDrugSearch, boolean includeAll, int maxItems);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	Result<StockItemDTO> findStockItems(StockItemSearchFilter filter);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	StockItem saveStockItem(StockItemDTO stockItemDTO);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	StockItem saveStockItem(StockItem stockItem);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	StockRuleDTO saveStockRule(StockRuleDTO stockRuleDTO);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	Result<StockRuleDTO> findStockRules(StockRuleSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	Result<StockRuleDTO> findStockRules(StockRuleSearchFilter filter, HashSet<RecordPrivilegeFilter> recordPrivilegeFilters);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	StockRule getStockRuleByUuid(String uuid);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	void voidStockRules(List<String> stockRuleUuids, String reason, int voidedBy);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	StockItem saveStockItem(StockItem stockItem, StockItemPackagingUOM stockItemPackagingUOM);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	StockItem getStockItemByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	Result<StockOperationDTO> findStockOperations(StockOperationSearchFilter filter);
	
	@Transactional(readOnly = true)
	Result<StockOperationDTO> findStockOperations(StockOperationSearchFilter filter,
	        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS)
	Result<StockOperationLinkDTO> getParentStockOperationLinks(String stockOperationUuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKSOURCES)
	StockSource getStockSourceByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKSOURCES)
	Result<StockSource> findStockSources(StockSourceSearchFilter filter);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKSOURCES_MUTATE)
	StockSource saveStockSource(StockSource stockSource);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKSOURCES_MUTATE)
	void voidStockSources(List<String> stockSourceIds, String reason, int voidedBy);
	
	@Transactional(readOnly = true)
	Party getPartyByStockSource(StockSource stockSource);
	
	@Transactional(readOnly = true)
	Party getPartyByLocation(Location location);
	
	@Transactional
	Party saveParty(Party party);
	
	@Transactional(readOnly = true)
	List<Party> findParty(Boolean hasLocation, Boolean hasStockSource);
	
	@Transactional(readOnly = true)
	Result<PartyDTO> findParty(PartySearchFilter filter);
	
	@Transactional(readOnly = true)
	Party getPartyByUuid(String uuid);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_PARTY_READ)
	List<PartyDTO> getAllParties();
	
	@Transactional(readOnly = true)
	SessionInfo getCurrentUserSessionInfo();
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	Result<StockItemPackagingUOMDTO> findStockItemPackagingUOMs(StockItemPackagingUOMSearchFilter filter);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	StockItemPackagingUOM saveStockItemPackagingUOM(StockItemPackagingUOMDTO stockItemPackagingUOM);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	StockItemPackagingUOM saveStockItemPackagingUOM(StockItemPackagingUOM stockItemPackagingUOM);
	
	@Transactional(readOnly = true)
	boolean userHasStockManagementPrivilege(User user, Location location, StockOperationType stockOperationType,
	        String stockManagementPrivilege);
	
	@Transactional(readOnly = true)
	HashSet<PrivilegeScope> getPrivilegeScopes(User user, Location location, StockOperationType stockOperationType,
	        String stockManagementPrivilege);
	
	@Transactional(readOnly = true)
	HashSet<PrivilegeScope> getPrivilegeScopes(User user, Location location, StockOperationType stockOperationType,
	        List<String> stockManagementPrivileges);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS, Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	Result<StockBatchDTO> findStockBatches(StockBatchSearchFilter filter);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
	void voidStockOperationItem(String stockOperationItemUuid, String reason, int voidedBy);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
	void submitStockOperation(StockOperationDTO stockOperationDTO);
	
	@Transactional
	@Authorized({ Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE,
	        Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS })
	void completeStockOperation(StockOperationDTO stockOperationDTO);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE)
	void approveStockOperation(StockOperationDTO stockOperationDTO);
	
	@Transactional
	@Authorized({ Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE,
	        Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE })
	void dispatchStockOperation(StockOperationDTO stockOperationDTO);
	
	@Transactional
	@Authorized({ Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS })
	void stockOperationItemsReceived(StockOperationDTO stockOperationDTO, List<StockOperationActionLineItem> lineItems);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE)
	void rejectStockOperation(StockOperationDTO stockOperationDTO, String reason);
	
	@Transactional
	@Authorized({ Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE,
	        Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS })
	void returnStockOperation(StockOperationDTO stockOperationDTO, String reason);
	
	@Transactional
	@Authorized({ Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE,
	        Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE })
	void cancelStockOperation(StockOperationDTO stockOperationDTO, String reason);
	
	@Transactional
	@Authorized({ Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE })
	void dispenseStockItems(List<DispenseRequest> dispenseRequests);
	
	@Transactional
	void deleteReservedTransations(Integer stockOperationId);
	
	List<StockItemInventory> getStockBatchLocationInventory(List<Integer> stockBatchIds);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockInventoryResult getStockInventory(StockItemInventorySearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockInventoryResult getStockInventory(StockItemInventorySearchFilter filter,
	        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	Result<StockItemTransactionDTO> findStockItemTransactions(StockItemTransactionSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockItemPackagingUOM getStockItemPackagingUOMByUuid(String uuid);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	void voidStockItemPackagingUOM(String uuid, String reason, int voidedBy);
	
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)
	ImportResult importStockItems(Path file, boolean hasHeader);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	List<StockItemDTO> getExistingStockItemIds(Collection<StockItemSearchFilter.ItemGroupFilter> stockItemFilters);
	
	@Transactional(readOnly = true)
	List<Drug> getDrugs(Collection<Integer> drugIds);
	
	@Transactional(readOnly = true)
	List<Concept> getConcepts(Collection<Integer> conceptIds);
	
	@Transactional(readOnly = true)
	List<StockItem> getStockItems(Collection<Integer> stockItemIds);
	
	@Transactional(readOnly = true)
	List<StockItemPackagingUOM> getStockItemPackagingUOMs(List<StockItemPackagingUOMSearchFilter.ItemGroupFilter> filters);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	List<StockItem> getStockItemByDrug(Integer drugId);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	List<StockItem> getStockItemByConcept(Integer conceptId);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockItemPackagingUOM getStockItemPackagingUOMByConcept(Integer stockItemId, Integer conceptId);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockItemPackagingUOM getStockItemPackagingUOMByConcept(Integer stockItemId, String conceptUuid);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockItemPackagingUOM getStockItemPackagingUOMByConcept(String stockItemUuid, Integer conceptId);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.APP_STOCKMANAGEMENT_STOCKITEMS }, requireAll = false)
	StockItemPackagingUOM getStockItemPackagingUOMByConcept(String stockItemUuid, String conceptUuid);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY }, requireAll = false)
	List<OrderItem> getOrderItemsByOrder(Integer... orderIds);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY }, requireAll = false)
	List<OrderItem> getOrderItemsByEncounter(Integer... encounterIds);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY }, requireAll = false)
	Result<OrderItemDTO> findOrderItems(OrderItemSearchFilter filter);
	
	@Transactional(readOnly = true)
	@Authorized(value = { Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY }, requireAll = false)
	Result<OrderItemDTO> findOrderItems(OrderItemSearchFilter filter, HashSet<RecordPrivilegeFilter> recordPrivilegeFilters);
	
	@Transactional
	@Authorized(value = { Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY }, requireAll = false)
	OrderItem saveOrderItem(OrderItem orderItem);
	
	@Transactional(readOnly = true)
	List<StockRuleNotificationUser> getDueStockRules(Integer lastStockRuleId, int limit);
	
	@Transactional(readOnly = true)
	List<Integer> getActiveUsersAssignedForScope(Integer locationId, List<String> roles);
	
	@Transactional
	void updateStockBatchExpiryNotificationDate(Collection<Integer> stockBatchIds, Date notificationDate);
	
	@Transactional
	void updateStockRuleJobNextEvaluationDate(List<Integer> stockRuleIds, Date nextEvaluationDate);
	
	@Transactional
	void updateStockRuleJobNextActionDate(List<Integer> stockRuleIds, Date nextEvaluationDate);
	
	@Transactional(readOnly = true)
	void setStockItemCurrentBalanceWithDescendants(List<StockRuleCurrentQuantity> stockRuleCurrentQuantities);
	
	@Transactional(readOnly = true)
	void setStockItemCurrentBalanceWithoutDescendants(List<StockRuleCurrentQuantity> stockRuleCurrentQuantities);
	
	@Transactional(readOnly = true)
	Map<Integer, String> getStockItemNames(List<Integer> stockItemIds);
	
	@Transactional(readOnly = true)
	Map<Integer, String> getConceptNames(List<Integer> conceptIds);
	
	@Transactional(readOnly = true)
	Map<Integer, String> getLocationNames(List<Integer> locationIds);
	
	@Transactional(readOnly = true)
	String getHealthCenterName();
	
	void runStockRuleEvaluationJob();
	
	void runStockBatchExpiryNoticationJob();
	
	List<PartyDTO> getAllStockHoldingPartyList();
	
	List<StockBatchDTO> getExpiringStockBatchesDueForNotification(Integer defaultExpiryNotificationNoticePeriod);
	
	@Transactional
	@Authorized(value = PrivilegeConstants.MANAGE_LOCATIONS, requireAll = false)
	void deleteLocation(String uuid);
	
	void sendStockOperationNotification(String stockOperationUuid, StockOperationAction.Action action, String actionReason,
	        Integer actionByUserId);
	
	@Transactional
	@Authorized(value = Privileges.TASK_STOCKMANAGEMENT_REPORTS_MUTATE, requireAll = false)
	BatchJobDTO saveBatchJob(BatchJobDTO batchJobDTO);
	
	@Transactional(readOnly = true)
	@Authorized(value = Privileges.APP_STOCKMANAGEMENT_REPORTS)
	Result<BatchJobDTO> findBatchJobs(BatchJobSearchFilter batchJobSearchFilter);
	
	@Transactional(readOnly = true)
	@Authorized(value = Privileges.APP_STOCKMANAGEMENT_REPORTS)
	Result<BatchJobDTO> findBatchJobs(BatchJobSearchFilter batchJobSearchFilter,
	        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters);
	
	@Transactional
	@Authorized(value = Privileges.TASK_STOCKMANAGEMENT_REPORTS_MUTATE, requireAll = false)
	void cancelBatchJob(String batchJobUuid, String reason);
	
	@Transactional
	@Authorized(value = Privileges.TASK_STOCKMANAGEMENT_REPORTS_MUTATE, requireAll = false)
	void failBatchJob(String batchJobUuid, String reason);
	
	@Transactional
	@Authorized(value = Privileges.TASK_STOCKMANAGEMENT_REPORTS_MUTATE, requireAll = false)
	void expireBatchJob(String batchJobUuid, String reason);
	
	@Transactional(readOnly = true)
	@Authorized(value = Privileges.APP_STOCKMANAGEMENT_REPORTS)
	List<Report> getReports();
	
	@Transactional(readOnly = true)
	BatchJob getNextActiveBatchJob();
	
	@Transactional(readOnly = true)
	BatchJob getBatchJobByUuid(String batchJobUuid);
	
	@Transactional
	void saveBatchJob(BatchJob batchJob);
	
	Result<StockOperationLineItem> findStockOperationLineItems(StockOperationLineItemFilter filter);
	
	@Transactional
	void updateBatchJobRunning(String batchJobUuid);
	
	@Transactional
	void updateBatchJobExecutionState(String batchJobUuid, String executionState);
	
	String getUserEmailAddress(User user);
	
	Result<StockBatchLineItem> getExpiringStockBatchList(StockExpiryFilter filter);
	
	<T extends StockItemInventory> void getStockInventory(StockItemInventorySearchFilter filter,
	        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters, Function<T, Boolean> consumer, Class<T> resultClass);
	
	StockInventoryResult postProcessInventoryResult(StockItemInventorySearchFilter filter, StockInventoryResult result);
	
	void setStockItemInformation(List<StockItemInventory> reportStockItemInventories);
	
	Result<DispensingLineItem> findDispensingLineItems(DispensingLineFilter filter);
	
	Result<PrescriptionLineItem> findPrescriptionLineItems(PrescriptionLineFilter filter);
	
	Result<StockItemInventory> getLeastMovingStockInventory(StockItemInventorySearchFilter filter);
	
	Result<StockItemInventory> getMostMovingStockInventory(StockItemInventorySearchFilter filter);
	
	void getStockInventoryForecastData(StockItemInventorySearchFilter filter, Function<Object[], Boolean> consumer);
	
	void getStockInventoryExpiryForecastData(StockItemInventorySearchFilter filter, Function<Object[], Boolean> consumer);
	
	List<BatchJob> getExpiredBatchJobs();
	
	@Transactional
	void deleteBatchJob(BatchJob batchJob);
	
	@Transactional(readOnly = true)
	Map<Integer, Boolean> checkStockBatchHasTransactionsAfterOperation(Integer stockOperationId, List<Integer> stockBatchIds);
	
	@Transactional
	@Authorized(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
	StockOperationBatchNumbersDTO saveStockOperationBatchNumbers(StockOperationBatchNumbersDTO stockOperationBatchNumbers);
	
	@Transactional(readOnly = true)
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	StockItem getStockItemByReference(StockSource stockSource, String code);
	
	@Transactional
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	StockItemReference saveStockItemReference(StockItemReference stockItemReference);
	
	@Transactional
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	void voidStockItemReference(String uuid, String reason, Integer userId);
	
	@Transactional
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	StockItemReference getStockItemReferenceByUuid(String uuid);
	
	@Transactional
	@Authorized(Privileges.APP_STOCKMANAGEMENT_STOCKITEMS)
	List<StockItemReference> getStockItemReferenceByStockItem(String uuid);
}
