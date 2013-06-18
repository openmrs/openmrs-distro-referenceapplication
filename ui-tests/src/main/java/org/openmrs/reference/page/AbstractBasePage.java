package org.openmrs.reference.page;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * A superclass for "real" pages. Has lots of handy methods for accessing
 * elements, clicking, filling fields. etc.
 */
public abstract class AbstractBasePage {
    protected TestProperties properties = new TestProperties();
    protected WebDriver driver;
    private String serverURL;

    public AbstractBasePage(WebDriver driver) {
        this.driver = driver;
        serverURL = properties.getWebAppUrl();
    }

    public void gotoPage(String address) {
        driver.get(serverURL + address);
    }
    
    // Convenience method. TODO Should this be named findElement instead?
    public WebElement getElement(By by) {
    	return driver.findElement(by);
    }

    // Convenience method. TODO Should this be named findElementById instead?
    public WebElement getElementById(String id) {
    	return getElement(By.id(id));
    }
    
    public String getText(By by){
        return getElement(by).getText();
    }

    public void setTextToField(String textFieldId, String text) {
        setText(getElement(By.id(textFieldId)), text);
    }

    public void setTextToFieldInsideSpan(String spanId, String text) {
        setText(findTextFieldInsideSpan(spanId), text);
    }

    private void setText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
        element.sendKeys(Keys.RETURN);
    }

    public void clickOn(By elementId) {
        getElement(elementId).click();
    }

    public void hoverOn(By elementId) {
        Actions builder = new Actions(driver);
        Actions hover = builder.moveToElement(driver.findElement(elementId));
        hover.perform();
    }

    private WebElement findTextFieldInsideSpan(String spanId) {
        return getElementById(spanId).findElement(By.tagName("input"));
    }

	public String title() {
	    return getText(By.tagName("title"));
    }
	
	/**
	 * Real pages supply their title.
	 * 
	 * @return The title of the page.
	 */
	public abstract String expectedTitle();
}
