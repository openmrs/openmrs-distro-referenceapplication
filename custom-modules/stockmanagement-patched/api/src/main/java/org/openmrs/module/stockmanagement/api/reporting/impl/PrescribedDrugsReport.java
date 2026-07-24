package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.Result;
import org.openmrs.module.stockmanagement.api.dto.reporting.*;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.api.reporting.ReportGenerator;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;
import org.openmrs.module.stockmanagement.api.utils.GlobalProperties;
import org.openmrs.module.stockmanagement.api.utils.csv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

public class PrescribedDrugsReport extends ReportGenerator {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected StockManagementService stockManagementService = null;
	
	protected BatchJob batchJob = null;
	
	private BigDecimal negativeOne = BigDecimal.valueOf(-1);
	
	protected boolean isFullfullmentReport() {
		return false;
	}
	
	protected void setAdditionFilters(PrescriptionLineFilter filter, Properties parameters) {
	}
	
	@Override
	public void generateReport(BatchJob batchJob, Function<BatchJob, Boolean> shouldStopExecution) {
		this.batchJob = batchJob;
		stockManagementService = Context.getService(StockManagementService.class);
		Integer pageSize = GlobalProperties.GetReportingRecordsPageSize();
		
		if (!restoreExecutionState(batchJob, stockManagementService, log)) {
			return;
		}
		
		if (shouldStopExecution.apply(batchJob)) {
			return;
		}
		
		Properties parameters = null;
		
		try {
			parameters = GlobalProperties.fromString(batchJob.getParameters());
		}
		catch (IOException e) {
			stockManagementService.failBatchJob(batchJob.getUuid(), "Failed to read parameters");
			log.error(e.getMessage(), e);
			return;
		}
		
		Date startDate = getStartDate(parameters);
		Date endDate = getEndDate(parameters);
		String locationUuid = getLocation(parameters);
		Boolean childLocations = getChildLocations(parameters);
		String stockItemUuid = getStockItem(parameters);
		String patientUuid = getPatient(parameters);
		String stockItemCategoryUuid = getStockItemCategory(parameters);
		
		CSVWriter csvWriter = null;
		Writer writer = null;
		
		try {
			boolean hasMoreRecords = true;
			PrescriptionLineFilter filter = new PrescriptionLineFilter();
			filter.setStartDate(startDate);
			if (endDate != null) {
				filter.setEndDate(DateUtil.endOfDay(endDate));
			}
			if (!StringUtils.isBlank(locationUuid)) {
				Location location = Context.getLocationService().getLocationByUuid(locationUuid);
				if (location == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(), "Report location parameter not found");
					return;
				}
				filter.setAtLocationId(location.getId());
				filter.setChildLocations(childLocations);
			}
			
			if (!StringUtils.isBlank(stockItemUuid)) {
				StockItem stockItem = stockManagementService.getStockItemByUuid(stockItemUuid);
				if (stockItem == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(), "Report stock item parameter not found");
					return;
				}
				if (stockItem.getDrug() != null) {
					filter.setDrugId(stockItem.getDrug().getId());
				} else {
					filter.setStockItemId(stockItem.getId());
				}
			}
			
			if (!StringUtils.isBlank(patientUuid)) {
				Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);
				if (patient == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(), "Report patient parameter not found");
					return;
				}
				filter.setPatientId(patient.getId());
			}
			
			if (!StringUtils.isBlank(stockItemCategoryUuid)) {
				Concept concept = Context.getConceptService().getConceptByUuid(stockItemCategoryUuid);
				if (concept == null) {
					stockManagementService
					        .failBatchJob(batchJob.getUuid(), "Report stock item category parameter not found");
					return;
				}
				filter.setStockItemCategoryConceptId(concept.getConceptId());
			}
			
			Integer observationDispensingLocationId = null, observationDrugId = null;
			String configuredConcept = GlobalProperties.getObservationDispensingLocationConcept();
			if (!StringUtils.isBlank(configuredConcept)) {
				Concept concept = Context.getConceptService().getConceptByUuid(configuredConcept);
				if (concept == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(),
					    "Concept configured for Observation: dispensing location concept not found");
					return;
				}
				observationDispensingLocationId = concept.getConceptId();
			}
			
			configuredConcept = GlobalProperties.getObservationDrugConcept();
			if (!StringUtils.isBlank(configuredConcept)) {
				Concept concept = Context.getConceptService().getConceptByUuid(configuredConcept);
				if (concept == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(),
					    "Concept configured for Observation: drug concept not found");
					return;
				}
				observationDrugId = concept.getConceptId();
			}
			filter.setObservationDrugConceptId(observationDrugId);
			filter.setObservationDispensingLocationConceptId(observationDispensingLocationId);
			setAdditionFilters(filter, parameters);
			
			MessageSourceService messageSourceService = Context.getMessageSourceService();
			filter.setLimit(pageSize);
			boolean hasAppendedHeaders = false;
			while (hasMoreRecords) {
				if (shouldStopExecution.apply(batchJob)) {
					return;
				}
				filter.setStartIndex(pageIndex);
				filter.setPrescriptionTransactionMin(lastRecordProcessed);
				Result<PrescriptionLineItem> data = stockManagementService.findPrescriptionLineItems(filter);
				if (shouldStopExecution.apply(batchJob)) {
					return;
				}
				if (!hasAppendedHeaders) {
					if (writer == null) {
						writer = Files.newBufferedWriter(resultsFile.toPath(), StandardCharsets.UTF_8,
						    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
					}
					if (csvWriter == null) {
						csvWriter = createCsvWriter(writer);
					}
					if (!hasRestoredExecutionState) {
						writeHeaders(csvWriter);
					}
					hasAppendedHeaders = true;
				}
				if (!data.getData().isEmpty()) {
					for (PrescriptionLineItem row : data.getData()) {
						writeRow(csvWriter, row, messageSourceService);
					}
					csvWriter.flush();
					recordsProcessed += data.getData().size();
					pageIndex++;
					PrescriptionLineItem lastRecord = data.getData().get(data.getData().size() - 1);
					updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, lastRecord.getId(), null,
					    stockManagementService, null);
				} else if (pageIndex == 0) {
					updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, null, null,
					    stockManagementService, null);
				}
				
				hasMoreRecords = data.getData().size() >= pageSize;
			}
			
			csvWriter.close();
			long fileSizeInBytes = Files.size(resultsFile.toPath());
			completeBatchJob(batchJob, fileSizeInBytes, "csv", fileSizeInBytes <= (1024 * 1024), stockManagementService);
		}
		catch (IOException e) {
			stockManagementService.failBatchJob(batchJob.getUuid(), "Input/Output error: " + e.getMessage());
			log.error(e.getMessage(), e);
		}
		finally {
			if (csvWriter != null) {
				try {
					try {
						csvWriter.flush();
					}
					catch (Exception e) {}
					csvWriter.close();
				}
				catch (Exception csvWriterException) {}
			}
			if (writer != null) {
				try {
					try {
						writer.flush();
					}
					catch (Exception e) {}
					writer.close();
				}
				catch (Exception we) {}
			}
		}
		
	}
	
	protected void writeRow(CSVWriter csvWriter, PrescriptionLineItem row, MessageSourceService messageSourceService) {
		String[] columns = new String[31 + (isFullfullmentReport() ? 9 : 0)];
		int columnIndex = 0;
		columns[columnIndex++] = row.getCreatedFrom();
		columns[columnIndex++] = row.getFulfilmentLocation();
		if (isFullfullmentReport()) {
			columns[columnIndex++] = row.getDispensingLocation();
		}
		columns[columnIndex++] = TIMESTAMP_FORMATTER.format(row.getDateCreated());
		columns[columnIndex++] = row.getDateActivated() != null ? TIMESTAMP_FORMATTER.format(row.getDateActivated()) : null;
		columns[columnIndex++] = row.getDateStopped() != null ? TIMESTAMP_FORMATTER.format(row.getDateStopped()) : null;
		if (isFullfullmentReport()) {
			columns[columnIndex++] = row.getDateDispensed() != null ? TIMESTAMP_FORMATTER.format(row.getDateDispensed())
			        : null;
		}
		columns[columnIndex++] = String.format("%1$s %2$s", row.getOrdererFamilyName(), row.getOrdererGivenName());
		if (isFullfullmentReport()) {
			columns[columnIndex++] = row.getDispenserFamilyName() != null ? String.format("%1$s %2$s",
			    row.getDispenserFamilyName(), row.getDispenserGivenName()) : null;
		}
		columns[columnIndex++] = row.getPatientIdentifier();
		columns[columnIndex++] = row.getStockItemDrugName() == null ? row.getStockItemConceptName() : row
		        .getStockItemDrugName();
		columns[columnIndex++] = row.getStockItemDrugName() == null ? "" : row.getStockItemConceptName();
		columns[columnIndex++] = emptyIfNull(row.getCommonName());
		columns[columnIndex++] = emptyIfNull(row.getAcronym());
		columns[columnIndex++] = emptyIfNull(row.getStockItemCategoryName());
		columns[columnIndex++] = row.getQuantity() != null ? row.getQuantity().toPlainString() : null;
		columns[columnIndex++] = row.getQuantityUnitsConceptName();
		columns[columnIndex++] = row.getStockItemPackagingUOMFactor() != null ? row.getStockItemPackagingUOMFactor()
		        .toPlainString() : null;
		if (isFullfullmentReport()) {
			columns[columnIndex++] = row.getQuantityDispensed() == null ? null : row.getQuantityDispensed()
			        .multiply(negativeOne).toPlainString();
			columns[columnIndex++] = row.getQuantityDispensedStockItemPackagingUOMName();
			columns[columnIndex++] = row.getQuantityDispensedStockItemPackagingUOMFactor() == null ? null : row
			        .getQuantityDispensedStockItemPackagingUOMFactor().toPlainString();
			columns[columnIndex++] = row.getBatchNo();
			columns[columnIndex++] = row.getBatchExpiryDate() != null ? DATE_FORMATTER.format(row.getBatchExpiryDate())
			        : null;
		}
		columns[columnIndex++] = row.getDose() != null ? row.getDose().toPlainString() : null;
		columns[columnIndex++] = row.getDoseUnitsConceptName();
		columns[columnIndex++] = row.getFrequencyPerDay() != null ? row.getFrequencyPerDay().toPlainString() : null;
		columns[columnIndex++] = row.getFrequencyConceptName();
		columns[columnIndex++] = row.getNumRefills() != null ? row.getNumRefills().toString() : null;
		columns[columnIndex++] = row.getDuration() != null ? row.getDuration().toString() : null;
		columns[columnIndex++] = row.getDurationUnitsConceptName();
		columns[columnIndex++] = row.getRouteConceptName();
		columns[columnIndex++] = row.getDispenseAsWritten() == null ? null
		        : (row.getDispenseAsWritten() ? messageSourceService.getMessage("stockmanagement.report.yes")
		                : messageSourceService.getMessage("stockmanagement.report.no"));
		columns[columnIndex++] = row.getAsNeeded() == null ? null : (row.getAsNeeded() ? messageSourceService
		        .getMessage("stockmanagement.report.yes") : messageSourceService.getMessage("stockmanagement.report.no"));
		columns[columnIndex++] = row.getAsNeededCondition();
		columns[columnIndex++] = row.getDosingInstructions();
		columns[columnIndex++] = row.getOrderNumber();
		columns[columnIndex++] = row.getId().toString();
		if (isFullfullmentReport()) {
			columns[columnIndex++] = row.getStockItemTransactionId() == null ? null : row.getStockItemTransactionId()
			        .toString();
		}
		columns[columnIndex++] = row.getStockItemDrugId() == null ? "" : row.getStockItemDrugId().toString();
		columns[columnIndex++] = row.getStockItemConceptId() == null ? "" : row.getStockItemConceptId().toString();
		writeLineToCsv(csvWriter, columns);
	}
	
	protected void writeHeaders(CSVWriter csvWriter) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		String[] columns = new String[31 + (isFullfullmentReport() ? 9 : 0)];
		int columnIndex = 0;
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.location");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.preferreddispensery");
		if (isFullfullmentReport()) {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dispensedfrom");
		}
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.datecreated");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dateActivated");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dateStopped");
		if (isFullfullmentReport()) {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.datedispensed");
		}
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.prescriber");
		if (isFullfullmentReport()) {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dispenser");
		}
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.patient");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.genericname");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.tradename");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.commonname");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.acronym");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.category");
		if (isFullfullmentReport()) {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.quantityprescribed");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.qtyunitprescribed");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.packsizeprescribed");
		} else {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.quantity");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.qtyunit");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.packsize");
		}
		if (isFullfullmentReport()) {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.quantitydispensed");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.qtyunitdispensed");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.packsizedispensed");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.batchno");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.batchexpiry");
		}
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dose");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.doseunits");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.frequencyperday");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.frequencyunits");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.numRefills");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.duration");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.durationunits");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.route");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dispenseaswritten");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.asNeeded");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.asNeededCondition");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dosingInstructions");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.ordernumber");
		if (isFullfullmentReport()) {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.prescriptiontxnid");
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.dispensetxnid");
		} else {
			columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.txnid");
		}
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.drugid");
		columns[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.conceptid");
		writeLineToCsv(csvWriter, columns);
	}
}
