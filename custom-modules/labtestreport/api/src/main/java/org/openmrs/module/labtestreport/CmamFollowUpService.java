package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.List;

import org.openmrs.api.OpenmrsService;

public interface CmamFollowUpService extends OpenmrsService {

	/**
	 * @param startDate only consider CMAM encounters on/after this date (inclusive), or null for no lower bound
	 * @param endDate only consider CMAM encounters through the end of this date (inclusive), or null for no upper bound
	 * @return counts of children, by their most recent CMAM encounter's Current Diagnosis, Child
	 *         Last Status and Alert Status
	 */
	List<CmamSummaryRow> getSummaryReport(Date startDate, Date endDate);

	/**
	 * @param dimensionConceptUuid the question concept's UUID (Current Diagnosis / Child Last Status / Alert Status)
	 * @param categoryConceptId the specific answer concept within that dimension
	 * @param startDate only consider CMAM encounters on/after this date (inclusive), or null for no lower bound
	 * @param endDate only consider CMAM encounters through the end of this date (inclusive), or null for no upper bound
	 * @return the children whose most recent CMAM encounter has that answer for that dimension
	 */
	List<CmamPatientRow> getPatientsForCategory(String dimensionConceptUuid, Integer categoryConceptId, Date startDate,
	        Date endDate);
}
