package org.openmrs.module.stockmanagement;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openmrs.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.dao.StockManagementDao;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;

public class EntityUtil {
	
	public static final String BASE_DATASET_DIR = "org/openmrs/module/stockmanagement/api/";
	
	public static final String STOCK_OPERATION_TYPE_DATA_SET = BASE_DATASET_DIR + "StockOperationType.xml";
	
	public static final String STOCK_ITEMS_IMPORT_CSV = BASE_DATASET_DIR + "StockItemsImport2.csv";
	
	private static Random random = new Random();
	
	private Drug drug;
	
	private User user;
	
	private Location location;
	
	private Role role;
	
	private Concept concept;
	
	private Patient patient;
	
	private Party party;
	
	public EntityUtil(Drug drug, User user, Location location, Role role, Concept concept, Patient patient) {
		this.drug = drug;
		this.user = user;
		this.location = location;
		
		this.role = role;
		this.concept = concept;
		this.patient = patient;
	}
	
	public Order getOrder() {
		int[] orders = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int index = random.nextInt(orders.length - 1);
		return Context.getOrderService().getOrder(orders[index]);
	}
	
	public Encounter getEncounter() {
		int[] encounters = { 3, 4, 5, 6 };
		int index = random.nextInt(encounters.length - 1);
		return Context.getEncounterService().getEncounter(encounters[index]);
	}
	
	public boolean getRandomBool() {
		return random.nextBoolean();
	}
	
	public Date getRandomDate() {
		int day = random.nextInt(365);
		day = getRandomBool() ? day : day * -1;
		Date date = new Date();
		return DateUtils.addDays(date, day);
	}
	
	public Double getRandomDouble() {
		return random.nextDouble();
	}
	
	public float getRandomFloat() {
		return random.nextFloat();
	}
	
	public long getRandomLong() {
		return random.nextLong();
	}
	
	public int getRandomInt() {
		return random.nextInt();
	}
	
	public BigDecimal getRandomBigDecimal() {
		return new BigDecimal(Math.random());
	}
	
	public short getRandomShort() {
		return (short) random.nextInt(Short.MAX_VALUE);
	}
	
	public byte getRandomByte() {
		byte[] bytes = new byte[1];
		random.nextBytes(bytes);
		return bytes[0];
	}
	
	public String getLocationTag() {
		return "Store";
	}
	
	public String getRandomString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}
	
	public Object getRandomEnum(Class classType) {
		Field f = null;
		try {
			f = classType.getDeclaredField("$VALUES");
			
			f.setAccessible(true);
			Object o = f.get(null);
			Object[] list = (Object[]) o;
			return list[random.nextInt(list.length - 1)];
		}
		catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public void setProperty(Object object, String field, Object value) {
		try {
			Field f = object.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(object, value);
		}
		catch (Exception e) {
			throw new RuntimeException();
		}
	}
	
	public Drug getDrug() {
		return drug;
	}
	
	public User getUser() {
		return user;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Party getParty(StockManagementDao dao) {
		Location location = getLocation();
		Party party1 = dao.getPartyByLocation(location);
		if (party1 == null) {
			party1 = new Party();
			party1.setLocation(location);
			party1.setCreator(getUser());
			party1.setDateCreated(getRandomDate());
			dao.saveParty(party1);
		}
		return party1;
	}
	
	public Role getRole() {
		return role;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public LocationTree newLocationTree(StockManagementDao dao) {
		LocationTree locationTree = new LocationTree();
		locationTree.setParentLocationId(1);
		locationTree.setChildLocationId(1);
		locationTree.setDepth(getRandomInt());
		return locationTree;
	}
	
	public StockItemTransaction newStockItemTransaction(StockManagementDao dao) {
		StockItem stockItem = newStockItem(dao, false);
		dao.saveStockItem(stockItem);
		
		StockBatch stockBatch = newStockBatch(dao);
		dao.saveStockBatch(stockBatch);
		
		StockItemPackagingUOM stockItemPackagingUOM = newStockItemPackagingUOM(dao);
		dao.saveStockItemPackagingUOM(stockItemPackagingUOM);
		
		return newStockItemTransaction(dao, stockItem, stockBatch, stockItemPackagingUOM);
	}
	
	public StockItemTransaction newStockItemTransaction(StockManagementDao dao, StockItem stockItem, StockBatch stockBatch,
	        StockItemPackagingUOM stockItemPackagingUOM) {
		StockItemTransaction stockItemTransaction = new StockItemTransaction();
		stockItemTransaction.setCreator(getUser());
		stockItemTransaction.setDateCreated(getRandomDate());
		stockItemTransaction.setParty(getParty(dao));
		stockItemTransaction.setPatient(getPatient());
		stockItemTransaction.setQuantity(getRandomBigDecimal().abs());
		stockItemTransaction.setStockBatch(stockBatch);
		stockItemTransaction.setStockItem(stockItem);
		StockOperation stockOperation = newStockOperation(dao);
		dao.saveStockOperation(stockOperation);
		stockItemTransaction.setStockOperation(stockOperation);
		stockItemTransaction.setStockItemPackagingUOM(stockItemPackagingUOM);
		return stockItemTransaction;
	}
	
	public StockItemPackagingUOM newStockItemPackagingUOM(StockManagementDao dao) {
		return newStockItemPackagingUOM(dao, false);
	}
	
	public StockItemPackagingUOM newStockItemPackagingUOM(StockManagementDao dao, boolean associateStockItemUoms) {
		StockItem stockItem = newStockItem(dao, false);
		dao.saveStockItem(stockItem);
		return newStockItemPackagingUOM(dao, associateStockItemUoms, stockItem);
	}
	
	public StockItemPackagingUOM newStockItemPackagingUOM(StockManagementDao dao, boolean associateStockItemUoms,
	        StockItem stockItem) {
		StockItemPackagingUOM stockItemPackagingUOM = new StockItemPackagingUOM();
		stockItemPackagingUOM.setCreator(getUser());
		stockItemPackagingUOM.setDateCreated(getRandomDate());
		stockItemPackagingUOM.setChangedBy(getUser());
		stockItemPackagingUOM.setDateChanged(getRandomDate());
		stockItemPackagingUOM.setVoided(getRandomBool());
		stockItemPackagingUOM.setDateVoided(getRandomDate());
		stockItemPackagingUOM.setVoidedBy(getUser());
		stockItemPackagingUOM.setVoidReason(getRandomString(255));
		stockItemPackagingUOM.setFactor(BigDecimal.valueOf(2));
		stockItemPackagingUOM.setPackagingUom(getConcept());
		stockItemPackagingUOM.setStockItem(stockItem);
		return stockItemPackagingUOM;
	}
	
	public UserRoleScopeOperationType newUserRoleScopeOperationType(StockManagementDao dao) {
		UserRoleScopeOperationType userRoleScopeOperationType = new UserRoleScopeOperationType();
		userRoleScopeOperationType.setCreator(getUser());
		userRoleScopeOperationType.setDateCreated(getRandomDate());
		userRoleScopeOperationType.setChangedBy(getUser());
		userRoleScopeOperationType.setDateChanged(getRandomDate());
		userRoleScopeOperationType.setVoided(getRandomBool());
		userRoleScopeOperationType.setDateVoided(getRandomDate());
		userRoleScopeOperationType.setVoidedBy(getUser());
		userRoleScopeOperationType.setVoidReason(getRandomString(255));
		UserRoleScope userRoleScope = newUserRoleScope(dao);
		dao.saveUserRoleScope(userRoleScope);
		userRoleScopeOperationType.setUserRoleScope(userRoleScope);
		StockOperationType stockOperationType = newStockOperationType(dao);
		dao.saveStockOperationType(stockOperationType);
		userRoleScopeOperationType.setStockOperationType(stockOperationType);
		return userRoleScopeOperationType;
	}
	
	public StockOperationType newStockOperationType(StockManagementDao dao) {/*
	                                                                         StockOperationType stockOperationType = new StockOperationType();
	                                                                         stockOperationType.setCreator(getUser());
	                                                                         stockOperationType.setDateCreated(getRandomDate());
	                                                                         stockOperationType.setChangedBy(getUser());
	                                                                         stockOperationType.setDateChanged(getRandomDate());
	                                                                         stockOperationType.setVoided(getRandomBool());
	                                                                         stockOperationType.setDateVoided(getRandomDate());
	                                                                         stockOperationType.setVoidedBy(getUser());
	                                                                         stockOperationType.setVoidReason(getRandomString(255));
	                                                                         stockOperationType.setId(1);
	                                                                         stockOperationType.setName(getRandomString(255));
	                                                                         stockOperationType.setDescription(getRandomString(1024));
	                                                                         stockOperationType.setOperationType(getRandomString(255));
	                                                                         stockOperationType.setHasSource(getRandomBool());
	                                                                         stockOperationType.setSourceType((LocationType) getRandomEnum(LocationType.class));
	                                                                         stockOperationType.setHasDestination(getRandomBool());
	                                                                         stockOperationType.setDestinationType((LocationType) getRandomEnum(LocationType.class));
	                                                                         stockOperationType.setAvailableWhenReserved(getRandomBool());
	                                                                         return stockOperationType;*/
		return dao.getAllStockOperationTypes().get(0);
	}
	
	public StockItem newStockItem(StockManagementDao dao, boolean associateUoMs) {
		StockItem stockItem = new StockItem();
		stockItem.setCreator(getUser());
		stockItem.setDateCreated(getRandomDate());
		stockItem.setChangedBy(getUser());
		stockItem.setDateChanged(getRandomDate());
		stockItem.setVoided(getRandomBool());
		stockItem.setDateVoided(getRandomDate());
		stockItem.setVoidedBy(getUser());
		stockItem.setVoidReason(getRandomString(255));
		stockItem.setDrug(getDrug());
		stockItem.setConcept(stockItem.getDrug().getConcept());
		stockItem.setHasExpiration(getRandomBool());
		StockSource preferredVendor = newStockSource(dao);
		dao.saveStockSource(preferredVendor);
		stockItem.setPreferredVendor(preferredVendor);
		stockItem.setPurchasePrice(getRandomBigDecimal());
		if (associateUoMs) {
			StockItemPackagingUOM purchasePriceUoM = newStockItemPackagingUOM(dao, false);
			dao.saveStockItemPackagingUOM(purchasePriceUoM);
			stockItem.setPurchasePriceUoM(purchasePriceUoM);
		}
		stockItem.setDispensingUnit(getConcept());
		if (associateUoMs) {
			StockItemPackagingUOM defaultStockOperationsUoM = newStockItemPackagingUOM(dao, false);
			dao.saveStockItemPackagingUOM(defaultStockOperationsUoM);
			stockItem.setDefaultStockOperationsUoM(defaultStockOperationsUoM);
		}
		return stockItem;
	}
	
	public StockBatch newStockBatch(StockManagementDao dao) {
		StockItem stockItem = newStockItem(dao, false);
		dao.saveStockItem(stockItem);
		return newStockBatch(dao, stockItem);
	}
	
	public StockBatch newStockBatch(StockManagementDao dao, StockItem stockItem) {
		StockBatch stockBatch = new StockBatch();
		stockBatch.setCreator(getUser());
		stockBatch.setDateCreated(getRandomDate());
		stockBatch.setChangedBy(getUser());
		stockBatch.setDateChanged(getRandomDate());
		stockBatch.setVoided(getRandomBool());
		stockBatch.setDateVoided(getRandomDate());
		stockBatch.setVoidedBy(getUser());
		stockBatch.setVoidReason(getRandomString(255));
		stockBatch.setBatchNo(getRandomString(50));
		stockBatch.setExpiration(getRandomDate());
		if (!stockBatch.getExpiration().after(DateUtil.today())) {
			stockBatch.setExpiration(DateUtils.addDays(DateUtil.today(), Math.abs(getRandomInt())));
		}
		stockBatch.setStockItem(stockItem);
		return stockBatch;
	}
	
	public UserRoleScope newUserRoleScope(StockManagementDao dao) {
		UserRoleScope userRoleScope = new UserRoleScope();
		userRoleScope.setCreator(getUser());
		userRoleScope.setDateCreated(getRandomDate());
		userRoleScope.setChangedBy(getUser());
		userRoleScope.setDateChanged(getRandomDate());
		userRoleScope.setVoided(getRandomBool());
		userRoleScope.setDateVoided(getRandomDate());
		userRoleScope.setVoidedBy(getUser());
		userRoleScope.setVoidReason(getRandomString(255));
		userRoleScope.setUser(getUser());
		userRoleScope.setRole(getRole());
		userRoleScope.setPermanent(getRandomBool());
		userRoleScope.setActiveFrom(getRandomDate());
		userRoleScope.setActiveTo(getRandomDate());
		userRoleScope.setEnabled(getRandomBool());
		return userRoleScope;
	}
	
	public UserRoleScopeLocation newUserRoleScopeLocation(StockManagementDao dao) {
		UserRoleScopeLocation userRoleScopeLocation = new UserRoleScopeLocation();
		userRoleScopeLocation.setCreator(getUser());
		userRoleScopeLocation.setDateCreated(getRandomDate());
		userRoleScopeLocation.setChangedBy(getUser());
		userRoleScopeLocation.setDateChanged(getRandomDate());
		userRoleScopeLocation.setVoided(getRandomBool());
		userRoleScopeLocation.setDateVoided(getRandomDate());
		userRoleScopeLocation.setVoidedBy(getUser());
		userRoleScopeLocation.setVoidReason(getRandomString(255));
		UserRoleScope userRoleScope = newUserRoleScope(dao);
		dao.saveUserRoleScope(userRoleScope);
		userRoleScopeLocation.setUserRoleScope(userRoleScope);
		userRoleScopeLocation.setLocation(getLocation());
		userRoleScopeLocation.setEnableDescendants(getRandomBool());
		return userRoleScopeLocation;
	}
	
	public StockRule newStockRule(StockManagementDao dao) {
		StockItem stockItem = newStockItem(dao, false);
		dao.saveStockItem(stockItem);
		StockItemPackagingUOM stockItemPackagingUOM = newStockItemPackagingUOM(dao);
		dao.saveStockItemPackagingUOM(stockItemPackagingUOM);
		return newStockRule(dao, stockItem, stockItemPackagingUOM);
	}
	
	public StockRule newStockRule(StockManagementDao dao, StockItem stockItem, StockItemPackagingUOM stockItemPackagingUOM) {
		StockRule stockRule = new StockRule();
		stockRule.setCreator(getUser());
		stockRule.setDateCreated(getRandomDate());
		stockRule.setChangedBy(getUser());
		stockRule.setDateChanged(getRandomDate());
		stockRule.setVoided(getRandomBool());
		stockRule.setDateVoided(getRandomDate());
		stockRule.setVoidedBy(getUser());
		stockRule.setVoidReason(getRandomString(255));
		stockRule.setStockItem(stockItem);
		stockRule.setName(getRandomString(255));
		stockRule.setDescription(getRandomString(500));
		stockRule.setLocation(getLocation());
		stockRule.setQuantity(getRandomBigDecimal());
		stockRule.setStockItemPackagingUOM(stockItemPackagingUOM);
		stockRule.setEnabled(getRandomBool());
		stockRule.setEvaluationFrequency(getRandomLong());
		stockRule.setLastEvaluation(getRandomDate());
		stockRule.setNextEvaluation(getRandomDate());
		stockRule.setActionFrequency(getRandomLong());
		stockRule.setLastActionDate(getRandomDate());
		stockRule.setAlertRole(getRandomString(255));
		stockRule.setMailRole(getRandomString(255));
		return stockRule;
	}
	
	public StockOperationItem newStockOperationItem(StockManagementDao dao) {
		StockOperationItem stockOperationItem = new StockOperationItem();
		stockOperationItem.setCreator(getUser());
		stockOperationItem.setDateCreated(getRandomDate());
		stockOperationItem.setChangedBy(getUser());
		stockOperationItem.setDateChanged(getRandomDate());
		stockOperationItem.setVoided(getRandomBool());
		stockOperationItem.setDateVoided(getRandomDate());
		stockOperationItem.setVoidedBy(getUser());
		stockOperationItem.setVoidReason(getRandomString(255));
		stockOperationItem.setQuantity(getRandomBigDecimal());
		stockOperationItem.setPurchasePrice(getRandomBigDecimal());
		StockBatch stockBatch = newStockBatch(dao);
		dao.saveStockBatch(stockBatch);
		stockOperationItem.setStockBatch(stockBatch);
		StockItemPackagingUOM stockItemPackagingUOM = newStockItemPackagingUOM(dao);
		dao.saveStockItemPackagingUOM(stockItemPackagingUOM);
		stockOperationItem.setStockItemPackagingUOM(stockItemPackagingUOM);
		StockItem stockItem = newStockItem(dao, false);
		dao.saveStockItem(stockItem);
		stockOperationItem.setStockItem(stockItem);
		StockOperation stockOperation = newStockOperation(dao);
		dao.saveStockOperation(stockOperation);
		stockOperationItem.setStockOperation(stockOperation);
		return stockOperationItem;
	}
	
	public StockOperation newStockOperation(StockManagementDao dao) {
		StockOperation stockOperation = new StockOperation();
		stockOperation.setCreator(getUser());
		stockOperation.setDateCreated(getRandomDate());
		stockOperation.setChangedBy(getUser());
		stockOperation.setDateChanged(getRandomDate());
		stockOperation.setVoided(getRandomBool());
		stockOperation.setDateVoided(getRandomDate());
		stockOperation.setVoidedBy(getUser());
		stockOperation.setVoidReason(getRandomString(255));
		stockOperation.setCancelReason(getRandomString(500));
		stockOperation.setCancelledBy(getUser());
		stockOperation.setCancelledDate(getRandomDate());
		stockOperation.setCompletedBy(getUser());
		stockOperation.setCompletedDate(getRandomDate());
		stockOperation.setDestination(getParty(dao));
		stockOperation.setExternalReference(getRandomString(50));
		stockOperation.setAtLocation(getLocation());
		stockOperation.setOperationDate(getRandomDate());
		stockOperation.setLocked(getRandomBool());
		stockOperation.setOperationNumber(getRandomString(255));
		stockOperation.setOperationOrder(getRandomInt());
		stockOperation.setReason(getConcept());
		stockOperation.setRemarks(getRandomString(255));
		stockOperation.setSource(getParty(dao));
		setProperty(stockOperation, "status", getRandomEnum(StockOperationStatus.class));
		stockOperation.setReturnReason(getRandomString(500));
		stockOperation.setRejectionReason(getRandomString(500));
		StockOperationType stockOperationType = newStockOperationType(dao);
		dao.saveStockOperationType(stockOperationType);
		stockOperation.setStockOperationType(stockOperationType);
		stockOperation.setResponsiblePerson(getUser());
		stockOperation.setResponsiblePersonOther(getRandomString(50));
		stockOperation.setApprovalRequired(getRandomBool());
		stockOperation.setSubmittedBy(getUser());
		stockOperation.setSubmittedDate(getRandomDate());
		stockOperation.setReturnedBy(getUser());
		stockOperation.setReturnedDate(getRandomDate());
		stockOperation.setRejectedBy(getUser());
		stockOperation.setRejectedDate(getRandomDate());
		return stockOperation;
	}
	
	public StockOperationTypeLocationScope newStockOperationTypeLocationScope(StockManagementDao dao) {
		StockOperationTypeLocationScope stockOperationTypeLocationScope = new StockOperationTypeLocationScope();
		stockOperationTypeLocationScope.setCreator(getUser());
		stockOperationTypeLocationScope.setDateCreated(getRandomDate());
		stockOperationTypeLocationScope.setChangedBy(getUser());
		stockOperationTypeLocationScope.setDateChanged(getRandomDate());
		stockOperationTypeLocationScope.setVoided(getRandomBool());
		stockOperationTypeLocationScope.setDateVoided(getRandomDate());
		stockOperationTypeLocationScope.setVoidedBy(getUser());
		stockOperationTypeLocationScope.setVoidReason(getRandomString(255));
		StockOperationType stockOperationType = newStockOperationType(dao);
		dao.saveStockOperationType(stockOperationType);
		stockOperationTypeLocationScope.setStockOperationType(stockOperationType);
		stockOperationTypeLocationScope.setLocationTag(getLocationTag());
		return stockOperationTypeLocationScope;
	}
	
	public StockSource newStockSource(StockManagementDao dao) {
		StockSource stockSource = new StockSource();
		stockSource.setCreator(getUser());
		stockSource.setDateCreated(getRandomDate());
		stockSource.setChangedBy(getUser());
		stockSource.setDateChanged(getRandomDate());
		stockSource.setVoided(getRandomBool());
		stockSource.setDateVoided(getRandomDate());
		stockSource.setVoidedBy(getUser());
		stockSource.setVoidReason(getRandomString(255));
		stockSource.setName(getRandomString(255));
		stockSource.setAcronym(getRandomString(255));
		stockSource.setSourceType(getConcept());
		return stockSource;
	}
	
	public Party newParty(StockManagementDao dao) {
		Party party = new Party();
		party.setCreator(getUser());
		party.setDateCreated(getRandomDate());
		party.setChangedBy(getUser());
		party.setDateChanged(getRandomDate());
		party.setVoided(getRandomBool());
		party.setDateVoided(getRandomDate());
		party.setVoidedBy(getUser());
		party.setVoidReason(getRandomString(255));
		party.setLocation(getLocation());
		StockSource stockSource = newStockSource(dao);
		dao.saveStockSource(stockSource);
		party.setStockSource(stockSource);
		return party;
	}
	
	public StockOperationLink newStockOperationLink(StockManagementDao dao) {
		StockOperationLink stockOperationLink = new StockOperationLink();
		stockOperationLink.setCreator(getUser());
		stockOperationLink.setDateCreated(getRandomDate());
		stockOperationLink.setChangedBy(getUser());
		stockOperationLink.setDateChanged(getRandomDate());
		stockOperationLink.setVoided(getRandomBool());
		stockOperationLink.setDateVoided(getRandomDate());
		stockOperationLink.setVoidedBy(getUser());
		stockOperationLink.setVoidReason(getRandomString(255));
		StockOperation parent = newStockOperation(dao);
		dao.saveStockOperation(parent);
		stockOperationLink.setParent(parent);
		StockOperation child = newStockOperation(dao);
		dao.saveStockOperation(child);
		stockOperationLink.setChild(child);
		return stockOperationLink;
	}
	
	public OrderItem newOrderItem(StockManagementDao dao, StockItem stockItem, StockItemPackagingUOM stockItemPackagingUOM) {
		OrderItem orderItem = new OrderItem();
		orderItem.setCreator(getUser());
		orderItem.setDateCreated(getRandomDate());
		orderItem.setChangedBy(getUser());
		orderItem.setDateChanged(getRandomDate());
		orderItem.setVoided(getRandomBool());
		orderItem.setDateVoided(getRandomDate());
		orderItem.setVoidedBy(getUser());
		orderItem.setVoidReason(getRandomString(255));
		orderItem.setOrder(getOrder());
		orderItem.setStockItem(stockItem);
		orderItem.setStockItemPackagingUOM(stockItemPackagingUOM);
		orderItem.setCreatedFrom(getLocation());
		orderItem.setFulfilmentLocation(getLocation());
		return orderItem;
	}
	
	public BatchJobOwner newBatchJobOwner(StockManagementDao dao) {
		BatchJobOwner batchJobOwner = new BatchJobOwner();
		BatchJob batchJob = newBatchJob(dao);
		dao.saveBatchJob(batchJob);
		batchJobOwner.setBatchJob(batchJob);
		batchJobOwner.setOwner(getUser());
		batchJobOwner.setDateCreated(getRandomDate());
		return batchJobOwner;
	}
	
	public BatchJob newBatchJob(StockManagementDao dao) {
		BatchJob batchJob = new BatchJob();
		batchJob.setCreator(getUser());
		batchJob.setDateCreated(getRandomDate());
		batchJob.setChangedBy(getUser());
		batchJob.setDateChanged(getRandomDate());
		batchJob.setVoided(getRandomBool());
		batchJob.setDateVoided(getRandomDate());
		batchJob.setVoidedBy(getUser());
		batchJob.setVoidReason(getRandomString(255));
		setProperty(batchJob, "batchJobType", getRandomEnum(BatchJobType.class));
		setProperty(batchJob, "status", getRandomEnum(BatchJobStatus.class));
		batchJob.setDescription(getRandomString(255));
		batchJob.setStartTime(getRandomDate());
		batchJob.setEndTime(getRandomDate());
		batchJob.setExpiration(getRandomDate());
		batchJob.setParameters(getRandomString(5000));
		batchJob.setPrivilegeScope(getRandomString(255));
		batchJob.setLocationScope(getLocation());
		batchJob.setExecutionState(getRandomString(5000));
		batchJob.setCancelReason(getRandomString(500));
		batchJob.setCancelledBy(getUser());
		batchJob.setCancelledDate(getRandomDate());
		batchJob.setExitMessage(getRandomString(2500));
		batchJob.setCompletedDate(getRandomDate());
		batchJob.setOutputArtifactSize(getRandomLong());
		return batchJob;
	}
}
