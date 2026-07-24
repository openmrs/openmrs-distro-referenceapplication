package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.math.BigDecimal;
import java.util.*;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockitempackaginguom", supportedClass = StockItemPackagingUOMDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockItemPackagingUOMResource extends ResourceBase<StockItemPackagingUOMDTO> {
	
	@Override
	public StockItemPackagingUOMDTO getByUniqueId(String uniqueId) {
		StockItemPackagingUOMSearchFilter filter = new StockItemPackagingUOMSearchFilter();
		filter.setIncludeVoided(false);
		filter.setUuid(uniqueId);
		Result<StockItemPackagingUOMDTO> result = getStockManagementService().findStockItemPackagingUOMs(filter);
		return result.getData().isEmpty() ? null : result.getData().get(0);
	}
	
	@Override
	protected void delete(StockItemPackagingUOMDTO delegate, String reason, RequestContext context) throws ResponseException {
		getStockManagementService().voidStockItemPackagingUOM(delegate.getUuid(), reason,
		    Context.getAuthenticatedUser().getUserId());
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		StockItemPackagingUOMSearchFilter filter = new StockItemPackagingUOMSearchFilter();
		filter.setIncludeVoided(context.getIncludeAll());
		filter.setStartIndex(context.getStartIndex());
		filter.setLimit(context.getLimit());
		String param = context.getParameter("stockItemUuid");
		if (StringUtils.isNotBlank(param)) {
			filter.setStockItemUuids(Arrays.asList(param));
		}
		return toAlreadyPaged(getStockManagementService().findStockItemPackagingUOMs(filter), context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockItemPackagingUOMDTO newDelegate() {
		return new StockItemPackagingUOMDTO();
	}
	
	@Override
	public StockItemPackagingUOMDTO save(StockItemPackagingUOMDTO delegate) {
		StockItemPackagingUOM stockItemPackagingUOM = getStockManagementService().saveStockItemPackagingUOM(delegate);
		return getByUniqueId(stockItemPackagingUOM.getUuid());
	}
	
	@Override
	public void purge(StockItemPackagingUOMDTO delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("factor");
		description.addProperty("packagingUomUuid");
		description.addProperty("stockItemUuid");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("factor");
		description.addProperty("packagingUomUuid");
		description.addProperty("stockItemUuid");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("factor");
			description.addProperty("packagingUomUuid");
			description.addProperty("packagingUomName");
			description.addProperty("stockItemUuid");
			description.addProperty("isDefaultStockOperationsUoM");
			description.addProperty("isDispensingUnit");
			
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("packagingUomName");
			
		}
		
		return description;
	}
	
	@PropertySetter("factor")
	public void setFactor(StockItemPackagingUOMDTO instance, Double value) {
		if (value == null) {
			instance.setFactor(null);
		} else {
			instance.setFactor(BigDecimal.valueOf(value));
		}
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("factor", new DecimalProperty())
			        .property("packagingUomUuid", new StringProperty()).property("packagingUomName", new StringProperty())
			        .property("stockItemUuid", new StringProperty())
			        .property("isDefaultStockOperationsUoM", new BooleanProperty())
			        .property("isDispensingUnit", new BooleanProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("packagingUomName", new StringProperty());
			;
		}
		
		return modelImpl;
	}
	
}
