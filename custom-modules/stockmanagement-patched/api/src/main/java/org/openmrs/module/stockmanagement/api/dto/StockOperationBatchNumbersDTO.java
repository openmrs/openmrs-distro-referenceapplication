package org.openmrs.module.stockmanagement.api.dto;

import java.util.Date;
import java.util.List;

public class StockOperationBatchNumbersDTO {
	
	private Integer id;
	
	private String uuid;
	
	private List<StockOperationItemBatchNumber> batchNumbers;
	
	private StockOperationDTO stockOperationDTO;
	
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
	
	public List<StockOperationItemBatchNumber> getBatchNumbers() {
		return batchNumbers;
	}
	
	public void setBatchNumbers(List<StockOperationItemBatchNumber> batchNumbers) {
		this.batchNumbers = batchNumbers;
	}
	
	public StockOperationDTO getStockOperationDTO() {
		return stockOperationDTO;
	}
	
	public void setStockOperationDTO(StockOperationDTO stockOperationDTO) {
		this.stockOperationDTO = stockOperationDTO;
	}
	
	public static class StockOperationItemBatchNumber {
		
		private Integer id;
		
		private String uuid;
		
		private String batchNo;
		
		private Date expiration;
		
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
		
		public String getBatchNo() {
			return batchNo;
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
	}
}
