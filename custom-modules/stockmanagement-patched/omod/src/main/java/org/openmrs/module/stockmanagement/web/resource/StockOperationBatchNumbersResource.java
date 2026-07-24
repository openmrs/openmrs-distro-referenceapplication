package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.StringProperty;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.PropertySetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockoperationbatchnumbers", supportedClass = StockOperationBatchNumbersDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockOperationBatchNumbersResource extends ResourceBase<StockOperationBatchNumbersDTO> {
	
	@Override
	public StockOperationBatchNumbersDTO getByUniqueId(String uniqueId) {
		StockOperationSearchFilter filter = new StockOperationSearchFilter();
		filter.setStockOperationUuid(uniqueId);
		Result<StockOperationDTO> result = getStockManagementService().findStockOperations(filter);
		StockOperationDTO stockOperationDTO = result.getData().isEmpty() ? null : result.getData().get(0);
		return mapToBatchNumbers(stockOperationDTO);
	}
	
	@Override
	protected void delete(StockOperationBatchNumbersDTO delegate, String reason, RequestContext context)
	        throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public StockOperationBatchNumbersDTO newDelegate() {
		return new StockOperationBatchNumbersDTO();
	}
	
	@Override
	public StockOperationBatchNumbersDTO save(StockOperationBatchNumbersDTO delegate) {
		try {
			StockOperationBatchNumbersDTO result = getStockManagementService().saveStockOperationBatchNumbers(delegate);
			return result;
		}
		catch (StockManagementException exception) {
			throw new RestClientException(exception.getMessage());
		}
	}
	
	@Override
	public void purge(StockOperationBatchNumbersDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@PropertyGetter("batchNumbers")
	public List<SimpleObject> getBatchNumbers(StockOperationBatchNumbersDTO instance) {
		if(instance == null || instance.getBatchNumbers() == null) return null;
		List<SimpleObject> result = new ArrayList<>();
		for(StockOperationBatchNumbersDTO.StockOperationItemBatchNumber batchNumber : instance.getBatchNumbers()){
			SimpleObject simpleObject = new SimpleObject();
			simpleObject.add("uuid", batchNumber.getUuid());
			simpleObject.add("batchNo", batchNumber.getBatchNo());
			simpleObject.add("expiration", batchNumber.getExpiration());
			result.add(simpleObject);
		}
		return result;
	}
	
	@PropertySetter("batchNumbers")
	public void setBatchNumbers(StockOperationBatchNumbersDTO instance, ArrayList<Map<String, ?>> items) {
		if (items == null) {
			instance.setBatchNumbers(null);
			return;
		}
		if (items.isEmpty()) {
			instance.setBatchNumbers(new ArrayList<>());
			return;
		}

		instance.setBatchNumbers(new ArrayList<>());
		for (Map<String, ?> item : items) {
			StockOperationBatchNumbersDTO.StockOperationItemBatchNumber itemDTO = new StockOperationBatchNumbersDTO.StockOperationItemBatchNumber();
			boolean isNew = true;
			if (item.containsKey("uuid") && item.get("uuid") != null) {
				itemDTO.setUuid(item.get("uuid").toString());
			}
			if (item.containsKey("batchNo") && item.get("batchNo") != null) {
				itemDTO.setBatchNo(item.get("batchNo").toString());
			}
			if (item.containsKey("expiration") && item.get("expiration") != null) {
				itemDTO.setExpiration((Date) ConversionUtil.convert(item.get("expiration"), Date.class));
			}
			instance.getBatchNumbers().add(itemDTO);
		}
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("batchNumbers");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("batchNumbers");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("batchNumbers");
		}
		
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty());
			modelImpl.addProperty("batchNumbers", new ArrayProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
	private StockOperationBatchNumbersDTO mapToBatchNumbers(StockOperationDTO parent){
		if(parent == null) return null;
		StockOperationBatchNumbersDTO batchNumbersDTO = new StockOperationBatchNumbersDTO();
		batchNumbersDTO.setUuid(parent.getUuid());
		batchNumbersDTO.setId(parent.getId());
		batchNumbersDTO.setBatchNumbers(new ArrayList<>());
		if (parent.getStockOperationItems() != null) {
			for (StockOperationItemDTO stockOperationItemDTO : parent.getStockOperationItems()) {
				StockOperationBatchNumbersDTO.StockOperationItemBatchNumber batchNumber = new StockOperationBatchNumbersDTO.StockOperationItemBatchNumber();
				batchNumber.setId(stockOperationItemDTO.getId());
				batchNumber.setUuid(stockOperationItemDTO.getUuid());
				batchNumber.setBatchNo(stockOperationItemDTO.getBatchNo());
				batchNumber.setExpiration(stockOperationItemDTO.getExpiration());
				batchNumbersDTO.getBatchNumbers().add(batchNumber);
			}
		}
		return batchNumbersDTO;
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		List<StockOperationBatchNumbersDTO> result = new ArrayList<>();
		return toAlreadyPaged(result,context);
	}
}
