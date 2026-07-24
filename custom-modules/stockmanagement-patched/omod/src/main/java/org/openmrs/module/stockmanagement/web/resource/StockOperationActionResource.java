package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementException;
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
import org.springframework.web.client.RestClientException;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperationaction", supportedClass = StockOperationAction.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationActionResource extends ResourceBase<StockOperationAction> {
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public StockOperationAction getByUniqueId(String uniqueId) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected void delete(StockOperationAction delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public StockOperationAction newDelegate() {
		return new StockOperationAction();
	}
	
	@Override
	public StockOperationAction save(StockOperationAction delegate) {
		StockOperationDTO stockOperation = null;
		try {
			StockOperationSearchFilter filter = new StockOperationSearchFilter();
			filter.setStockOperationUuid(delegate.getUuid());
			Result<StockOperationDTO> result = getStockManagementService().findStockOperations(filter);
			if (result.getData().isEmpty()) {
				throw new RestClientException("Stock operation " + delegate.getUuid() + " not found");
			}
			stockOperation = result.getData().get(0);
			
			switch (delegate.getName()) {
				case COMPLETE:
					getStockManagementService().completeStockOperation(stockOperation);
					break;
				case SUBMIT:
					getStockManagementService().submitStockOperation(stockOperation);
					break;
				case APPROVE:
					getStockManagementService().approveStockOperation(stockOperation);
					break;
				case RETURN:
					getStockManagementService().returnStockOperation(stockOperation, delegate.getReason());
					break;
				case REJECT:
					getStockManagementService().rejectStockOperation(stockOperation, delegate.getReason());
					break;
				case CANCEL:
					getStockManagementService().cancelStockOperation(stockOperation, delegate.getReason());
					break;
				case DISPATCH:
					getStockManagementService().dispatchStockOperation(stockOperation);
					break;
				// Receivers not allowed to modiy quntities received
				//				case QUANTITY_RECEIVED:
				//					getStockManagementService().stockOperationItemsReceived(stockOperation, delegate.getLineItems());
				//					break;
				default:
					throw new ResourceDoesNotSupportOperationException();
			}
		}
		catch (StockManagementException exception) {
			throw new RestClientException(exception.getMessage());
		}
		return delegate;
	}
	
	@Override
	public void purge(StockOperationAction delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("reason");
			description.addProperty("name");
			description.addProperty("uuid");
			description.addProperty("lineItems");
		}
		
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("reason", new StringProperty()).property("name", new StringProperty())
			        .property("uuid", new StringProperty()).property("lineItems", new ArrayProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {}
		
		return modelImpl;
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("reason");
		description.addProperty("name");
		description.addProperty("uuid");
		description.addProperty("lineItems");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		throw new ResourceDoesNotSupportOperationException();
	}
}
