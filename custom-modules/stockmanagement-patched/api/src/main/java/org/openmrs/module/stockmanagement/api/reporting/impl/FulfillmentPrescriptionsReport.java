package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementException;
import org.openmrs.module.stockmanagement.api.dto.reporting.Fullfillment;
import org.openmrs.module.stockmanagement.api.dto.reporting.PrescriptionLineFilter;

import java.util.List;
import java.util.Properties;

public class FulfillmentPrescriptionsReport extends PrescribedDrugsReport {
	
	@Override
	protected boolean isFullfullmentReport() {
		return true;
	}
	
	@Override
	protected void setAdditionFilters(PrescriptionLineFilter filter, Properties parameters) {
		List<Fullfillment> fullfillments = getFullfillment(parameters);
		if (fullfillments == null || fullfillments.isEmpty()) {
			throw new StockManagementException(Context.getMessageSourceService().getMessage(
			    "stockmanagement.report.fullfillmentparameterrequired"));
		}
		filter.setFullfillments(fullfillments);
	}
}
