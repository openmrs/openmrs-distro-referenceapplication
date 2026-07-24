package org.openmrs.module.stockmanagement.api.reporting;

import org.apache.commons.lang.*;
import org.apache.commons.logging.Log;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockItemInventorySearchFilter;
import org.openmrs.module.stockmanagement.api.dto.reporting.Fullfillment;
import org.openmrs.module.stockmanagement.api.dto.reporting.MostLeastMoving;
import org.openmrs.module.stockmanagement.api.model.BatchJob;
import org.openmrs.module.stockmanagement.api.model.BatchJobStatus;
import org.openmrs.module.stockmanagement.api.utils.*;
import org.openmrs.module.stockmanagement.api.utils.csv.CSVWriter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public abstract class ReportGenerator {
	
	private volatile boolean stopExecution = false;
	
	protected Integer pageIndex = 0;
	
	protected Integer recordsProcessed = 0;
	
	protected Integer lastRecordProcessed = 0;
	
	protected Integer lastStockOperationProcessed = 0;
	
	protected Integer executionStep = 0;
	
	protected File resultsFile = null;
	
	protected Properties executionState = null;
	
	protected boolean hasRestoredExecutionState = false;
	
	protected static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	
	protected static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd-MMM-yyyy");
	
	public void stop() {
		stopExecution = true;
	}
	
	public boolean shouldStop() {
		return stopExecution;
	}
	
	public void generateReport(BatchJob batchJob, Function<BatchJob, Boolean> shouldStopExecution) {
		throw new NotImplementedException();
	}
	
	public void setRecordsProcessed(Properties properties, Integer recordsProcessed) {
		setParameter(properties, "RecordsProcessed", "Records Processed", recordsProcessed.toString(),
		    NumberFormatUtil.integerDisplayFormat(recordsProcessed));
	}
	
	public Integer getRecordsProcessed(Properties properties) {
		try {
			String key = "param.RecordsProcessed.value";
			if (properties.containsKey(key)) {
				return Integer.parseInt(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public void setCurrentPageIndex(Properties properties, Integer currentPageIndex) {
		setParameter(properties, "CurrentPageIndex", "Current Page", currentPageIndex.toString(),
		    NumberFormatUtil.integerDisplayFormat(currentPageIndex + 1));
	}
	
	public Integer getCurrentPageIndex(Properties properties) {
		try {
			String key = "param.CurrentPageIndex.value";
			if (properties.containsKey(key)) {
				return Integer.parseInt(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public void setLastRecordProcessed(Properties properties, Integer lastRecordProcessed) {
		setParameter(properties, "LastRecordProcessed", "Last Record Processed", lastRecordProcessed.toString(),
		    NumberFormatUtil.integerDisplayFormat(lastRecordProcessed));
	}
	
	public void setExecutionStep(Properties properties, Integer lastRecordProcessed) {
		setParameter(properties, "ExecutionStep", "Execution Step", executionStep.toString(),
		    NumberFormatUtil.integerDisplayFormat(executionStep));
	}
	
	public Integer getExecutionStep(Properties properties) {
		try {
			String key = "param.ExecutionStep.value";
			if (properties.containsKey(key)) {
				return Integer.parseInt(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public Integer getLastRecordProcessed(Properties properties) {
		try {
			String key = "param.LastRecordProcessed.value";
			if (properties.containsKey(key)) {
				return Integer.parseInt(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public void setLastStockOperationProcessed(Properties properties, Integer lastStockOperationProcessed) {
		setParameter(properties, "LastStockOperationProcessed", "Last Stock Operation Processed",
		    lastStockOperationProcessed.toString(), NumberFormatUtil.integerDisplayFormat(lastStockOperationProcessed));
	}
	
	public Integer getLastStockOperationProcessed(Properties properties) {
		try {
			String key = "param.LastStockOperationProcessed.value";
			if (properties.containsKey(key)) {
				return Integer.parseInt(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public Date getReportParameterDate(Properties properties, ReportParameter reportParameter) {
		try {
			String key = "param." + reportParameter.name() + ".value";
			if (properties.containsKey(key)) {
				return DateUtil.parseDate(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public String getReportParameterString(Properties properties, ReportParameter reportParameter) {
		try {
			String key = "param." + reportParameter.name() + ".value";
			if (properties.containsKey(key)) {
				return properties.getProperty(key);
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public Boolean getReportParameterBoolean(Properties properties, ReportParameter reportParameter) {
		try {
			String key = "param." + reportParameter.name() + ".value";
			if (properties.containsKey(key)) {
				return Boolean.valueOf(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public BigDecimal getReportParameterBigDecimal(Properties properties, ReportParameter reportParameter) {
		try {
			String key = "param." + reportParameter.name() + ".value";
			if (properties.containsKey(key)) {
				return new BigDecimal(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public Date getDate(Properties properties) {
		return getReportParameterDate(properties, ReportParameter.Date);
	}
	
	public Date getStartDate(Properties properties) {
		return getReportParameterDate(properties, ReportParameter.StartDate);
	}
	
	public Date getEndDate(Properties properties) {
		return getReportParameterDate(properties, ReportParameter.EndDate);
	}
	
	public String getStockItemCategory(Properties properties) {
		return getReportParameterString(properties, ReportParameter.StockItemCategory);
	}
	
	public String getLocation(Properties properties) {
		return getReportParameterString(properties, ReportParameter.Location);
	}
	
	public String getStockSource(Properties properties) {
		return getReportParameterString(properties, ReportParameter.StockSource);
	}
	
	public String getStockSourceDestination(Properties properties) {
		return getReportParameterString(properties, ReportParameter.StockSourceDestination);
	}
	
	public Boolean getChildLocations(Properties properties) {
		return getReportParameterBoolean(properties, ReportParameter.ChildLocations);
	}
	
	public BigDecimal getMaxReorderLevelRatio(Properties properties) {
		return getReportParameterBigDecimal(properties, ReportParameter.MaxReorderLevelRatio);
	}
	
	public String getPatient(Properties properties) {
		return getReportParameterString(properties, ReportParameter.Patient);
	}
	
	public String getStockItem(Properties properties) {
		return getReportParameterString(properties, ReportParameter.StockItem);
	}
	
	public StockItemInventorySearchFilter.InventoryGroupBy getInventoryGroupBy(Properties properties) {
		try {
			String key = "param." + ReportParameter.InventoryGroupBy.name() + ".value";
			if (properties.containsKey(key)) {
				return StockItemInventorySearchFilter.InventoryGroupBy.valueOf(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public MostLeastMoving getMostLeastMoving(Properties properties) {
		try {
			String key = "param." + ReportParameter.MostLeastMoving.name() + ".value";
			if (properties.containsKey(key)) {
				return MostLeastMoving.valueOf(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public List<Fullfillment> getFullfillment(Properties properties) {
		try {
			String key = "param." + ReportParameter.Fullfillment.name() + ".value";
			if (properties.containsKey(key)) {

				String value = properties.getProperty(key);
				if(org.apache.commons.lang.StringUtils.isBlank(value)){
					return null;
				}
				String[] values = value.split(",");
				List<Fullfillment> fullfillments=new ArrayList<>();
				for(String token : values){
					fullfillments.add(Fullfillment.valueOf(token));
				}
				return fullfillments;
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public Integer getLimit(Properties properties) {
		try {
			String key = "param." + ReportParameter.Limit.name() + ".value";
			if (properties.containsKey(key)) {
				return Integer.parseInt(properties.getProperty(key));
			}
		}
		catch (Exception exception) {}
		return null;
	}
	
	public void setParameter(Properties properties, String parameterName, String parameterDescription, String value,
	        String valueDescription) {
		properties.setProperty(String.format("param.%s.description", parameterName), parameterDescription);
		properties.setProperty(String.format("param.%s.value", parameterName), value);
		properties.setProperty(String.format("param.%s.value.desc", parameterName), valueDescription);
	}
	
	protected boolean restoreExecutionState(BatchJob batchJob, StockManagementService stockManagementService, Log log) {
		boolean resetExecutionState = false;
		if (batchJob.getExecutionState() != null) {
			try {
				executionState = GlobalProperties.fromString(batchJob.getExecutionState());
				pageIndex = getCurrentPageIndex(executionState);
				recordsProcessed = getRecordsProcessed(executionState);
				lastRecordProcessed = getLastRecordProcessed(executionState);
				lastStockOperationProcessed = getLastStockOperationProcessed(executionState);
				executionStep = getExecutionStep(executionState);
				hasRestoredExecutionState = true;
			}
			catch (IOException e) {
				resetExecutionState = true;
			}
		}
		pageIndex = pageIndex == null ? 0 : pageIndex;
		recordsProcessed = recordsProcessed == null ? 0 : recordsProcessed;
		lastRecordProcessed = lastRecordProcessed == null ? 0 : lastRecordProcessed;
		lastStockOperationProcessed = lastStockOperationProcessed == null ? 0 : lastStockOperationProcessed;
		resultsFile = new File(FileUtil.getBatchJobFolder(), batchJob.getUuid());
		if (resetExecutionState) {
			executionState = null;
			if (resultsFile.exists()) {
				try {
					resultsFile.delete();
					hasRestoredExecutionState = false;
				}
				catch (Exception exception) {
					hasRestoredExecutionState = false;
					stockManagementService.failBatchJob(batchJob.getUuid(), "Failed to delete the existing report file "
					        + resultsFile.toString());
					log.error(exception.getMessage(), exception);
					return false;
				}
			}
		}
		if (executionState == null) {
			executionState = new Properties();
		}
		return true;
	}
	
	protected void updateExecutionState(BatchJob batchJob, Properties properties, Integer currentPageIndex,
	        Integer recordsProcessed, Integer lastRecordProcessed, StockManagementService stockManagementService)
	        throws IOException {
		updateExecutionState(batchJob, properties, currentPageIndex, recordsProcessed, lastRecordProcessed, null,
		    stockManagementService, null);
	}
	
	protected void updateExecutionState(BatchJob batchJob, Properties properties, Integer currentPageIndex,
	        Integer recordsProcessed, Integer lastRecordProcessed, Integer lastStockOperationProcessed,
	        StockManagementService stockManagementService, Integer executionStep) throws IOException {
		
		setCurrentPageIndex(properties, currentPageIndex);
		if (recordsProcessed != null) {
			setRecordsProcessed(properties, recordsProcessed);
		}
		if (lastRecordProcessed != null) {
			setLastRecordProcessed(properties, lastRecordProcessed);
		}
		if (lastStockOperationProcessed != null) {
			setLastStockOperationProcessed(properties, lastStockOperationProcessed);
		}
		if (executionStep != null) {
			setExecutionStep(properties, executionStep);
		}
		pageIndex = currentPageIndex;
		this.recordsProcessed = recordsProcessed;
		this.lastRecordProcessed = lastRecordProcessed;
		this.lastStockOperationProcessed = lastStockOperationProcessed;
		this.executionStep = executionStep;
		
		if (executionState != null) {
			stockManagementService.updateBatchJobExecutionState(batchJob.getUuid(),
			    GlobalProperties.toString(executionState, null));
		}
	}
	
	public void completeBatchJob(BatchJob batchJob, Long outputArtifactSize, String outputArtifactFileExt,
	        Boolean outputArtifactViewable, StockManagementService stockManagementService) throws IOException {
		BatchJob job = stockManagementService.getBatchJobByUuid(batchJob.getUuid());
		job.setStatus(BatchJobStatus.Completed);
		job.setEndTime(new Date());
		job.setOutputArtifactFileExt(outputArtifactFileExt);
		job.setOutputArtifactViewable(outputArtifactViewable);
		job.setOutputArtifactSize(outputArtifactSize);
		stockManagementService.saveBatchJob(job);
	}
	
	protected CSVWriter createCsvWriter(Writer writer) {
		return new CSVWriter(writer);
	}
	
	protected void writeLineToCsv(CSVWriter csvWriter, String... columns) {
		csvWriter.writeNext(columns, false);
	}
	
	protected String emptyIfNull(String value) {
		return value == null ? "" : value;
	}
	
}
