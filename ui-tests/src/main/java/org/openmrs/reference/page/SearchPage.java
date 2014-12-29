package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SearchPage extends AbstractBasePage {

	static final By SEARCH = null;
	static final By TABLE = null;
	
	public SearchPage(WebDriver driver) {
		super(driver);
	}

	public void search(String template) {
		
	}
	
	public List<String> getStringsFromColumn(int column) {
		return null;
	}
	
	public boolean verifyTable(int column, String template) throws Exception {
		return true;
	}
	
	@Override
	public String expectedUrlPath() {
		return null;
	}

}
