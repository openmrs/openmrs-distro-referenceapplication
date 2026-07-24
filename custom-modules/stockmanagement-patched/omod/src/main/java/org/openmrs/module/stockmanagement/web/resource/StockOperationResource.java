package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.*;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.stream.Collectors;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperation", supportedClass = StockOperationDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationResource extends ResourceBase<StockOperationDTO> {
	
	private Map<String, SimpleObject> permissionCache;
	
	public StockOperationResource() {
	}
	
	@Override
	public StockOperationDTO getByUniqueId(String uniqueId) {
		StockOperationSearchFilter filter = new StockOperationSearchFilter();
		filter.setStockOperationUuid(uniqueId);
		Result<StockOperationDTO> result = getStockManagementService().findStockOperations(filter);
		StockOperationDTO stockOperationDTO = result.getData().isEmpty() ? null : result.getData().get(0);
		if (stockOperationDTO != null && StockOperationType.STOCK_ISSUE.equals(stockOperationDTO.getOperationType())) {
			Result<StockOperationLinkDTO> parents = getStockManagementService().getParentStockOperationLinks(
			    stockOperationDTO.getUuid());
			if (parents.getData() != null && parents.getData().size() > 0) {
				stockOperationDTO.setRequisitionStockOperationUuid(parents.getData().get(0).getParentUuid());
			}
		}
		return stockOperationDTO;
	}
	
	@Override
	protected void delete(StockOperationDTO delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
    protected PageableResult doSearch(RequestContext context) {
        StockOperationSearchFilter filter = new StockOperationSearchFilter();
        filter.setIncludeVoided(context.getIncludeAll());
        filter.setStartIndex(context.getStartIndex());
        filter.setLimit(context.getLimit());
        String param = context.getParameter("q");
        if (StringUtils.isNotBlank(param))
            filter.setSearchText(param);
        StockManagementService stockManagementService = getStockManagementService();
        param = context.getParameter("operationTypeUuid");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<Integer> typeIds = new ArrayList<>();
            for (String typeUuid : params) {
                StockOperationType operationType = stockManagementService.getStockOperationTypeByUuid(typeUuid);
                if (operationType != null) {
                    typeIds.add(operationType.getId());
                }
            }
            if (typeIds.isEmpty()) {
                return emptyResult(context);
            }
            filter.setOperationTypeId(typeIds);
        }

        param = context.getParameter("status");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<StockOperationStatus> statusIds = new ArrayList<>();
            for (String status : params) {
                StockOperationStatus opStatus = (StockOperationStatus) Enum.valueOf(StockOperationStatus.class, status);
                statusIds.add(opStatus);
            }
            if (statusIds.isEmpty()) {
                return emptyResult(context);
            }
            filter.setStatus(statusIds);
        }

        param = context.getParameter("locationUuid");
        if (StringUtils.isNotBlank(param)) {
            Location location = Context.getLocationService().getLocationByUuid(param);
            if (location == null) {
                return emptyResult(context);
            }
            filter.setLocationId(location.getId());
        }

        param = context.getParameter("isLocationOther");
        if (StringUtils.isNotBlank(param)) {
            filter.setIsLocationOther("true".equalsIgnoreCase(param) || "1".equals(param));
        }

        param = context.getParameter("sourceTypeUuid");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<Integer> conceptIds = new ArrayList<>();
            for (String conceptUuid : params) {
                Concept concept = Context.getConceptService().getConceptByUuid(conceptUuid);
                if (concept != null) {
                    conceptIds.add(concept.getId());
                }
            }
            if (conceptIds.isEmpty()) {
                return emptyResult(context);
            }
            filter.setSourceTypeIds(conceptIds);
        }

        param = context.getParameter("stockItemUuid");
        if (StringUtils.isNotBlank(param)) {
            StockItem stockItem = stockManagementService.getStockItemByUuid(param);
            if (stockItem == null) {
                return emptyResult(context);
            }
            filter.setStockItemId(stockItem.getId());
        }

        param = context.getParameter("operationDateMin");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setOperationDateMin(date);
        }

        param = context.getParameter("operationDateMax");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setOperationDateMax(date);
        }

        Result<StockOperationDTO> result = getStockManagementService().findStockOperations(filter);
        if (!result.getData().isEmpty() && context.getRepresentation() == Representation.FULL) {
            StockOperationItemSearchFilter itemSearchFilter = new StockOperationItemSearchFilter();
            itemSearchFilter.setIncludeStockUnitName(true);
            itemSearchFilter.setIncludePackagingUnitName(true);
            itemSearchFilter.setStockOperationUuids(result.getData().stream().map(p -> p.getUuid()).collect(Collectors.toList()));
            Result<StockOperationItemDTO> items = getStockManagementService().findStockOperationItems(itemSearchFilter);
            for (StockOperationDTO stockOperation : result.getData()) {
                stockOperation.setStockOperationItems(items.getData().stream().filter(p -> p.getStockOperationUuid().equals(stockOperation.getUuid())).collect(Collectors.toList()));
            }
        }

        return toAlreadyPaged(result, context);
    }
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockOperationDTO newDelegate() {
		return new StockOperationDTO();
	}
	
	@Override
	public StockOperationDTO save(StockOperationDTO delegate) {
		try {
			StockOperation stockOperation = getStockManagementService().saveStockOperation(delegate);
			return getByUniqueId(stockOperation.getUuid());
		}
		catch (StockManagementException exception) {
			throw new RestClientException(exception.getMessage());
		}
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("destinationUuid");
		description.addProperty("atLocationUuid");
		description.addProperty("externalReference");
		description.addProperty("operationDate");
		description.addProperty("reasonUuid");
		description.addProperty("remarks");
		description.addProperty("sourceUuid");
		description.addProperty("operationTypeUuid");
		description.addProperty("responsiblePersonUuid");
		description.addProperty("approvalRequired");
		description.addProperty("responsiblePersonOther");
		description.addProperty("requisitionStockOperationUuid");
		description.addProperty("stockOperationItems");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("destinationUuid");
		description.addProperty("atLocationUuid");
		description.addProperty("externalReference");
		description.addProperty("operationDate");
		description.addProperty("reasonUuid");
		description.addProperty("remarks");
		description.addProperty("sourceUuid");
		description.addProperty("approvalRequired");
		description.addProperty("responsiblePersonUuid");
		description.addProperty("responsiblePersonOther");
		description.addProperty("stockOperationItems");
		return description;
	}
	
	@Override
	public void purge(StockOperationDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("cancelReason");
			description.addProperty("cancelledBy");
			description.addProperty("cancelledByGivenName");
			description.addProperty("cancelledByFamilyName");
			description.addProperty("cancelledDate");
			description.addProperty("completedBy");
			description.addProperty("completedByGivenName");
			description.addProperty("completedByFamilyName");
			description.addProperty("completedDate");
			description.addProperty("destinationUuid");
			description.addProperty("destinationName");
			description.addProperty("externalReference");
			description.addProperty("atLocationUuid");
			description.addProperty("atLocationName");
			description.addProperty("operationDate");
			description.addProperty("locked");
			description.addProperty("operationNumber");
			description.addProperty("operationOrder");
			description.addProperty("approvalRequired");
			description.addProperty("reasonUuid");
			description.addProperty("reasonName");
			description.addProperty("remarks");
			description.addProperty("sourceUuid");
			description.addProperty("sourceName");
			description.addProperty("status");
			description.addProperty("returnReason");
			description.addProperty("rejectionReason");
			description.addProperty("operationTypeUuid");
			description.addProperty("operationType");
			description.addProperty("operationTypeName");
			description.addProperty("responsiblePersonUuid");
			description.addProperty("responsiblePersonGivenName");
			description.addProperty("responsiblePersonFamilyName");
			description.addProperty("responsiblePersonOther");
			description.addProperty("creator");
			description.addProperty("dateCreated");
			description.addProperty("creatorGivenName");
			description.addProperty("creatorFamilyName");
			description.addProperty("requisitionStockOperationUuid");
			description.addProperty("submittedByGivenName");
			description.addProperty("submittedByFamilyName");
			description.addProperty("submittedDate");
			description.addProperty("returnedByGivenName");
			description.addProperty("returnedByFamilyName");
			description.addProperty("returnedDate");
			description.addProperty("rejectedByGivenName");
			description.addProperty("rejectedByFamilyName");
			description.addProperty("rejectedDate");
			
			description.addProperty("dispatchedByGivenName");
			description.addProperty("dispatchedByFamilyName");
			description.addProperty("dispatchedDate");
			
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addProperty("permission");
			description.addProperty("stockOperationItems");
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("operationNumber");
			
		}
		
		return description;
	}
	
	@PropertySetter("stockOperationItems")
    public void setStockOperationItems(StockOperationDTO instance, ArrayList<Map<String, ?>> items) {

        if (items == null) {
            instance.setStockOperationItems(null);
            return;
        }
        if (items.isEmpty()) {
            instance.setStockOperationItems(new ArrayList<>());
            return;
        }

        StockOperationItemResource handler = new StockOperationItemResource();
        DelegatingResourceDescription creatableProperties = handler.getCreatableProperties();
        DelegatingResourceDescription modifiableProperties = handler.getUpdatableProperties();
        List<StockOperationItemDTO> existingStockOperationItems = null;
        if (StringUtils.isBlank(instance.getUuid())) {
            existingStockOperationItems = new ArrayList<>();
        } else {
            StockOperationItemSearchFilter itemSearchFilter = new StockOperationItemSearchFilter();
            itemSearchFilter.setStockOperationUuids(Arrays.asList(instance.getUuid()));
            existingStockOperationItems = getStockManagementService().findStockOperationItems(itemSearchFilter).getData();
        }
        List<StockOperationItemDTO> itemsToUpdate = new ArrayList<>();

        // Some resources do not have delegating resource description
        for (Map<String, ?> item : items) {
            StockOperationItemDTO itemDTO = null;
            boolean isNew = true;
            if (item.containsKey("uuid") && item.get("uuid") != null && !StringUtils.isBlank(item.get("uuid").toString())) {
                String uuid = item.get("uuid").toString();
                if (existingStockOperationItems == null) {
                    StockOperationItemSearchFilter itemSearchFilter = new StockOperationItemSearchFilter();
                    itemSearchFilter.setUuid(uuid);
                    existingStockOperationItems = getStockManagementService().findStockOperationItems(itemSearchFilter).getData();
                    if (!existingStockOperationItems.isEmpty()) {
                        itemSearchFilter.setUuid(null);
                        itemSearchFilter.setStockOperationUuids(Arrays.asList(existingStockOperationItems.get(0).getStockOperationUuid()));
                        existingStockOperationItems = getStockManagementService().findStockOperationItems(itemSearchFilter).getData();
                    }
                }
                Optional<StockOperationItemDTO> existingItem = existingStockOperationItems.stream().filter(p -> uuid.equals(p.getUuid())).findFirst();
                if (existingItem.isPresent()) {
                    isNew = false;
                    itemDTO = existingItem.get();
                } else
                    throw new RestClientException(String.format("Item %s not found", uuid));

            } else {
                itemDTO = new StockOperationItemDTO();
            }

            DelegatingResourceDescription propertiesToApply = isNew ? creatableProperties : modifiableProperties;
            for (Map.Entry<String, DelegatingResourceDescription.Property> prop : propertiesToApply.getProperties().entrySet()) {
                if (item.containsKey(prop.getKey()) && !RestConstants.PROPERTY_FOR_TYPE.equals(prop.getKey())) {
                    handler.setProperty(itemDTO, prop.getKey(), item.get(prop.getKey()));
                }
            }
            itemsToUpdate.add(itemDTO);
        }
        instance.setStockOperationItems(itemsToUpdate);
    }
	
	@PropertyGetter("permission")
    public SimpleObject getPermission(StockOperationDTO stockOperationDTO) {
        if(permissionCache != null && permissionCache.containsKey(stockOperationDTO.getUuid()))
            return permissionCache.get(stockOperationDTO.getUuid());

        SimpleObject simpleObject = new SimpleObject();
        simpleObject.add("canView", true);

        boolean canEdit = stockOperationDTO.isUpdateable();
        boolean canApprove = stockOperationDTO.isApproveable();
        boolean canReceiveItems = stockOperationDTO.canReceiveItems();
        boolean canDisplayReceivedItems = stockOperationDTO.canDisplayReceivedItems();
        boolean isRequisitionAndCanIssueStock = stockOperationDTO.isRequisitionAndCanIssueStock();
        StockOperationType stockOperationType = getStockManagementService().getStockOperationTypeByUuid(stockOperationDTO.getOperationTypeUuid());
        boolean canUpdateBatchInformation = stockOperationDTO.canUpdateBatchInformation(stockOperationType);
        Boolean userHasEditPermissions = null;

        if (canEdit || canApprove) {
            HashSet<PrivilegeScope> privilegeScopes = getStockManagementService().getPrivilegeScopes(
                    Context.getAuthenticatedUser(),
                    Context.getLocationService().getLocationByUuid(stockOperationDTO.getAtLocationUuid()),
                    getStockManagementService().getStockOperationTypeByUuid(stockOperationDTO.getOperationTypeUuid()),
                    Arrays.asList(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE, Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE)
            );

            if(canEdit) {
                canEdit = canEdit && privilegeScopes.stream().anyMatch(p -> p.getPrivilege().equals(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE));
            }else{
                userHasEditPermissions = Boolean.valueOf(privilegeScopes.stream().anyMatch(p -> p.getPrivilege().equals(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)));
            }
            canApprove = canApprove && privilegeScopes.stream().anyMatch(p -> p.getPrivilege().equals(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE));
        }

        if (canReceiveItems || isRequisitionAndCanIssueStock) {
            if(StringUtils.isBlank(stockOperationDTO.getDestinationUuid())){
                canReceiveItems = false;
                isRequisitionAndCanIssueStock = false;
            }else {
                Party party = getStockManagementService().getPartyByUuid(stockOperationDTO.getDestinationUuid());
                if(party == null || party.getLocation() == null){
                    canReceiveItems = false;
                    isRequisitionAndCanIssueStock = false;
                }
                if(canReceiveItems){
                    HashSet<PrivilegeScope> privilegeScopes = getStockManagementService().getPrivilegeScopes(
                            Context.getAuthenticatedUser(),
                            party.getLocation(),
                            getStockManagementService().getStockOperationTypeByUuid(stockOperationDTO.getOperationTypeUuid()),
                            Arrays.asList(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS)
                    );
                    canReceiveItems = canReceiveItems && privilegeScopes.stream().anyMatch(p -> p.getPrivilege().equals(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS));
                }
                if(isRequisitionAndCanIssueStock){
                    HashSet<PrivilegeScope> privilegeScopes = getStockManagementService().getPrivilegeScopes(
                            Context.getAuthenticatedUser(),
                            party.getLocation(),
                            getStockManagementService().getStockOperationTypeByType(StockOperationType.STOCK_ISSUE),
                            Arrays.asList(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
                    );
                    isRequisitionAndCanIssueStock = isRequisitionAndCanIssueStock && privilegeScopes.stream().anyMatch(p -> p.getPrivilege().equals(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE));
                }
            }
        }

        if(canUpdateBatchInformation){
            if(userHasEditPermissions == null){
                HashSet<PrivilegeScope> privilegeScopes = getStockManagementService().getPrivilegeScopes(
                        Context.getAuthenticatedUser(),
                        Context.getLocationService().getLocationByUuid(stockOperationDTO.getAtLocationUuid()),
                        getStockManagementService().getStockOperationTypeByUuid(stockOperationDTO.getOperationTypeUuid()),
                        Arrays.asList(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)
                );
                userHasEditPermissions = Boolean.valueOf(!privilegeScopes.isEmpty());
            }
            canUpdateBatchInformation = userHasEditPermissions.booleanValue();
        }

        simpleObject.add("canEdit", canEdit);
        simpleObject.add("canApprove", canApprove);
        simpleObject.add("canReceiveItems", canReceiveItems);
        simpleObject.add("canDisplayReceivedItems", canDisplayReceivedItems);
        simpleObject.add("isRequisitionAndCanIssueStock", isRequisitionAndCanIssueStock);
        simpleObject.add("canUpdateBatchInformation", canUpdateBatchInformation);

        if(permissionCache == null){
            permissionCache=new HashMap<>();
            permissionCache.put(stockOperationDTO.getUuid(), simpleObject);
        }
        return simpleObject;
    }
	
	@PropertyGetter("stockOperationItems")
    public Collection<StockOperationItemDTO> getStockOperationItems(StockOperationDTO stockOperationDTO) {
        if (stockOperationDTO.getStockOperationItems() != null)
            return stockOperationDTO.getStockOperationItems();
        StockOperationItemSearchFilter itemSearchFilter = new StockOperationItemSearchFilter();
        itemSearchFilter.setIncludeStockUnitName(true);
        itemSearchFilter.setIncludePackagingUnitName(true);
        itemSearchFilter.setStockOperationUuids(Arrays.asList(stockOperationDTO.getUuid()));
        Result<StockOperationItemDTO> items = getStockManagementService().findStockOperationItems(itemSearchFilter);

        StockItemPackagingUOMSearchFilter filter = new StockItemPackagingUOMSearchFilter();
        filter.setIncludeVoided(false);
        filter.setStockItemUuids(items.getData().stream().map(p -> p.getStockItemUuid()).distinct().collect(Collectors.toList()));
        List<StockItemPackagingUOMDTO> packagingUnits = getStockManagementService().findStockItemPackagingUOMs(filter).getData();

        boolean canUpdateBatchInformation = false;
        SimpleObject permissions = getPermission(stockOperationDTO);
        Map<Integer, Boolean> stockBatchHasTransactions = null;
        if(permissions != null){
            if(permissions.containsKey("canUpdateBatchInformation")) {
                canUpdateBatchInformation = (boolean)permissions.get("canUpdateBatchInformation");
                if(canUpdateBatchInformation){
                    stockBatchHasTransactions = getStockManagementService().checkStockBatchHasTransactionsAfterOperation(stockOperationDTO.getId(), items.getData().stream().filter(p->p.getStockBatchId() != null).map(p -> p.getStockBatchId()).distinct().collect(Collectors.toList()));
                }
            }
        }

        for (StockOperationItemDTO itemDTO : items.getData()) {
            List<StockItemPackagingUOMDTO> units = packagingUnits.stream().filter(p -> p.getStockItemUuid().equals(itemDTO.getStockItemUuid())).collect(Collectors.toList());
            if (!units.isEmpty()) {
                itemDTO.setPackagingUnits(units);
            }
            if(canUpdateBatchInformation){
                itemDTO.setCanUpdateBatchInformation( stockBatchHasTransactions != null && !stockBatchHasTransactions.containsKey(itemDTO.getStockBatchId()));
            }
        }

        return items.getData();
    }
	
	@Override
	public List<String> getPropertiesToExposeAsSubResources() {
		return Arrays.asList("action");
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("cancelReason", new StringProperty())
			        .property("cancelledBy", new IntegerProperty()).property("cancelledByGivenName", new StringProperty())
			        .property("cancelledByFamilyName", new StringProperty())
			        .property("cancelledDate", new DateTimeProperty()).property("completedBy", new IntegerProperty())
			        .property("completedByGivenName", new StringProperty())
			        .property("completedByFamilyName", new StringProperty())
			        .property("completedDate", new DateTimeProperty()).property("destinationUuid", new StringProperty())
			        .property("destinationName", new StringProperty()).property("externalReference", new StringProperty())
			        .property("atLocationUuid", new StringProperty()).property("atLocationName", new StringProperty())
			        .property("operationDate", new DateTimeProperty()).property("locked", new BooleanProperty())
			        .property("operationNumber", new StringProperty()).property("operationOrder", new IntegerProperty())
			        .property("reasonUuid", new StringProperty()).property("reasonName", new StringProperty())
			        .property("remarks", new StringProperty()).property("sourceUuid", new StringProperty())
			        .property("sourceName", new StringProperty()).property("status", new StringProperty())
			        .property("returnReason", new StringProperty()).property("rejectionReason", new StringProperty())
			        .property("operationTypeUuid", new StringProperty()).property("operationType", new StringProperty())
			        .property("operationTypeName", new StringProperty())
			        .property("responsiblePersonUuid", new StringProperty())
			        .property("responsiblePersonGivenName", new StringProperty())
			        .property("responsiblePersonFamilyName", new StringProperty())
			        .property("responsiblePersonOther", new StringProperty())
			        .property("approvalRequired", new BooleanProperty()).property("dateCreated", new DateTimeProperty())
			        .property("creatorGivenName", new StringProperty()).property("creatorFamilyName", new StringProperty())
			        .property("submittedByGivenName", new StringProperty())
			        .property("submittedByFamilyName", new StringProperty()).property("submittedDate", new DateProperty())
			        .property("returnedByGivenName", new StringProperty())
			        .property("returnedByFamilyName", new StringProperty()).property("returnedDate", new DateProperty())
			        .property("rejectedByGivenName", new StringProperty())
			        .property("rejectedByFamilyName", new StringProperty()).property("rejectedDate", new DateProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {
			modelImpl.property("stockOperationItems", new ArrayProperty());
		}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
			modelImpl.property("operationNumber", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
