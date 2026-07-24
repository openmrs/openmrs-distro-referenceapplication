package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.Party;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperationitem", supportedClass = StockOperationItemDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationItemResource extends ResourceBase<StockOperationItemDTO> {
	
	public StockOperationItemResource() {
	}
	
	@Override
	public StockOperationItemDTO getByUniqueId(String uniqueId) {
		StockOperationItemSearchFilter filter = new StockOperationItemSearchFilter();
		filter.setUuid(uniqueId);
		List<StockOperationItemDTO> result = getStockManagementService().findStockOperationItems(filter).getData();
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	protected void delete(StockOperationItemDTO delegate, String reason, RequestContext context) throws ResponseException {
		try {
			getStockManagementService().voidStockOperationItem(delegate.getUuid(), reason,
			    Context.getAuthenticatedUser().getUserId());
		}
		catch (StockManagementException exception) {
			throw new RestClientException(exception.getMessage());
		}
	}
	
	@Override
	public StockOperationItemDTO newDelegate() {
		return new StockOperationItemDTO();
	}
	
	@Override
	public StockOperationItemDTO save(StockOperationItemDTO delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(StockOperationItemDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("stockItemUuid");
		description.addProperty("stockItemPackagingUOMUuid");
		description.addProperty("stockBatchUuid");
		description.addProperty("batchNo");
		description.addProperty("expiration");
		description.addProperty("quantity");
		description.addProperty("purchasePrice");
		description.addProperty("quantityRequested");
		description.addProperty("quantityRequestedPackagingUOMUuid");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("stockItemUuid");
		description.addProperty("stockItemPackagingUOMUuid");
		description.addProperty("stockBatchUuid");
		description.addProperty("batchNo");
		description.addProperty("expiration");
		description.addProperty("quantity");
		description.addProperty("purchasePrice");
		return description;
	}
	
	@PropertySetter("purchasePrice")
	public void setPurchasePrice(StockOperationItemDTO instance, Double value) {
		if (value == null) {
			instance.setPurchasePrice(null);
		} else {
			instance.setPurchasePrice(BigDecimal.valueOf(value));
		}
	}
	
	@PropertySetter("quantity")
	public void setQuantity(StockOperationItemDTO instance, Double value) {
		if (value == null) {
			instance.setQuantity(null);
		} else {
			instance.setQuantity(BigDecimal.valueOf(value));
		}
	}
	
	@PropertySetter("quantityRequested")
	public void setQuantityRequested(StockOperationItemDTO instance, Double value) {
		if (value == null) {
			instance.setQuantityRequested(null);
		} else {
			instance.setQuantityRequested(BigDecimal.valueOf(value));
		}
	}
	
	@PropertyGetter("permission")
	public SimpleObject getPermission(StockOperationItemDTO stockOperationItemDTO) {
		SimpleObject simpleObject = new SimpleObject();
		simpleObject.add(
		    "canUpdateBatchInformation",
		    stockOperationItemDTO.getCanUpdateBatchInformation() == null ? Boolean.FALSE : stockOperationItemDTO
		            .getCanUpdateBatchInformation());
		return simpleObject;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("stockItemUuid");
			description.addProperty("stockItemName");
			description.addProperty("stockItemPackagingUOMUuid");
			description.addProperty("stockItemPackagingUOMName");
			description.addProperty("stockItemPackagingUOMFactor");
			description.addProperty("stockBatchUuid");
			description.addProperty("stockOperationUuid");
			description.addProperty("batchNo");
			description.addProperty("expiration");
			description.addProperty("quantity");
			description.addProperty("quantityReceived");
			description.addProperty("quantityReceivedPackagingUOMUuid");
			description.addProperty("quantityReceivedPackagingUOMName");
			description.addProperty("quantityReceivedPackagingUOMFactor");
			description.addProperty("quantityRequested");
			description.addProperty("quantityRequestedPackagingUOMUuid");
			description.addProperty("quantityRequestedPackagingUOMName");
			description.addProperty("quantityRequestedPackagingUOMFactor");
			description.addProperty("purchasePrice");
			description.addProperty("hasExpiration");
			description.addProperty("packagingUnits");
			description.addProperty("commonName");
			description.addProperty("acronym");
			description.addProperty("permission");
		}
		
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("stockItemName");
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("stockItemUuid", new StringProperty())
			        .property("stockItemName", new StringProperty())
			        .property("stockItemPackagingUOMUuid", new StringProperty())
			        .property("stockItemPackagingUOMName", new StringProperty())
			        .property("stockItemPackagingUOMFactor", new DecimalProperty())
			        .property("stockBatchUuid", new StringProperty()).property("stockOperationUuid", new StringProperty())
			        .property("batchNo", new StringProperty()).property("expiration", new DateTimeProperty())
			        .property("quantity", new DecimalProperty()).property("quantityReceived", new DecimalProperty())
			        .property("quantityReceivedPackagingUOMUuid", new StringProperty())
			        .property("quantityReceivedPackagingUOMName", new StringProperty())
			        .property("quantityReceivedPackagingUOMFactor", new DecimalProperty())
			        .property("quantityRequested", new DecimalProperty())
			        .property("quantityRequestedPackagingUOMUuid", new StringProperty())
			        .property("quantityRequestedPackagingUOMName", new StringProperty())
			        .property("quantityRequestedPackagingUOMFactor", new DecimalProperty())
			        .property("commonName", new StringProperty()).property("acronym", new StringProperty())
			        .property("purchasePrice", new DecimalProperty()).property("hasExpiration", new BooleanProperty())
			        .property("packagingUnits", new ArrayProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("stockItemName", new StringProperty());
		}
		
		return modelImpl;
	}
}
