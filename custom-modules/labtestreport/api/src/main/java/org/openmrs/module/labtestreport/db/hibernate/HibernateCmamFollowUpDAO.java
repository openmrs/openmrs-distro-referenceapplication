package org.openmrs.module.labtestreport.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.labtestreport.db.CmamFollowUpDAO;
import org.openmrs.module.labtestreport.db.SqlResources;

public class HibernateCmamFollowUpDAO implements CmamFollowUpDAO {

	private static final String CMAM_SUMMARY_SQL = SqlResources.load("cmam_summary_report.sql");

	private static final String CMAM_PATIENTS_FOR_CATEGORY_SQL = SqlResources.load("cmam_patients_for_category.sql");

	private DbSessionFactory sessionFactory;

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getSummaryRows(Date startDate, Date endDate) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(CMAM_SUMMARY_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPatientsForCategory(String dimensionConceptUuid, Integer categoryConceptId,
	        Date startDate, Date endDate) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(CMAM_PATIENTS_FOR_CATEGORY_SQL);
		query.setParameter("dimensionConceptUuid", dimensionConceptUuid);
		query.setParameter("categoryConceptId", categoryConceptId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.list();
	}
}
