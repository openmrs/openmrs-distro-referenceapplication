package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
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

@SubResource(parent = UserRoleScopeResource.class, path = "operationtypes", supportedClass = UserRoleScopeOperationTypeDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class UserRoleScopeOperationTypeResource extends SubResourceBase<UserRoleScopeOperationTypeDTO, UserRoleScopeDTO, UserRoleScopeResource> {
	
	@Override
	public UserRoleScopeOperationTypeDTO getByUniqueId(String uniqueId) {
		return null;
	}
	
	@Override
	protected void delete(UserRoleScopeOperationTypeDTO delegate, String reason, RequestContext context)
	        throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public UserRoleScopeOperationTypeDTO newDelegate() {
		return new UserRoleScopeOperationTypeDTO();
	}
	
	@Override
	public UserRoleScopeOperationTypeDTO save(UserRoleScopeOperationTypeDTO delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public void purge(UserRoleScopeOperationTypeDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("userRoleScopeId");
			description.addProperty("operationTypeName");
			description.addProperty("operationTypeUuid");
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
			        .property("operationTypeName", new StringProperty()).property("operationTypeUuid", new StringProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
	@Override
	public UserRoleScopeDTO getParent(UserRoleScopeOperationTypeDTO instance) {
		return null;
	}
	
	@Override
	public void setParent(UserRoleScopeOperationTypeDTO instance, UserRoleScopeDTO parent) {
	}
	
	@Override
	public PageableResult doGetAll(UserRoleScopeDTO parent, RequestContext context) throws ResponseException {
		return null;
	}
}
