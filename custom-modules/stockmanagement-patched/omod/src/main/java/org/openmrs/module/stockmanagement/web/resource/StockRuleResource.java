package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.dto.*;
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
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.IllegalRequestException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.math.BigDecimal;
import java.util.*;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/stockrule", supportedClass = StockRuleDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class StockRuleResource extends ResourceBase<StockRuleDTO> {
	
	private HashSet<PrivilegeScope> privilegeScopes = null;
	
	@Override
	public StockRuleDTO getByUniqueId(String uniqueId) {
		StockRuleSearchFilter filter = new StockRuleSearchFilter();
		filter.setUuids(Arrays.asList(uniqueId));
		Result<StockRuleDTO> result = getStockManagementService().findStockRules(filter, null);
		return result.getData().isEmpty() ? null : result.getData().get(0);
	}
	
	@Override
	protected void delete(StockRuleDTO delegate, String reason, RequestContext context) throws ResponseException {
		if (reason != null && reason.length() > 250) {
			throw new IllegalRequestException("Parameter reason can not exceed 250 characters");
		}
		List<String> stockRulesToDelete = new ArrayList<>();
		stockRulesToDelete.add(delegate.getUuid());
		String ids = context.getParameter("ids");
		if (ids != null && StringUtils.isNotEmpty(ids)) {
			for (String id : ids.split(",")) {
				if (id.isEmpty()) continue;
				if (id.length() > 38) {
					throw new IllegalRequestException("Id not recognized");
				}
				stockRulesToDelete.add(id);
			}
		}
		getStockManagementService().voidStockRules(stockRulesToDelete, reason, Context.getAuthenticatedUser().getId());
	}
	
	@Override
	protected PageableResult doSearch(RequestContext context) {
		String locationUuid = null;
		String stockItemUuid = null;
		
		String param = context.getParameter("locationUuid");
		if (!StringUtils.isBlank(param)) {
			locationUuid = param;
		}
		
		param = context.getParameter("stockItemUuid");
		if (!StringUtils.isBlank(param)) {
			stockItemUuid = param;
		}
		
		StockRuleSearchFilter filter = new StockRuleSearchFilter();
		if (locationUuid != null) {
			filter.setLocationUuids(Arrays.asList(locationUuid));
		}
		if (stockItemUuid != null) {
			filter.setStockItemUuids(Arrays.asList(stockItemUuid));
		}
		
		filter.setIncludeVoided(context.getIncludeAll());
		filter.setStartIndex(context.getStartIndex());
		filter.setLimit(context.getLimit());
		Result<StockRuleDTO> result = getStockManagementService().findStockRules(filter);
		return toAlreadyPaged(result, context);
	}
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public StockRuleDTO newDelegate() {
		return new StockRuleDTO();
	}
	
	@Override
	public StockRuleDTO save(StockRuleDTO delegate) {
		return getStockManagementService().saveStockRule(delegate);
	}
	
	@Override
	public void purge(StockRuleDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("stockItemUuid");
		description.addProperty("name");
		description.addProperty("description");
		description.addProperty("locationUuid");
		description.addProperty("quantity");
		description.addProperty("stockItemPackagingUOMUuid");
		description.addProperty("enabled");
		description.addProperty("evaluationFrequency");
		description.addProperty("actionFrequency");
		description.addProperty("alertRole");
		description.addProperty("mailRole");
		description.addProperty("enableDescendants");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("name");
		description.addProperty("description");
		description.addProperty("quantity");
		description.addProperty("stockItemPackagingUOMUuid");
		description.addProperty("enabled");
		description.addProperty("evaluationFrequency");
		description.addProperty("actionFrequency");
		description.addProperty("alertRole");
		description.addProperty("mailRole");
		description.addProperty("enableDescendants");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("uuid");
			description.addProperty("stockItemUuid");
			description.addProperty("name");
			description.addProperty("description");
			description.addProperty("locationUuid");
			description.addProperty("locationName");
			description.addProperty("quantity");
			description.addProperty("stockItemPackagingUOMUuid");
			description.addProperty("packagingUomName");
			description.addProperty("enabled");
			description.addProperty("evaluationFrequency");
			description.addProperty("lastEvaluation");
			description.addProperty("nextEvaluation");
			description.addProperty("actionFrequency");
			description.addProperty("lastActionDate");
			description.addProperty("alertRole");
			description.addProperty("mailRole");
			description.addProperty("dateCreated");
			description.addProperty("creatorGivenName");
			description.addProperty("creatorFamilyName");
			description.addProperty("enableDescendants");
			description.addProperty("nextActionDate");
			
		}
		
		if (rep instanceof DefaultRepresentation) {
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			description.addProperty("permission");
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
		}
		
		return description;
	}
	
	@PropertySetter("quantity")
	public void setQuantity(StockRuleDTO instance, Double value) {
		if (value == null) {
			instance.setQuantity(null);
		} else {
			instance.setQuantity(BigDecimal.valueOf(value));
		}
	}
	
	@PropertySetter("evaluationFrequency")
	public void setEvaluationFrequency(StockRuleDTO instance, Integer value) {
		if (value == null) {
			instance.setEvaluationFrequency(null);
		} else {
			instance.setEvaluationFrequency(Long.valueOf(value.longValue()));
		}
	}
	
	@PropertySetter("actionFrequency")
	public void setActionFrequency(StockRuleDTO instance, Double value) {
		if (value == null) {
			instance.setActionFrequency(null);
		} else {
			instance.setActionFrequency(Long.valueOf(value.longValue()));
		}
	}
	
	@PropertyGetter("permission")
	public SimpleObject getPermission(StockRuleDTO stockRuleDTO) {
		SimpleObject simpleObject = new SimpleObject();
		simpleObject.add("canView", true);

		if(privilegeScopes == null){
			privilegeScopes  = getStockManagementService().getPrivilegeScopes(Context.getAuthenticatedUser(), null, null, Arrays.asList(Privileges.TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE));
		}

		boolean canEdit = !stockRuleDTO.getVoided() && privilegeScopes.stream().anyMatch(p-> stockRuleDTO.getLocationUuid().equals(p.getLocationUuid()));
		simpleObject.add("canEdit", canEdit);
		return simpleObject;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("uuid", new StringProperty()).property("stockItemUuid", new StringProperty())
			        .property("name", new StringProperty()).property("description", new StringProperty())
			        .property("locationUuid", new StringProperty()).property("locationName", new StringProperty())
			        .property("quantity", new DecimalProperty()).property("stockItemPackagingUOMUuid", new StringProperty())
			        .property("packagingUomName", new StringProperty()).property("enabled", new BooleanProperty())
			        .property("evaluationFrequency", new LongProperty()).property("lastEvaluation", new DateTimeProperty())
			        .property("nextEvaluation", new DateTimeProperty()).property("actionFrequency", new LongProperty())
			        .property("lastActionDate", new DateTimeProperty()).property("alertRole", new StringProperty())
			        .property("mailRole", new StringProperty()).property("creator", new IntegerProperty())
			        .property("dateCreated", new DateTimeProperty()).property("creatorGivenName", new StringProperty())
			        .property("creatorFamilyName", new StringProperty()).property("nextActionDate", new DateTimeProperty())
			        .property("enableDescendants", new BooleanProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {
			modelImpl.property("uuid", new StringProperty());
		}
		
		return modelImpl;
	}
	
}
