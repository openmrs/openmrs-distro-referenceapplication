package org.openmrs.reference.page;

import java.util.List;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ManageUserAccountPage extends Page {

    public static String URL = "/adminui/systemadmin/accounts/manageAccounts.page";
    private static final By SEARCH_USER = By.cssSelector("#list-accounts_filter input[type=text]");
    private static final By ADD_NEW_ACCOUNT = By.cssSelector("#content a[href='/openmrs/adminui/systemadmin/accounts/account.page']");
    private static final By FAMILY_NAME = By.cssSelector("#adminui-familyName-field");
    private static final By GIVEN_NAME = By.cssSelector("#adminui-givenName-field");
    private static final By GENDER_FEMALE = By.cssSelector("#adminui-gender-1-field");
    private static final By ADD_USER_ACCOUNT = By.id("adminui-addUserAccount");
    private static final By USER_NAME = By.cssSelector("#adminui-username-field");
    private static final By PRIVILEGE_LEVEL = By.cssSelector("#adminui-privilegeLevel-field");
    private static final By PASSWORD = By.cssSelector("#adminui-password-field");
    private static final By CONFIRM_PASSWORD = By.cssSelector("#adminui-confirmPassword-field");
    private static final By ADD_PROVIDER_ACCOUNT = By.id("adminui-addProviderAccount");
    private static final By IDENTIFIER = By.cssSelector("#adminui-identifier-field");
    private static final By PROVIDER_ROLE = By.cssSelector("#adminui-providerRole-field");  
    private static final By EDIT_USER_ACCOUNT = By.cssSelector("i.icon-pencil.edit-action");
    private static final By ADMINISTER_SYSTEM = By.id("adminui-capabilities-Application: Administers System");
    private static final By CONFIGURE_FORMS = By.id("adminui-capabilities-Application: Configures Forms");
    private static final By CAPTURE_VITALS = By.id("adminui-capabilities-Application: Enters Vitals");
    private static final By RECORD_ALLERGIES = By.id("adminui-capabilities-Application: Records Allergies");
    private static final By CONFIGURE_METADATA = By.id("adminui-capabilities-Application: Configures Metadata");
    private static final By SUPER_USER_PRIVILEGES = By.id("adminui-capabilities-Application: Has Super User Privileges");
    private static final By REGISTER_PATIENT = By.id("adminui-capabilities-Application: Registers Patients");
    private static final By SCHEDULE_APPOINTMENTS = By.id("adminui-capabilities-Application: Schedules And Overbooks Appointments"); 
    private static final By PATIENT_SUMMARY = By.id("adminui-capabilities-Application: Uses Patient Summary");  
    private static final By SAVE_BUTTON = By.id("save-button");

    public ManageUserAccountPage(Page page) {
        super(page);
    }

    @Override
    public String getPageUrl() {
        return URL;
    }

    public void clickOnAddUser() {
        clickOn(ADD_NEW_ACCOUNT);
    }
    
    public void searchUser(String username) {
        setTextToFieldNoEnter(SEARCH_USER, username);
    }
    
    public void enterPersonalDetails(String familyName, String givenName) {
        findElement(FAMILY_NAME).clear();
        findElement(FAMILY_NAME).sendKeys(familyName); 
        findElement(GIVEN_NAME).clear();
        findElement(GIVEN_NAME).sendKeys(givenName);
    }
    
    public void selectGender() {
        clickOn(GENDER_FEMALE);
    }

    public void clickOnAddUserAccount() {
        clickOn(ADD_USER_ACCOUNT);
    }
   
    public void enterUsername(String username) {
        findElement(USER_NAME).clear();
        findElement(USER_NAME).sendKeys(username);
    }
  
    public void setUserPrivilegeLevel(String privilegeLevel) {
        findElement(PRIVILEGE_LEVEL).sendKeys(privilegeLevel);
    } 
  
    public void setUserPassword(String password, String confirm) {
        findElement(PASSWORD).clear();
        findElement(PASSWORD).sendKeys(password);
        findElement(CONFIRM_PASSWORD).clear();
        findElement(CONFIRM_PASSWORD).sendKeys(confirm);
    }
   
    public boolean isConfirmPasswordMatching(List<String> validationErrors) {
        return !validationErrors.contains("Passwords don't match");
    } 
    
    public void selectAdministersSystem() {
        clickOn(ADMINISTER_SYSTEM);
    }
    
    public void selectConfiguresForms() {
        clickOn(CONFIGURE_FORMS);
    }
   
    public void selectEntersVitals() {
        clickOn(CAPTURE_VITALS);
    }
    
    public void selectRecordsAllergies() {
        clickOn(RECORD_ALLERGIES);
    }
    
    public void selectConfiguresMetadata() {
        clickOn(CONFIGURE_METADATA);
    }
    
    public void selectHasSuperUserPrivileges() {
        clickOn(SUPER_USER_PRIVILEGES);
    }
   
    public void selectRegistersPatients() {
        clickOn(REGISTER_PATIENT);
    }
   
    public void selectSchedulesAndOverbooksAppointments() {
        clickOn(SCHEDULE_APPOINTMENTS);
    }
   
    public void selectUsesPatientSummary() {
        clickOn(PATIENT_SUMMARY);
    }
      
    public void addProviderDetails() {
        clickOn(ADD_PROVIDER_ACCOUNT);
    }
    
    public void setUserIdentifier(String identifier) {
        findElement(IDENTIFIER).clear();
        setText(IDENTIFIER, identifier);
    }
    
    public void setUserProviderRole(String role) {
        findElement(PROVIDER_ROLE).sendKeys(role);
    }
     
    public void clickOnEditUserAccount() {
        clickOn(EDIT_USER_ACCOUNT);
    }
    
    public void saveUserAccount() {
        clickOn(SAVE_BUTTON);
    }
    
    public boolean isDataCorrect(List<String> validationErrors) {
        return !validationErrors.contains("Validation errors found Failed to save account details");
    }
}
