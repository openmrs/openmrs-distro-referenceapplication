package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProviderPage extends AbstractBasePage {

	static final By MANAGE_PROVIDERS = By.linkText("Manage Providers");
	static final By ADD_PROVIDER = By.linkText("Add Provider");
	static final By SAVE_PROVIDER = By.name("saveProviderButton");
	static final By IDENTIFIER = By.name("identifier");
	static final By PERSON = By.id("person_id_selection");
	static final By HOME = By.id("homeNavLink");
	public ProviderPage(WebDriver driver) {
		super(driver);
	}
	

    public void clickWhenVisible(By by) throws InterruptedException {
        Long startTime = System.currentTimeMillis();
        while((System.currentTimeMillis() - startTime) < 5000) {
            try {
                clickOn(by);
                break;
            } catch (Exception e) {
                Thread.sleep(500);
            }
        }

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

	public void fillInField(WebElement field, String text) {
		field.clear();
		field.sendKeys(text);
	}

	public void clickOnHomeLink() {
		clickOn(HOME);
	}
	public void fillInIdentifier(String text) {
		fillInField(findElement(IDENTIFIER),text);
	}

	public void fillInPerson(String text) {
		fillInField(findElement(PERSON),text);
	}
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/admin/provider/index.htm";
	}
	
}
