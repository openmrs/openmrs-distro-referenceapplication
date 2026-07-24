package org.openmrs.module.labtestreport;

import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.labtestreport.report.DiseaseSummaryReportManager;
import org.openmrs.module.labtestreport.report.LabTestSummaryReportManager;
import org.openmrs.module.labtestreport.report.PatientEncounterSummaryReportManager;
import org.openmrs.module.labtestreport.report.SessionAttendanceReportManager;
import org.openmrs.module.labtestreport.report.StockLedgerReportManager;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;

/**
 * Registers this module's reports with the Reporting module on startup, in addition to the
 * module descriptor schema requiring some activator class to be present.
 */
public class LabTestReportActivator extends BaseModuleActivator {

	@Override
	public void started() {
		ReportManagerUtil.setupReport(new LabTestSummaryReportManager());
		ReportManagerUtil.setupReport(new PatientEncounterSummaryReportManager());
		ReportManagerUtil.setupReport(new DiseaseSummaryReportManager());
		ReportManagerUtil.setupReport(new SessionAttendanceReportManager());
		ReportManagerUtil.setupReport(new StockLedgerReportManager());
	}
}
