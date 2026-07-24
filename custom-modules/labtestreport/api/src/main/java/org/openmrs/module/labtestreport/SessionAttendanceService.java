package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface SessionAttendanceService extends OpenmrsService {

	/**
	 * @param startDate only include visits on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include visits through the end of this date (inclusive), or null for no upper bound
	 * @return the full session attendance report: one row per day/sessionType combination
	 */
	List<SessionAttendanceRow> getSummaryReport(Date startDate, Date endDate);

	/**
	 * Drills down into a single summary report cell to list the patients behind its count.
	 *
	 * @param sessionDate the day to match
	 * @param sessionType "Individual Sessions" or "Group Sessions"
	 * @param gender "M" or "F", or null to match any gender (e.g. drilling down from a total column)
	 * @param ageGroup one of "0-4", "5-14", "15-18", "19-49", "50-65", "65+", or null to match any age
	 */
	List<PatientRow> getPatientsForCell(Date sessionDate, String sessionType, String gender, String ageGroup);
}
