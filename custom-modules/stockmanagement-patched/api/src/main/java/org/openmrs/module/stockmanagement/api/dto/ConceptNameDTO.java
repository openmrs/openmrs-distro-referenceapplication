package org.openmrs.module.stockmanagement.api.dto;

import java.util.Locale;

public class ConceptNameDTO {
	
	private Integer conceptId;
	
	private String name;

	private Locale locale;

	public Integer getConceptId() {
		return conceptId;
	}
	
	public void setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
