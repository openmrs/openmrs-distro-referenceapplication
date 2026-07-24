package org.openmrs.module.stockmanagement.web.resource;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.properties.StringProperty;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.module.stockmanagement.api.dto.DispenseRequest;
import org.openmrs.module.stockmanagement.api.dto.StockItemSearchFilter;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.*;
import org.openmrs.module.webservices.rest.web.resource.api.PageableResult;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.resource.impl.NeedsPaging;
import org.openmrs.module.webservices.rest.web.response.ResourceDoesNotSupportOperationException;
import org.openmrs.module.webservices.rest.web.response.ResponseException;


import java.math.BigDecimal;
import java.util.*;

@Resource(name = RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/dispenserequest", supportedClass = DispenseRequest.class, supportedOpenmrsVersions = {
        "1.9.*", "1.10.*", "1.11.*", "1.12.*", "2.*"})
public class DispenseRequestResource extends ResourceBase<DispenseRequest> {

    @Override
    public DispenseRequest getByUniqueId(String uniqueId) {
        StockItemSearchFilter filter = new StockItemSearchFilter();
        filter.setIncludeVoided(true);
        filter.setUuid(uniqueId);

        return null;
    }

    @Override
    protected void delete(DispenseRequest delegate, String reason, RequestContext context) throws ResponseException {
        throw new ResourceDoesNotSupportOperationException();
    }

    @Override
    protected PageableResult doSearch(RequestContext context) {
        String searchToken = context.getParameter("q");
        if (StringUtils.isBlank(searchToken)) {
            return null;
        } else {
            return null;
        }
    }

    @Override
    public Object create(SimpleObject propertiesToCreate, RequestContext context) throws ResponseException {
        List<DispenseRequest> dispenseRequests = new ArrayList<>();
        SimpleObject dispenseRequestObject = new SimpleObject();
        String DISPENSE_CUSTOM_REP = "(dispenseLocation,patient,order,encounter,stockItem,quantity,stockItemPackagingUOM)";

        if (propertiesToCreate.containsKey("dispenseItems") && !propertiesToCreate.get("dispenseItems").equals(null) && !propertiesToCreate.get("dispenseItems").equals("")) {
            List<SimpleObject> dispenseItems = propertiesToCreate.get("dispenseItems");
            for (int i = 0; i < dispenseItems.size(); i++) {
                dispenseRequests.add(covertToDispenseRequest(dispenseItems.get(i)));
            }

        } else {
            dispenseRequests.add(covertToDispenseRequest(propertiesToCreate));
        }

        getStockManagementService().dispenseStockItems(dispenseRequests);

        return new NeedsPaging<>(dispenseRequests, context);
    }

    public DispenseRequest covertToDispenseRequest(Map dispenseItem) {
        Integer dispenseItemQuantity;
        if (!dispenseItem.get("quantity").equals("") && !dispenseItem.get("quantity").equals(null)) {
            dispenseItemQuantity = Integer.parseInt(dispenseItem.get("quantity").toString());
        } else {
            throw new IllegalArgumentException("quantity can not be null");
        }

        DispenseRequest dispenseRequest = new DispenseRequest();
        Patient patient = null;
        Order order = null;
        Encounter encounter = null;

        if (dispenseItem.get("patient") != null) {
            patient = Context.getPatientService().getPatientByUuid(dispenseItem.get("patient").toString());
        }

        if (dispenseItem.get("encounter") != null) {
            encounter = Context.getEncounterService().getEncounterByUuid(dispenseItem.get("encounter").toString());
        }

        if (dispenseItem.get("order") != null) {
            order = Context.getOrderService().getOrderByUuid(dispenseItem.get("order").toString());
        }

        if (patient != null && order != null && encounter != null) {
            dispenseRequest.setPatientId(patient.getPatientId());
            dispenseRequest.setOrderId(order.getOrderId());
            dispenseRequest.setEncounterId(encounter.getEncounterId());
        } else {
            throw new IllegalArgumentException("patient,order,and encounter can not be null");
        }

        if ((dispenseItem.get("stockItem") != null && dispenseItem.get("stockBatch") != null && dispenseItem.get("stockItemPackagingUOM") != null) && dispenseItem.get("dispenseLocation") != null) {
            dispenseRequest.setLocationUuid(dispenseItem.get("dispenseLocation").toString());
            dispenseRequest.setStockItemUuid(dispenseItem.get("stockItem").toString());
            dispenseRequest.setStockBatchUuid(dispenseItem.get("stockBatch").toString());
            dispenseRequest.setStockItemPackagingUOMUuid(dispenseItem.get("stockItemPackagingUOM").toString());
        } else {
            throw new IllegalArgumentException("stockItem,stockBatch,and stockItemPackagingUOM,dispensingLocation can not be null");
        }

        dispenseRequest.setQuantity(BigDecimal.valueOf(dispenseItemQuantity));
        return dispenseRequest;
    }

    @Override
    protected PageableResult doGetAll(RequestContext context) throws ResponseException {
        return doSearch(context);
    }

    @Override
    public DispenseRequest newDelegate() {
        return new DispenseRequest();
    }

    @Override
    public DispenseRequest save(DispenseRequest delegate) {
        return null;
    }

    @Override
    public void purge(DispenseRequest delegate, RequestContext context) throws ResponseException {
        delete(delegate, null, context);
    }

    @Override
    public DelegatingResourceDescription getCreatableProperties() throws ResourceDoesNotSupportOperationException {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("dispenseLocation");
        description.addProperty("patient");
        description.addProperty("order");
        description.addProperty("encounter");
        description.addProperty("stockItem");
        description.addProperty("stockBatch");
        description.addProperty("quantity");
        description.addProperty("stockItemPackagingUOM");
        description.addProperty("dispenseItems");
        return description;
    }

    @Override
    public DelegatingResourceDescription getUpdatableProperties() {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        description.addProperty("dispenseLocation");
        description.addProperty("patient");
        description.addProperty("order");
        description.addProperty("encounter");
        description.addProperty("stockItem");
        description.addProperty("stockBatch");
        description.addProperty("quantity");
        return description;
    }

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
        DelegatingResourceDescription description = new DelegatingResourceDescription();
        if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
            description.addProperty("dispenseLocation");
            description.addProperty("patient");
            description.addProperty("order");
            description.addProperty("encounter");
            description.addProperty("stockItem");
            description.addProperty("stockBatch");
            description.addProperty("quantity");
        }

        if (rep instanceof DefaultRepresentation) {
            description.addLink("full", ".?v=" + RestConstants.REPRESENTATION_FULL);
        }

        if (rep instanceof FullRepresentation) {
            description.addProperty("dispenseLocation");
            description.addProperty("patient");
            description.addProperty("order");
            description.addProperty("encounter");
            description.addProperty("stockItem");
            description.addProperty("stockBatch");
            description.addProperty("quantity");
            description.addSelfLink();
        }

        if (rep instanceof RefRepresentation) {
            description.addProperty("dispenseLocation");
            description.addProperty("patient");
            description.addProperty("order");
            description.addProperty("encounter");
            description.addProperty("stockItem");
            description.addProperty("stockBatch");
            description.addProperty("quantity");
        }

        return description;
    }

    @Override
    public Model getGETModel(Representation rep) {
        ModelImpl modelImpl = (ModelImpl) super.getGETModel(rep);
        if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
            modelImpl.property("dispenseLocation", new StringProperty()).property("patient", new StringProperty())
                    .property("order", new StringProperty()).property("encounter", new StringProperty())
                    .property("stockItem", new StringProperty()).property("stockBatch", new StringProperty())
                    .property("quantity", new StringProperty());
        }
        if (rep instanceof DefaultRepresentation) {
        }

        if (rep instanceof FullRepresentation) {
        }

        if (rep instanceof RefRepresentation) {
            modelImpl.property("dispenseLocation", new StringProperty()).property("patient", new StringProperty())
                    .property("order", new StringProperty()).property("encounter", new StringProperty())
                    .property("stockItem", new StringProperty()).property("stockBatch", new StringProperty())
                    .property("quantity", new StringProperty());
        }
        return modelImpl;
    }

}
