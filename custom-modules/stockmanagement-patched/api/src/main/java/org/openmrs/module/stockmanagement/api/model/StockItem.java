package org.openmrs.module.stockmanagement.api.model;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.openmrs.Concept;
import org.openmrs.Drug;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_stock_item database table.
 */
@Entity(name = "stockmanagement.StockItem")
@Table(name = "stockmgmt_stock_item")
@Indexed
public class StockItem extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_item_id")
	@DocumentId
	private Integer id;
	
	@JoinColumn(name = "concept_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Concept concept;
	
	@JoinColumn(name = "drug_id")
	@OneToOne(fetch = FetchType.LAZY)
	private Drug drug;
	
	@Column(name = "has_expiration")
	private boolean hasExpiration;
	
	@OneToMany(mappedBy = "stockItem")
	private Set<StockBatch> stockBatches;
	
	@JoinColumn(name = "preferred_vendor_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private StockSource preferredVendor;
	
	@Column(name = "purchase_price", nullable = true)
	private BigDecimal purchasePrice;
	
	@JoinColumn(name = "purchase_price_uom_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private StockItemPackagingUOM purchasePriceUoM;
	
	@JoinColumn(name = "dispensing_unit_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Concept dispensingUnit;
	
	@JoinColumn(name = "dispensing_unit_packaging_uom_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private StockItemPackagingUOM dispensingUnitPackagingUoM;
	
	@JoinColumn(name = "default_stock_operations_uom_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private StockItemPackagingUOM defaultStockOperationsUoM;
	
	@OneToMany(mappedBy = "stockItem")
	private Set<StockItemPackagingUOM> stockItemPackagingUOMs;
	
	@OneToMany(mappedBy = "stockItem", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<StockItemReference> references;
	
	@GenericField
	@Column(name = "is_drug", nullable = false)
	private Boolean isDrug;
	
	@FullTextField
	@Column(name = "common_name", length = 255, nullable = true)
	private String commonName;
	
	@FullTextField
	@Column(name = "acronym", length = 255, nullable = true)
	private String acronym;
	
	@Column(name = "reorder_level", nullable = true)
	private BigDecimal reorderLevel;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reorder_level_uom_id")
	private StockItemPackagingUOM reorderLevelUOM;
	
	@JoinColumn(name = "category_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Concept category;
	
	@Column(name = "expiry_notice", nullable = true)
	private Integer expiryNotice;
	
	public StockItem() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Drug getDrug() {
		return this.drug;
	}
	
	public void setDrug(Drug drug) {
		this.drug = drug;
		if (!((drug != null && isDrug != null && isDrug) || (drug == null && isDrug != null && !isDrug))) {
			isDrug = drug != null;
		}
	}
	
	public boolean getHasExpiration() {
		return this.hasExpiration;
	}
	
	public void setHasExpiration(boolean hasExpiration) {
		this.hasExpiration = hasExpiration;
	}
	
	public Concept getConcept() {
		return concept;
	}
	
	public void setConcept(Concept concept) {
		this.concept = concept;
	}
	
	public StockItemPackagingUOM getDispensingUnitPackagingUoM() {
		return dispensingUnitPackagingUoM;
	}
	
	public void setDispensingUnitPackagingUoM(StockItemPackagingUOM dispensingUnitPackagingUoM) {
		this.dispensingUnitPackagingUoM = dispensingUnitPackagingUoM;
	}
	
	public Set<StockBatch> getStockBatches() {
		return this.stockBatches;
	}
	
	public void setStockBatches(Set<StockBatch> stockBatches) {
		this.stockBatches = stockBatches;
	}
	
	public StockBatch addStockBatch(StockBatch stockBatch) {
		getStockBatches().add(stockBatch);
		stockBatch.setStockItem(this);
		
		return stockBatch;
	}
	
	public StockBatch removeStockBatch(StockBatch stockBatch) {
		getStockBatches().remove(stockBatch);
		stockBatch.setStockItem(null);
		
		return stockBatch;
	}
	
	public Set<StockItemPackagingUOM> getStockItemPackagingUOMs() {
		return this.stockItemPackagingUOMs;
	}
	
	public void setStockItemPackagingUOMs(Set<StockItemPackagingUOM> stockItemPackagingUOMs) {
		this.stockItemPackagingUOMs = stockItemPackagingUOMs;
	}
	
	public StockItemPackagingUOM addStockItemPackagingUom(StockItemPackagingUOM stockItemPackagingUom) {
		getStockItemPackagingUOMs().add(stockItemPackagingUom);
		stockItemPackagingUom.setStockItem(this);
		
		return stockItemPackagingUom;
	}
	
	public StockItemPackagingUOM removeStockItemPackagingUom(StockItemPackagingUOM stockItemPackagingUom) {
		getStockItemPackagingUOMs().remove(stockItemPackagingUom);
		stockItemPackagingUom.setStockItem(null);
		
		return stockItemPackagingUom;
	}
	
	public Set<StockItemReference> getReferences() {
		return references;
	}
	
	public void setStockItemReferences(Set<StockItemReference> references) {
		this.references = references;
	}
	
	public StockItemReference addStockItemReference(StockItemReference reference) {
		getReferences().add(reference);
		reference.setStockItem(this);
		
		return reference;
	}
	
	public StockItemReference removeStockItemReferences(StockItemReference reference) {
		getReferences().remove(reference);
		reference.setStockItem(null);
		return reference;
	}
	
	public boolean isHasExpiration() {
		return hasExpiration;
	}
	
	public StockSource getPreferredVendor() {
		return preferredVendor;
	}
	
	public void setPreferredVendor(StockSource preferredVendor) {
		this.preferredVendor = preferredVendor;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public StockItemPackagingUOM getPurchasePriceUoM() {
		return purchasePriceUoM;
	}
	
	public void setPurchasePriceUoM(StockItemPackagingUOM purchasePriceUoM) {
		this.purchasePriceUoM = purchasePriceUoM;
	}
	
	public Concept getDispensingUnit() {
		return dispensingUnit;
	}
	
	public void setDispensingUnit(Concept dispensingUnit) {
		this.dispensingUnit = dispensingUnit;
	}
	
	public StockItemPackagingUOM getDefaultStockOperationsUoM() {
		return defaultStockOperationsUoM;
	}
	
	public void setDefaultStockOperationsUoM(StockItemPackagingUOM defaultStockOperationsUoM) {
		this.defaultStockOperationsUoM = defaultStockOperationsUoM;
	}
	
	public String getCommonName() {
		return commonName;
	}
	
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	public BigDecimal getReorderLevel() {
		return reorderLevel;
	}
	
	public void setReorderLevel(BigDecimal reorderLevel) {
		this.reorderLevel = reorderLevel;
	}
	
	public StockItemPackagingUOM getReorderLevelUOM() {
		return reorderLevelUOM;
	}
	
	public void setReorderLevelUOM(StockItemPackagingUOM reorderLevelUOM) {
		this.reorderLevelUOM = reorderLevelUOM;
	}
	
	public Boolean getIsDrug() {
		return isDrug;
	}
	
	public void setIsDrug(Boolean isDrug) {
		this.isDrug = isDrug;
	}
	
	public Concept getCategory() {
		return category;
	}
	
	public void setCategory(Concept category) {
		this.category = category;
	}
	
	public Integer getExpiryNotice() {
		return expiryNotice;
	}
	
	public void setExpiryNotice(Integer expiryNotice) {
		this.expiryNotice = expiryNotice;
	}
}
