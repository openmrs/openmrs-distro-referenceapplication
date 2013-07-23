package org.openmrs.reference.page;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openmrs.reference.helper.TestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

/**
 * A superclass for "real" pages. Has lots of handy methods for accessing
 * elements, clicking, filling fields. etc.
 */
public abstract class AbstractBasePage implements Page {
    protected TestProperties properties = TestProperties.instance();
    protected WebDriver driver;
    private String serverURL;

    public AbstractBasePage(WebDriver driver) {
        this.driver = driver;
        serverURL = properties.getWebAppUrl();
    }

    @Override
    public void gotoPage(String address) {
        driver.get(serverURL + address);
    }
    
    @Override
    public WebElement findElement(By by) {
    	return driver.findElement(by);
    }

    @Override
    public WebElement findElementById(String id) {
    	return findElement(By.id(id));
    }
    
    @Override
    public String getText(By by) {
        return findElement(by).getText();
    }

    @Override
    public void setTextToField(By textFieldId, String text) {
        setText(findElement(textFieldId), text);
    }

    @Override
    public void setTextToFieldInsideSpan(String spanId, String text) {
        setText(findTextFieldInsideSpan(spanId), text);
    }

    private void setText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
//        element.sendKeys(Keys.RETURN);   It clears out the entered text in the text box
    }

    @Override
    public void clickOn(By by) {
        findElement(by).click();
    }

    @Override
    public void selectFrom(By by, String value){
        Select droplist = new Select(findElement(by));
        droplist.selectByVisibleText(value);
    }

    @Override
    public void hoverOn(By by) {
        Actions builder = new Actions(driver);
        Actions hover = builder.moveToElement(findElement(by));
        hover.perform();
    }

    private WebElement findTextFieldInsideSpan(String spanId) {
        return findElementById(spanId).findElement(By.tagName("input"));
    }

	@Override
    public String title() {
	    return getText(By.tagName("title"));
    }
	
	@Override
	public String urlPath() {
	    try {
	        return new URL(driver.getCurrentUrl()).getPath();
        }
        catch (MalformedURLException e) {
	        return null;
        }
    }

    @Override
    public List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }
	
	/**
	 * Real pages supply their expected URL path.
	 * 
	 * @return The path portion of the url of the page.
	 */
	@Override
    public abstract String expectedUrlPath();
}
