package org.openmrs.module.stockmanagement.api.dto.reporting;

import org.openmrs.module.stockmanagement.api.dto.StockItemInventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StockItemInventoryForecast extends StockItemInventory {
	
	private List<BigDecimal> quantityConsumed;
	
	private BigDecimal consumptionRate;
	
	public StockItemInventoryForecast() {
        quantityConsumed=new ArrayList<>();
    }
	
	public List<BigDecimal> getQuantityConsumed() {
		return quantityConsumed;
	}
	
	public void setQuantityConsumed(List<BigDecimal> quantityConsumed) {
		this.quantityConsumed = quantityConsumed;
	}
	
	public BigDecimal getConsumptionRate() {
		return consumptionRate;
	}
	
	public void setConsumptionRate(BigDecimal consumptionRate) {
		this.consumptionRate = consumptionRate;
	}
}
