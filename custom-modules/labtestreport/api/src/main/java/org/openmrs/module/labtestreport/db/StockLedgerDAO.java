package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the stock inventory ledger report. Plain native SQL (see
 * src/main/resources/queries) since the report pivots over the stock management module's own
 * tables directly rather than something naturally expressed through this module's domain model.
 */
public interface StockLedgerDAO {

	/**
	 * @param startDate only include activity on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include activity through the end of this date (inclusive), or null for no upper bound
	 * @return one row per stock item/day it had activity, each a 6-element array matching the
	 *         column order of queries/stock_ledger_report.sql
	 */
	List<Object[]> getLedgerRows(Date startDate, Date endDate) throws DAOException;
}
