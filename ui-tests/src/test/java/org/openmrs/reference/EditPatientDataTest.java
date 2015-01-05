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
package org.openmrs.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.EditPatientContactInfoPage;
import org.openmrs.reference.page.EditPatientDemographicsPage;
import org.openmrs.reference.page.FindPatientRecordPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

public class EditPatientDataTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private FindPatientRecordPage findPatientRecordPage;
	private EditPatientDemographicsPage editPatientDemographicsPage;
	private EditPatientContactInfoPage editPatientContactInfoPage;
	private Random rand;
	
	private static final String[] USER_NAMES = { "Alexandre", "Achint", "Brittany", "Burke", "Cesar", "Cosmin",
        "Daniel", "Darius", "David", "Ellen", "Émerson", "Evan", "Fernando", "Gabou", "Glauber", "Hamish", "Louise",
        "Mário", "Mark", "Natália", "Neil", "Neissi", "Nelice", "Rafal", "Renee", "Wyclif" };
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        findPatientRecordPage = new FindPatientRecordPage(driver);
        editPatientDemographicsPage = new EditPatientDemographicsPage(driver);
        editPatientContactInfoPage = new EditPatientContactInfoPage(driver);
        rand = new Random();
	}
	
	@Test
	public void editNameTest() {
		login();
		assertPage(homePage);
		
		// Go to find patient page
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
		assertPage(findPatientRecordPage);
		
		// Go to first patient
		findPatientRecordPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		String oldGiven = patientDashboardPage.getGivenName();
		String oldFamily = patientDashboardPage.getFamilyName();
		
		// Go to edit patient demographics page
		patientDashboardPage.editDemographics();
		assertPage(editPatientDemographicsPage);
		
		// Edit name
		editPatientDemographicsPage.chooseDemographicsOption(0);
		String newGiven = USER_NAMES[rand.nextInt(USER_NAMES.length)];
		String newFamily = USER_NAMES[rand.nextInt(USER_NAMES.length)];
		editPatientDemographicsPage.setGivenName(newGiven);
		editPatientDemographicsPage.setFamilyName(newFamily);
		editPatientDemographicsPage.confirm();
		
		// Check new name
		assertPage(patientDashboardPage);
		assertEquals(patientDashboardPage.getGivenName(), newGiven);
		assertEquals(patientDashboardPage.getFamilyName(), newFamily);
		
		// Go to edit patient demographics page
		patientDashboardPage.editDemographics();
		assertPage(editPatientDemographicsPage);
		
		// Return old name
		editPatientDemographicsPage.chooseDemographicsOption(0);
		editPatientDemographicsPage.setGivenName(oldGiven);
		editPatientDemographicsPage.setFamilyName(oldFamily);
		editPatientDemographicsPage.confirm();
		
		// Check old name
		assertPage(patientDashboardPage);
		assertEquals(patientDashboardPage.getGivenName(), oldGiven);
		assertEquals(patientDashboardPage.getFamilyName(), oldFamily);
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void editGenderTest() {
		login();
		assertPage(homePage);
		
		// Go to find patient page
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
		assertPage(findPatientRecordPage);
		
		// Go to first patient
		findPatientRecordPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		String oldGender = patientDashboardPage.getGender();
		
		// Go to edit patient demographics page
		patientDashboardPage.editDemographics();
		assertPage(editPatientDemographicsPage);
		
		// Edit gender
		editPatientDemographicsPage.chooseDemographicsOption(1);
		if(oldGender.contains("Female")) {
			editPatientDemographicsPage.setGender(1);
		} else {
			editPatientDemographicsPage.setGender(2);
		}
		editPatientDemographicsPage.confirm();
		
		// Check new gender
		assertPage(patientDashboardPage);
		assertFalse(patientDashboardPage.getGender().contains(oldGender));
		
		// Go to edit patient demographics page
		patientDashboardPage.editDemographics();
		assertPage(editPatientDemographicsPage);
		
		// Return old gender
		editPatientDemographicsPage.chooseDemographicsOption(1);
		if(oldGender.contains("Female")) {
			editPatientDemographicsPage.setGender(2);
		} else {
			editPatientDemographicsPage.setGender(1);
		}
		editPatientDemographicsPage.confirm();
		
		// Check old gender
		assertPage(patientDashboardPage);
		assertTrue(patientDashboardPage.getGender().contains(oldGender));
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void editBirthdateTest() {
		login();
		assertPage(homePage);
		
		// Go to find patient page
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
		assertPage(findPatientRecordPage);
		
		// Go to first patient
		findPatientRecordPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		// Go to edit patient demographics page
		patientDashboardPage.editDemographics();
		assertPage(editPatientDemographicsPage);
		
		// Edit birthdate
		editPatientDemographicsPage.chooseDemographicsOption(2);
		
		String oldBirthdateMonthString = editPatientDemographicsPage.getBirthdateMonthString();
		oldBirthdateMonthString = oldBirthdateMonthString.substring(0, 3);
		
		int oldBirthdateYear = editPatientDemographicsPage.getBirthdateYear();
		int oldBirthdateMonth = editPatientDemographicsPage.getBirthdateMonth();
		int oldBirthdateDay = editPatientDemographicsPage.getBirthdateDay();
		int newBirthdateYear = rand.nextInt(30) + 1984;	// Years [1984..2013]
		int newBirthdateMonth = rand.nextInt(12) + 1;
		int newBirthdateDay = rand.nextInt(28) + 1;

		editPatientDemographicsPage.setBirthdateYear(newBirthdateYear);
		editPatientDemographicsPage.setBirthdateMonth(newBirthdateMonth);
		editPatientDemographicsPage.setBirthdateDay(newBirthdateDay);
		
		String newBirthdateMonthString = editPatientDemographicsPage.getBirthdateMonthString();
		newBirthdateMonthString = newBirthdateMonthString.substring(0, 3);
		
		editPatientDemographicsPage.confirm();
		
		// Check new birthdate
		assertPage(patientDashboardPage);
		String[] data = patientDashboardPage.getBirthdateData();
		assertEquals(Integer.parseInt(data[0]), newBirthdateDay);
		assertEquals(data[1], newBirthdateMonthString);
		assertEquals(Integer.parseInt(data[2]), newBirthdateYear);
		
		// Go to edit patient demographics page
		patientDashboardPage.editDemographics();
		assertPage(editPatientDemographicsPage);
		
		// Return old birthdate
		editPatientDemographicsPage.chooseDemographicsOption(2);
		
		editPatientDemographicsPage.setBirthdateYear(oldBirthdateYear);
		editPatientDemographicsPage.setBirthdateMonth(oldBirthdateMonth);
		editPatientDemographicsPage.setBirthdateDay(oldBirthdateDay);
		
		editPatientDemographicsPage.confirm();
		
		// Check old birthdate year
		assertPage(patientDashboardPage);
		data = patientDashboardPage.getBirthdateData();
		assertEquals(Integer.parseInt(data[0]), oldBirthdateDay);
		assertEquals(data[1], oldBirthdateMonthString);
		assertEquals(Integer.parseInt(data[2]), oldBirthdateYear);
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void editAddressTest() {
		login();
		assertPage(homePage);
		
		// Go to find patient page
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
		assertPage(findPatientRecordPage);
		
		// Go to first patient
		findPatientRecordPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		// Go to edit patient contact info page
		patientDashboardPage.showContactInfo();
		patientDashboardPage.editContactInfo();
		assertPage(editPatientContactInfoPage);
		
		// Edit address
		editPatientContactInfoPage.chooseContactInfoOption(0);
		
		String oldAddress = editPatientContactInfoPage.getAddress();
		String oldAddressSecond = editPatientContactInfoPage.getAddressSecond();
		String oldCityVillage = editPatientContactInfoPage.getCityVillage();
		String oldStateProvince = editPatientContactInfoPage.getStateProvince();
		String oldCountry = editPatientContactInfoPage.getCountry();
		String oldPostalCode = editPatientContactInfoPage.getPostalCode();
		
		String newAddress = "SomeAddress";
		String newAddressSecond = "SomeSecondAddress";
		String newCityVillage = "SomeCity/Village";
		String newStateProvince = "SomeState/Province";
		String newCountry = "SomeCountry";
		String newPostalCode = "123123";
		
		editPatientContactInfoPage.setAddress(newAddress);
		editPatientContactInfoPage.setAddressSecond(newAddressSecond);
		editPatientContactInfoPage.setCityVillage(newCityVillage);
		editPatientContactInfoPage.setStateProvince(newStateProvince);
		editPatientContactInfoPage.setCountry(newCountry);
		editPatientContactInfoPage.setPostalCode(newPostalCode);
		
		editPatientContactInfoPage.confirm();
		
		// Check new address
		assertPage(patientDashboardPage);
		patientDashboardPage.showContactInfo();
		String[] data = patientDashboardPage.getAddressData();
		assertEquals(data[0], newAddress);
		assertEquals(data[1], newAddressSecond);
		assertEquals(data[2], newCityVillage);
		assertEquals(data[3], newStateProvince);
		assertEquals(data[4], newCountry);
		assertEquals(data[5], newPostalCode);
		
		// Go to edit patient contact info page
		patientDashboardPage.editContactInfo();
		assertPage(editPatientContactInfoPage);
		
		// Return old address
		editPatientContactInfoPage.chooseContactInfoOption(0);
		
		editPatientContactInfoPage.setAddress(oldAddress);
		editPatientContactInfoPage.setAddressSecond(oldAddressSecond);
		editPatientContactInfoPage.setCityVillage(oldCityVillage);
		editPatientContactInfoPage.setStateProvince(oldStateProvince);
		editPatientContactInfoPage.setCountry(oldCountry);
		editPatientContactInfoPage.setPostalCode(oldPostalCode);
		
		editPatientContactInfoPage.confirm();
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void editPhoneNumberTest() {
		login();
		assertPage(homePage);
		
		// Go to find patient page
		currentPage().gotoPage("/coreapps/findpatient/findPatient.page?app=coreapps.findPatient");
		assertPage(findPatientRecordPage);
		
		// Go to first patient
		findPatientRecordPage.clickFirstPatient();
		assertPage(patientDashboardPage);
		
		// Go to edit patient contact info page
		patientDashboardPage.showContactInfo();
		patientDashboardPage.editContactInfo();
		assertPage(editPatientContactInfoPage);
		
		// Edit phone number
		editPatientContactInfoPage.chooseContactInfoOption(1);
		
		String oldPhoneNumber = editPatientContactInfoPage.getPhoneNumber();
		
		// Generate random phone number
		String newPhoneNumber = "";
		for(int i = 0; i < 10; i++) {
			newPhoneNumber += (char)(rand.nextInt(10) + '0');
		}
		
		editPatientContactInfoPage.setPhoneNumber(newPhoneNumber);
		
		editPatientContactInfoPage.confirm();
		
		// Check new phone number
		assertPage(patientDashboardPage);
		patientDashboardPage.showContactInfo();
		assertEquals(patientDashboardPage.getPhoneNumber(), newPhoneNumber);
		
		// Go to edit patient contact info page
		patientDashboardPage.editContactInfo();
		assertPage(editPatientContactInfoPage);
		
		// Return old phone number
		editPatientContactInfoPage.chooseContactInfoOption(1);
		editPatientContactInfoPage.setPhoneNumber(oldPhoneNumber);
		editPatientContactInfoPage.confirm();
		
		// Check old phone number
		assertPage(patientDashboardPage);
		patientDashboardPage.showContactInfo();
		assertEquals(patientDashboardPage.getPhoneNumber(), oldPhoneNumber);
		
		// Logout
		homePage.go();
		assertPage(homePage);
		headerPage.logOut();
		assertPage(loginPage);
	}
}
