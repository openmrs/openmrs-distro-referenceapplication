package org.openmrs.module.stockmanagement.api.reporting.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockInventoryResult;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventory;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockItemInventoryExpiryForecast;
import org.openmrs.module.stockmanagement.api.dto.reporting.StockItemInventoryForecast;
import org.openmrs.module.stockmanagement.api.model.BatchJob;
import org.openmrs.module.stockmanagement.api.model.Party;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.reporting.ReportGenerator;
import org.openmrs.module.stockmanagement.api.utils.DateUtil;
import org.openmrs.module.stockmanagement.api.utils.FileUtil;
import org.openmrs.module.stockmanagement.api.utils.GlobalProperties;
import org.openmrs.module.stockmanagement.api.utils.csv.CSVWriter;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockExpiryForecastReport extends ReportGenerator {
	
	Date startDate = null, endDate = null;
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	protected StockManagementService stockManagementService = null;
	
	protected BatchJob batchJob = null;
	
	private File step1File = null;
	
	private int abortReadingInventory = 0;
	
	private Integer daysToEndOfMonth = null;
	
	private Integer inventoryReadCount = 0;
	
	protected BigDecimal maxReorderLevelRatio = null;
	
	private Date todaysDate;
	
	CSVWriter csvWriter = null;
	
	Writer writer = null;
	
	boolean hasAppendedHeaders = false;
	
	Integer limit = null;
	
	public int getAbortReadingInventory() {
		return abortReadingInventory;
	}
	
	BigDecimal daysInMonth = null;
	
	public void setAbortReadingInventory(int abortReadingInventory) {
		this.abortReadingInventory = abortReadingInventory;
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
            log.error(e);
            return;
        }
        todaysDate = DateUtil.today();
        daysInMonth = GlobalProperties.GetReportingCalculationsNoDaysInMonth();
        startDate = getStartDate(parameters);
        endDate = getEndDate(parameters);
        String locationUuid = getLocation(parameters);
        Boolean childLocations = getChildLocations(parameters);
        String stockItemCategoryUuid = getStockItemCategory(parameters);
        StockItemInventorySearchFilter.InventoryGroupBy inventoryGroupBy = getInventoryGroupBy(parameters);
        String stockItemUuid = getStockItem(parameters);
        limit = getLimit(parameters);
        if(limit == null){
            limit = 1;
        }

        BufferedReader bufferedStagingReader = null;

        try {
            StockItemInventorySearchFilter inventorySearchFilter=new StockItemInventorySearchFilter();
            inventorySearchFilter.setRequireNonExpiredStockBatches(false);
            inventorySearchFilter.setStartDate(startDate);
            inventorySearchFilter.setEndDate(DateUtil.endOfDay(endDate));
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



            if (!StringUtils.isBlank(stockItemUuid)) {
                StockItem stockItem = stockManagementService.getStockItemByUuid(stockItemUuid);
                if (stockItem == null) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Report stock item parameter not found");
                    return;
                }
                inventorySearchFilter.setItemGroupFilters(new ArrayList<>());
                StockItemInventorySearchFilter.ItemGroupFilter itemGroupFilter=new StockItemInventorySearchFilter.ItemGroupFilter();
                itemGroupFilter.setStockItemId(stockItem.getId());
                inventorySearchFilter.getItemGroupFilters().add(itemGroupFilter);
            }

            if (!StringUtils.isBlank(stockItemCategoryUuid)) {
                Concept concept = Context.getConceptService().getConceptByUuid(stockItemCategoryUuid);
                if (concept == null) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Report stock item category parameter not found");
                    return;
                }
                inventorySearchFilter.setStockItemCategoryConceptId(concept.getConceptId());
            }
            inventorySearchFilter.setLimit(pageSize);

            boolean step1Complete = requireStockInventory(inventorySearchFilter,shouldStopExecution, parameters);
            if(abortReadingInventory > 0) {
                if(abortReadingInventory == 1){
                    return;
                }
                else if (!step1Complete) {
                    stockManagementService.failBatchJob(batchJob.getUuid(), "Failed to fetch the stock inventory in step 1");
                    return;
                }
            }

            updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, null, null, stockManagementService, 1);

            inventorySearchFilter.setDoSetBatchFields(true);
            inventorySearchFilter.setDoSetPartyNameField(true);
            inventorySearchFilter.setDoSetQuantityUoM(true);

            bufferedStagingReader = Files.newBufferedReader(step1File.toPath());
            String inventoryLine = null;
            int recordsToSkip = recordsProcessed;
            int bufferRecordCount = 100;
            StockInventoryResult stockInventoryResult=new StockInventoryResult();
            stockInventoryResult.setData(new ArrayList<>());
            boolean includeBatchInfo = true;
            boolean includeLocationInfo = false;
            boolean hasWritenRecords = false;
            List<StockItemInventoryExpiryForecast> currentStockItemGroup=new ArrayList<>();
            while((inventoryLine = bufferedStagingReader.readLine()) != null){
                if (shouldStopExecution.apply(batchJob)) {
                    return;
                }
                if(recordsToSkip > 0) {
                    recordsToSkip--;
                    continue;
                }
                if(inventoryLine.length() < 4){
                    continue;
                }
                String[] lineParts = inventoryLine.split(",", -1);
                StockItemInventoryExpiryForecast stockItemInventory = new StockItemInventoryExpiryForecast();
                if(lineParts[0].length() > 0) {
                    stockItemInventory.setStockItemId(Integer.parseInt(lineParts[0]));
                }
                if(lineParts[1].length() > 0) {
                    stockItemInventory.setStockBatchId(Integer.parseInt(lineParts[1]));
                }
                if(lineParts[2].length() > 0) {
                    stockItemInventory.setExpiration(new Date(Long.parseLong(lineParts[2])));
                }
                int maxLinePartIndex = lineParts.length - 1;
                BigDecimal sumConsumed = BigDecimal.ZERO;
                int countOfSummed = 0;
                for(int linePartIndex = 3; linePartIndex < maxLinePartIndex; linePartIndex++ ){
                    BigDecimal consumed = new BigDecimal(lineParts[linePartIndex]);
                    stockItemInventory.getQuantityConsumed().add(consumed);
                    if(countOfSummed > 0 || consumed.compareTo(BigDecimal.ZERO) != 0){
                        countOfSummed++;
                        sumConsumed = sumConsumed.add(consumed);
                    }
                }
                stockItemInventory.setConsumptionRate(countOfSummed == 0 ? BigDecimal.ZERO : sumConsumed.divide(BigDecimal.valueOf(countOfSummed),5,BigDecimal.ROUND_HALF_EVEN));

                if(lineParts[maxLinePartIndex].length() > 0) {
                    stockItemInventory.setQuantity(new BigDecimal(lineParts[maxLinePartIndex]));
                }

                if(currentStockItemGroup.isEmpty() || currentStockItemGroup.get(0).getStockItemId().equals(stockItemInventory.getStockItemId())){
                    currentStockItemGroup.add(stockItemInventory);
                }
                else{
                    processCurrentStockItemGroup(currentStockItemGroup, stockInventoryResult);
                    currentStockItemGroup.add(stockItemInventory);
                }

                if(stockInventoryResult.getData().size() >= bufferRecordCount){
                    writeBuffer(batchJob, inventorySearchFilter, stockInventoryResult, includeBatchInfo, includeLocationInfo, shouldStopExecution);
                    hasWritenRecords = true;
                }
            }

            if(!currentStockItemGroup.isEmpty()){
                processCurrentStockItemGroup(currentStockItemGroup, stockInventoryResult);
            }

            if(stockInventoryResult.getData().size() > 0){
                writeBuffer(batchJob, inventorySearchFilter, stockInventoryResult, includeBatchInfo, includeLocationInfo, shouldStopExecution);
            }else{
                if(!hasWritenRecords){
                    writeBuffer(batchJob, inventorySearchFilter, stockInventoryResult, includeBatchInfo, includeLocationInfo, shouldStopExecution);
                }else {
                    updateExecutionState(batchJob, executionState, pageIndex, recordsProcessed, null, null, stockManagementService, null);
                }
            }

            csvWriter.close();
            long fileSizeInBytes = Files.size(resultsFile.toPath());
            completeBatchJob(batchJob, fileSizeInBytes, "csv", fileSizeInBytes <= (1024 * 1024), stockManagementService);
            if(step1File != null){
                try{
                    step1File.delete();
                }catch (Exception e){}
            }
        }
        catch (IOException e) {
            stockManagementService.failBatchJob(batchJob.getUuid(), "Input/Output error: " + e.getMessage());
            log.error(e.getMessage(),e);
        }
        finally {
            if(bufferedStagingReader != null){
                try {
                    bufferedStagingReader.close();
                }catch (Exception e){}
            }
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
	
	private void processCurrentStockItemGroup(List<StockItemInventoryExpiryForecast> currentStockItemGroup, StockInventoryResult stockInventoryResult) {
        BigDecimal consumptionRate = BigDecimal.ZERO;
        for(StockItemInventoryExpiryForecast forecast : currentStockItemGroup){
            consumptionRate = consumptionRate.add(forecast.getConsumptionRate());
        }

        LocalDate today = DateUtil.today().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        daysToEndOfMonth = today.lengthOfMonth() - today.getDayOfMonth();
        List<LocalDate> dates = new ArrayList<>();
        if (daysToEndOfMonth > 0) {
            dates.add(today.withDayOfMonth(today.lengthOfMonth()));
        }
        LocalDate date = today.withDayOfMonth(1);
        for (int i = 1; i <= limit; i++) {
            date = date.plusMonths(1);
            dates.add(date.withDayOfMonth(date.lengthOfMonth()));
        }
        currentStockItemGroup.removeIf(p->p.getStockBatchId() != null && p.getExpiration() != null && todaysDate.after(p.getExpiration()));

        LocalDate balanceStartDate = today;
        BigDecimal balanceSpillOver = BigDecimal.ZERO;
        for(StockItemInventoryExpiryForecast forecast : currentStockItemGroup){
            forecast.setStockItemConsumptionRate(consumptionRate);
            forecast.setForecastBalances(new ArrayList<>());
            BigDecimal balance = forecast.getQuantity();
            LocalDate expiration = forecast.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int datesToProcess = dates.size();
            for(LocalDate forecastDate : dates){
                datesToProcess--;
                if(forecastDate.isBefore(balanceStartDate)){
                    forecast.getForecastBalances().add(balance);
                }else{
                    // Check if we are in the balance month
                    //if((forecastDate.getYear() == balanceStartDate.getYear()) && (forecastDate.getMonth() == balanceStartDate.getMonth())){
                    // We are in the balance month, check if this is the month the batch expires
                    if((forecastDate.getYear() == expiration.getYear()) && (forecastDate.getMonth() == expiration.getMonth())){
                        // We are in the month the batch expires, check if todays month is the one in which the batch expires
                        int daysToExpiry = expiration.getDayOfMonth() - balanceStartDate.getDayOfMonth();
                        // Get the estimated usage till end the expiry date
                        BigDecimal dailyUsage = forecast.getStockItemConsumptionRate().divide(BigDecimal.valueOf(forecastDate.lengthOfMonth()), 5, BigDecimal.ROUND_HALF_EVEN);
                        BigDecimal usageForecast = dailyUsage.multiply(BigDecimal.valueOf(daysToExpiry)).add(balanceSpillOver);
                        balanceSpillOver = BigDecimal.ZERO;
                        BigDecimal tempBalance = balance.subtract(usageForecast);
                        if(tempBalance.compareTo(BigDecimal.ZERO) < 0){
                            forecast.setLoss(BigDecimal.ZERO);
                            balanceSpillOver = tempBalance.multiply(BigDecimal.valueOf(-1));
                            BigDecimal daysToAddOnStartDate =  balance.divide(dailyUsage, 0, BigDecimal.ROUND_FLOOR);
                            balanceStartDate = balanceStartDate.plusDays(daysToAddOnStartDate.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) < 0 ? daysToAddOnStartDate.intValue() : Integer.MAX_VALUE);
                            balance = BigDecimal.ZERO;
                        }else{
                            forecast.setLoss(tempBalance);
                            balance =  BigDecimal.ZERO;
                            balanceStartDate = expiration;
                        }
                        forecast.getForecastBalances().add(balance);
                        if(datesToProcess > 0){
                            while (datesToProcess > 0){
                                forecast.getForecastBalances().add(balance);
                                datesToProcess--;
                            }
                        }
                        break;
                    }else{
                        BigDecimal usageForecast = null;
                        // This is not the month the batch expires, check if today's date is the one the forecast date is in.
                        if((forecastDate.getYear() == today.getYear()) && (forecastDate.getMonth() == today.getMonth()) && today.getDayOfMonth() != 1){
                            // Calculate days to end of month and use it as balance.
                            int daysToMonthEnd = forecastDate.getDayOfMonth() - balanceStartDate.getDayOfMonth() + 1;
                            // Get the estimated usage till end of month
                            usageForecast = forecast.getStockItemConsumptionRate().divide(BigDecimal.valueOf(forecastDate.lengthOfMonth()), 5, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(daysToMonthEnd));
                        }else{
                            if(balanceStartDate.getDayOfMonth() == 1){
                                usageForecast = forecast.getStockItemConsumptionRate();
                            }else{
                                int daysToMonthEnd = forecastDate.getDayOfMonth() - balanceStartDate.getDayOfMonth() + 1;
                                usageForecast = forecast.getStockItemConsumptionRate().divide(BigDecimal.valueOf(forecastDate.lengthOfMonth()), 5, BigDecimal.ROUND_HALF_EVEN).multiply(BigDecimal.valueOf(daysToMonthEnd));
                            }
                        }
                        usageForecast = usageForecast.add(balanceSpillOver);
                        balanceSpillOver = BigDecimal.ZERO;
                        BigDecimal tempBalance = balance.subtract(usageForecast);
                        if(tempBalance.compareTo(BigDecimal.ZERO) < 0){
                            balanceSpillOver = tempBalance.multiply(BigDecimal.valueOf(-1));
                            BigDecimal dailyUsage = forecast.getStockItemConsumptionRate().divide(BigDecimal.valueOf(forecastDate.lengthOfMonth()), 5, BigDecimal.ROUND_HALF_EVEN);
                            BigDecimal daysToAddOnStartDate =  balance.divide(dailyUsage, 0, BigDecimal.ROUND_FLOOR);
                            balanceStartDate = balanceStartDate.plusDays(daysToAddOnStartDate.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) < 0 ? daysToAddOnStartDate.intValue() : Integer.MAX_VALUE);
                            balance = BigDecimal.ZERO;
                            forecast.getForecastBalances().add(balance);
                            if(datesToProcess > 0){
                                while (datesToProcess > 0){
                                    forecast.getForecastBalances().add(balance);
                                    datesToProcess--;
                                }
                            }
                            break;
                        }
                        else{
                            balance=tempBalance;
                            forecast.getForecastBalances().add(balance);
                            balanceStartDate = balanceStartDate.withDayOfMonth(1).plusMonths(1);
                        }
                    }
                }
            }
        }
        stockInventoryResult.getData().addAll(currentStockItemGroup);
        currentStockItemGroup.clear();
    }
	
	protected void preWriteBuffer(StockItemInventorySearchFilter inventorySearchFilter,
	        StockInventoryResult stockInventoryResult) {
		if (!stockInventoryResult.getData().isEmpty()) {
			stockManagementService.setStockItemInformation(stockInventoryResult.getData());
			stockManagementService.postProcessInventoryResult(inventorySearchFilter, stockInventoryResult);
			for (StockItemInventory stockItemInventory : stockInventoryResult.getData()) {
				StockItemInventoryExpiryForecast forecast = (StockItemInventoryExpiryForecast) stockItemInventory;
				forecast.setConsumptionRate(forecast.getConsumptionRate().divide(forecast.getQuantityFactor(), 5,
				    BigDecimal.ROUND_HALF_EVEN));
				forecast.setStockItemConsumptionRate(forecast.getStockItemConsumptionRate().divide(
				    forecast.getQuantityFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
				if (forecast.getLoss() != null) {
					forecast.setLoss(forecast.getLoss().divide(forecast.getQuantityFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
				}
				for (int i = 0; i < forecast.getQuantityConsumed().size(); i++) {
					forecast.getQuantityConsumed().set(
					    i,
					    forecast.getQuantityConsumed().get(i)
					            .divide(forecast.getQuantityFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
				}
				for (int i = 0; i < forecast.getForecastBalances().size(); i++) {
					forecast.getForecastBalances().set(
					    i,
					    forecast.getForecastBalances().get(i)
					            .divide(forecast.getQuantityFactor(), 5, BigDecimal.ROUND_HALF_EVEN));
				}
			}
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
			writeRow(csvWriter, (StockItemInventoryExpiryForecast) locationBatchInventory, includeBatchInfo,
			    includeLocationInfo);
		}
	}
	
	private String toString(BigDecimal bigDecimal) {
		return bigDecimal == null ? "" : bigDecimal.toPlainString();
	}
	
	protected void writeRow(CSVWriter csvWriter, StockItemInventoryExpiryForecast row, boolean includeBatchInfo,
	        boolean includeLocationInfo) {
		String[] line = new String[15 + (includeBatchInfo ? 2 : 0) + (includeLocationInfo ? 1 : 0) + limit
		        + (daysToEndOfMonth > 0 ? 1 : 0) + row.getQuantityConsumed().size()];
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
		line[columnIndex++] = row.getQuantity().toPlainString();
		line[columnIndex++] = row.getQuantityUoM();
		line[columnIndex++] = row.getQuantityFactor().toPlainString();
		line[columnIndex++] = row.getReorderLevel() == null ? "" : row.getReorderLevel().toPlainString();
		line[columnIndex++] = emptyIfNull(row.getReorderLevelUoM());
		line[columnIndex++] = row.getReorderLevelFactor() == null ? "" : row.getReorderLevelFactor().toPlainString();
		for (BigDecimal quantityConsumed : row.getQuantityConsumed()) {
			line[columnIndex++] = quantityConsumed.toPlainString();
		}
		line[columnIndex++] = row.getStockItemConsumptionRate().toPlainString();
		for (BigDecimal forecastBalance : row.getForecastBalances()) {
			line[columnIndex++] = forecastBalance.toPlainString();
		}
		line[columnIndex++] = row.getLoss() == null ? "0" : row.getLoss().toPlainString();
		
		line[columnIndex++] = row.getDrugId() == null ? "" : row.getDrugId().toString();
		line[columnIndex++] = row.getConceptId() == null ? "" : row.getConceptId().toString();
		writeLineToCsv(csvWriter, line);
		
	}
	
	protected void writeHeaders(CSVWriter csvWriter, boolean includeBatchInfo, boolean includeLocationInfo) {
        Date today = DateUtil.today();
        LocalDate today1 = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        daysToEndOfMonth = today1.lengthOfMonth() - today1.getDayOfMonth();
        LocalDate startDateLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        startDateLocal = startDateLocal.withDayOfMonth(1);
        endDateLocal = endDateLocal.withDayOfMonth(1);
        List<String> historicalHeaders=new ArrayList<>();
        do{
            historicalHeaders.add(DATE_FORMATTER.format(Date.from(startDateLocal.withDayOfMonth(startDateLocal.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault())
                    .toInstant())));
            startDateLocal = startDateLocal.plusMonths(1);
        }while (!startDateLocal.isAfter(endDateLocal));

        MessageSourceService messageSourceService = Context.getMessageSourceService();
        String[] headers = new String[15 + (includeBatchInfo ? 2 : 0) + (includeLocationInfo ? 1 : 0) + limit
                + (daysToEndOfMonth > 0 ? 1 : 0) + historicalHeaders.size()];
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

        for(String historicalHeader : historicalHeaders){
            headers[columnIndex++] = historicalHeader;
        }

        headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.consumptionrate");

        if (daysToEndOfMonth > 0) {
            headers[columnIndex++] = DATE_FORMATTER.format(Date.from(today1.withDayOfMonth(today1.lengthOfMonth())
                    .atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        Date date = DateUtils.setDays(today, 1);
        for (int i = 1; i <= limit; i++) {
            date = DateUtils.addMonths(date, 1);
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            localDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
            headers[columnIndex++] = DATE_FORMATTER.format(Date.from(localDate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant()));
        }

        headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.loss");
        headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.drugid");
        headers[columnIndex++] = messageSourceService.getMessage("stockmanagement.report.conceptid");
        writeLineToCsv(csvWriter, headers);
    }
	
	protected boolean requireStockInventory(StockItemInventorySearchFilter filter, Function<BatchJob, Boolean> shouldStopExecution, Properties parameters) throws IOException {
        step1File =  new File(FileUtil.getBatchJobFolder(), batchJob.getUuid()+".step1");
        if(hasRestoredExecutionState && executionStep > 0 && step1File.exists()){
            return true;
        }
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = Files.newBufferedWriter(step1File.toPath(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            final BufferedWriter finalBufferedWriter = bufferedWriter;
            setFilters(filter, parameters);
            stockManagementService.getStockInventoryExpiryForecastData(filter, inventory -> {
                if (shouldStopExecution.apply(batchJob)) {
                    setAbortReadingInventory(1);
                    return false;
                }

                try {
                    int length = inventory.length;
                    int objectIndex = 0;
                    for (Object object : inventory) {
                        finalBufferedWriter.write(object == null ? "" : objectIndex == 2 ? Long.toString(((Date) object).getTime()) : object.toString());
                        length--;
                        objectIndex++;
                        if (length > 0) {
                            finalBufferedWriter.write(",");
                        }
                    }
                    finalBufferedWriter.write(System.lineSeparator());
                    incrementInventoryReadCount();
                    if (getInventoryReadCount() == 100) {
                        finalBufferedWriter.flush();
                        resetInventoryReadCount();
                    }
                } catch (IOException e) {
                    setAbortReadingInventory(2);
                    log.error(e.getMessage(), e);
                    return false;
                }
                return true;
            });
            if(abortReadingInventory > 0){
                return false;
            }
        }finally {
            if(bufferedWriter != null){
                try{
                    try {
                        bufferedWriter.flush();
                    }catch (Exception e){}
                    bufferedWriter.close();
                }catch (Exception exception){
                }
            }
        }
        return true;
    }
	
	protected void incrementInventoryReadCount() {
		inventoryReadCount = inventoryReadCount + 1;
	}
	
	protected void resetInventoryReadCount() {
		inventoryReadCount = 0;
	}
	
	protected Integer getInventoryReadCount() {
		return inventoryReadCount;
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
	
	protected void setFilters(StockItemInventorySearchFilter filter, Properties parameters) {
	}
}
