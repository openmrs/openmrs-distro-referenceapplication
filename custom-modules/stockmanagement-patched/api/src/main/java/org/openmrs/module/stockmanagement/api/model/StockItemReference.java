package org.openmrs.module.stockmanagement.api.model;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.openmrs.Concept;
import org.openmrs.Drug;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_stock_item database table.
 */
@Entity(name = "stockmanagement.StockItemReference")
@Table(name = "stockmgmt_stock_item_reference")
@Indexed
public class StockItemReference extends org.openmrs.BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_item_reference_id")
	@DocumentId
	private Integer id;
	
	@JoinColumn(name = "stock_source_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private StockSource referenceSource;
	
	@JoinColumn(name = "stock_item_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private StockItem stockItem;
	
	@GenericField
	@Column(name = "stock_reference_code", length = 255)
	private String stockReferenceCode;
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public StockSource getReferenceSource() {
		return referenceSource;
	}
	
	public void setReferenceSource(StockSource referenceSource) {
		this.referenceSource = referenceSource;
	}
	
	public StockItem getStockItem() {
		return stockItem;
	}
	
	public void setStockItem(StockItem stockItem) {
		this.stockItem = stockItem;
	}
	
	public String getStockReferenceCode() {
		return stockReferenceCode;
	}
	
	public void setStockReferenceCode(String stockReferenceCode) {
		this.stockReferenceCode = stockReferenceCode;
	}
	
}
