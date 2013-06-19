package org.openmrs.reference.page;

import org.openmrs.reference.helper.TestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

public class AbstractBasePage {
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

    public String getText(By elementId){
        return driver.findElement(elementId).getText();
    }

    public void setTextToField(String textFieldId, String text) {
        setText(driver.findElement(By.id(textFieldId)), text);
    }

    public void setTextToFieldInsideSpan(String spanId, String text) {
        setText(findTextFieldInsideSpan(spanId), text);
    }

    private void setText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
//        element.sendKeys(Keys.RETURN);   It clears out the entered text in the text box
    }

    public void clickOn(By elementId) {
        driver.findElement(elementId).click();
    }

    public void selectCombo(By elementId, String value){
        Select droplist = new Select(driver.findElement(elementId)) ;
        droplist.selectByVisibleText(value);
    }

    public void hoverOn(By elementId) {
        Actions builder = new Actions(driver);
        Actions hover = builder.moveToElement(driver.findElement(elementId));
        hover.perform();
    }

    private WebElement findTextFieldInsideSpan(String spanId) {
        return driver.findElement(By.id(spanId)).findElement(By.tagName("input"));
    }

}
