package org.openmrs.module.labtestreport.web;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openmrs.module.labtestreport.StockLedgerRow;

/**
 * Turns the sparse rows returned by the service (one row per item/day it actually had activity)
 * into a dense item x day grid, carrying each item's balance forward across days it had no
 * activity, so the pivot table always shows every tracked item on every day in range.
 */
public class StockLedgerGrouping {

	public static List<StockLedgerItem> buildItemList(List<StockLedgerRow> rows) {
		Map<Integer, String> byId = new LinkedHashMap<>();
		for (StockLedgerRow row : rows) {
			byId.putIfAbsent(row.getStockItemId(), row.getItemName());
		}
		List<StockLedgerItem> items = new ArrayList<>();
		for (Map.Entry<Integer, String> entry : byId.entrySet()) {
			items.add(new StockLedgerItem(entry.getKey(), entry.getValue()));
		}
		items.sort(Comparator.comparing(StockLedgerItem::getItemName));
		return items;
	}

	public static List<StockLedgerDayBlock> buildDayBlocks(List<StockLedgerRow> rows, List<StockLedgerItem> items) {
		Map<Integer, Map<Date, StockLedgerRow>> byItemAndDate = new HashMap<>();
		SortedSet<Date> allDates = new TreeSet<>();
		for (StockLedgerRow row : rows) {
			byItemAndDate.computeIfAbsent(row.getStockItemId(), k -> new HashMap<>()).put(row.getLedgerDate(), row);
			allDates.add(row.getLedgerDate());
		}

		Map<Integer, Double> lastRemaining = new HashMap<>();
		for (StockLedgerItem item : items) {
			lastRemaining.put(item.getStockItemId(), 0d);
		}

		List<StockLedgerDayBlock> blocks = new ArrayList<>();
		for (Date date : allDates) {
			StockLedgerDayBlock block = new StockLedgerDayBlock();
			block.setDate(date);
			List<StockLedgerRow> cells = new ArrayList<>();
			for (StockLedgerItem item : items) {
				Map<Date, StockLedgerRow> byDate = byItemAndDate.get(item.getStockItemId());
				StockLedgerRow actualRow = byDate == null ? null : byDate.get(date);
				double opening = lastRemaining.get(item.getStockItemId());

				StockLedgerRow cell = new StockLedgerRow();
				cell.setStockItemId(item.getStockItemId());
				cell.setItemName(item.getItemName());
				cell.setLedgerDate(date);
				cell.setActualQty(opening);
				if (actualRow != null) {
					cell.setIncomingQty(actualRow.getIncomingQty());
					cell.setOutgoingQty(actualRow.getOutgoingQty());
					cell.setRemainingQty(actualRow.getRemainingQty());
					lastRemaining.put(item.getStockItemId(), actualRow.getRemainingQty());
				} else {
					cell.setIncomingQty(0);
					cell.setOutgoingQty(0);
					cell.setRemainingQty(opening);
				}
				cells.add(cell);
			}
			block.setCells(cells);
			blocks.add(block);
		}
		return blocks;
	}
}
