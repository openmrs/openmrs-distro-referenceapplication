package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.LabTestReportService;
import org.openmrs.module.labtestreport.PatientRow;
import org.openmrs.module.labtestreport.SummaryRow;
import org.openmrs.module.labtestreport.db.LabTestReportDAO;

public class LabTestReportServiceImpl extends BaseOpenmrsService implements LabTestReportService {

	private LabTestReportDAO dao;

	public void setDao(LabTestReportDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<SummaryRow> getSummaryReport(Date startDate, Date endDate) {
		List<SummaryRow> rows = new ArrayList<>();
		for (Object[] r : dao.getSummaryRows(startDate, endDate)) {
			SummaryRow row = new SummaryRow();
			row.setCategoryConceptId(toInteger(r[0]));
			row.setCategory((String) r[1]);
			row.setTestConceptId(toInteger(r[2]));
			row.setTestLabel((String) r[3]);
			row.setTotalTests(toLong(r[4]));
			row.getCounts().put("0-4_M", toLong(r[5]));
			row.getCounts().put("0-4_F", toLong(r[6]));
			row.getCounts().put("5-14_M", toLong(r[7]));
			row.getCounts().put("5-14_F", toLong(r[8]));
			row.getCounts().put("15-18_M", toLong(r[9]));
			row.getCounts().put("15-18_F", toLong(r[10]));
			row.getCounts().put("19-49_M", toLong(r[11]));
			row.getCounts().put("19-49_F", toLong(r[12]));
			row.getCounts().put("50-65_M", toLong(r[13]));
			row.getCounts().put("50-65_F", toLong(r[14]));
			row.getCounts().put("65+_M", toLong(r[15]));
			row.getCounts().put("65+_F", toLong(r[16]));
			row.setTotal(toLong(r[17]));
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<PatientRow> getPatientsForCell(Integer testConceptId, String gender, String ageGroup, Date startDate,
	        Date endDate) {
		List<PatientRow> rows = new ArrayList<>();
		for (Object[] r : dao.getPatientsForCell(testConceptId, gender, ageGroup, startDate, endDate)) {
			PatientRow row = new PatientRow();
			row.setPatientId(toInteger(r[0]));
			row.setPatientUuid((String) r[1]);
			row.setGivenName((String) r[2]);
			row.setFamilyName((String) r[3]);
			row.setIdentifier((String) r[4]);
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
