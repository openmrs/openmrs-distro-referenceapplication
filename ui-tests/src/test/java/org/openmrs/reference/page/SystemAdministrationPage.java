/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class SystemAdministrationPage extends Page {
	
	private static final By ADVANCED_ADMINISTRATION = By.id("referenceapplication-legacyAdmin-app");
	
	private static By STYLES_GUIDE_LINK = By.id("referenceapplication-styleGuide-app");
	
	private static By EDIT_WEEKENDS = By
	        .xpath("//*[@id=\"manage-system-settings\"]/ui-view/table/tbody/tr[24]/td[3]/a[2]/i");
	
	private static By ADMINISTRATION = By
	        .id("coreapps-systemadministration-homepageLink-coreapps-systemadministration-homepageLink-extension");
	
	private static By MANAGE_GLOBALS = By.id("org-openmrs-module-adminui-globalProperties-app");
	
	private static By FIELD_VALUE = By.xpath("//*[@id=\"manage-system-settings\"]/ui-view/form/p[3]/textarea");
	
	private static By Save = By.xpath("//*[@id=\"manage-system-settings\"]/ui-view/form/p[4]/button[1]");
	
	private static By WEEKEND_STATE = By.xpath("//*[@id=\"manage-system-settings\"]/ui-view/table/tbody/tr[24]/td[2]");
	
	public SystemAdministrationPage(Page parent) {
		super(parent);
	}
	
	@Override
	public String getPageUrl() {
		return "coreapps/systemadministration/systemAdministration.page";
	}
	
	public AdministrationPage goToAdvancedAdministration() {
		clickOn(ADVANCED_ADMINISTRATION);
		return new AdministrationPage(this);
	}
	
	public StylesGuidePage clickOnStylesGuideAppLink() {
		clickOn(STYLES_GUIDE_LINK);
		return new StylesGuidePage(this);
	}
	
	public void gotoManageProperties() {
		clickOn(ADMINISTRATION);
		clickOn(MANAGE_GLOBALS);
	}
	
	public String propState() {
		String propertyState = findElement(WEEKEND_STATE).getText();
		return propertyState;
	}
	
	public AdministrationPage activateWeekends() {
		gotoManageProperties();
		if (propState().contains("false")) {
			driver.findElement(EDIT_WEEKENDS).click();
			setText(FIELD_VALUE, "true");
			clickOn(Save);
		}
		return new AdministrationPage(this);
	}
	
	public AdministrationPage deactivateWeekends() {
		gotoManageProperties();
		if (propState().contains("true")) {
			driver.findElement(EDIT_WEEKENDS).click();
			setText(FIELD_VALUE, "false");
			clickOn(Save);
		}
		return new AdministrationPage(this);
	}
}
