package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
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
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperationtype", supportedClass = StockOperationType.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationTypeResource extends ResourceBase<StockOperationType> {
	
	@Override
	public StockOperationType getByUniqueId(String uniqueId) {
		return getStockManagementService().getStockOperationTypeByUuid(uniqueId);
	}
	
	@Override
	protected void delete(StockOperationType delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		return doGetAll(context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<StockOperationType> operationTypes = getStockManagementService().getAllStockOperationTypes();
		return toAlreadyPaged(operationTypes, context);
	}
	
	@Override
	public StockOperationType newDelegate() {
		return new StockOperationType();
	}
	
	@Override
	public StockOperationType save(StockOperationType delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(StockOperationType delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("dateCreated");
			description.addProperty("dateChanged");
			description.addProperty("name");
			description.addProperty("description");
			description.addProperty("operationType");
			description.addProperty("hasSource");
			description.addProperty("sourceType");
			description.addProperty("hasDestination");
			description.addProperty("destinationType");
			description.addProperty("availableWhenReserved");
			description.addProperty("allowExpiredBatchNumbers");
			description.addProperty("stockOperationTypeLocationScopes", Representation.DEFAULT);
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
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
			modelImpl
			        .property("uuid", new StringProperty())
			        .property("dateCreated", new DateTimeProperty())
			        .property("dateChanged", new DateTimeProperty())
			        .property("name", new StringProperty())
			        .property("description", new StringProperty())
			        .property("operationType", new StringProperty())
			        .property("hasSource", new BooleanProperty())
			        .property("sourceType", new StringProperty())
			        .property("hasDestination", new BooleanProperty())
			        .property("destinationType", new StringProperty())
			        .property("availableWhenReserved", new BooleanProperty())
			        .property("allowExpiredBatchNumbers", new BooleanProperty())
			        .property("stockOperationTypeLocationScopes",
			            new ArrayProperty(new RefProperty("#/definitions/StockOperationTypeLocationScopeResourceGet")));
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {
			
		}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("name", new StringProperty());
		}
		
		return modelImpl;
	}
}
