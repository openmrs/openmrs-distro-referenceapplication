package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
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

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/location", supportedClass = LocationDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class LocationResource extends ResourceBase<LocationDTO> {
	
	@Override
	public LocationDTO getByUniqueId(String uniqueId) {
		Location location = null;
		if (!StringUtils.isBlank(uniqueId)) {
			if (StringUtils.isNumeric(uniqueId)) {
				Integer locationId = Integer.parseInt(uniqueId);
				location = Context.getLocationService().getLocation(locationId);
			} else {
				location = Context.getLocationService().getLocationByUuid(uniqueId);
			}
		}
		return mapLocation(location);
	}
	
	private LocationDTO mapLocation(Location location) {
		if (location == null)
			return null;
		LocationDTO locationDTO = new LocationDTO();
		locationDTO.setId(location.getId());
		locationDTO.setDisplay(location.getDisplayString());
		locationDTO.setName(location.getName());
		locationDTO.setUuid(location.getUuid());
		return locationDTO;
	}
	
	@Override
	protected void delete(LocationDTO delegate, String reason, RequestContext context) throws ResponseException {
		if (delegate == null)
			return;
		getStockManagementService().deleteLocation(delegate.getUuid());
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public LocationDTO newDelegate() {
		return new LocationDTO();
	}
	
	@Override
	public LocationDTO save(LocationDTO delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(LocationDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("id");
			description.addProperty("uuid");
			description.addProperty("name");
			description.addProperty("display");
			
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("id");
			
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("id", new IntegerProperty()).property("uuid", new StringProperty())
			        .property("name", new StringProperty()).property("display", new StringProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
			modelImpl.property("id", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
