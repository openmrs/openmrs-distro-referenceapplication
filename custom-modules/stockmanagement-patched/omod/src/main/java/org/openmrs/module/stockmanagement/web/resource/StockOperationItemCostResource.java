package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.dto.*;
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

import java.util.Arrays;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperationitemcost", supportedClass = StockOperationItemCost.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationItemCostResource extends ResourceBase<StockOperationItemCost> {
	
	@Override
	public StockOperationItemCost getByUniqueId(String uniqueId) {
		StockOperationItemSearchFilter filter = new StockOperationItemSearchFilter();
		filter.setUuid(uniqueId);
		List<StockOperationItemCost> result = getStockManagementService().getStockOperationItemCosts(filter).getData();
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	protected void delete(StockOperationItemCost delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		
		String param = context.getParameter("stockOperationUuid");
		if (StringUtils.isBlank(param)) {
			return emptyResult(context);
		}
		
		StockOperationItemSearchFilter itemSearchFilter = new StockOperationItemSearchFilter();
		itemSearchFilter.setIncludeStockUnitName(false);
		itemSearchFilter.setIncludePackagingUnitName(true);
		itemSearchFilter.setStockOperationUuids(Arrays.asList(param));
		Result<StockOperationItemCost> result = getStockManagementService().getStockOperationItemCosts(itemSearchFilter);
		return toAlreadyPaged(result, context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockOperationItemCost newDelegate() {
		return new StockOperationItemCost();
	}
	
	@Override
	public StockOperationItemCost save(StockOperationItemCost delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(StockOperationItemCost delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("stockItemUuid");
			description.addProperty("stockItemPackagingUOMUuid");
			description.addProperty("stockItemPackagingUOMName");
			description.addProperty("stockBatchUuid");
			description.addProperty("batchNo");
			description.addProperty("quantity");
			description.addProperty("unitCost");
			description.addProperty("unitCostUOMUuid");
			description.addProperty("unitCostUOMName");
			description.addProperty("totalCost");
			
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
			modelImpl.property("uuid", new StringProperty()).property("stockItemUuid", new StringProperty())
			        .property("stockItemPackagingUOMUuid", new StringProperty())
			        .property("stockItemPackagingUOMName", new StringProperty())
			        .property("stockBatchUuid", new StringProperty()).property("batchNo", new StringProperty())
			        .property("quantity", new DecimalProperty()).property("unitCost", new DecimalProperty())
			        .property("unitCostUOMUuid", new StringProperty()).property("unitCostUOMName", new StringProperty())
			        .property("totalCost", new DecimalProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
