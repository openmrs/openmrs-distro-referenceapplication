package org.openmrs.module.stockmanagement.api.validator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.Role;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockRuleDTO;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Handler(supports = { StockRuleDTO.class }, order = 50)
public class StockRuleValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return StockRuleDTO.class.isAssignableFrom(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		if (target == null) {
			errors.reject(messageSourceService.getMessage("error.general"));
			return;
		}
		
		StockRuleDTO object = (StockRuleDTO) target;
		StockItem stockItem = null;
		
		if (StringUtils.isBlank(object.getStockItemUuid())) {
			errors.rejectValue("stockItemUuid",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "stock item"));
			return;
		} else {
			stockItem = Context.getService(StockManagementService.class).getStockItemByUuid(object.getStockItemUuid());
			if (stockItem == null) {
				errors.rejectValue("stockItemUuid", String.format(
				    messageSourceService.getMessage("stockmanagement.stockrule.fieldvaluenotexist"), "stock item"));
				return;
			}
		}
		
		if (StringUtils.isBlank(object.getName())) {
			errors.rejectValue("name",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "name"));
			return;
		} else if (object.getName().length() > 255) {
			errors.rejectValue("name",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.exceedslimit"), "name", 255));
			return;
		}
		
		if (!StringUtils.isBlank(object.getDescription()) && object.getDescription().length() > 500) {
			errors.rejectValue("description",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.exceedslimit"), "description", 500));
			return;
		}
		
		if (StringUtils.isBlank(object.getLocationUuid())) {
			errors.rejectValue("locationUuid",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "locationUuid"));
			return;
		} else {
			Location location = Context.getLocationService().getLocationByUuid(object.getLocationUuid());
			if (location == null) {
				errors.rejectValue("locationUuid", String.format(
				    messageSourceService.getMessage("stockmanagement.stockrule.fieldvaluenotexist"), "location"));
				return;
			}
		}
		
		if (object.getQuantity() == null) {
			errors.rejectValue("quantity",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "quantity"));
			return;
		} else if (object.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
			errors.rejectValue("quantity", String.format(
			    messageSourceService.getMessage("stockmanagement.stockrule.qtygreaterthanequalzero"), "quantity"));
			return;
		}
		
		if (object.getEnabled() == null) {
			errors.rejectValue("enabled",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "enabled"));
			return;
		}
		
		if (object.getEvaluationFrequency() == null) {
			errors.rejectValue("evaluationFrequency", String.format(
			    messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "evaluation frequency"));
			return;
		} else if (object.getEvaluationFrequency().compareTo(Long.valueOf(0)) <= 0) {
			errors.rejectValue("evaluationFrequency",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.qtygreaterthanequalzero"),
			        "evaluation frequency"));
			return;
		}
		
		if (object.getActionFrequency() == null) {
			errors.rejectValue("actionFrequency", String.format(
			    messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "action frequency"));
			return;
		} else if (object.getActionFrequency().compareTo(Long.valueOf(0)) <= 0) {
			errors.rejectValue("actionFrequency", String.format(
			    messageSourceService.getMessage("stockmanagement.stockrule.qtygreaterthanequalzero"), "action frequency"));
			return;
		}
		
		if (object.getStockItemPackagingUOMUuid() == null) {
			errors.rejectValue("stockItemPackagingUOMUuid",
			    String.format(messageSourceService.getMessage("stockmanagement.stockrule.fieldrequired"), "packaging unit"));
			return;
		} else {
			StockItemPackagingUOM stockItemPackagingUOM = Context.getService(StockManagementService.class)
			        .getStockItemPackagingUOMByUuid(object.getStockItemPackagingUOMUuid());
			if (stockItemPackagingUOM == null) {
				errors.rejectValue("stockItemPackagingUOMUuid", String.format(
				    messageSourceService.getMessage("stockmanagement.stockrule.fieldvaluenotexist"), "packaging unit"));
				return;
			} else if (!stockItemPackagingUOM.getStockItem().getUuid().equals(stockItem.getUuid())) {
				errors.rejectValue("stockItemPackagingUOMUuid",
				    messageSourceService.getMessage("stockmanagement.stockrule.packagingunitinvalidforstockitem"));
				return;
			}
		}
		
		if (!StringUtils.isBlank(object.getAlertRole())) {
			Role role = Context.getUserService().getRole(object.getAlertRole());
			if (role == null) {
				errors.rejectValue("alertRole", String.format(
				    messageSourceService.getMessage("stockmanagement.stockrule.fieldvaluenotexist"), "alert role"));
				return;
			}
		}
		
		if (!StringUtils.isBlank(object.getMailRole())) {
			Role role = Context.getUserService().getRole(object.getMailRole());
			if (role == null) {
				errors.rejectValue("mailRole", String.format(
				    messageSourceService.getMessage("stockmanagement.stockrule.fieldvaluenotexist"), "mail role"));
				return;
			}
		}
	}
}
