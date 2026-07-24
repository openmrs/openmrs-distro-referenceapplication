package org.openmrs.module.labtestreport.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.module.labtestreport.SessionAttendanceRow;

/**
 * Groups the flat rows returned by the service into per-day blocks, synthesizing a zero-filled row
 * for any session type that had no data that day (including "Case follow up", which this system
 * has no underlying data source for at all - it's a structural placeholder row, matching the
 * report template it replicates), and computing each day's "Total of the day" row.
 */
public class SessionAttendanceGrouping {

	private static final List<String> SESSION_TYPES_IN_ORDER = Arrays.asList("Individual Sessions", "Group Sessions",
	    "Case follow up");

	private static final List<String> AGE_GENDER_KEYS = Arrays.asList("0-4_M", "0-4_F", "5-14_M", "5-14_F", "15-18_M",
	    "15-18_F", "19-49_M", "19-49_F", "50-65_M", "50-65_F", "65+_M", "65+_F");

	public static List<SessionAttendanceDayBlock> buildDayBlocks(List<SessionAttendanceRow> rows) {
		Map<java.util.Date, Map<String, SessionAttendanceRow>> byDate = new LinkedHashMap<>();
		for (SessionAttendanceRow row : rows) {
			byDate.computeIfAbsent(row.getSessionDate(), d -> new LinkedHashMap<>()).put(row.getSessionType(), row);
		}

		List<SessionAttendanceDayBlock> blocks = new ArrayList<>();
		for (Map.Entry<java.util.Date, Map<String, SessionAttendanceRow>> entry : byDate.entrySet()) {
			SessionAttendanceDayBlock block = new SessionAttendanceDayBlock();
			block.setDate(entry.getKey());

			List<SessionAttendanceRow> blockRows = new ArrayList<>();
			SessionAttendanceRow total = zeroRow(entry.getKey(), null);
			for (String type : SESSION_TYPES_IN_ORDER) {
				SessionAttendanceRow row = entry.getValue().get(type);
				if (row == null) {
					row = zeroRow(entry.getKey(), type);
				}
				blockRows.add(row);
				addInto(total, row);
			}
			block.setRows(blockRows);
			block.setDailyTotal(total);
			blocks.add(block);
		}
		return blocks;
	}

	private static SessionAttendanceRow zeroRow(java.util.Date date, String type) {
		SessionAttendanceRow row = new SessionAttendanceRow();
		row.setSessionDate(date);
		row.setSessionType(type);
		for (String key : AGE_GENDER_KEYS) {
			row.getCounts().put(key, 0L);
		}
		return row;
	}

	private static void addInto(SessionAttendanceRow total, SessionAttendanceRow row) {
		total.setTotalAttendees(total.getTotalAttendees() + row.getTotalAttendees());
		total.setTotal(total.getTotal() + row.getTotal());
		for (String key : AGE_GENDER_KEYS) {
			total.getCounts().put(key, total.getCounts().getOrDefault(key, 0L) + row.getCounts().getOrDefault(key, 0L));
		}
	}
}
