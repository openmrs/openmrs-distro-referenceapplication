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
 * Registers the CMAM Follow-up report with the Reporting module so it also shows up in the O3
 * Reports dashboard (as a plain counts table, without the admin page's clickable drill-down).
 */
public class CmamFollowUpReportManager extends BaseReportManager {

	public static final String UUID = "3f6a7b8c-9d0e-4f1a-8b2c-3d4e5f6a7b8c";

	@Override
	public String getUuid() {
		return UUID;
	}

	@Override
	public String getName() {
		return "CMAM Follow-up Summary Report";
	}

	@Override
	public String getDescription() {
		return "Children by their most recent CMAM Follow-up encounter's Current Diagnosis, Child Last Status "
		        + "and Alert Status. For clickable rows that jump to each patient's chart, use the report "
		        + "under Administration instead.";
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

		reportDefinition.addDataSetDefinition("cmamFollowUpSummary", Mapped.mapStraightThrough(dataSetDefinition));

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

	/**
	 * Wraps the base query (shared with the interactive admin page) in an outer projection that
	 * gives the columns display-friendly headers, since the O3 Reports web preview just renders
	 * whatever columns the dataset returns.
	 */
	private static String buildPreviewSql() {
		return "SELECT dimension AS `Dimension`, category AS `Category`, total AS `Number of Children` "
		        + "FROM (" + SqlResources.load("cmam_summary_report.sql") + ") base";
	}
}
