package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class RoleFormPage extends Page {
	private static final By ROLE_TITLE = By.cssSelector("#role");
	private static final By SAVE_ROLE = By.cssSelector("input[type='submit']");
	private static final By DESCRIPTION = By.cssSelector("#content > form > table > tbody > tr:nth-child(2) > td > textarea");
	private static String user = "<script>alert(1);</script>";
	private static String description = "some description";

	public RoleFormPage(Page page) {
		super(page);
	}
	
	public void enterRoleTitle(String user, String description) {
		  setText(ROLE_TITLE, user);
		  setText(DESCRIPTION,description);
		  clickOn(SAVE_ROLE);
	}

	@Override
	public String getPageUrl() {

		return "/openmrs/admin/users/role.form";
	}	
}
