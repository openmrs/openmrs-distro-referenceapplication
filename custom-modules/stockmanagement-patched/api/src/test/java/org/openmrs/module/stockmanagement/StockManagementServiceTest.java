/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.*;
import org.openmrs.api.LocationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.dao.StockManagementDao;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockOperationLineItem;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockOperationLineItemFilter;
import org.openmrs.module.stockmanagement.api.impl.StockManagementServiceImpl;
import org.openmrs.module.stockmanagement.api.jobs.StockItemImportJob;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;
import org.openmrs.module.stockmanagement.tasks.LocationTagsSynchronize;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * This is a unit test, which verifies logic in StockManagementService. It doesn't extend
 * BaseModuleContextSensitiveTest, thus it is run without the in-memory DB and Spring context.
 */
public class StockManagementServiceTest extends BaseModuleContextSensitiveTest {
	
	@InjectMocks
	StockManagementServiceImpl stockManagementService;
	
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
	
	//	@Mock
	//	StockManagementDao dao;
	//
	//	@Mock
	//	UserService userService;
	
	@Before
	public void setupMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
    public void findUserRoleScopes_shouldFilterOnAllCriteria() {
        stockManagementService.setDao(dao());
        UserRoleScope userRoleScope = eu().newUserRoleScope(dao());
        userRoleScope.setVoided(false);
        dao().saveUserRoleScope(userRoleScope);

        UserRoleScopeLocation userRoleScopeLocation = eu().newUserRoleScopeLocation(dao());
        UserRoleScope userRoleScope2 = userRoleScopeLocation.getUserRoleScope();
        userRoleScope2.setVoided(false);
        dao().saveUserRoleScope(userRoleScope2);
        userRoleScopeLocation.setVoided(false);
        dao().saveUserRoleScopeLocation(userRoleScopeLocation);

        UserRoleScopeOperationType userRoleScopeOperationType = eu().newUserRoleScopeOperationType(dao());
        UserRoleScope userRoleScope3 = userRoleScopeOperationType.getUserRoleScope();
        userRoleScope3.setVoided(false);
        dao().saveUserRoleScope(userRoleScope3);
        userRoleScopeOperationType.setVoided(false);
        dao().saveUserRoleScopeOperationType(userRoleScopeOperationType);
        userRoleScopeOperationType = dao().getUserRoleScopeOperationTypeByUuid(userRoleScopeOperationType.getUuid());

        //Let's clean up the cache to be sure getUserRoleScopeByUuid fetches from DB and not from cache
        Context.flushSession();
        Context.flushSession();

        UserRoleScopeSearchFilter filter = new UserRoleScopeSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<UserRoleScopeDTO> result = stockManagementService.findUserRoleScopes(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(3));
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScope.getUuid())).findFirst().isPresent());
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScopeLocation.getUserRoleScope().getUuid())).findFirst().isPresent());
        final UserRoleScopeOperationType finalUserRoleScopeOperationType = userRoleScopeOperationType;
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(finalUserRoleScopeOperationType.getUserRoleScope().getUuid())).findFirst().isPresent());

        assertEquals(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScope.getUuid())).findFirst().get().getUserFamilyName(), userRoleScope.getUser().getFamilyName());
        assertEquals(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScopeLocation.getUserRoleScope().getUuid())).findFirst().get().getLocations().get(0).getLocationName(),
                userRoleScopeLocation.getLocation().getName()
        );
        assertEquals(result.getData().stream().filter(p -> p.getUuid() .equals(finalUserRoleScopeOperationType.getUserRoleScope().getUuid())).findFirst().get().getOperationTypes().get(0).getOperationTypeName(),
                userRoleScopeOperationType.getStockOperationType().getName());


        filter = new UserRoleScopeSearchFilter();
        filter.setLocation(userRoleScopeLocation.getLocation());
        result = stockManagementService.findUserRoleScopes(filter);
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScope.getUuid())).findFirst().isPresent());
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScopeLocation.getUserRoleScope().getUuid())).findFirst().isPresent());
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(finalUserRoleScopeOperationType.getUserRoleScope().getUuid())).findFirst().isPresent());

        filter = new UserRoleScopeSearchFilter();
        filter.setOperationType(userRoleScopeOperationType.getStockOperationType());
        result = stockManagementService.findUserRoleScopes(filter);
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScope.getUuid())).findFirst().isPresent());
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScopeLocation.getUserRoleScope().getUuid())).findFirst().isPresent());
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(finalUserRoleScopeOperationType.getUserRoleScope().getUuid())).findFirst().isPresent());

        filter = new UserRoleScopeSearchFilter();
        filter.setName(userRoleScope.getUser().getFamilyName());
        result = stockManagementService.findUserRoleScopes(filter);
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScope.getUuid())).findFirst().isPresent());
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScopeLocation.getUserRoleScope().getUuid())).findFirst().isPresent());
        assertTrue(result.getData().stream().filter(p -> p.getUuid() .equals(finalUserRoleScopeOperationType.getUserRoleScope().getUuid())).findFirst().isPresent());

        filter = new UserRoleScopeSearchFilter();
        filter.setName(UUID.randomUUID().toString());
        result = stockManagementService.findUserRoleScopes(filter);
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScope.getUuid())).findFirst().isPresent());
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(userRoleScopeLocation.getUserRoleScope().getUuid())).findFirst().isPresent());
        assertFalse(result.getData().stream().filter(p -> p.getUuid() .equals(finalUserRoleScopeOperationType.getUserRoleScope().getUuid())).findFirst().isPresent());
    }
	
	@Test
	public void findStockOperations_shouldFilterOnAllCriteria() {
		stockManagementService.setDao(dao());
		StockOperation so = eu().newStockOperation(dao());
        so.setVoided(false);
        so.setOperationNumber(so.getOperationNumber().toUpperCase());
		daoInstance.saveStockOperation(so);
		StockOperationSearchFilter filter = new StockOperationSearchFilter();
		filter.setLimit(10);
		filter.setStartIndex(0);
		Result<StockOperationDTO> result = dao().findStockOperations(filter, null);
		assertThat(result.getData().size(), greaterThanOrEqualTo(1));


        filter = new StockOperationSearchFilter();
        filter.setLocationId(so.getSource().getLocation().getLocationId());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setLocationId(so.getDestination().getLocation().getLocationId());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setLocationId(so.getAtLocation().getLocationId());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationDateMax(so.getOperationDate());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationDateMax(DateUtils.addDays(so.getOperationDate(), 1));
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationDateMax(DateUtils.addDays(so.getOperationDate(), -1));
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationDateMin(so.getOperationDate());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationDateMin(DateUtils.addDays(so.getOperationDate(), 1));
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationDateMin(DateUtils.addDays(so.getOperationDate(), -1));
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationNumber(so.getOperationNumber());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationTypeId(Arrays.asList(so.getStockOperationType().getId()));
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setOperationTypeId(Arrays.asList(so.getStockOperationType().getId() + 3));
        result = stockManagementService.findStockOperations(filter);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setStatus(Arrays.asList(so.getStatus()));
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setStatus(Arrays.asList(Arrays.asList(StockOperationStatus.values()).stream().filter(p -> p != so.getStatus()).findFirst().get()));
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setStockItemId(Integer.MAX_VALUE - 10);
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setStockOperationUuid(so.getUuid());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setStockOperationUuid(so.getUuid()+"00");
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setPartyId(so.getSource().getId());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setPartyId(so.getDestination().getId());
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        filter.setSourceTypeIds(Arrays.asList(Integer.MAX_VALUE));
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        String searchText = null;
        if(so.getDestination().getLocation() != null){
            searchText = so.getDestination().getLocation().getName().substring(0,Math.min(3, so.getDestination().getLocation().getName().length()));
        }
        else if(so.getDestination().getStockSource() != null){
            searchText = so.getDestination().getStockSource().getName().substring(0,Math.min(3, so.getDestination().getStockSource().getName().length()));
        }
        filter.setSearchText(searchText);
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        searchText = null;
        if(so.getSource().getLocation() != null){
            searchText = so.getDestination().getLocation().getName().substring(0,Math.min(3, so.getSource().getLocation().getName().length()));
        }
        else if(so.getSource().getStockSource() != null){
            searchText = so.getDestination().getStockSource().getName().substring(0,Math.min(3, so.getSource().getStockSource().getName().length()));
        }
        filter.setSearchText(searchText);
        result = dao().findStockOperations(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationSearchFilter();
        searchText = null;
        if(so.getSource().getLocation() != null){
            searchText = so.getDestination().getLocation().getName().substring(0,Math.min(3, so.getSource().getLocation().getName().length()));
        }
        else if(so.getSource().getStockSource() != null){
            searchText = so.getDestination().getStockSource().getName().substring(0,Math.min(3, so.getSource().getStockSource().getName().length()));
        }
        filter.setSearchText(searchText+"b_540");
        result = dao().findStockOperations(filter, null);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));


        filter = new StockOperationSearchFilter();
        filter.setLocationId(so.getSource().getLocation().getLocationId());
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters =new HashSet<>();
        RecordPrivilegeFilter recordPrivilegeFilter=new RecordPrivilegeFilter();
        recordPrivilegeFilter.setOperationTypeId(Integer.MAX_VALUE);
        recordPrivilegeFilter.setLocationId(so.getDestination().getLocation().getId());
        recordPrivilegeFilters.add(recordPrivilegeFilter);
        result = dao().findStockOperations(filter, recordPrivilegeFilters);
        assertFalse(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
    public void findStockSources_shouldFilterOnAllCriteria() {
        stockManagementService.setDao(dao());
        StockSource so = eu().newStockSource(dao());
        so.setVoided(false);
        daoInstance.saveStockSource(so);
        StockSourceSearchFilter filter = new StockSourceSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockSource> result = stockManagementService.findStockSources(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));

        filter = new StockSourceSearchFilter();
        filter.setTextSearch(so.getName().substring(0,Math.min(3,so.getName().length())));
        result = stockManagementService.findStockSources(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockSourceSearchFilter();
        filter.setTextSearch(so.getName());
        result = stockManagementService.findStockSources(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockSourceSearchFilter();
        filter.setTextSearch(so.getAcronym().substring(0,Math.min(3,so.getAcronym().length())));
        result = stockManagementService.findStockSources(filter);
        assertTrue(result.getData().stream().anyMatch(p->p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockSourceSearchFilter();
        filter.setTextSearch(so.getAcronym());
        result = stockManagementService.findStockSources(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockSourceSearchFilter();
        filter.setSourceType(so.getSourceType());
        result = stockManagementService.findStockSources(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockSourceSearchFilter();
        filter.setIncludeVoided(so.getVoided());
        result = stockManagementService.findStockSources(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
	public void getAllDTOS_shouldHaveAllLocations() {
		stockManagementService.setDao(dao());
		List<PartyDTO> partyDTOs = stockManagementService.getAllParties();
	}
	
	@Test
	public void getFlattenedUserRoleScopesByUser_shouldHaveAllLocations() {
		stockManagementService.setDao(dao());
		dao().getFlattenedUserRoleScopesByUser(eu().getUser(), new HashSet<Role>(Arrays.asList(eu().getRole())), null, null);
	}
	
	@Test
    public void findStockItemPackagingUOMs_shouldFilterOnAllCriteria(){
        stockManagementService.setDao(dao());
        StockItemPackagingUOM so = eu().newStockItemPackagingUOM(dao());
        so.setVoided(false);
        daoInstance.saveStockItemPackagingUOM(so);
        StockItemPackagingUOMSearchFilter filter = new StockItemPackagingUOMSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockItemPackagingUOMDTO> result = stockManagementService.findStockItemPackagingUOMs(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));

        filter = new StockItemPackagingUOMSearchFilter();
        filter.setStockItemUuids(Arrays.asList(so.getStockItem().getUuid()));
        result = stockManagementService.findStockItemPackagingUOMs(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockItemPackagingUOMSearchFilter();
        filter.setStockItemIds(Arrays.asList(so.getStockItem().getId()));
        result = stockManagementService.findStockItemPackagingUOMs(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
    public void findStockOperationItems_shouldFilterOnAllCriteria(){
        stockManagementService.setDao(dao());
        StockOperationItem so = eu().newStockOperationItem(dao());
        so.setVoided(false);
        daoInstance.saveStockOperationItem(so);
        StockOperationItemSearchFilter filter=new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockOperationItemDTO> result = stockManagementService.findStockOperationItems(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));

        filter = new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setStockItemUuid(so.getStockItem().getUuid());
        result = stockManagementService.findStockOperationItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setStockOperationUuids(Arrays.asList(so.getStockOperation().getUuid()));
        result = stockManagementService.findStockOperationItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setStockOperationIds(Arrays.asList(so.getStockOperation().getId()));
        result = stockManagementService.findStockOperationItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
    public void findStockOperationItemCosts_shouldFilterOnAllCriteria(){
        stockManagementService.setDao(dao());
        StockOperationItem so = eu().newStockOperationItem(dao());
        so.setVoided(false);
        daoInstance.saveStockOperationItem(so);
        StockOperationItemSearchFilter filter=new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockOperationItemCost> result = stockManagementService.getStockOperationItemCosts(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));

        filter = new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setStockItemUuid(so.getStockItem().getUuid());
        result = stockManagementService.getStockOperationItemCosts(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setStockOperationUuids(Arrays.asList(so.getStockOperation().getUuid()));
        result = stockManagementService.getStockOperationItemCosts(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockOperationItemSearchFilter();
        filter.setIncludePackagingUnitName(true);
        filter.setIncludeStockUnitName(true);
        filter.setStockOperationIds(Arrays.asList(so.getStockOperation().getId()));
        result = stockManagementService.getStockOperationItemCosts(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
    public void findStockBatchs_shouldFilterOnAllCriteria(){
        stockManagementService.setDao(dao());
        StockBatch so = eu().newStockBatch(dao());
        so.setVoided(false);
        daoInstance.saveStockBatch(so);

        StockBatchSearchFilter filter=new StockBatchSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockBatchDTO> result = stockManagementService.findStockBatches(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));

        filter = new StockBatchSearchFilter();
        filter.setStockItemUuid(so.getStockItem().getUuid());
        result = stockManagementService.findStockBatches(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockBatchSearchFilter();
        filter.setStockItemId(so.getStockItem().getId());
        result = stockManagementService.findStockBatches(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
    public void findStockItems_shouldFilterOnAllCriteria(){
        stockManagementService.setDao(dao());
        StockItem so = eu().newStockItem(dao(), true);
        so.setVoided(false);
        daoInstance.saveStockItem(so);

        StockItemSearchFilter filter=new StockItemSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockItemDTO> result = stockManagementService.findStockItems(filter);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));

        filter = new StockItemSearchFilter();
        filter.setDrugs(Arrays.asList(so.getDrug()));
        result = stockManagementService.findStockItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockItemSearchFilter();
        filter.setConcepts(Arrays.asList(so.getConcept()));
        result = stockManagementService.findStockItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockItemSearchFilter();
        filter.setSearchEitherDrugsOrConcepts(true);
        filter.setConcepts(Arrays.asList(so.getConcept()));
        filter.setDrugs(Arrays.asList(so.getDrug()));
        result = stockManagementService.findStockItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));

        filter = new StockItemSearchFilter();
        filter.setUuid(so.getUuid());
        result = stockManagementService.findStockItems(filter);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(so.getUuid())));
    }
	
	@Test
	public void findStockOperationLinks_shouldFilterOnAllCriteria() {
		stockManagementService.setDao(dao());
		Result<StockOperationLinkDTO> result = stockManagementService.findStockOperationLinks(UUID.randomUUID().toString());
	}
	
	@Test
    public void getStockInventory_shouldFilterOnAllCriteria(){
        stockManagementService.setDao(dao());
        StockItem stockItem = eu().newStockItem(dao(), false);
        dao().saveStockItem(stockItem);

        StockItemPackagingUOM packagingUom =  entityUtil.newStockItemPackagingUOM(dao(), false, stockItem);
        packagingUom.setFactor(BigDecimal.valueOf(2));
        dao().saveStockItemPackagingUOM(packagingUom);

        StockBatch stockBatch = eu().newStockBatch(dao(),stockItem);
        dao().saveStockBatch(stockBatch);


        StockItemTransaction so = eu().newStockItemTransaction(dao(), stockItem, stockBatch, packagingUom);
        so.setQuantity(BigDecimal.valueOf(10));
        dao().saveStockItemTransaction(so);


        StockItemInventorySearchFilter filter = new StockItemInventorySearchFilter();
        List<StockItemInventorySearchFilter.ItemGroupFilter> itemsToSearch = new ArrayList<>();
        StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter = new StockItemInventorySearchFilter.ItemGroupFilter(
                Arrays.asList(so.getParty().getId()),
                so.getStockItem().getId(),
                so.getStockBatch().getId()
        );
        itemsToSearch.add(itemGroupFilter);
        filter.setItemGroupFilters(itemsToSearch);
        Result<StockItemInventory> result = stockManagementService.getStockInventory(filter, null);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));
        assertTrue(result.getData().get(0).getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN).compareTo(so.getQuantity().multiply(BigDecimal.valueOf(2))) == 0);


        filter.setDoSetQuantityUoM(true);
        result = stockManagementService.getStockInventory(filter, null);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));
        //assertEquals(result.getData().get(0).getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN),so.getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN).multiply(packagingUom.getFactor()));
        //assertTrue(result.getData().get(0).getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN).compareTo(so.getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN)) == 0);
    }
	
	@Test
	public void findStockItemTransactions_shouldFilterOnAllCriteria() {
		stockManagementService.setDao(dao());
		StockItemTransaction so = eu().newStockItemTransaction(dao());
		so.setQuantity(BigDecimal.TEN);
		dao().saveStockItemTransaction(so);
		
		StockItemTransactionSearchFilter filter = new StockItemTransactionSearchFilter();
		filter.setStockItemId(so.getStockItem().getId());
		Result<StockItemTransactionDTO> result = dao().findStockItemTransactions(filter, null);
		assertThat(result.getData().size(), greaterThanOrEqualTo(1));
		assertTrue(result.getData().get(0).getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN).compareTo(so.getQuantity()) == 0);
		
		filter = new StockItemTransactionSearchFilter();
		filter.setUuid(so.getUuid());
		result = dao().findStockItemTransactions(filter, null);
		assertThat(result.getData().size(), greaterThanOrEqualTo(1));
		assertTrue(result.getData().get(0).getQuantity().setScale(2, BigDecimal.ROUND_HALF_EVEN).compareTo(so.getQuantity()) == 0);
		assertEquals(result.getData().get(0).getUuid(), so.getUuid());
	}
	
	@Test
    public void  dispenseStockItems(){
        StockItem stockItem = eu().newStockItem(dao(), false);
        dao().saveStockItem(stockItem);

        StockItemPackagingUOM packagingUom =  entityUtil.newStockItemPackagingUOM(dao(), false, stockItem);
        dao().saveStockItemPackagingUOM(packagingUom);

        StockBatch stockBatch = eu().newStockBatch(dao(),stockItem);
        dao().saveStockBatch(stockBatch);

        for(int i = 3; i > 0; i--){
            StockItemTransaction stockItemTransaction = eu().newStockItemTransaction(dao(), stockItem, stockBatch, packagingUom);
            stockItemTransaction.setQuantity(BigDecimal.valueOf(200));
            stockItemTransaction.setPatient(null);
            dao().saveStockItemTransaction(stockItemTransaction);
        }

        List<DispenseRequest> dispenseRequests=new ArrayList<>();
        DispenseRequest dispenseRequest=new DispenseRequest();
        dispenseRequest.setPatientId(eu().getPatient().getId());
        dispenseRequest.setLocationUuid(eu().getLocation().getUuid());
        dispenseRequest.setQuantity(BigDecimal.valueOf(50));
        dispenseRequest.setStockItemUuid(stockItem.getUuid());
        dispenseRequest.setOrderId(eu().getOrder().getId());
        dispenseRequest.setEncounterId(eu().getEncounter().getId());
        dispenseRequest.setStockBatchUuid(stockBatch.getUuid());
        dispenseRequest.setStockItemPackagingUOMUuid(packagingUom.getUuid());
        dispenseRequests.add(dispenseRequest);

        dispenseRequest=new DispenseRequest();
        dispenseRequest.setPatientId(eu().getPatient().getId());
        dispenseRequest.setLocationUuid(eu().getLocation().getUuid());
        dispenseRequest.setQuantity(BigDecimal.valueOf(100));
        dispenseRequest.setStockItemUuid(stockItem.getUuid());
        dispenseRequests.add(dispenseRequest);
        dispenseRequest.setOrderId(eu().getOrder().getId());
        dispenseRequest.setEncounterId(eu().getEncounter().getId());
        dispenseRequest.setStockBatchUuid(stockBatch.getUuid());
        dispenseRequest.setStockItemPackagingUOMUuid(packagingUom.getUuid());
        stockManagementService.setDao(dao());

        stockManagementService.dispenseStockItems(dispenseRequests);

        StockItemTransactionSearchFilter searchFilter=new StockItemTransactionSearchFilter();
        searchFilter.setPartyId(eu().getParty(dao()).getId());
        searchFilter.setStockItemId(stockItem.getId());

        Result<StockItemTransactionDTO> result = dao().findStockItemTransactions(searchFilter, null);
        assertThat(result.getData().size(), greaterThanOrEqualTo(4));
        List<StockItemTransactionDTO> entity = result.getData().stream().filter(p->
                dispenseRequests.get(0).getPatientId().equals(p.getPatientId()))
                .collect(Collectors.toList());
        assertEquals(entity.size(), 2);
        assertTrue(entity.stream().anyMatch(p -> dispenseRequests.stream().anyMatch(x -> p.getQuantity().multiply(BigDecimal.valueOf(-1)).setScale(2, BigDecimal.ROUND_HALF_EVEN).compareTo(x.getQuantity()) == 0)));
    }
	
	@Test
	public void importStockItems() throws Exception {
		URL resource = getClass().getClassLoader().getResource(EntityUtil.STOCK_ITEMS_IMPORT_CSV);
		Path path = Paths.get(resource.toURI()).toFile().toPath();
		StockItemImportJob importJob = new StockItemImportJob(path, false);
		importJob.execute();
		ImportResult importResult = (ImportResult) importJob.getResult();
        if(!importResult.getSuccess()){
            throw new Exception(String.join(", ",importResult.getErrors()));
        }
        assertTrue(importResult.getSuccess());

        StockItemSearchFilter stockItemSearchFilter=new StockItemSearchFilter();
        stockItemSearchFilter.setIncludeVoided(true);
        Result<StockItemDTO> stockItemDTOs = dao().findStockItems(stockItemSearchFilter);

        assertEquals(5, stockItemDTOs.getData().size());
        Optional<StockItemDTO> stockItemOptional = stockItemDTOs.getData().stream().filter(p -> p.getDrugId().equals(2)).findFirst();
        assertTrue("Drug Stock item is present", stockItemOptional.isPresent());
        StockItemDTO stockItem = stockItemOptional.get();
        assertTrue(stockItem.getHasExpiration());
        assertEquals(stockItem.getCommonName(), "TEST 2");
        assertNull(stockItem.getAcronym());
        assertEquals(stockItem.getPreferredVendorName(), "National Medical Stores");
        assertEquals(stockItem.getDispensingUnitId(), (Integer) 20);
        assertEquals(stockItem.getDispensingUnitPackagingConceptId(), (Integer) 20);
        assertEquals(stockItem.getReorderLevel().compareTo(BigDecimal.valueOf(50)), 0);
        assertEquals(stockItem.getReorderLevelConceptId(), (Integer) 20);
        assertEquals(stockItem.getPurchasePrice().compareTo(BigDecimal.valueOf(13)), 0);
        assertEquals(stockItem.getPurchasePriceConceptId(), (Integer) 20);


        stockItemOptional = stockItemDTOs.getData().stream().filter(p -> p.getConceptId().equals(5497)).findFirst();
        assertTrue("Concenpt Stock item is present", stockItemOptional.isPresent());
        stockItem = stockItemOptional.get();
        assertFalse(stockItem.getHasExpiration());
        assertEquals(stockItem.getCommonName(), "TEST 2");
        assertNull(stockItem.getAcronym());
        assertEquals(stockItem.getPreferredVendorName(), "Uganda Medical Stores");
        assertEquals(stockItem.getDispensingUnitId(), (Integer) 20);
        assertEquals(stockItem.getDispensingUnitPackagingConceptId(), (Integer) 20);
        assertEquals(stockItem.getReorderLevel().compareTo(BigDecimal.valueOf(50)), 0);
        assertEquals(stockItem.getReorderLevelConceptId(), (Integer) 20);
        assertEquals(stockItem.getPurchasePrice().compareTo(BigDecimal.valueOf(13)), 0);
        assertEquals(stockItem.getPurchasePriceConceptId(), (Integer) 20);

	}
	
	private void deleteAllStockItems() {
		DbSession session = dao().getSession();
		Query query = session.createQuery("delete from stockmanagement.StockItem");
		query.executeUpdate();
	}
	
	@Test
	public void getStockItemByDrug() {
		deleteAllStockItems();
		Context.flushSession();
		Context.flushSession();
		
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		stockManagementService.setDao(dao());
        List<StockItem> stockItemLeft = stockManagementService.getStockItemByDrug(stockItem.getDrug().getId());
		assertNotNull(stockItemLeft);
        assertTrue(stockItemLeft.stream().map(StockItem::getUuid).collect(Collectors.toList()).contains(stockItem.getUuid()));
	}
	
	@Test
	public void getStockItemByConcept() {
		StockItem stockItem = eu().newStockItem(dao(), true);
		stockItem.setDrug(null);
		dao().saveStockItem(stockItem);
		
		stockManagementService.setDao(dao());
        List<StockItem> stockItemLeft = stockManagementService.getStockItemByConcept(stockItem.getConcept().getId());
		assertNotNull(stockItemLeft);
        assertTrue(stockItemLeft.stream().map(StockItem::getUuid).collect(Collectors.toList()).contains(stockItem.getUuid()));
	}
	
	@Test
	public void getStockItemPackagingUOMByConcept() {
		stockManagementService.setDao(dao());
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		
		StockItemPackagingUOM packagingUomLeft = stockManagementService.getStockItemPackagingUOMByConcept(stockItem.getId(),
		    packagingUom.getPackagingUom().getConceptId());
		assertNotNull(packagingUomLeft);
		assertEquals(packagingUom.getUuid(), packagingUomLeft.getUuid());
	}
	
	@Test
	public void getStockItemPackagingUOMByConceptStockItemIdAndConceptUuid() {
		stockManagementService.setDao(dao());
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		
		StockItemPackagingUOM packagingUomLeft = stockManagementService.getStockItemPackagingUOMByConcept(stockItem.getId(),
		    packagingUom.getPackagingUom().getUuid());
		assertNotNull(packagingUomLeft);
		assertEquals(packagingUom.getUuid(), packagingUomLeft.getUuid());
	}
	
	@Test
	public void getStockItemPackagingUOMByConceptByStockItemUuidAndConceptId() {
		stockManagementService.setDao(dao());
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		
		StockItemPackagingUOM packagingUomLeft = stockManagementService.getStockItemPackagingUOMByConcept(
		    stockItem.getUuid(), packagingUom.getPackagingUom().getConceptId());
		assertNotNull(packagingUomLeft);
		assertEquals(packagingUom.getUuid(), packagingUomLeft.getUuid());
	}
	
	@Test
	public void getStockItemPackagingUOMByConceptByStockItemUuidAndConceptUuid() {
		stockManagementService.setDao(dao());
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		
		StockItemPackagingUOM packagingUomLeft = stockManagementService.getStockItemPackagingUOMByConcept(
		    stockItem.getUuid(), packagingUom.getPackagingUom().getUuid());
		assertNotNull(packagingUomLeft);
		assertEquals(packagingUom.getUuid(), packagingUomLeft.getUuid());
	}
	
	@Test
	public void getOrderItemsByOrderByOrderIds() {
		stockManagementService.setDao(dao());
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		//Given
		OrderItem orderItem = eu().newOrderItem(dao(), stockItem, packagingUom);
		dao().saveOrderItem(orderItem);
		List<OrderItem> orderItems = stockManagementService.getOrderItemsByOrder(orderItem.getOrder().getId());
		assertNotNull(orderItems);
		assertEquals(orderItems.size(), 1);
		assertEquals(orderItems.get(0).getUuid(), orderItem.getUuid());
	}
	
	@Test
	public void getOrderItemsByEncounterByEncounterIds() {
		stockManagementService.setDao(dao());
		StockItem stockItem = eu().newStockItem(dao(), false);
		dao().saveStockItem(stockItem);
		
		StockItemPackagingUOM packagingUom = eu().newStockItemPackagingUOM(dao(), false, stockItem);
		packagingUom.setFactor(BigDecimal.valueOf(2));
		dao().saveStockItemPackagingUOM(packagingUom);
		//Given
		OrderItem orderItem = eu().newOrderItem(dao(), stockItem, packagingUom);
		dao().saveOrderItem(orderItem);
		List<OrderItem> orderItems = stockManagementService.getOrderItemsByEncounter(orderItem.getOrder().getEncounter()
		        .getEncounterId());
		assertNotNull(orderItems);
		assertEquals(orderItems.size(), 1);
		assertEquals(orderItems.get(0).getUuid(), orderItem.getUuid());
	}
	
	private void updateOrderScheduledDate(Order order, Date scheduledDate) {
		DbSession session = dao().getSession();
		Query query = session.createQuery("Update Order set scheduledDate = :scheduledDate where orderId = :orderId");
		query.setParameter("scheduledDate", scheduledDate);
		query.setParameter("orderId", order.getOrderId());
		query.executeUpdate();
		Context.flushSession();
		Context.flushSession();
	}
	
	@Test
    public void findOrderItems(){
        stockManagementService.setDao(dao());
        StockItem stockItem = eu().newStockItem(dao(), false);
        dao().saveStockItem(stockItem);

        StockItemPackagingUOM packagingUom =  eu().newStockItemPackagingUOM(dao(), false, stockItem);
        packagingUom.setFactor(BigDecimal.valueOf(2));
        dao().saveStockItemPackagingUOM(packagingUom);
//Given
        Party party = eu().getParty(dao());

        OrderItem orderItem = eu().newOrderItem(dao(), stockItem, packagingUom);
//
        orderItem.setCreatedFrom(party.getLocation());
        orderItem.setFulfilmentLocation(party.getLocation());
        orderItem.setVoided(false);
        dao().saveOrderItem(orderItem);

        Date scheduledDate = DateUtil.today();
        updateOrderScheduledDate(orderItem.getOrder(), scheduledDate);

        OrderItemSearchFilter filter = new OrderItemSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<OrderItemDTO> result = stockManagementService.findOrderItems(filter, null);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));
        assertEquals(result.getData().get(0).getUuid(), orderItem.getUuid());

        filter = new OrderItemSearchFilter();
        filter.setId(orderItem.getId());
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setId(Integer.MAX_VALUE);
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setEncounterIds(Arrays.asList(orderItem.getOrder().getEncounter().getEncounterId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setEncounterIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setEncounterUuids(Arrays.asList(orderItem.getOrder().getEncounter().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setEncounterUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setUuid(orderItem.getUuid());
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setUuid(UUID.randomUUID().toString());
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());


        filter = new OrderItemSearchFilter();
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setOrderDateMin(DateUtils.addDays(scheduledDate, 1));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setOrderDateMax(scheduledDate);
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setOrderDateMax(DateUtils.addDays(scheduledDate,-1));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setOrderIds(Arrays.asList(orderItem.getOrder().getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setOrderIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setOrderUuids(Arrays.asList(orderItem.getOrder().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setOrderUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setOrderNumber(orderItem.getOrder().getOrderNumber());
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setOrderNumber(orderItem.getOrder().getOrderNumber().substring(0, orderItem.getOrder().getOrderNumber().length() - 1));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setOrderNumber(UUID.randomUUID().toString());
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setPatientIds(Arrays.asList(orderItem.getOrder().getPatient().getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setPatientIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setStockItemIds(Arrays.asList(stockItem.getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setStockItemIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setStockItemUuids(Arrays.asList(orderItem.getStockItem().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setStockItemUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setDrugIds(Arrays.asList(stockItem.getDrug().getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setDrugIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setDrugUuids(Arrays.asList(stockItem.getDrug().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setDrugUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setConceptIds(Arrays.asList(stockItem.getConcept().getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setConceptIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setConceptUuids(Arrays.asList(stockItem.getConcept().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setConceptUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setDrugIds(Arrays.asList(stockItem.getDrug().getId()));
        filter.setConceptUuids(Arrays.asList(stockItem.getConcept().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setSearchEitherDrugOrConceptStockItems(true);
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setConceptUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setDrugIds(Arrays.asList(Integer.MAX_VALUE));
        filter.setConceptUuids(Arrays.asList(stockItem.getConcept().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setConceptUuids(Arrays.asList(UUID.randomUUID().toString()));
        filter.setConceptUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setCreatedFromLocationIds(Arrays.asList(orderItem.getCreatedFrom().getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setCreatedFromLocationIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setCreatedFromLocationUuids(Arrays.asList(orderItem.getCreatedFrom().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setCreatedFromLocationUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setFulfilmentLocationIds(Arrays.asList(orderItem.getFulfilmentLocation().getId()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setFulfilmentLocationIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setFulfilmentLocationUuids(Arrays.asList(orderItem.getFulfilmentLocation().getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setFulfilmentLocationUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setCreatedFromPartyUuids(Arrays.asList(party.getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setCreatedFromPartyUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setCreatedFromPartyUuids(Arrays.asList(party.getUuid()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setCreatedFromPartyUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new OrderItemSearchFilter();
        filter.setId(orderItem.getId());
        filter.setUuid(orderItem.getUuid());
        filter.setOrderIds(Arrays.asList(orderItem.getOrder().getId()));
        filter.setOrderNumber(orderItem.getOrder().getOrderNumber());
        filter.setPatientIds(Arrays.asList(orderItem.getOrder().getPatient().getId()));
        filter.setStockItemIds(Arrays.asList(stockItem.getId()));
        filter.setStockItemUuids(Arrays.asList(orderItem.getStockItem().getUuid()));
        filter.setDrugIds(Arrays.asList(stockItem.getDrug().getId()));
        filter.setDrugUuids(Arrays.asList(stockItem.getDrug().getUuid()));
        filter.setConceptIds(Arrays.asList(stockItem.getConcept().getId()));
        filter.setConceptUuids(Arrays.asList(stockItem.getConcept().getUuid()));
        filter.setSearchEitherDrugOrConceptStockItems(true);
        filter.setCreatedFromLocationIds(Arrays.asList(orderItem.getCreatedFrom().getId()));
        filter.setCreatedFromLocationUuids(Arrays.asList(orderItem.getCreatedFrom().getUuid()));
        filter.setFulfilmentLocationIds(Arrays.asList(orderItem.getFulfilmentLocation().getId()));
        filter.setFulfilmentLocationUuids(Arrays.asList(orderItem.getFulfilmentLocation().getUuid()));
        filter.setCreatedFromPartyUuids(Arrays.asList(party.getUuid()));
        filter.setEncounterIds(Arrays.asList(orderItem.getOrder().getEncounter().getEncounterId()));
        filter.setEncounterUuids(Arrays.asList(orderItem.getOrder().getEncounter().getUuid()));
        filter.setCreatedFromPartyUuids(Arrays.asList(party.getUuid()));
        filter.setOrderDateMin(scheduledDate);
        filter.setOrderDateMax(scheduledDate);
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(orderItem.getUuid())));

        filter.setId(Integer.MAX_VALUE);
        result = stockManagementService.findOrderItems(filter, null);
        assertTrue(result.getData().isEmpty());
    }
	
	@Test
	public void synchronizeTags() {
		LocationTagsSynchronize locationTagsSynchronize = new LocationTagsSynchronize();
		locationTagsSynchronize.execute();
		LocationService locationService = Context.getLocationService();
		LocationTag mainPharmacy = locationService.getLocationTagByName(StockLocationTags.MAIN_PHARMACY_LOCATION_TAG);
		assertNotNull(mainPharmacy);
		LocationTag mainStore = locationService.getLocationTagByName(StockLocationTags.MAIN_STORE_LOCATION_TAG);
		assertNotNull(mainStore);
		LocationTag dispensary = locationService.getLocationTagByName(StockLocationTags.DISPENSARY_LOCATION_TAG);
		assertNotNull(dispensary);
		assertTrue(!locationService.getLocationsHavingAnyTag(Arrays.asList(mainPharmacy)).isEmpty());
		assertTrue(!locationService.getLocationsHavingAnyTag(Arrays.asList(mainStore)).isEmpty());
	}
	
	@Test
    public void findStockRules(){
        stockManagementService.setDao(dao());
        StockItem stockItem = eu().newStockItem(dao(), false);
        dao().saveStockItem(stockItem);

        StockItemPackagingUOM packagingUom =  eu().newStockItemPackagingUOM(dao(), false, stockItem);
        packagingUom.setFactor(BigDecimal.valueOf(2));
        dao().saveStockItemPackagingUOM(packagingUom);

        StockRule stockRule = eu().newStockRule(dao(), stockItem, packagingUom);
        stockRule.setVoided(false);
        stockRule.setEnabled(true);
        dao().saveStockRule(stockRule);

        StockRuleSearchFilter filter = new StockRuleSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<StockRuleDTO> result = stockManagementService.findStockRules(filter, null);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));
        assertEquals(result.getData().get(0).getUuid(), stockRule.getUuid());

        filter = new StockRuleSearchFilter();
        filter.setId(stockRule.getId());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setId(Integer.MAX_VALUE);
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setEnabled(true);
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setEnabled(false);
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setHasNotificationRoleSet(true);
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setHasNotificationRoleSet(false);
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setUuids(Arrays.asList(stockRule.getUuid()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setStockItemUuids(Arrays.asList(stockItem.getUuid()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setStockItemUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setLocationUuids(Arrays.asList(entityUtil.getLocation().getUuid()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setLocationUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setLastEvaluationMin(DateUtils.addDays(stockRule.getLastEvaluation(), 1));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setLastEvaluationMin(stockRule.getLastEvaluation());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setLastEvaluationMax(DateUtils.addDays(stockRule.getLastEvaluation(), -1));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setLastEvaluationMax(stockRule.getLastEvaluation());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));
        
        filter = new StockRuleSearchFilter();
        filter.setNextEvaluationMin(DateUtils.addDays(stockRule.getNextEvaluation(), 1));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setNextEvaluationMin(stockRule.getNextEvaluation());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setNextEvaluationMax(DateUtils.addDays(stockRule.getNextEvaluation(),-1));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setNextEvaluationMax(stockRule.getNextEvaluation());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter = new StockRuleSearchFilter();
        filter.setLastActionDateMin(DateUtils.addDays(stockRule.getLastActionDate(), 1));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setLastActionDateMin(stockRule.getLastActionDate());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setLastActionDateMax(DateUtils.addDays(stockRule.getLastActionDate(), -1));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new StockRuleSearchFilter();
        filter.setLastActionDateMax(stockRule.getLastActionDate());
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));
        
        filter = new StockRuleSearchFilter();
        filter.setId(stockRule.getId());
        filter.setUuids(Arrays.asList(stockRule.getUuid()));
        filter.setStockItemUuids(Arrays.asList(stockItem.getUuid()));
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(stockRule.getUuid())));

        filter.setId(Integer.MAX_VALUE);
        result = stockManagementService.findStockRules(filter, null);
        assertTrue(result.getData().isEmpty());
    }
	
	@Test
    public void findBatchJobs(){
        stockManagementService.setDao(dao());
        BatchJobDTO batchJobDTO=new BatchJobDTO();
        batchJobDTO.setBatchJobType(BatchJobType.Report);
        batchJobDTO.setDescription("Stock Receipt Report");
        StringBuilder parameters = new StringBuilder();
        String newLine = "\r\n";
        parameters.append( "param.report: " + "STOCK_RECEIPT_REPORT");
        parameters.append(newLine);
        parameters.append("param.StartDate.description: Starting");
        parameters.append(newLine);
        Date date = DateUtils.addDays(new Date(),90);
        parameters.append("param.StartDate.value.desc: " + DateUtil.formatDDMMMyyyy(date));
        parameters.append(newLine);
        parameters.append("param.StartDate.value: " + DateUtil.formatDateForJson(date));
        parameters.append(newLine);
        parameters.append("param.EndDate.description: Ending");
        parameters.append(newLine);
        date = new Date();
        parameters.append("param.EndDate.value.desc: " + DateUtil.formatDDMMMyyyy(date));
        parameters.append(newLine);
        parameters.append("param.EndDate.value: " + DateUtil.formatDateForJson(date));
        parameters.append(newLine);
        parameters.append("param.StockItemCategory.description: Stock Item Category");
        parameters.append(newLine);
        date = new Date();
        parameters.append("param.StockItemCategory.value.desc: Cough Syrup");
        parameters.append(newLine);
        parameters.append("param.StockItemCategory.value: 0cbe2ed3-cd5f-4f46-9459-26127c9265ab");
        parameters.append(newLine);
        parameters.append("param.Location.description: Location");
        parameters.append(newLine);
        date = new Date();
        parameters.append("param.Location.value.desc: Unknown Location");
        parameters.append(newLine);
        parameters.append("param.Location.value: 8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
        parameters.append(newLine);
        parameters.append("param.ChildLocations.description: Include Child Locations");
        parameters.append(newLine);
        date = new Date();
        parameters.append("param.ChildLocations.value.desc: Yes");
        parameters.append(newLine);
        parameters.append("param.ChildLocations.value: true");
        parameters.append(newLine);
        batchJobDTO.setParameters(parameters.toString());
        batchJobDTO.setLocationScopeUuid("8d6c993e-c2cc-11de-8d13-0010c6dffd0f");
        batchJobDTO.setPrivilegeScope(Privileges.APP_STOCKMANAGEMENT_REPORTS);
        BatchJobDTO batchJob = stockManagementService.saveBatchJob(batchJobDTO);

        Location location = Context.getLocationService().getLocationByUuid("8d6c993e-c2cc-11de-8d13-0010c6dffd0f");

        BatchJobSearchFilter filter = new BatchJobSearchFilter();
        filter.setLimit(10);
        filter.setStartIndex(0);
        Result<BatchJobDTO> result = stockManagementService.findBatchJobs(filter, null);
        assertThat(result.getData().size(), greaterThanOrEqualTo(1));
        assertEquals(result.getData().get(0).getUuid(), batchJob.getUuid());

        filter = new BatchJobSearchFilter();
        filter.setBatchJobIds(Arrays.asList(batchJob.getId()));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setBatchJobIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setBatchJobUuids(Arrays.asList(batchJob.getUuid()));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setBatchJobUuids(Arrays.asList(UUID.randomUUID().toString()));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setParameters(batchJob.getParameters());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setParameters(UUID.randomUUID().toString());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setPrivilegeScope(batchJob.getPrivilegeScope());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setPrivilegeScope(UUID.randomUUID().toString());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setLocationScopeIds(Arrays.asList(location.getId()));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setLocationScopeIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setBatchJobType(BatchJobType.Report);
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setBatchJobType(BatchJobType.Other);
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setBatchJobStatus(Arrays.asList(BatchJobStatus.Pending));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setBatchJobStatus(Arrays.asList(BatchJobStatus.Cancelled));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setDateCreatedMin(batchJob.getDateCreated());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setDateCreatedMin(DateUtils.addDays(batchJob.getDateCreated(), 1));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();
        filter.setDateCreatedMax(batchJob.getDateCreated());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setDateCreatedMax(DateUtils.addDays(batchJob.getDateCreated(), -1));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());

        filter = new BatchJobSearchFilter();

        filter.setBatchJobIds(Arrays.asList(batchJob.getId()));
        filter.setBatchJobUuids(Arrays.asList(batchJob.getUuid()));
        filter.setParameters(batchJob.getParameters());
        filter.setPrivilegeScope(batchJob.getPrivilegeScope());
        filter.setLocationScopeIds(Arrays.asList(location.getId()));
        filter.setBatchJobType(BatchJobType.Report);
        filter.setBatchJobStatus(Arrays.asList(BatchJobStatus.Pending));
        filter.setDateCreatedMin(batchJob.getDateCreated());
        filter.setDateCreatedMax(batchJob.getDateCreated());
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().stream().anyMatch(p -> p.getUuid().equalsIgnoreCase(batchJob.getUuid())));

        filter.setBatchJobIds(Arrays.asList(Integer.MAX_VALUE));
        result = stockManagementService.findBatchJobs(filter, null);
        assertTrue(result.getData().isEmpty());
    }
}
