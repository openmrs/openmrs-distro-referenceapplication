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

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperationtypelocationscope", supportedClass = StockOperationTypeLocationScope.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationTypeLocationScopeResource extends ResourceBase<StockOperationTypeLocationScope> {
	
	@Override
	public StockOperationTypeLocationScope getByUniqueId(String uniqueId) {
		return getStockManagementService().getStockOperationTypeLocationScopeByUuid(uniqueId);
	}
	
	@Override
	protected void delete(StockOperationTypeLocationScope delegate, String reason, RequestContext context)
	        throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		return doGetAll(context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		List<StockOperationTypeLocationScope> result = getStockManagementService().getAllStockOperationTypeLocationScopes();
		return toAlreadyPaged(result, context);
	}
	
	@Override
	public StockOperationTypeLocationScope newDelegate() {
		return new StockOperationTypeLocationScope();
	}
	
	@Override
	public StockOperationTypeLocationScope save(StockOperationTypeLocationScope delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(StockOperationTypeLocationScope delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("dateCreated");
			description.addProperty("dateChanged");
			description.addProperty("locationTag");
			description.addProperty("isSource");
			description.addProperty("isDestination");
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("locationTag");
		}
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("dateCreated", new DateTimeProperty())
			        .property("dateChanged", new DateTimeProperty()).property("locationTag", new StringProperty())
			        .property("isSource", new BooleanProperty()).property("isDestination", new BooleanProperty());
		}
		if (rep instanceof DefaultRepresentation) {
			
		}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("locationTag", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
