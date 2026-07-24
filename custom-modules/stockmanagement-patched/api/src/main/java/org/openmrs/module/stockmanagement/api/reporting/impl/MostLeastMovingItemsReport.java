package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.Result;
import org.openmrs.module.stockmanagement.api.dto.StockInventoryResult;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventory;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;
import org.openmrs.module.stockmanagement.api.dto.reporting.MostLeastMoving;
import org.openmrs.module.stockmanagement.api.model.BatchJob;
import org.openmrs.module.stockmanagement.api.model.Party;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MostLeastMovingItemsReport extends ReportGenerator {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected StockManagementService stockManagementService = null;
	
	protected BatchJob batchJob = null;
	
	CSVWriter csvWriter = null;
	
	Writer writer = null;
	
	boolean hasAppendedHeaders = false;
	
	BigDecimal negativeOne = BigDecimal.valueOf(-1);
	
	@Override
    public void generateReport(BatchJob batchJob, Function<BatchJob, Boolean> shouldStopExecution) {
        this.batchJob = batchJob;
        stockManagementService = Context.getService(StockManagementService.class);

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
            log.error(e);
            return;
        }

        String locationUuid = getLocation(parameters);
        Boolean childLocations = getChildLocations(parameters);
        String stockItemCategoryUuid = getStockItemCategory(parameters);
        StockItemInventorySearchFilter.InventoryGroupBy inventoryGroupBy = getInventoryGroupBy(parameters);
        MostLeastMoving mostLeastMoving = getMostLeastMoving(parameters);
        Integer limit = getLimit(parameters);
        Date startDate = getStartDate(parameters);
        Date endDate = getEndDate(parameters);

        if(mostLeastMoving == null){
            mostLeastMoving = MostLeastMoving.MostMoving;
        }
        if(limit == null){
            limit = 20;
        }

        try {
            StockItemInventorySearchFilter inventorySearchFilter=new StockItemInventorySearchFilter();
            inventorySearchFilter.setRequireNonExpiredStockBatches(false);

            inventorySearchFilter.setStartDate(startDate);
            if (endDate != null) {
                inventorySearchFilter.setEndDate(DateUtil.endOfDay(endDate));
            }

            inventorySearchFilter.setInventoryMode(mostLeastMoving.equals(MostLeastMoving.MostMoving) ? StockItemInventorySearchFilter.InventoryMode.MostMoving : StockItemInventorySearchFilter.InventoryMode.LeastMoving);

            inventorySearchFilter.setInventoryGroupBy(inventoryGroupBy == null ?  StockItemInventorySearchFilter.InventoryGroupBy.StockItemOnly : inventoryGroupBy);
            inventorySearchFilter.setRequireItemGroupFilters(false);
            List<Integer> partyIds = null;
            if (!StringUtils.isBlank(locationUuid)) {
                Location location = Context.getLocationService().getLocationByUuid(locationUuid);
                if (location == null) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Report location parameter not found");
                    return;
                }
                if(childLocations != null && childLocations){
                    partyIds = stockManagementService.getCompletePartyList(location.getLocationId()).stream().map(p->p.getId()).collect(Collectors.toList());
                }else{
                    Party party = stockManagementService.getPartyByLocation(location);
                    if(party == null){
                        stockManagementService.failBatchJob(batchJob.getUuid(), "Report location party parameter not found");
                        return;
                    }
                    partyIds = new ArrayList<>();
                    partyIds.add(party.getId());
                }
                inventorySearchFilter.setUnRestrictedPartyIds(partyIds);
            }

            if (!StringUtils.isBlank(stockItemCategoryUuid)) {
                Concept concept = Context.getConceptService().getConceptByUuid(stockItemCategoryUuid);
                if (concept == null) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Report stock item category parameter not found");
                    return;
                }
                inventorySearchFilter.setStockItemCategoryConceptId(concept.getConceptId());
            }
            inventorySearchFilter.setLimit(limit);
            Result<StockItemInventory> result = mostLeastMoving.equals(MostLeastMoving.MostMoving) ? stockManagementService.getMostMovingStockInventory(inventorySearchFilter) : stockManagementService.getLeastMovingStockInventory(inventorySearchFilter);

            inventorySearchFilter.setDoSetBatchFields(true);
            inventorySearchFilter.setDoSetPartyNameField(true);
            inventorySearchFilter.setDoSetQuantityUoM(true);

            StockInventoryResult stockInventoryResult=new StockInventoryResult();
            stockInventoryResult.setData(new ArrayList<>());
            boolean includeBatchInfo = inventoryGroupBy == StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItemBatchNo;
            boolean includeLocationInfo = inventoryGroupBy == StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItemBatchNo ||
                    inventoryGroupBy == StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItem;
            boolean hasWritenRecords = false;
            boolean hasMoreRecordsToRead = false;
            int writePageIndex = 0;
            do{
                if (shouldStopExecution.apply(batchJob)) {
                    return;
                }
                stockInventoryResult.getData().addAll(result.getData().stream().skip(writePageIndex * 100).limit(100).collect(Collectors.toList()));
                if(stockInventoryResult.getData().isEmpty()){
                    break;
                }else{
                    writeBuffer(batchJob, inventorySearchFilter, stockInventoryResult, includeBatchInfo, includeLocationInfo, shouldStopExecution);
                    hasWritenRecords = true;
                }
                writePageIndex++;
            }while (hasMoreRecordsToRead);

            if(!hasWritenRecords){
                writeBuffer(batchJob, inventorySearchFilter, stockInventoryResult, includeBatchInfo, includeLocationInfo, shouldStopExecution);
            }

            csvWriter.close();
            long fileSizeInBytes = Files.size(resultsFile.toPath());
            completeBatchJob(batchJob, fileSizeInBytes, "csv", fileSizeInBytes <= (1024 * 1024), stockManagementService);
        }
        catch (IOException e) {
            stockManagementService.failBatchJob(batchJob.getUuid(), "Input/Output error: " + e.getMessage());
            log.error(e.getMessage(),e);
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
	
	protected void preWriteBuffer(StockItemInventorySearchFilter inventorySearchFilter,
	        StockInventoryResult stockInventoryResult) {
		if (!stockInventoryResult.getData().isEmpty()) {
			stockManagementService.setStockItemInformation(stockInventoryResult.getData());
			stockManagementService.postProcessInventoryResult(inventorySearchFilter, stockInventoryResult);
		}
	}
	
	private StockInventoryResult writeBuffer(BatchJob batchJob, StockItemInventorySearchFilter inventorySearchFilter,
	        StockInventoryResult stockInventoryResult, boolean includeBatchInfo, boolean includeLocationInfo,
	        Function<BatchJob, Boolean> shouldStopExecution) throws IOException {
		preWriteBuffer(inventorySearchFilter, stockInventoryResult);
		if (shouldStopExecution.apply(batchJob)) {
			return null;
		}
		writeRows(stockInventoryResult.getData(), includeBatchInfo, includeLocationInfo);
		csvWriter.flush();
		recordsProcessed += stockInventoryResult.getData().size();
		stockInventoryResult.getData().clear();
		updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, null, null, stockManagementService, null);
		return stockInventoryResult;
	}
	
	protected void writeRows(List<StockItemInventory> stockItemInventories, boolean includeBatchInfo,
	        boolean includeLocationInfo) throws IOException {
		requireHeaders(includeBatchInfo, includeLocationInfo);
		for (StockItemInventory locationBatchInventory : stockItemInventories) {
			writeRow(csvWriter, locationBatchInventory, includeBatchInfo, includeLocationInfo);
		}
	}
	
	private String toString(BigDecimal bigDecimal) {
		return bigDecimal == null ? "" : bigDecimal.toPlainString();
	}
	
	protected void writeRow(CSVWriter csvWriter, StockItemInventory row, boolean includeBatchInfo,
	        boolean includeLocationInfo) {
		String[] line = new String[13 + (includeBatchInfo ? 2 : 0) + (includeLocationInfo ? 1 : 0)];
		int columnIndex = 0;
		line[columnIndex++] = row.getDrugName() == null ? row.getConceptName() : row.getDrugName();
		line[columnIndex++] = row.getDrugName() == null ? "" : row.getConceptName();
		line[columnIndex++] = emptyIfNull(row.getCommonName());
		line[columnIndex++] = emptyIfNull(row.getAcronym());
		line[columnIndex++] = emptyIfNull(row.getStockItemCategoryName());
		if (includeLocationInfo) {
			line[columnIndex++] = emptyIfNull(row.getPartyName());
		}
		if (includeBatchInfo) {
			line[columnIndex++] = emptyIfNull(row.getBatchNumber());
			line[columnIndex++] = row.getExpiration() != null ? DATE_FORMATTER.format(row.getExpiration()) : "";
		}
		line[columnIndex++] = (row.getQuantity().multiply(negativeOne)).toPlainString();
		
		line[columnIndex++] = row.getQuantityUoM();
		line[columnIndex++] = row.getQuantityFactor().toPlainString();
		line[columnIndex++] = row.getReorderLevel() == null ? "" : row.getReorderLevel().toPlainString();
		line[columnIndex++] = emptyIfNull(row.getReorderLevelUoM());
		line[columnIndex++] = row.getReorderLevelFactor() == null ? "" : row.getReorderLevelFactor().toPlainString();
		line[columnIndex++] = row.getDrugId() == null ? "" : row.getDrugId().toString();
		line[columnIndex++] = row.getConceptId() == null ? "" : row.getConceptId().toString();
		writeLineToCsv(csvWriter, line);
	}
	
	protected void writeHeaders(CSVWriter csvWriter, boolean includeBatchInfo, boolean includeLocationInfo) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		String[] headers = new String[13 + (includeBatchInfo ? 2 : 0) + (includeLocationInfo ? 1 : 0)];
		int columnIndex = 0;
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.genericname");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.tradename");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.commonname");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.acronym");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.category");
		if (includeLocationInfo) {
			headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.location");
		}
		if (includeBatchInfo) {
			headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.batchno");
			headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.batchexpiry");
		}
		
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.quantity");
		
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.qtyunit");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.packsize");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.reorderlevel");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.reorderlevelunit");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.reorderlevelpacksize");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.drugid");
		headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.conceptid");
		writeLineToCsv(csvWriter, headers);
	}
	
	private void requireHeaders(boolean includeBatchInfo, boolean includeLocationInfo) throws IOException {
		if (!hasAppendedHeaders) {
			if (writer == null) {
				writer = Files.newBufferedWriter(resultsFile.toPath(), StandardCharsets.UTF_8, StandardOpenOption.CREATE,
				    StandardOpenOption.APPEND);
				
			}
			if (csvWriter == null) {
				csvWriter = createCsvWriter(writer);
			}
			if (!hasRestoredExecutionState || Files.size(resultsFile.toPath()) < 10) {
				writeHeaders(csvWriter, includeBatchInfo, includeLocationInfo);
			}
			hasAppendedHeaders = true;
		}
	}
	
}
