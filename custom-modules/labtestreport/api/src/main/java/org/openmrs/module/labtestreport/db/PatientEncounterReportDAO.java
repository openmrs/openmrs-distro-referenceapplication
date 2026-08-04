package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the patient visit summary report.
 */
public interface PatientEncounterReportDAO {

	/**
	 * @param startDate only count visits started on/after this date (inclusive), or null for no lower bound
	 * @param endDate only count visits started through the end of this date (inclusive), or null for no upper bound
	 * @return one row per patient with at least one matching visit, each a 7-element array
	 *         matching the column order of queries/patient_encounter_summary.sql
	 */
	List<Object[]> getPatientEncounterSummary(Date startDate, Date endDate) throws DAOException;

	/**
	 * @param startDate only include visits started on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include visits started through the end of this date (inclusive), or null for no upper bound
	 * @return one row per (non-voided) visit, each an 8-element array matching the column order
	 *         of queries/patient_encounter_details.sql, ordered by patient then visit date
	 */
	List<Object[]> getPatientEncounterDetails(Date startDate, Date endDate) throws DAOException;
}
