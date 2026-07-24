package org.openmrs.module.stockmanagement.api.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class StockOperationItemCost {
	
	private Integer id;
	
	private String uuid;
	
	private Integer stockItemId;
	
	private String stockItemUuid;
	
	private String stockItemPackagingUOMUuid;
	
	private Integer packagingUoMId;
	
	private String stockItemPackagingUOMName;
	
	private Integer stockBatchId;
	
	private String stockBatchUuid;
	
	private String batchNo;
	
	private BigDecimal quantity;
	
	private BigDecimal unitCost;
	
	private String unitCostUOMUuid;
	
	private Integer unitCostUOMId;
	
	private String unitCostUOMName;
	
	private BigDecimal totalCost;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public Integer getStockItemId() {
		return stockItemId;
	}
	
	public void setStockItemId(Integer stockItemId) {
		this.stockItemId = stockItemId;
	}
	
	public String getStockItemUuid() {
		return stockItemUuid;
	}
	
	public void setStockItemUuid(String stockItemUuid) {
		this.stockItemUuid = stockItemUuid;
	}
	
	public String getStockItemPackagingUOMUuid() {
		return stockItemPackagingUOMUuid;
	}
	
	public void setStockItemPackagingUOMUuid(String stockItemPackagingUOMUuid) {
		this.stockItemPackagingUOMUuid = stockItemPackagingUOMUuid;
	}
	
	public Integer getPackagingUoMId() {
		return packagingUoMId;
	}
	
	public void setPackagingUoMId(Integer packagingUoMId) {
		this.packagingUoMId = packagingUoMId;
	}
	
	public String getStockItemPackagingUOMName() {
		return stockItemPackagingUOMName;
	}
	
	public void setStockItemPackagingUOMName(String stockItemPackagingUOMName) {
		this.stockItemPackagingUOMName = stockItemPackagingUOMName;
	}
	
	public Integer getStockBatchId() {
		return stockBatchId;
	}
	
	public void setStockBatchId(Integer stockBatchId) {
		this.stockBatchId = stockBatchId;
	}
	
	public String getStockBatchUuid() {
		return stockBatchUuid;
	}
	
	public void setStockBatchUuid(String stockBatchUuid) {
		this.stockBatchUuid = stockBatchUuid;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	public BigDecimal getQuantity() {
		return quantity;
	}
	
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getUnitCost() {
		return unitCost;
	}
	
	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}
	
	public String getUnitCostUOMUuid() {
		return unitCostUOMUuid;
	}
	
	public void setUnitCostUOMUuid(String unitCostUOMUuid) {
		this.unitCostUOMUuid = unitCostUOMUuid;
	}
	
	public Integer getUnitCostUOMId() {
		return unitCostUOMId;
	}
	
	public void setUnitCostUOMId(Integer unitCostUOMId) {
		this.unitCostUOMId = unitCostUOMId;
	}
	
	public String getUnitCostUOMName() {
		return unitCostUOMName;
	}
	
	public void setUnitCostUOMName(String unitCostUOMName) {
		this.unitCostUOMName = unitCostUOMName;
	}
	
	public BigDecimal getTotalCost() {
		return totalCost;
	}
	
	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}
}
