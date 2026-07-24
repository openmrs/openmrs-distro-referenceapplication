package org.openmrs.module.stockmanagement.api.dto;

import org.openmrs.Concept;
import org.openmrs.module.stockmanagement.api.model.StockItem;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

public class StockItemPackagingUOMDTO {
	
	private Integer id;
	
	private String uuid;
	
	private boolean voided;
	
	private BigDecimal factor;
	
	private int packagingUomId;
	
	private String packagingUomUuid;
	
	private String packagingUomName;
	
	private int stockItemId;
	
	private String stockItemUuid;
	
	private Integer stockItemDispensingUnitId;
	
	private String stockItemDispensingUnitName;
	
	private boolean isDispensingUnit;
	
	private boolean isDefaultStockOperationsUoM;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public boolean getIsDispensingUnit() {
		return isDispensingUnit;
	}
	
	public void setIsDispensingUnit(boolean isDispensingUnit) {
		this.isDispensingUnit = isDispensingUnit;
	}
	
	public boolean getIsDefaultStockOperationsUoM() {
		return isDefaultStockOperationsUoM;
	}
	
	public void setIsDefaultStockOperationsUoM(boolean isDefaultStockOperationsUoM) {
		this.isDefaultStockOperationsUoM = isDefaultStockOperationsUoM;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public boolean isVoided() {
		return voided;
	}
	
	public void setVoided(boolean voided) {
		this.voided = voided;
	}
	
	public BigDecimal getFactor() {
		return factor;
	}
	
	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	
	public int getPackagingUomId() {
		return packagingUomId;
	}
	
	public void setPackagingUomId(int packagingUomId) {
		this.packagingUomId = packagingUomId;
	}
	
	public String getPackagingUomUuid() {
		return packagingUomUuid;
	}
	
	public void setPackagingUomUuid(String packagingUomUuid) {
		this.packagingUomUuid = packagingUomUuid;
	}
	
	public String getPackagingUomName() {
		return packagingUomName;
	}
	
	public void setPackagingUomName(String packagingUomName) {
		this.packagingUomName = packagingUomName;
	}
	
	public int getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(int stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public Integer getStockItemDispensingUnitId() {
		return stockItemDispensingUnitId;
	}
	
	public void setStockItemDispensingUnitId(Integer stockItemDispensingUnitId) {
		this.stockItemDispensingUnitId = stockItemDispensingUnitId;
	}
	
	public String getStockItemDispensingUnitName() {
		return stockItemDispensingUnitName;
	}
	
	public void setStockItemDispensingUnitName(String stockItemDispensingUnitName) {
		this.stockItemDispensingUnitName = stockItemDispensingUnitName;
	}
}
