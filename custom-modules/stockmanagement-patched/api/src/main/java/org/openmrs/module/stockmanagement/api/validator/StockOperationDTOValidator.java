package org.openmrs.module.stockmanagement.api.validator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Handler(supports = { StockOperationDTO.class }, order = 50)
public class StockOperationDTOValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> aClass) {
		return StockOperationDTO.class.isAssignableFrom(aClass);
	}
	
	@Override
    public void validate(Object target, Errors errors) {
        MessageSourceService messageSourceService = Context.getMessageSourceService();
        if (target == null) {
            errors.reject(messageSourceService.getMessage("error.general"));
            return;
        }

        if (Context.getAuthenticatedUser() == null) {
            errors.reject(messageSourceService.getMessage("stockmanagement.stockoperation.authrequired"));
            return;
        }

        StockManagementService service = Context.getService(StockManagementService.class);
        StockOperationDTO object = (StockOperationDTO) target;
        StockOperation stockOperation = null;
        if (object.getUuid() != null) {
            stockOperation = service.getStockOperationByUuid(object.getUuid());
            if(stockOperation == null || !stockOperation.isUpdateable()){
                errors.rejectValue("uuid", messageSourceService.getMessage("stockmanagement.stockoperation.notupdateable"));
                return;
            }
            object.setOperationTypeUuid(stockOperation.getStockOperationType().getUuid());
        }

        if (object.getOperationTypeUuid() == null) {
            if (StringUtils.isBlank(object.getOperationTypeUuid())) {
                errors.rejectValue("operationTypeUuid", messageSourceService.getMessage("stockmanagement.stockoperation.operationtypeuuidrequired"));
                return;
            }
        }

        if (object.getOperationDate() == null) {
            errors.rejectValue("operationDate", messageSourceService.getMessage("stockmanagement.stockoperation.sourcerequired"));
            return;
        }

        if (object.getOperationDate().after(new Date())) {
            errors.rejectValue("operationDate", messageSourceService.getMessage("stockmanagement.stockoperation.operationdatenotfuture"));
            return;
        }

        Location permissionLocation = null;

        StockOperationType stockOperationType = stockOperation != null ? stockOperation.getStockOperationType() : service.getStockOperationTypeByUuid(object.getOperationTypeUuid());
        if (stockOperationType.getHasSource() != null && stockOperationType.getHasSource()) {
            if (object.getSourceUuid() == null) {
                errors.rejectValue("sourceUuid", messageSourceService.getMessage("stockmanagement.stockoperation.sourcerequired"));
                return;
            }
            Party party = service.getPartyByUuid(object.getSourceUuid());
            if (party == null || party.getVoided()) {
                errors.rejectValue("sourceUuid", messageSourceService.getMessage("stockmanagement.stockoperation.sourceuuidinvalid"));
                return;
            }
            if (stockOperationType.getSourceType() == LocationType.Location) {
                if (party.getLocation() == null) {
                    errors.rejectValue("sourceUuid", messageSourceService.getMessage("stockmanagement.stockoperation.sourcenotlocation"));
                    return;
                }

                Set<StockOperationTypeLocationScope> locationScope = stockOperationType.getStockOperationTypeLocationScopes();
                if (locationScope != null && !locationScope.isEmpty()) {
                    List<StockOperationTypeLocationScope> sourceScope = locationScope.stream().filter(p -> p.getIsSource()).collect(Collectors.toList());
                    if (!sourceScope.isEmpty()) {
                        Set<LocationTag> locationTags = party.getLocation().getTags();
                        if (locationTags == null || locationTags.isEmpty() || !sourceScope.stream().anyMatch(p -> locationTags.stream().anyMatch(x -> x.getName().equals(p.getLocationTag())))) {
                            errors.rejectValue("sourceUuid", messageSourceService.getMessage("stockmanagement.stockoperation.sourcelocationtagnotmatched"));
                            return;
                        }
                    }
                }
                permissionLocation = party.getLocation();
            } else {
                if (party.getStockSource() == null) {
                    errors.rejectValue("sourceUuid", messageSourceService.getMessage("stockmanagement.stockoperation.sourcenotstocksource"));
                    return;
                }
            }
        }
        else if(object.getDestinationUuid() != null){
            errors.rejectValue("sourceUuid", messageSourceService.getMessage("stockmanagement.stockoperation.sourcenotrequired"));
            return;
        }

        if (stockOperationType.getHasDestination() != null && stockOperationType.getHasDestination()) {
            if (object.getDestinationUuid() == null) {
                errors.rejectValue("destinationUuid", messageSourceService.getMessage("stockmanagement.stockoperation.destinationrequired"));
                return;
            }
            Party party = service.getPartyByUuid(object.getDestinationUuid());
            if (party == null || party.getVoided()) {
                errors.rejectValue("destinationUuid", messageSourceService.getMessage("stockmanagement.stockoperation.destinationuuidinvalid"));
                return;
            }
            if (stockOperationType.getDestinationType() == LocationType.Location) {
                if (party.getLocation() == null) {
                    errors.rejectValue("destinationUuid", messageSourceService.getMessage("stockmanagement.stockoperation.destinationnotlocation"));
                    return;
                }

                Set<StockOperationTypeLocationScope> locationScope = stockOperationType.getStockOperationTypeLocationScopes();
                if (locationScope != null && !locationScope.isEmpty()) {
                    List<StockOperationTypeLocationScope> destinationScope = locationScope.stream().filter(p -> p.getIsDestination()).collect(Collectors.toList());
                    if (!destinationScope.isEmpty()) {
                        Set<LocationTag> locationTags = party.getLocation().getTags();
                        if (locationTags == null || locationTags.isEmpty() || !destinationScope.stream().anyMatch(p -> locationTags.stream().anyMatch(x -> x.getName().equals(p.getLocationTag())))) {
                            errors.rejectValue("destinationUuid", messageSourceService.getMessage("stockmanagement.stockoperation.destinationlocationtagnotmatched"));
                            return;
                        }
                    }
                }

                if (permissionLocation == null)
                    permissionLocation = party.getLocation();

            } else {
                if (party.getStockSource() == null) {
                    errors.rejectValue("destinationUuid", messageSourceService.getMessage("stockmanagement.stockoperation.destinationnotstockdestination"));
                    return;
                }
            }
        }
        else if(object.getDestinationUuid() != null){
            errors.rejectValue("destinationUuid", messageSourceService.getMessage("stockmanagement.stockoperation.destinationnotrequired"));
            return;
        }

        if(stockOperationType.requiresReason() && StringUtils.isBlank(object.getReasonUuid())){
            errors.rejectValue("operationTypeUuid", messageSourceService.getMessage("stockmanagement.stockoperation.noreason"));
            return;
        }

        if(object.getStockOperationItems() == null  || object.getStockOperationItems().isEmpty()){
            errors.rejectValue("operationTypeUuid", messageSourceService.getMessage("stockmanagement.stockoperation.itemsrequired"));
            return;
        }

        if(permissionLocation == null){
            errors.rejectValue("operationTypeUuid", messageSourceService.getMessage("stockmanagement.stockoperation.nopermission"));
            return;
        }

        object.setAtLocationUuid(permissionLocation.getUuid());
        if (!stockOperationType.userCanProcess(Context.getAuthenticatedUser(), permissionLocation)) {
            errors.rejectValue("operationTypeUuid", messageSourceService.getMessage("stockmanagement.stockoperation.nopermission"));
            return;
        }
        Result<StockOperationItemDTO> stockOperationItems = null;
        if(object.getUuid() != null){
            StockOperationItemSearchFilter itemsFilter = new StockOperationItemSearchFilter();
            itemsFilter.setStockOperationUuids(Arrays.asList(object.getUuid()));
            stockOperationItems = service.findStockOperationItems(itemsFilter);
            if(!stockOperationItems.getData().stream().allMatch(p-> object.getStockOperationItems().stream().anyMatch(x-> p.getUuid().equals(x.getUuid()) && p.getStockItemUuid().equals(x.getStockItemUuid())))){
                errors.rejectValue("stockOperationItems", messageSourceService.getMessage("stockmanagement.stockoperation.itemsmissinginupdate"));
                return;
            }
        }

        if(!StringUtils.isBlank(object.getRemarks()) && object.getRemarks().length() > 255){
            errors.rejectValue("remarks", messageSourceService.getMessage("stockmanagement.stockoperation.remarks255"));
            return;
        }

        if(!StringUtils.isBlank(object.getResponsiblePersonOther()) && object.getResponsiblePersonOther().length() > 150){
            errors.rejectValue("responsiblePersonOther", messageSourceService.getMessage("stockmanagement.stockoperation.responsiblePersonOther150"));
            return;
        }

        int index = 1;
        BigDecimal zero = new BigDecimal(0);
        for(StockOperationItemDTO stockOperationItemDTO : object.getStockOperationItems()){
            if(stockOperationItemDTO.getStockItemUuid() == null){
                errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.itemuuidrequired"), index));
                return;
            }

            if(stockOperationType.requiresBatchUuid() && stockOperationItemDTO.getStockBatchUuid() == null){
                errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.batchuuidrequired"), index));
                return;
            }

            if(stockOperationType.requiresActualBatchInformation()) {
                if (StringUtils.isBlank(stockOperationItemDTO.getBatchNo())) {
                    errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.batchnorequired"), index));
                    return;
                }

                if(stockOperationItemDTO.getExpiration() != null && !stockOperationItemDTO.getExpiration().after(DateUtil.today())){
                    errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.expirydateinpast"), index));
                    return;
                }

                if(!StringUtils.isBlank(stockOperationItemDTO.getUuid())){
                    Optional<StockOperationItemDTO> existingItemDto = stockOperationItems.getData().stream().filter(p->p.getUuid().equals(stockOperationItemDTO.getUuid())).findFirst();
                    if(existingItemDto.isPresent() && existingItemDto.get().getHasExpiration()){
                        if(stockOperationItemDTO.getExpiration() == null){
                            errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.expirydaterequired"), index));
                            return;
                        }
                    }
                }
            }

            if(!stockOperationType.isQuantityOptional() && stockOperationItemDTO.getQuantity() == null){
                errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.qtyrequired"), index));
                return;
            }

            if(stockOperationItemDTO.getQuantity() != null){
                if(!stockOperationType.isNegativeItemQuantityAllowed() && stockOperationItemDTO.getQuantity().compareTo(zero) <= 0){
                    errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.qtyrequired"), index));
                    return;
                }

                if(stockOperationItemDTO.getStockItemPackagingUOMUuid() == null){
                    errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.uomrequired"), index));
                    return;
                }
            }

            if(stockOperationItemDTO.getPurchasePrice() != null && stockOperationItemDTO.getPurchasePrice().compareTo(zero) < -1){
                errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.uomrequired"), index));
                return;
            }

            if(stockOperationType.getOperationType().equals(StockOperationType.STOCK_ISSUE)){
                if(stockOperationItemDTO.getQuantityRequested() != null || stockOperationItemDTO.getStockItemPackagingUOMUuid() != null){
                    if(stockOperationItemDTO.getQuantityRequested() != null && stockOperationItemDTO.getQuantityRequested().compareTo(zero) <= 0){
                        errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.qtyrequestedrequired"), index));
                        return;
                    }

                    if(stockOperationItemDTO.getStockItemPackagingUOMUuid() == null){
                        errors.rejectValue("stockOperationItems", String.format(messageSourceService.getMessage("stockmanagement.stockoperation.qtyrequesteduomrequired"), index));
                        return;
                    }
                }
            }
        }
    }
}
