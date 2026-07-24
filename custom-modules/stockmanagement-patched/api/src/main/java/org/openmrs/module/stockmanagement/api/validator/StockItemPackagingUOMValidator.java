package org.openmrs.module.stockmanagement.api.validator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockItemPackagingUOMDTO;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Handler(supports = { StockItemPackagingUOMDTO.class }, order = 50)
public class StockItemPackagingUOMValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return StockItemPackagingUOMDTO.class.isAssignableFrom(aClass);
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
		StockItemPackagingUOMDTO object = (StockItemPackagingUOMDTO) target;
		StockItemPackagingUOM stockItemPackagingUOM = null;
		if (object.getUuid() != null) {
			stockItemPackagingUOM = service.getStockItemPackagingUOMByUuid(object.getUuid());
			if (stockItemPackagingUOM == null) {
				errors.rejectValue("uuid",
				    messageSourceService.getMessage("stockmanagement.stockitempackagingunit.notexists"));
				return;
			}
		}
		
		if (StringUtils.isBlank(object.getPackagingUomUuid())) {
			errors.rejectValue("packagingUomUuid",
			    messageSourceService.getMessage("stockmanagement.stockitempackagingunit.packagingUoMRequired"));
			return;
		}
		
		if (object.getFactor() == null) {
			errors.rejectValue("factor",
			    messageSourceService.getMessage("stockmanagement.stockitempackagingunit.factorrequired"));
			return;
		} else if (object.getFactor().compareTo(BigDecimal.ZERO) <= 0) {
			errors.rejectValue("factor",
			    messageSourceService.getMessage("stockmanagement.stockitempackagingunit.factorpostive"));
			return;
		}
	}
}
