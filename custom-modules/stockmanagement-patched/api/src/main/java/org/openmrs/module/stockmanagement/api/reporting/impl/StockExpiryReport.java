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
import org.openmrs.module.stockmanagement.api.dto.StockItemInventory;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockBatchLineItem;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockExpiryFilter;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockExpiryReport extends ReportGenerator {
	
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
            log.error(e);
            return;
        }

        Date startDate = getStartDate(parameters);
        Date endDate = getEndDate(parameters);
        String locationUuid = getLocation(parameters);
        Boolean childLocations = getChildLocations(parameters);
        String stockItemCategoryUuid = getStockItemCategory(parameters);

        CSVWriter csvWriter = null;
        Writer writer = null;

        //try(Writer writer = Files.newBufferedWriter(new File(file.toString() + "_errors").toPath())) {
        try {
            boolean hasMoreRecords = true;
            StockExpiryFilter filter = new StockExpiryFilter();
            filter.setStartDate(startDate);
            if(endDate != null) {
                filter.setEndDate(DateUtil.endOfDay(endDate));
            }
            List<Integer> partyIds = null;
            if (!StringUtils.isBlank(locationUuid)) {
                Location location = Context.getLocationService().getLocationByUuid(locationUuid);
                if (location == null) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Report location parameter not found");
                    return;
                }
                filter.setAtLocationId(location.getLocationId());
                filter.setChildLocations(childLocations);
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
            }

            if (!StringUtils.isBlank(stockItemCategoryUuid)) {
                Concept concept = Context.getConceptService().getConceptByUuid(stockItemCategoryUuid);
                if (concept == null) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Report stock item category parameter not found");
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
                filter.setStockBatchIdMin(lastRecordProcessed);
                Result<StockBatchLineItem> data = stockManagementService.getExpiringStockBatchList(filter);

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

                if(!data.getData().isEmpty()){
                    Map<Integer, List<StockBatchLineItem>> stockItemGroups = data.getData().stream().collect(Collectors.groupingBy(p -> p.getStockItemId()));
                    int startIndex = 0;
                    StockItemInventorySearchFilter inventorySearchFilter=new StockItemInventorySearchFilter();
                    inventorySearchFilter.setRequireNonExpiredStockBatches(false);
                    inventorySearchFilter.setUnRestrictedPartyIds(partyIds);
                    inventorySearchFilter.setDoSetQuantityUoM(true);
                    inventorySearchFilter.setDoSetPartyNameField(true);
                    inventorySearchFilter.setInventoryGroupBy(StockItemInventorySearchFilter.InventoryGroupBy.LocationStockItemBatchNo);

                    boolean hasMoreUpdatesToDo;
                    do {
                        inventorySearchFilter.setItemGroupFilters(new ArrayList<>());
                        List<Map.Entry<Integer, List<StockBatchLineItem>>>  stockItemGroupsPage =  stockItemGroups.entrySet().stream().skip(startIndex * 100).limit(100).collect(Collectors.toList());
                        if (stockItemGroupsPage.isEmpty()) {
                            break;
                        }

                        for(Map.Entry<Integer, List<StockBatchLineItem>> p : stockItemGroupsPage){
                            StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter = new StockItemInventorySearchFilter.ItemGroupFilter();
                            itemGroupFilter.setStockItemId(p.getKey());
                            itemGroupFilter.setStockBatchIds(p.getValue().stream().map(x -> x.getStockBatchId()).collect(Collectors.toList()));
                            inventorySearchFilter.getItemGroupFilters().add(itemGroupFilter);
                        }

                        Map<Integer, List<StockItemInventory>> inventoryResult = stockManagementService.getStockInventory(inventorySearchFilter, null).getData()
                                .stream().collect(Collectors.groupingBy(p->p.getStockBatchId()));
                        for(Map.Entry<Integer, List<StockBatchLineItem>> p : stockItemGroupsPage){
                            for(StockBatchLineItem stockBatchLineItem : p.getValue()){
                                List<StockItemInventory> stockItemInventory = inventoryResult.get(stockBatchLineItem.getStockBatchId());
                                if(stockItemInventory == null){
                                    continue;
                                }
                                for(StockItemInventory locationBatchInventory : stockItemInventory){
                                    if(locationBatchInventory.getQuantity().compareTo(BigDecimal.ZERO) <= 0){
                                        continue;
                                    }
                                    writeRow(csvWriter,stockBatchLineItem, locationBatchInventory);
                                }
                            }
                        }

                        hasMoreUpdatesToDo = stockItemGroupsPage.size() >= 100;
                        startIndex++;
                    } while (hasMoreUpdatesToDo);

                    csvWriter.flush();
                    recordsProcessed += data.getData().size();
                    pageIndex++;
                    StockBatchLineItem lastRecord = data.getData().get(data.getData().size() - 1);
                    updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, lastRecord.getStockBatchId(), null, stockManagementService, null);
                }
                else if (pageIndex == 0) {
                    updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, null, null, stockManagementService, null);
                }

                hasMoreRecords = data.getData().size() >= pageSize;
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
	
	protected void writeRow(CSVWriter csvWriter, StockBatchLineItem row, StockItemInventory inventory) {
		writeLineToCsv(csvWriter,
		    row.getStockItemDrugName() == null ? row.getStockItemConceptName() : row.getStockItemDrugName(),
		    row.getStockItemDrugName() == null ? "" : row.getStockItemConceptName(), emptyIfNull(row.getCommonName()),
		    emptyIfNull(row.getAcronym()), emptyIfNull(row.getStockItemCategoryName()),
		    emptyIfNull(inventory.getPartyName()), emptyIfNull(row.getBatchNo()), TIMESTAMP_FORMATTER.format(row
		            .getDateCreated()), row.getExpiration() != null ? DATE_FORMATTER.format(row.getExpiration()) : "",
		    inventory.getQuantity().toPlainString(), inventory.getQuantityUoM(), inventory.getQuantityFactor()
		            .toPlainString(), row.getStockItemDrugId() == null ? "" : row.getStockItemDrugId().toString(),
		    row.getStockItemConceptId() == null ? "" : row.getStockItemConceptId().toString());
	}
	
	protected void writeHeaders(CSVWriter csvWriter) {
		MessageSourceService messageSourceService = Context.getMessageSourceService();
		writeLineToCsv(csvWriter, messageSourceService.getMessage("stockmanagement.report.genericname"),
		    messageSourceService.getMessage("stockmanagement.report.tradename"),
		    messageSourceService.getMessage("stockmanagement.report.commonname"),
		    messageSourceService.getMessage("stockmanagement.report.acronym"),
		    messageSourceService.getMessage("stockmanagement.report.category"),
		    messageSourceService.getMessage("stockmanagement.report.location"),
		    messageSourceService.getMessage("stockmanagement.report.batchno"),
		    messageSourceService.getMessage("stockmanagement.report.datecreated"),
		    messageSourceService.getMessage("stockmanagement.report.batchexpiry"),
		    messageSourceService.getMessage("stockmanagement.report.quantity"),
		    messageSourceService.getMessage("stockmanagement.report.qtyunit"),
		    messageSourceService.getMessage("stockmanagement.report.packsize"),
		    messageSourceService.getMessage("stockmanagement.report.drugid"),
		    messageSourceService.getMessage("stockmanagement.report.conceptid"));
	}
}
