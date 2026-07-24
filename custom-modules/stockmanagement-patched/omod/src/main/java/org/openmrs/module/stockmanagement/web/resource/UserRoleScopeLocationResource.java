package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.annotation.SubResource;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@SubResource(parent = UserRoleScopeResource.class, path = "locations", supportedClass = UserRoleScopeLocationDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class UserRoleScopeLocationResource extends SubResourceBase<UserRoleScopeLocationDTO, UserRoleScopeDTO, UserRoleScopeResource> {
	
	@Override
	public UserRoleScopeLocationDTO getByUniqueId(String uniqueId) {
		return null;
	}
	
	@Override
	protected void delete(UserRoleScopeLocationDTO delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public UserRoleScopeLocationDTO newDelegate() {
		return new UserRoleScopeLocationDTO();
	}
	
	@Override
	public UserRoleScopeLocationDTO save(UserRoleScopeLocationDTO delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(UserRoleScopeLocationDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("userRoleScopeId");
			description.addProperty("locationUuid");
			description.addProperty("locationName");
			description.addProperty("enableDescendants");
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
			modelImpl.property("uuid", new StringProperty()).property("userRoleScopeId", new StringProperty())
			        .property("locationUuid", new StringProperty()).property("locationName", new StringProperty())
			        .property("enableDescendants", new BooleanProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
	@Override
	public UserRoleScopeDTO getParent(UserRoleScopeLocationDTO instance) {
		return null;
	}
	
	@Override
	public void setParent(UserRoleScopeLocationDTO instance, UserRoleScopeDTO parent) {
	}
	
	@Override
	public PageableResult doGetAll(UserRoleScopeDTO parent, RequestContext context) throws ResponseException {
		return null;
	}
}
