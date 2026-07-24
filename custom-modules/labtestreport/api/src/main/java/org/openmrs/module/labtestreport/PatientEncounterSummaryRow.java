package org.openmrs.module.labtestreport;

import java.util.Date;

/**
 * One row of the patient encounter summary report: a patient with at least one (non-voided)
 * encounter in the selected date range, their current age, how many encounters they have, and
 * the most recent one's date.
 */
public class PatientEncounterSummaryRow {

	private Integer patientId;

	private String patientUuid;

	private String givenName;

	private String familyName;

	private Integer age;

	private long encounterCount;

	private Date mostRecentEncounterDate;

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientUuid() {
		return patientUuid;
	}

	public void setPatientUuid(String patientUuid) {
		this.patientUuid = patientUuid;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public long getEncounterCount() {
		return encounterCount;
	}

	public void setEncounterCount(long encounterCount) {
		this.encounterCount = encounterCount;
	}

	public Date getMostRecentEncounterDate() {
		return mostRecentEncounterDate;
	}

	public void setMostRecentEncounterDate(Date mostRecentEncounterDate) {
		this.mostRecentEncounterDate = mostRecentEncounterDate;
	}
}
