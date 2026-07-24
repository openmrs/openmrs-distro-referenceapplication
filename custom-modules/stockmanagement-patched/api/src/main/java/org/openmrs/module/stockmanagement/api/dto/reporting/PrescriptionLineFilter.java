package org.openmrs.module.stockmanagement.api.dto.reporting;

import java.util.List;

public class PrescriptionLineFilter extends ReportFilter {
	
	private Integer prescriptionTransactionMin;
	
	private Integer patientId;
	
	private Integer stockItemId;
	
	private Integer observationDispensingLocationConceptId;
	
	private Integer observationDrugConceptId;
	
	private Integer drugId;
	
	private List<Fullfillment> fullfillments;
	
	public Integer getPrescriptionTransactionMin() {
		return prescriptionTransactionMin;
	}
	
	public void setPrescriptionTransactionMin(Integer prescriptionTransactionMin) {
		this.prescriptionTransactionMin = prescriptionTransactionMin;
	}
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public Integer getDrugId() {
		return drugId;
	}
	
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	
	public Integer getObservationDispensingLocationConceptId() {
		return observationDispensingLocationConceptId;
	}
	
	public void setObservationDispensingLocationConceptId(Integer observationDispensingLocationConceptId) {
		this.observationDispensingLocationConceptId = observationDispensingLocationConceptId;
	}
	
	public Integer getObservationDrugConceptId() {
		return observationDrugConceptId;
	}
	
	public void setObservationDrugConceptId(Integer observationDrugConceptId) {
		this.observationDrugConceptId = observationDrugConceptId;
	}
	
	public List<Fullfillment> getFullfillments() {
		return fullfillments;
	}
	
	public void setFullfillments(List<Fullfillment> fullfillments) {
		this.fullfillments = fullfillments;
	}
}
