package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the patient encounter summary report.
 */
public interface PatientEncounterReportDAO {

	/**
	 * @param startDate only count encounters on/after this date (inclusive), or null for no lower bound
	 * @param endDate only count encounters through the end of this date (inclusive), or null for no upper bound
	 * @return one row per patient with at least one matching encounter, each a 7-element array
	 *         matching the column order of queries/patient_encounter_summary.sql
	 */
	List<Object[]> getPatientEncounterSummary(Date startDate, Date endDate) throws DAOException;
}
