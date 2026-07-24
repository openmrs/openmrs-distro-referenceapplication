package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.Result;
import org.openmrs.module.stockmanagement.api.dto.reporting.DispensingLineFilter;
import org.openmrs.module.stockmanagement.api.dto.reporting.DispensingLineItem;
import org.openmrs.module.stockmanagement.api.model.BatchJob;
import org.openmrs.module.stockmanagement.api.model.StockItem;
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
import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

public class DispensingLogsReport extends ReportGenerator {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected StockManagementService stockManagementService = null;
	
	protected BatchJob batchJob = null;
	
	private BigDecimal negativeOne = BigDecimal.valueOf(-1);
	
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
			DispensingLineFilter filter = new DispensingLineFilter();
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
				filter.setStockItemId(stockItem.getId());
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
			
			filter.setLimit(pageSize);
			boolean hasAppendedHeaders = false;
			while (hasMoreRecords) {
				if (shouldStopExecution.apply(batchJob)) {
					return;
				}
				filter.setStartIndex(pageIndex);
				filter.setStockItemTransactionMin(lastRecordProcessed);
				Result<DispensingLineItem> data = stockManagementService.findDispensingLineItems(filter);
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
					for (DispensingLineItem row : data.getData()) {
						writeRow(csvWriter, row);
					}
					csvWriter.flush();
					recordsProcessed += data.getData().size();
					pageIndex++;
					DispensingLineItem lastRecord = data.getData().get(data.getData().size() - 1);
					updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed,
					    lastRecord.getStockItemTransactionId(), null, stockManagementService, null);
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
	
	protected void writeRow(CSVWriter csvWriter, DispensingLineItem row) {
		writeLineToCsv(csvWriter, row.getPartyName(), TIMESTAMP_FORMATTER.format(row.getDateCreated()), String.format(
		    "%1$s %2$s", row.getCreatorFamilyName(), row.getCreatorGivenName()), row.getPatientIdentifier(),
		    row.getStockItemDrugName() == null ? row.getStockItemConceptName() : row.getStockItemDrugName(),
		    row.getStockItemDrugName() == null ? "" : row.getStockItemConceptName(), emptyIfNull(row.getCommonName()),
		    emptyIfNull(row.getAcronym()), emptyIfNull(row.getStockItemCategoryName()), emptyIfNull(row.getBatchNo()),
		    row.getExpiration() != null ? DATE_FORMATTER.format(row.getExpiration()) : "",
		    row.getQuantity().multiply(negativeOne).toPlainString(), row.getStockItemPackagingUOMName(), row
		            .getStockItemPackagingUOMFactor().toPlainString(), row.getOrderNumber() == null ? "" : row
		            .getOrderNumber().toString(), row.getStockItemTransactionId().toString(),
		    row.getStockItemDrugId() == null ? "" : row.getStockItemDrugId().toString(),
		    row.getStockItemConceptId() == null ? "" : row.getStockItemConceptId().toString());
	}
	
	protected void writeHeaders(CSVWriter csvWriter) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		writeLineToCsv(csvWriter, messageSourceService.getMessage("stockmanagement.report.location"),
		    messageSourceService.getMessage("stockmanagement.report.datecreated"),
		    messageSourceService.getMessage("stockmanagement.report.dispenser"),
		    messageSourceService.getMessage("stockmanagement.report.patient"),
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
		    messageSourceService.getMessage("stockmanagement.report.ordernumber"),
		    messageSourceService.getMessage("stockmanagement.report.txnid"),
		    messageSourceService.getMessage("stockmanagement.report.drugid"),
		    messageSourceService.getMessage("stockmanagement.report.conceptid"));
	}
}
