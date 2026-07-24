package org.openmrs.module.labtestreport.web;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;

import org.openmrs.module.labtestreport.DiseaseSummaryRow;
import org.openmrs.module.labtestreport.SummaryRow;

/**
 * Shared by the JSP-rendered summary reports and their JSON API equivalents: rows are already
 * ordered by category then item, so consecutive rows sharing a category form a contiguous group;
 * the first row of each group gets a rowspan covering the rest.
 */
public class SummaryRowGrouping {

	private SummaryRowGrouping() {
	}

	public static void applyCategoryRowSpans(List<SummaryRow> rows) {
		applyCategoryRowSpans(rows, SummaryRow::getCategoryConceptId, SummaryRow::setCategoryRowSpan);
	}

	public static void applyDiseaseCategoryRowSpans(List<DiseaseSummaryRow> rows) {
		applyCategoryRowSpans(rows, DiseaseSummaryRow::getCategoryConceptId, DiseaseSummaryRow::setCategoryRowSpan);
	}

	private static <T> void applyCategoryRowSpans(List<T> rows, Function<T, Integer> categoryConceptId,
	        ObjIntConsumer<T> rowSpanSetter) {
		int i = 0;
		while (i < rows.size()) {
			Integer currentCategory = categoryConceptId.apply(rows.get(i));
			int span = 1;
			while (i + span < rows.size() && Objects.equals(categoryConceptId.apply(rows.get(i + span)), currentCategory)) {
				span++;
			}
			rowSpanSetter.accept(rows.get(i), span);
			i += span;
		}
	}
}
