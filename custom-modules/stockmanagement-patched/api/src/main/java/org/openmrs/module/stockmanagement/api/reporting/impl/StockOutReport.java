package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.openmrs.module.stockmanagement.api.dto.StockInventoryResult;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;

import java.math.BigDecimal;

public class StockOutReport extends StockStatusReport {
	
	private BigDecimal oneHundered = BigDecimal.valueOf(100);
	
	@Override
	protected void preWriteBuffer(StockItemInventorySearchFilter inventorySearchFilter, StockInventoryResult stockInventoryResult){
        if (!stockInventoryResult.getData().isEmpty()) {
            stockManagementService.setStockItemInformation(stockInventoryResult.getData());
            if(maxReorderLevelRatio == null){
                maxReorderLevelRatio = BigDecimal.ZERO;
            }
            stockInventoryResult.getData().removeIf(p -> {
                return p.getQuantity()
                        .compareTo(maxReorderLevelRatio.divide(oneHundered, 5, BigDecimal.ROUND_HALF_EVEN).multiply(p.getReorderLevel() == null ? BigDecimal.ZERO : p.getReorderLevel().multiply(p.getReorderLevelFactor()))) > 0;
            });
            if(!stockInventoryResult.getData().isEmpty()) {
                stockManagementService.postProcessInventoryResult(inventorySearchFilter, stockInventoryResult);
            }
        }
    }
}
