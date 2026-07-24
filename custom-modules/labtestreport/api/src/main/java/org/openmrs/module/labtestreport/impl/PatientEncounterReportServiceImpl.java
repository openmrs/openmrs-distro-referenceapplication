package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.PatientEncounterReportService;
import org.openmrs.module.labtestreport.PatientEncounterSummaryRow;
import org.openmrs.module.labtestreport.db.PatientEncounterReportDAO;

public class PatientEncounterReportServiceImpl extends BaseOpenmrsService implements PatientEncounterReportService {

	private PatientEncounterReportDAO dao;

	public void setDao(PatientEncounterReportDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<PatientEncounterSummaryRow> getPatientEncounterSummary(Date startDate, Date endDate) {
		List<PatientEncounterSummaryRow> rows = new ArrayList<>();
		for (Object[] r : dao.getPatientEncounterSummary(startDate, endDate)) {
			PatientEncounterSummaryRow row = new PatientEncounterSummaryRow();
			row.setPatientId(toInteger(r[0]));
			row.setPatientUuid((String) r[1]);
			row.setGivenName((String) r[2]);
			row.setFamilyName((String) r[3]);
			row.setAge(toInteger(r[4]));
			row.setEncounterCount(toLong(r[5]));
			row.setMostRecentEncounterDate((Date) r[6]);
			rows.add(row);
		}
		return rows;
	}

	private static Integer toInteger(Object value) {
		return value == null ? null : ((Number) value).intValue();
	}

	private static long toLong(Object value) {
		return value == null ? 0L : ((Number) value).longValue();
	}
}
