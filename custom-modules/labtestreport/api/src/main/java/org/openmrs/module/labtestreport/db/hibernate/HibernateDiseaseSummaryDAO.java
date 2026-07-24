package org.openmrs.module.labtestreport.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.labtestreport.db.DiseaseSummaryDAO;
import org.openmrs.module.labtestreport.db.SqlResources;

public class HibernateDiseaseSummaryDAO implements DiseaseSummaryDAO {

	private static final String SUMMARY_REPORT_SQL = SqlResources.load("disease_summary_report.sql");

	private static final String PATIENTS_FOR_CELL_SQL = SqlResources.load("patients_for_diagnosis_cell.sql");

	private DbSessionFactory sessionFactory;

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getSummaryRows(Date startDate, Date endDate) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(SUMMARY_REPORT_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPatientsForDiagnosisCell(Integer diagnosisConceptId, String gender, String ageGroup,
	        Date startDate, Date endDate) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(PATIENTS_FOR_CELL_SQL);
		query.setParameter("diagnosisConceptId", diagnosisConceptId);
		query.setParameter("gender", gender);
		query.setParameter("ageGroup", ageGroup);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}
}
