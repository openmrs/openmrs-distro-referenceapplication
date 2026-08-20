package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface StockLedgerService extends OpenmrsService {

	/**
	 * @param startDate only include activity on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include activity through the end of this date (inclusive), or null for no upper bound
	 * @param locationUuid only include this location's activity, or null for all locations combined
	 * @return the sparse stock ledger report: one row per stock item/day it actually had activity
	 */
	List<StockLedgerRow> getLedgerReport(Date startDate, Date endDate, String locationUuid);
}
