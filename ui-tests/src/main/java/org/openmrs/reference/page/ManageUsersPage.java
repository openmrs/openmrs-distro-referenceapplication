/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.AbstractBasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ManageUsersPage extends AbstractBasePage {

	public ManageUsersPage(WebDriver driver) {
		super(driver);
	}
	
	public void enterNewUserGivenName(String name) {
		setTextToFieldNoEnter(By.xpath("//input[@name='person.names[0].givenName']"), name);
	}
	
	public void enterNewUserFamilyName(String name) {
		setTextToFieldNoEnter(By.xpath("//input[@name='person.names[0].familyName']"), name);
	}
	
	public void setNewUserMaleGender() {
		clickOn(By.id("M"));
	}
	
	public void enterNewUserUsername(String name) {
		setTextToFieldNoEnter(By.xpath("//input[@name='username']"), name);
	}
	
	public void enterNewPasswordWithConfirm(String pass) {
		setTextToFieldNoEnter(By.xpath("//input[@name='userFormPassword']"), pass);
		setTextToFieldNoEnter(By.xpath("//input[@name='confirm']"), pass);
	}
	
	public void chooseNewUserRole(String roleId) {
		clickOn(By.id(roleId));
	}
	
	public void saveUser() {
		clickOn(By.id("saveButton"));
	}
	
	public void findPageSearchAndChooseUser(String name) {
		setTextToFieldNoEnter(By.xpath("//input[@name='name']"), name);
		clickOn(By.xpath("//input[@name='action']"));
		clickOn(By.xpath("//td[@style='white-space: nowrap']/a"));
	}
	
	public void clickBecomeUser() {
		clickOn(By.xpath("//input[@value='Become this user']"));
	}
	
	public void clickDeleteUser() {
		clickOn(By.xpath("//input[@value='Delete User']"));
	}
	
	@Override
	public String expectedUrlPath() {
		return URL_ROOT + "/admin/users/users.list";
	}

}
