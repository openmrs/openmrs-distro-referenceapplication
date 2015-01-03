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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.AddAllergyPage;
import org.openmrs.reference.page.AllergiesDashboardPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

public class AllergiesTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private ActiveVisitsPage activeVisitsPage;
	private AllergiesDashboardPage allergiesDashboardPage;
	private AddAllergyPage addAllergyPage;
	private Random rand;
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        activeVisitsPage = new ActiveVisitsPage(driver);
        allergiesDashboardPage = new AllergiesDashboardPage(driver);
        addAllergyPage = new AddAllergyPage(driver);
        rand = new Random();
	}
	
	@Test
	public void allergyDrugTest() {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	// Go to edit allergies page
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	
    	// Go to add allergy page
    	allergiesDashboardPage.addAllergy(driver.getCurrentUrl());
    	
    	// Allergens
    	int type = 0;	// Type = DRUG
    	addAllergyPage.chooseAllergenType(type); 	// Choose allergen type
    	int allergen = rand.nextInt(addAllergyPage.getAllergensCount(type));	// Choose random allergen
    	String allergenName = addAllergyPage.getAllergenName(type, allergen);
    	addAllergyPage.chooseAllergen(type, allergen);
    	
    	// Reactions
    	int reaction = rand.nextInt(addAllergyPage.getReactionsCount());
    	String reactionName = addAllergyPage.getReactionName(reaction);
    	addAllergyPage.chooseReaction(reaction);
    	
    	// Severity
    	int severityType = rand.nextInt(3);
    	String severityName = addAllergyPage.getSeverityName(severityType);
    	addAllergyPage.chooseSeverity(severityType);
    	
    	// Comment
    	String comment = "Test comment";
    	addAllergyPage.addComment(comment);
		
    	// Save record
    	addAllergyPage.save();
    	
    	// Check new allergy
    	assertEquals(allergiesDashboardPage.getAllergyString(0), allergenName);
    	assertEquals(allergiesDashboardPage.getAllergyString(1), reactionName);
    	assertEquals(allergiesDashboardPage.getAllergyString(2), severityName);
    	assertEquals(allergiesDashboardPage.getAllergyString(3), comment);
    	
    	// Go to patient dashboard and check "ALLERGIES" table
    	allergiesDashboardPage.viewPatientDashboard(driver.getCurrentUrl());
    	
    	assertTrue(patientDashboardPage.getAllergen().contains(allergenName));
    	assertTrue(patientDashboardPage.getAllergyReaction().contains(reactionName));
    	
    	// Delete allergy
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	allergiesDashboardPage.deleteAllergy();
    	
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void allergyFoodTest() {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	// Go to edit allergies page
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	
    	// Go to add allergy page
    	allergiesDashboardPage.addAllergy(driver.getCurrentUrl());
    	
    	// Allergens
    	int type = 1;	// Type = FOOD
    	addAllergyPage.chooseAllergenType(type); 	// Choose allergen type
    	int allergen = rand.nextInt(addAllergyPage.getAllergensCount(type));	// Choose random allergen
    	String allergenName = addAllergyPage.getAllergenName(type, allergen);
    	addAllergyPage.chooseAllergen(type, allergen);
    	
    	// Reactions
    	int reaction = rand.nextInt(addAllergyPage.getReactionsCount());
    	String reactionName = addAllergyPage.getReactionName(reaction);
    	addAllergyPage.chooseReaction(reaction);
    	
    	// Severity
    	int severityType = rand.nextInt(3);
    	String severityName = addAllergyPage.getSeverityName(severityType);
    	addAllergyPage.chooseSeverity(severityType);
    	
    	// Comment
    	String comment = "Test comment";
    	addAllergyPage.addComment(comment);
		
    	// Save record
    	addAllergyPage.save();
    	
    	// Check new allergy
    	assertEquals(allergiesDashboardPage.getAllergyString(0), allergenName);
    	assertEquals(allergiesDashboardPage.getAllergyString(1), reactionName);
    	assertEquals(allergiesDashboardPage.getAllergyString(2), severityName);
    	assertEquals(allergiesDashboardPage.getAllergyString(3), comment);
    	
    	// Go to patient dashboard and check "ALLERGIES" table
    	allergiesDashboardPage.viewPatientDashboard(driver.getCurrentUrl());
    	
    	assertTrue(patientDashboardPage.getAllergen().contains(allergenName));
    	assertTrue(patientDashboardPage.getAllergyReaction().contains(reactionName));
    	
    	// Delete allergy
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	allergiesDashboardPage.deleteAllergy();
    	
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void allergyEnvironmentalTest() {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	// Go to edit allergies page
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	
    	// Go to add allergy page
    	allergiesDashboardPage.addAllergy(driver.getCurrentUrl());
    	
    	// Allergens
    	int type = 2;	// Type = ENVIRONMENTAL
    	addAllergyPage.chooseAllergenType(type); 	// Choose allergen type
    	int allergen = rand.nextInt(addAllergyPage.getAllergensCount(type));	// Choose random allergen
    	String allergenName = addAllergyPage.getAllergenName(type, allergen);
    	addAllergyPage.chooseAllergen(type, allergen);
    	
    	// Reactions
    	int reaction = rand.nextInt(addAllergyPage.getReactionsCount());
    	String reactionName = addAllergyPage.getReactionName(reaction);
    	addAllergyPage.chooseReaction(reaction);
    	
    	// Severity
    	int severityType = rand.nextInt(3);
    	String severityName = addAllergyPage.getSeverityName(severityType);
    	addAllergyPage.chooseSeverity(severityType);
    	
    	// Comment
    	String comment = "Test comment";
    	addAllergyPage.addComment(comment);
		
    	// Save record
    	addAllergyPage.save();
    	
    	// Check new allergy
    	assertEquals(allergiesDashboardPage.getAllergyString(0), allergenName);
    	assertEquals(allergiesDashboardPage.getAllergyString(1), reactionName);
    	assertEquals(allergiesDashboardPage.getAllergyString(2), severityName);
    	assertEquals(allergiesDashboardPage.getAllergyString(3), comment);
    	
    	// Go to patient dashboard and check "ALLERGIES" table
    	allergiesDashboardPage.viewPatientDashboard(driver.getCurrentUrl());
    	
    	assertTrue(patientDashboardPage.getAllergen().contains(allergenName));
    	assertTrue(patientDashboardPage.getAllergyReaction().contains(reactionName));
    	
    	// Delete allergy
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	allergiesDashboardPage.deleteAllergy();
    	
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void allergyMultipleReactionsTest() {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	// Go to edit allergies page
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	
    	// Go to add allergy page
    	allergiesDashboardPage.addAllergy(driver.getCurrentUrl());
    	
    	// Allergens
    	int type = 0;	// Type = DRUG
    	addAllergyPage.chooseAllergenType(type); 	// Choose allergen type
    	int allergen = rand.nextInt(addAllergyPage.getAllergensCount(type));	// Choose random allergen
    	String allergenName = addAllergyPage.getAllergenName(type, allergen);
    	addAllergyPage.chooseAllergen(type, allergen);
    	
    	// Multiple Reactions
    	int reactions = addAllergyPage.getReactionsCount();
    	int multiple = rand.nextInt(3) + 3;	// Reactions count = [3..5]
    	
    	List<Integer> reactionsList = new ArrayList<Integer>();
    	for(int i = 0; i < reactions; i++) {
    		reactionsList.add(i);
    	}
    	
    	List<String> reactionsNames = new ArrayList<String>();
    	for(int i = 0; i < multiple; i++) {
    		int reaction = 0;
    		String reactionName = addAllergyPage.getReactionName(reactionsList.get(reaction));
        	addAllergyPage.chooseReaction(reactionsList.get(reaction));
        	
        	reactionsList.remove(reaction);
        	reactionsNames.add(reactionName);
    	}
    	
    	// Severity
    	int severityType = rand.nextInt(3);
    	String severityName = addAllergyPage.getSeverityName(severityType);
    	addAllergyPage.chooseSeverity(severityType);
    	
    	// Comment
    	String comment = "Test comment";
    	addAllergyPage.addComment(comment);
		
    	// Save record
    	addAllergyPage.save();
    	
    	// Check new allergy
    	assertEquals(allergiesDashboardPage.getAllergyString(0), allergenName);
    	for(int i = 0; i < reactionsNames.size(); i++) {
    		assertTrue(allergiesDashboardPage.getAllergyString(1).contains(reactionsNames.get(i)));
    	}
    	assertEquals(allergiesDashboardPage.getAllergyString(2), severityName);
    	assertEquals(allergiesDashboardPage.getAllergyString(3), comment);
    	
    	// Go to patient dashboard and check "ALLERGIES" table
    	allergiesDashboardPage.viewPatientDashboard(driver.getCurrentUrl());
    	
    	assertTrue(patientDashboardPage.getAllergen().contains(allergenName));
    	for(int i = 0; i < reactionsNames.size(); i++) {
    		assertTrue(patientDashboardPage.getAllergyReaction(i).contains(reactionsNames.get(i)));
    	}
    	
    	// Delete allergy
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	allergiesDashboardPage.deleteAllergy();
    	
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void noAllergiesTest() {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	// Go to edit allergies page
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	
    	// Press "No Known Allergy"
    	allergiesDashboardPage.addNoKnownAllergy();
    	
    	// Go to patient dashboard and check "ALLERGIES" table
    	allergiesDashboardPage.viewPatientDashboard(driver.getCurrentUrl());
    	assertTrue(patientDashboardPage.getAllergiesString().contains("No known allergies"));
    	
    	// Delete "no known allergies"
    	patientDashboardPage.editAllergies(driver.getCurrentUrl());
    	allergiesDashboardPage.deleteNoKnownAllergy();
    	
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
}
