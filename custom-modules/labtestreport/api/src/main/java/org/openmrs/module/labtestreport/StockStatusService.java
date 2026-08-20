package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface StockStatusService extends OpenmrsService {

	/**
	 * Batches nearing (or past) expiry, with their remaining quantity, so stock can be routed to
	 * where it'll be used before it expires.
	 *
	 * @param daysAhead only include batches expiring within this many days from today, or null for
	 *            all batches with a remaining quantity and a recorded expiration date
	 * @param locationUuid only include this location's batches, or null for all locations
	 * @return one row per batch per location holding a remaining quantity of it, soonest-expiring first
	 */
	List<StockBatchExpiryRow> getExpiryRisk(Integer daysAhead, String locationUuid);

	/**
	 * Current on-hand quantity per item/location against its average daily consumption over a
	 * date range, projecting how many days of stock remain at that rate.
	 *
	 * @param startDate start of the window used to compute the average daily consumption rate;
	 *            defaults to 30 days before endDate when null
	 * @param endDate end of the averaging window (inclusive); defaults to today when null
	 * @param locationUuid only include this location, or null for all locations
	 * @return one row per stock item per location
	 */
	List<StockDaysRemainingRow> getDaysOfStockRemaining(Date startDate, Date endDate, String locationUuid);

	/**
	 * Items currently below their configured reorder level at a location (Stock Rules feature).
	 *
	 * @param locationUuid only include this location, or null for all locations
	 * @return one row per (item, location) with an enabled reorder rule whose on-hand quantity is
	 *         currently below that rule's threshold, biggest deficit first
	 */
	List<StockReorderRow> getReorderStatus(String locationUuid);

	/**
	 * How often each item/location's stock ran out over a date range.
	 *
	 * @param startDate only include activity on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include activity through the end of this date (inclusive), or null for no upper bound
	 * @param locationUuid only include this location, or null for all locations
	 * @return one row per stock item per location, most stockouts first
	 */
	List<StockoutFrequencyRow> getStockoutFrequency(Date startDate, Date endDate, String locationUuid);
}
