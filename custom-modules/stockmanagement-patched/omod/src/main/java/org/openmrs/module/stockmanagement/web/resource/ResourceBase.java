package org.openmrs.module.stockmanagement.web.resource;

import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.Result;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

public abstract class ResourceBase<T> extends DelegatingCrudResource<T> {
	
	private StockManagementService stockManagementService;
	
	protected StockManagementService getStockManagementService() {
		if (stockManagementService == null) {
			stockManagementService = Context.getService(StockManagementService.class);
		}
		return stockManagementService;
	}
	
	protected void forbidden() {
		throw new RestClientException("403: Forbidden");
	}
	
	protected void notFound() {
		throw new RestClientException("404: Forbidden");
	}
	
	protected void invalidRequest(String messageKey, Object... args) {
		if (args != null && args.length > 0)
			throw new RestClientException(String.format(Context.getMessageSourceService().getMessage(messageKey), args));
		else
			throw new RestClientException(Context.getMessageSourceService().getMessage(messageKey));
	}
	
	protected void requirePriviledge(String priviledge) {
		UserContext userContext = Context.getUserContext();
		if (!userContext.hasPrivilege(priviledge))
			forbidden();
	}
	
	protected String nullIfEmpty(String string) {
		if (string == null)
			return null;
		string = string.trim();
		if (string.isEmpty())
			return null;
		return string;
	}
	
	protected <E> AlreadyPaged<E> emptyResult(RequestContext context) {
		return new AlreadyPaged<E>(context, new ArrayList<E>(), false);
	}
	
	protected <E> AlreadyPaged<E> toAlreadyPaged(Result<E> result, RequestContext context) {
		return new AlreadyPaged<E>(context, result.getData(), result.hasMoreResults(), result.getTotalRecordCount());
	}
	
	protected <E> AlreadyPaged<E> toAlreadyPaged(List<E> result, RequestContext context) {
		return new AlreadyPaged<E>(context, result, false, (long) result.size());
	}
	
}
