package org.openmrs.module.labtestreport.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labtestreport.StockBatchExpiryRow;
import org.openmrs.module.labtestreport.StockDaysRemainingRow;
import org.openmrs.module.labtestreport.StockFlowService;
import org.openmrs.module.labtestreport.StockLocationQtyRow;
import org.openmrs.module.labtestreport.StockReorderRow;
import org.openmrs.module.labtestreport.StockStatusService;
import org.openmrs.module.labtestreport.StockoutFrequencyRow;
import org.openmrs.module.labtestreport.db.StockStatusDAO;

public class StockStatusServiceImpl extends BaseOpenmrsService implements StockStatusService {

	private static final int DEFAULT_WINDOW_DAYS = 30;

	private StockStatusDAO dao;

	private StockFlowService stockFlowService;

	public void setDao(StockStatusDAO dao) {
		this.dao = dao;
	}

	public void setStockFlowService(StockFlowService stockFlowService) {
		this.stockFlowService = stockFlowService;
	}

	@Override
	public List<StockBatchExpiryRow> getExpiryRisk(Integer daysAhead, String locationUuid) {
		List<StockBatchExpiryRow> rows = new ArrayList<>();
		for (Object[] r : dao.getExpiryRiskRows(daysAhead, locationUuid)) {
			StockBatchExpiryRow row = new StockBatchExpiryRow();
			row.setStockItemId(toInteger(r[0]));
			row.setItemName((String) r[1]);
			row.setLocationId(toInteger(r[2]));
			row.setLocationName((String) r[3]);
			row.setBatchNo((String) r[4]);
			row.setExpirationDate((Date) r[5]);
			row.setRemainingQty(toDouble(r[6]));
			row.setDaysUntilExpiry(toInteger(r[7]));
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<StockDaysRemainingRow> getDaysOfStockRemaining(Date startDate, Date endDate, String locationUuid) {
		Date effectiveEndDate = endDate != null ? endDate : new Date();
		Date effectiveStartDate = startDate != null ? startDate : addDays(effectiveEndDate, -(DEFAULT_WINDOW_DAYS - 1));
		double windowDays = Math.max(1, daysBetweenInclusive(effectiveStartDate, effectiveEndDate));

		Map<String, Double> consumedByKey = new HashMap<>();
		for (StockLocationQtyRow r : stockFlowService.getConsumptionByLocation(effectiveStartDate, effectiveEndDate,
		    locationUuid)) {
			consumedByKey.put(rowKey(r.getStockItemId(), r.getLocationId()), r.getQuantity());
		}

		List<StockDaysRemainingRow> rows = new ArrayList<>();
		for (Object[] r : dao.getCurrentOnHandRows(locationUuid)) {
			StockDaysRemainingRow row = new StockDaysRemainingRow();
			Integer stockItemId = toInteger(r[0]);
			Integer locationId = toInteger(r[2]);
			row.setStockItemId(stockItemId);
			row.setItemName((String) r[1]);
			row.setLocationId(locationId);
			row.setLocationName((String) r[3]);
			row.setOnHandQty(toDouble(r[4]));

			double totalConsumed = consumedByKey.getOrDefault(rowKey(stockItemId, locationId), 0d);
			double avgDailyConsumption = totalConsumed / windowDays;
			row.setAvgDailyConsumption(avgDailyConsumption);
			row.setDaysRemaining(avgDailyConsumption > 0 ? row.getOnHandQty() / avgDailyConsumption : null);
			rows.add(row);
		}
		rows.sort((a, b) -> {
			if (a.getDaysRemaining() == null)
				return b.getDaysRemaining() == null ? 0 : 1;
			if (b.getDaysRemaining() == null)
				return -1;
			return Double.compare(a.getDaysRemaining(), b.getDaysRemaining());
		});
		return rows;
	}

	@Override
	public List<StockReorderRow> getReorderStatus(String locationUuid) {
		List<StockReorderRow> rows = new ArrayList<>();
		for (Object[] r : dao.getReorderStatusRows(locationUuid)) {
			StockReorderRow row = new StockReorderRow();
			row.setStockItemId(toInteger(r[0]));
			row.setItemName((String) r[1]);
			row.setLocationId(toInteger(r[2]));
			row.setLocationName((String) r[3]);
			row.setRuleName((String) r[4]);
			row.setReorderLevel(toDouble(r[5]));
			row.setOnHandQty(toDouble(r[6]));
			rows.add(row);
		}
		return rows;
	}

	@Override
	public List<StockoutFrequencyRow> getStockoutFrequency(Date startDate, Date endDate, String locationUuid) {
		List<StockoutFrequencyRow> rows = new ArrayList<>();
		for (Object[] r : dao.getStockoutFrequencyRows(startDate, endDate, locationUuid)) {
			StockoutFrequencyRow row = new StockoutFrequencyRow();
			row.setStockItemId(toInteger(r[0]));
			row.setItemName((String) r[1]);
			row.setLocationId(toInteger(r[2]));
			row.setLocationName((String) r[3]);
			row.setStockoutDays(toInteger(r[4]));
			row.setActiveDays(toInteger(r[5]));
			rows.add(row);
		}
		return rows;
	}

	private static String rowKey(Integer stockItemId, Integer locationId) {
		return stockItemId + ":" + locationId;
	}

	private static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}

	private static long daysBetweenInclusive(Date start, Date end) {
		long millisPerDay = 24L * 60 * 60 * 1000;
		return ((end.getTime() - start.getTime()) / millisPerDay) + 1;
	}

	private static Integer toInteger(Object value) {
		return value == null ? null : ((Number) value).intValue();
	}

	private static double toDouble(Object value) {
		return value == null ? 0d : ((Number) value).doubleValue();
	}
}
