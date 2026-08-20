package org.openmrs.module.labtestreport.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.labtestreport.db.SqlResources;
import org.openmrs.module.labtestreport.db.StockStatusDAO;

public class HibernateStockStatusDAO implements StockStatusDAO {

	private static final String EXPIRY_RISK_SQL = SqlResources.load("stock_expiry_risk.sql");

	private static final String CURRENT_ONHAND_SQL = SqlResources.load("stock_current_onhand.sql");

	private static final String REORDER_STATUS_SQL = SqlResources.load("stock_reorder_status.sql");

	private static final String STOCKOUT_FREQUENCY_SQL = SqlResources.load("stock_stockout_frequency.sql");

	private DbSessionFactory sessionFactory;

	public void setSessionFactory(DbSessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getExpiryRiskRows(Integer daysAhead, String locationUuid) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(EXPIRY_RISK_SQL);
		query.setParameter("daysAhead", daysAhead);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getCurrentOnHandRows(String locationUuid) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(CURRENT_ONHAND_SQL);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getReorderStatusRows(String locationUuid) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(REORDER_STATUS_SQL);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getStockoutFrequencyRows(Date startDate, Date endDate, String locationUuid) throws DAOException {
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(STOCKOUT_FREQUENCY_SQL);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("locationUuid", locationUuid);
		return query.list();
	}
}
