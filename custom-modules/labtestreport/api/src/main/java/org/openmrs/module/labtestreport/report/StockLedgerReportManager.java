package org.openmrs.module.labtestreport.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.module.labtestreport.db.SqlResources;
import org.openmrs.module.reporting.dataset.definition.SqlDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;

/**
 * Registers the stock inventory ledger report with the Reporting module so it also shows up in
 * the O3 Reports dashboard (as a plain data table; the item x day pivot is only rendered on the
 * interactive admin page and O3 report page).
 */
public class StockLedgerReportManager extends BaseReportManager {

	public static final String UUID = "5e9c2a41-7f83-4b6e-9d1a-3c8f6e2b0a94";

	@Override
	public String getUuid() {
		return UUID;
	}

	@Override
	public String getName() {
		return "Stock Inventory Ledger Report";
	}

	@Override
	public String getDescription() {
		return "Daily opening/incoming/outgoing/closing stock balances per item. For the interactive pivot table, "
		        + "use the report under Administration instead.";
	}

	@Override
	public List<Parameter> getParameters() {
		List<Parameter> parameters = new ArrayList<>();
		parameters.add(new Parameter("startDate", "Start Date", Date.class, null, null, null, false));
		parameters.add(new Parameter("endDate", "End Date", Date.class, null, null, null, false));
		return parameters;
	}

	@Override
	public ReportDefinition constructReportDefinition() {
		ReportDefinition reportDefinition = new ReportDefinition();
		reportDefinition.setUuid(getUuid());
		reportDefinition.setName(getName());
		reportDefinition.setDescription(getDescription());
		for (Parameter parameter : getParameters()) {
			reportDefinition.addParameter(parameter);
		}

		SqlDataSetDefinition dataSetDefinition = new SqlDataSetDefinition();
		dataSetDefinition.setName(getName());
		dataSetDefinition.setDescription(getDescription());
		dataSetDefinition.setSqlQuery(buildPreviewSql());
		for (Parameter parameter : getParameters()) {
			dataSetDefinition.addParameter(parameter);
		}

		reportDefinition.addDataSetDefinition("stockLedger", Mapped.mapStraightThrough(dataSetDefinition));

		return reportDefinition;
	}

	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		return new ArrayList<>();
	}

	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}

	private static String buildPreviewSql() {
		return "SELECT itemName AS `Item`, ledgerDate AS `Date`, "
		        + "(remainingQty - incomingQty + outgoingQty) AS `Actual Qty`, "
		        + "incomingQty AS `Incoming`, outgoingQty AS `Outgoing`, remainingQty AS `Remaining` "
		        + "FROM (" + SqlResources.load("stock_ledger_report.sql") + ") base";
	}
}
