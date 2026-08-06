package org.openmrs.module.labtestreport;

import java.util.Date;

/**
 * A child contributing to one cell of the CMAM Follow-up summary's drill-down: their identity
 * plus their most recent CMAM encounter's Current Diagnosis / Child Last Status / Alert Status /
 * Next Visit Date, regardless of which of those three dimensions the cell being drilled into was.
 */
public class CmamPatientRow {

	private Integer patientId;

	private String patientUuid;

	private String givenName;

	private String familyName;

	private String identifier;

	private String sex;

	private String nationalId;

	private String phoneNumber;

	private String currentDiagnosis;

	private String childLastStatus;

	private String alertStatus;

	private Date nextVisitDate;

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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCurrentDiagnosis() {
		return currentDiagnosis;
	}

	public void setCurrentDiagnosis(String currentDiagnosis) {
		this.currentDiagnosis = currentDiagnosis;
	}

	public String getChildLastStatus() {
		return childLastStatus;
	}

	public void setChildLastStatus(String childLastStatus) {
		this.childLastStatus = childLastStatus;
	}

	public String getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(String alertStatus) {
		this.alertStatus = alertStatus;
	}

	public Date getNextVisitDate() {
		return nextVisitDate;
	}

	public void setNextVisitDate(Date nextVisitDate) {
		this.nextVisitDate = nextVisitDate;
	}
}
