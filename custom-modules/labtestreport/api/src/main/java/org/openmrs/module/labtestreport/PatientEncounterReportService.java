package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface PatientEncounterReportService extends OpenmrsService {

	/**
	 * @param startDate only count visits started on/after this date (inclusive), or null for no lower bound
	 * @param endDate only count visits started through the end of this date (inclusive), or null for no upper bound
	 * @return one row per patient with at least one matching visit
	 */
	List<PatientEncounterSummaryRow> getPatientEncounterSummary(Date startDate, Date endDate);

	/**
	 * @param startDate only include visits started on/after this date (inclusive), or null for no lower bound
	 * @param endDate only include visits started through the end of this date (inclusive), or null for no upper bound
	 * @return one row per (non-voided) visit, ordered by patient then visit date
	 */
	List<PatientEncounterDetailRow> getPatientEncounterDetails(Date startDate, Date endDate);
}
