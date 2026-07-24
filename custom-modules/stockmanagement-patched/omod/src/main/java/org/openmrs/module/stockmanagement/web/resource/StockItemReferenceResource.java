package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.StringProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockItemReferenceDTO;
import org.openmrs.module.stockmanagement.api.model.StockItemReference;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
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

import java.util.ArrayList;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockitemreference", supportedClass = StockItemReferenceDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockItemReferenceResource extends ResourceBase<StockItemReferenceDTO> {
	
	@Override
	public StockItemReferenceDTO getByUniqueId(String uniqueId) {
		return convertToDTO(getStockManagementService().getStockItemReferenceByUuid(uniqueId));
	}
	
	@Override
	protected void delete(StockItemReferenceDTO delegate, String reason, RequestContext context) throws ResponseException {
		getStockManagementService().voidStockItemReference(delegate.getUuid(), reason,
		    Context.getAuthenticatedUser().getUserId());
	}
	
	@Override
    protected PageableResult doSearch(RequestContext context) {
        String param = context.getParameter("stockItemUuid");
        List<StockItemReferenceDTO> stockItemReferenceDTOS = new ArrayList<>();
        for (StockItemReference stockItemReference : getStockManagementService().getStockItemReferenceByStockItem(param)) {
            stockItemReferenceDTOS.add(convertToDTO(stockItemReference));
        }
        return toAlreadyPaged(stockItemReferenceDTOS, context);
    }
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockItemReferenceDTO newDelegate() {
		return new StockItemReferenceDTO();
	}
	
	@Override
	public StockItemReferenceDTO save(StockItemReferenceDTO delegate) {
		StockItemReference stockItemReference = getStockManagementService().saveStockItemReference(convertFromDTO(delegate));
		return getByUniqueId(stockItemReference.getUuid());
	}
	
	@Override
	public void purge(StockItemReferenceDTO delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("referenceCode");
		description.addProperty("stockSourceUuid");
		description.addProperty("stockItemUuid");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("referenceCode");
		description.addProperty("stockSourceUuid");
		description.addProperty("stockItemUuid");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("referenceCode");
			description.addProperty("stockSourceUuid");
			description.addProperty("stockSourceName");
			description.addProperty("stockItemUuid");
			
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("stockSourceName");
			description.addProperty("uuid");
			description.addProperty("referenceCode");
			description.addProperty("stockSourceUuid");
			description.addProperty("stockSourceName");
			description.addProperty("stockItemUuid");
		}
		
		return description;
	}
	
	@PropertySetter("referenceCode")
	public void setReferenceCode(StockItemReferenceDTO instance, String value) {
		if (value == null) {
			instance.setReferenceCode(null);
		} else {
			instance.setReferenceCode(value);
		}
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("referenceCode", new StringProperty())
			        .property("stockSourceUuid", new StringProperty()).property("stockSourceName", new StringProperty())
			        .property("stockItemUuid", new StringProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {
			
		}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("referenceCode", new StringProperty())
			        .property("stockSourceUuid", new StringProperty()).property("stockSourceName", new StringProperty())
			        .property("stockItemUuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
	public StockItemReferenceDTO convertToDTO(StockItemReference stockItemReference) {
		if (stockItemReference != null) {
			StockItemReferenceDTO stockItemReferenceDTO = new StockItemReferenceDTO();
			stockItemReferenceDTO.setStockItemId(stockItemReference.getStockItem().getId());
			stockItemReferenceDTO.setId(stockItemReference.getStockItem().getId());
			stockItemReferenceDTO.setStockItemUuid(stockItemReference.getStockItem().getUuid());
			stockItemReferenceDTO.setStockSourceName(stockItemReference.getReferenceSource().getName());
			stockItemReferenceDTO.setStockSourceUuid(stockItemReference.getReferenceSource().getUuid());
			stockItemReferenceDTO.setStockSourceId(stockItemReference.getReferenceSource().getId());
			stockItemReferenceDTO.setReferenceCode(stockItemReference.getStockReferenceCode());
			stockItemReferenceDTO.setUuid(stockItemReference.getUuid());
			stockItemReferenceDTO.setVoided(stockItemReference.getVoided());
			return stockItemReferenceDTO;
		} else {
			return null;
		}
	}
	
	public StockItemReference convertFromDTO(StockItemReferenceDTO dto) {
		StockManagementService stockManagementService = Context.getService(StockManagementService.class);
		if (dto != null) {
			StockItemReference stockItemReference = stockManagementService.getStockItemReferenceByUuid(dto.getUuid());
			if (stockItemReference == null) {
				stockItemReference = new StockItemReference();
			}
			stockItemReference.setReferenceSource(stockManagementService.getStockSourceByUuid(dto.getStockSourceUuid()));
			stockItemReference.setStockItem(stockManagementService.getStockItemByUuid(dto.getStockItemUuid()));
			stockItemReference.setStockReferenceCode(dto.getReferenceCode());
			return stockItemReference;
		} else {
			return null;
		}
	}
	
}
