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
 * Registers the lab test summary report with the Reporting module so it also shows up in the O3
 * Reports dashboard (as a plain data table). {@link org.openmrs.module.labtestreport.LabTestReportActivator}
 * installs this via {@link org.openmrs.module.reporting.report.manager.ReportManagerUtil#setupReport}
 * on module startup. This is a read-only view of the same query the interactive admin page uses;
 * it has no clickable drill-down since the O3 Reports web preview is a generic table renderer.
 */
public class LabTestSummaryReportManager extends BaseReportManager {

	public static final String UUID = "d3b8f1a2-9c4e-4a1b-8f6d-2e7c5a0b9d31";

	@Override
	public String getUuid() {
		return UUID;
	}

	@Override
	public String getName() {
		return "Lab Test Summary Report";
	}

	@Override
	public String getDescription() {
		return "Lab test orders broken down by category, test, age group and gender. "
		        + "For clickable drill-down to the patients behind each count, use the report under Administration instead.";
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

		reportDefinition.addDataSetDefinition("labTestSummary", Mapped.mapStraightThrough(dataSetDefinition));

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
	 * drops the internal concept-id columns and gives the remaining ones display-friendly headers,
	 * since the O3 Reports web preview just renders whatever columns the dataset returns.
	 */
	private static String buildPreviewSql() {
		return "SELECT category AS `Category`, testLabel AS `Lab Test`, totalTests AS `Total Tests`, "
		        + "age_0_4_male AS `0-4 M`, age_0_4_female AS `0-4 F`, "
		        + "age_5_14_male AS `5-14 M`, age_5_14_female AS `5-14 F`, "
		        + "age_15_18_male AS `15-18 M`, age_15_18_female AS `15-18 F`, "
		        + "age_19_49_male AS `19-49 M`, age_19_49_female AS `19-49 F`, "
		        + "age_50_65_male AS `50-65 M`, age_50_65_female AS `50-65 F`, "
		        + "age_65_plus_male AS `65+ M`, age_65_plus_female AS `65+ F`, "
		        + "total AS `Total` "
		        + "FROM (" + SqlResources.load("summary_report.sql") + ") base";
	}
}
