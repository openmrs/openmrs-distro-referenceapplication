package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.dto.NonDrugItem;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.RefRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.AlreadyPaged;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Lists items answering the "Non-drug" bucket concept under "Stock item category"
 * (8ccf6066-9297-4d76-aaf3-00aa3714d198), mirroring the shape of the core /drug resource so the
 * stock item "add item" form can pick a non-pharmaceutical item the same way it picks a drug.
 */
@Resource(name = RestConstants.VERSION_1 + "/non-drug", supportedClass = NonDrugItem.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*" })
public class NonDrugResource extends ResourceBase<NonDrugItem> {

	private static final String NON_DRUG_CATEGORY_UUID = "7b0f51f3-6fcb-4462-8746-3328e11a7d97";

	@Override
	public NonDrugItem getByUniqueId(String uniqueId) {
		Concept concept = Context.getConceptService().getConceptByUuid(uniqueId);
		if (concept == null) {
			return null;
		}
		return new NonDrugItem(concept.getUuid(), concept.getDisplayString());
	}

	@Override
	protected void delete(NonDrugItem delegate, String reason, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}

	@Override
	protected PageableResult doSearch(RequestContext context) {
		return doGetAll(context);
	}

	@Override
	protected PageableResult doGetAll(RequestContext context) {
		Concept nonDrugCategory = Context.getConceptService().getConceptByUuid(NON_DRUG_CATEGORY_UUID);
		List<NonDrugItem> items = new ArrayList<>();
		if (nonDrugCategory != null) {
			String q = context.getParameter("q");
			for (ConceptAnswer answer : nonDrugCategory.getAnswers()) {
				Concept answerConcept = answer.getAnswerConcept();
				String display = answerConcept.getDisplayString();
				if (StringUtils.isBlank(q) || display.toLowerCase().contains(q.toLowerCase())) {
					items.add(new NonDrugItem(answerConcept.getUuid(), display));
				}
			}
		}

		int startIndex = context.getStartIndex();
		Integer limit = context.getLimit();
		List<NonDrugItem> page = items;
		boolean hasMore = false;
		if (startIndex > 0 || (limit != null && limit > 0)) {
			int end = (limit != null && limit > 0) ? Math.min(items.size(), startIndex + limit) : items.size();
			hasMore = end < items.size();
			page = startIndex < items.size() ? items.subList(startIndex, end) : new ArrayList<>();
		}
		return new AlreadyPaged<>(context, page, hasMore, (long) items.size());
	}

	@Override
	public NonDrugItem newDelegate() {
		return new NonDrugItem();
	}

	@Override
	public NonDrugItem save(NonDrugItem delegate) {
		throw new ResourceDoesNotSupportOperationException();
	}

	@Override
	protected String getUniqueId(NonDrugItem delegate) {
		return delegate.getUuid();
	}

	@Override
	public void purge(NonDrugItem delegate, RequestContext context) throws ResponseException {
		throw new ResourceDoesNotSupportOperationException();
	}

	@Override
	public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
		DelegatingResourceDescription description = new DelegatingResourceDescription();
		if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation || rep instanceof RefRepresentation) {
			description.addProperty("uuid");
			description.addProperty("display");
		}
		return description;
	}

	@Override
	public Model getGETModel(Representation rep) {
		ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
		modelImpl.property("uuid", new StringProperty()).property("display", new StringProperty());
		return modelImpl;
	}
}
