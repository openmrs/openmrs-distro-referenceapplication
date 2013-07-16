package org.openmrs.reference.page;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.Keys;

public class AdvancedSettingsPage extends AbstractBasePage {
    public AdvancedSettingsPage(WebDriver driver) {
        super(driver);
    }

    private String GivenNameAutoSuggestList = "//tbody[@id='globalPropsList']/tr[418]/td[2]/textarea";        //Once we get an id, this x-path should be replaced
    private String FamilyNameAutoSuggestList = "//tbody[@id='globalPropsList']/tr[417]/td[2]/textarea";       //Once we get an id, this x-path should be replaced
    private String Save_BTN = "//div[@id='buttonsAtBottom']//input[@value='Save']";                           //Once we get an id, this x-path should be replaced


    public void setGivenNameAutoSuggestList(){
      Actions action1 =new Actions(driver);
      action1.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0030')).build().perform();
      setTextToField(By.xpath(GivenNameAutoSuggestList),"Abraham,Babu,Catherine,Dane,Elisa,Franklin,Gothai,Hema");
    }

    public void setFamilyNameAutoSuggestList(){
      setTextToField(By.xpath(FamilyNameAutoSuggestList),"Sundaram,Karthik,Chandru,Arvind");
    }

    public void saveChanges(){
      clickOn(By.xpath(Save_BTN));
    }

    @Override
    public String expectedUrlPath() {
        return "/openmrs/admin/maintenance/globalProps.form";
    }
}
