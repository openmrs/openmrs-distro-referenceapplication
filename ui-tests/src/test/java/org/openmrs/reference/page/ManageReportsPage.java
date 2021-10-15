package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageReportsPage extends Page {
	
	private static final By EDIT_REPORT_ICON = By.xpath("//tbody/tr[12]/td[6]/a[1]/img[1]");
	
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
