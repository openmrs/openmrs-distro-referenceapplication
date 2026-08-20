package org.openmrs.module.labtestreport.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.labtestreport.db.SqlResources;
import org.openmrs.module.labtestreport.db.StockFlowDAO;

public class HibernateStockFlowDAO implements StockFlowDAO {

	private static final String CONSUMPTION_SQL = SqlResources.load("stock_consumption_by_location.sql");

	private static final String DISTRIBUTION_SQL = SqlResources.load("stock_distribution_from_source.sql");

	private static final String WASTAGE_SQL = SqlResources.load("stock_wastage_by_location.sql");

	private DbSessionFactory sessionFactory;

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getConsumptionRows(Date startDate, Date endDate, String locationUuid) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(CONSUMPTION_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getDistributionRows(Date startDate, Date endDate, String sourceLocationUuid)
	        throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(DISTRIBUTION_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("sourceLocationUuid", sourceLocationUuid);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getWastageRows(Date startDate, Date endDate, String locationUuid) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(WASTAGE_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}
}
