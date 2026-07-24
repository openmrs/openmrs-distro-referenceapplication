package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.*;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.BatchJobDTO;
import org.openmrs.module.stockmanagement.api.dto.BatchJobSearchFilter;
import org.openmrs.module.stockmanagement.api.dto.Result;
import org.openmrs.module.stockmanagement.api.model.BatchJobStatus;
import org.openmrs.module.stockmanagement.api.model.BatchJobType;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
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
import org.openmrs.module.webservices.rest.web.response.IllegalRequestException;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/batchjob", supportedClass = BatchJobDTO.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class BatchJobResource extends ResourceBase<BatchJobDTO> {
	
	@Override
	public BatchJobDTO getByUniqueId(String uniqueId) {
		if (StringUtils.isBlank(uniqueId))
			return null;
		
		BatchJobSearchFilter batchJobSearchFilter = new BatchJobSearchFilter();
		batchJobSearchFilter.setBatchJobUuids(Arrays.asList(uniqueId));
		Result<BatchJobDTO> jobs = getStockManagementService().findBatchJobs(batchJobSearchFilter);
		return jobs.getData().isEmpty() ? null : jobs.getData().get(0);
	}
	
	@Override
    protected void delete(BatchJobDTO delegate, String reason, RequestContext context) throws ResponseException {
        if (reason != null && reason.length() > 250) {
            throw new IllegalRequestException("Parameter reason can not exceed 250 characters");
        }
		if("web service call".equals(reason)){
			reason = null;
		}
        List<String> batchJobsToDelete = new ArrayList<>();
        batchJobsToDelete.add(delegate.getUuid());
        String ids = context.getParameter("ids");
        if (ids != null && StringUtils.isNotEmpty(ids)) {
            for (String id : ids.split(",")) {
                if (id.isEmpty()) continue;
                if (id.length() > 38) {
                    throw new IllegalRequestException("Id not recognized");
                }
                batchJobsToDelete.add(id);
            }
        }
        for (String uuid : batchJobsToDelete){
            getStockManagementService().cancelBatchJob(uuid, reason);
        }
    }
	
	@Override
    protected PageableResult doSearch(RequestContext context) {
        BatchJobSearchFilter filter = new BatchJobSearchFilter();
        filter.setIncludeVoided(context.getIncludeAll());
        filter.setStartIndex(context.getStartIndex());
        filter.setLimit(context.getLimit());
        filter.setBatchJobType(BatchJobType.Report);

        StockManagementService stockManagementService = getStockManagementService();
        String param = context.getParameter("status");
        if (StringUtils.isNotBlank(param)) {
            String[] params = param.split(",", 10);
            List<BatchJobStatus> statusIds = new ArrayList<>();
            for (String status : params) {
                BatchJobStatus opStatus = (BatchJobStatus) Enum.valueOf(BatchJobStatus.class, status);
                statusIds.add(opStatus);
            }
            if (statusIds.isEmpty()) {
                return emptyResult(context);
            }
            filter.setBatchJobStatus(statusIds);
        }

        param = context.getParameter("locationScopeUuid");
        if (StringUtils.isNotBlank(param)) {
            Location location = Context.getLocationService().getLocationByUuid(param);
            if (location == null) {
                return emptyResult(context);
            }
            filter.setLocationScopeIds(Arrays.asList(location.getId()));
        }

        param = context.getParameter("dateCreatedMin");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setDateCreatedMin(date);
        }

        param = context.getParameter("dateCreatedMax");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setDateCreatedMax(date);
        }

        param = context.getParameter("completedDateMin");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setCompletedDateMin(date);
        }

        param = context.getParameter("completedDateMax");
        if (StringUtils.isNotBlank(param)) {
            Date date = (Date) ConversionUtil.convert(param, Date.class);
            if (date == null) {
                return emptyResult(context);
            }
            filter.setCompletedDateMax(date);
        }

        Result<BatchJobDTO> result = getStockManagementService().findBatchJobs(filter);
        return toAlreadyPaged(result, context);
    }
	
	@Override
	protected PageableResult doGetAll(RequestContext context) throws ResponseException {
		return doSearch(context);
	}
	
	@Override
	public BatchJobDTO newDelegate() {
		return new BatchJobDTO();
	}
	
	@Override
	public BatchJobDTO save(BatchJobDTO delegate) {
		return getStockManagementService().saveBatchJob(delegate);
	}
	
	@PropertySetter("batchJobType")
	public void setBatchJobType(BatchJobDTO instance, String value) {
		if (value == null) {
			instance.setBatchJobType(null);
		} else {
			instance.setBatchJobType(BatchJobType.valueOf(value));
		}
	}
	
	@Override
	public void purge(BatchJobDTO delegate, RequestContext context) throws ResponseException {
		delete(delegate, null, context);
	}
	
	@Override
	public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		description.addProperty("batchJobType");
		description.addProperty("description");
		description.addProperty("parameters");
		return description;
	}
	
	@Override
	public DelegatingResourceDescription getUpdatableProperties() {
		throw new ResourceDoesNotSupportOperationException();
	}
	
	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			description.addProperty("batchJobType");
			description.addProperty("status");
			description.addProperty("description");
			description.addProperty("startTime");
			description.addProperty("endTime");
			description.addProperty("expiration");
			description.addProperty("parameters");
			description.addProperty("privilegeScope");
			description.addProperty("locationScope");
			description.addProperty("locationScopeUuid");
			description.addProperty("executionState");
			description.addProperty("cancelReason");
			description.addProperty("cancelledByUuid");
			description.addProperty("cancelledByGivenName");
			description.addProperty("cancelledByFamilyName");
			description.addProperty("cancelledDate");
			description.addProperty("exitMessage");
			description.addProperty("completedDate");
			description.addProperty("dateCreated");
			description.addProperty("creatorUuid");
			description.addProperty("creatorGivenName");
			description.addProperty("creatorFamilyName");
			description.addProperty("outputArtifactSize");
			description.addProperty("outputArtifactFileExt");
			description.addProperty("outputArtifactViewable");
			description.addProperty("uuid");
			description.addProperty("owners");
		}
		
		if (rep instanceof DefaultRepresentation) {
			
			description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
		}
		
		if (rep instanceof FullRepresentation) {
			
			description.addSelfLink();
		}
		
		if (rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("description");
		}
		
		return description;
	}
	
	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
			modelImpl.property("batchJobType", new StringProperty()).property("status", new StringProperty())
			        .property("description", new StringProperty()).property("startTime", new DateTimeProperty())
			        .property("endTime", new DateTimeProperty()).property("expiration", new DateTimeProperty())
			        .property("parameters", new StringProperty()).property("privilegeScope", new StringProperty())
			        .property("locationScope", new StringProperty()).property("locationScopeUuid", new IntegerProperty())
			        .property("executionState", new StringProperty()).property("cancelReason", new StringProperty())
			        .property("cancelledByUuid", new StringProperty())
			        .property("cancelledByGivenName", new StringProperty())
			        .property("cancelledByFamilyName", new StringProperty())
			        .property("cancelledDate", new DateTimeProperty()).property("exitMessage", new StringProperty())
			        .property("completedDate", new DateTimeProperty()).property("dateCreated", new DateTimeProperty())
			        .property("creatorUuid", new StringProperty()).property("creatorGivenName", new StringProperty())
			        .property("creatorFamilyName", new StringProperty()).property("uuid", new StringProperty())
			        .property("owners", new ArrayProperty()).property("outputArtifactSize", new LongProperty())
			        .property("outputArtifactFileExt", new StringProperty())
			        .property("outputArtifactViewable", new BooleanProperty());
		}
		if (rep instanceof DefaultRepresentation) {}
		
		if (rep instanceof FullRepresentation) {}
		
		if (rep instanceof RefRepresentation) {}
		
		return modelImpl;
	}
	
}
