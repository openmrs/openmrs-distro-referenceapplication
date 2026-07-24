/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement.api.impl;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openmrs.*;
import org.openmrs.api.APIException;
import org.openmrs.api.LocationService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.StockLocationTags;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.StockOperationPrivelegeTarget;
import org.openmrs.module.stockmanagement.api.dao.StockManagementDao;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.dto.reporting.*;
import org.openmrs.module.stockmanagement.api.jobs.AsyncTasksBatchJob;
import org.openmrs.module.stockmanagement.api.jobs.StockBatchExpiryJob;
import org.openmrs.module.stockmanagement.api.jobs.StockRuleEvaluationJob;
import org.openmrs.module.stockmanagement.api.reporting.Report;
import org.openmrs.module.stockmanagement.api.utils.*;
import org.openmrs.module.stockmanagement.api.jobs.StockItemImportJob;
import org.openmrs.module.stockmanagement.api.model.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.stockmanagement.tasks.StockOperationNotificationTask;
import org.openmrs.notification.Alert;
import org.openmrs.notification.Template;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.util.Assert;

import javax.mail.Session;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockManagementServiceImpl extends BaseOpenmrsService implements StockManagementService {

    StockManagementDao dao;

    private Log log = LogFactory.getLog(this.getClass());

    private static final UUID STOCK_OPERATION_PROCESSING_LOCK = UUID.randomUUID();
    private static final UUID DISPENSING_PROCESSING_LOCK = UUID.randomUUID();
    private static final Map<String, UUID> LOCATION_DISPENSING_OPERATION_LOCKS = new HashMap<>();

    public StockManagementServiceImpl() {

    }

    /**
     * Injected in moduleApplicationContext.xml
     */
    public void setDao(StockManagementDao dao) {
        this.dao = dao;
    }

    @Override
    public List<LocationTree> getCompleteLocationTree() {
        return dao.getCompleteLocationTree();
    }

    @Override
    public void deleteLocationTreeNodes(List<LocationTree> nodes) {
        dao.deleteLocationTreeNodes(nodes);
    }

    @Override
    public void saveLocationTreeNodes(List<LocationTree> nodes) {
        dao.saveLocationTreeNodes(nodes);
    }

    public List<LocationTree> getCompleteLocationTree(Integer atLocationId) {
        return dao.getCompleteLocationTree(atLocationId);
    }

    public List<PartyDTO> getCompletePartyList(Integer atLocationId) {
        List<LocationTree> locations = getCompleteLocationTree(atLocationId);
        if (locations == null || locations.isEmpty())
            return new ArrayList<>();

        PartySearchFilter partySearchFilter = new PartySearchFilter();
        partySearchFilter.setIncludeVoided(false);
        partySearchFilter
                .setLocationIds(locations.stream().map(p -> p.getChildLocationId()).collect(Collectors.toList()));
        return dao.findParty(partySearchFilter).getData();
    }

    public List<PartyDTO> getAllStockHoldingPartyList() {
        LocationService locationService = Context.getLocationService();

        List<LocationTag> locationTags = new ArrayList<>();
        LocationTag locationTag = locationService.getLocationTagByName(StockLocationTags.MAIN_PHARMACY_LOCATION_TAG);
        if (locationTag != null)
            locationTags.add(locationTag);
        locationTag = locationService.getLocationTagByName(StockLocationTags.DISPENSARY_LOCATION_TAG);
        if (locationTag != null)
            locationTags.add(locationTag);
        locationTag = locationService.getLocationTagByName(StockLocationTags.MAIN_STORE_LOCATION_TAG);
        if (locationTag != null)
            locationTags.add(locationTag);

        if (locationTags.isEmpty()) {
            return new ArrayList<>();
        }

        final List<Location> stockLocations = locationService.getLocationsHavingAnyTag(locationTags);
        stockLocations.removeIf(p -> p.getRetired());
        if (stockLocations.isEmpty()) return new ArrayList<>();

        PartySearchFilter partySearchFilter = new PartySearchFilter();
        partySearchFilter.setIncludeVoided(false);
        partySearchFilter.setLocationIds(stockLocations.stream().map(p -> p.getLocationId()).collect(Collectors.toList()));
        return dao.findParty(partySearchFilter).getData();
    }

    public List<PartyDTO> getCompleteStockDispensingLocationPartyList(Integer atLocationId) {
        List<LocationTree> locations = getCompleteLocationTree(atLocationId);
        if (locations == null || locations.isEmpty())
            return new ArrayList<>();
        LocationService locationService = Context.getLocationService();
        List<LocationTag> locationTags = new ArrayList<>();
        LocationTag locationTag = locationService.getLocationTagByName(StockLocationTags.MAIN_PHARMACY_LOCATION_TAG);
        if (locationTag != null)
            locationTags.add(locationTag);
        locationTag = locationService.getLocationTagByName(StockLocationTags.DISPENSARY_LOCATION_TAG);
        if (locationTag != null)
            locationTags.add(locationTag);

        final List<Location> stockLocations = locationService.getLocationsHavingAnyTag(locationTags);
        locations.removeIf(p -> !stockLocations.stream().anyMatch(x -> x.getId().equals(p.getChildLocationId())));
        if (locations == null || locations.isEmpty())
            return new ArrayList<>();

        PartySearchFilter partySearchFilter = new PartySearchFilter();
        partySearchFilter.setIncludeVoided(false);
        partySearchFilter
                .setLocationIds(locations.stream().map(p -> p.getChildLocationId()).collect(Collectors.toList()));
        return dao.findParty(partySearchFilter).getData();
    }

    public List<Location> getMainPharmacyLocations() {
        LocationService locationService = Context.getLocationService();
        LocationTag locationTag = locationService.getLocationTagByName(StockLocationTags.MAIN_PHARMACY_LOCATION_TAG);
        if (locationTag == null) {
            return new ArrayList<>();
        }
        List<Location> locations = locationService.getLocationsByTag(locationTag);
        return locations;
    }

    public List<PartyDTO> getMainPharmacyPartyList() {
        List<Location> locations = getMainPharmacyLocations();
        if (locations == null || locations.isEmpty())
            return new ArrayList<>();

        PartySearchFilter partySearchFilter = new PartySearchFilter();
        partySearchFilter.setIncludeVoided(false);
        partySearchFilter.setLocationIds(locations.stream().map(p -> p.getId()).collect(Collectors.toList()));
        return dao.findParty(partySearchFilter).getData();
    }

    public Result<UserRoleScopeLocation> findUserRoleScopeLocations(UserRoleScopeLocationSearchFilter filter) {
        return dao.findUserRoleScopeLocations(filter);
    }

    public Result<UserRoleScopeOperationType> findUserRoleScopeOperationTypeFilters(
            UserRoleScopeOperationTypeSearchFilter filter) {
        return dao.findUserRoleScopeOperationTypeFilters(filter);
    }

    public List<StockOperationType> getAllStockOperationTypes() {
        return dao.getAllStockOperationTypes();
    }

    public List<StockOperationTypeLocationScope> getAllStockOperationTypeLocationScopes() {
        return dao.getAllStockOperationTypeLocationScopes();
    }

    public Result<UserRoleScopeDTO> findUserRoleScopes(UserRoleScopeSearchFilter filter) {
        Result<UserRoleScopeDTO> result = new Result<>(new ArrayList<>(), (long) 0);
        result.setPageIndex(filter.getStartIndex());
        result.setPageSize(filter.getLimit());
        if (filter != null && StringUtils.isNotEmpty(filter.getName())) {
            List<User> users = Context.getUserService().getUsers(filter.getName(), new ArrayList<>(), true);
            if (users.isEmpty())
                return result;
            if (filter.getUsers() == null || filter.getUsers().isEmpty()) {
                filter.setUsers(users);
            } else {
                filter.getUsers().removeIf(p -> !users.stream().anyMatch(x -> x.getUserId().equals(p.getUserId())));
                if (filter.getUsers().isEmpty())
                    return result;
            }
        }

        Result<UserRoleScope> userRoleScopes = dao.findUserRoleScopes(filter);
        userRoleScopes.copyPagingInfoTo(result);
        if (userRoleScopes.getData().isEmpty())
            return result;

        Result<UserRoleScopeOperationType> userRoleScopeOperationTypes = findUserRoleScopeOperationTypeFilters(
                new UserRoleScopeOperationTypeSearchFilter(userRoleScopes.getData()));
        Result<UserRoleScopeLocation> userRoleScopeLocations = findUserRoleScopeLocations(
                new UserRoleScopeLocationSearchFilter(userRoleScopes.getData()));

        List<Location> locations = Context.getLocationService().getAllLocations(true);
        List<StockOperationType> operationTypes = getAllStockOperationTypes();

        for (UserRoleScope userRoleScope : userRoleScopes.getData()) {
            UserRoleScopeDTO userRoleScopeDTO = new UserRoleScopeDTO();
            userRoleScopeDTO.setId(userRoleScope.getId());
            userRoleScopeDTO.setActiveFrom(userRoleScope.getActiveFrom());
            userRoleScopeDTO.setActiveTo(userRoleScope.getActiveTo());
            userRoleScopeDTO.setEnabled(userRoleScope.getEnabled());
            userRoleScopeDTO.setPermanent(userRoleScope.getPermanent());
            userRoleScopeDTO.setUserUuid(userRoleScope.getUser().getUuid());
            userRoleScopeDTO.setRole(userRoleScope.getRole().getRole());
            userRoleScopeDTO.setUserName(userRoleScope.getUser().getUsername());
            userRoleScopeDTO.setUuid(userRoleScope.getUuid());
            userRoleScopeDTO.setUserFamilyName(userRoleScope.getUser().getFamilyName());
            userRoleScopeDTO.setUserGivenName(userRoleScope.getUser().getGivenName());
            userRoleScopeDTO.setOperationTypes(mapToUserRoleScopeOperationTypeDTO(userRoleScopeOperationTypes.getData()
                    .stream().filter(p -> p.getUserRoleScope().getId().equals(userRoleScope.getId()))
                    .collect(Collectors.toList()), operationTypes));
            userRoleScopeDTO.setLocations(mapToUserRoleScopeLocationDTO(userRoleScopeLocations.getData().stream()
                    .filter(p -> p.getUserRoleScope().getId().equals(userRoleScope.getId()))
                    .collect(Collectors.toList()), locations));
            result.getData().add(userRoleScopeDTO);

        }
        return result;
    }

    private List<UserRoleScopeLocationDTO> mapToUserRoleScopeLocationDTO(
            List<UserRoleScopeLocation> userRoleScopeLocations, List<Location> locations) {
        List<UserRoleScopeLocationDTO> result = new ArrayList<>();
        for (UserRoleScopeLocation userRoleScopeLocation : userRoleScopeLocations) {
            Optional<Location> location = locations.stream()
                    .filter(p -> p.getId().equals(userRoleScopeLocation.getLocation().getId())).findFirst();
            if (!location.isPresent())
                continue;
            UserRoleScopeLocationDTO userRoleScopeLocationDTO = new UserRoleScopeLocationDTO();
            userRoleScopeLocationDTO.setUuid(userRoleScopeLocation.getUuid());
            userRoleScopeLocationDTO.setEnableDescendants(userRoleScopeLocation.getEnableDescendants());
            userRoleScopeLocationDTO.setLocationName(location.get().getName());
            userRoleScopeLocationDTO.setLocationUuid(location.get().getUuid());
            userRoleScopeLocationDTO.setUserRoleScopeId(userRoleScopeLocation.getUserRoleScope().getId());
            result.add(userRoleScopeLocationDTO);
        }
        return result;
    }

    private List<UserRoleScopeOperationTypeDTO> mapToUserRoleScopeOperationTypeDTO(
            List<UserRoleScopeOperationType> userRoleScopeOperationTypes, List<StockOperationType> operationTypes) {
        List<UserRoleScopeOperationTypeDTO> result = new ArrayList<>();
        for (UserRoleScopeOperationType userRoleScopeOperationType : userRoleScopeOperationTypes) {
            Optional<StockOperationType> stockOperationType = operationTypes.stream()
                    .filter(p -> p.getUuid().equals(userRoleScopeOperationType.getStockOperationType().getUuid()))
                    .findFirst();
            if (!stockOperationType.isPresent())
                continue;
            UserRoleScopeOperationTypeDTO userRoleScopeOperationTypeDTO = new UserRoleScopeOperationTypeDTO();
            userRoleScopeOperationTypeDTO.setUuid(userRoleScopeOperationType.getUuid());
            userRoleScopeOperationTypeDTO.setOperationTypeName(stockOperationType.get().getName());
            userRoleScopeOperationTypeDTO.setOperationTypeUuid(stockOperationType.get().getUuid());
            userRoleScopeOperationTypeDTO.setUserRoleScopeId(userRoleScopeOperationType.getUserRoleScope().getId());
            result.add(userRoleScopeOperationTypeDTO);
        }
        return result;
    }

    public StockOperation getStockOperationByUuid(String uuid) {
        return dao.getStockOperationByUuid(uuid);
    }

    public StockOperationType getStockOperationTypeByUuid(String uuid) {
        return dao.getStockOperationTypeByUuid(uuid);
    }

    public StockOperationType getStockOperationTypeByType(String type) {
        return dao.getStockOperationTypeByType(type);
    }

    public StockOperationTypeLocationScope getStockOperationTypeLocationScopeByUuid(String uuid) {
        return dao.getStockOperationTypeLocationScopeByUuid(uuid);
    }

    public UserRoleScopeOperationType getUserRoleScopeOperationTypeByUuid(String uuid) {
        return dao.getUserRoleScopeOperationTypeByUuid(uuid);
    }

    public void voidUserRoleScopes(List<String> userRoleScopeIds, String reason) {
        dao.voidUserRoleScopes(userRoleScopeIds, reason, Context.getAuthenticatedUser().getUserId());
    }

    @Override
    public UserRoleScope getUserRoleScopeByUuid(String uuid) {
        return dao.getUserRoleScopeByUuid(uuid);
    }

    @Override
    public UserRoleScope saveUserRoleScope(UserRoleScope userRoleScope) {
        if (userRoleScope.getId() != null && userRoleScope.getId() > 0) {
            userRoleScope.setChangedBy(Context.getAuthenticatedUser());
            userRoleScope.setDateChanged(new Date());
        }
        return dao.saveUserRoleScope(userRoleScope);
    }

    public UserRoleScopeLocation saveUserRoleScopeLocation(UserRoleScopeLocation userRoleScopeLocation) {
        if (userRoleScopeLocation.getId() != null && userRoleScopeLocation.getId() > 0) {
            userRoleScopeLocation.setChangedBy(Context.getAuthenticatedUser());
            userRoleScopeLocation.setDateChanged(new Date());
        }
        return dao.saveUserRoleScopeLocation(userRoleScopeLocation);
    }

    public UserRoleScopeOperationType saveUserRoleScopeOperationType(
            UserRoleScopeOperationType userRoleScopeOperationType) {
        if (userRoleScopeOperationType.getId() != null && userRoleScopeOperationType.getId() > 0) {
            userRoleScopeOperationType.setChangedBy(Context.getAuthenticatedUser());
            userRoleScopeOperationType.setDateChanged(new Date());
        }
        return dao.saveUserRoleScopeOperationType(userRoleScopeOperationType);
    }

    public void voidUserRoleScopeLocations(List<String> userRoleScopeLocationIds, String reason, int voidedBy) {
        dao.voidUserRoleScopeLocations(userRoleScopeLocationIds, reason, voidedBy);
    }

    public void voidUserRoleScopeOperationTypes(List<String> userRoleScopeOperationTypeIds, String reason,
                                                int voidedBy) {
        dao.voidUserRoleScopeOperationTypes(userRoleScopeOperationTypeIds, reason, voidedBy);

    }

    public Result<StockItem> findStockItemEntities(StockItemSearchFilter filter) {
        return dao.findStockItemEntities(filter);
    }

    public List<Integer> searchStockItemCommonName(String text, Boolean isDrugSearch, boolean includeAll,
                                                   int maxItems) {
        return dao.searchStockItemCommonName(text, isDrugSearch, includeAll, maxItems);
    }

    public Result<StockItemDTO> findStockItems(StockItemSearchFilter filter) {
        Result<StockItemDTO> result = dao.findStockItems(filter);
        List<UserPersonNameDTO> personNames = dao.getPersonNameByUserIds(result.getData().stream()
                .map(p -> p.getCreator()).filter(p -> p != null).distinct().collect(Collectors.toList()));
        for (StockItemDTO stockItemDTO : result.getData()) {
            if (stockItemDTO.getCreator() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockItemDTO.getCreator())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockItemDTO.setCreatorFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockItemDTO.setCreatorGivenName(userPersonNameDTO.get().getGivenName());
                }
            }
        }
        return result;
    }

    public StockItem saveStockItem(StockItem stockItem) {
        dao.saveStockItem(stockItem);
        return stockItem;
    }

    public StockItem saveStockItem(StockItem stockItem, StockItemPackagingUOM stockItemPackagingUOM) {
        dao.saveStockItem(stockItem);
        dao.saveStockItemPackagingUOM(stockItemPackagingUOM);
        return stockItem;
    }

    public StockItem saveStockItem(StockItemDTO stockItemDTO) {
        StockItem stockItem = null;
        boolean isNew = false;
        if (stockItemDTO.getUuid() == null) {
            isNew = true;
            stockItem = new StockItem();
            stockItem.setCreator(Context.getAuthenticatedUser());
            stockItem.setDateCreated(new Date());

            if (!StringUtils.isBlank(stockItemDTO.getDrugUuid())) {
                Drug drug = Context.getConceptService().getDrugByUuid(stockItemDTO.getDrugUuid());
                if (drug != null) {
                    StockItemSearchFilter filter = new StockItemSearchFilter();
                    filter.setDrugId(drug.getDrugId());
                    filter.setIsDrug(true);
                    filter.setIncludeVoided(true);
                    filter.setStartIndex(0);
                    filter.setLimit(1);
                    Result<StockItemDTO> stockItemResult = findStockItems(filter);
                    if (!stockItemResult.getData().isEmpty()) {
                        invalidRequest("stockmanagement.stockitem.drugexists");
                    }
                    stockItem.setDrug(drug);
                    stockItem.setIsDrug(true);
                    stockItem.setConcept(drug.getConcept());
                } else {
                    invalidRequest("stockmanagement.stockitem.drugnoexist");
                }
            } else if (!StringUtils.isBlank(stockItemDTO.getConceptUuid())) {
                Concept concept = Context.getConceptService().getConceptByUuid(stockItemDTO.getConceptUuid());
                if (concept != null) {
                    StockItemSearchFilter filter = new StockItemSearchFilter();
                    filter.setIsDrug(false);
                    filter.setConceptId(concept.getConceptId());
                    filter.setIncludeVoided(true);
                    filter.setStartIndex(0);
                    filter.setLimit(1);
                    Result<StockItemDTO> stockItemResult = findStockItems(filter);
                    if (!stockItemResult.getData().isEmpty()) {
                        invalidRequest("stockmanagement.stockitem.conceptexists");
                    }

                    stockItem.setIsDrug(false);
                    stockItem.setConcept(concept);
                } else {
                    invalidRequest("stockmanagement.stockitem.conceptnoexist");
                }
            } else {
                invalidRequest("stockmanagement.stockitem.drugorconceptrequired");
            }
        } else {
            stockItem = getStockItemByUuid(stockItemDTO.getUuid());
            if (stockItem == null) {
                invalidRequest("stockmanagement.stockitem.notexists");
            }
            if (stockItem.getIsDrug() == null) {
                stockItem.setIsDrug(stockItem.getDrug() != null);
            }
            stockItem.setChangedBy(Context.getAuthenticatedUser());
            stockItem.setDateChanged(new Date());
        }

        stockItem.setCommonName(stockItemDTO.getCommonName());
        stockItem.setAcronym(stockItemDTO.getAcronym());
        stockItem.setHasExpiration(stockItemDTO.getHasExpiration());
        stockItem.setExpiryNotice(stockItemDTO.getExpiryNotice());

        if (!StringUtils.isBlank(stockItemDTO.getPreferredVendorUuid())) {
            StockSource stockSource = getStockSourceByUuid(stockItemDTO.getPreferredVendorUuid());
            if (stockSource == null) {
                invalidRequest("stockmanagement.stockitem.preferredvendornoexist");
            }
            stockItem.setPreferredVendor(stockSource);
        } else {
            stockItem.setPreferredVendor(null);
        }

        if (!StringUtils.isBlank(stockItemDTO.getDispensingUnitUuid())) {
            Concept concept = Context.getConceptService().getConceptByUuid(stockItemDTO.getDispensingUnitUuid());
            if (concept != null) {
                stockItem.setDispensingUnit(concept);
            } else {
                invalidRequest("stockmanagement.stockitem.dispensingunitnoexist");
            }
        } else {
            stockItem.setDispensingUnit(null);
        }

        if (!StringUtils.isBlank(stockItemDTO.getCategoryUuid())) {
            Concept concept = Context.getConceptService().getConceptByUuid(stockItemDTO.getCategoryUuid());
            if (concept != null) {
                stockItem.setCategory(concept);
            } else {
                invalidRequest("stockmanagement.stockitem.categorynoexist");
            }
        } else {
            stockItem.setCategory(null);
        }

        if (!isNew) {

            if (!StringUtils.isBlank(stockItemDTO.getPurchasePriceUoMUuid())
                    && stockItemDTO.getPurchasePrice() != null) {
                StockItemPackagingUOM stockItemPackagingUOM = getStockItemPackagingUOMByUuid(stockItemDTO
                        .getPurchasePriceUoMUuid());
                if (stockItemPackagingUOM == null) {
                    invalidRequest("stockmanagement.stockitem.purchasepriceuomnoexist");
                } else if (!stockItemPackagingUOM.getStockItem().getId().equals(stockItem.getId())) {
                    invalidRequest("stockmanagement.stockitem.purchasepriceuomnotrelatedtostockitem");
                }
                stockItem.setPurchasePriceUoM(stockItemPackagingUOM);
                stockItem.setPurchasePrice(stockItemDTO.getPurchasePrice());
            } else {
                stockItem.setPurchasePrice(null);
                stockItem.setPurchasePriceUoM(null);
            }

            if (!StringUtils.isBlank(stockItemDTO.getReorderLevelUoMUuid()) && stockItemDTO.getReorderLevel() != null) {
                StockItemPackagingUOM stockItemPackagingUOM = getStockItemPackagingUOMByUuid(stockItemDTO
                        .getReorderLevelUoMUuid());
                if (stockItemPackagingUOM == null) {
                    invalidRequest("stockmanagement.stockitem.reorderleveluomnoexist");
                } else if (!stockItemPackagingUOM.getStockItem().getId().equals(stockItem.getId())) {
                    invalidRequest("stockmanagement.stockitem.reorderleveluomnotrelatedtostockitem");
                }
                stockItem.setReorderLevelUOM(stockItemPackagingUOM);
                stockItem.setReorderLevel(stockItemDTO.getReorderLevel());
            } else {
                stockItem.setReorderLevel(null);
                stockItem.setReorderLevelUOM(null);
            }

            if (!StringUtils.isBlank(stockItemDTO.getDispensingUnitPackagingUoMUuid())) {
                StockItemPackagingUOM stockItemPackagingUOM = getStockItemPackagingUOMByUuid(stockItemDTO
                        .getDispensingUnitPackagingUoMUuid());
                if (stockItemPackagingUOM == null) {
                    invalidRequest("stockmanagement.stockitem.dispensingunituomnoexist");
                } else if (!stockItemPackagingUOM.getStockItem().getId().equals(stockItem.getId())) {
                    invalidRequest("stockmanagement.stockitem.dispensingunituomnotrelatedtostockitem");
                }
                stockItem.setDispensingUnitPackagingUoM(stockItemPackagingUOM);
            } else {
                stockItem.setDispensingUnitPackagingUoM(null);
            }

            if (!StringUtils.isBlank(stockItemDTO.getDefaultStockOperationsUoMUuid())) {
                StockItemPackagingUOM stockItemPackagingUOM = getStockItemPackagingUOMByUuid(stockItemDTO
                        .getDefaultStockOperationsUoMUuid());
                if (stockItemPackagingUOM == null) {
                    throw new StockManagementException("stockmanagement.stockitem.defaultstockoperationsuomnoexist");
                } else if (!stockItemPackagingUOM.getStockItem().getId().equals(stockItem.getId())) {
                    invalidRequest("stockmanagement.stockitem.defaultstockoperationsuomnotrelatedtostockitem");
                }
                stockItem.setDefaultStockOperationsUoM(stockItemPackagingUOM);
            } else {
                stockItem.setDefaultStockOperationsUoM(null);
            }
        }

        dao.saveStockItem(stockItem);

        return stockItem;
    }

    public StockItemPackagingUOM getStockItemPackagingUOMByUuid(String uuid) {
        return dao.getStockItemPackagingUOMByUuid(uuid);
    }

    public StockItem getStockItemByUuid(String uuid) {
        return dao.getStockItemByUuid(uuid);
    }

    public Result<StockOperationLinkDTO> getParentStockOperationLinks(String stockOperationUuid) {
        return dao.findStockOperationLinks(null, stockOperationUuid);
    }

    public Result<StockOperationDTO> findStockOperations(StockOperationSearchFilter filter) {
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters = getRecordPrivilegeFilters(
                Context.getAuthenticatedUser(), null, null, Privileges.APP_STOCKMANAGEMENT_STOCKOPERATIONS);
        if (recordPrivilegeFilters == null || recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);

        return findStockOperations(filter, recordPrivilegeFilters);
    }

    public Result<StockOperationDTO> findStockOperations(StockOperationSearchFilter filter, HashSet<RecordPrivilegeFilter> recordPrivilegeFilters) {
        Result<StockOperationDTO> result = dao.findStockOperations(filter, recordPrivilegeFilters);
        if (result.getData().isEmpty())
            return result;

        List<Integer> userIds = new ArrayList<>();
        for (StockOperationDTO stockOperationDTO : result.getData()) {
            if (stockOperationDTO.getCreator() != null)
                userIds.add(stockOperationDTO.getCreator());
            if (stockOperationDTO.getCancelledBy() != null)
                userIds.add(stockOperationDTO.getCancelledBy());
            if (stockOperationDTO.getCompletedBy() != null)
                userIds.add(stockOperationDTO.getCompletedBy());
            if (stockOperationDTO.getResponsiblePerson() != null)
                userIds.add(stockOperationDTO.getResponsiblePerson());
            if (stockOperationDTO.getSubmittedBy() != null)
                userIds.add(stockOperationDTO.getSubmittedBy());
            if (stockOperationDTO.getReturnedBy() != null)
                userIds.add(stockOperationDTO.getReturnedBy());
            if (stockOperationDTO.getRejectedBy() != null)
                userIds.add(stockOperationDTO.getRejectedBy());
            if (stockOperationDTO.getDispatchedBy() != null)
                userIds.add(stockOperationDTO.getDispatchedBy());
        }

        List<UserPersonNameDTO> personNames = dao
                .getPersonNameByUserIds(userIds.stream().distinct().collect(Collectors.toList()));
        for (StockOperationDTO stockOperationDTO : result.getData()) {
            if (stockOperationDTO.getCreator() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getCreator())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setCreatorFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setCreatorGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getCancelledBy() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getCancelledBy())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setCancelledByFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setCancelledByGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getCompletedBy() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getCompletedBy())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setCompletedByFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setCompletedByGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getResponsiblePerson() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getResponsiblePerson())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setResponsiblePersonFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setResponsiblePersonGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getSubmittedBy() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getSubmittedBy())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setSubmittedByFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setSubmittedByGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getDispatchedBy() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getDispatchedBy())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setDispatchedByFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setDispatchedByGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getReturnedBy() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getReturnedBy())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setReturnedByFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setReturnedByGivenName(userPersonNameDTO.get().getGivenName());
                }
            }

            if (stockOperationDTO.getRejectedBy() != null) {
                Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                        .filter(p -> p.getUserId().equals(stockOperationDTO.getRejectedBy())).findFirst();
                if (userPersonNameDTO.isPresent()) {
                    stockOperationDTO.setRejectedByFamilyName(userPersonNameDTO.get().getFamilyName());
                    stockOperationDTO.setRejectedByGivenName(userPersonNameDTO.get().getGivenName());
                }
            }
        }
        return result;
    }

    public StockSource getStockSourceByUuid(String uuid) {
        return dao.getStockSourceByUuid(uuid);
    }

    public StockSource saveStockSource(StockSource stockSource) {
        if (stockSource.getId() != null && stockSource.getId() > 0) {
            stockSource.setChangedBy(Context.getAuthenticatedUser());
            stockSource.setDateChanged(new Date());
        }
        StockSource result = dao.saveStockSource(stockSource);
        Party party = dao.getPartyByStockSource(result);
        if (party == null) {
            party = new Party();
            party.setStockSource(result);
            party.setCreator(Context.getAuthenticatedUser());
            party.setDateCreated(new Date());
            dao.saveParty(party);
        }
        return result;
    }

    public Result<StockOperationLinkDTO> findStockOperationLinks(String stockOperationUuid) {
        return dao.findStockOperationLinks(stockOperationUuid);
    }

    public Result<StockSource> findStockSources(StockSourceSearchFilter filter) {
        return dao.findStockSources(filter);
    }

    public void voidStockSources(List<String> stockSourceIds, String reason, int voidedBy) {
        dao.voidStockSources(stockSourceIds, reason, voidedBy);
    }

    public Party getPartyByStockSource(StockSource stockSource) {
        return dao.getPartyByStockSource(stockSource);
    }

    public Party getPartyByLocation(Location location) {
        return dao.getPartyByLocation(location);
    }

    public List<Party> getPartyListByLocations(Collection<Location> locations) {
        return dao.getPartyListByLocations(locations);
    }

    public Party saveParty(Party party) {
        if (party.getId() != null && party.getId() > 0) {
            party.setChangedBy(Context.getAuthenticatedUser());
            party.setDateChanged(new Date());
        }
        return dao.saveParty(party);
    }

    public Result<PartyDTO> findParty(PartySearchFilter filter) {
        return dao.findParty(filter);
    }

    public List<Party> findParty(Boolean hasLocation, Boolean hasStockSource) {
        return dao.findParty(hasLocation, hasStockSource);
    }

    public Party getPartyByUuid(String uuid) {
        return dao.getPartyByUuid(uuid);
    }

    public boolean userHasStockManagementPrivilege(User user, Location location, StockOperationType stockOperationType,
                                                   String stockManagementPrivilege) {
        return !getPrivilegeScopes(user, location, stockOperationType, stockManagementPrivilege).isEmpty();
    }

    public HashSet<RecordPrivilegeFilter> getRecordPrivilegeFilters(User user, Location location,
                                                                    StockOperationType stockOperationType, String stockManagementPrivilege) {
        HashSet<RecordPrivilegeFilter> target = new HashSet<>();
        List<Location> allLocations = Context.getLocationService().getAllLocations(true);
        HashMap<String, Integer> operationTypes = new HashMap<>();
        Set<Role> allRoles = user.getAllRoles();
        List<PrivilegeScope> privilegeScopes = dao.getFlattenedUserRoleScopesByUser(user, allRoles, location,
                stockOperationType);
        Map<String, List<PrivilegeScope>> roleGroups = privilegeScopes.stream()
                .collect(Collectors.groupingBy(PrivilegeScope::getRole));
        for (Map.Entry<String, List<PrivilegeScope>> entry : roleGroups.entrySet()) {
            Optional<Role> role = allRoles.stream().filter(p -> p.getRole().equals(entry.getKey())).findFirst();
            if (!role.isPresent()) {
                continue;
            }
            boolean hasPrivilege = Stream.concat(role.get().getPrivileges().stream(),
                    role.get().getAllParentRoles().stream()
                            .map(p -> p.getPrivileges())
                            .flatMap(Collection::stream))
                    .map(p -> p.getPrivilege())
                    .filter(p -> (stockManagementPrivilege == null
                            || p.equals(stockManagementPrivilege) && Privileges.ALL.contains(p)))
                    .count() > 0;
            if (!hasPrivilege)
                continue;

            for (PrivilegeScope scope : entry.getValue()) {
                RecordPrivilegeFilter recordPrivilegeFilter = new RecordPrivilegeFilter();
                Optional<Location> scopeLocation = allLocations.stream()
                        .filter(p -> p.getUuid().equals(scope.getLocationUuid())).findFirst();
                if (!scopeLocation.isPresent())
                    continue;
                recordPrivilegeFilter.setLocationId(scopeLocation.get().getId());
                if (!operationTypes.containsKey(scope.getOperationTypeUuid())) {
                    StockOperationType stockOperationTypeEntity = getStockOperationTypeByUuid(
                            scope.getOperationTypeUuid());
                    if (stockOperationTypeEntity != null) {
                        recordPrivilegeFilter.setOperationTypeId(stockOperationTypeEntity.getId());
                        operationTypes.putIfAbsent(scope.getOperationTypeUuid(), stockOperationTypeEntity.getId());
                    } else {
                        continue;
                    }
                } else {
                    recordPrivilegeFilter.setOperationTypeId(operationTypes.get(scope.getOperationTypeUuid()));
                }
                target.add(recordPrivilegeFilter);
            }
        }
        return target;
    }

    public HashSet<PrivilegeScope> getPrivilegeScopes(User user, Location location,
                                                      StockOperationType stockOperationType,
                                                      String stockManagementPrivilege) {
        return getPrivilegeScopes(user, location, stockOperationType, stockManagementPrivilege == null ? null
                : Arrays.asList(stockManagementPrivilege));
    }

    public HashSet<PrivilegeScope> getPrivilegeScopes(User user, Location location,
                                                      StockOperationType stockOperationType, List<String> stockManagementPrivileges) {
        HashSet<PrivilegeScope> target = new HashSet<>();
        Set<Role> allRoles = user.getAllRoles();
        List<PrivilegeScope> privilegeScopes = dao.getFlattenedUserRoleScopesByUser(user, allRoles, location,
                stockOperationType);
        Map<String, List<PrivilegeScope>> roleGroups = privilegeScopes.stream()
                .collect(Collectors.groupingBy(PrivilegeScope::getRole));
        for (Map.Entry<String, List<PrivilegeScope>> entry : roleGroups.entrySet()) {
            Optional<Role> role = allRoles.stream().filter(p -> p.getRole().equals(entry.getKey())).findFirst();
            if (!role.isPresent()) {
                continue;
            }
            List<String> allRolePrivileges = Stream.concat(role.get().getPrivileges().stream(),
                    role.get().getAllParentRoles().stream()
                            .map(p -> p.getPrivileges())
                            .flatMap(Collection::stream))
                    .map(p -> p.getPrivilege())
                    .filter(p -> (stockManagementPrivileges == null
                            || stockManagementPrivileges.contains(p) && Privileges.ALL.contains(p)))
                    .distinct()
                    .collect(Collectors.toList());

            for (PrivilegeScope scope : entry.getValue()) {
                for (String privilege : allRolePrivileges) {
                    PrivilegeScope newScope = scope.clone();
                    scope.setRole(null);
                    newScope.setPrivilege(privilege);
                    target.add(newScope);
                }
            }
        }
        return target;
    }

    public SessionInfo getCurrentUserSessionInfo() {
        SessionInfo sessionInfo = new SessionInfo();
        User currentUser = Context.getAuthenticatedUser();
        sessionInfo.setPrivileges(getPrivilegeScopes(currentUser, null, null, (String) null));
        return sessionInfo;
    }

    public List<PartyDTO> getAllParties() {
        List<PartyDTO> partyDTOs = dao.getAllParties();
        List<Location> locations = Context.getLocationService().getAllLocations();
        for (PartyDTO partyDTO : partyDTOs) {
            partyDTO.setTags(locations.stream().filter(p -> p.getUuid().equals(partyDTO.getLocationUuid()))
                    .map(p -> p.getTags())
                    .flatMap(Collection::stream)
                    .map(p -> p.getName())
                    .collect(Collectors.toList()));
        }
        return partyDTOs;
    }

    private void invalidRequest(String messageKey) {
        throw new StockManagementException(Context.getMessageSourceService().getMessage(messageKey));
    }

    private void invalidRequestWithKey(String messageKey, String... args){
        throw new StockManagementException(String.format(Context.getMessageSourceService().getMessage(messageKey), args));
    }

    private void invalidRequest(String message, String... args) {
        throw new StockManagementException(String.format(message, args));
    }

    public UserRoleScope saveUserRoleScope(UserRoleScopeDTO delegate) {
        UserRoleScope userRoleScope = null;
        if (StringUtils.isNotBlank(delegate.getUuid())) {
            userRoleScope = getUserRoleScopeByUuid(delegate.getUuid());
            if (userRoleScope == null)
                throw new APIException("User role scope " + delegate.getUuid() + " not found");
            if (userRoleScope.getUser().getUuid().equalsIgnoreCase(Context.getAuthenticatedUser().getUuid())) {
                invalidRequest("stockmanagement.userrolescopes.userUuid.selfupdate");
            }
        } else {
            userRoleScope = new UserRoleScope();
            User user = Context.getUserService().getUserByUuid(delegate.getUserUuid());
            if (user == null) {
                invalidRequest("stockmanagement.userrolescopes.user.notfound");
            }
            userRoleScope.setUser(user);
            if (userRoleScope.getUser().getUuid().equalsIgnoreCase(Context.getAuthenticatedUser().getUuid())) {
                invalidRequest("stockmanagement.userrolescopes.userUuid.selfupdate");
            }
        }
        Role role = Context.getUserService().getRole(delegate.getRole());
        if (role == null) {
            invalidRequest("stockmanagement.userrolescopes.role.notfound");
        }

        userRoleScope.setCreator(Context.getAuthenticatedUser());
        userRoleScope.setDateCreated(new Date());
        userRoleScope.setRole(role);
        userRoleScope.setPermanent(delegate.getPermanent());
        userRoleScope.setActiveFrom(delegate.getActiveFrom());
        userRoleScope.setActiveTo(delegate.getActiveTo());
        userRoleScope.setEnabled(delegate.getEnabled());

        LocationService locationService = Context.getLocationService();
        List<UserRoleScopeLocation> locationsToAdd = new ArrayList<>();
        for (UserRoleScopeLocationDTO userRoleScopeLocationDTO : delegate.getLocations()) {
            Location location = locationService.getLocationByUuid(userRoleScopeLocationDTO.getLocationUuid());
            if (location == null) {
                invalidRequest("stockmanagement.userrolescopes.location.invalid", userRoleScopeLocationDTO.getUuid());
            } else {
                Optional<UserRoleScopeLocation> exisitingUserRoleScope = userRoleScope
                        .getUserRoleScopeLocations() != null ? userRoleScope.getUserRoleScopeLocations()
                        .stream()
                        .filter(p -> !p.getVoided() && p.getLocation().getUuid()
                                .equalsIgnoreCase(userRoleScopeLocationDTO.getLocationUuid()))
                        .findAny() : Optional.<UserRoleScopeLocation>empty();
                if (exisitingUserRoleScope.isPresent()) {
                    if ((userRoleScopeLocationDTO.getEnableDescendants() == null &&
                            exisitingUserRoleScope.get().getEnableDescendants())
                            || (userRoleScopeLocationDTO.getEnableDescendants() != null
                            && exisitingUserRoleScope.get().getEnableDescendants() != userRoleScopeLocationDTO
                            .getEnableDescendants().booleanValue())) {
                        exisitingUserRoleScope.get()
                                .setEnableDescendants(userRoleScopeLocationDTO.getEnableDescendants() == null ? false
                                        : userRoleScopeLocationDTO.getEnableDescendants());
                        saveUserRoleScopeLocation(exisitingUserRoleScope.get());
                    }
                    continue;
                }
                UserRoleScopeLocation userRoleScopeLocation = new UserRoleScopeLocation();
                userRoleScopeLocation.setLocation(location);
                userRoleScopeLocation.setEnableDescendants(userRoleScopeLocationDTO.getEnableDescendants());
                userRoleScopeLocation.setCreator(Context.getAuthenticatedUser());
                userRoleScopeLocation.setDateCreated(new Date());
                locationsToAdd.add(userRoleScopeLocation);
            }
        }

        List<UserRoleScopeLocation> locationsToRemove = userRoleScope.getUserRoleScopeLocations() == null
                ? new ArrayList<>()
                : userRoleScope.getUserRoleScopeLocations()
                .stream()
                .filter(p -> !p.getVoided() && delegate.getLocations() != null
                        && !delegate.getLocations().stream()
                        .anyMatch(x -> x.getLocationUuid().equalsIgnoreCase(p.getLocation().getUuid())))
                .collect(Collectors.toList());

        List<UserRoleScopeOperationType> stockOperationTypesToAdd = new ArrayList<>();
        List<StockOperationType> allStockOperationTypes = getAllStockOperationTypes();
        for (UserRoleScopeOperationTypeDTO userRoleScopeOperationTypeDTO : delegate.getOperationTypes()) {
            Optional<StockOperationType> stockOperationType = allStockOperationTypes.stream()
                    .filter(p -> p.getUuid().equalsIgnoreCase(userRoleScopeOperationTypeDTO.getOperationTypeUuid()))
                    .findFirst();
            if (!stockOperationType.isPresent()) {
                invalidRequest(
                        Context.getMessageSourceService()
                                .getMessage("stockmanagement.userrolescopes.operationtype.invalid"),
                        userRoleScopeOperationTypeDTO.getOperationTypeUuid());
            } else {
                Optional<UserRoleScopeOperationType> exisitingOperationType = userRoleScope
                        .getUserRoleScopeOperationTypes() != null ? userRoleScope.getUserRoleScopeOperationTypes()
                        .stream()
                        .filter(p -> !p.getVoided() && p.getStockOperationType().getUuid()
                                .equalsIgnoreCase(userRoleScopeOperationTypeDTO.getOperationTypeUuid()))
                        .findAny() : Optional.<UserRoleScopeOperationType>empty();
                if (exisitingOperationType.isPresent())
                    continue;
                UserRoleScopeOperationType userRoleScopeOperationType = new UserRoleScopeOperationType();
                userRoleScopeOperationType.setStockOperationType(stockOperationType.get());
                userRoleScopeOperationType.setCreator(Context.getAuthenticatedUser());
                userRoleScopeOperationType.setDateCreated(new Date());
                stockOperationTypesToAdd.add(userRoleScopeOperationType);
            }
        }

        List<UserRoleScopeOperationType> stockOperationTypesToRemove = userRoleScope
                .getUserRoleScopeOperationTypes() == null
                ? new ArrayList<>()
                : userRoleScope.getUserRoleScopeOperationTypes()
                .stream()
                .filter(p -> !p.getVoided() && delegate.getOperationTypes() != null
                        && !delegate.getOperationTypes().stream()
                        .anyMatch(x -> x.getOperationTypeUuid()
                                .equalsIgnoreCase(p.getStockOperationType().getUuid())))
                .collect(Collectors.toList());

        saveUserRoleScope(userRoleScope);

        if (!locationsToRemove.isEmpty())
            voidUserRoleScopeLocations(locationsToRemove.stream().map(p -> p.getUuid()).collect(Collectors.toList()),
                    null, Context.getAuthenticatedUser().getUserId());

        if (!stockOperationTypesToRemove.isEmpty())
            voidUserRoleScopeOperationTypes(
                    stockOperationTypesToRemove.stream().map(p -> p.getUuid()).collect(Collectors.toList()), null,
                    Context.getAuthenticatedUser().getUserId());

        for (UserRoleScopeLocation location : locationsToAdd) {
            location.setUserRoleScope(userRoleScope);
            saveUserRoleScopeLocation(location);
        }

        for (UserRoleScopeOperationType scopeOperationType : stockOperationTypesToAdd) {
            scopeOperationType.setUserRoleScope(userRoleScope);
            saveUserRoleScopeOperationType(scopeOperationType);
        }

        return userRoleScope;
    }

    public Result<StockItemPackagingUOMDTO> findStockItemPackagingUOMs(StockItemPackagingUOMSearchFilter filter) {
        return dao.findStockItemPackagingUOMs(filter);
    }

    public StockItemPackagingUOM saveStockItemPackagingUOM(StockItemPackagingUOM stockItemPackagingUOM) {
        dao.saveStockItemPackagingUOM(stockItemPackagingUOM);
        return stockItemPackagingUOM;
    }

    public StockItemPackagingUOM saveStockItemPackagingUOM(StockItemPackagingUOMDTO stockItemPackagingUOMDTO) {
        StockItemPackagingUOM stockItemPackagingUOM = null;
        if (stockItemPackagingUOMDTO.getUuid() != null) {
            stockItemPackagingUOM = getStockItemPackagingUOMByUuid(stockItemPackagingUOMDTO.getUuid());
            if (stockItemPackagingUOM == null) {
                invalidRequest("stockmanagement.stockitempackagingunit.notexists");
            }
            stockItemPackagingUOM.setChangedBy(Context.getAuthenticatedUser());
            stockItemPackagingUOM.setDateChanged(new Date());
        } else {
            stockItemPackagingUOM = new StockItemPackagingUOM();
            stockItemPackagingUOM.setCreator(Context.getAuthenticatedUser());
            stockItemPackagingUOM.setDateCreated(new Date());
            StockItem stockItem = getStockItemByUuid(stockItemPackagingUOMDTO.getStockItemUuid());
            if (stockItem == null) {
                invalidRequest("stockmanagement.stockitem.notexists");
            }
            stockItemPackagingUOM.setStockItem(stockItem);
        }

        Concept concept = Context.getConceptService().getConceptByUuid(stockItemPackagingUOMDTO.getPackagingUomUuid());
        if (concept == null) {
            invalidRequest("stockmanagement.stockitempackagingunit.packaginguomnotexist");
        }
        stockItemPackagingUOM.setPackagingUom(concept);
        stockItemPackagingUOM.setFactor(stockItemPackagingUOMDTO.getFactor());

        return dao.saveStockItemPackagingUOM(stockItemPackagingUOM);

    }

    public List<StockOperationItem> getStockOperationItemsByStockOperation(Integer stockOperationId) {
        return dao.getStockOperationItemsByStockOperation(stockOperationId);
    }

    public Result<StockOperationItemDTO> findStockOperationItems(StockOperationItemSearchFilter filter) {
        return dao.findStockOperationItems(filter);
    }

    public Result<StockOperationItemCost> getStockOperationItemCosts(StockOperationItemSearchFilter filter) {
        Result<StockOperationItemDTO> stockOperationItems = findStockOperationItems(filter);
        if (stockOperationItems.getData().isEmpty()) {
            return new Result<>(new ArrayList<>(), 0);
        }

        StockItemSearchFilter stockItemSearchFilter = new StockItemSearchFilter();
        stockItemSearchFilter.setStockItemIds(stockOperationItems.getData().stream().map(p -> p.getStockItemId())
                .distinct().collect(Collectors.toList()));
        stockItemSearchFilter.setIncludeVoided(true);
        Map<Integer, List<StockItemDTO>> stockItems = findStockItems(stockItemSearchFilter).getData().stream()
                .collect(Collectors.groupingBy(StockItemDTO::getId));
        if (stockItems.isEmpty()) {
            return new Result<>(new ArrayList<>(), 0);
        }
        StockItemPackagingUOMSearchFilter uomFilter = new StockItemPackagingUOMSearchFilter();
        uomFilter.setStockItemIds(stockOperationItems.getData().stream().map(p -> p.getStockItemId())
                .filter(p -> p != null).collect(Collectors.toList()));
        uomFilter.setIncludeVoided(true);
        Map<Integer, List<StockItemPackagingUOMDTO>> uoms = findStockItemPackagingUOMs(uomFilter).getData().stream()
                .collect(Collectors.groupingBy(StockItemPackagingUOMDTO::getStockItemId));

        List<StockOperationItemCost> stockOperationItemCosts = new ArrayList<>();
        for (StockOperationItemDTO stockOperationItem : stockOperationItems.getData()) {
            StockOperationItemCost itemCost = new StockOperationItemCost();
            itemCost.setUuid(stockOperationItem.getUuid());
            itemCost.setStockItemId(stockOperationItem.getStockItemId());
            itemCost.setBatchNo(stockOperationItem.getBatchNo());
            itemCost.setStockBatchId(stockOperationItem.getStockBatchId());
            itemCost.setId(stockOperationItem.getId());
            itemCost.setPackagingUoMId(stockOperationItem.getPackagingUoMId());
            itemCost.setStockItemPackagingUOMName(stockOperationItem.getStockItemPackagingUOMName());
            itemCost.setStockItemPackagingUOMUuid(stockOperationItem.getStockItemPackagingUOMUuid());
            itemCost.setQuantity(stockOperationItem.getQuantity());
            stockOperationItemCosts.add(itemCost);

            List<StockItemDTO> stockItemDtos = stockItems.getOrDefault(stockOperationItem.getStockItemId(), null);
            if (stockItemDtos == null) {
                continue;
            }
            StockItemDTO stockItem = stockItemDtos.get(0);
            if (stockItem.getPurchasePrice() == null || stockItem.getPurchasePriceUoMId() == null) {
                continue;
            }

            // If for some reason we don't have the packaging unit of measure, we can only
            // set the purchase price but no total cost since we cant do conversion
            if (itemCost.getPackagingUoMId() == null || itemCost.getQuantity() == null) {
                itemCost.setUnitCost(stockItem.getPurchasePrice());
                itemCost.setUnitCostUOMId(stockItem.getPurchasePriceUoMId());
                itemCost.setUnitCostUOMName(stockItem.getPurchasePriceUoMName());
                itemCost.setUnitCostUOMUuid(stockItem.getPurchasePriceUoMUuid());
                continue;
            }

            // No need for conversion if the packaging unit is same as purchase price
            if (itemCost.getPackagingUoMId().equals(stockItem.getPurchasePriceUoMId())) {
                itemCost.setUnitCost(stockItem.getPurchasePrice());
                itemCost.setUnitCostUOMId(stockItem.getPurchasePriceUoMId());
                itemCost.setUnitCostUOMName(stockItem.getPurchasePriceUoMName());
                itemCost.setUnitCostUOMUuid(stockItem.getPurchasePriceUoMUuid());
                if (itemCost.getQuantity() != null) {
                    itemCost.setTotalCost(itemCost.getQuantity().multiply(stockItem.getPurchasePrice()));
                }
                continue;
            }

            // convert the purchase price to the packaging uom
            List<StockItemPackagingUOMDTO> stockItemUoms = uoms.getOrDefault(itemCost.getStockItemId(), null);
            if (stockItemUoms == null) {
                itemCost.setUnitCost(stockItem.getPurchasePrice());
                itemCost.setUnitCostUOMId(stockItem.getPurchasePriceUoMId());
                itemCost.setUnitCostUOMName(stockItem.getPurchasePriceUoMName());
                itemCost.setUnitCostUOMUuid(stockItem.getPurchasePriceUoMUuid());
                continue;
            }

            // if unable to find the purchase price, set only unit cost and move one
            Optional<StockItemPackagingUOMDTO> purchasePricePackagingUoMDTO = stockItemUoms.stream()
                    .filter(p -> p.getUuid().equals(stockItem.getPurchasePriceUoMUuid())).findFirst();
            Optional<StockItemPackagingUOMDTO> quantityUoMDTO = stockItemUoms.stream()
                    .filter(p -> p.getUuid().equals(stockOperationItem.getStockItemPackagingUOMUuid())).findFirst();
            if (!purchasePricePackagingUoMDTO.isPresent() || !quantityUoMDTO.isPresent()) {
                itemCost.setUnitCost(stockItem.getPurchasePrice());
                itemCost.setUnitCostUOMId(stockItem.getPurchasePriceUoMId());
                itemCost.setUnitCostUOMName(stockItem.getPurchasePriceUoMName());
                itemCost.setUnitCostUOMUuid(stockItem.getPurchasePriceUoMUuid());
                continue;
            }

            // Unit price of the smallest factor without UOM
            BigDecimal purchasePricePerUnitBaseValue = stockItem.getPurchasePrice()
                    .divide(purchasePricePackagingUoMDTO.get().getFactor(), 5, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal packagingUnitPrice = purchasePricePerUnitBaseValue.multiply(quantityUoMDTO.get().getFactor())
                    .setScale(2, BigDecimal.ROUND_HALF_EVEN);
            itemCost.setTotalCost(packagingUnitPrice.multiply(itemCost.getQuantity()));

            itemCost.setUnitCost(packagingUnitPrice);
            itemCost.setUnitCostUOMId(quantityUoMDTO.get().getPackagingUomId());
            itemCost.setUnitCostUOMName(quantityUoMDTO.get().getPackagingUomName());
            itemCost.setUnitCostUOMUuid(quantityUoMDTO.get().getPackagingUomUuid());
        }
        Result<StockOperationItemCost> result = new Result<>(stockOperationItemCosts, 0);
        stockOperationItems.copyPagingInfoTo(result);
        return result;
    }

    public StockOperation saveStockOperation(StockOperationDTO dto) {
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        StockOperation stockOperation = null;
        boolean isNew = true;
        StockOperationType stockOperationType = null;
        if (!StringUtils.isBlank(dto.getUuid())) {
            stockOperation = dao.getStockOperationByUuid(dto.getUuid());
            if (stockOperation == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.stockoperation.notfound"));
            }
            isNew = false;
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());
            stockOperationType = stockOperation.getStockOperationType();
        } else {
            stockOperation = new StockOperation();
            stockOperation.setOperationOrder(1);
            stockOperation.setStatus(StockOperationStatus.NEW);
            stockOperation.setCreator(Context.getAuthenticatedUser());
            stockOperation.setDateCreated(new Date());
            stockOperationType = getStockOperationTypeByUuid(dto.getOperationTypeUuid());
            stockOperation.setStockOperationType(stockOperationType);
            stockOperation.setLocked(false);
        }

        stockOperation.setApprovalRequired(dto.getApprovalRequired());
        stockOperation.setOperationDate(dto.getOperationDate());
        stockOperation.setExternalReference(dto.getExternalReference());
        stockOperation.setRemarks(StringUtils.isBlank(dto.getRemarks()) ? null : dto.getRemarks());

        if (!StringUtils.isBlank(dto.getReasonUuid())) {
            Concept concept = Context.getConceptService().getConceptByUuid(dto.getReasonUuid());
            if (concept == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.stockoperation.notfound"));
            }
            stockOperation.setReason(concept);
        } else {
            stockOperation.setReason(null);
        }

        stockOperation.setAtLocation(Context.getLocationService().getLocationByUuid(dto.getAtLocationUuid()));

        if (!StringUtils.isBlank(dto.getSourceUuid())) {
            stockOperation.setSource(dao.getPartyByUuid(dto.getSourceUuid()));
        }

        if (!StringUtils.isBlank(dto.getDestinationUuid())) {
            stockOperation.setDestination(dao.getPartyByUuid(dto.getDestinationUuid()));
        }

        if (!StringUtils.isBlank(dto.getResponsiblePersonUuid())) {
            User user = Context.getUserService().getUserByUuid(dto.getResponsiblePersonUuid());
            if (user == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.stockoperation.responsiblepersonnotfound"));
            }
            stockOperation.setResponsiblePerson(user);
        } else if (!StringUtils.isBlank(dto.getResponsiblePersonOther())) {
            stockOperation.setResponsiblePersonOther(dto.getResponsiblePersonOther());
        }

        StockOperationLink stockOperationLink = null;
        if (isNew) {
            if(stockOperationType.canBeRelatedToRequisition() && !StringUtils.isBlank(dto.getRequisitionStockOperationUuid())){
                StockOperation parentStockOperation = getStockOperationByUuid(dto.getRequisitionStockOperationUuid());
                if (parentStockOperation != null) {
                    stockOperationLink = new StockOperationLink();
                    stockOperationLink.setParent(parentStockOperation);
                    stockOperationLink.setCreator(Context.getAuthenticatedUser());
                    stockOperationLink.setDateCreated(new Date());
                } else {
                    throw new StockManagementException(messageSourceService.getMessage("stockmanagement.stockoperation.requisitionstocksperationnnotfound"));
                }
            }else if(StockOperationType.STOCK_ISSUE.equals(stockOperationType.getOperationType()) && !GlobalProperties.allowStockIssueWithoutRequisition()){
                throw new StockManagementException(messageSourceService.getMessage("stockmanagement.stockoperation.requisitionstocksperationnrequired"));
            }
        }



        List<StockBatch> newStockBatches = new ArrayList<>();
        List<StockOperationItem> newStockOperationItems = new ArrayList<>();
        HashMap<String, StockBatch> stockBatchMapping = new HashMap<>();
        List<String> preloadPackagingUomIds = dto.getStockOperationItems().stream()
                .filter(p -> !StringUtils.isBlank(p.getStockItemPackagingUOMUuid()))
                .map(p -> p.getStockItemPackagingUOMUuid()).collect(Collectors.toList());
        preloadPackagingUomIds.addAll(dto.getStockOperationItems().stream()
                .filter(p -> !StringUtils.isBlank(p.getQuantityReceivedPackagingUOMUuid()))
                .map(p -> p.getQuantityReceivedPackagingUOMUuid()).collect(Collectors.toList()));
        preloadPackagingUomIds.addAll(dto.getStockOperationItems().stream()
                .filter(p -> !StringUtils.isBlank(p.getQuantityRequestedPackagingUOMUuid()))
                .map(p -> p.getQuantityRequestedPackagingUOMUuid()).collect(Collectors.toList()));
        List<StockItemPackagingUOM> preloadStockItemPackagingUOMs = dao
                .getStockItemPackagingUOMsByUuids(preloadPackagingUomIds);

        List<StockItem> preloadStockItems = dao.getStockItemsByUuids(
                dto.getStockOperationItems().stream().filter(p -> !StringUtils.isBlank(p.getStockItemUuid()))
                        .map(p -> p.getStockItemUuid()).collect(Collectors.toList()));

        List<StockOperationItem> stockOperationItems = isNew ? new ArrayList<>()
                : getStockOperationItemsByStockOperation(stockOperation.getId());
        for (StockOperationItemDTO itemDto : dto.getStockOperationItems()) {
            StockOperationItem item = null;
            boolean isStockItemNew = isNew || StringUtils.isBlank(itemDto.getUuid());
            if (isStockItemNew) {
                item = new StockOperationItem();
                item.setCreator(Context.getAuthenticatedUser());
                item.setDateCreated(new Date());
            } else {
                Optional<StockOperationItem> existingItem = stockOperationItems.stream()
                        .filter(p -> p.getUuid().equals(itemDto.getUuid())).findFirst();
                if (!existingItem.isPresent()) {
                    throw new StockManagementException(String.format(
                            messageSourceService.getMessage("stockmanagement.stockoperation.itemnotfound"),
                            itemDto.getUuid()));
                }
                item = existingItem.get();
                item.setChangedBy(Context.getAuthenticatedUser());
                item.setDateChanged(new Date());
            }

            if (itemDto.getQuantity() != null) {
                item.setQuantity(itemDto.getQuantity());
                Optional<StockItemPackagingUOM> stockItemPackagingUOMOptional = preloadStockItemPackagingUOMs.stream()
                        .filter(p -> p.getUuid().equalsIgnoreCase(itemDto.getStockItemPackagingUOMUuid())).findFirst();
                if (!stockItemPackagingUOMOptional.isPresent()) {
                    throw new StockManagementException(String.format(
                            messageSourceService.getMessage("stockmanagement.stockoperation.stockitemuomnotfound"),
                            itemDto.getStockItemPackagingUOMUuid()));
                }
                item.setStockItemPackagingUOM(stockItemPackagingUOMOptional.get());
            }

            if (itemDto.getPurchasePrice() != null) {
                item.setPurchasePrice(itemDto.getPurchasePrice());
            }

            Optional<StockItem> stockItemOptional = preloadStockItems.stream()
                    .filter(p -> p.getUuid().equalsIgnoreCase(itemDto.getStockItemUuid())).findFirst();
            if (!stockItemOptional.isPresent()) {
                throw new StockManagementException(String.format(
                        messageSourceService.getMessage("stockmanagement.stockoperation.stockitemnotfound"),
                        itemDto.getStockItemUuid()));
            }
            StockItem stockItem = stockItemOptional.get();
            if (isStockItemNew) {
                item.setStockItem(stockItem);
            }
            if (stockOperationType.requiresBatchUuid()) {
                StockBatch stockBatch = getStockBatchByUuid(itemDto.getStockBatchUuid());
                if (stockBatch == null) {
                    throw new StockManagementException(String.format(
                            messageSourceService.getMessage("stockmanagement.stockoperation.stockbatchnotfound"),
                            itemDto.getStockBatchUuid()));
                }
                item.setStockBatch(stockBatch);
            } else if (stockOperationType.requiresActualBatchInformation()) {
                {
                    StockBatch stockBatch = findStockBatch(stockItem, itemDto.getBatchNo(), itemDto.getExpiration());
                    if (stockBatch == null) {
                        Optional<StockBatch> newlyAddedStockBatch = newStockBatches
                                .stream().filter(p -> p.getStockItem().getId().equals(stockItem.getId()) &&
                                        itemDto.getBatchNo().equalsIgnoreCase(p.getBatchNo()) &&
                                        ((stockItem.getHasExpiration() && p.getExpiration().equals(p.getExpiration()))
                                                ||
                                                (!stockItem.getHasExpiration() && p.getExpiration() == null)))
                                .findFirst();
                        if (newlyAddedStockBatch.isPresent()) {
                            stockBatchMapping.putIfAbsent(item.getUuid(), newlyAddedStockBatch.get());
                        } else {
                            stockBatch = new StockBatch();
                            newStockBatches.add(stockBatch);
                            stockBatch.setStockItem(stockItem);
                            stockBatch.setBatchNo(itemDto.getBatchNo());
                            if (stockItem.getHasExpiration()) {
                                stockBatch.setExpiration(itemDto.getExpiration());
                            }
                            stockBatch.setCreator(Context.getAuthenticatedUser());
                            stockBatch.setDateCreated(new Date());
                            stockBatchMapping.putIfAbsent(item.getUuid(), stockBatch);
                        }
                    }else{
                        stockBatchMapping.putIfAbsent(item.getUuid(), stockBatch);
                    }
                }
                if (stockOperationType.canCapturePurchasePrice()) {
                    item.setPurchasePrice(itemDto.getPurchasePrice());
                }
            }

            if (isStockItemNew && stockOperationType.getOperationType().equals(StockOperationType.STOCK_ISSUE)) {
                if (itemDto.getQuantityRequested() != null) {
                    item.setQuantityRequested(itemDto.getQuantityRequested());
                    Optional<StockItemPackagingUOM> stockItemPackagingUOMOptional = preloadStockItemPackagingUOMs
                            .stream()
                            .filter(p -> p.getUuid().equalsIgnoreCase(itemDto.getQuantityRequestedPackagingUOMUuid()))
                            .findFirst();
                    if (!stockItemPackagingUOMOptional.isPresent()) {
                        throw new StockManagementException(String.format(
                                messageSourceService
                                        .getMessage("stockmanagement.stockoperation.qtyrequesteduomnotexist"),
                                itemDto.getQuantityRequestedPackagingUOMUuid()));
                    }
                    item.setQuantityRequestedPackagingUOM(stockItemPackagingUOMOptional.get());
                }

            }
            newStockOperationItems.add(item);
        }

        for (StockBatch stockBatch : newStockBatches) {
            dao.saveStockBatch(stockBatch);
        }

        dao.saveStockOperation(stockOperation);
        if (isNew) {
            stockOperation.setOperationNumber(String.format("%1s-%2s", stockOperationType.getAcronym(),
                    StringUtils.leftPad(Integer.toString(stockOperation.getId()), 4, '0')));
            dao.saveStockOperation(stockOperation);
        }

        for (StockOperationItem item : newStockOperationItems) {
            item.setStockOperation(stockOperation);
            if (stockBatchMapping.containsKey(item.getUuid()))
                item.setStockBatch(stockBatchMapping.get(item.getUuid()));
            dao.saveStockOperationItem(item);
        }

        if (stockOperationLink != null) {
            stockOperationLink.setChild(stockOperation);
            dao.saveStockOperationLink(stockOperationLink);
        }

        return stockOperation;
    }

    public StockBatch getStockBatchByUuid(String uuid) {
        return dao.getStockBatchByUuid(uuid);
    }

    public StockBatch findStockBatch(StockItem stockItem, String batchNo, Date expiration) {
        return dao.findStockBatch(stockItem, batchNo, expiration);
    }

    public Result<StockBatchDTO> findStockBatches(StockBatchSearchFilter filter) {
        Result<StockBatchDTO> stockBatchDTOResult = dao.findStockBatches(filter);
        if (!stockBatchDTOResult.getData().isEmpty() && !StringUtils.isBlank(filter.getLocationUuid())) {
            Location location = Context.getLocationService().getLocationByUuid(filter.getLocationUuid());
            if (location == null)
                return new Result<>(new ArrayList<>(), 0);

            Party party = getPartyByLocation(location);
            if (party == null)
                return new Result<>(new ArrayList<>(), 0);

            List<StockItemInventorySearchFilter.ItemGroupFilter> itemsToSearch = stockBatchDTOResult.getData().stream().map(p ->
            {
                StockItemInventorySearchFilter.ItemGroupFilter k = new StockItemInventorySearchFilter.ItemGroupFilter();
                k.setStockItemUuid(filter.getStockItemUuid());
                k.setStockBatchIds(Arrays.asList(p.getId()));
                k.setPartyIds(Arrays.asList(party.getId()));
                return k;
            }).collect(Collectors.toList());

            StockItemInventorySearchFilter searchFilter = new StockItemInventorySearchFilter();
            searchFilter.setItemGroupFilters(itemsToSearch);
            List<StockItemInventory> stockItemInventories = dao.getStockItemInventory(searchFilter, null).getData();
            stockBatchDTOResult.setData(stockBatchDTOResult.getData().stream().filter(p -> stockItemInventories.stream().anyMatch(x -> x.getStockBatchId().equals(p.getId()) && (!filter.getExcludeEmptyStock() || x.getQuantity().compareTo(BigDecimal.ZERO) > 0))).collect(Collectors.toList()));
        }

        return stockBatchDTOResult;
    }

    public StockOperationItem getStockOperationItemByUuid(String uuid) {
        return dao.getStockOperationItemByUuid(uuid);
    }

    public long getStockOperationItemCount(Integer stockOperationId) {
        return dao.getStockOperationItemCount(stockOperationId);
    }

    public void voidStockOperationItem(String stockOperationItemUuid, String reason, int voidedBy) {
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        StockOperationItem stockOperationItem = getStockOperationItemByUuid(stockOperationItemUuid);
        StockOperation stockOperation = stockOperationItem.getStockOperation();
        if (!stockOperation.isUpdateable()) {
            throw new StockManagementException(
                    messageSourceService.getMessage("stockmanagement.stockoperation.notupdateable"));
        }
        if (!stockOperation.getStockOperationType().userCanProcess(Context.getAuthenticatedUser(),
                stockOperation.getAtLocation())) {
            throw new StockManagementException(
                    messageSourceService.getMessage("stockmanagement.stockoperation.nopermission"));
        }

        if (getStockOperationItemCount(stockOperation.getId()) < 2) {
            throw new StockManagementException(
                    messageSourceService.getMessage("stockmanagement.stockoperation.nodeletelaststockoperationitem"));
        }

        dao.voidStockOperationItem(stockOperationItemUuid, reason, voidedBy);
    }

    public void submitStockOperation(StockOperationDTO stockOperationDTO) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.SUBMIT, null,
                parameter1 -> new Pair<>(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE,
                        StockOperationPrivelegeTarget.AtLocation));
    }

    public void dispatchStockOperation(StockOperationDTO stockOperationDTO) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.DISPATCH, null,
                parameter1 -> {
                    String privelege = null;
                    if (parameter1.isUpdateable()) {
                        privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE;
                    } else {
                        privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE;
                    }
                    return new Pair<>(privelege, StockOperationPrivelegeTarget.AtLocation);
                });
    }

    public void stockOperationItemsReceived(StockOperationDTO stockOperationDTO,
                                            List<StockOperationActionLineItem> lineItems) {
        if (lineItems == null || lineItems.isEmpty())
            return;
        StockOperation stockOperation = getStockOperationByUuid(stockOperationDTO.getUuid());
        StockOperationType stockOperationType = stockOperation.getStockOperationType();
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        if (!stockOperationType.requiresDispatchAcknowledgement() || !stockOperation.canReceiveItems()) {
            throw new StockManagementException(
                    messageSourceService.getMessage("stockmanagement.stockoperation.cannotreceiveitems"));
        }

        if (stockOperation.getDestination() == null || stockOperation.getDestination().getLocation() == null ||
                !stockOperationType.userCanProcess(Context.getAuthenticatedUser(),
                        stockOperation.getDestination().getLocation(),
                        Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS)) {
            throw new StockManagementException(
                    messageSourceService.getMessage("stockmanagement.stockoperation.nopermission"));
        }

        BigDecimal threshold = GlobalProperties.getExcessReceivedItemThreshold();
        for (StockOperationActionLineItem lineItem : lineItems) {
            if (lineItem.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new StockManagementException(messageSourceService
                        .getMessage("stockmanagement.stockoperation.nosupportreceivenegativeamount"));
            }

            Optional<StockOperationItem> stockOperationItem = stockOperation.getStockOperationItems().stream()
                    .filter(x -> lineItem.getUuid() != null && x.getUuid().equals(lineItem.getUuid())).findFirst();
            if (!stockOperationItem.isPresent()) {
                throw new StockManagementException(String.format(
                        messageSourceService.getMessage("stockmanagement.stockoperation.lineitemsnotfound"),
                        lineItem.getUuid()));
            }

            Optional<StockItemPackagingUOM> quantityUoM = stockOperationItem.get().getStockItem()
                    .getStockItemPackagingUOMs().stream()
                    .filter(p -> p.getUuid().equals(lineItem.getPackagingUoMUuId())).findFirst();
            if (!quantityUoM.isPresent()) {
                throw new StockManagementException(String.format(
                        messageSourceService.getMessage("stockmanagement.stockoperation.lineitemuomnotfound"),
                        lineItem.getUuid(), lineItem.getPackagingUoMUuId()));
            }

            BigDecimal baseAmountRecevied = lineItem.getAmount().multiply(quantityUoM.get().getFactor());
            BigDecimal baseQuantitySent = stockOperationItem.get().getQuantity()
                    .multiply(stockOperationItem.get().getStockItemPackagingUOM().getFactor());

            if (baseAmountRecevied.compareTo(
                    baseQuantitySent.multiply(BigDecimal.valueOf(100).add(threshold).divide(BigDecimal.valueOf(100), 5,
                            BigDecimal.ROUND_HALF_EVEN))) > 0) {
                throw new StockManagementException(
                        String.format(messageSourceService
                                .getMessage("stockmanagement.stockoperation.cannotreceivemorethansent"), threshold));
            }

            stockOperationItem.get().setQuantityReceived(lineItem.getAmount());
            stockOperationItem.get().setQuantityReceivedPackagingUOM(quantityUoM.get());
        }
        dao.saveStockOperation(stockOperation);
    }

    public void completeStockOperation(StockOperationDTO stockOperationDTO) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.COMPLETE, null, parameter1 -> {
            String privelege = null;
            StockOperationPrivelegeTarget target = null;
            if (parameter1.isUpdateable()) {
                privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE;
                target = StockOperationPrivelegeTarget.AtLocation;
            } else if (parameter1.getStatus() == StockOperationStatus.DISPATCHED) {
                privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS;
                target = StockOperationPrivelegeTarget.Destination;
            } else {
                privelege = UUID.randomUUID().toString();
                target = StockOperationPrivelegeTarget.AtLocation;
            }
            return new Pair<String, StockOperationPrivelegeTarget>(privelege, target);
        });

    }

    public void approveStockOperation(StockOperationDTO stockOperationDTO) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.APPROVE, null,
                parameter1 -> new Pair<>(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE,
                        StockOperationPrivelegeTarget.AtLocation));
    }

    public void rejectStockOperation(StockOperationDTO stockOperationDTO, String reason) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.REJECT, reason,
                parameter1 -> new Pair<>(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE,
                        StockOperationPrivelegeTarget.AtLocation));
    }

    public void returnStockOperation(StockOperationDTO stockOperationDTO, String reason) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.RETURN, reason, parameter1 -> {
            String privelege = null;
            StockOperationPrivelegeTarget target = null;
            if (parameter1.getStatus().equals(StockOperationStatus.DISPATCHED)) {
                privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS;
                target = StockOperationPrivelegeTarget.Destination;
            } else {
                privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE;
                target = StockOperationPrivelegeTarget.AtLocation;
            }
            return new Pair<String, StockOperationPrivelegeTarget>(privelege, target);
        });
    }

    public void cancelStockOperation(StockOperationDTO stockOperationDTO, String reason) {
        processStockOperationAction(stockOperationDTO, StockOperationAction.Action.CANCEL, reason, stockOperation -> {
            String privelege = null;
            StockOperationPrivelegeTarget target = null;
            if (stockOperation.getStatus() == StockOperationStatus.NEW
                    || stockOperation.getStatus() == StockOperationStatus.RETURNED) {
                privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE;
                target = StockOperationPrivelegeTarget.AtLocation;
            } else if (stockOperation.getStatus() == StockOperationStatus.SUBMITTED) {
                privelege = Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE;
                target = StockOperationPrivelegeTarget.AtLocation;
            } else {
                privelege = UUID.randomUUID().toString();
                target = StockOperationPrivelegeTarget.AtLocation;
            }
            return new Pair<String, StockOperationPrivelegeTarget>(privelege, target);
        });
    }

    private void validateStockInventoryAfterOperation(StockOperation stockOperation,
                                                      StockOperationType stockOperationType, MessageSourceService messageSourceService) {
        List<StockOperationItem> applicableStockOperationItems = stockOperation.getStockOperationItems().stream()
                .filter(p -> !p.getVoided()).collect(Collectors.toList());
        List<StockItemInventorySearchFilter.ItemGroupFilter> itemsToSearch = applicableStockOperationItems.stream()
                .filter(p -> !p.getVoided()).map(p -> {
                    StockItemInventorySearchFilter.PartyStockItemBatch group = new StockItemInventorySearchFilter.PartyStockItemBatch(
                            stockOperation.getSource().getId(),
                            p.getStockItem().getId(),
                            p.getStockBatch().getId());
                    return group;
                }).collect(Collectors.groupingBy(p -> p))
                .entrySet().stream().map(p -> new StockItemInventorySearchFilter.ItemGroupFilter(
                        Arrays.asList(p.getKey().getPartyId()),
                        p.getKey().getStockItemId(),
                        p.getKey().getStockBatchId()))
                .collect(Collectors.toList());

        StockItemInventorySearchFilter searchFilter = new StockItemInventorySearchFilter();
        searchFilter.setItemGroupFilters(itemsToSearch);
        if(stockOperationType.getAllowExpiredBatchNumbers() != null && stockOperationType.getAllowExpiredBatchNumbers()){
            searchFilter.setRequireNonExpiredStockBatches(false);
        }
        List<StockItemInventory> stockItemInventories = dao.getStockItemInventory(searchFilter, null).getData();

        List<String> errors = new ArrayList<>();

        boolean uomPriorityIsBigToSmall = GlobalProperties.uomPriorityIsBigToSmall();

        for (StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter : itemsToSearch) {

            List<StockOperationItem> itemGroupItems = applicableStockOperationItems
                    .stream()
                    .filter(p -> !p.getVoided() && p.getStockItem().getId().equals(itemGroupFilter.getStockItemId()) &&
                            p.getStockBatch().getId().equals(itemGroupFilter.getFirstStockBatchId()))
                    .collect(Collectors.toList());

            BigDecimal batchEffect = itemGroupItems.stream()
                    .map(p -> stockOperationType.getQuantityToApplyAtSource(
                            p.getQuantity().multiply(p.getStockItemPackagingUOM().getFactor())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Optional<StockItemInventory> batchCurrentBalance = stockItemInventories.stream()
                    .filter(p -> p.getStockItemId().equals(itemGroupFilter.getStockItemId()) &&
                            p.getStockBatchId().equals(itemGroupFilter.getFirstStockBatchId()))
                    .findAny();

            BigDecimal balance = BigDecimal.ZERO;
            if (batchCurrentBalance.isPresent()) {
                balance = batchCurrentBalance.get().getQuantity();
            }
            BigDecimal netEffectBalance = balance.add(batchEffect);
            if (netEffectBalance.compareTo(BigDecimal.ZERO) < 0) {
                StockOperationItem stockOperationItem = itemGroupItems.get(0);
                String stockItemName = null;
                if (stockOperationItem.getStockItem().getDrug() != null) {
                    stockItemName = stockOperationItem.getStockItem().getDrug().getName();
                    if (stockOperationItem.getStockItem().getConcept() != null) {
                        stockItemName = stockItemName + " ("
                                + stockOperationItem.getStockItem().getConcept().getDisplayString() + ")";
                    }
                } else if (stockOperationItem.getStockItem().getConcept() != null) {
                    stockItemName = stockOperationItem.getStockItem().getConcept().getDisplayString();
                }

                BigDecimal displayUnitValue = netEffectBalance;
                String displayUom = null;
                for (StockOperationItem itemInGroup : itemGroupItems) {
                    displayUnitValue = netEffectBalance.divide(itemInGroup.getStockItemPackagingUOM().getFactor(), 5,
                            BigDecimal.ROUND_HALF_EVEN);
                    if (displayUnitValue.abs().compareTo(BigDecimal.ZERO) > 0) {
                        displayUom = itemInGroup.getStockItemPackagingUOM().getPackagingUom().getDisplayString();
                        break;
                    }
                }
                if (StringUtils.isBlank(displayUom)) {
                    StockItemPackagingUOMSearchFilter uomFilter = new StockItemPackagingUOMSearchFilter();
                    uomFilter.setStockItemIds(Arrays.asList(stockOperationItem.getStockItem().getId()));
                    Result<StockItemPackagingUOMDTO> uoms = findStockItemPackagingUOMs(uomFilter);
                    if (!uoms.getData().isEmpty()) {
                        StockItemPackagingUOMDTO preferredUoM = getPreferredPackagingUoM(netEffectBalance, uoms.getData(),false,uomPriorityIsBigToSmall,stockOperationItem.getStockItemPackagingUOM() != null ? stockOperationItem.getStockItemPackagingUOM().getId() : null);
                        displayUnitValue = netEffectBalance.divide(preferredUoM.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN);
                        displayUom = preferredUoM.getPackagingUomName();
                    }
                }

                if (displayUom == null) {
                    displayUnitValue = netEffectBalance;
                }

                errors.add(
                        String.format(
                                messageSourceService.getMessage(
                                        "stockmanagement.stockoperation.itembatchbalancenegativeafteroperation"),
                                stockItemName,
                                stockOperationItem.getStockBatch().getBatchNo(),
                                NumberFormatUtil.qtyDisplayFormat(displayUnitValue),
                                displayUom));
            }
        }
        if (!errors.isEmpty()) {
            throw new StockManagementException(String.join(", ", errors));
        }
    }

    private List<ReservedTransaction> calculateStockIssueAdjustments(StockOperation stockOperation,
                                                                     StockOperationType stockOperationType, MessageSourceService messageSourceService) {
        List<StockOperationItem> applicableStockOperationItems = stockOperation.getStockOperationItems().stream()
                .filter(p -> !p.getVoided()).collect(Collectors.toList());
        List<StockItemInventorySearchFilter.ItemGroupFilter> itemsToSearch = applicableStockOperationItems.stream()
                .filter(p -> !p.getVoided()).map(p -> {
                    StockItemInventorySearchFilter.PartyStockItemBatch group = new StockItemInventorySearchFilter.PartyStockItemBatch(
                            stockOperation.getSource().getId(),
                            p.getStockItem().getId(),
                            p.getStockBatch().getId());
                    return group;
                }).collect(Collectors.groupingBy(p -> p))
                .entrySet().stream().map(p -> new StockItemInventorySearchFilter.ItemGroupFilter(
                        Arrays.asList(p.getKey().getPartyId()),
                        p.getKey().getStockItemId(),
                        p.getKey().getStockBatchId()))
                .collect(Collectors.toList());

        StockItemInventorySearchFilter searchFilter = new StockItemInventorySearchFilter();
        searchFilter.setItemGroupFilters(itemsToSearch);
        List<StockItemInventory> stockItemInventories = dao.getStockItemInventory(searchFilter, null).getData();
        boolean uomPriorityIsBigToSmall = GlobalProperties.uomPriorityIsBigToSmall();
        List<ReservedTransaction> reservedTransactions = new ArrayList<ReservedTransaction>();
        for (StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter : itemsToSearch) {

            List<StockOperationItem> itemGroupItems = applicableStockOperationItems
                    .stream()
                    .filter(p -> !p.getVoided() && p.getStockItem().getId().equals(itemGroupFilter.getStockItemId()) &&
                            p.getStockBatch().getId().equals(itemGroupFilter.getFirstStockBatchId()))
                    .collect(Collectors.toList());

            BigDecimal expectedBalance = itemGroupItems.stream()
                    .map(p -> stockOperationType.getQuantityToApplyAtSource(
                            p.getQuantity().multiply(p.getStockItemPackagingUOM().getFactor())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Optional<StockItemInventory> currentBatchBalance = stockItemInventories.stream()
                    .filter(p -> p.getStockItemId().equals(itemGroupFilter.getStockItemId()) &&
                            p.getStockBatchId().equals(itemGroupFilter.getFirstStockBatchId()))
                    .findAny();

            BigDecimal currentBalance = BigDecimal.ZERO;
            if (currentBatchBalance.isPresent()) {
                currentBalance = currentBatchBalance.get().getQuantity();
            }

            BigDecimal adjustment = expectedBalance.subtract(currentBalance);
            BigDecimal finalAdjustmentUnitValue = null;
            StockItemPackagingUOM finalAdjustmentUom = null;
            BigDecimal absAdjustment = adjustment.abs();
            for (StockOperationItem itemInGroup : itemGroupItems) {

                if (absAdjustment.remainder(itemInGroup.getStockItemPackagingUOM().getFactor())
                        .compareTo(BigDecimal.ZERO) == 0) {
                    finalAdjustmentUom = itemInGroup.getStockItemPackagingUOM();
                    finalAdjustmentUnitValue = adjustment.divide(itemInGroup.getStockItemPackagingUOM().getFactor(), 5,
                            BigDecimal.ROUND_HALF_EVEN);
                    break;
                }
            }
            if (finalAdjustmentUom == null) {
                StockItemPackagingUOMSearchFilter uomFilter = new StockItemPackagingUOMSearchFilter();
                uomFilter.setStockItemIds(Arrays.asList(itemGroupItems.get(0).getStockItem().getId()));
                Result<StockItemPackagingUOMDTO> uoms = findStockItemPackagingUOMs(uomFilter);
                if (!uoms.getData().isEmpty()) {
                    StockItemPackagingUOMDTO preferredUoM = getPreferredPackagingUoM(absAdjustment,uoms.getData(),false,uomPriorityIsBigToSmall,itemGroupItems.get(0).getStockItemPackagingUOM() != null ? itemGroupItems.get(0).getStockItemPackagingUOM().getId() : null);
                    finalAdjustmentUom = dao.getStockItemPackagingUOMByUuid(preferredUoM.getUuid());
                    finalAdjustmentUnitValue = adjustment.divide(preferredUoM.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN);
                }
            }

            if (finalAdjustmentUom == null) {
                StockOperationItem stockOperationItem = itemGroupItems.get(0);
                String stockItemName = null;
                if (stockOperationItem.getStockItem().getDrug() != null) {
                    stockItemName = stockOperationItem.getStockItem().getDrug().getName();
                    if (stockOperationItem.getStockItem().getConcept() != null) {
                        stockItemName = stockItemName + " ("
                                + stockOperationItem.getStockItem().getConcept().getDisplayString() + ")";
                    }
                } else if (stockOperationItem.getStockItem().getConcept() != null) {
                    stockItemName = stockOperationItem.getStockItem().getConcept().getDisplayString();
                }
                throw new StockManagementException(
                        String.format(
                                messageSourceService.getMessage("stockmanagement.stockoperation.nocompatibleuom"),
                                stockItemName,
                                adjustment.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
            }

            boolean assignedFirstItem = false;
            for (StockOperationItem stockOperationItem : itemGroupItems) {
                ReservedTransaction tx = new ReservedTransaction(stockOperation, stockOperationItem);
                tx.setCreator(Context.getAuthenticatedUser());
                tx.setDateCreated(new Date());
                tx.setParty(stockOperation.getSource());
                stockOperation.addReservedTransaction(tx);
                if (assignedFirstItem) {
                    tx.setQuantity(BigDecimal.ZERO);
                } else {
                    tx.setQuantity(finalAdjustmentUnitValue);
                    tx.setStockItemPackagingUOM(finalAdjustmentUom);
                    assignedFirstItem = true;
                }
                reservedTransactions.add(tx);
            }
        }

        return reservedTransactions;
    }

    private void processStockOperationAction(StockOperationDTO stockOperationDTO, StockOperationAction.Action action,
                                             String reason, Function<StockOperation, Pair<String, StockOperationPrivelegeTarget>> getRequiredPrivilege) {
        StockOperationType stockOperationType;
        synchronized (STOCK_OPERATION_PROCESSING_LOCK) {
            MessageSourceService messageSourceService = Context.getMessageSourceService();
            StockOperation stockOperation = getStockOperationByUuid(stockOperationDTO.getUuid());
            if (action.equals(StockOperationAction.Action.SUBMIT)) {
                if (!stockOperation.isUpdateable()) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.notupdateable"));
                }
            } else if (action.equals(StockOperationAction.Action.COMPLETE)) {
                boolean canComplete = false;
                if (stockOperation.isUpdateable()
                        && !stockOperation.getStockOperationType().requiresDispatchAcknowledgement()) {
                    canComplete = true;
                } else if (!stockOperation.isUpdateable()
                        && stockOperation.getStockOperationType().requiresDispatchAcknowledgement()
                        && stockOperation.getStatus().equals(StockOperationStatus.DISPATCHED)) {
                    canComplete = true;
                }
                if (!canComplete) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.notupdateable"));
                }
            } else if (action.equals(StockOperationAction.Action.DISPATCH)) {
                if (!((stockOperation.isUpdateable()
                        || stockOperation.getStatus().equals(StockOperationStatus.SUBMITTED)) && stockOperation
                        .getStockOperationType().requiresDispatchAcknowledgement())) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.notdispatcheable"));
                }
            } else if (action.equals(StockOperationAction.Action.RETURN)) {
                boolean canReturn = false;
                if (stockOperation.getStatus().equals(StockOperationStatus.SUBMITTED)) {
                    canReturn = true;
                } else if (stockOperation.getStatus().equals(StockOperationStatus.DISPATCHED)) {
                    canReturn = true;
                }
                if (!canReturn) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.approvalactionnotallowed"));
                }
            } else if (action.equals(StockOperationAction.Action.REJECT)) {
                if ((!stockOperation.getStatus().equals(StockOperationStatus.SUBMITTED))) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.approvalactionnotallowed"));
                }
            } else if (action.equals(StockOperationAction.Action.APPROVE)) {
                if (!(stockOperation.getStatus().equals(StockOperationStatus.SUBMITTED) && !stockOperation
                        .getStockOperationType().requiresDispatchAcknowledgement())) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.approvalactionnotallowed"));
                }
            } else if (action.equals(StockOperationAction.Action.CANCEL)) {
                boolean canCancel = false;
                if (stockOperation.isUpdateable()) {
                    canCancel = true;
                } else if (stockOperation.getStatus().equals(StockOperationStatus.SUBMITTED)) {
                    canCancel = true;
                }
                if (!canCancel) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.stockoperation.cancelactionnotallowed"));
                }
            } else {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.stockoperation.cancelactionnotallowed"));
            }

            stockOperationType = stockOperation.getStockOperationType();
            Location location = null;

            Pair<String, StockOperationPrivelegeTarget> requiredPrivilege = getRequiredPrivilege.apply(stockOperation);
            if (requiredPrivilege == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.stockoperation.nopermission"));
            } else {
                if (requiredPrivilege.getValue2().equals(StockOperationPrivelegeTarget.AtLocation)) {
                    location = stockOperation.getAtLocation();
                } else if (requiredPrivilege.getValue2().equals(StockOperationPrivelegeTarget.Destination)) {
                    location = stockOperation.getDestination().getLocation();
                } else if (requiredPrivilege.getValue2().equals(StockOperationPrivelegeTarget.Source)) {
                    location = stockOperation.getSource().getLocation();
                }
            }

            if (location == null
                    || !stockOperationType.userCanProcess(Context.getAuthenticatedUser(), location,
                    requiredPrivilege.getValue1())) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.stockoperation.nopermission"));
            }

            List<ReservedTransaction> reservedTransactions = null;
            if (action == StockOperationAction.Action.SUBMIT
                    || (stockOperation.isUpdateable() && (action.equals(StockOperationAction.Action.COMPLETE) || action
                    .equals(StockOperationAction.Action.DISPATCH)))) {
                if (!stockOperationType.isQuantityOptional()) {
                    if (stockOperationType.shouldVerifyNegativeStockAmountsAtSource()
                            && !GlobalProperties.getNegativeStockBalanceAllowed() && stockOperation.getSource() != null
                            && stockOperation.getSource().getLocation() != null) {
                        validateStockInventoryAfterOperation(stockOperation, stockOperationType, messageSourceService);
                    }

                    if (StockOperationType.STOCKTAKE.equals(stockOperationType.getOperationType())) {
                        reservedTransactions = calculateStockIssueAdjustments(stockOperation, stockOperationType,
                                messageSourceService);
                    }
                }
            }

            processStockOperationAction(action, reason, stockOperation, stockOperationType, messageSourceService,
                    reservedTransactions);
        }

        boolean notify = false;
        switch (action){
            case SUBMIT:
                notify = stockOperationType.getNotifySubmitted();
                break;
            case APPROVE:
                notify = stockOperationType.getNotifyApproved();
                break;
            case DISPATCH:
                notify = stockOperationType.getNotifyDispatched();
                break;
            case RETURN:
                notify = stockOperationType.getNotifyReturned();
                break;
            case REJECT:
                notify = stockOperationType.getNotifyRejected();
                break;
            case COMPLETE:
                notify = stockOperationType.getNotifyCompleted();
                break;
            case CANCEL:
                notify = stockOperationType.getNotifyCancelled();
                break;
        }
        if(notify){
            try {
                StockOperationNotificationTask stockOperationNotificationTask = new StockOperationNotificationTask(stockOperationDTO.getUuid(), action, reason, Context.getAuthenticatedUser().getUserId());
                stockOperationNotificationTask.fireAndForget();
            }catch (Exception exception){
                log.error(exception);
            }
        }
    }

    public void processStockOperationAction(StockOperationAction.Action action, String reason,
                                            StockOperation stockOperation, StockOperationType stockOperationType,
                                            MessageSourceService messageSourceService,
                                            List<ReservedTransaction> reservedTransactions) {
        if (action.equals(StockOperationAction.Action.SUBMIT)
                || (stockOperation.isUpdateable() && (action.equals(StockOperationAction.Action.COMPLETE) || action
                .equals(StockOperationAction.Action.DISPATCH)))) {
            if (!stockOperationType.isQuantityOptional()) {
                if (StockOperationType.STOCKTAKE.equals(stockOperationType.getOperationType())) {
                    // reserved transactions
                    for (ReservedTransaction tx : reservedTransactions) {
                        stockOperation.addReservedTransaction(tx);
                    }
                } else {
                    for (StockOperationItem item : stockOperation.getStockOperationItems()) {
                        if (item.getVoided())
                            continue;

                        ReservedTransaction tx = new ReservedTransaction(stockOperation, item);
                        tx.setCreator(Context.getAuthenticatedUser());
                        tx.setDateCreated(new Date());
                        tx.setParty(stockOperation.getSource());
                        stockOperation.addReservedTransaction(tx);
                    }
                }
            }

            stockOperation.setLocked(true);
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());

            if (action == StockOperationAction.Action.SUBMIT) {
                stockOperation.setSubmittedBy(Context.getAuthenticatedUser());
                stockOperation.setStatus(StockOperationStatus.SUBMITTED);
                stockOperation.setSubmittedDate(new Date());
            } else if (action == StockOperationAction.Action.COMPLETE) {
                stockOperation.setCompletedBy(Context.getAuthenticatedUser());
                stockOperation.setStatus(StockOperationStatus.COMPLETED);
                stockOperation.setCompletedDate(new Date());
            } else if (action == StockOperationAction.Action.DISPATCH) {
                resetDispatchedQuantities(stockOperation);
                stockOperation.setDispatchedBy(Context.getAuthenticatedUser());
                stockOperation.setStatus(StockOperationStatus.DISPATCHED);
                stockOperation.setDispatchedDate(new Date());
            }

            stockOperationType.onPending(stockOperation);
            if (action == StockOperationAction.Action.COMPLETE) {
                stockOperationType.onCompleted(stockOperation);
            }
        } else if (action == StockOperationAction.Action.DISPATCH) {
            resetDispatchedQuantities(stockOperation);
            stockOperation.setDispatchedBy(Context.getAuthenticatedUser());
            stockOperation.setStatus(StockOperationStatus.DISPATCHED);
            stockOperation.setDispatchedDate(new Date());
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());
        } else if (action == StockOperationAction.Action.COMPLETE) {

            stockOperation.setCompletedBy(Context.getAuthenticatedUser());
            stockOperation.setStatus(StockOperationStatus.COMPLETED);
            stockOperation.setCompletedDate(new Date());

            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());

            stockOperationType.onCompleted(stockOperation);

        } else if (action == StockOperationAction.Action.APPROVE) {
            stockOperation.setCompletedBy(Context.getAuthenticatedUser());
            stockOperation.setStatus(StockOperationStatus.COMPLETED);
            stockOperation.setCompletedDate(new Date());
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());

            stockOperationType.onCompleted(stockOperation);
        } else if (action == StockOperationAction.Action.RETURN) {
            stockOperation.setLocked(false);
            stockOperation.setReturnedBy(Context.getAuthenticatedUser());
            stockOperation.setStatus(StockOperationStatus.RETURNED);
            stockOperation.setReturnedDate(new Date());
            stockOperation.setReturnReason(reason);
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());

            stockOperationType.onCancelled(stockOperation);
        } else if (action == StockOperationAction.Action.REJECT) {
            stockOperation.setRejectedBy(Context.getAuthenticatedUser());
            stockOperation.setStatus(StockOperationStatus.REJECTED);
            stockOperation.setRejectedDate(new Date());
            stockOperation.setRejectionReason(reason);
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());

            stockOperationType.onCancelled(stockOperation);
        } else if (action == StockOperationAction.Action.CANCEL) {
            stockOperation.setCancelledBy(Context.getAuthenticatedUser());
            stockOperation.setStatus(StockOperationStatus.CANCELLED);
            stockOperation.setCancelledDate(new Date());
            stockOperation.setCancelReason(reason);
            stockOperation.setChangedBy(Context.getAuthenticatedUser());
            stockOperation.setDateChanged(new Date());

            stockOperationType.onCancelled(stockOperation);
        }
        dao.saveStockOperation(stockOperation);
    }

    private void resetDispatchedQuantities(StockOperation stockOperation) {
        for (StockOperationItem stockOperationItem : stockOperation.getStockOperationItems()) {
            stockOperationItem.setQuantityReceived(stockOperationItem.getQuantity());
            stockOperationItem.setQuantityReceivedPackagingUOM(stockOperationItem.getStockItemPackagingUOM());
        }
    }

    public void dispenseStockItems(List<DispenseRequest> dispenseRequests) {
        if (dispenseRequests == null || dispenseRequests.isEmpty())
            return;
        MessageSourceService messageSourceService = Context.getMessageSourceService();

        Map<String, StockItem> stockItemMap = new HashMap<>();
        Map<String, StockBatch> stockBatchMap = new HashMap<>();
        Map<String, StockItemPackagingUOM> stockItemPackagingUOMMap = new HashMap<>();
        Map<Integer, Patient> patientMap = new HashMap<>();
        Map<Integer, Order> orderMap = new HashMap<>();
        Map<Integer, Encounter> encounterMap = new HashMap<>();
        Map<String, Location> locationMap = new HashMap<>();
        boolean uomPriorityIsBigToSmall = GlobalProperties.uomPriorityIsBigToSmall();
        List<DispenseRequestProcessingInfo> processingInformation = new ArrayList<>();

        for (DispenseRequest dispenseRequest : dispenseRequests) {
            DispenseRequestProcessingInfo processingInfo = new DispenseRequestProcessingInfo();
            Location location = null;
            if (dispenseRequest.getLocationUuid() != null) {
                location = locationMap.getOrDefault(dispenseRequest.getLocationUuid(), null);
                if (location == null) {
                    location = Context.getLocationService().getLocationByUuid(dispenseRequest.getLocationUuid());
                    if (location != null) {
                        locationMap.putIfAbsent(dispenseRequest.getLocationUuid(), location);
                    }
                }
            }
            if (location == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.dispenseoperation.locationrequired"));
            }
            processingInfo.setLocation(location);
            processingInfo.setParty(getPartyByLocation(location));

            StockItem stockItem = null;
            if (!StringUtils.isBlank(dispenseRequest.getStockItemUuid())) {
                stockItem = stockItemMap.getOrDefault(dispenseRequest.getStockItemUuid(), null);
                if (stockItem == null) {
                    stockItem = getStockItemByUuid(dispenseRequest.getStockItemUuid());
                    if (stockItem != null) {
                        stockItemMap.putIfAbsent(dispenseRequest.getStockItemUuid(), stockItem);
                    }
                }
            }
            if (stockItem == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.dispenseoperation.stockitemrequired"));
            }
            processingInfo.setStockItem(stockItem);

            StockBatch stockBatch = null;
            if (!StringUtils.isBlank(dispenseRequest.getStockBatchUuid())) {
                stockBatch = stockBatchMap.getOrDefault(dispenseRequest.getStockBatchUuid(), null);
                if (stockBatch == null) {
                    stockBatch = getStockBatchByUuid(dispenseRequest.getStockBatchUuid());
                    if (stockBatch != null) {
                        stockItemMap.putIfAbsent(dispenseRequest.getStockBatchUuid(), stockItem);
                    }
                }
            }
            if (stockBatch == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.dispenseoperation.stockbatchrequired"));
            }
            processingInfo.setStockBatch(stockBatch);

            Patient patient = null;
            if (dispenseRequest.getPatientId() != null) {
                patient = patientMap.getOrDefault(dispenseRequest.getPatientId(), null);
                if (patient == null) {
                    patient = Context.getPatientService().getPatient(dispenseRequest.getPatientId());
                    if (patient != null) {
                        patientMap.putIfAbsent(dispenseRequest.getPatientId(), patient);
                    }
                }
            }
            if (patient == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.dispenseoperation.patientrequired"));
            }
            processingInfo.setPatient(patient);

            if (dispenseRequest.getOrderId() != null) {
                Order order = orderMap.getOrDefault(dispenseRequest.getOrderId(), null);
                if (order == null) {
                    order = Context.getOrderService().getOrder(dispenseRequest.getOrderId());
                    if (order != null) {
                        orderMap.putIfAbsent(dispenseRequest.getOrderId(), order);
                    }
                }
                if (order == null) {
                    throw new StockManagementException(
                            messageSourceService.getMessage("stockmanagement.dispenseoperation.validorderrequired"));
                }
                processingInfo.setOrder(order);
            }

            if (dispenseRequest.getEncounterId() != null) {
                Encounter encounter = encounterMap.getOrDefault(dispenseRequest.getEncounterId(), null);
                if (encounter == null) {
                    encounter = Context.getEncounterService().getEncounter(dispenseRequest.getEncounterId());
                    if (encounter != null) {
                        encounterMap.putIfAbsent(dispenseRequest.getEncounterId(), encounter);
                    }
                }
                if (encounter == null) {
                    throw new StockManagementException(messageSourceService
                            .getMessage("stockmanagement.dispenseoperation.validencounterrequired"));
                }
                processingInfo.setEncounter(encounter);
            }

            if (dispenseRequest.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.dispenseoperation.quantitygreaterthanzero"));
            }
            processingInfo.setQuantity(dispenseRequest.getQuantity());

            StockItemPackagingUOM stockItemPackagingUOM = null;
            if (dispenseRequest.getStockItemPackagingUOMUuid() != null) {
                stockItemPackagingUOM = stockItemPackagingUOMMap
                        .getOrDefault(dispenseRequest.getStockItemPackagingUOMUuid(), null);
                if (stockItemPackagingUOM == null) {
                    stockItemPackagingUOM = getStockItemPackagingUOMByUuid(
                            dispenseRequest.getStockItemPackagingUOMUuid());
                    if (stockItemPackagingUOM != null) {
                        stockItemPackagingUOMMap.putIfAbsent(dispenseRequest.getStockItemPackagingUOMUuid(),
                                stockItemPackagingUOM);
                    }
                }
            }
            if (stockItemPackagingUOM == null) {
                throw new StockManagementException(
                        messageSourceService.getMessage("stockmanagement.dispenseoperation.packaginguomrequired"));
            }
            processingInfo.setPackagingUOM(stockItemPackagingUOM);
            processingInformation.add(processingInfo);
        }

        // get corresponding locations
        final Map<String, List<Party>> partyMap = getPartyListByLocations(locationMap.values()).stream()
                .collect(Collectors.groupingBy(p -> p.getLocation().getUuid().toLowerCase()));

        // prepare the stock inventory search filters
        List<StockItemInventorySearchFilter.ItemGroupFilter> itemsToSearch = processingInformation.stream().map(p -> {
            StockItemInventorySearchFilter.PartyStockItemBatch group = new StockItemInventorySearchFilter.PartyStockItemBatch(
                    p.getParty().getId(),
                    p.getStockItem().getId(),
                    p.getStockBatch().getId());
            return group;
        }).collect(Collectors.groupingBy(p -> p))
                .entrySet().stream().map(p -> new StockItemInventorySearchFilter.ItemGroupFilter(
                        Arrays.asList(p.getKey().getPartyId()),
                        p.getKey().getStockItemId(),
                        p.getKey().getStockBatchId()))
                .collect(Collectors.toList());

        // Ensure all the location dispensing locks are setup
        for (Map.Entry<String, Location> location : locationMap.entrySet()) {
            if (!LOCATION_DISPENSING_OPERATION_LOCKS.containsKey(location.getKey().toLowerCase())) {
                synchronized (DISPENSING_PROCESSING_LOCK) {
                    if (!LOCATION_DISPENSING_OPERATION_LOCKS.containsKey(location.getKey().toLowerCase())) {
                        LOCATION_DISPENSING_OPERATION_LOCKS.putIfAbsent(location.getKey().toLowerCase(),
                                UUID.randomUUID());
                    }
                }
            }
        }

        Map<String, List<DispenseRequestProcessingInfo>> dispenseLocationGroups = processingInformation.stream()
                .collect(Collectors.groupingBy(p -> p.getLocation().getUuid().toLowerCase()));
        for (Map.Entry<String, List<DispenseRequestProcessingInfo>> dispenseGroup : dispenseLocationGroups.entrySet()) {
            int locationPartyId = partyMap.get(dispenseGroup.getKey()).get(0).getId();
            StockItemInventorySearchFilter searchFilter = new StockItemInventorySearchFilter();
            searchFilter.setItemGroupFilters(itemsToSearch.stream()
                    .filter(p -> p.getPartyIds().contains(locationPartyId)).collect(Collectors.toList()));
            if (searchFilter.getItemGroupFilters().isEmpty()) {
                throw new StockManagementException("Item group filters mismatch");
            }

            UUID lockObject = LOCATION_DISPENSING_OPERATION_LOCKS.get(dispenseGroup.getKey());
            synchronized (lockObject) {
                List<StockItemInventory> stockItemInventories = dao.getStockItemInventory(searchFilter, null).getData();
                List<String> errors = new ArrayList<>();
                for (StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter : searchFilter
                        .getItemGroupFilters()) {
                    List<DispenseRequestProcessingInfo> itemGroupItems = dispenseGroup.getValue().stream()
                            .filter(p -> p.getStockItem().getId().equals(itemGroupFilter.getStockItemId()) &&
                                    p.getStockBatch().getId().equals(itemGroupFilter.getFirstStockBatchId()))
                            .collect(Collectors.toList());

                    StockItem stockItem = itemGroupItems.get(0).getStockItem();
                    StockBatch stockBatch = itemGroupItems.get(0).getStockBatch();

                    BigDecimal batchEffect = itemGroupItems.stream()
                            .map(p -> p.getQuantity().multiply(p.getPackagingUOM().getFactor())
                                    .multiply(BigDecimal.valueOf(-1)))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    Optional<StockItemInventory> batchCurrentBalance = stockItemInventories.stream()
                            .filter(p -> p.getStockItemId().equals(itemGroupFilter.getStockItemId()) &&
                                    p.getStockBatchId().equals(itemGroupFilter.getFirstStockBatchId()))
                            .findAny();

                    BigDecimal balance = BigDecimal.ZERO;
                    if (batchCurrentBalance.isPresent()) {
                        balance = batchCurrentBalance.get().getQuantity();
                    }
                    BigDecimal netEffectBalance = balance.add(batchEffect);
                    if (netEffectBalance.compareTo(BigDecimal.ZERO) < 0) {
                        String stockItemName = null;
                        if (stockItem.getDrug() != null) {
                            stockItemName = stockItem.getDrug().getName();
                            if (stockItem.getConcept() != null) {
                                stockItemName = stockItemName + " (" + stockItem.getConcept().getDisplayString() + ")";
                            }
                        } else if (stockItem.getConcept() != null) {
                            stockItemName = stockItem.getConcept().getDisplayString();
                        }

                        BigDecimal displayUnitValue = netEffectBalance;
                        String displayUom = null;
                        for (DispenseRequestProcessingInfo itemInGroup : itemGroupItems) {
                            displayUnitValue = netEffectBalance.divide(itemInGroup.getPackagingUOM().getFactor(), 5,
                                    BigDecimal.ROUND_HALF_EVEN);
                            if (displayUnitValue.abs().compareTo(BigDecimal.ZERO) > 0) {
                                displayUom = itemInGroup.getPackagingUOM().getPackagingUom().getDisplayString();
                                break;
                            }
                        }
                        if (StringUtils.isBlank(displayUom)) {
                            StockItemPackagingUOMSearchFilter uomFilter = new StockItemPackagingUOMSearchFilter();
                            uomFilter.setStockItemIds(Arrays.asList(stockItem.getId()));
                            Result<StockItemPackagingUOMDTO> uoms = findStockItemPackagingUOMs(uomFilter);
                            if (!uoms.getData().isEmpty()) {
                                StockItemPackagingUOMDTO preferredUoM = getPreferredPackagingUoM(netEffectBalance,uoms.getData(),true,uomPriorityIsBigToSmall,null);
                                displayUnitValue = netEffectBalance.divide(preferredUoM.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN);
                                displayUom = preferredUoM.getPackagingUomName();
                            }
                        }

                        if (displayUom == null) {
                            displayUnitValue = netEffectBalance;
                        }

                        errors.add(
                                String.format(
                                        messageSourceService.getMessage(
                                                "stockmanagement.stockoperation.itembatchbalancenegativeafteroperation"),
                                        stockItemName,
                                        stockBatch.getBatchNo(),
                                        NumberFormatUtil.qtyDisplayFormat(displayUnitValue),
                                        displayUom));
                    }
                }
                if (!errors.isEmpty()) {
                    throw new StockManagementException(String.join(", ", errors));
                }

                Party party = partyMap.get(dispenseGroup.getKey()).get(0);
                for (DispenseRequestProcessingInfo item : dispenseGroup.getValue()) {
                    StockItemTransaction stockItemTransaction = new StockItemTransaction();
                    stockItemTransaction.setParty(party);
                    stockItemTransaction.setPatient(item.getPatient());
                    stockItemTransaction.setCreator(Context.getAuthenticatedUser());
                    stockItemTransaction.setDateCreated(new Date());
                    stockItemTransaction.setStockItem(item.getStockItem());
                    stockItemTransaction.setStockBatch(item.getStockBatch());
                    stockItemTransaction.setQuantity(item.getQuantity().multiply(BigDecimal.valueOf(-1)));
                    stockItemTransaction.setStockItemPackagingUOM(item.getPackagingUOM());
                    if (item.getOrder() != null) {
                        stockItemTransaction.setOrder(item.getOrder());
                    }
                    if (item.getEncounter() != null) {
                        stockItemTransaction.setEncounter(item.getEncounter());
                    }
                    dao.saveStockItemTransaction(stockItemTransaction);

                }
            }
        }
    }

    public void deleteReservedTransations(Integer stockOperationId) {
        dao.deleteReservedTransations(stockOperationId);
    }

    public List<StockItemInventory> getStockBatchLocationInventory(List<Integer> stockBatchIds) {
        List<StockItemInventory> result = dao.getStockBatchLocationInventory(stockBatchIds);
        if (!result.isEmpty()) {
            StockItemPackagingUOMSearchFilter uomFilter = new StockItemPackagingUOMSearchFilter();
            uomFilter.setStockItemIds(result.stream().map(p -> p.getStockItemId()).filter(p -> p != null).collect(Collectors.toList()));
            Map<Integer, List<StockItemPackagingUOMDTO>> uoms = findStockItemPackagingUOMs(uomFilter).getData().stream().collect(Collectors.groupingBy(StockItemPackagingUOMDTO::getStockItemId));
            if (!uoms.isEmpty()) {
                boolean uomPriorityIsBigToSmall = GlobalProperties.uomPriorityIsBigToSmall();
                for (Map.Entry<Integer, List<StockItemPackagingUOMDTO>> entry : uoms.entrySet()) {
                    for (StockItemInventory stockItemInventory : result.stream().filter(p -> p.getStockItemId().equals(entry.getKey())).collect(Collectors.toList())) {
                        List<StockItemPackagingUOMDTO> uomList = entry.getValue();
                        StockItemPackagingUOMDTO preferredUoM = getPreferredPackagingUoM(stockItemInventory.getQuantity(), uomList, false, uomPriorityIsBigToSmall, null);
                        stockItemInventory.setQuantity(stockItemInventory.getQuantity().divide(preferredUoM.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
                        stockItemInventory.setQuantityUoM(preferredUoM.getPackagingUomName());
                        stockItemInventory.setQuantityUoMUuid(preferredUoM.getUuid());
                    }
                }
            }
        }

        return result;
    }

    public StockInventoryResult getStockInventory(StockItemInventorySearchFilter filter) {
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters = null;

        if (!filter.dispensing()) {
            recordPrivilegeFilters = getRecordPrivilegeFilters(Context.getAuthenticatedUser(), null, null,
                    Privileges.APP_STOCKMANAGEMENT_STOCKITEMS);
            if (recordPrivilegeFilters == null || recordPrivilegeFilters.isEmpty())
                return new StockInventoryResult(new ArrayList<>(), 0);
        }
        return getStockInventory(filter, recordPrivilegeFilters);
    }

    public StockInventoryResult getStockInventory(StockItemInventorySearchFilter filter, HashSet<RecordPrivilegeFilter> recordPrivilegeFilters) {
        if (filter == null || (recordPrivilegeFilters != null && recordPrivilegeFilters.isEmpty())) {
            return new StockInventoryResult(new ArrayList<>(), 0);
        }
        StockInventoryResult result = dao.getStockItemInventory(filter, recordPrivilegeFilters);
        return postProcessInventoryResult(filter, result);
    }

    public StockInventoryResult postProcessInventoryResult(StockItemInventorySearchFilter filter, StockInventoryResult result) {
        Map<Integer, String> partyNames = null;
        Map<Integer, StockBatchDTO> stockBatchNames = null;
        if (!result.getData().isEmpty()) {
            if (filter.getDoSetPartyNameField()) {
                List<Integer> partyIds = result.getData().stream().filter(p -> p.getPartyId() != null).map(p -> p.getPartyId()).distinct().collect(Collectors.toList());
                if (!partyIds.isEmpty()) {
                    partyNames = dao.getPartyNames(partyIds);
                }
            }

            if (filter.getDoSetBatchFields()) {
                List<Integer> batchIds = result.getData().stream().filter(p -> p.getStockBatchId() != null).map(p -> p.getStockBatchId()).distinct().collect(Collectors.toList());
                if (!batchIds.isEmpty()) {
                    StockBatchSearchFilter stockBatchSearchFilter = new StockBatchSearchFilter();
                    stockBatchSearchFilter.setStockBatchIds(batchIds);
                    Result<StockBatchDTO> stockBatches = dao.findStockBatches(stockBatchSearchFilter);
                    if (!stockBatches.getData().isEmpty()) {
                        stockBatchNames = stockBatches.getData().stream().collect(Collectors.toMap(StockBatchDTO::getId, p -> p));
                    }
                }
            }

            if (partyNames != null || stockBatchNames != null) {
                for (StockItemInventory stockItemInventory : result.getData()) {
                    if (partyNames != null && stockItemInventory.getPartyId() != null) {
                        stockItemInventory.setPartyName(partyNames.getOrDefault(stockItemInventory.getPartyId(), null));
                    }
                    if (stockBatchNames != null && stockItemInventory.getStockBatchId() != null) {
                        StockBatchDTO batch = stockBatchNames.getOrDefault(stockItemInventory.getStockBatchId(), null);
                        if (batch != null) {
                            stockItemInventory.setBatchNumber(batch.getBatchNo());
                            stockItemInventory.setExpiration(batch.getExpiration());
                            stockItemInventory.setStockBatchUuid(batch.getUuid());
                        }
                    }
                }
            }

            if (filter.getTotalBy() != null) {
                switch (filter.getTotalBy()) {
                    case LocationStockItem:
                        result.setTotals(result.getData().stream().collect(Collectors.groupingBy(p -> new Pair<>(p.getPartyId(), p.getStockItemId())))
                                        .entrySet()
                                        .stream()
                                        .map(p -> {
                                            StockItemInventory stockItemInventory = new StockItemInventory();
                                            StockItemInventory inventory = p.getValue().get(0);
                                            stockItemInventory.setPartyId(inventory.getPartyId());
                                            stockItemInventory.setLocationUuid(inventory.getLocationUuid());
                                            stockItemInventory.setStockItemId(inventory.getStockItemId());
                                            stockItemInventory.setQuantity(p.getValue().stream().map(x -> x.getQuantity()).reduce(BigDecimal.ZERO, BigDecimal::add));
                                            return stockItemInventory;
                                        }).collect(Collectors.toList())
                        );
                        break;
                    case LocationStockItemBatchNo:
                        result.setTotals(result.getData());
                        break;
                    case StockItemOnly:
                        result.setTotals(result.getData().stream().collect(Collectors.groupingBy(p -> p.getStockItemId()))
                                        .entrySet()
                                        .stream()
                                        .map(p -> {
                                            StockItemInventory stockItemInventory = new StockItemInventory();
                                            StockItemInventory inventory = p.getValue().get(0);
                                            stockItemInventory.setStockItemId(inventory.getStockItemId());
                                            stockItemInventory.setQuantity(p.getValue().stream().map(x -> x.getQuantity()).reduce(BigDecimal.ZERO, BigDecimal::add));
                                            return stockItemInventory;
                                        }).collect(Collectors.toList())
                        );
                        break;
                }
            }

            if (filter.getDoSetQuantityUoM()) {
                boolean uomPriorityIsBigToSmall = GlobalProperties.uomPriorityIsBigToSmall();
                StockItemPackagingUOMSearchFilter uomFilter = new StockItemPackagingUOMSearchFilter();
                uomFilter.setStockItemIds(result.getData().stream().map(p -> p.getStockItemId()).filter(p -> p != null).collect(Collectors.toList()));
                if (filter.dispensing()) {
                    uomFilter.setIncludeDispensingUnit(true);
                }
                if (!uomFilter.getStockItemIds().isEmpty()) {
                    Map<Integer, List<StockItemPackagingUOMDTO>> uoms = findStockItemPackagingUOMs(uomFilter).getData().stream().collect(Collectors.groupingBy(StockItemPackagingUOMDTO::getStockItemId));
                    if (!uoms.isEmpty()) {

                        for (Map.Entry<Integer, List<StockItemPackagingUOMDTO>> entry : uoms.entrySet()) {
                            // When dispensing, trim non-dispensing units if the stock item has a unit for dispensing already setup.
                            if (filter.dispensing()) {
                                Optional<StockItemPackagingUOMDTO> dispensingUnit = entry.getValue().stream().filter(p -> p.getIsDispensingUnit() && p.getStockItemDispensingUnitId() != null).findFirst();
                                if (dispensingUnit.isPresent()) {
                                    // remove the rest of other units and leave only the dispensing unit.
                                    entry.getValue().removeIf(p -> !p.getId().equals(dispensingUnit.get().getId()));
                                    // override the UoM name with the dispensing name.
                                    dispensingUnit.get().setPackagingUomName(dispensingUnit.get().getStockItemDispensingUnitName());
                                }
                            }

                            for (StockItemInventory stockItemInventory : result.getData().stream().filter(p -> p.getStockItemId().equals(entry.getKey())).collect(Collectors.toList())) {
                                List<StockItemPackagingUOMDTO> uomList = entry.getValue();
                                StockItemPackagingUOMDTO preferredUoM = getPreferredPackagingUoM(stockItemInventory.getQuantity(), uomList,false, uomPriorityIsBigToSmall, null);
                                stockItemInventory.setQuantity(stockItemInventory.getQuantity().divide(preferredUoM.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
                                stockItemInventory.setQuantityUoM(preferredUoM.getPackagingUomName());
                                stockItemInventory.setQuantityUoMUuid(preferredUoM.getUuid());
                                stockItemInventory.setQuantityFactor(preferredUoM.getFactor());
                            }

                            if (filter.getTotalBy() != null && filter.getTotalBy() != StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItemBatchNo) {
                                for (StockItemInventory stockItemInventory : result.getTotals().stream().filter(p -> p.getStockItemId().equals(entry.getKey())).collect(Collectors.toList())) {
                                    List<StockItemPackagingUOMDTO> uomList = entry.getValue();
                                    StockItemPackagingUOMDTO preferredUoM = getPreferredPackagingUoM(stockItemInventory.getQuantity(), uomList, false, uomPriorityIsBigToSmall, null);
                                    stockItemInventory.setQuantity(stockItemInventory.getQuantity().divide(preferredUoM.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
                                    stockItemInventory.setQuantityUoM(preferredUoM.getPackagingUomName());
                                    stockItemInventory.setQuantityUoMUuid(preferredUoM.getUuid());
                                    stockItemInventory.setQuantityFactor(preferredUoM.getFactor());
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public Result<StockItemTransactionDTO> findStockItemTransactions(StockItemTransactionSearchFilter filter) {
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters = getRecordPrivilegeFilters(
                Context.getAuthenticatedUser(), null, null, Privileges.APP_STOCKMANAGEMENT_STOCKITEMS);
        if (recordPrivilegeFilters == null || recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);
        Result<StockItemTransactionDTO> result = dao.findStockItemTransactions(filter, recordPrivilegeFilters);
        return result;
    }

    public void voidStockItemPackagingUOM(String uuid, String reason, int voidedBy) {
        dao.voidStockItemPackagingUOM(uuid, reason, voidedBy);
    }

    public ImportResult importStockItems(Path file, boolean hasHeader) {
        StockItemImportJob importJob = new StockItemImportJob(file, hasHeader);
        importJob.execute();
        return (ImportResult) importJob.getResult();
    }

    public List<StockItemDTO> getExistingStockItemIds(
            Collection<StockItemSearchFilter.ItemGroupFilter> stockItemFilters) {
        return dao.getExistingStockItemIds(stockItemFilters);
    }

    public List<Drug> getDrugs(Collection<Integer> drugIds) {
        return dao.getDrugs(drugIds);
    }

    public List<Concept> getConcepts(Collection<Integer> conceptIds) {
        return dao.getConcepts(conceptIds);
    }

    public List<StockItem> getStockItems(Collection<Integer> stockItemIds) {
        return dao.getStockItems(stockItemIds);
    }


    public List<StockItemPackagingUOM> getStockItemPackagingUOMs(
            List<StockItemPackagingUOMSearchFilter.ItemGroupFilter> filters) {
        return dao.getStockItemPackagingUOMs(filters);
    }

    public List<StockItem> getStockItemByDrug(Integer drugId) {
        return dao.getStockItemByDrug(drugId);
    }

    public List<StockItem> getStockItemByConcept(Integer conceptId) {
        return dao.getStockItemByConcept(conceptId);
    }

    public StockItemPackagingUOM getStockItemPackagingUOMByConcept(Integer stockItemId, Integer conceptId) {
        return dao.getStockItemPackagingUOMByConcept(stockItemId, conceptId);
    }

    public StockItemPackagingUOM getStockItemPackagingUOMByConcept(Integer stockItemId, String conceptUuid) {
        Concept concept = Context.getConceptService().getConceptByUuid(conceptUuid);
        if (concept == null)
            return null;
        return getStockItemPackagingUOMByConcept(stockItemId, concept.getConceptId());
    }

    public StockItemPackagingUOM getStockItemPackagingUOMByConcept(String stockItemUuid, Integer conceptId) {
        Map<String, Integer> stockItemIds = dao.getStockItemIds(Arrays.asList(stockItemUuid));
        if (stockItemIds.isEmpty())
            return null;
        for (Integer stockItemId : stockItemIds.values()) {
            return getStockItemPackagingUOMByConcept(stockItemId, conceptId);
        }
        return null;
    }

    public StockItemPackagingUOM getStockItemPackagingUOMByConcept(String stockItemUuid, String conceptUuid) {
        Concept concept = Context.getConceptService().getConceptByUuid(conceptUuid);
        if (concept == null)
            return null;
        Map<String, Integer> stockItemIds = dao.getStockItemIds(Arrays.asList(stockItemUuid));
        if (stockItemIds.isEmpty())
            return null;
        for (Integer stockItemId : stockItemIds.values()) {
            return getStockItemPackagingUOMByConcept(stockItemId, concept.getId());
        }
        return null;
    }

    public List<OrderItem> getOrderItemsByOrder(Integer... orderIds) {
        return dao.getOrderItemsByOrder(orderIds);
    }

    public List<OrderItem> getOrderItemsByEncounter(Integer... encounterIds) {
        return dao.getOrderItemsByEncounter(encounterIds);
    }

    public Result<OrderItemDTO> findOrderItems(OrderItemSearchFilter filter) {
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters = getRecordPrivilegeFilters(
                Context.getAuthenticatedUser(), null, null, Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY);
        if (recordPrivilegeFilters == null || recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);
        return dao.findOrderItems(filter, recordPrivilegeFilters);
    }

    public Result<OrderItemDTO> findOrderItems(OrderItemSearchFilter filter,
                                               HashSet<RecordPrivilegeFilter> recordPrivilegeFilters) {
        if (recordPrivilegeFilters != null && recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);
        return dao.findOrderItems(filter, recordPrivilegeFilters);
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return dao.saveOrderItem(orderItem);
    }

    public StockRule getStockRuleByUuid(String uuid) {
        return dao.getStockRuleByUuid(uuid);
    }

    public StockRuleDTO saveStockRule(StockRuleDTO stockRuleDTO) {
        StockRule stockRule = null;
        boolean isNew = false;
        if (stockRuleDTO.getUuid() != null) {
            stockRule = dao.getStockRuleByUuid(stockRuleDTO.getUuid());
            if (stockRule == null) {
                invalidRequest("stockmanagement.stockrule.notexists");
            }
            if (!userHasStockManagementPrivilege(Context.getAuthenticatedUser(), stockRule.getLocation(), null,
                    Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)) {
                invalidRequest("stockmanagement.stockrule.nopermissiontoupdate");
            }
            stockRule.setChangedBy(Context.getAuthenticatedUser());
            stockRule.setDateChanged(new Date());

        } else {
            isNew = true;
            stockRule = new StockRule();
            stockRule.setCreator(Context.getAuthenticatedUser());
            stockRule.setDateCreated(new Date());
            stockRule.setStockItem(dao.getStockItemByUuid(stockRuleDTO.getStockItemUuid()));
            stockRule.setLocation(Context.getLocationService().getLocationByUuid(stockRuleDTO.getLocationUuid()));
            if (stockRule.getLocation() == null || !userHasStockManagementPrivilege(Context.getAuthenticatedUser(),
                    stockRule.getLocation(), null, Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE)) {
                invalidRequest("stockmanagement.stockrule.nopermissiontoupdate");
            }
        }
        stockRule.setName(stockRuleDTO.getName());
        stockRule.setDescription(stockRuleDTO.getDescription());
        stockRule.setQuantity(stockRuleDTO.getQuantity());
        stockRule.setStockItemPackagingUOM(
                dao.getStockItemPackagingUOMByUuid(stockRuleDTO.getStockItemPackagingUOMUuid()));
        stockRule.setEnabled(stockRuleDTO.getEnabled());
        if (!isNew && stockRule.getLastEvaluation() != null && stockRuleDTO.getEvaluationFrequency() != null) {
            if (!stockRuleDTO.getEvaluationFrequency().equals(stockRule.getEvaluationFrequency())) {
                int minutesToAdd = 0;
                if (stockRuleDTO.getEvaluationFrequency() < Integer.MIN_VALUE) {
                    minutesToAdd = Integer.MIN_VALUE;
                } else if (stockRuleDTO.getEvaluationFrequency() > Integer.MAX_VALUE) {
                    minutesToAdd = Integer.MAX_VALUE;
                } else {
                    minutesToAdd = Math.toIntExact(stockRuleDTO.getEvaluationFrequency().longValue());
                }
                stockRule.setNextEvaluation(DateUtils.addMinutes(stockRuleDTO.getLastEvaluation(), minutesToAdd));
            }
        }
        stockRule.setEvaluationFrequency(stockRuleDTO.getEvaluationFrequency());
        stockRule.setActionFrequency(stockRuleDTO.getActionFrequency());

        stockRule.setAlertRole(StringUtils.isBlank(stockRuleDTO.getAlertRole()) ? null : stockRuleDTO.getAlertRole());
        stockRule.setMailRole(StringUtils.isBlank(stockRuleDTO.getMailRole()) ? null : stockRuleDTO.getMailRole());
        stockRule.setEnableDescendants(stockRuleDTO.getEnableDescendants() == null ? false : stockRuleDTO.getEnableDescendants());

        stockRule = dao.saveStockRule(stockRule);

        StockRuleSearchFilter stockRuleSearchFilter = new StockRuleSearchFilter();
        stockRuleSearchFilter.setId(stockRule.getId());
        Result<StockRuleDTO> result = dao.findStockRules(stockRuleSearchFilter, null);
        if (!result.getData().isEmpty()) {
            return result.getData().get(0);
        }
        return null;
    }

    public Result<StockRuleDTO> findStockRules(StockRuleSearchFilter filter) {
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters = getRecordPrivilegeFilters(
                Context.getAuthenticatedUser(), null, null, Privileges.APP_STOCKMANAGEMENT_STOCKITEMS);
        if (recordPrivilegeFilters == null || recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);
        return findStockRules(filter, recordPrivilegeFilters);
    }

    public Result<StockRuleDTO> findStockRules(StockRuleSearchFilter filter,
                                               HashSet<RecordPrivilegeFilter> recordPrivilegeFilters) {
        if (recordPrivilegeFilters != null && recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);
        Result<StockRuleDTO> result = dao.findStockRules(filter, recordPrivilegeFilters);
        if (!result.getData().isEmpty()) {
            List<UserPersonNameDTO> personNames = dao.getPersonNameByUserIds(result.getData().stream()
                    .map(p -> p.getCreator()).filter(p -> p != null).distinct().collect(Collectors.toList()));
            for (StockRuleDTO stockRule : result.getData()) {
                if (stockRule.getCreator() != null) {
                    Optional<UserPersonNameDTO> userPersonNameDTO = personNames.stream()
                            .filter(p -> p.getUserId().equals(stockRule.getCreator())).findFirst();
                    if (userPersonNameDTO.isPresent()) {
                        stockRule.setCreatorFamilyName(userPersonNameDTO.get().getFamilyName());
                        stockRule.setCreatorGivenName(userPersonNameDTO.get().getGivenName());
                    }
                }
            }
        }
        return result;
    }

    public void voidStockRules(List<String> stockRuleUuids, String reason, int voidedBy) {
        StockRuleSearchFilter stockRuleSearchFilter = new StockRuleSearchFilter();
        stockRuleSearchFilter.setUuids(stockRuleUuids);
        Result<StockRuleDTO> result = findStockRules(stockRuleSearchFilter);
        if (result.getData().isEmpty()) {
            return;
        }
        dao.voidStockRules(result.getData().stream().map(p -> p.getUuid()).collect(Collectors.toList()), reason, voidedBy);
    }

    public List<StockRuleNotificationUser> getDueStockRules(Integer lastStockRuleId, int limit) {
        return dao.getDueStockRules(lastStockRuleId, limit);
    }

    public List<Integer> getActiveUsersAssignedForScope(Integer locationId, List<String> roles) {
        return dao.getActiveUsersAssignedForScope(locationId, roles);
    }

    public void updateStockBatchExpiryNotificationDate(Collection<Integer> stockBatchIds, Date notificationDate) {
        dao.updateStockBatchExpiryNotificationDate(stockBatchIds, notificationDate);
    }

    public void updateStockRuleJobNextEvaluationDate(List<Integer> stockRuleIds, Date nextEvaluationDate) {
        dao.updateStockRuleJobNextEvaluationDate(stockRuleIds, nextEvaluationDate);
    }

    public void updateStockRuleJobNextActionDate(List<Integer> stockRuleIds, Date nextEvaluationDate) {
        dao.updateStockRuleJobNextActionDate(stockRuleIds, nextEvaluationDate);
    }

    public void setStockItemCurrentBalanceWithDescendants(List<StockRuleCurrentQuantity> stockRuleCurrentQuantities) {
        dao.setStockItemCurrentBalanceWithDescendants(stockRuleCurrentQuantities);
    }


    public void setStockItemCurrentBalanceWithoutDescendants(List<StockRuleCurrentQuantity> stockRuleCurrentQuantities) {
        dao.setStockItemCurrentBalanceWithoutDescendants(stockRuleCurrentQuantities);
    }

    public Map<Integer, String> getStockItemNames(List<Integer> stockItemIds) {
        return dao.getStockItemNames(stockItemIds);
    }

    public Map<Integer, String> getConceptNames(List<Integer> conceptIds) {
        return dao.getConceptNames(conceptIds);
    }

    public Map<Integer, String> getLocationNames(List<Integer> locationIds) {
        return dao.getLocationNames(locationIds);
    }

    public String getHealthCenterName() {
        String healthCenterName = GlobalProperties.getHealthCenterName();
        if (StringUtils.isBlank(healthCenterName)) {
            List<Location> locations = Context.getLocationService().getAllLocations();
            Optional<Pair<String, Long>> topLocation = locations.stream().filter(p -> p.getParentLocation() == null).map(p -> {
                long count = locations.stream()
                        .filter(x -> x.getParentLocation() != null && x.getParentLocation().getUuid().equals(p.getUuid()))
                        .count();
                return new Pair<String, Long>(p.getName(), count);
            }).sorted((p1, p2) -> p2.getValue2().compareTo(p1.getValue2()))
                    .findFirst();
            if (topLocation.isPresent()) {
                healthCenterName = topLocation.get().getValue1();
            } else {
                healthCenterName = "NOT SET";
            }
        }
        return healthCenterName;
    }

    public void runStockRuleEvaluationJob() {
        StockRuleEvaluationJob stockRuleEvaluationJob = new StockRuleEvaluationJob();
        stockRuleEvaluationJob.execute();
    }

    public void runStockBatchExpiryNoticationJob() {
        StockBatchExpiryJob stockBatchExpiryJob = new StockBatchExpiryJob();
        stockBatchExpiryJob.execute();
    }

    public List<StockBatchDTO> getExpiringStockBatchesDueForNotification(Integer defaultExpiryNotificationNoticePeriod) {
        return dao.getExpiringStockBatchesDueForNotification(defaultExpiryNotificationNoticePeriod);
    }

    public void deleteLocation(String uuid) {
        try {
            deleteLocationInternal(uuid);
        } catch (Exception exception) {
            invalidRequest("stockmanagement.location.inuse");
        }
    }

    public void deleteLocationInternal(String uuid) {
        LocationService locationService = Context.getLocationService();
        Location location = locationService.getLocationByUuid(uuid);
        if (location == null) {
            invalidRequest("stockmanagement.location.notexists");
        }

        if (location.getChildLocations() != null && location.getChildLocations().size() > 0) {
            invalidRequest("stockmanagement.location.childlocationsexist");
        }

        Set<LocationAttribute> locationAttributes = location.getAttributes();
        Set<LocationTag> locationTags = location.getTags();

        // Remove the part record
        Party party = dao.getPartyByLocation(location);
        if (party != null) {
            dao.deleteParty(party);
        }

        // remove the location tree notes related to this location
        dao.deleteLocationTreeNodes(location.getId());

        if (locationTags != null) {
            locationTags.stream().collect(Collectors.toList()).forEach(p -> location.removeTag(p));
            locationService.saveLocation(location);
        }

        if (locationAttributes != null && !locationAttributes.isEmpty()) {
            dao.deleteLocationAttributes(locationAttributes.stream().map(p -> p.getLocationAttributeId()).collect(Collectors.toList()));
        }

        dao.deleteLocation(location.getId());
    }

    @SuppressWarnings({ "unchecked" })
    public void sendStockOperationNotification(String stockOperationUuid, StockOperationAction.Action action, String actionReason, Integer actionByUserId) {
        StockOperationSearchFilter filter = new StockOperationSearchFilter();
        filter.setStockOperationUuid(stockOperationUuid);
        Result<StockOperationDTO> result = Context.getService(StockManagementService.class).findStockOperations(filter, null);
        StockOperationDTO stockOperationDTO = result.getData().isEmpty() ? null : result.getData().get(0);
        if (stockOperationDTO == null) return;

        String emailToNotity = GlobalProperties.getStockOperationNotificationEmail();
        if(!org.openmrs.module.stockmanagement.api.utils.StringUtils.isValidEmail(emailToNotity)){
            emailToNotity=null;
        }
        String roleToNotify = GlobalProperties.getStockOperationNotificationRole();
        if (StringUtils.isBlank(emailToNotity) && StringUtils.isBlank(roleToNotify)) {
            log.info(String.format("Stock Operation %1$s Action %2$s not sent. Email and role notification settings not set", stockOperationDTO.getOperationNumber(), action.toString()));
            return;
        }

        String actionName = StockOperationAction.getActionName(action);
        String userName = "System";
        User user = null;
        if (actionByUserId != null) {
             user = Context.getUserService().getUser(actionByUserId);

            if (user == null) {
                userName = "User ID " + actionByUserId.toString();
            } else {
                userName = user.getDisplayString();
            }
        }

        List<User> usersToNotify = null;
        if (StringUtils.isNotBlank(roleToNotify)) {
            Role role = Context.getUserService().getRole(roleToNotify);
            if (role == null) {
                log.info(String.format("Stock Operation %1$s Action %2$s not sent. Role not found", stockOperationDTO.getOperationNumber(), action.toString()));
            } else {

                usersToNotify = Context.getUserService().getUsersByRole(role);
                if (usersToNotify.isEmpty()) {
                    log.info(String.format("Stock Operation %1$s Action %2$s not sent. No users with role found", stockOperationDTO.getOperationNumber(), action.toString()));
                } else {
                    Alert alert = new Alert();
                    alert.setDateCreated(new Date());
                    alert.setCreator(user);
                    alert.setDateToExpire(DateUtils.addDays(new Date(), 7));
                    alert.setText(String.format(Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.alertmsg"),
                            stockOperationDTO.getAtLocationName(),
                            stockOperationDTO.getOperationTypeName(),
                            stockOperationDTO.getOperationNumber(),
                            actionName,
                            userName
                    ));
                    usersToNotify.forEach(p -> alert.addRecipient(p));
                    Context.getAlertService().saveAlert(alert);
                }
            }
        }
        List<String> emailsToNotify = null;
        if(emailToNotity != null && !emailToNotity.isEmpty()){
            emailsToNotify = usersToNotify.stream().map(u->{
                String emailAddress = getUserEmailAddress(u);
                if(StringUtils.isBlank(emailAddress)){
                    return null;
                }
                return emailAddress;
            }).filter(p-> p != null).collect(Collectors.toList());
        }
        if( emailToNotity == null && (emailsToNotify == null || emailsToNotify.isEmpty())){
            log.info(String.format("Stock Operation %1$s Action %2$s not sent. No users to notify via email", stockOperationDTO.getOperationNumber(), action.toString()));
            return;
        }

        if(emailsToNotify == null){
            emailsToNotify=new Vector<>();
        }
        if(emailToNotity != null){
            emailsToNotify.add(emailToNotity);
        }

        Template template = null;
        try {
            List<Template> templates = Context.getMessageService().getTemplatesByName("STOCK_MGMT_OPERATION_ACTION");
            if (templates.isEmpty()) {
                log.debug("Template STOCK_MGMT_OPERATION_ACTION not found");
                return;
            }
            template = templates.get(0);
        } catch (Exception exception) {
            log.error(exception);
            return;
        }

        if (!SmtpUtil.hasSmptHostSetup()) {
            log.info(String.format("Stock Operation %1$s Action %2$s not sent. No smtp settings are not setup", stockOperationDTO.getOperationNumber(), action.toString()));
            return;
        }

        Session session = SmtpUtil.getSession();
        String healthCenterName = getHealthCenterName();

        StringBuilder basicInfo = new StringBuilder();
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        if(stockOperationDTO.getSourceUuid() != null) {
            basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                    messageSourceService.getMessage("stockmanagement.stockoperation.notification.source"),
                    stockOperationDTO.getSourceName()
            ));
        }
        if(stockOperationDTO.getDestinationUuid() != null) {
            basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                    messageSourceService.getMessage("stockmanagement.stockoperation.notification.destination"),
                    stockOperationDTO.getDestinationName()
                    ));
        }
        basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                messageSourceService.getMessage("stockmanagement.stockoperation.notification.operationdate"),
                DateUtil.formatDDMMMyyyy(stockOperationDTO.getOperationDate())));
        basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                messageSourceService.getMessage("stockmanagement.stockoperation.notification.responsibleperson"),
                stockOperationDTO.getResponsiblePersonFamilyName() != null ? String.format("%1$s %2$s", stockOperationDTO.getResponsiblePersonFamilyName(), stockOperationDTO.getResponsiblePersonGivenName()) : stockOperationDTO.getResponsiblePersonOther()));
        basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                messageSourceService.getMessage("stockmanagement.stockoperation.notification.remarks"),
                stockOperationDTO.getRemarks() != null ? StringEscapeUtils.escapeHtml(stockOperationDTO.getRemarks()): "&nbsp;"
                ));
        if(action == StockOperationAction.Action.RETURN){
            basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                    messageSourceService.getMessage("stockmanagement.stockoperation.notification.returnreason"),
                    actionReason != null ? StringEscapeUtils.escapeHtml(actionReason): "&nbsp;"
            ));
        }else if(action == StockOperationAction.Action.REJECT){
            basicInfo.append(String.format("<tr><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'><b>%1$s:</b></td><td style='padding: 0.2rem 0.5rem; font-size: 95%%;'>%2$s</td>",
                    messageSourceService.getMessage("stockmanagement.stockoperation.notification.rejectionreason"),
                    actionReason != null ? StringEscapeUtils.escapeHtml(actionReason): "&nbsp;"
            ));
        }

        String body = template.getTemplate()
                .replace("%LOCATION%", stockOperationDTO.getAtLocationName())
                .replace("%OPERATION_TYPE%", stockOperationDTO.getOperationTypeName())
                .replace("%OPERATION_REF%", stockOperationDTO.getOperationNumber())
                .replace("%OPERATION_ACTION%", actionName)
                .replace("%ACTION_BY%", userName)
                .replace("%OPERATION_BASIC_INFO%", basicInfo.toString())
                .replace("%CENTER_NAME%", healthCenterName);

        String subject = template.getSubject()
                .replace("%LOCATION%", stockOperationDTO.getAtLocationName())
                .replace("%OPERATION_TYPE%", stockOperationDTO.getOperationTypeName())
                .replace("%OPERATION_REF%", stockOperationDTO.getOperationNumber())
                .replace("%OPERATION_ACTION%", actionName)
                .replace("%ACTION_BY%", userName)
                .replace("%OPERATION_BASIC_INFO%", basicInfo.toString())
                .replace("%CENTER_NAME%", healthCenterName);

        try {
            SmtpUtil.sendEmail(subject, body, session, emailsToNotify.toArray(new String[emailsToNotify.size()]));
        } catch (Exception exception) {
            log.error(exception);
        }
    }

    public BatchJobDTO saveBatchJob(BatchJobDTO batchJobDTO){
        Location locationScope = null;
        if(!StringUtils.isBlank(batchJobDTO.getLocationScopeUuid())) {
            locationScope = Context.getLocationService().getLocationByUuid(batchJobDTO.getLocationScopeUuid());
            if(locationScope == null){
                invalidRequest(Context.getMessageSourceService().getMessage("stockmanagement.batchjob.fieldvaluenotexist"), "report");
            }
        }

        BatchJobSearchFilter batchJobSearchFilter=new BatchJobSearchFilter();
        batchJobSearchFilter.setBatchJobType(batchJobDTO.getBatchJobType());
        batchJobSearchFilter.setParameters(batchJobDTO.getParameters());
        batchJobSearchFilter.setPrivilegeScope(batchJobDTO.getPrivilegeScope());
        if(locationScope != null) {
            batchJobSearchFilter.setLocationScopeIds(Arrays.asList(locationScope.getId()));
        }
        batchJobSearchFilter.setBatchJobStatus(Arrays.asList(BatchJobStatus.Pending, BatchJobStatus.Running));
        Result<BatchJobDTO> pendingSimilarJobs = findBatchJobs(batchJobSearchFilter, null);
        BatchJob batchJob = null;
        if(!pendingSimilarJobs.getData().isEmpty()){
            batchJob = dao.getBatchJobById(pendingSimilarJobs.getData().get(0).getId());
            if(batchJob.getBatchJobOwners() == null){
                batchJob.setBatchJobOwners(new HashSet<>());
            }
            Integer currentUserId = Context.getAuthenticatedUser().getId();
            if(!batchJob.getBatchJobOwners().stream().anyMatch(p->p.getOwner().getId().equals(currentUserId))){
                BatchJobOwner batchJobOwner = new BatchJobOwner();
                batchJobOwner.setOwner(Context.getAuthenticatedUser());
                batchJob.addBatchJobOwner(batchJobOwner);
            }
        }
        else {

            batchJob = new BatchJob();
            batchJob.setCreator(Context.getAuthenticatedUser());
            batchJob.setDateCreated(new Date());
            batchJob.setBatchJobType(batchJobDTO.getBatchJobType());
            batchJob.setStatus(BatchJobStatus.Pending);
            batchJob.setDescription(batchJobDTO.getDescription());
            batchJob.setExpiration(DateUtils.addMinutes(new Date(), GlobalProperties.getBatchJobExpiryInMinutes()));
            batchJob.setParameters(batchJobDTO.getParameters());
            batchJob.setPrivilegeScope(batchJobDTO.getPrivilegeScope());

            if (locationScope != null) {
                batchJob.setLocationScope(locationScope);
            }

            BatchJobOwner batchJobOwner = new BatchJobOwner();
            batchJobOwner.setOwner(Context.getAuthenticatedUser());
            batchJob.addBatchJobOwner(batchJobOwner);
        }

        dao.saveBatchJob(batchJob);
        AsyncTasksBatchJob.queueBatchJob(batchJob);

        batchJobSearchFilter = new BatchJobSearchFilter();
        batchJobSearchFilter.setBatchJobUuids(Arrays.asList(batchJob.getUuid()));
        Result<BatchJobDTO> jobs = findBatchJobs(batchJobSearchFilter, null);
        return jobs.getData().isEmpty() ? null : jobs.getData().get(0);
    }

    public Result<BatchJobDTO> findBatchJobs(BatchJobSearchFilter filter){
        HashSet<RecordPrivilegeFilter> recordPrivilegeFilters = getRecordPrivilegeFilters(
                Context.getAuthenticatedUser(), null, null, Privileges.APP_STOCKMANAGEMENT_REPORTS);
        if (recordPrivilegeFilters == null || recordPrivilegeFilters.isEmpty())
            return new Result<>(new ArrayList<>(), 0);
        return findBatchJobs(filter, recordPrivilegeFilters);
    }

    public Result<BatchJobDTO> findBatchJobs(BatchJobSearchFilter batchJobSearchFilter,HashSet<RecordPrivilegeFilter> recordPrivilegeFilters){
        return dao.findBatchJobs(batchJobSearchFilter, recordPrivilegeFilters);
    }

    public void failBatchJob(String batchJobUuid, String reason){
        BatchJob batchJob = dao.getBatchJobByUuid(batchJobUuid);
        if(batchJob == null) return;

        if(!batchJob.getStatus().equals(BatchJobStatus.Running) && !batchJob.getStatus().equals(BatchJobStatus.Pending)){
            invalidRequest("stockmanagement.batchjob.notcancellable");
        }
        if(reason != null && reason.length() > 2500){
            reason = reason.substring(0,2500-1);
        }
        batchJob.setExitMessage(reason);
        batchJob.setStatus(BatchJobStatus.Failed);
        batchJob.setDateChanged(new Date());
        batchJob.setChangedBy(Context.getAuthenticatedUser());
        dao.saveBatchJob(batchJob);
    }


    public void cancelBatchJob(String batchJobUuid, String reason){
        BatchJob batchJob = dao.getBatchJobByUuid(batchJobUuid);
        if(batchJob == null) return;

        if(!batchJob.getStatus().equals(BatchJobStatus.Running) && !batchJob.getStatus().equals(BatchJobStatus.Pending)){
            invalidRequest("stockmanagement.batchjob.notcancellable");
        }

        batchJob.setStatus(BatchJobStatus.Cancelled);
        batchJob.setCancelReason(reason);
        batchJob.setCancelledBy(Context.getAuthenticatedUser());
        batchJob.setCancelledDate(new Date());
        batchJob.setDateChanged(new Date());
        batchJob.setChangedBy(Context.getAuthenticatedUser());
        dao.saveBatchJob(batchJob);

        AsyncTasksBatchJob.stopBatchJob(batchJob);
    }

    public List<Report> getReports(){
        return Report.getAllReports();
    }

    public BatchJob getNextActiveBatchJob(){
        return dao.getNextActiveBatchJob();
    }

    public BatchJob getBatchJobByUuid(String batchJobUuid){
        return dao.getBatchJobByUuid(batchJobUuid);
    }

    public void saveBatchJob(BatchJob batchJob){
        dao.saveBatchJob(batchJob);
    }

    public Result<StockOperationLineItem> findStockOperationLineItems(StockOperationLineItemFilter filter){
        return dao.findStockOperationLineItems(filter);
    }

    public void updateBatchJobRunning(String batchJobUuid){
        BatchJob batchJob = dao.getBatchJobByUuid(batchJobUuid);
        if(batchJob == null) return;
        batchJob.setStatus(BatchJobStatus.Running);
        if(batchJob.getStartTime() == null) {
            batchJob.setStartTime(new Date());
        }
        batchJob.setDateChanged(new Date());
        batchJob.setChangedBy(Context.getAuthenticatedUser());
        dao.saveBatchJob(batchJob);
    }

    public void expireBatchJob(String batchJobUuid, String reason){
        BatchJob batchJob = dao.getBatchJobByUuid(batchJobUuid);
        if(batchJob == null) return;
        batchJob.setStatus(BatchJobStatus.Expired);
        if(batchJob.getStartTime() != null){
            batchJob.setEndTime(new Date());
        }
        batchJob.setExitMessage(reason);
        batchJob.setDateChanged(new Date());
        batchJob.setChangedBy(Context.getAuthenticatedUser());
        dao.saveBatchJob(batchJob);
    }

    public void updateBatchJobExecutionState(String batchJobUuid, String  executionState){
        BatchJob batchJob = dao.getBatchJobByUuid(batchJobUuid);
        if(batchJob == null) return;
        batchJob.setExecutionState(executionState);
        batchJob.setDateChanged(new Date());
        batchJob.setChangedBy(Context.getAuthenticatedUser());
        dao.saveBatchJob(batchJob);
    }


    public String getUserEmailAddress(User user){
        String emailAddress = dao.getUserEmail(user.getId());
        if (!org.openmrs.module.stockmanagement.api.utils.StringUtils.isValidEmail(emailAddress)) {
            emailAddress = user.getUserProperty(OpenmrsConstants.USER_PROPERTY_NOTIFICATION_ADDRESS);
            if (!org.openmrs.module.stockmanagement.api.utils.StringUtils.isValidEmail(emailAddress)) {
                emailAddress = user.getUsername();
                if (!org.openmrs.module.stockmanagement.api.utils.StringUtils.isValidEmail(emailAddress)) {
                    return  null;
                }
            }
        }
        return emailAddress;
    }

    public Result<StockBatchLineItem> getExpiringStockBatchList(StockExpiryFilter filter){
        return dao.getExpiringStockBatchList(filter);
    }

    public <T extends StockItemInventory> void getStockInventory(StockItemInventorySearchFilter filter, HashSet<RecordPrivilegeFilter> recordPrivilegeFilters, Function<T, Boolean> consumer, Class<T> resultClass){
        dao.getStockInventory(filter, recordPrivilegeFilters, consumer, resultClass);
    }

    public void setStockItemInformation(List<StockItemInventory> reportStockItemInventories){
        dao.setStockItemInformation(reportStockItemInventories);
    }

    public Result<DispensingLineItem> findDispensingLineItems(DispensingLineFilter filter){
        return dao.findDispensingLineItems(filter);
    }

    public Result<PrescriptionLineItem> findPrescriptionLineItems(PrescriptionLineFilter filter){
        return dao.findPrescriptionLineItems(filter);
    }

    public Result<StockItemInventory> getLeastMovingStockInventory(StockItemInventorySearchFilter filter){
        return dao.getLeastMovingStockInventory(filter);
    }

    public Result<StockItemInventory> getMostMovingStockInventory(StockItemInventorySearchFilter filter){
        return dao.getMostMovingStockInventory(filter);
    }

    public void getStockInventoryForecastData(StockItemInventorySearchFilter filter, Function<Object[], Boolean> consumer){
        dao.getStockInventoryForecastData(filter, consumer);
    }

    public void getStockInventoryExpiryForecastData(StockItemInventorySearchFilter filter, Function<Object[], Boolean> consumer){
        dao.getStockInventoryExpiryForecastData(filter, consumer);
    }

    public List<BatchJob> getExpiredBatchJobs(){
        return dao.getExpiredBatchJobs();
    }

    public void deleteBatchJob(BatchJob batchJob){
        dao.deleteBatchJob(batchJob);
    }

    private StockItemPackagingUOMDTO getPreferredPackagingUoM(BigDecimal value, List<StockItemPackagingUOMDTO> uoms, boolean isDispensing, boolean uomPriorityIsBigToSmall, Integer preferredStockItemPackagingUOMId){
        if(isDispensing || !uomPriorityIsBigToSmall) {
            // When dispensing or the priority is small to big, we order the units from small pack size to biggest
            uoms.sort(Comparator.comparing(StockItemPackagingUOMDTO::getIsDefaultStockOperationsUoM).reversed().thenComparing(StockItemPackagingUOMDTO::getFactor));
        }else{
            // When not dispensing or the priority is big to small, we order the units from small pack size to biggest
            uoms.sort(Comparator.comparing(StockItemPackagingUOMDTO::getIsDefaultStockOperationsUoM).reversed().thenComparing(Comparator.comparing(StockItemPackagingUOMDTO::getFactor).reversed()));
        }
        StockItemPackagingUOMDTO foundUoM = null;
        boolean checkedPreferred = false;
        boolean checkPreferred = preferredStockItemPackagingUOMId != null;
        boolean thisIsThePreferred = false;
        for(StockItemPackagingUOMDTO uom : uoms){
            if(checkPreferred){
                thisIsThePreferred = preferredStockItemPackagingUOMId.equals(uom.getId());
            }
            if(value.divide(uom.getFactor(), 5, BigDecimal.ROUND_HALF_EVEN).abs().compareTo(BigDecimal.valueOf(1)) >= 0) {
                // If we have no preferred, we return the first uom
                if(!checkPreferred) return uom;
                // If this is the preferred, return it immediately
                if(thisIsThePreferred) return uom;
                // If we already checked the preferred and it did not quality, return the first uom that was found or the current one
                if(checkedPreferred) return foundUoM != null ? foundUoM : uom;
                if(foundUoM == null){
                    foundUoM = uom;
                }
            }
            if(thisIsThePreferred){
                checkedPreferred=true;
            }
        }
        return (checkedPreferred && foundUoM != null) ? foundUoM : (uoms.isEmpty() ? null : uoms.get(0));
    }

    public Map<Integer, Boolean> checkStockBatchHasTransactionsAfterOperation(Integer stockOperationId, List<Integer> stockBatchIds){
        return dao.checkStockBatchHasTransactionsAfterOperation(stockOperationId, stockBatchIds);
    }

    public StockOperationBatchNumbersDTO saveStockOperationBatchNumbers(StockOperationBatchNumbersDTO stockOperationBatchNumbers){
        if(stockOperationBatchNumbers == null) {
            invalidRequest("stockmanagement.stockoperation.updatebatchnumbersnotset");
        }

        if(StringUtils.isBlank(stockOperationBatchNumbers.getUuid())){
            invalidRequest("stockmanagement.stockoperation.updatebatchnumbersuuidnotset");
        }

        if(stockOperationBatchNumbers.getBatchNumbers() == null || stockOperationBatchNumbers.getBatchNumbers().isEmpty()){
            invalidRequest("stockmanagement.stockoperation.updatebatchnumbersnosnotset");
        }
        synchronized (STOCK_OPERATION_PROCESSING_LOCK) {

            StockOperationSearchFilter filter = new StockOperationSearchFilter();
            filter.setStockOperationUuid(stockOperationBatchNumbers.getUuid());
            Result<StockOperationDTO> result = findStockOperations(filter);
            StockOperationDTO stockOperationDTO = result.getData().isEmpty() ? null : result.getData().get(0);
            if (stockOperationDTO == null) {
                invalidRequest("stockmanagement.stockoperation.notfound");
            }

            StockOperation stockOperation = dao.getStockOperationByUuid(stockOperationDTO.getUuid());
            StockOperationType stockOperationType = stockOperation.getStockOperationType();
            if (!stockOperationType.requiresActualBatchInformation()) {
                invalidRequest("stockmanagement.stockoperation.updatebatchnumbersnotsupported");
            }

            if (!stockOperationDTO.canUpdateBatchInformation(stockOperationType)) {
                invalidRequest("stockmanagement.stockoperation.updatebatchnumbersdenied");
            }

            HashSet<PrivilegeScope> privilegeScopes = getPrivilegeScopes(Context.getAuthenticatedUser(),
                    stockOperation.getAtLocation(), stockOperationType,
                    Arrays.asList(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
            );

            if (privilegeScopes.isEmpty()) {
                invalidRequest("stockmanagement.stockoperation.updatebatchnumbersaccessdenied");
            }

            Set<StockOperationItem> stockOperationItems = stockOperation.getStockOperationItems();
            Map<Integer, Boolean> stockBatchHasTransactions = checkStockBatchHasTransactionsAfterOperation(stockOperationDTO.getId(), stockOperationItems.stream().filter(p -> p.getStockBatch() != null).map(p -> p.getStockBatch().getId()).distinct().collect(Collectors.toList()));
            HashMap<String, StockBatch> stockBatchMapping = new HashMap<>();
            List<StockBatch> newStockBatches = new ArrayList<>();
            Integer index = 0;

            List<StockOperationItem> stockOperationItemsToUpdate = new ArrayList<>();
            for (StockOperationBatchNumbersDTO.StockOperationItemBatchNumber batchNumber : stockOperationBatchNumbers.getBatchNumbers()) {
                index++;
                if (StringUtils.isBlank(batchNumber.getBatchNo())) {
                    invalidRequestWithKey("stockmanagement.stockoperation.batchnorequired", index.toString());
                }

                if (batchNumber.getExpiration() != null && !batchNumber.getExpiration().after(DateUtil.today())) {
                    invalidRequestWithKey("stockmanagement.stockoperation.expirydateinpast", index.toString());
                }

                if (StringUtils.isBlank(batchNumber.getUuid())) {
                    invalidRequestWithKey("stockmanagement.stockoperation.updatebatchnumbersitemrequired", index.toString());
                }

                Optional<StockOperationItem> existingItemDto = stockOperationItems.stream().filter(p -> p.getUuid().equals(batchNumber.getUuid())).findFirst();
                if (!existingItemDto.isPresent()) {
                    invalidRequestWithKey("stockmanagement.stockoperation.updatebatchnumbersvaliditemrequired", index.toString());
                }

                if (existingItemDto.get().getStockItem().getHasExpiration()) {
                    if (batchNumber.getExpiration() == null) {
                        invalidRequestWithKey("stockmanagement.stockoperation.expirydaterequired", index.toString());
                    }
                }

                if (stockBatchHasTransactions.containsKey(existingItemDto.get().getStockBatch().getId())) {
                    invalidRequestWithKey("stockmanagement.stockoperation.updatebatchnumbersalreadyinuse", existingItemDto.get().getStockBatch().getBatchNo());
                }

                StockItem stockItem = existingItemDto.get().getStockItem();
                StockBatch stockBatch = findStockBatch(stockItem, batchNumber.getBatchNo(), batchNumber.getExpiration());
                if (stockBatch == null) {
                    if (stockBatchHasTransactions.containsKey(existingItemDto.get().getStockBatch().getId())) {
                        invalidRequestWithKey("stockmanagement.stockoperation.updatebatchnumbersalreadyinuse", existingItemDto.get().getStockBatch().getBatchNo());
                    }
                    Optional<StockBatch> newlyAddedStockBatch = newStockBatches
                            .stream().filter(p -> p.getStockItem().getId().equals(stockItem.getId()) &&
                                    batchNumber.getBatchNo().equalsIgnoreCase(p.getBatchNo()) &&
                                    ((stockItem.getHasExpiration() && p.getExpiration().equals(p.getExpiration()))
                                            ||
                                            (!stockItem.getHasExpiration() && p.getExpiration() == null)))
                            .findFirst();
                    if (newlyAddedStockBatch.isPresent()) {
                        stockBatchMapping.putIfAbsent(existingItemDto.get().getUuid(), newlyAddedStockBatch.get());
                    } else {
                        stockBatch = new StockBatch();
                        newStockBatches.add(stockBatch);
                        stockBatch.setStockItem(stockItem);
                        stockBatch.setBatchNo(batchNumber.getBatchNo());
                        if (stockItem.getHasExpiration()) {
                            stockBatch.setExpiration(batchNumber.getExpiration());
                        }
                        stockBatch.setCreator(Context.getAuthenticatedUser());
                        stockBatch.setDateCreated(new Date());
                        stockBatchMapping.putIfAbsent(existingItemDto.get().getUuid(), stockBatch);
                    }
                    stockOperationItemsToUpdate.add(existingItemDto.get());
                } else {
                    if (existingItemDto.get().getStockBatch() == null || !stockBatch.getId().equals(existingItemDto.get().getStockBatch().getId())) {
                        if (stockBatchHasTransactions.containsKey(existingItemDto.get().getStockBatch().getId())) {
                            invalidRequestWithKey("stockmanagement.stockoperation.updatebatchnumbersalreadyinuse", existingItemDto.get().getStockBatch().getBatchNo());
                        }
                        stockBatchMapping.putIfAbsent(existingItemDto.get().getUuid(), stockBatch);
                        stockOperationItemsToUpdate.add(existingItemDto.get());
                    }
                }
            }

            for (StockBatch stockBatch : newStockBatches) {
                dao.saveStockBatch(stockBatch);
            }

            boolean saveStockOperation = false;
            for (StockOperationItem item : stockOperationItemsToUpdate) {
                item.setStockOperation(stockOperation);
                if (stockBatchMapping.containsKey(item.getUuid())) {
                    saveStockOperation = true;
                    StockBatch stockBatch = stockBatchMapping.get(item.getUuid());
                    item.setStockBatch(stockBatch);
                    stockOperation.getReservedTransactions().stream()
                            .filter(p -> p.getStockOperationItem().getId().equals(item.getId()))
                            .forEach(reservedTransaction -> reservedTransaction.setStockBatch(stockBatch));
                    stockOperation.getStockItemTransactions().stream()
                            .filter(p -> p.getStockOperationItem().getId().equals(item.getId()))
                            .forEach(stockItemTransaction -> stockItemTransaction.setStockBatch(stockBatch));
                }
                dao.saveStockOperationItem(item);
            }

            if (saveStockOperation) {
                dao.saveStockOperation(stockOperation);
            }

        }

        return stockOperationBatchNumbers;
    }

    @Override
    public StockItem getStockItemByReference(StockSource stockSource, String code) {
       StockItemReference stockItemReference = dao.getStockItemByReference(stockSource,code);
        if(stockItemReference!=null) {
            return dao.getStockItemByReference(stockSource, code).getStockItem();
        }

        return null;
    }

    @Override
    public StockItemReference saveStockItemReference(StockItemReference stockItemReference) {
        return dao.saveStockItemReference(stockItemReference);
    }

    @Override
    public void voidStockItemReference(String uuid, String reason, Integer userId) {
        dao.voidStockItemReference(uuid, reason, userId);
    }

    @Override
    public StockItemReference getStockItemReferenceByUuid(String uuid) {
        return dao.getStockItemReferenceByUuid(uuid);
    }

    @Override
    public List<StockItemReference> getStockItemReferenceByStockItem(String uuid) {

       StockItem stockItem= getStockItemByUuid(uuid);
        if(stockItem!=null) {
            return dao.getStockItemReferenceByStockItem(stockItem);
        }else {
            return null;
        }
    }
}
