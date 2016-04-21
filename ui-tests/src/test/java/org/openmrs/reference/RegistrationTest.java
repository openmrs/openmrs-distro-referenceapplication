/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference;

import static org.junit.Assert.assertTrue;

import java.awt.AWTException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.helper.PatientGenerator;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.RegistrationPage;

/**
 * Created by tomasz on 25.06.15.
 * Edited by cards31 on 16.03.16
 */
public class RegistrationTest extends ReferenceApplicationTestBase {
	
	private RegistrationPage registrationPage;
    private TestPatient patient;

    @Before
    public void setUp() throws AWTException, InterruptedException {
    	registrationPage = homePage.openRegisterAPatientApp();
    	patient = PatientGenerator.generateTestPatient();
    }
    
    // Combined tests for RA-71 and RA-711
    @Test
    @Category(BuildTests.class)
    public void registerTest() throws InterruptedException {
    	registrationPage.registerAPatient(patient);
        registrationPage.confirmPatient();
        patient.Uuid = patientIdFromUrl();
    }
    
    // Test for RA-472
    @Test
    @Category(BuildTests.class)
    public void registerUnidentifiedPatient() throws InterruptedException {
        registrationPage.enterUnidentifiedPatient(patient);

        assertTrue(registrationPage.getGenderInConfirmationPage().contains(patient.gender));

        registrationPage.confirmPatient();
        patient.Uuid = patientIdFromUrl();
        assertTrue(registrationPage.containsText("UNKNOWN UNKNOWN"));
    }
    
    @After
    public void tearDown() throws Exception {
        deletePatient(patient.Uuid);
        waitForPatientDeletion(patient.Uuid);
    }
}