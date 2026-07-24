package org.openmrs.module.stockmanagement.api.model;

import org.openmrs.BaseChangeableOpenmrsData;
import org.openmrs.BaseOpenmrsData;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the stockmgmt_stock_batch database table.
 */
@Entity(name = "stockmanagement.StockBatch")
@Table(name = "stockmgmt_stock_batch")
public class StockBatch extends BaseChangeableOpenmrsData implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "stock_batch_id")
	private Integer id;
	
	@Column(name = "batch_no", length = 50)
	private String batchNo;
	
	@Column(name = "expiration")
	private Date expiration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stock_item_id")
	private StockItem stockItem;
	
	@Column(name = "expiry_notification_date")
	private Date expiryNotificationDate;
	
	public StockBatch() {
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getBatchNo() {
		return this.batchNo;
	}
	
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public StockItem getStockItem() {
		return this.stockItem;
	}
	
	public void setStockItem(StockItem StockItem) {
		this.stockItem = StockItem;
	}
	
	public Date getExpiryNotificationDate() {
		return expiryNotificationDate;
	}
	
	public void setExpiryNotificationDate(Date expiryNotificationDate) {
		this.expiryNotificationDate = expiryNotificationDate;
	}
}
