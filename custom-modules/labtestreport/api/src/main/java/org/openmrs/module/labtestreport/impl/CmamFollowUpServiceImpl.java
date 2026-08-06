package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.CmamFollowUpService;
import org.openmrs.module.labtestreport.CmamPatientRow;
import org.openmrs.module.labtestreport.CmamSummaryRow;
import org.openmrs.module.labtestreport.db.CmamFollowUpDAO;

public class CmamFollowUpServiceImpl extends BaseOpenmrsService implements CmamFollowUpService {

	private CmamFollowUpDAO dao;

	public void setDao(CmamFollowUpDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<CmamSummaryRow> getSummaryReport(Date startDate, Date endDate) {
		List<CmamSummaryRow> rows = new ArrayList<>();
		for (Object[] r : dao.getSummaryRows(startDate, endDate)) {
			CmamSummaryRow row = new CmamSummaryRow();
			row.setDimension((String) r[0]);
			row.setCategoryConceptId(toInteger(r[1]));
			row.setCategory((String) r[2]);
			row.setTotal(toLong(r[3]));
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<CmamPatientRow> getPatientsForCategory(String dimensionConceptUuid, Integer categoryConceptId,
	        Date startDate, Date endDate) {
		List<CmamPatientRow> rows = new ArrayList<>();
		for (Object[] r : dao.getPatientsForCategory(dimensionConceptUuid, categoryConceptId, startDate, endDate)) {
			CmamPatientRow row = new CmamPatientRow();
			row.setPatientId(toInteger(r[0]));
			row.setPatientUuid((String) r[1]);
			row.setGivenName((String) r[2]);
			row.setFamilyName((String) r[3]);
			row.setIdentifier((String) r[4]);
			row.setSex((String) r[5]);
			row.setNationalId((String) r[6]);
			row.setPhoneNumber((String) r[7]);
			row.setCurrentDiagnosis((String) r[8]);
			row.setChildLastStatus((String) r[9]);
			row.setAlertStatus((String) r[10]);
			row.setNextVisitDate((java.util.Date) r[11]);
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
