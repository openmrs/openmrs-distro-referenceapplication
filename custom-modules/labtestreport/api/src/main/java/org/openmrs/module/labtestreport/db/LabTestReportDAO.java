package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the lab test summary report. Both queries are plain native SQL
 * (see src/main/resources/queries) since the report is a direct pivot over order/encounter/person
 * tables rather than something naturally expressed through the OpenMRS domain model.
 */
public interface LabTestReportDAO {

	/**
	 * @param startDate only include orders on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include orders through the end of this date (inclusive), or null for no upper bound
	 * @return one row per category/lab-test combination, each an 18-element array matching the
	 *         column order of queries/summary_report.sql
	 */
	List<Object[]> getSummaryRows(Date startDate, Date endDate) throws DAOException;

	/**
	 * @param testConceptId concept id of the lab test order to match
	 * @param gender "M" or "F", or null to match any gender
	 * @param ageGroup one of "0-4", "5-14", "15-18", "19-49", "50-65", "65+", or null to match any age
	 * @param startDate only include orders on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include orders through the end of this date (inclusive), or null for no upper bound
	 * @return one row per distinct patient, each a 5-element array matching the column order of
	 *         queries/patients_for_cell.sql
	 */
	List<Object[]> getPatientsForCell(Integer testConceptId, String gender, String ageGroup, Date startDate,
	        Date endDate) throws DAOException;
}
