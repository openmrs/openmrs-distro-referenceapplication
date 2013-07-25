package org.openmrs.reference.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;

public class AdvancedSettingsPage extends AbstractBasePage {
	
    public AdvancedSettingsPage(WebDriver driver) {
        super(driver);
    }

    static final String GIVEN_NAME_AUTOSUGGEST_LIST = "//tbody[@id='globalPropsList']/tr[418]/td[2]/textarea";        //Once we get an id, this x-path should be replaced
    static final String FAMILY_NAME_AUTOSUGGEST_LIST = "//tbody[@id='globalPropsList']/tr[417]/td[2]/textarea";       //Once we get an id, this x-path should be replaced
    static final String SAVE_BUTTON = "//div[@id='buttonsAtBottom']//input[@value='Save']";                           //Once we get an id, this x-path should be replaced


    public void setGivenNameAutoSuggestList() {
      Actions action1 = new Actions(driver);
      action1.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0030')).build().perform();
      setTextToField(By.xpath(GIVEN_NAME_AUTOSUGGEST_LIST),"Abraham,Babu,Catherine,Dane,Elisa,Franklin,Gothai,Hema");
    }

    public void setFamilyNameAutoSuggestList() {
      setTextToField(By.xpath(FAMILY_NAME_AUTOSUGGEST_LIST),"Sundaram,Karthik,Chandru,Arvind");
    }

    public void saveChanges() {
      clickOn(By.xpath(SAVE_BUTTON));
    }

    @Override
    public String expectedUrlPath() {
        return "/openmrs/admin/maintenance/globalProps.form";
    }
}
