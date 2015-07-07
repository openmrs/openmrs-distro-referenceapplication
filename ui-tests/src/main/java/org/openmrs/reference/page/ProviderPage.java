package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openmrs.uitestframework.test.TestBase.currentPage;

public class ProviderPage extends AbstractBasePage {

	static final By MANAGE_PROVIDERS = By.linkText("Manage Providers");
	static final By ADD_PROVIDER = By.linkText("Add Provider");
	static final By RETIRE_PROVIDER = By.name("retireProviderButton");
	static final By SAVE_PROVIDER = By.name("saveProviderButton");
	static final By RETIRE_REASON = By.id("retire");
	static final By IDENTIFIER = By.name("identifier");
	static final By PERSON = By.id("providerName");
	static final By HOME = By.id("homeNavLink");
	static final By PROVIDER_ELEMENT = By.className("odd");
	public ProviderPage(WebDriver driver) {
		super(driver);
	}
	

	public void manageProviders() {
		clickOn(MANAGE_PROVIDERS);

	}

	public void addProvider() {
		clickOn(ADD_PROVIDER);
	}

	public void saveProvider() {
		clickOn(SAVE_PROVIDER);
	}

	public void findProvider() {
		currentPage().gotoPage("/admin/provider/provider.form?providerId=7");
	}
	public void retireProvider() {
		clickOn(RETIRE_PROVIDER);
	}

	public void fillInField(WebElement field, String text) {
		field.clear();
		field.sendKeys(text);
	}

	public void clickOnHomeLink() {
		clickOn(HOME);
	}

	public void fillInIdentifier(String text) {
		fillInField(findElement(IDENTIFIER), text);
	}

	public void fillInPerson(String text) {
		fillInField(findElement(PERSON),text);
		//clickOn(By.className("hit"));
	}

	public void fillInRetireReason(String text) {
		fillInField(findElement(RETIRE_REASON), text);
	}
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/admin/provider/index.htm";
	}
	
}
