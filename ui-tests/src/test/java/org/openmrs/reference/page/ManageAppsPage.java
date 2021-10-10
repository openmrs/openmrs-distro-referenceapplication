package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageAppsPage extends Page {
	
	private static final  By ADD_APP_DEFINITION_BUTTON = By.cssSelector("#content > button");
	private static final  By APP_ID_FIELD = By.cssSelector("#appId-field");
	private static final By  JSON_FIELD = By.cssSelector("#json-field");
	private static final String appIdField = "my.reportingui.reports";
	private static final By SAVE_BUTTON = By.cssSelector("#save-button");
	private static final String definitionjsonField = "{"
			+ "{\n" + 
			"        \"id\": \"my.reportingui.reports\",\n" + 
			"        \"description\": \"Homepage showing a list of different kinds of reports\",\n" + 
			"        \"order\": 10,\n" + 
			"        \"extensionPoints\": [\n" + 
			"            {\n" + 
			"                \"id\": \"org.openmrs.module.reportingui.reports.overview\",\n" + 
			"                \"description\": \"Links to available Overview Reports\",\n" + 
			"                \"supportedExtensionTypes\": [ \"link\" ]\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"id\": \"org.openmrs.module.reportingui.reports.dataquality\",\n" + 
			"                \"description\": \"Links to available Data Quality Reports\",\n" + 
			"                \"supportedExtensionTypes\": [ \"link\" ]\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"id\": \"org.openmrs.module.reportingui.reports.dataexport\",\n" + 
			"                \"description\": \"Links to available Data Exports\",\n" + 
			"                \"supportedExtensionTypes\": [ \"link\" ]\n" + 
			"            }\n" + 
			"        ],\n" + 
			"        \"extensions\": [\n" + 
			"            {\n" + 
			"                \"id\": \"reportingui.reports.homepagelink\",\n" + 
			"                \"extensionPointId\": \"org.openmrs.referenceapplication.homepageLink\",\n" + 
			"                \"type\": \"link\",\n" + 
			"                \"label\": \"reportingui.reportsapp.home.title\",\n" + 
			"                \"url\": \"reportingui/reportsapp/home.page\",\n" + 
			"                \"icon\": \"icon-list-alt\",\n" + 
			"                \"order\": 5,\n" + 
			"                \"requiredPrivilege\": \"App: reportingui.reports\"\n" + 
			"            },\n" + 
			"        {\n" + 
			"                \"id\": \"reportingui.dataExports.adHoc\",\n" + 
			"                \"extensionPointId\": \"org.openmrs.module.reportingui.reports.dataexport\",\n" + 
			"                \"type\": \"link\",\n" + 
			"                \"label\": \"reportingui.runReport.label\",\n" + 
			"                \"url\": \"reportingui/runReport.page\",\n" + 
			"                \"order\": 9999,\n" + 
			"                \"requiredPrivilege\": \"App: reportingui.runReports\",\n" + 
			"                \"featureToggle\": \"reportingui_runReports\"\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"id\": \"reportingui.dataExports.adHoc\",\n" + 
			"                \"extensionPointId\": \"org.openmrs.module.reportingui.reports.dataexport\",\n" + 
			"                \"type\": \"link\",\n" + 
			"                \"label\": \"reportingui.adHocAnalysis.label\",\n" + 
			"                \"url\": \"reportingui/adHocManage.page\",\n" + 
			"                \"order\": 9999,\n" + 
			"                \"requiredPrivilege\": \"App: reportingui.adHocAnalysis\",\n" + 
			"                \"featureToggle\": \"reportingui_adHocAnalysis\"\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"id\": \"Hospitalization Diagnosis Report\",\n" + 
			"                \"extensionPointId\": \"org.openmrs.module.reportingui.reports.overview\",\n" + 
			"                \"type\": \"link\",\n" + 
			"                \"label\": \"Hospitalization Diagnosis Report\",\n" + 
			"                \"url\": \"reportingui/adHocManage.page\",\n" + 
			"                \"order\": 9999,\n" + 
			"                \"requiredPrivilege\": \"App: reportingui.reports\"\n" + 
			"            }\n" + 
			"        ],\n" + 
			"        \"requiredPrivilege\": \"App: reportingui.reports\"\n" + 
			"    }"
			+ "}";
	
	public ManageAppsPage(Page page) {
		super(page);
	}
	
	public void clickOnAddAppDefinition() {
		clickOn(ADD_APP_DEFINITION_BUTTON);
	}
	
	public void enterAppIdField(String appIdField) {
		setTextToFieldNoEnter(APP_ID_FIELD , appIdField);
	}
	
	public void enterDefinitionJsonField(String definitionjsonField) {
		setTextToFieldNoEnter(JSON_FIELD , definitionjsonField);
	}
	
	public void clickOnSaveButton() {
		clickOn(SAVE_BUTTON);
	}

	@Override
	public String getPageUrl() {
		return "/referenceapplication/manageApps.page";
	}
	
}
