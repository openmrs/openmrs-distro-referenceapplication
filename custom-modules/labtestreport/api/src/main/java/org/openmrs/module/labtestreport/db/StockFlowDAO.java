package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the stock consumption-by-location and distribution
 * reports. Plain native SQL (see src/main/resources/queries), same rationale as
 * {@link StockLedgerDAO} - these pivot over the stock management module's own tables
 * directly.
 */
public interface StockFlowDAO {

	/**
	 * @return one row per stock item (and per consuming location, when locationUuid is null),
	 *         each a 7-element array matching the column order of
	 *         queries/stock_consumption_by_location.sql
	 */
	List<Object[]> getConsumptionRows(Date startDate, Date endDate, String locationUuid) throws DAOException;

	/**
	 * @return one row per stock item per destination location, each a 7-element array matching
	 *         the column order of queries/stock_distribution_from_source.sql
	 */
	List<Object[]> getDistributionRows(Date startDate, Date endDate, String sourceLocationUuid) throws DAOException;

	/**
	 * @return one row per stock item (and per location, when locationUuid is null), each a
	 *         7-element array matching the column order of queries/stock_wastage_by_location.sql
	 */
	List<Object[]> getWastageRows(Date startDate, Date endDate, String locationUuid) throws DAOException;
}
