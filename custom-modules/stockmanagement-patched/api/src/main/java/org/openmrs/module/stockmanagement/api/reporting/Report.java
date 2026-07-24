package org.openmrs.module.stockmanagement.api.reporting;

import org.openmrs.module.stockmanagement.api.reporting.impl.*;

import java.util.ArrayList;
import java.util.List;

public class Report<T extends ReportGenerator> {
	
	private String uuid;
	
	private int order;
	
	private String name;
	
	private String systemName;
	
	private ReportParameter[] parameters;
	
	private Class<T> reportGeneratorClass;
	
	public Report() {
	}
	
	public Report(String uuid, int order, String name, String systemName, ReportParameter[] parameters,
	    Class<T> reportGeneratorClass) {
		this.uuid = uuid;
		this.order = order;
		this.name = name;
		this.systemName = systemName;
		this.parameters = parameters;
		this.reportGeneratorClass = reportGeneratorClass;
	}
	
	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSystemName() {
		return systemName;
	}
	
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	
	public ReportParameter[] getParameters() {
		return parameters;
	}
	
	public void setParameters(ReportParameter[] parameters) {
		this.parameters = parameters;
	}
	
	public Class<T> getReportGeneratorClass() {
		return reportGeneratorClass;
	}
	
	public void setReportGeneratorClass(Class<T> reportGeneratorClass) {
		this.reportGeneratorClass = reportGeneratorClass;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public static List<Report> getAllReports(){
        List<Report> reports = new ArrayList<>();

        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-806d973f13a9", 1,"Stock Status Report","STOCK_STATUS_REPORT", new ReportParameter[]{ReportParameter.Date, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.InventoryGroupBy},StockStatusReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-816d973f13a9", 1, "Stock Consumption Report", "STOCK_CONSUMPTION_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.InventoryGroupBy}, StockConsumptionReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-826d973f13a9", 1, "Stock Receipt Report", "STOCK_RECEIPT_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.StockSource}, StockReceiptReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-836d973f13a9", 1, "Stock Issue Report", "STOCK_ISSUE_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations}, StockIssueReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-846d973f13a9", 1, "Dispensing Logs", "DISPENSING_LOGS", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.Patient, ReportParameter.StockItem}, DispensingLogsReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-856d973f13a9", 1, "Prescribed Drugs Report", "PRESCRIBED_DRUGS_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.Patient, ReportParameter.StockItem}, PrescribedDrugsReport.class));
		reports.add(new Report<>("8df8c605-6b37-11ed-93a2-866d973f13a9", 1, "Fulfillment Prescriptions Report", "FULFILLMENT_PRESCRIPTIONS_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.Patient, ReportParameter.StockItem, ReportParameter.Fullfillment}, FulfillmentPrescriptionsReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-876d973f13a9", 1, "Stock Forecast Report", "STOCK_FORECAST_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.InventoryGroupBy, ReportParameter.Limit, ReportParameter.StockItem}, StockForecastReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-886d973f13a9", 1, "Stock Expiry Forecast Report", "STOCK_EXPIRY_FORECAST_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.Limit, ReportParameter.StockItem}, StockExpiryForecastReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-906d973f13a9", 1, "Stock-Out Report", "STOCK_OUT_REPORT", new ReportParameter[]{ReportParameter.Date, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.InventoryGroupBy, ReportParameter.MaxReorderLevelRatio}, StockOutReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-916d973f13a9", 1, "Stock Expiry Report", "STOCK_EXPIRY_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations}, StockExpiryReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-926d973f13a9", 1, "Stock Transfer Out Report", "STOCK_TRANSFER_OUT_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.StockSourceDestination}, StockTransferOutReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-936d973f13a9", 1, "Stock Transfer In Report", "STOCK_TRANSFER_IN_REPORT", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations}, StockTransferInReport.class));
        reports.add(new Report<>("8df8c605-6b37-11ed-93a2-946d973f13a9", 1,"Most Moving Items/Least Moving Items","MOST_LEAST_MOVING_ITEMS", new ReportParameter[]{ReportParameter.StartDate, ReportParameter.EndDate, ReportParameter.StockItemCategory, ReportParameter.Location, ReportParameter.ChildLocations, ReportParameter.InventoryGroupBy, ReportParameter.Limit, ReportParameter.MostLeastMoving},MostLeastMovingItemsReport.class));

        return reports;
    }
}
