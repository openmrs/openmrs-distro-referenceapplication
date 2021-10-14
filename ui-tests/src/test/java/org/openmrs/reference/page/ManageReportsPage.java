package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageReportsPage extends Page {
	
	private static final By EDIT_REPORT_ICON = By.cssSelector("#container > div > table > tbody > tr:nth-child(13) > td:nth-child(6) > a:nth-child(1)");
	
	public ManageReportsPage(Page page) {
	    super(page);
	}
	
	public EditReportPage clickOnEditReportIcon() {
	    clickOn(EDIT_REPORT_ICON);
	    return new EditReportPage(this);
	}
	
	@Override
	public String getPageUrl() {
	    return "/reporting/reports/manageReports.form";
	}
}
