package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Encounter;
import org.openmrs.Location;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.module.stockmanagement.api.model.Party;
import org.openmrs.module.stockmanagement.api.model.StockBatch;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;

import java.math.BigDecimal;

public class DispenseRequestProcessingInfo {
	
	private Location location;
	
	private Party party;
	
	private Patient patient;
	
	private Order order;
	
	private Encounter encounter;
	
	private StockItem stockItem;
	
	private StockBatch stockBatch;
	
	private BigDecimal quantity;
	
	private StockItemPackagingUOM PackagingUOM;
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Party getParty() {
		return party;
	}
	
	public void setParty(Party party) {
		this.party = party;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public Order getOrder() {
		return order;
	}
	
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public Encounter getEncounter() {
		return encounter;
	}
	
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	
	public StockItem getStockItem() {
		return stockItem;
	}
	
	public void setStockItem(StockItem stockItem) {
		this.stockItem = stockItem;
	}
	
	public StockBatch getStockBatch() {
		return stockBatch;
	}
	
	public void setStockBatch(StockBatch stockBatch) {
		this.stockBatch = stockBatch;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public StockItemPackagingUOM getPackagingUOM() {
		return PackagingUOM;
	}
	
	public void setPackagingUOM(StockItemPackagingUOM packagingUOM) {
		PackagingUOM = packagingUOM;
	}
}
