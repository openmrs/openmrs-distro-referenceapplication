package org.openmrs.module.labtestreport.extension.html;

import java.util.LinkedHashMap;
import java.util.Map;

import org.openmrs.module.Extension;
import org.openmrs.module.web.extension.AdministrationSectionExt;

/**
 * Adds a "Lab Test Summary Report" link to the Administration page.
 */
public class LabTestReportAdminExt extends AdministrationSectionExt {

	@Override
	public Extension.MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}

	@Override
	public String getTitle() {
		return "labtestreport.title";
	}

	@Override
	public String getRequiredPrivilege() {
		return "";
	}

	@Override
	public Map<String, String> getLinks() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("module/labtestreport/summary.form", "labtestreport.title");
		map.put("module/labtestreport/patientEncounters.form", "labtestreport.patientEncounters.title");
		map.put("module/labtestreport/diseaseSummary.form", "labtestreport.disease.title");
		map.put("module/labtestreport/sessionAttendance.form", "labtestreport.sessionAttendance.title");
		map.put("module/labtestreport/stockLedger.form", "labtestreport.stockLedger.title");
		return map;
	}
}
