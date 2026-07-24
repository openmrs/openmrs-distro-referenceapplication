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
 * Registers the patient encounter summary report with the Reporting module so it also shows up
 * in the O3 Reports dashboard (as a plain data table, without the admin page's clickable rows).
 */
public class PatientEncounterSummaryReportManager extends BaseReportManager {

	public static final String UUID = "6f1a2b3c-4d5e-4f60-8a71-9b2c3d4e5f60";

	@Override
	public String getUuid() {
		return UUID;
	}

	@Override
	public String getName() {
		return "Patient Encounter Summary Report";
	}

	@Override
	public String getDescription() {
		return "Patients with at least one encounter, showing current age, number of encounters and most recent "
		        + "encounter date. For clickable rows that jump to each patient's chart, use the report under "
		        + "Administration instead.";
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

		reportDefinition.addDataSetDefinition("patientEncounterSummary", Mapped.mapStraightThrough(dataSetDefinition));

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
	 * drops the internal patient id/uuid columns and gives the remaining ones display-friendly
	 * headers, since the O3 Reports web preview just renders whatever columns the dataset returns.
	 */
	private static String buildPreviewSql() {
		return "SELECT givenName AS `Given Name`, familyName AS `Family Name`, age AS `Age`, "
		        + "encounterCount AS `Number of Encounters`, mostRecentEncounterDate AS `Most Recent Encounter Date` "
		        + "FROM (" + SqlResources.load("patient_encounter_summary.sql") + ") base";
	}
}
