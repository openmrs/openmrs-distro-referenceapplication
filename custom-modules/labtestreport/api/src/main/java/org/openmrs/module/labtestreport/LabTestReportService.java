package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface LabTestReportService extends OpenmrsService {

	/**
	 * @param startDate only include orders on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include orders through the end of this date (inclusive), or null for no upper bound
	 * @return the full lab test summary report: one row per category/lab-test combination
	 */
	List<SummaryRow> getSummaryReport(Date startDate, Date endDate);

	/**
	 * Drills down into a single summary report cell to list the patients behind its count.
	 *
	 * @param testConceptId concept id of the lab test order to match
	 * @param gender "M" or "F", or null to match any gender (e.g. drilling down from a total column)
	 * @param ageGroup one of "0-4", "5-14", "15-18", "19-49", "50-65", "65+", or null to match any age
	 * @param startDate only include orders on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include orders through the end of this date (inclusive), or null for no upper bound
	 */
	List<PatientRow> getPatientsForCell(Integer testConceptId, String gender, String ageGroup, Date startDate,
	        Date endDate);
}
