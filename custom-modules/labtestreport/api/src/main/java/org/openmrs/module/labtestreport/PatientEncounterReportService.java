package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface PatientEncounterReportService extends OpenmrsService {

	/**
	 * @param startDate only count encounters on/after this date (inclusive), or null for no lower bound
	 * @param endDate only count encounters through the end of this date (inclusive), or null for no upper bound
	 * @return one row per patient with at least one matching encounter
	 */
	List<PatientEncounterSummaryRow> getPatientEncounterSummary(Date startDate, Date endDate);
}
