package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the CMAM Follow-up report: each child's most recent CMAM
 * encounter (by form, within the selected date range) grouped by Current Diagnosis, Child Last
 * Status and Alert Status.
 */
public interface CmamFollowUpDAO {

	/**
	 * @param startDate only consider CMAM encounters on/after this date (inclusive), or null for no lower bound
	 * @param endDate only consider CMAM encounters through the end of this date (inclusive), or null for no upper bound
	 * @return one row per (dimension, category), each a 4-element array matching the column order
	 *         of queries/cmam_summary_report.sql
	 */
	List<Object[]> getSummaryRows(Date startDate, Date endDate) throws DAOException;

	/**
	 * @param dimensionConceptUuid the question concept's UUID (Current Diagnosis / Child Last Status / Alert Status)
	 * @param categoryConceptId the specific answer concept within that dimension
	 * @param startDate only consider CMAM encounters on/after this date (inclusive), or null for no lower bound
	 * @param endDate only consider CMAM encounters through the end of this date (inclusive), or null for no upper bound
	 * @return one row per child whose most recent CMAM encounter has that answer for that
	 *         dimension, each a 12-element array matching the column order of
	 *         queries/cmam_patients_for_category.sql
	 */
	List<Object[]> getPatientsForCategory(String dimensionConceptUuid, Integer categoryConceptId, Date startDate,
	        Date endDate) throws DAOException;
}
