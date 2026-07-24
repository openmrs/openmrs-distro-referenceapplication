package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.web.StockInventoryPageableResult;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.*;
import java.util.stream.Collectors;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockiteminventory", supportedClass = StockItemInventory.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockItemInventoryResource extends ResourceBase<StockItemInventory> {
	
	@Override
	public StockItemInventory getByUniqueId(String uniqueId) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected void delete(StockItemInventory delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
    protected PageableResult doSearch(RequestContext context) {
        String partyUuid = null;
        List<Integer> partyIds = null;
        Integer stockItemId = null;
        List<Integer> stockItemIds = null;
        Integer drugId = null;
        Integer conceptId = null;
        Integer stockBatchId = null;
        boolean withBatchInfo = false;
        boolean isDispensing = false;
        boolean includeStrength = false;
        boolean includeConceptRefIds = false;
        boolean includeStockItemName = false;
        boolean emptyBatch = false;
        Integer emptyBatchPartyId = null;
        boolean requireNonExpiredStockBatches=true;
        Date date = null;
        String param = context.getParameter("date");
        if (!StringUtils.isBlank(param)) {
            date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
        }

        StockItemInventorySearchFilter.InventoryGroupBy inventoryGroupBy = null, totalBy = null;
        param = context.getParameter("stockItemUuid");
        if (!StringUtils.isBlank(param)) {
            StockItem stockItem = getStockManagementService().getStockItemByUuid(param);
            if (stockItem == null) {
                return emptyResult(context);
            }
            stockItemId = stockItem.getId();
        } else {
            param = context.getParameter("drugId");
            if (!StringUtils.isBlank(param)) {
                drugId = NumberUtils.toInt(param, 0);
                if (drugId == 0) drugId = null;
                if (drugId != null) {
                    Drug drug = Context.getConceptService().getDrug(drugId);
                    if (drug == null) {
                        return emptyResult(context);
                    }
                }
            }

            param = context.getParameter("conceptId");
            if (!StringUtils.isBlank(param)) {
                conceptId = NumberUtils.toInt(param, 0);
                if (conceptId == 0) conceptId = null;
                if (conceptId != null) {
                    Concept concept = Context.getConceptService().getConcept(conceptId);
                    if (concept == null) {
                        return emptyResult(context);
                    }
                }
            }

            param = context.getParameter("drugUuid");
            if (!StringUtils.isBlank(param)) {
                Drug drug = Context.getConceptService().getDrugByUuid(param);
                if (drug == null) {
                    return emptyResult(context);
                } else {
                    drugId = drug.getDrugId();
                }
            }

            param = context.getParameter("conceptUuid");
            if (!StringUtils.isBlank(param)) {
                Concept concept = Context.getConceptService().getConceptByUuid(param);
                if (concept == null) {
                    return emptyResult(context);
                } else {
                    conceptId = concept.getConceptId();
                }
            }

            if (drugId != null || conceptId != null) {
                StockItemSearchFilter stockItemSearchFilter = new StockItemSearchFilter();
                stockItemSearchFilter.setDrugId(drugId);
                stockItemSearchFilter.setConceptId(conceptId);
                Result<StockItemDTO> stockItems = getStockManagementService().findStockItems(stockItemSearchFilter);
                if (stockItems.getData().isEmpty()) {
                    return emptyResult(context);
                }
                stockItemIds = stockItems.getData().stream().map(p -> p.getId()).collect(Collectors.toList());
            }else{

                param = context.getParameter("stockOperationUuid");
                if(!StringUtils.isBlank(param)){
                    StockOperationItemSearchFilter itemSearchFilter = new StockOperationItemSearchFilter();
                    itemSearchFilter.setIncludeStockUnitName(true);
                    itemSearchFilter.setIncludePackagingUnitName(true);
                    itemSearchFilter.setStockOperationUuids(Arrays.asList(param));
                    Result<StockOperationItemDTO> items = getStockManagementService().findStockOperationItems(itemSearchFilter);
                    if (items.getData().isEmpty()) {
                        return emptyResult(context);
                    }
                    stockItemIds = items.getData().stream().map(p -> p.getStockItemId()).collect(Collectors.toList());

                }else{
                    return emptyResult(context);
                }
            }
        }

        param = context.getParameter("partyUuid");
        if (!StringUtils.isBlank(param)) {
            partyUuid = param;
        } else {
            param = context.getParameter("locationUuid");
            if (!StringUtils.isBlank(param)) {
                Location location = Context.getLocationService().getLocationByUuid(param);
                if (location == null) {
                    return emptyResult(context);
                }
                Party party = getStockManagementService().getPartyByLocation(location);
                if (party == null) {
                    return emptyResult(context);
                }
                partyUuid = party.getUuid();
            } else {
                param = context.getParameter("dispenseLocationUuid");
                if (!StringUtils.isBlank(param)) {
                    Location location = Context.getLocationService().getLocationByUuid(param);
                    if (location == null) {
                        return emptyResult(context);
                    }

                    List<PartyDTO> partyList = getStockManagementService().getCompleteStockDispensingLocationPartyList(location.getLocationId());
                    partyIds = partyList.stream().map(p -> p.getId()).collect(Collectors.toList());

                    boolean includeDefaultMainPharmacy = true;
                    String dispenseAtLocation = context.getParameter("dispenseAtLocation");
                    if (StringUtils.isNotBlank(dispenseAtLocation) && (dispenseAtLocation.equalsIgnoreCase("true") || dispenseAtLocation.equalsIgnoreCase("1"))) {
                        includeDefaultMainPharmacy=false;
                    }
                    if(includeDefaultMainPharmacy) {
                        List<PartyDTO> mainPharmacies = getStockManagementService().getMainPharmacyPartyList();
                        if (!mainPharmacies.isEmpty()) {
                            partyIds.addAll(mainPharmacies.stream().map(p -> p.getId()).collect(Collectors.toList()));
                        }
                    }
                    partyIds = partyIds.stream().distinct().collect(Collectors.toList());
                    if (partyIds.isEmpty()) {
                        return emptyResult(context);
                    }
                    isDispensing = Context.getUserContext().hasPrivilege(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY);
                    if (isDispensing) {
                        includeStockItemName = true;
                        param = context.getParameter("emptyBatch");
                        if(!StringUtils.isBlank(param)) {
                            emptyBatch = param.equalsIgnoreCase("true") || param.equalsIgnoreCase("1");
                        }
                        if(emptyBatch){
                            param = context.getParameter("emptyBatchLocationUuid");
                            if(!StringUtils.isBlank(param)) {
                                Location emptyBatchlocation = Context.getLocationService().getLocationByUuid(param);
                                if (location == null) {
                                    return emptyResult(context);
                                }
                                Party party = getStockManagementService().getPartyByLocation(emptyBatchlocation);
                                if (party == null) {
                                    return emptyResult(context);
                                }
                                emptyBatchPartyId = party.getId();
                            }
                        }
                    }
                }
            }
        }

        param = context.getParameter("includeBatchNo");
        if (!StringUtils.isBlank(param)) {
            withBatchInfo = param.equalsIgnoreCase("true") || param.equalsIgnoreCase("1");
        }


        param = context.getParameter("includeStockItemName");
        if (!StringUtils.isBlank(param)) {
            includeStockItemName = param.equalsIgnoreCase("true") || param.equalsIgnoreCase("1");
        }


        param = context.getParameter("includeStrength");
        if (!StringUtils.isBlank(param)) {
            includeStrength = param.equalsIgnoreCase("true") || param.equalsIgnoreCase("1");
        }

        param = context.getParameter("includeConceptRefIds");
        if (!StringUtils.isBlank(param)) {
            includeConceptRefIds = param.equalsIgnoreCase("true") || param.equalsIgnoreCase("1");
        }

        param = context.getParameter("excludeExpired");
        if (!StringUtils.isBlank(param)) {
            requireNonExpiredStockBatches = param.equalsIgnoreCase("true") || param.equalsIgnoreCase("1");
        }

        param = context.getParameter("stockBatchUuid");
        if (!StringUtils.isBlank(param)) {
            withBatchInfo = true;
            StockBatchSearchFilter stockBatchSearchFilter = new StockBatchSearchFilter();
            stockBatchSearchFilter.setStockBatchUuid(param);
            stockBatchSearchFilter.setIncludeVoided(true);
            Result<StockBatchDTO> stockBatches = getStockManagementService().findStockBatches(stockBatchSearchFilter);
            if (stockBatches.getData().isEmpty()) {
                return emptyResult(context);
            }
            stockBatchId = stockBatches.getData().get(0).getId();
        }
        param = context.getParameter("groupBy");
        if (!StringUtils.isBlank(param)) {
            StockItemInventorySearchFilter.InventoryGroupBy groupBy = (StockItemInventorySearchFilter.InventoryGroupBy) Enum.valueOf(StockItemInventorySearchFilter.InventoryGroupBy.class, param);
            inventoryGroupBy = groupBy;

        }

        param = context.getParameter("totalBy");
        if (!StringUtils.isBlank(param)) {
            StockItemInventorySearchFilter.InventoryGroupBy groupBy = (StockItemInventorySearchFilter.InventoryGroupBy) Enum.valueOf(StockItemInventorySearchFilter.InventoryGroupBy.class, param);
            totalBy = groupBy;

        }

        StockItemInventorySearchFilter filter = new StockItemInventorySearchFilter();
        if (inventoryGroupBy == null) {
            filter.setInventoryGroupBy(withBatchInfo ? StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItemBatchNo : StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItem);
        } else {
            filter.setInventoryGroupBy(inventoryGroupBy);
        }
        filter.setIsDispensing(isDispensing);
        filter.setAllowEmptyBatchInfo(emptyBatch);
        filter.setEmptyBatchPartyId(emptyBatchPartyId);
        filter.setIncludeStrength(includeStrength);
        filter.setIncludeConceptRefIds(includeConceptRefIds);
        filter.setTotalBy(totalBy);
        filter.setDoSetBatchFields(true);
        filter.setDoSetPartyNameField(true);
        filter.setDoSetQuantityUoM(true);
        filter.setIncludeStockItemName(includeStockItemName);
        filter.setRequireNonExpiredStockBatches(requireNonExpiredStockBatches);
        filter.setDate(date);
        List<StockItemInventorySearchFilter.ItemGroupFilter> itemsToSearch = new ArrayList<>();
        if (stockItemId != null) {
            if (partyIds == null) {
                StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter = new StockItemInventorySearchFilter.ItemGroupFilter(
                        stockItemId,
                        partyUuid != null ? Arrays.asList(partyUuid) : null,
                        stockBatchId
                );
                itemsToSearch.add(itemGroupFilter);
            } else {
                StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter = new StockItemInventorySearchFilter.ItemGroupFilter(
                        partyIds,
                        stockItemId,
                        stockBatchId
                );
                itemsToSearch.add(itemGroupFilter);
            }
        } else if (stockItemIds != null) {
            for (Integer stockItemId0 : stockItemIds) {
                if (partyIds == null) {
                    StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter = new StockItemInventorySearchFilter.ItemGroupFilter(
                            stockItemId0,
                            partyUuid != null ? Arrays.asList(partyUuid) : null,
                            stockBatchId
                    );
                    itemsToSearch.add(itemGroupFilter);
                } else {
                    StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter = new StockItemInventorySearchFilter.ItemGroupFilter(
                            partyIds,
                            stockItemId0,
                            stockBatchId
                    );
                    itemsToSearch.add(itemGroupFilter);
                }
            }
        }
        filter.setItemGroupFilters(itemsToSearch);
        //filter.setStartIndex(context.getStartIndex());
        //filter.setLimit(context.getLimit());
        StockInventoryResult stockItemInventoryResult = getStockManagementService().getStockInventory(filter);
        return new StockInventoryPageableResult(context, stockItemInventoryResult, stockItemInventoryResult.hasMoreResults(), stockItemInventoryResult.getTotalRecordCount());
    }
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockItemInventory newDelegate() {
		return new StockItemInventory();
	}
	
	@Override
	public StockItemInventory save(StockItemInventory delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(StockItemInventory delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("partyUuid");
			description.addProperty("locationUuid");
			description.addProperty("partyName");
			description.addProperty("stockItemUuid");
			description.addProperty("drugId");
			description.addProperty("drugUuid");
			description.addProperty("drugStrength");
			description.addProperty("conceptId");
			description.addProperty("conceptUuid");
			description.addProperty("stockBatchUuid");
			description.addProperty("batchNumber");
			description.addProperty("quantity");
			description.addProperty("quantityUoM");
			description.addProperty("quantityFactor");
			description.addProperty("quantityUoMUuid");
			description.addProperty("expiration");
			description.addProperty("commonName");
			description.addProperty("acronym");
			description.addProperty("drugName");
			description.addProperty("conceptName");
		}
		
		if (rep instanceof DefaultRepresentation) {
			
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("partyUuid", new StringProperty()).property("locationUuid", new StringProperty())
			        .property("partyName", new StringProperty()).property("stockItemUuid", new StringProperty())
			        .property("drugId", new StringProperty()).property("drugUuid", new StringProperty())
			        .property("drugStrength", new StringProperty()).property("conceptId", new StringProperty())
			        .property("conceptUuid", new StringProperty()).property("stockBatchUuid", new StringProperty())
			        .property("batchNumber", new StringProperty()).property("quantity", new DecimalProperty())
			        .property("quantityUoM", new StringProperty()).property("quantityFactor", new DecimalProperty())
			        .property("quantityUoMUuid", new StringProperty()).property("expiration", new DateTimeProperty())
			        .property("commonName", new StringProperty()).property("acronym", new StringProperty())
			        .property("drugName", new StringProperty()).property("conceptName", new StringProperty());
			
		}
		if (rep instanceof DefaultRepresentation) {
			;
		}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {}
		
		return modelImpl;
	}
	
}
