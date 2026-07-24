package org.openmrs.module.stockmanagement.api.validator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockItemDTO;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Handler(supports = { StockItemDTO.class }, order = 50)
public class StockItemDTOValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return StockItemDTO.class.isAssignableFrom(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		if (target == null) {
			errors.reject(messageSourceService.getMessage("error.general"));
			return;
		}
		
		if (Context.getAuthenticatedUser() == null) {
			errors.reject(messageSourceService.getMessage("stockmanagement.stockitem.authrequired"));
			return;
		}
		
		StockManagementService service = Context.getService(StockManagementService.class);
		StockItemDTO object = (StockItemDTO) target;
		StockItem stockItem = null;
		if (object.getUuid() != null) {
			stockItem = service.getStockItemByUuid(object.getUuid());
			if (stockItem == null) {
				errors.rejectValue("uuid", messageSourceService.getMessage("stockmanagement.stockitem.notexists"));
				return;
			}
		}
		
		if (stockItem == null) {
			if (StringUtils.isBlank(object.getDrugUuid()) && StringUtils.isBlank(object.getConceptUuid())) {
				errors.rejectValue("drugUuid",
				    messageSourceService.getMessage("stockmanagement.stockitem.drugorconceptrequired"));
				return;
			}
			
			if (!StringUtils.isBlank(object.getDrugUuid()) && !StringUtils.isBlank(object.getConceptUuid())) {
				errors.rejectValue("drugUuid", messageSourceService.getMessage("stockmanagement.stockitem.drugorconcept"));
				return;
			}
		}
		
		if (object.getHasExpiration() == null) {
			errors.rejectValue("hasExpiration",
			    messageSourceService.getMessage("stockmanagement.stockitem.hasExpirationrequired"));
			return;
		}
		
		if (!StringUtils.isBlank(object.getDrugUuid()) && StringUtils.isBlank(object.getDispensingUnitUuid())) {
			errors.rejectValue("dispensingUnitUuid",
			    messageSourceService.getMessage("stockmanagement.stockitem.dispensingUnitUuidRequired"));
			return;
		}
		
		if (StringUtils.isBlank(object.getCommonName())) {
			object.setCommonName(null);
		} else if (object.getCommonName().length() > 255) {
			errors.rejectValue("commonName", messageSourceService.getMessage("stockmanagement.stockitem.commonName255"));
			return;
		}
		
		if (StringUtils.isBlank(object.getAcronym())) {
			object.setAcronym(null);
		} else if (object.getAcronym().length() > 255) {
			errors.rejectValue("acronym", messageSourceService.getMessage("stockmanagement.stockitem.acronym255"));
			return;
		}
		
		if ((!StringUtils.isBlank(object.getPurchasePriceUoMUuid()) && object.getPurchasePrice() == null)
		        || (StringUtils.isBlank(object.getPurchasePriceUoMUuid()) && object.getPurchasePrice() != null)) {
			errors.rejectValue("purchasePrice",
			    messageSourceService.getMessage("stockmanagement.stockitem.purchasepriceanduomrequired"));
			return;
		}
		
		if (object.getPurchasePrice() != null && object.getPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
			errors.rejectValue("purchasePrice",
			    messageSourceService.getMessage("stockmanagement.stockitem.purchasepositive"));
			return;
		}
		
		if ((!StringUtils.isBlank(object.getReorderLevelUoMUuid()) && object.getReorderLevel() == null)
		        || (StringUtils.isBlank(object.getReorderLevelUoMUuid()) && object.getReorderLevel() != null)) {
			errors.rejectValue("reorderlevel",
			    messageSourceService.getMessage("stockmanagement.stockitem.reorderlevelanduomrequired"));
			return;
		}
		
		if (object.getReorderLevel() != null && object.getReorderLevel().compareTo(BigDecimal.ZERO) < 0) {
			errors.rejectValue("reorderlevel",
			    messageSourceService.getMessage("stockmanagement.stockitem.reorderlevelpositive"));
			return;
		}
		
		if (object.getExpiryNotice() != null && object.getExpiryNotice() < 0) {
			errors.rejectValue("expiryNotice",
			    messageSourceService.getMessage("stockmanagement.stockitem.expirynoticepositive"));
			return;
		}
		
	}
}
