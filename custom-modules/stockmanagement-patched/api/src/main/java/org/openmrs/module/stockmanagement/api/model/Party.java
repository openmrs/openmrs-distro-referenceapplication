package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.BaseChangeableOpenmrsData;
import org.openmrs.BaseOpenmrsData;
import org.openmrs.Concept;
import org.openmrs.Location;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the stockmgmt_party database table.
 */
@Entity(name = "stockmanagement.Party")
@Table(name = "stockmgmt_party")
public class Party extends BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "party_id")
	private Integer id;
	
	@JoinColumn(name = "location_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;
	
	@JoinColumn(name = "stock_source_id", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private StockSource stockSource;
	
	public Party() {
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public StockSource getStockSource() {
		return stockSource;
	}
	
	public void setStockSource(StockSource stockSource) {
		this.stockSource = stockSource;
	}
	
	@Override
	public String toString() {
		if (getLocation() != null) {
			return getLocation().getName();
		}
		if (getStockSource() != null) {
			return getStockSource().getName();
		}
		return "";
	}
}
