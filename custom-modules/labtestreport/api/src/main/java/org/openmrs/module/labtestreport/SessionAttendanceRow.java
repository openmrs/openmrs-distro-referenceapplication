package org.openmrs.module.labtestreport;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One row of the session attendance report: attendance for a single session type (Individual
 * Sessions / Group Sessions) on a single day, with its age-group/gender breakdown. Rows are
 * grouped into per-day blocks (with a synthesized "Case follow up" placeholder row and a daily
 * total row) by the web layer, not here - this DTO only carries what the database actually knows.
 */
public class SessionAttendanceRow {

	private Date sessionDate;

	private String sessionType;

	private String sessionSubject;

	private long totalAttendees;

	private final Map<String, Long> counts = new LinkedHashMap<>();

	private long total;

	public Date getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(Date sessionDate) {
		this.sessionDate = sessionDate;
	}

	public String getSessionType() {
		return sessionType;
	}

	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}

	public String getSessionSubject() {
		return sessionSubject;
	}

	public void setSessionSubject(String sessionSubject) {
		this.sessionSubject = sessionSubject;
	}

	public long getTotalAttendees() {
		return totalAttendees;
	}

	public void setTotalAttendees(long totalAttendees) {
		this.totalAttendees = totalAttendees;
	}

	public Map<String, Long> getCounts() {
		return counts;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}
