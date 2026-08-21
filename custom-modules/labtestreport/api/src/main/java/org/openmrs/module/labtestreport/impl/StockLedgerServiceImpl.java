package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.StockLedgerRow;
import org.openmrs.module.labtestreport.StockLedgerService;
import org.openmrs.module.labtestreport.db.StockLedgerDAO;

public class StockLedgerServiceImpl extends BaseOpenmrsService implements StockLedgerService {

	private StockLedgerDAO dao;

	public void setDao(StockLedgerDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<StockLedgerRow> getLedgerReport(Date startDate, Date endDate, String locationUuid) {
		List<StockLedgerRow> rows = new ArrayList<>();
		for (Object[] r : dao.getLedgerRows(startDate, endDate, locationUuid)) {
			StockLedgerRow row = new StockLedgerRow();
			row.setStockItemId(toInteger(r[0]));
			row.setItemName((String) r[1]);
			row.setLocationId(toInteger(r[2]));
			row.setLocationName((String) r[3]);
			row.setLedgerDate((Date) r[4]);
			row.setIncomingQty(toDouble(r[5]));
			row.setOutgoingQty(toDouble(r[6]));
			row.setRemainingQty(toDouble(r[7]));
			row.setActualQty(row.getRemainingQty() - row.getIncomingQty() + row.getOutgoingQty());
			row.setUnitName((String) r[8]);
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
