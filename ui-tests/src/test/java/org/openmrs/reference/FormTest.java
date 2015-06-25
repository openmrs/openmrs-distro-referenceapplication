package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.ManageFormsPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import static org.junit.Assert.assertNotNull;


/**
 * Created by nata on 24.06.15.
 */
public class FormTest extends TestBase {
    private HomePage homePage;
    private HeaderPage headerPage;
    private ManageFormsPage manageForm;
    private String id;


    @Before
    public void setUp() throws Exception {
        homePage = new HomePage(driver);
        loginPage.loginAsAdmin();
        assertPage(homePage);
        headerPage = new HeaderPage(driver);
        manageForm = new ManageFormsPage(driver);
    }

    @Test
    public void formTest() throws Exception {
//        Add Form
        homePage.goToManageForm();
        if(!manageForm.addPresent()) {
            manageForm.delete();
        }
        manageForm.add();
        manageForm.addLabel("Eye Report");
        manageForm.addIcon("icon-align-justify");
        id = manageForm.formIdFromUrl();
        manageForm.save();
        headerPage.clickOnHomeIcon();
        homePage.goToActiveVisitPatient();
        assertNotNull("Eye Report", driver.findElement(By.className("action-section")).getText());
//        Edit Form
        headerPage.clickOnHomeIcon();
        homePage.goToManageForm();
        driver.findElement(By.xpath("//i[@onclick=\"location.href='forms/extension.page?formId=" + id +"&extensionId=patientDashboard.overallActions.form." + id + "'\"]")).click();
        manageForm.addLabel("Eye Test");
        manageForm.save();
        headerPage.clickOnHomeIcon();
        homePage.goToActiveVisitPatient();
        assertNotNull("Eye Test", driver.findElement(By.className("action-section")).getText());
//        Delete Form
        headerPage.clickOnHomeIcon();
        homePage.goToManageForm();
        driver.findElement(By.xpath("//i[@onclick=\"location.href='forms/deleteExtension.page?formId="+id+"&extensionId=patientDashboard.overallActions.form."+id+"'\"]")).click();
        assertNotNull("Add", driver.findElement(By.linkText("Add")).getText());

    }
    @After
    public void tearDown() throws Exception {
        headerPage.clickOnHomeIcon();
        headerPage.logOut();
    }
}
