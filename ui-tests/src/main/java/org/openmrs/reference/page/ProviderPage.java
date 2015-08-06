package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openmrs.uitestframework.test.TestBase.currentPage;

public class ProviderPage extends AdminManagementPage {

	static final By IDENTIFIER = By.name("identifier");
	static final By PERSON = By.id("providerName");
	static final By SEARCH_ELEMENT = By.id("inputNode");
	static final By ERROR = By.cssSelector("span.error");
	public ProviderPage(WebDriver driver) {
		super(driver);
		MANAGE = By.linkText("Manage Providers");
		ADD = By.linkText("Add Provider");
		RETIRE = By.name("retireProviderButton");
		SAVE = By.name("saveProviderButton");
	}
	



	public void fillInIdentifier(String text) {
		fillInField(findElement(IDENTIFIER), text);
	}

	public void fillInPerson(String text) {
		fillInField(findElement(PERSON),text);
	}

	public void waitForError(){
		waitForElement(ERROR);
	}

	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/admin/provider/index.htm";
	}
	
}
