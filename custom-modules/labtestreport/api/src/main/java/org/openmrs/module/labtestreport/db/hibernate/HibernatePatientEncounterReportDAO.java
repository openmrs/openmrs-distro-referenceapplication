package org.openmrs.module.labtestreport.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.labtestreport.db.PatientEncounterReportDAO;
import org.openmrs.module.labtestreport.db.SqlResources;

public class HibernatePatientEncounterReportDAO implements PatientEncounterReportDAO {

	private static final String PATIENT_ENCOUNTER_SUMMARY_SQL = SqlResources.load("patient_encounter_summary.sql");

	private static final String PATIENT_ENCOUNTER_DETAILS_SQL = SqlResources.load("patient_encounter_details.sql");

	private DbSessionFactory sessionFactory;

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPatientEncounterSummary(Date startDate, Date endDate) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(PATIENT_ENCOUNTER_SUMMARY_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPatientEncounterDetails(Date startDate, Date endDate) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(PATIENT_ENCOUNTER_DETAILS_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}
}
