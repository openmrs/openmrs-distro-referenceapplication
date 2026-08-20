package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface StockFlowService extends OpenmrsService {

	/**
	 * How much of each stock item was actually issued out of (consumed at) one or all
	 * locations over a date range - i.e. stock leaving the system for patient use, not
	 * internal transfers between locations.
	 *
	 * @param startDate only include activity on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include activity through the end of this date (inclusive), or null for no upper bound
	 * @param locationUuid only include this location's consumption, or null for all locations combined
	 * @return one row per stock item (and, when locationUuid is null, per consuming location too)
	 */
	List<StockLocationQtyRow> getConsumptionByLocation(Date startDate, Date endDate, String locationUuid);

	/**
	 * How much of each stock item was transferred out from a given source location to each
	 * destination location over a date range - e.g. distribution from a central Main Store
	 * out to clinic locations.
	 *
	 * @param startDate only include activity on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include activity through the end of this date (inclusive), or null for no upper bound
	 * @param sourceLocationUuid only include transfers out of this location, or null for all sources combined
	 * @return one row per stock item per destination location that received a transfer
	 */
	List<StockLocationQtyRow> getDistributionFromSource(Date startDate, Date endDate, String sourceLocationUuid);

	/**
	 * How much of each stock item was disposed of (expired or damaged, removed from circulation)
	 * at each location over a date range.
	 *
	 * @param startDate only include activity on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include activity through the end of this date (inclusive), or null for no upper bound
	 * @param locationUuid only include this location's disposals, or null for all locations combined
	 * @return one row per stock item (and, when locationUuid is null, per location too)
	 */
	List<StockLocationQtyRow> getWastageByLocation(Date startDate, Date endDate, String locationUuid);
}
