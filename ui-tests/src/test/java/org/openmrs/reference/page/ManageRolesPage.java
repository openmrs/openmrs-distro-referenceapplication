package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ManageRolesPage  extends Page {
	private static final By ADD_ROLE = By.cssSelector("a[href='role.form']");
	public ManageRolesPage(Page page) {
		super(page);

	}
	
	public RoleFormPage clickOnAddRole() {
        waiter.until(ExpectedConditions.visibilityOfElementLocated(ADD_ROLE));
        findElement(ADD_ROLE).click();
        return new RoleFormPage(this);
    }

	@Override
	public String getPageUrl() {

		return "/openmrs/admin/users/role.list";
	}
	
	
}
