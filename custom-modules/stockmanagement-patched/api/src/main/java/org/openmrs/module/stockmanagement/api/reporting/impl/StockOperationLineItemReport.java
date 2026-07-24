package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.Result;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockOperationLineItem;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockOperationLineItemFilter;
import org.openmrs.module.stockmanagement.api.model.*;
import org.openmrs.module.stockmanagement.api.reporting.ReportGenerator;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;
import org.openmrs.module.stockmanagement.api.utils.GlobalProperties;
import org.openmrs.module.stockmanagement.api.utils.csv.CSVWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.function.Function;

public abstract class StockOperationLineItemReport extends ReportGenerator {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected StockManagementService stockManagementService = null;
	
	protected BatchJob batchJob = null;
	
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
		String stockSourceUuid = getStockSource(parameters);
		String stockItemCategoryUuid = getStockItemCategory(parameters);
		String stockSourceDestinationUuid = getStockSourceDestination(parameters);
		
		CSVWriter csvWriter = null;
		Writer writer = null;
		
		//try(Writer writer = Files.newBufferedWriter(new File(file.toString() + "_errors").toPath())) {
		try {
			boolean hasMoreRecords = true;
			StockOperationLineItemFilter filter = new StockOperationLineItemFilter();
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
				setLocationFilter(filter, location, childLocations);
			}
			
			if (!StringUtils.isBlank(stockSourceUuid)) {
				StockSource stockSource = stockManagementService.getStockSourceByUuid(stockSourceUuid);
				if (stockSource == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(), "Report source parameter not found");
					return;
				}
				Party party = stockManagementService.getPartyByStockSource(stockSource);
				if (party == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(),
					    "Party related to report source parameter not found");
					return;
				}
				filter.setSourcePartyId(party.getId());
			}
			
			if (!StringUtils.isBlank(stockSourceDestinationUuid)) {
				StockSource stockSourceDestination = stockManagementService.getStockSourceByUuid(stockSourceDestinationUuid);
				if (stockSourceDestination == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(), "Report destination parameter not found");
					return;
				}
				Party party = stockManagementService.getPartyByStockSource(stockSourceDestination);
				if (party == null) {
					stockManagementService.failBatchJob(batchJob.getUuid(),
					    "Party related to report destination parameter not found");
					return;
				}
				filter.setDestinationPartyId(party.getId());
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
			
			filter.setStockOperationStatuses(Arrays.asList(StockOperationStatus.COMPLETED));
			filter.setLimit(pageSize);
			setFilters(filter, parameters);
			
			boolean hasAppendedHeaders = false;
			while (hasMoreRecords) {
				if (shouldStopExecution.apply(batchJob)) {
					return;
				}
				filter.setStartIndex(pageIndex);
				filter.setStockOperationIdMin(lastStockOperationProcessed);
				filter.setStockOperationItemIdMin(lastRecordProcessed);
				Result<StockOperationLineItem> data = stockManagementService.findStockOperationLineItems(filter);
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
					for (StockOperationLineItem row : data.getData()) {
						writeRow(csvWriter, row);
					}
					csvWriter.flush();
					recordsProcessed += data.getData().size();
					pageIndex++;
					StockOperationLineItem lastRecord = data.getData().get(data.getData().size() - 1);
					updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed,
					    lastRecord.getStockOperationItemId(), lastRecord.getStockOperationId(), stockManagementService, null);
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
	
	protected abstract void setLocationFilter(StockOperationLineItemFilter filter, Location location, Boolean childLocations);
	
	protected abstract void setFilters(StockOperationLineItemFilter filter, Properties parameters);
	
	protected abstract void writeRow(CSVWriter csvWriter, StockOperationLineItem row);
	
	protected abstract void writeHeaders(CSVWriter csvWriter);
}
