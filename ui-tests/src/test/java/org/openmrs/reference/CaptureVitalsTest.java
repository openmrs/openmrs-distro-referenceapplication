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

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.CaptureVitalsPage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.PatientDashboardPage;
import org.openmrs.uitestframework.test.TestBase;

public class CaptureVitalsTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	private PatientDashboardPage patientDashboardPage;
	private ActiveVisitsPage activeVisitsPage;
	private CaptureVitalsPage captureVitalsPage;
	private Random rand;
	
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        patientDashboardPage = new PatientDashboardPage(driver);
        activeVisitsPage = new ActiveVisitsPage(driver);
        captureVitalsPage = new CaptureVitalsPage(driver);
        rand = new Random();
	}
	
	@Test
	public void correctVitalsHeight() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	double height = 10.0 + (228.0 - 10.0) * rand.nextDouble();
    	captureVitalsPage.clickVitalUL(0);
    	captureVitalsPage.setHeight(height);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsHeight(), height, 0.00001);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsWeight() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	double weight = 250.0 * rand.nextDouble();
    	captureVitalsPage.clickVitalUL(1);
    	captureVitalsPage.setWeight(weight);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsWeight(), weight, 0.00001);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsTemperature() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	double temperature = 25.0 + (43.0 - 25.0) * rand.nextDouble();
    	captureVitalsPage.clickVitalUL(3);
    	captureVitalsPage.setTemperature(temperature);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsTemperature(), temperature, 0.00001);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsHeartRate() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	int heartRate = (int)(230.0 * rand.nextDouble());
    	captureVitalsPage.clickVitalUL(4);
    	captureVitalsPage.setHeartRate(heartRate);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsHeartRate(), heartRate);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsRespiratoryRate() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	long respiratoryRate = (long)(rand.nextFloat() * Long.MAX_VALUE);	// Note: This value has no maximum
    	captureVitalsPage.clickVitalUL(5);
    	captureVitalsPage.setRespiratoryRate(respiratoryRate);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsRespiratoryRate(), respiratoryRate);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsBpSystolic() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	int bpSystolic = (int)(50.0 + (250.0 - 50.0) * rand.nextDouble());
    	captureVitalsPage.clickVitalUL(6);
    	captureVitalsPage.setBpSystolic(bpSystolic);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsBpSystolic(), bpSystolic);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsBpDiastolic() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	int bpDiastolic = (int)(30.0 + (150.0 - 30.0) * rand.nextDouble());
    	captureVitalsPage.clickVitalUL(6);
    	captureVitalsPage.setBpDiastolic(bpDiastolic);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsBpDiastolic(), bpDiastolic);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
	@Test
	public void correctVitalsOxygenSaturation() throws InterruptedException {
		login();
        assertPage(homePage);
        
		currentPage().gotoPage("/coreapps/activeVisits.page?app=coreapps.activeVisits");
    	assertPage(activeVisitsPage);
		
    	activeVisitsPage.clickFirstPatient();
    	assertPage(patientDashboardPage);
    	
    	patientDashboardPage.captureVitals();
    	// Login
    	
    	int oxygenSaturation = (int)(100.0 * rand.nextDouble());
    	captureVitalsPage.clickVitalUL(7);
    	captureVitalsPage.setOxygenSaturation(oxygenSaturation);
    	
    	captureVitalsPage.save();
    	
    	Thread.sleep(5000);
    	assertEquals(patientDashboardPage.getVitalsOxygenSaturation(), oxygenSaturation, 0.00001);
		
    	// Logout
		homePage.go();
    	assertPage(homePage);
    	
    	headerPage.logOut();
    	assertPage(loginPage);
	}
	
}
