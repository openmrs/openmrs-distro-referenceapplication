package org.openmrs.module.stockmanagement.api.validator;

import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.dto.UserRoleScopeDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = { UserRoleScopeDTO.class }, order = 50)
public class UserRoleScopeDTOValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return UserRoleScopeDTO.class.isAssignableFrom(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		if (target == null) {
			errors.reject(messageSourceService.getMessage("error.general"));
			return;
		}
		
		UserRoleScopeDTO object = (UserRoleScopeDTO) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userUuid", "error.null");
		
		if (Context.getAuthenticatedUser().getUuid().equalsIgnoreCase(object.getUserUuid()))
			errors.rejectValue("userUuid",
			    messageSourceService.getMessage("stockmanagement.userrolescopes.useruuid.selfupdate"));
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role", messageSourceService.getMessage("error.null"));
		
		if (object.getPermanent() == null || !object.getPermanent()) {
			if (object.getActiveFrom() == null) {
				errors.rejectValue("activeFrom", messageSourceService.getMessage("error.null"));
			}
			if (object.getActiveTo() == null) {
				errors.rejectValue("activeTo", messageSourceService.getMessage("error.null"));
			}
			
			if (object.getActiveTo() != null && object.getActiveFrom() != null
			        && object.getActiveFrom().after(object.getActiveTo())) {
				errors.rejectValue("activeFrom",
				    messageSourceService.getMessage("stockmanagement.userrolescopes.activefrom.after.to"));
			}
		}
		
		if (object.getLocations() == null || object.getLocations().isEmpty()) {
			errors.rejectValue("locations", messageSourceService.getMessage("error.null"));
		}
		
		if (object.getOperationTypes() == null || object.getOperationTypes().isEmpty()) {
			errors.rejectValue("operationTypes", messageSourceService.getMessage("error.null"));
		}
		
		//ValidateUtil.validateFieldLengths(errors, UserRoleScopeDTO.class, "gender", "personVoidReason");
	}
}
