package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.PropertyGetter;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.IllegalRequestException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.*;
import java.util.stream.Collectors;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/userrolescope", supportedClass = UserRoleScopeDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class UserRoleScopeResource extends ResourceBase<UserRoleScopeDTO> {
	
	@Override
	public UserRoleScopeDTO getByUniqueId(String uniqueId) {
		Result<UserRoleScopeDTO> result = getStockManagementService().findUserRoleScopes(
		    new UserRoleScopeSearchFilter(uniqueId).withIncludeVoided(false));
		return !result.getData().isEmpty() ? result.getData().get(0) : null;
	}
	
	@Override
    protected void delete(UserRoleScopeDTO delegate, String reason, RequestContext context) throws ResponseException {
        if (reason != null && reason.length() > 250) {
            throw new IllegalRequestException("Parameter reason can not exceed 250 characters");
        }
        List<String> userRoleScopesToDelete = new ArrayList<>();
        userRoleScopesToDelete.add(delegate.getUuid());
        String ids = context.getParameter("ids");
        if (ids != null && StringUtils.isNotEmpty(ids)) {
            for (String id : ids.split(",")) {
                if (id.isEmpty()) continue;
                if (id.length() > 38) {
                    throw new IllegalRequestException("Id not recognized");
                }
                userRoleScopesToDelete.add(id);
            }
        }
        getStockManagementService().voidUserRoleScopes(userRoleScopesToDelete, reason);
    }
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String q = nullIfEmpty(context.getRequest().getParameter("q"));
		String locationUuid = nullIfEmpty(context.getRequest().getParameter("locationUuid"));
		String operationTypeUuid = nullIfEmpty(context.getRequest().getParameter("operationTypeUuid"));
		
		UserRoleScopeSearchFilter filter = new UserRoleScopeSearchFilter();
		
		if (locationUuid != null) {
			Location location = Context.getLocationService().getLocationByUuid(locationUuid);
			if (location == null)
				return emptyResult(context);
			filter.setLocation(location);
		}
		
		if (operationTypeUuid != null) {
			StockOperationType stockOperationType = getStockManagementService().getStockOperationTypeByUuid(
			    operationTypeUuid);
			if (stockOperationType == null)
				return emptyResult(context);
			filter.setOperationType(stockOperationType);
		}
		
		if (q != null)
			filter.setName(q);
		
		filter.setLimit(context.getLimit());
		filter.setStartIndex(context.getStartIndex());
		
		Result<UserRoleScopeDTO> result = getStockManagementService().findUserRoleScopes(filter);
		return toAlreadyPaged(result, context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public UserRoleScopeDTO newDelegate() {
		return new UserRoleScopeDTO();
	}
	
	@Override
	public UserRoleScopeDTO save(UserRoleScopeDTO delegate) {
		UserRoleScope userRoleScope = getStockManagementService().saveUserRoleScope(delegate);
		return getByUniqueId(userRoleScope.getUuid());
	}
	
	@Override
	public void purge(UserRoleScopeDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("userUuid");
		description.addProperty("role");
		description.addProperty("permanent");
		description.addProperty("activeFrom");
		description.addProperty("activeTo");
		description.addProperty("enabled");
		description.addProperty("locations");
		description.addProperty("operationTypes");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("role");
		description.addProperty("permanent");
		description.addProperty("activeFrom");
		description.addProperty("activeTo");
		description.addProperty("enabled");
		description.addProperty("locations");
		description.addProperty("operationTypes");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("uuid");
		description.addProperty("userUuid");
		description.addProperty("userName");
		description.addProperty("userGivenName");
		description.addProperty("userFamilyName");
		description.addProperty("role");
		description.addProperty("permanent");
		description.addProperty("activeFrom");
		description.addProperty("activeTo");
		description.addProperty("enabled");
		description.addProperty("locations", Representation.FULL);
		description.addProperty("operationTypes", Representation.FULL);
		
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
	
	@PropertyGetter("locations")
    public Collection<SimpleObject> getLocations(UserRoleScopeDTO userRoleScope) {
        if (userRoleScope.getLocations() == null || userRoleScope.getLocations().size() == 0) {
            return new ArrayList<SimpleObject>();
        }

        return userRoleScope.getLocations().stream()
                .map(p -> new SimpleObject().add("locationUuid", p.getLocationUuid()).
                        add("locationName", p.getLocationName()).
                        add("enableDescendants", p.getEnableDescendants()).
                        add("uuid", p.getUuid()))
                .collect(Collectors.toList());

    }
	
	@PropertyGetter("operationTypes")
    public Collection<SimpleObject> getOperationTypes(UserRoleScopeDTO userRoleScope) {
        if (userRoleScope.getOperationTypes() == null || userRoleScope.getOperationTypes().size() == 0) {
            return new ArrayList<SimpleObject>();
        }

        return userRoleScope.getOperationTypes().stream()
                .map(p -> new SimpleObject().add("uuid", p.getUuid()).
                        add("operationTypeName", p.getOperationTypeName()).
                        add("operationTypeUuid", p.getOperationTypeUuid()))
                .collect(Collectors.toList());
    }
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("permanent", new BooleanProperty())
			        .property("activeFrom", new DateTimeProperty()).property("activeTo", new DateTimeProperty())
			        .property("enabled", new BooleanProperty()).property("locations", new ArrayProperty())
			        .property("operationTypes", new ArrayProperty());
		}
		if (rep instanceof DefaultRepresentation) {
			
		}
		
		if (rep instanceof FullRepresentation) {
			
		}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
}
