package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.IllegalRequestException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.*;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stocksource", supportedClass = StockSource.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockSourceResource extends ResourceBase<StockSource> {
	
	@Override
	public StockSource getByUniqueId(String uniqueId) {
		return getStockManagementService().getStockSourceByUuid(uniqueId);
	}
	
	@Override
    protected void delete(StockSource delegate, String reason, RequestContext context) throws ResponseException {
        if (reason != null && reason.length() > 250) {
            throw new IllegalRequestException("Parameter reason can not exceed 250 characters");
        }
        List<String> stockSourcesToDelete = new ArrayList<>();
        stockSourcesToDelete.add(delegate.getUuid());
        String ids = context.getParameter("ids");
        if (ids != null && StringUtils.isNotEmpty(ids)) {
            for (String id : ids.split(",")) {
                if (id.isEmpty()) continue;
                if (id.length() > 38) {
                    throw new IllegalRequestException("Id not recognized");
                }
                stockSourcesToDelete.add(id);
            }
        }
        getStockManagementService().voidStockSources(stockSourcesToDelete, reason, Context.getAuthenticatedUser().getUserId());
    }
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		StockSourceSearchFilter filter = new StockSourceSearchFilter();
		filter.setIncludeVoided(context.getIncludeAll());
		filter.setStartIndex(context.getStartIndex());
		filter.setLimit(context.getLimit());
		
		String param = context.getParameter("q");
		if (StringUtils.isNotBlank(param))
			filter.setTextSearch(param);
		
		param = context.getParameter("sourceTypeUuid");
		if (StringUtils.isNotBlank(param)) {
			Concept sourceType = Context.getConceptService().getConceptByUuid(param);
			if (sourceType == null) {
				return emptyResult(context);
			}
			filter.setSourceType(sourceType);
		}
        Result<StockSource> result = getStockManagementService().findStockSources(filter);
        result.getData().sort(Comparator.comparing(p -> p.getName().toLowerCase()));
		return toAlreadyPaged(result, context);
	}
	
	@Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        StockSourceSearchFilter filter = new StockSourceSearchFilter();
        filter.setIncludeVoided(context.getIncludeAll());
        Result<StockSource> result = getStockManagementService().findStockSources(filter);
        result.getData().sort(Comparator.comparing(p -> p.getName().toLowerCase()));
        return toAlreadyPaged(result, context);
    }
	
	@Override
	public StockSource newDelegate() {
		return new StockSource();
	}
	
	@Override
	public StockSource save(StockSource delegate) {
		return getStockManagementService().saveStockSource(delegate);
	}
	
	@Override
	public void purge(StockSource delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("name");
		description.addProperty("acronym");
		description.addProperty("sourceType");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("name");
		description.addProperty("acronym");
		description.addProperty("sourceType");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("name");
			description.addProperty("acronym");
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addProperty("sourceType", Representation.REF);
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addProperty("sourceType", Representation.DEFAULT);
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("name");
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("name", new StringProperty())
			        .property("acronym", new StringProperty());
		}
		if (rep instanceof DefaultRepresentation) {
			modelImpl.property("sourceType", new RefProperty("#/definitions/ConceptGetRef"));
		}
		
		if (rep instanceof FullRepresentation) {
			modelImpl.property("sourceType", new RefProperty("#/definitions/ConceptGet"));
		}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("name", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
