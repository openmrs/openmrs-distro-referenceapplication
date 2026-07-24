package org.openmrs.module.labtestreport;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * One row of the lab test summary report: a single lab test within a category, with its
 * age-group/gender breakdown. {@link #categoryRowSpan} is a presentation-only value filled in by
 * the controller so the JSP can merge consecutive rows that share a category into one cell.
 */
public class SummaryRow {

	private Integer categoryConceptId;

	private String category;

	private int categoryRowSpan = 0;

	private Integer testConceptId;

	private String testLabel;

	private long totalTests;

	private final Map<String, Long> counts = new LinkedHashMap<>();

	private long total;

	public Integer getCategoryConceptId() {
		return categoryConceptId;
	}

	public void setCategoryConceptId(Integer categoryConceptId) {
		this.categoryConceptId = categoryConceptId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCategoryRowSpan() {
		return categoryRowSpan;
	}

	public void setCategoryRowSpan(int categoryRowSpan) {
		this.categoryRowSpan = categoryRowSpan;
	}

	public Integer getTestConceptId() {
		return testConceptId;
	}

	public void setTestConceptId(Integer testConceptId) {
		this.testConceptId = testConceptId;
	}

	public String getTestLabel() {
		return testLabel;
	}

	public void setTestLabel(String testLabel) {
		this.testLabel = testLabel;
	}

	public long getTotalTests() {
		return totalTests;
	}

	public void setTotalTests(long totalTests) {
		this.totalTests = totalTests;
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
