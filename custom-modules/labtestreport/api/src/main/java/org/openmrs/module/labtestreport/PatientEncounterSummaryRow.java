package org.openmrs.module.labtestreport;

import java.util.Date;

/**
 * One row of the patient visit summary report: a patient with at least one (non-voided) visit in
 * the selected date range, their current age, how many visits they have, and the most recent
 * one's date.
 */
public class PatientEncounterSummaryRow {

	private Integer patientId;

	private String patientUuid;

	private String givenName;

	private String familyName;

	private Integer age;

	private long visitCount;

	private Date mostRecentVisitDate;

	private String sex;

	private String nationalId;

	private String phoneNumber;

	private String location;

	private String serviceType;

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

	public long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(long visitCount) {
		this.visitCount = visitCount;
	}

	public Date getMostRecentVisitDate() {
		return mostRecentVisitDate;
	}

	public void setMostRecentVisitDate(Date mostRecentVisitDate) {
		this.mostRecentVisitDate = mostRecentVisitDate;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}
