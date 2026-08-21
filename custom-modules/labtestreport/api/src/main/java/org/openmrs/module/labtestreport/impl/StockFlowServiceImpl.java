package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.StockFlowService;
import org.openmrs.module.labtestreport.StockLocationQtyRow;
import org.openmrs.module.labtestreport.db.StockFlowDAO;

public class StockFlowServiceImpl extends BaseOpenmrsService implements StockFlowService {

	private StockFlowDAO dao;

	public void setDao(StockFlowDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<StockLocationQtyRow> getConsumptionByLocation(Date startDate, Date endDate, String locationUuid) {
		return toRows(dao.getConsumptionRows(startDate, endDate, locationUuid));
	}

	@Override
	public List<StockLocationQtyRow> getDistributionFromSource(Date startDate, Date endDate, String sourceLocationUuid) {
		return toRows(dao.getDistributionRows(startDate, endDate, sourceLocationUuid));
	}

	@Override
	public List<StockLocationQtyRow> getWastageByLocation(Date startDate, Date endDate, String locationUuid) {
		return toRows(dao.getWastageRows(startDate, endDate, locationUuid));
	}

	private static List<StockLocationQtyRow> toRows(List<Object[]> results) {
		List<StockLocationQtyRow> rows = new ArrayList<>();
		for (Object[] r : results) {
			StockLocationQtyRow row = new StockLocationQtyRow();
			row.setStockItemId(toInteger(r[0]));
			row.setItemName((String) r[1]);
			row.setLocationId(toInteger(r[2]));
			row.setLocationName((String) r[3]);
			row.setQuantity(toDouble(r[4]));
			row.setUnitName((String) r[5]);
			row.setSourceLocationName((String) r[6]);
			rows.add(row);
		}
		return rows;
	}

	private static Integer toInteger(Object value) {
		return value == null ? null : ((Number) value).intValue();
	}

	private static double toDouble(Object value) {
		return value == null ? 0d : ((Number) value).doubleValue();
	}
}
