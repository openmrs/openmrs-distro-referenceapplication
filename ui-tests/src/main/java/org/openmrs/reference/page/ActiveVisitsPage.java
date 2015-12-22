package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Created by tomasz on 09.07.15.
 */
public class ActiveVisitsPage extends Page {

    private static final By PATIENT_SEARCH = By.id("patient-search");
    private static final By FIRST_FOUND_PATIENT = By.cssSelector("i.icon-vitals");
    public static String URL_PATH = "/coreapps/activeVisits.page";
    private By SEARCH = By.tagName("input");
    private By FIRST_ACTIVE_VISIT = By.className("odd");

    public ActiveVisitsPage(WebDriver driver) {
        super(driver);
    }


    @Override
    public String expectedUrlPath() {
        return URL_ROOT + URL_PATH;
    }

    public String getPatientName() {
        return findElement(FIRST_ACTIVE_VISIT).findElements(By.xpath("//td/a")).get(0).getText().trim();
    }

    public String getPatientId() {
        return findElement(FIRST_ACTIVE_VISIT).findElements(By.xpath("//td")).get(0).getText().trim();
    }

    public String getPatientLastSeen() {
        return findElement(FIRST_ACTIVE_VISIT).findElements(By.xpath("//td[3]")).get(0).getText().trim();
    }

    public void search(String text) {
        WebElement searchField = findElement(SEARCH);
        try {
            searchField.clear();
            searchField.sendKeys(text);
        } catch(Exception e) {
        }
    }

}