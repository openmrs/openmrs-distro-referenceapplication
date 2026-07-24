package org.openmrs.module.labtestreport.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.labtestreport.db.SessionAttendanceDAO;
import org.openmrs.module.labtestreport.db.SqlResources;

public class HibernateSessionAttendanceDAO implements SessionAttendanceDAO {

	private static final String SUMMARY_REPORT_SQL = SqlResources.load("session_attendance_report.sql");

	private static final String PATIENTS_FOR_CELL_SQL = SqlResources.load("patients_for_session_cell.sql");

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
	public List<Object[]> getPatientsForSessionCell(Date sessionDate, String sessionType, String gender,
	        String ageGroup) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(PATIENTS_FOR_CELL_SQL);
		query.setParameter("sessionDate", sessionDate);
		query.setParameter("sessionType", sessionType);
		query.setParameter("gender", gender);
		query.setParameter("ageGroup", ageGroup);
		return query.list();
	}
}
