package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.PatientRow;
import org.openmrs.module.labtestreport.SessionAttendanceRow;
import org.openmrs.module.labtestreport.SessionAttendanceService;
import org.openmrs.module.labtestreport.db.SessionAttendanceDAO;

public class SessionAttendanceServiceImpl extends BaseOpenmrsService implements SessionAttendanceService {

	private SessionAttendanceDAO dao;

	public void setDao(SessionAttendanceDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<SessionAttendanceRow> getSummaryReport(Date startDate, Date endDate) {
		List<SessionAttendanceRow> rows = new ArrayList<>();
		for (Object[] r : dao.getSummaryRows(startDate, endDate)) {
			SessionAttendanceRow row = new SessionAttendanceRow();
			row.setSessionDate((Date) r[0]);
			row.setSessionType((String) r[1]);
			row.setSessionSubject((String) r[2]);
			row.setTotalAttendees(toLong(r[3]));
			row.getCounts().put("0-4_M", toLong(r[4]));
			row.getCounts().put("0-4_F", toLong(r[5]));
			row.getCounts().put("5-14_M", toLong(r[6]));
			row.getCounts().put("5-14_F", toLong(r[7]));
			row.getCounts().put("15-18_M", toLong(r[8]));
			row.getCounts().put("15-18_F", toLong(r[9]));
			row.getCounts().put("19-49_M", toLong(r[10]));
			row.getCounts().put("19-49_F", toLong(r[11]));
			row.getCounts().put("50-65_M", toLong(r[12]));
			row.getCounts().put("50-65_F", toLong(r[13]));
			row.getCounts().put("65+_M", toLong(r[14]));
			row.getCounts().put("65+_F", toLong(r[15]));
			row.setTotal(toLong(r[16]));
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<PatientRow> getPatientsForCell(Date sessionDate, String sessionType, String gender, String ageGroup) {
		List<PatientRow> rows = new ArrayList<>();
		for (Object[] r : dao.getPatientsForSessionCell(sessionDate, sessionType, gender, ageGroup)) {
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
