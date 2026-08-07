package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.PatientEncounterDetailRow;
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
			row.setVisitCount(toLong(r[5]));
			row.setMostRecentVisitDate((Date) r[6]);
			row.setSex((String) r[7]);
			row.setNationalId((String) r[8]);
			row.setPhoneNumber((String) r[9]);
			row.setLocation((String) r[10]);
			row.setServiceType((String) r[11]);
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<PatientEncounterDetailRow> getPatientEncounterDetails(Date startDate, Date endDate) {
		List<PatientEncounterDetailRow> rows = new ArrayList<>();
		for (Object[] r : dao.getPatientEncounterDetails(startDate, endDate)) {
			PatientEncounterDetailRow row = new PatientEncounterDetailRow();
			row.setPatientId(toInteger(r[0]));
			row.setPatientUuid((String) r[1]);
			row.setGivenName((String) r[2]);
			row.setFamilyName((String) r[3]);
			row.setVisitId(toInteger(r[4]));
			row.setVisitDate((Date) r[5]);
			row.setLocationName((String) r[6]);
			row.setProviderName((String) r[7]);
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
