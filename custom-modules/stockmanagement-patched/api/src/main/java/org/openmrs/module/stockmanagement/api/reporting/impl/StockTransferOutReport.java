package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockOperationLineItem;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockOperationLineItemFilter;
import org.openmrs.module.stockmanagement.api.model.Party;
import org.openmrs.module.stockmanagement.api.model.StockOperationType;
import org.openmrs.module.stockmanagement.api.reporting.ReportGenerator;
import org.openmrs.module.stockmanagement.api.utils.csv.CSVWriter;

import java.util.Arrays;
import java.util.Properties;

public class StockTransferOutReport extends StockOperationLineItemReport {
	
	@Override
	protected void setLocationFilter(StockOperationLineItemFilter filter, Location location, Boolean childLocations) {
		filter.setAtLocationId(location.getLocationId());
		filter.setChildLocations(childLocations);
	}
	
	@Override
	protected void setFilters(StockOperationLineItemFilter filter, Properties parameters) {
		StockOperationType stockOperationType = stockManagementService
		        .getStockOperationTypeByType(StockOperationType.TRANSFER_OUT);
		filter.setStockOperationTypes(Arrays.asList(stockOperationType));
		filter.setIncludeRequisitionInfo(false);
	}
	
	@Override
	protected void writeRow(CSVWriter csvWriter, StockOperationLineItem row) {
		writeLineToCsv(
		    csvWriter,
		    TIMESTAMP_FORMATTER.format(row.getDateCreated()),
		    DATE_FORMATTER.format(row.getOperationDate()),
		    row.getOperationTypeName(),
		    row.getOperationNumber(),
		    String.format("%1$s %2$s", row.getCreatorFamilyName(), row.getCreatorGivenName()),
		    emptyIfNull(row.getResponsiblePerson() != null ? String.format("%1$s %2$s",
		        row.getResponsiblePersonFamilyName(), row.getResponsiblePersonGivenName()) : row.getResponsiblePersonOther()),
		    row.getCompletedDate() == null ? "" : TIMESTAMP_FORMATTER.format(row.getCompletedDate()), String.format(
		        "%1$s %2$s", row.getCompletedByFamilyName(), row.getCompletedByGivenName()), emptyIfNull(row.getRemarks()),
		    emptyIfNull(row.getSourceName()), emptyIfNull(row.getDestinationName()),
		    row.getStockItemDrugName() == null ? row.getStockItemConceptName() : row.getStockItemDrugName(), row
		            .getStockItemDrugName() == null ? "" : row.getStockItemConceptName(), emptyIfNull(row.getCommonName()),
		    emptyIfNull(row.getAcronym()), emptyIfNull(row.getStockItemCategoryName()), emptyIfNull(row.getBatchNo()), row
		            .getExpiration() != null ? DATE_FORMATTER.format(row.getExpiration()) : "", row.getQuantity()
		            .toPlainString(), row.getStockItemPackagingUOMName(), row.getStockItemPackagingUOMFactor()
		            .toPlainString(), row.getStockItemDrugId() == null ? "" : row.getStockItemDrugId().toString(), row
		            .getStockItemConceptId() == null ? "" : row.getStockItemConceptId().toString());
	}
	
	@Override
	protected void writeHeaders(CSVWriter csvWriter) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		writeLineToCsv(csvWriter, messageSourceService.getMessage("stockmanagement.report.datecreated"),
		    messageSourceService.getMessage("stockmanagement.report.operationdate"),
		    messageSourceService.getMessage("stockmanagement.report.operationtype"),
		    messageSourceService.getMessage("stockmanagement.report.operationnumber"),
		    messageSourceService.getMessage("stockmanagement.report.startedby"),
		    messageSourceService.getMessage("stockmanagement.report.responsibleperson"),
		    messageSourceService.getMessage("stockmanagement.report.completeddate"),
		    messageSourceService.getMessage("stockmanagement.report.completedby"),
		    messageSourceService.getMessage("stockmanagement.report.remarks"),
		    messageSourceService.getMessage("stockmanagement.report.source"),
		    messageSourceService.getMessage("stockmanagement.report.destination"),
		    messageSourceService.getMessage("stockmanagement.report.genericname"),
		    messageSourceService.getMessage("stockmanagement.report.tradename"),
		    messageSourceService.getMessage("stockmanagement.report.commonname"),
		    messageSourceService.getMessage("stockmanagement.report.acronym"),
		    messageSourceService.getMessage("stockmanagement.report.category"),
		    messageSourceService.getMessage("stockmanagement.report.batchno"),
		    messageSourceService.getMessage("stockmanagement.report.batchexpiry"),
		    messageSourceService.getMessage("stockmanagement.report.quantity"),
		    messageSourceService.getMessage("stockmanagement.report.qtyunit"),
		    messageSourceService.getMessage("stockmanagement.report.packsize"),
		    messageSourceService.getMessage("stockmanagement.report.drugid"),
		    messageSourceService.getMessage("stockmanagement.report.conceptid"));
	}
}
