package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.*;
import org.openmrs.module.stockmanagement.api.StockOperationTypeProcessor;
import org.openmrs.module.stockmanagement.api.impl.*;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 * The persistent class for the stockmgmt_stock_source database table.
 */
@Entity(name = "stockmanagement.StockSource")
@Table(name = "stockmgmt_stock_source")
public class StockSource extends BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_source_id")
	private Integer id;
	
	@Column(name = "name", length = 255, nullable = false)
	private String name;
	
	@Column(name = "acronym", length = 255, nullable = false)
	private String acronym;
	
	@JoinColumn(name = "source_type_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Concept sourceType;
	
	public StockSource() {
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAcronym() {
		return acronym;
	}
	
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	public Concept getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(Concept sourceType) {
		this.sourceType = sourceType;
	}
}
