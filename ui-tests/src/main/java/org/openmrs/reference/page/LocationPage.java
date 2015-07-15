package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



/**
 * Created by nata on 14.07.15.
 */
public class LocationPage extends AbstractBasePage {

    private static final By MANAGE_LOCATION = By.linkText("Manage Locations");
    private static final By ADD_LOCATION = By.linkText("Add Location");
    private static final By SAVE_LOCATION = By.name("saveLocation");
    private static final By NAME = By.name("name");
    private static final By TAGS = By.name("tags");
    public static String NAME_LOCATION;
    private static By DELETE_LOCATION = By.name("action");
    private static By CHECK_LOCATION = By.xpath("//table[@id='locationTable']/tbody/tr[8]/td/input");
    private static By ADDED_LOCATION ;


    public void clickOnManageLocation(){ clickOn(MANAGE_LOCATION);}
    public void clickOnAddLocation(){ clickOn(ADD_LOCATION);}
    public void clickOnSaveLocation(){ clickOn(SAVE_LOCATION);}
    public void enterName(String name){ findElement (NAME).sendKeys(name);
    NAME_LOCATION = name;
    }
    public void chooseTags (String tags) {findElement(TAGS).getText().contains(tags);}

    public void clickOnTags(){ clickOn(TAGS);}
    public void clickOnCheckLocation(){ clickOn(CHECK_LOCATION);}
    public void clickOnDelete(){ clickOn(DELETE_LOCATION);}
    public void addedLocation(){
        clickOn(ADDED_LOCATION = By.linkText(NAME_LOCATION));
    }
    public boolean locationPresent(){
        try {
            return driver.findElement(ADDED_LOCATION = By.linkText(NAME_LOCATION)) != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public LocationPage(WebDriver driver) {
        super(driver);
    }


    @Override
    public String expectedUrlPath() {return URL_ROOT + "/locations/location.list";}
}
