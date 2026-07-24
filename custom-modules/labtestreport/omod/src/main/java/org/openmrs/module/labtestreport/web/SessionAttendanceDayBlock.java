package org.openmrs.module.labtestreport.web;

import java.util.Date;
import java.util.List;

import org.openmrs.module.labtestreport.SessionAttendanceRow;

/**
 * One day's worth of the session attendance report: its fixed-order session-type rows (Individual
 * Sessions, Group Sessions, Case follow up - synthesized as zero rows if the day has no matching
 * data) plus the summed "Total of the day" row.
 */
public class SessionAttendanceDayBlock {

	private Date date;

	private List<SessionAttendanceRow> rows;

	private SessionAttendanceRow dailyTotal;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<SessionAttendanceRow> getRows() {
		return rows;
	}

	public void setRows(List<SessionAttendanceRow> rows) {
		this.rows = rows;
	}

	public SessionAttendanceRow getDailyTotal() {
		return dailyTotal;
	}

	public void setDailyTotal(SessionAttendanceRow dailyTotal) {
		this.dailyTotal = dailyTotal;
	}
}
