/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement.dao;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.stockmanagement.EntityUtil;
import org.openmrs.module.stockmanagement.api.dao.StockManagementDao;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.openmrs.module.stockmanagement.api.model.*;

import java.math.BigDecimal;

/**
 * It is an integration test (extends BaseModuleContextSensitiveTest), which verifies DAO methods
 * against the in-memory H2 database. The database is initially loaded with data from
 * standardTestDataset.xml in openmrs-api. All test methods are executed in transactions, which are
 * rolled back by the end of each test method.
 */
public class StockManagementDaoTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	DbSessionFactory sessionFactory;
	
	private StockManagementDao daoInstance;
	
	private static EntityUtil entityUtil;
	
	private StockManagementDao dao() {
		if (daoInstance == null) {
			daoInstance = new StockManagementDao();
			daoInstance.setSessionFactory(sessionFactory);
		}
		return daoInstance;
	}
	
	@Before
	public void setup() throws Exception {
		initializeInMemoryDatabase();
		executeDataSet(EntityUtil.STOCK_OPERATION_TYPE_DATA_SET);
	}
	
	private EntityUtil eu() {
		if (entityUtil == null) {
			Drug drug = Context.getConceptService().getDrug(3);
			Concept concept = drug.getConcept();
			Role role = Context.getUserService().getRole("Provider");
			Patient patient = Context.getPatientService().getPatient(2);
			User user = Context.getUserService().getUser(501);
			Location location = Context.getLocationService().getLocation(1);
			entityUtil = new EntityUtil(drug, user, location, role, concept, patient);
		}
		return entityUtil;
	}
	
	@Test
	public void saveLocationTree_shouldSaveAllProperties() {
		//Given
		LocationTree locationTree = eu().newLocationTree(dao());
		
		//When
		dao().saveLocationTree(locationTree);
		
		//Let's clean up the cache to be sure getLocationTreeByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		LocationTree savedLocationTree = dao().getLocationTreeByUuid(locationTree.getUuid());
		assertThat(savedLocationTree, hasProperty("uuid", is(locationTree.getUuid())));
		assertThat(savedLocationTree, hasProperty("parentLocationId", is(locationTree.getParentLocationId())));
		assertThat(savedLocationTree, hasProperty("childLocationId", is(locationTree.getChildLocationId())));
		assertThat(savedLocationTree, hasProperty("depth", is(locationTree.getDepth())));
	}
	
	@Test
	public void saveStockItemTransaction_shouldSaveAllProperties() {
		//Given
		StockItemTransaction stockItemTransaction = eu().newStockItemTransaction(dao());
		
		//When
		dao().saveStockItemTransaction(stockItemTransaction);
		
		//Let's clean up the cache to be sure getStockItemTransactionByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockItemTransaction savedStockItemTransaction = dao().getStockItemTransactionByUuid(stockItemTransaction.getUuid());
		assertThat(savedStockItemTransaction, hasProperty("uuid", is(stockItemTransaction.getUuid())));
		assertThat(savedStockItemTransaction, hasProperty("creator", is(stockItemTransaction.getCreator())));
		assertThat(savedStockItemTransaction, hasProperty("dateCreated", is(stockItemTransaction.getDateCreated())));
		assertThat(savedStockItemTransaction, hasProperty("party", is(stockItemTransaction.getParty())));
		assertThat(savedStockItemTransaction, hasProperty("patient", is(stockItemTransaction.getPatient())));
		assertThat(savedStockItemTransaction, hasProperty("quantity", is(stockItemTransaction.getQuantity())));
		assertThat(savedStockItemTransaction, hasProperty("stockBatch", is(stockItemTransaction.getStockBatch())));
		assertThat(savedStockItemTransaction, hasProperty("stockItem", is(stockItemTransaction.getStockItem())));
		assertThat(savedStockItemTransaction, hasProperty("stockOperation", is(stockItemTransaction.getStockOperation())));
		assertThat(savedStockItemTransaction,
		    hasProperty("stockItemPackagingUOM", is(stockItemTransaction.getStockItemPackagingUOM())));
	}
	
	@Test
	public void saveStockItemPackagingUOM_shouldSaveAllProperties() {
		//Given
		StockItemPackagingUOM stockItemPackagingUOM = eu().newStockItemPackagingUOM(dao());
		
		//When
		dao().saveStockItemPackagingUOM(stockItemPackagingUOM);
		
		//Let's clean up the cache to be sure getStockItemPackagingUOMByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockItemPackagingUOM savedStockItemPackagingUOM = dao().getStockItemPackagingUOMByUuid(
		    stockItemPackagingUOM.getUuid());
		assertThat(savedStockItemPackagingUOM, hasProperty("uuid", is(stockItemPackagingUOM.getUuid())));
		assertThat(savedStockItemPackagingUOM, hasProperty("creator", is(stockItemPackagingUOM.getCreator())));
		assertThat(savedStockItemPackagingUOM, hasProperty("dateCreated", is(stockItemPackagingUOM.getDateCreated())));
		assertThat(savedStockItemPackagingUOM, hasProperty("changedBy", is(stockItemPackagingUOM.getChangedBy())));
		assertThat(savedStockItemPackagingUOM, hasProperty("dateChanged", is(stockItemPackagingUOM.getDateChanged())));
		assertThat(savedStockItemPackagingUOM, hasProperty("voided", is(stockItemPackagingUOM.getVoided())));
		assertThat(savedStockItemPackagingUOM, hasProperty("dateVoided", is(stockItemPackagingUOM.getDateVoided())));
		assertThat(savedStockItemPackagingUOM, hasProperty("voidedBy", is(stockItemPackagingUOM.getVoidedBy())));
		assertThat(savedStockItemPackagingUOM, hasProperty("voidReason", is(stockItemPackagingUOM.getVoidReason())));
		assertThat(savedStockItemPackagingUOM, hasProperty("factor", is(stockItemPackagingUOM.getFactor())));
		assertThat(savedStockItemPackagingUOM, hasProperty("packagingUom", is(stockItemPackagingUOM.getPackagingUom())));
		assertThat(savedStockItemPackagingUOM, hasProperty("stockItem", is(stockItemPackagingUOM.getStockItem())));
	}
	
	@Test
	public void saveUserRoleScopeOperationType_shouldSaveAllProperties() {
		//Given
		UserRoleScopeOperationType userRoleScopeOperationType = eu().newUserRoleScopeOperationType(dao());
		
		//When
		dao().saveUserRoleScopeOperationType(userRoleScopeOperationType);
		
		//Let's clean up the cache to be sure getUserRoleScopeOperationTypeByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		UserRoleScopeOperationType savedUserRoleScopeOperationType = dao().getUserRoleScopeOperationTypeByUuid(
		    userRoleScopeOperationType.getUuid());
		assertThat(savedUserRoleScopeOperationType, hasProperty("uuid", is(userRoleScopeOperationType.getUuid())));
		assertThat(savedUserRoleScopeOperationType, hasProperty("creator", is(userRoleScopeOperationType.getCreator())));
		assertThat(savedUserRoleScopeOperationType,
		    hasProperty("dateCreated", is(userRoleScopeOperationType.getDateCreated())));
		assertThat(savedUserRoleScopeOperationType, hasProperty("changedBy", is(userRoleScopeOperationType.getChangedBy())));
		assertThat(savedUserRoleScopeOperationType,
		    hasProperty("dateChanged", is(userRoleScopeOperationType.getDateChanged())));
		assertThat(savedUserRoleScopeOperationType, hasProperty("voided", is(userRoleScopeOperationType.getVoided())));
		assertThat(savedUserRoleScopeOperationType,
		    hasProperty("dateVoided", is(userRoleScopeOperationType.getDateVoided())));
		assertThat(savedUserRoleScopeOperationType, hasProperty("voidedBy", is(userRoleScopeOperationType.getVoidedBy())));
		assertThat(savedUserRoleScopeOperationType,
		    hasProperty("voidReason", is(userRoleScopeOperationType.getVoidReason())));
		assertThat(savedUserRoleScopeOperationType,
		    hasProperty("userRoleScope", is(userRoleScopeOperationType.getUserRoleScope())));
		assertThat(savedUserRoleScopeOperationType,
		    hasProperty("stockOperationType", is(userRoleScopeOperationType.getStockOperationType())));
	}
	
	@Test
	public void saveStockOperationType_shouldSaveAllProperties() {
		//Given
		StockOperationType stockOperationType = eu().newStockOperationType(dao());
		
		//When
		dao().saveStockOperationType(stockOperationType);
		
		//Let's clean up the cache to be sure getStockOperationTypeByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockOperationType savedStockOperationType = dao().getStockOperationTypeByUuid(stockOperationType.getUuid());
		assertThat(savedStockOperationType, hasProperty("uuid", is(stockOperationType.getUuid())));
		assertThat(savedStockOperationType, hasProperty("creator", is(stockOperationType.getCreator())));
		assertThat(savedStockOperationType, hasProperty("dateCreated", is(stockOperationType.getDateCreated())));
		assertThat(savedStockOperationType, hasProperty("changedBy", is(stockOperationType.getChangedBy())));
		assertThat(savedStockOperationType, hasProperty("dateChanged", is(stockOperationType.getDateChanged())));
		assertThat(savedStockOperationType, hasProperty("voided", is(stockOperationType.getVoided())));
		assertThat(savedStockOperationType, hasProperty("dateVoided", is(stockOperationType.getDateVoided())));
		assertThat(savedStockOperationType, hasProperty("voidedBy", is(stockOperationType.getVoidedBy())));
		assertThat(savedStockOperationType, hasProperty("voidReason", is(stockOperationType.getVoidReason())));
		assertThat(savedStockOperationType, hasProperty("name", is(stockOperationType.getName())));
		assertThat(savedStockOperationType, hasProperty("description", is(stockOperationType.getDescription())));
		assertThat(savedStockOperationType, hasProperty("operationType", is(stockOperationType.getOperationType())));
		assertThat(savedStockOperationType, hasProperty("hasSource", is(stockOperationType.getHasSource())));
		assertThat(savedStockOperationType, hasProperty("sourceType", is(stockOperationType.getSourceType())));
		assertThat(savedStockOperationType, hasProperty("hasDestination", is(stockOperationType.getHasDestination())));
		assertThat(savedStockOperationType, hasProperty("destinationType", is(stockOperationType.getDestinationType())));
		assertThat(savedStockOperationType,
		    hasProperty("availableWhenReserved", is(stockOperationType.getAvailableWhenReserved())));
	}
	
	@Test
	public void saveStockItem_shouldSaveAllProperties() {
		//Given
		StockItem stockItem = eu().newStockItem(dao(), true);
		
		//When
		dao().saveStockItem(stockItem);
		
		//Let's clean up the cache to be sure getStockItemByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockItem savedStockItem = dao().getStockItemByUuid(stockItem.getUuid());
		assertThat(savedStockItem, hasProperty("uuid", is(stockItem.getUuid())));
		assertThat(savedStockItem, hasProperty("creator", is(stockItem.getCreator())));
		assertThat(savedStockItem, hasProperty("dateCreated", is(stockItem.getDateCreated())));
		assertThat(savedStockItem, hasProperty("changedBy", is(stockItem.getChangedBy())));
		assertThat(savedStockItem, hasProperty("dateChanged", is(stockItem.getDateChanged())));
		assertThat(savedStockItem, hasProperty("voided", is(stockItem.getVoided())));
		assertThat(savedStockItem, hasProperty("dateVoided", is(stockItem.getDateVoided())));
		assertThat(savedStockItem, hasProperty("voidedBy", is(stockItem.getVoidedBy())));
		assertThat(savedStockItem, hasProperty("voidReason", is(stockItem.getVoidReason())));
		assertThat(savedStockItem, hasProperty("concept", is(stockItem.getConcept())));
		assertThat(savedStockItem, hasProperty("drug", is(stockItem.getDrug())));
		assertThat(savedStockItem, hasProperty("hasExpiration", is(stockItem.getHasExpiration())));
		assertThat(savedStockItem, hasProperty("preferredVendor", is(stockItem.getPreferredVendor())));
		assertThat(savedStockItem, hasProperty("purchasePrice", is(stockItem.getPurchasePrice())));
		assertThat(savedStockItem, hasProperty("purchasePriceUoM", is(stockItem.getPurchasePriceUoM())));
		assertThat(savedStockItem, hasProperty("dispensingUnit", is(stockItem.getDispensingUnit())));
		assertThat(savedStockItem, hasProperty("defaultStockOperationsUoM", is(stockItem.getDefaultStockOperationsUoM())));
	}
	
	@Test
	public void saveStockBatch_shouldSaveAllProperties() {
		//Given
		StockBatch stockBatch = eu().newStockBatch(dao());
		
		//When
		dao().saveStockBatch(stockBatch);
		
		//Let's clean up the cache to be sure getStockBatchByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockBatch savedStockBatch = dao().getStockBatchByUuid(stockBatch.getUuid());
		assertThat(savedStockBatch, hasProperty("uuid", is(stockBatch.getUuid())));
		assertThat(savedStockBatch, hasProperty("creator", is(stockBatch.getCreator())));
		assertThat(savedStockBatch, hasProperty("dateCreated", is(stockBatch.getDateCreated())));
		assertThat(savedStockBatch, hasProperty("changedBy", is(stockBatch.getChangedBy())));
		assertThat(savedStockBatch, hasProperty("dateChanged", is(stockBatch.getDateChanged())));
		assertThat(savedStockBatch, hasProperty("voided", is(stockBatch.getVoided())));
		assertThat(savedStockBatch, hasProperty("dateVoided", is(stockBatch.getDateVoided())));
		assertThat(savedStockBatch, hasProperty("voidedBy", is(stockBatch.getVoidedBy())));
		assertThat(savedStockBatch, hasProperty("voidReason", is(stockBatch.getVoidReason())));
		assertThat(savedStockBatch, hasProperty("batchNo", is(stockBatch.getBatchNo())));
		assertThat(savedStockBatch, hasProperty("expiration", is(stockBatch.getExpiration())));
		assertThat(savedStockBatch, hasProperty("stockItem", is(stockBatch.getStockItem())));
	}
	
	@Test
	public void saveUserRoleScope_shouldSaveAllProperties() {
		//Given
		UserRoleScope userRoleScope = eu().newUserRoleScope(dao());
		
		//When
		dao().saveUserRoleScope(userRoleScope);
		
		//Let's clean up the cache to be sure getUserRoleScopeByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		UserRoleScope savedUserRoleScope = dao().getUserRoleScopeByUuid(userRoleScope.getUuid());
		assertThat(savedUserRoleScope, hasProperty("uuid", is(userRoleScope.getUuid())));
		assertThat(savedUserRoleScope, hasProperty("creator", is(userRoleScope.getCreator())));
		assertThat(savedUserRoleScope, hasProperty("dateCreated", is(userRoleScope.getDateCreated())));
		assertThat(savedUserRoleScope, hasProperty("changedBy", is(userRoleScope.getChangedBy())));
		assertThat(savedUserRoleScope, hasProperty("dateChanged", is(userRoleScope.getDateChanged())));
		assertThat(savedUserRoleScope, hasProperty("voided", is(userRoleScope.getVoided())));
		assertThat(savedUserRoleScope, hasProperty("dateVoided", is(userRoleScope.getDateVoided())));
		assertThat(savedUserRoleScope, hasProperty("voidedBy", is(userRoleScope.getVoidedBy())));
		assertThat(savedUserRoleScope, hasProperty("voidReason", is(userRoleScope.getVoidReason())));
		assertThat(savedUserRoleScope, hasProperty("user", is(userRoleScope.getUser())));
		assertThat(savedUserRoleScope, hasProperty("role", is(userRoleScope.getRole())));
		assertThat(savedUserRoleScope, hasProperty("permanent", is(userRoleScope.getPermanent())));
		assertThat(savedUserRoleScope, hasProperty("activeFrom", is(userRoleScope.getActiveFrom())));
		assertThat(savedUserRoleScope, hasProperty("activeTo", is(userRoleScope.getActiveTo())));
		assertThat(savedUserRoleScope, hasProperty("enabled", is(userRoleScope.getEnabled())));
	}
	
	@Test
	public void saveUserRoleScopeLocation_shouldSaveAllProperties() {
		//Given
		UserRoleScopeLocation userRoleScopeLocation = eu().newUserRoleScopeLocation(dao());
		
		//When
		dao().saveUserRoleScopeLocation(userRoleScopeLocation);
		
		//Let's clean up the cache to be sure getUserRoleScopeLocationByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		UserRoleScopeLocation savedUserRoleScopeLocation = dao().getUserRoleScopeLocationByUuid(
		    userRoleScopeLocation.getUuid());
		assertThat(savedUserRoleScopeLocation, hasProperty("uuid", is(userRoleScopeLocation.getUuid())));
		assertThat(savedUserRoleScopeLocation, hasProperty("creator", is(userRoleScopeLocation.getCreator())));
		assertThat(savedUserRoleScopeLocation, hasProperty("dateCreated", is(userRoleScopeLocation.getDateCreated())));
		assertThat(savedUserRoleScopeLocation, hasProperty("changedBy", is(userRoleScopeLocation.getChangedBy())));
		assertThat(savedUserRoleScopeLocation, hasProperty("dateChanged", is(userRoleScopeLocation.getDateChanged())));
		assertThat(savedUserRoleScopeLocation, hasProperty("voided", is(userRoleScopeLocation.getVoided())));
		assertThat(savedUserRoleScopeLocation, hasProperty("dateVoided", is(userRoleScopeLocation.getDateVoided())));
		assertThat(savedUserRoleScopeLocation, hasProperty("voidedBy", is(userRoleScopeLocation.getVoidedBy())));
		assertThat(savedUserRoleScopeLocation, hasProperty("voidReason", is(userRoleScopeLocation.getVoidReason())));
		assertThat(savedUserRoleScopeLocation, hasProperty("userRoleScope", is(userRoleScopeLocation.getUserRoleScope())));
		assertThat(savedUserRoleScopeLocation, hasProperty("location", is(userRoleScopeLocation.getLocation())));
		assertThat(savedUserRoleScopeLocation,
		    hasProperty("enableDescendants", is(userRoleScopeLocation.getEnableDescendants())));
	}
	
	@Test
	public void saveStockRule_shouldSaveAllProperties() {
		//Given
		StockRule stockRule = eu().newStockRule(dao());
		
		//When
		dao().saveStockRule(stockRule);
		
		//Let's clean up the cache to be sure getStockRuleByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockRule savedStockRule = dao().getStockRuleByUuid(stockRule.getUuid());
		assertThat(savedStockRule, hasProperty("uuid", is(stockRule.getUuid())));
		assertThat(savedStockRule, hasProperty("creator", is(stockRule.getCreator())));
		assertThat(savedStockRule, hasProperty("dateCreated", is(stockRule.getDateCreated())));
		assertThat(savedStockRule, hasProperty("changedBy", is(stockRule.getChangedBy())));
		assertThat(savedStockRule, hasProperty("dateChanged", is(stockRule.getDateChanged())));
		assertThat(savedStockRule, hasProperty("voided", is(stockRule.getVoided())));
		assertThat(savedStockRule, hasProperty("dateVoided", is(stockRule.getDateVoided())));
		assertThat(savedStockRule, hasProperty("voidedBy", is(stockRule.getVoidedBy())));
		assertThat(savedStockRule, hasProperty("voidReason", is(stockRule.getVoidReason())));
		assertThat(savedStockRule, hasProperty("stockItem", is(stockRule.getStockItem())));
		assertThat(savedStockRule, hasProperty("name", is(stockRule.getName())));
		assertThat(savedStockRule, hasProperty("description", is(stockRule.getDescription())));
		assertThat(savedStockRule, hasProperty("location", is(stockRule.getLocation())));
		assertThat(savedStockRule, hasProperty("quantity", is(stockRule.getQuantity())));
		assertThat(savedStockRule, hasProperty("stockItemPackagingUOM", is(stockRule.getStockItemPackagingUOM())));
		assertThat(savedStockRule, hasProperty("enabled", is(stockRule.getEnabled())));
		assertThat(savedStockRule, hasProperty("evaluationFrequency", is(stockRule.getEvaluationFrequency())));
		assertThat(savedStockRule, hasProperty("lastEvaluation", is(stockRule.getLastEvaluation())));
		assertThat(savedStockRule, hasProperty("nextEvaluation", is(stockRule.getNextEvaluation())));
		assertThat(savedStockRule, hasProperty("actionFrequency", is(stockRule.getActionFrequency())));
		assertThat(savedStockRule, hasProperty("alertRole", is(stockRule.getAlertRole())));
		assertThat(savedStockRule, hasProperty("mailRole", is(stockRule.getMailRole())));
	}
	
	@Test
	public void saveStockOperationItem_shouldSaveAllProperties() {
		//Given
		StockOperationItem stockOperationItem = eu().newStockOperationItem(dao());
		
		//When
		dao().saveStockOperationItem(stockOperationItem);
		
		//Let's clean up the cache to be sure getStockOperationItemByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockOperationItem savedStockOperationItem = dao().getStockOperationItemByUuid(stockOperationItem.getUuid());
		assertThat(savedStockOperationItem, hasProperty("uuid", is(stockOperationItem.getUuid())));
		assertThat(savedStockOperationItem, hasProperty("creator", is(stockOperationItem.getCreator())));
		assertThat(savedStockOperationItem, hasProperty("dateCreated", is(stockOperationItem.getDateCreated())));
		assertThat(savedStockOperationItem, hasProperty("changedBy", is(stockOperationItem.getChangedBy())));
		assertThat(savedStockOperationItem, hasProperty("dateChanged", is(stockOperationItem.getDateChanged())));
		assertThat(savedStockOperationItem, hasProperty("voided", is(stockOperationItem.getVoided())));
		assertThat(savedStockOperationItem, hasProperty("dateVoided", is(stockOperationItem.getDateVoided())));
		assertThat(savedStockOperationItem, hasProperty("voidedBy", is(stockOperationItem.getVoidedBy())));
		assertThat(savedStockOperationItem, hasProperty("voidReason", is(stockOperationItem.getVoidReason())));
		assertThat(savedStockOperationItem, hasProperty("quantity", is(stockOperationItem.getQuantity())));
		assertThat(savedStockOperationItem, hasProperty("purchasePrice", is(stockOperationItem.getPurchasePrice())));
		assertThat(savedStockOperationItem, hasProperty("stockBatch", is(stockOperationItem.getStockBatch())));
		assertThat(savedStockOperationItem,
		    hasProperty("stockItemPackagingUOM", is(stockOperationItem.getStockItemPackagingUOM())));
		assertThat(savedStockOperationItem, hasProperty("stockItem", is(stockOperationItem.getStockItem())));
		assertThat(savedStockOperationItem, hasProperty("stockOperation", is(stockOperationItem.getStockOperation())));
	}
	
	@Test
	public void saveStockOperation_shouldSaveAllProperties() {
		//Given
		StockOperation stockOperation = eu().newStockOperation(dao());
		
		//When
		dao().saveStockOperation(stockOperation);
		
		//Let's clean up the cache to be sure getStockOperationByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockOperation savedStockOperation = dao().getStockOperationByUuid(stockOperation.getUuid());
		assertThat(savedStockOperation, hasProperty("uuid", is(stockOperation.getUuid())));
		assertThat(savedStockOperation, hasProperty("creator", is(stockOperation.getCreator())));
		assertThat(savedStockOperation, hasProperty("dateCreated", is(stockOperation.getDateCreated())));
		assertThat(savedStockOperation, hasProperty("changedBy", is(stockOperation.getChangedBy())));
		assertThat(savedStockOperation, hasProperty("dateChanged", is(stockOperation.getDateChanged())));
		assertThat(savedStockOperation, hasProperty("voided", is(stockOperation.getVoided())));
		assertThat(savedStockOperation, hasProperty("dateVoided", is(stockOperation.getDateVoided())));
		assertThat(savedStockOperation, hasProperty("voidedBy", is(stockOperation.getVoidedBy())));
		assertThat(savedStockOperation, hasProperty("voidReason", is(stockOperation.getVoidReason())));
		assertThat(savedStockOperation, hasProperty("cancelReason", is(stockOperation.getCancelReason())));
		assertThat(savedStockOperation, hasProperty("cancelledBy", is(stockOperation.getCancelledBy())));
		assertThat(savedStockOperation, hasProperty("cancelledDate", is(stockOperation.getCancelledDate())));
		assertThat(savedStockOperation, hasProperty("completedBy", is(stockOperation.getCompletedBy())));
		assertThat(savedStockOperation, hasProperty("completedDate", is(stockOperation.getCompletedDate())));
		assertThat(savedStockOperation, hasProperty("destination", is(stockOperation.getDestination())));
		assertThat(savedStockOperation, hasProperty("externalReference", is(stockOperation.getExternalReference())));
		assertThat(savedStockOperation, hasProperty("atLocation", is(stockOperation.getAtLocation())));
		assertThat(savedStockOperation, hasProperty("operationDate", is(stockOperation.getOperationDate())));
		assertThat(savedStockOperation, hasProperty("locked", is(stockOperation.getLocked())));
		assertThat(savedStockOperation, hasProperty("operationNumber", is(stockOperation.getOperationNumber())));
		assertThat(savedStockOperation, hasProperty("operationOrder", is(stockOperation.getOperationOrder())));
		assertThat(savedStockOperation, hasProperty("reason", is(stockOperation.getReason())));
		assertThat(savedStockOperation, hasProperty("remarks", is(stockOperation.getRemarks())));
		assertThat(savedStockOperation, hasProperty("source", is(stockOperation.getSource())));
		assertThat(savedStockOperation, hasProperty("status", is(stockOperation.getStatus())));
		assertThat(savedStockOperation, hasProperty("returnReason", is(stockOperation.getReturnReason())));
		assertThat(savedStockOperation, hasProperty("rejectionReason", is(stockOperation.getRejectionReason())));
		assertThat(savedStockOperation, hasProperty("stockOperationType", is(stockOperation.getStockOperationType())));
		assertThat(savedStockOperation, hasProperty("approvalRequired", is(stockOperation.getApprovalRequired())));
		assertThat(savedStockOperation, hasProperty("submittedDate", is(stockOperation.getSubmittedDate())));
		assertThat(savedStockOperation, hasProperty("submittedBy", is(stockOperation.getSubmittedBy())));
		assertThat(savedStockOperation, hasProperty("returnedDate", is(stockOperation.getReturnedDate())));
		assertThat(savedStockOperation, hasProperty("returnedBy", is(stockOperation.getReturnedBy())));
		assertThat(savedStockOperation, hasProperty("rejectedDate", is(stockOperation.getRejectedDate())));
		assertThat(savedStockOperation, hasProperty("rejectedBy", is(stockOperation.getRejectedBy())));
	}
	
	@Test
	public void saveStockOperationTypeLocationScope_shouldSaveAllProperties() {
		//Given
		StockOperationTypeLocationScope stockOperationTypeLocationScope = eu().newStockOperationTypeLocationScope(dao());
		
		//When
		dao().saveStockOperationTypeLocationScope(stockOperationTypeLocationScope);
		
		//Let's clean up the cache to be sure getStockOperationTypeLocationScopeByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockOperationTypeLocationScope savedStockOperationTypeLocationScope = dao()
		        .getStockOperationTypeLocationScopeByUuid(stockOperationTypeLocationScope.getUuid());
		assertThat(savedStockOperationTypeLocationScope, hasProperty("uuid", is(stockOperationTypeLocationScope.getUuid())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("creator", is(stockOperationTypeLocationScope.getCreator())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("dateCreated", is(stockOperationTypeLocationScope.getDateCreated())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("changedBy", is(stockOperationTypeLocationScope.getChangedBy())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("dateChanged", is(stockOperationTypeLocationScope.getDateChanged())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("voided", is(stockOperationTypeLocationScope.getVoided())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("dateVoided", is(stockOperationTypeLocationScope.getDateVoided())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("voidedBy", is(stockOperationTypeLocationScope.getVoidedBy())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("voidReason", is(stockOperationTypeLocationScope.getVoidReason())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("stockOperationType", is(stockOperationTypeLocationScope.getStockOperationType())));
		assertThat(savedStockOperationTypeLocationScope,
		    hasProperty("locationTag", is(stockOperationTypeLocationScope.getLocationTag())));
	}
	
	@Test
	public void saveStockSource_shouldSaveAllProperties() {
		//Given
		StockSource stockSource = eu().newStockSource(dao());
		
		//When
		dao().saveStockSource(stockSource);
		
		//Let's clean up the cache to be sure getStockSourceByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockSource savedStockSource = dao().getStockSourceByUuid(stockSource.getUuid());
		assertThat(savedStockSource, hasProperty("uuid", is(stockSource.getUuid())));
		assertThat(savedStockSource, hasProperty("creator", is(stockSource.getCreator())));
		assertThat(savedStockSource, hasProperty("dateCreated", is(stockSource.getDateCreated())));
		assertThat(savedStockSource, hasProperty("changedBy", is(stockSource.getChangedBy())));
		assertThat(savedStockSource, hasProperty("dateChanged", is(stockSource.getDateChanged())));
		assertThat(savedStockSource, hasProperty("voided", is(stockSource.getVoided())));
		assertThat(savedStockSource, hasProperty("dateVoided", is(stockSource.getDateVoided())));
		assertThat(savedStockSource, hasProperty("voidedBy", is(stockSource.getVoidedBy())));
		assertThat(savedStockSource, hasProperty("voidReason", is(stockSource.getVoidReason())));
		assertThat(savedStockSource, hasProperty("name", is(stockSource.getName())));
		assertThat(savedStockSource, hasProperty("acronym", is(stockSource.getAcronym())));
		assertThat(savedStockSource, hasProperty("sourceType", is(stockSource.getSourceType())));
	}
	
	@Test
	public void saveParty_shouldSaveAllProperties() {
		//Given
		Party party = eu().newParty(dao());
		
		//When
		dao().saveParty(party);
		
		//Let's clean up the cache to be sure getPartyByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		Party savedParty = dao().getPartyByUuid(party.getUuid());
		assertThat(savedParty, hasProperty("uuid", is(party.getUuid())));
		assertThat(savedParty, hasProperty("creator", is(party.getCreator())));
		assertThat(savedParty, hasProperty("dateCreated", is(party.getDateCreated())));
		assertThat(savedParty, hasProperty("changedBy", is(party.getChangedBy())));
		assertThat(savedParty, hasProperty("dateChanged", is(party.getDateChanged())));
		assertThat(savedParty, hasProperty("voided", is(party.getVoided())));
		assertThat(savedParty, hasProperty("dateVoided", is(party.getDateVoided())));
		assertThat(savedParty, hasProperty("voidedBy", is(party.getVoidedBy())));
		assertThat(savedParty, hasProperty("voidReason", is(party.getVoidReason())));
		assertThat(savedParty, hasProperty("location", is(party.getLocation())));
		assertThat(savedParty, hasProperty("stockSource", is(party.getStockSource())));
	}
	
	@Test
	public void saveStockOperationLink_shouldSaveAllProperties() {
		//Given
		StockOperationLink stockOperationLink = eu().newStockOperationLink(dao());
		
		//When
		dao().saveStockOperationLink(stockOperationLink);
		
		//Let's clean up the cache to be sure getStockOperationLinkByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		StockOperationLink savedStockOperationLink = dao().getStockOperationLinkByUuid(stockOperationLink.getUuid());
		assertThat(savedStockOperationLink, hasProperty("uuid", is(stockOperationLink.getUuid())));
		assertThat(savedStockOperationLink, hasProperty("creator", is(stockOperationLink.getCreator())));
		assertThat(savedStockOperationLink, hasProperty("dateCreated", is(stockOperationLink.getDateCreated())));
		assertThat(savedStockOperationLink, hasProperty("changedBy", is(stockOperationLink.getChangedBy())));
		assertThat(savedStockOperationLink, hasProperty("dateChanged", is(stockOperationLink.getDateChanged())));
		assertThat(savedStockOperationLink, hasProperty("voided", is(stockOperationLink.getVoided())));
		assertThat(savedStockOperationLink, hasProperty("dateVoided", is(stockOperationLink.getDateVoided())));
		assertThat(savedStockOperationLink, hasProperty("voidedBy", is(stockOperationLink.getVoidedBy())));
		assertThat(savedStockOperationLink, hasProperty("voidReason", is(stockOperationLink.getVoidReason())));
		assertThat(savedStockOperationLink, hasProperty("parent", is(stockOperationLink.getParent())));
		assertThat(savedStockOperationLink, hasProperty("child", is(stockOperationLink.getChild())));
	}
	
	@Test
	public void saveOrderItem_shouldSaveAllProperties() {
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		//Given
		OrderItem orderItem = eu().newOrderItem(dao(), stockItem, packagingUom);
		
		//When
		dao().saveOrderItem(orderItem);
		
		//Let's clean up the cache to be sure getOrderItemByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		OrderItem savedOrderItem = dao().getOrderItemByUuid(orderItem.getUuid());
		assertThat(savedOrderItem, hasProperty("uuid", is(orderItem.getUuid())));
		assertThat(savedOrderItem, hasProperty("creator", is(orderItem.getCreator())));
		assertThat(savedOrderItem, hasProperty("dateCreated", is(orderItem.getDateCreated())));
		assertThat(savedOrderItem, hasProperty("changedBy", is(orderItem.getChangedBy())));
		assertThat(savedOrderItem, hasProperty("dateChanged", is(orderItem.getDateChanged())));
		assertThat(savedOrderItem, hasProperty("voided", is(orderItem.getVoided())));
		assertThat(savedOrderItem, hasProperty("dateVoided", is(orderItem.getDateVoided())));
		assertThat(savedOrderItem, hasProperty("voidedBy", is(orderItem.getVoidedBy())));
		assertThat(savedOrderItem, hasProperty("voidReason", is(orderItem.getVoidReason())));
		assertThat(savedOrderItem, hasProperty("order", is(orderItem.getOrder())));
		assertThat(savedOrderItem, hasProperty("stockItem", is(orderItem.getStockItem())));
		assertThat(savedOrderItem, hasProperty("stockItemPackagingUOM", is(orderItem.getStockItemPackagingUOM())));
		assertThat(savedOrderItem, hasProperty("createdFrom", is(orderItem.getCreatedFrom())));
		assertThat(savedOrderItem, hasProperty("fulfilmentLocation", is(orderItem.getFulfilmentLocation())));
	}
	
	@Test
	public void saveBatchJobOwner_shouldSaveAllProperties() {
		//Given
		BatchJobOwner batchJobOwner = eu().newBatchJobOwner(dao());
		
		//When
		dao().saveBatchJobOwner(batchJobOwner);
		
		//Let's clean up the cache to be sure getBatchJobOwnerByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		BatchJobOwner savedBatchJobOwner = dao().getBatchJobOwnerByUuid(batchJobOwner.getUuid());
		assertThat(savedBatchJobOwner, hasProperty("uuid", is(batchJobOwner.getUuid())));
		assertThat(savedBatchJobOwner, hasProperty("batchJob", is(batchJobOwner.getBatchJob())));
		assertThat(savedBatchJobOwner, hasProperty("owner", is(batchJobOwner.getOwner())));
		assertThat(savedBatchJobOwner, hasProperty("dateCreated", is(batchJobOwner.getDateCreated())));
	}
	
	@Test
	public void saveBatchJob_shouldSaveAllProperties() {
		//Given
		BatchJob batchJob = eu().newBatchJob(dao());
		
		//When
		dao().saveBatchJob(batchJob);
		
		//Let's clean up the cache to be sure getBatchJobByUuid fetches from DB and not from cache
		Context.flushSession();
		Context.flushSession();
		
		//Then
		BatchJob savedBatchJob = dao().getBatchJobByUuid(batchJob.getUuid());
		assertThat(savedBatchJob, hasProperty("uuid", is(batchJob.getUuid())));
		assertThat(savedBatchJob, hasProperty("creator", is(batchJob.getCreator())));
		assertThat(savedBatchJob, hasProperty("dateCreated", is(batchJob.getDateCreated())));
		assertThat(savedBatchJob, hasProperty("changedBy", is(batchJob.getChangedBy())));
		assertThat(savedBatchJob, hasProperty("dateChanged", is(batchJob.getDateChanged())));
		assertThat(savedBatchJob, hasProperty("voided", is(batchJob.getVoided())));
		assertThat(savedBatchJob, hasProperty("dateVoided", is(batchJob.getDateVoided())));
		assertThat(savedBatchJob, hasProperty("voidedBy", is(batchJob.getVoidedBy())));
		assertThat(savedBatchJob, hasProperty("voidReason", is(batchJob.getVoidReason())));
		assertThat(savedBatchJob, hasProperty("batchJobType", is(batchJob.getBatchJobType())));
		assertThat(savedBatchJob, hasProperty("status", is(batchJob.getStatus())));
		assertThat(savedBatchJob, hasProperty("description", is(batchJob.getDescription())));
		assertThat(savedBatchJob, hasProperty("startTime", is(batchJob.getStartTime())));
		assertThat(savedBatchJob, hasProperty("endTime", is(batchJob.getEndTime())));
		assertThat(savedBatchJob, hasProperty("expiration", is(batchJob.getExpiration())));
		assertThat(savedBatchJob, hasProperty("parameters", is(batchJob.getParameters())));
		assertThat(savedBatchJob, hasProperty("privilegeScope", is(batchJob.getPrivilegeScope())));
		assertThat(savedBatchJob, hasProperty("locationScope", is(batchJob.getLocationScope())));
		assertThat(savedBatchJob, hasProperty("executionState", is(batchJob.getExecutionState())));
		assertThat(savedBatchJob, hasProperty("cancelReason", is(batchJob.getCancelReason())));
		assertThat(savedBatchJob, hasProperty("cancelledBy", is(batchJob.getCancelledBy())));
		assertThat(savedBatchJob, hasProperty("cancelledDate", is(batchJob.getCancelledDate())));
		assertThat(savedBatchJob, hasProperty("exitMessage", is(batchJob.getExitMessage())));
		assertThat(savedBatchJob, hasProperty("completedDate", is(batchJob.getCompletedDate())));
		assertThat(savedBatchJob, hasProperty("outputArtifactSize", is(batchJob.getOutputArtifactSize())));
	}
}
