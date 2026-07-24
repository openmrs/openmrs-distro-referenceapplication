package org.openmrs.module.stockmanagement.api.validator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.Role;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.BatchJobDTO;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;
import org.openmrs.module.stockmanagement.api.dto.StockRuleDTO;
import org.openmrs.module.stockmanagement.api.dto.reporting.Fullfillment;
import org.openmrs.module.stockmanagement.api.dto.reporting.MostLeastMoving;
import org.openmrs.module.stockmanagement.api.model.BatchJobType;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;
import org.openmrs.module.stockmanagement.api.model.StockSource;
import org.openmrs.module.stockmanagement.api.reporting.Report;
import org.openmrs.module.stockmanagement.api.reporting.ReportGenerator;
import org.openmrs.module.stockmanagement.api.reporting.ReportParameter;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;
import org.openmrs.module.stockmanagement.api.utils.GlobalProperties;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

@Handler(supports = { BatchJobDTO.class }, order = 50)
public class BatchJobValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return BatchJobDTO.class.isAssignableFrom(aClass);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		if (target == null) {
			errors.reject(messageSourceService.getMessage("error.general"));
			return;
		}

		BatchJobDTO object = (BatchJobDTO) target;
		
		if (object.getBatchJobType() == null) {
			errors.rejectValue("batchJobType",
			    String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldrequired"), "Batch Job Type"));
			return;
		} else {
			BatchJobType batchJobType = object.getBatchJobType();
			if(batchJobType == null || !batchJobType.equals(BatchJobType.Report)){
				errors.rejectValue("batchJobType", String.format(
						messageSourceService.getMessage("stockmanagement.batchjob.fieldvaluenotexist"), "Batch Job Type"));
				return;
			}
		}
		
		if (StringUtils.isBlank(object.getDescription())) {
			errors.rejectValue("description",
			    String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldrequired"), "description"));
			return;
		} else if (object.getDescription().length() > 255) {
			errors.rejectValue("description",
			    String.format(messageSourceService.getMessage("stockmanagement.batchjob.exceedslimit"), "description", 255));
			return;
		}

		if (StringUtils.isBlank(object.getParameters())) {
			errors.rejectValue("parameters",
					String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldrequired"), "parameters"));
			return;
		} else if (object.getParameters().length() > 5000) {
			errors.rejectValue("parameters",
					String.format(messageSourceService.getMessage("stockmanagement.batchjob.exceedslimit"), "parameters", 5000));
			return;
		}

		Properties properties = null;
		try {
			properties = GlobalProperties.fromString(object.getParameters());
		} catch (IOException e) {
			errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldrequired"), "parameters"));
			return;
		}
		if(properties.isEmpty()){
			errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldrequired"), "parameters"));
			return;
		}

		String reportSystemName = null;
		if((!properties.containsKey("param.report")) || StringUtils.isBlank(reportSystemName = properties.getProperty("param.report"))){
			errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldrequired"), "param.report"));
			return;
		}

		final String finalReportSystemName = reportSystemName;
		Optional<Report> report = Report.getAllReports().stream().filter(p->p.getSystemName().equals(finalReportSystemName)).findAny();
		if(!report.isPresent()){
			errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldvaluenotexist"), "report"));
			return;
		}

		StockManagementService stockManagementService = Context.getService(StockManagementService.class);
		for(Map.Entry<Object,Object> property : properties.entrySet()) {
			String propertyName = (String) property.getKey();
			if (!propertyName.startsWith("param.")) {
				errors.rejectValue("parameters", messageSourceService.getMessage("stockmanagement.batchjob.paramformatinvalid"));
				return;
			}
			if (propertyName.equals("param.report")) {
				continue;
			} else if (propertyName.endsWith(".value")) {
				int indexOfToken = propertyName.lastIndexOf(".value");
				if (indexOfToken > 6) {
					String parameterName = propertyName.substring(6, indexOfToken);
					ReportParameter reportParameter = ReportParameter.findInList(parameterName, report.get().getParameters());
					if (reportParameter == null) {
						errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldvaluenotexist"), "parameter name"));
						return;
					}else if(!isValidReportParameter(reportParameter,(String)property.getValue(), object, stockManagementService)){
						errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldvaluenotexist"), parameterName));
						return;
					}
				} else {
					errors.rejectValue("parameters", messageSourceService.getMessage("stockmanagement.batchjob.supportedparams"));
					return;
				}
			} else if (propertyName.endsWith(".value.desc")) {
				int indexOfToken = propertyName.lastIndexOf(".value.desc");
				if (indexOfToken > 6) {
					String parameterName = propertyName.substring(6, indexOfToken);
					ReportParameter reportParameter = ReportParameter.findInList(parameterName, report.get().getParameters());
					if (reportParameter == null) {
						errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldvaluenotexist"), "parameter display value"));
						return;
					}
				} else {
					errors.rejectValue("parameters", messageSourceService.getMessage("stockmanagement.batchjob.supportedparams"));
					return;
				}
			}else if (propertyName.endsWith(".description")) {
				int indexOfToken = propertyName.lastIndexOf(".description");
				if (indexOfToken > 6) {
					String parameterName = propertyName.substring(6, indexOfToken);
					ReportParameter reportParameter = ReportParameter.findInList(parameterName, report.get().getParameters());
					if (reportParameter == null) {
						errors.rejectValue("parameters", String.format(messageSourceService.getMessage("stockmanagement.batchjob.fieldvaluenotexist"), "parameter description"));
						return;
					}
				} else {
					errors.rejectValue("parameters", messageSourceService.getMessage("stockmanagement.batchjob.supportedparams"));
					return;
				}
			} else {
				errors.rejectValue("parameters", messageSourceService.getMessage("stockmanagement.batchjob.supportedparams"));
				return;
			}
		}
		object.setPrivilegeScope(Privileges.APP_STOCKMANAGEMENT_REPORTS);
	}
	
	private boolean isValidReportParameter(ReportParameter reportParameter, String value, BatchJobDTO batchJobDTO,
	        StockManagementService stockManagementService) {
		if (reportParameter.isDate()) {
			try {
				Date date = DateUtil.parseDate(value);
			}
			catch (Exception exception) {
				return false;
			}
		} else if (reportParameter.isLocation()) {
			Location location = Context.getLocationService().getLocationByUuid(value);
			if (location == null) {
				return false;
			}
			batchJobDTO.setLocationScopeUuid(location.getUuid());
		} else if (reportParameter.isStockItemCategory()) {
			if (!StringUtils.isBlank(value)) {
				Concept concept = Context.getConceptService().getConceptByUuid(value);
				if (concept == null) {
					return false;
				}
			}
		} else if (reportParameter.isBoolean()) {
			return "true".equals(value) || "false".equals(value);
		} else if (reportParameter.isStockSource()) {
			if (!StringUtils.isBlank(value)) {
				StockSource stockSource = stockManagementService.getStockSourceByUuid(value);
				if (stockSource == null) {
					return false;
				}
			}
		} else if (reportParameter.isInventoryGroupBy()) {
			StockItemInventorySearchFilter.InventoryGroupBy result = StockItemInventorySearchFilter.InventoryGroupBy
			        .findByName(value);
			if (result == null) {
				return false;
			}
		} else if (reportParameter.isMaxReorderLevelRatio()) {
			if (StringUtils.isBlank(value)) {
				return false;
			}
			try {
				BigDecimal ratio = new BigDecimal(value);
				if (ratio.compareTo(BigDecimal.ZERO) < 0) {
					return false;
				}
			}
			catch (Exception e) {
				return false;
			}
		} else if (reportParameter.isPatient()) {
			if (!StringUtils.isBlank(value)) {
				Patient patient = Context.getPatientService().getPatientByUuid(value);
				if (patient == null) {
					return false;
				}
			}
		} else if (reportParameter.isStockItem()) {
			if (!StringUtils.isBlank(value)) {
				StockItem stockItem = stockManagementService.getStockItemByUuid(value);
				if (stockItem == null) {
					return false;
				}
			}
		} else if (reportParameter.isMostLeastMoving()) {
			if (StringUtils.isBlank(value)) {
				return false;
			}
			MostLeastMoving result = MostLeastMoving.findByName(value);
			if (result == null) {
				return false;
			}
		} else if (reportParameter.isUint()) {
			if (StringUtils.isBlank(value)) {
				return false;
			}
			try {
				Integer uInt = Integer.parseInt(value);
				if (uInt < 0) {
					return false;
				}
			}
			catch (Exception e) {
				return false;
			}
		} else if (reportParameter.isFullfillment()) {
			if (StringUtils.isBlank(value)) {
				return false;
			}
			String[] values = value.split(",");
			for (String option : values) {
				Fullfillment result = Fullfillment.findByName(option);
				if (result == null) {
					return false;
				}
			}
		}
		return true;
	}
}
