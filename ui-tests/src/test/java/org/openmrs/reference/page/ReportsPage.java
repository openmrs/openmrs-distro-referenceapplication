package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;

public class ReportsPage extends Page{

	public ReportsPage(Page page) {
		super(page);
	}

	@Override
	public String getPageUrl() {
		return "/reportingui/reportsapp/home.page";
	}
	
}
