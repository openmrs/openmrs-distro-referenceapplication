package org.openmrs.module.labtestreport.db;

import java.util.Date;
import java.util.List;

import org.openmrs.api.db.DAOException;

/**
 * Database access object backing the session attendance report. Both queries are plain native SQL
 * (see src/main/resources/queries) since the report pivots over visit/person tables directly
 * rather than something naturally expressed through the OpenMRS domain model.
 */
public interface SessionAttendanceDAO {

	/**
	 * @param startDate only include visits on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include visits through the end of this date (inclusive), or null for no upper bound
	 * @return one row per day/sessionType combination, each a 17-element array matching the column
	 *         order of queries/session_attendance_report.sql
	 */
	List<Object[]> getSummaryRows(Date startDate, Date endDate) throws DAOException;

	/**
	 * @param sessionDate the day to match (compared against DATE(visit.date_started))
	 * @param sessionType "Individual Sessions" or "Group Sessions"
	 * @param gender "M" or "F", or null to match any gender
	 * @param ageGroup one of "0-4", "5-14", "15-18", "19-49", "50-65", "65+", or null to match any age
	 * @return one row per distinct patient, each a 5-element array matching the column order of
	 *         queries/patients_for_session_cell.sql
	 */
	List<Object[]> getPatientsForSessionCell(Date sessionDate, String sessionType, String gender, String ageGroup)
	        throws DAOException;
}
