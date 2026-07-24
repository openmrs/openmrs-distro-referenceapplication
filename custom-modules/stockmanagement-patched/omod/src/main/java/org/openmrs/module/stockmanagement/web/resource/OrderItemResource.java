package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.*;
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

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/orderitem", supportedClass = OrderItemDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class OrderItemResource extends ResourceBase<OrderItemDTO> {
	
	@Override
	public OrderItemDTO getByUniqueId(String uniqueId) {
		OrderItemSearchFilter filter = new OrderItemSearchFilter();
		filter.setUuid(uniqueId);
		filter.setLimit(1);
		filter.setStartIndex(0);
		Result<OrderItemDTO> result = getStockManagementService().findOrderItems(filter);
		return result.getData().isEmpty() ? null : result.getData().get(0);
	}
	
	@Override
	protected void delete(OrderItemDTO delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
    protected PageableResult doSearch(RequestContext context) {
        OrderItemSearchFilter filter = new OrderItemSearchFilter();
        filter.setIncludeVoided(context.getIncludeAll());
        filter.setStartIndex(context.getStartIndex());
        filter.setLimit(context.getLimit());
        String param = context.getParameter("q");
        if (StringUtils.isNotBlank(param))
            filter.setOrderNumber(param);
        StockManagementService stockManagementService = getStockManagementService();
        param = context.getParameter("encounterUuid");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<String> encounterUuids = new ArrayList<>();
            for (String encounterUuid : params) {
                if (!StringUtils.isBlank(encounterUuid)) {
                    encounterUuids.add(encounterUuid);
                }
            }
            if (encounterUuids.isEmpty()) {
                return emptyResult(context);
            }
            filter.setEncounterUuids(encounterUuids);
        }

        param = context.getParameter("orderUuid");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<String> orderUuids = new ArrayList<>();
            for (String orderUuid : params) {
                if (!StringUtils.isBlank(orderUuid)) {
                    orderUuids.add(orderUuid);
                }
            }
            if (orderUuids.isEmpty()) {
                return emptyResult(context);
            }
            filter.setOrderUuids(orderUuids);
        }

        param = context.getParameter("stockItemId");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<Integer> stockitemIds = new ArrayList<>();
            for (String stockItemId : params) {
                stockitemIds.add(Integer.parseInt(stockItemId));
            }
            if (stockitemIds.isEmpty()) {
                return emptyResult(context);
            }
            filter.setStockItemIds(stockitemIds);
        }

        param = context.getParameter("createFromLocationUuid");
        if (StringUtils.isNotBlank(param)) {
            Location location = Context.getLocationService().getLocationByUuid(param);
            if (location == null) {
                return emptyResult(context);
            }
            filter.setCreatedFromLocationIds(Arrays.asList(location.getId()));
        }

        param = context.getParameter("fulfilmentLocationUuid");
        if (StringUtils.isNotBlank(param)) {
            Location location = Context.getLocationService().getLocationByUuid(param);
            if (location == null) {
                return emptyResult(context);
            }
            filter.setFulfilmentLocationIds(Arrays.asList(location.getId()));
        }

        param = context.getParameter("orderDateMin");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setOrderDateMin(date);
        }

        param = context.getParameter("orderDateMax");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setOrderDateMax(date);
        }

        Result<OrderItemDTO> result = getStockManagementService().findOrderItems(filter);
        return toAlreadyPaged(result, context);
    }
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public OrderItemDTO newDelegate() {
		return new OrderItemDTO();
	}
	
	@Override
	public OrderItemDTO save(OrderItemDTO delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(OrderItemDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("orderUuid");
			description.addProperty("action");
			description.addProperty("orderNumber");
			description.addProperty("encounterUuid");
			description.addProperty("stockItemUuid");
			description.addProperty("drugUuid");
			description.addProperty("drugName");
			description.addProperty("conceptUuid");
			description.addProperty("conceptId");
			description.addProperty("conceptName");
			description.addProperty("commonName");
			description.addProperty("acronym");
			description.addProperty("quantity");
			description.addProperty("duration");
			description.addProperty("stockItemPackagingUOMConceptId");
			description.addProperty("stockItemPackagingUOMUuid");
			description.addProperty("stockItemPackagingUOMName");
			description.addProperty("createdFromName");
			description.addProperty("createdFrom");
			description.addProperty("createdFromUuid");
			description.addProperty("createdFromPartyUuid");
			description.addProperty("fulfilmentLocationName");
			description.addProperty("fulfilmentLocationUuid");
			description.addProperty("fulfilmentPartyUuid");
			description.addProperty("creator");
			description.addProperty("dateCreated");
			description.addProperty("creatorGivenName");
			description.addProperty("creatorFamilyName");
			description.addProperty("patientId");
			description.addProperty("patientGivenName");
			description.addProperty("patientFamilyName");
			description.addProperty("scheduledDate");
		}
		
		if (rep instanceof DefaultRepresentation) {
			
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("orderUuid", new StringProperty())
			        .property("action", new StringProperty()).property("orderNumber", new StringProperty())
			        .property("encounterUuid", new StringProperty()).property("stockItemUuid", new StringProperty())
			        .property("drugUuid", new StringProperty()).property("drugName", new StringProperty())
			        .property("conceptUuid", new StringProperty()).property("conceptId", new IntegerProperty())
			        .property("conceptName", new StringProperty()).property("commonName", new StringProperty())
			        .property("acronym", new StringProperty()).property("quantity", new DecimalProperty())
			        .property("duration", new IntegerProperty())
			        .property("stockItemPackagingUOMConceptId", new IntegerProperty())
			        .property("stockItemPackagingUOMUuid", new StringProperty())
			        .property("stockItemPackagingUOMName", new StringProperty())
			        .property("createdFromName", new StringProperty()).property("createdFrom", new IntegerProperty())
			        .property("createdFromUuid", new StringProperty())
			        .property("createdFromPartyUuid", new StringProperty())
			        .property("fulfilmentLocationName", new StringProperty())
			        .property("fulfilmentLocationUuid", new StringProperty())
			        .property("fulfilmentPartyUuid", new StringProperty()).property("creator", new IntegerProperty())
			        .property("dateCreated", new DateTimeProperty()).property("creatorGivenName", new StringProperty())
			        .property("creatorFamilyName", new StringProperty()).property("patientId", new IntegerProperty())
			        .property("patientGivenName", new StringProperty()).property("patientFamilyName", new StringProperty())
			        .property("scheduledDate", new DateProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
