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
 * Registers the session attendance report with the Reporting module so it also shows up in the
 * O3 Reports dashboard (as a plain data table, without the admin page's clickable rows).
 */
public class SessionAttendanceReportManager extends BaseReportManager {

	public static final String UUID = "c3f7a1e2-9d4b-4a6f-8e1c-2b5d7f9a0c31";

	@Override
	public String getUuid() {
		return UUID;
	}

	@Override
	public String getName() {
		return "Session Attendance Report";
	}

	@Override
	public String getDescription() {
		return "Individual and Group session attendance broken down by day, age group and gender. For clickable "
		        + "drill-down to the patients behind each count, use the report under Administration instead.";
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

		reportDefinition.addDataSetDefinition("sessionAttendance", Mapped.mapStraightThrough(dataSetDefinition));

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
		return "SELECT sessionDate AS `Date`, sessionType AS `Session Type`, sessionSubject AS `Session Subject`, "
		        + "totalAttendees AS `Total Attendees`, "
		        + "age_0_4_male AS `0-4 M`, age_0_4_female AS `0-4 F`, "
		        + "age_5_14_male AS `5-14 M`, age_5_14_female AS `5-14 F`, "
		        + "age_15_18_male AS `15-18 M`, age_15_18_female AS `15-18 F`, "
		        + "age_19_49_male AS `19-49 M`, age_19_49_female AS `19-49 F`, "
		        + "age_50_65_male AS `50-65 M`, age_50_65_female AS `50-65 F`, "
		        + "age_65_plus_male AS `65+ M`, age_65_plus_female AS `65+ F`, "
		        + "total AS `Total` "
		        + "FROM (" + SqlResources.load("session_attendance_report.sql") + ") base";
	}
}
