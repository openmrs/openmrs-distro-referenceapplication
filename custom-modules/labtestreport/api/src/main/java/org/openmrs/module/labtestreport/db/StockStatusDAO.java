package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the expiry-risk, days-of-stock-remaining, reorder-status and
 * stockout-frequency reports. Plain native SQL (see src/main/resources/queries).
 */
public interface StockStatusDAO {

	/**
	 * @return one row per batch per location, each an 8-element array matching the column order of
	 *         queries/stock_expiry_risk.sql
	 */
	List<Object[]> getExpiryRiskRows(Integer daysAhead, String locationUuid) throws DAOException;

	/**
	 * @return one row per stock item per location, each a 5-element array matching the column
	 *         order of queries/stock_current_onhand.sql
	 */
	List<Object[]> getCurrentOnHandRows(String locationUuid) throws DAOException;

	/**
	 * @return one row per (item, location) with an enabled reorder rule, each a 7-element array
	 *         matching the column order of queries/stock_reorder_status.sql
	 */
	List<Object[]> getReorderStatusRows(String locationUuid) throws DAOException;

	/**
	 * @return one row per stock item per location, each a 6-element array matching the column
	 *         order of queries/stock_stockout_frequency.sql
	 */
	List<Object[]> getStockoutFrequencyRows(Date startDate, Date endDate, String locationUuid) throws DAOException;
}
