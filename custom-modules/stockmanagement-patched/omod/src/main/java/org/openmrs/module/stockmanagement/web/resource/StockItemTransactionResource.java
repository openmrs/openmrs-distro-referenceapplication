package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.Date;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockitemtransaction", supportedClass = StockItemTransactionDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockItemTransactionResource extends ResourceBase<StockItemTransactionDTO> {
	
	@Override
	public StockItemTransactionDTO getByUniqueId(String uniqueId) {
		StockItemTransactionSearchFilter filter = new StockItemTransactionSearchFilter();
		filter.setUuid(uniqueId);
		List<StockItemTransactionDTO> result = getStockManagementService().findStockItemTransactions(filter).getData();
		return result.isEmpty() ? null : result.get(0);
	}
	
	@Override
	protected void delete(StockItemTransactionDTO delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		StockItemTransactionSearchFilter filter = new StockItemTransactionSearchFilter();
		filter.setStartIndex(context.getStartIndex());
		filter.setLimit(context.getLimit());
		String param = context.getParameter("stockItemUuid");
		if (StringUtils.isNotBlank(param)) {
			StockItem stockItem = getStockManagementService().getStockItemByUuid(param);
			if (stockItem == null) {
				return emptyResult(context);
			}
			filter.setStockItemId(stockItem.getId());
		}
		
		param = context.getParameter("partyUuid");
		if (StringUtils.isNotBlank(param)) {
			Party party = getStockManagementService().getPartyByUuid(param);
			if (party == null) {
				return emptyResult(context);
			}
			filter.setPartyId(party.getId());
		}
		
		param = context.getParameter("stockOperationUuid");
		if (StringUtils.isNotBlank(param)) {
			StockOperation stockOperation = getStockManagementService().getStockOperationByUuid(param);
			if (stockOperation == null) {
				return emptyResult(context);
			}
			filter.setStockOperationId(stockOperation.getId());
		}
		
		param = context.getParameter("dateMin");
		if (StringUtils.isNotBlank(param)) {
			Date date = (Date) ConversionUtil.convert(param, Date.class);
			if (date == null) {
				return emptyResult(context);
			}
			filter.setTransactionDateMin(date);
		}
		
		param = context.getParameter("dateMax");
		if (StringUtils.isNotBlank(param)) {
			Date date = (Date) ConversionUtil.convert(param, Date.class);
			if (date == null) {
				return emptyResult(context);
			}
			filter.setTransactionDateMax(date);
		}

		param = context.getParameter("isPatientTransaction");
		if (StringUtils.isNotBlank(param)) {
			if(param.trim().equalsIgnoreCase("true")) {
				filter.setIsPatientTransaction(true);
			} else if(param.trim().equalsIgnoreCase("false")) {
				filter.setIsPatientTransaction(false);
			}
		}
		
		StockManagementService stockManagementService = getStockManagementService();
		Result<StockItemTransactionDTO> result = stockManagementService.findStockItemTransactions(filter);
		return toAlreadyPaged(result, context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockItemTransactionDTO newDelegate() {
		return new StockItemTransactionDTO();
	}
	
	@Override
	public StockItemTransactionDTO save(StockItemTransactionDTO delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(StockItemTransactionDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("dateCreated");
			description.addProperty("partyUuid");
			description.addProperty("partyName");
			description.addProperty("isPatientTransaction");
			description.addProperty("quantity");
			description.addProperty("stockBatchUuid");
			description.addProperty("stockBatchNo");
			description.addProperty("expiration");
			description.addProperty("stockItemUuid");
			description.addProperty("stockOperationUuid");
			description.addProperty("stockOperationStatus");
			description.addProperty("stockOperationNumber");
			description.addProperty("stockOperationTypeName");
			description.addProperty("stockItemPackagingUOMUuid");
			description.addProperty("packagingUomName");
			description.addProperty("packagingUomFactor");
			description.addProperty("operationSourcePartyName");
			description.addProperty("operationDestinationPartyName");
			description.addProperty("patientId");
			description.addProperty("patientUuid");
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
	
	@PropertyGetter("isPatientTransaction")
	public boolean getIsPatientTransaction(StockItemTransactionDTO stockItemTransactionDTO) {
		return stockItemTransactionDTO != null
		        && (stockItemTransactionDTO.getPatientId() != null || stockItemTransactionDTO.getOrderId() != null || stockItemTransactionDTO
		                .getEncounterId() != null);
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("dateCreated", new DateTimeProperty())
			        .property("partyUuid", new StringProperty()).property("partyName", new StringProperty())
			        .property("isPatientTransaction", new BooleanProperty()).property("quantity", new DecimalProperty())
			        .property("stockBatchUuid", new StringProperty()).property("stockBatchNo", new StringProperty())
			        .property("expiration", new StringProperty()).property("stockItemUuid", new StringProperty())
			        .property("stockOperationUuid", new StringProperty())
			        .property("stockOperationStatus", new StringProperty())
			        .property("stockOperationNumber", new StringProperty())
			        .property("stockOperationTypeName", new StringProperty())
			        .property("stockItemPackagingUOMUuid", new StringProperty())
			        .property("packagingUomName", new StringProperty())
			        .property("packagingUomFactor", new DecimalProperty())
			        .property("operationSourcePartyName", new StringProperty())
			        .property("operationDestinationPartyName", new StringProperty())
					.property("patientId", new IntegerProperty())
					.property("patientUuid", new StringProperty())
					;
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
