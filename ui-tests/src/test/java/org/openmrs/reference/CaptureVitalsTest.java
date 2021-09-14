/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.PatientCaptureVitalsPage;
import org.openmrs.uitestframework.test.TestData;

public class CaptureVitalsTest extends LocationSensitiveApplicationTestBase {

    private TestData.PatientInfo patient;

    @Before
    public void setup() {
        patient = createTestPatient();
        createTestVisit();
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient);
    }

    @Test
    @Category(BuildTests.class)
    public void captureVitalsTest() {
        ActiveVisitsPage activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);

        ClinicianFacingPatientDashboardPage patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        PatientCaptureVitalsPage patientCaptureVitalsPage = patientDashboardPage.goToPatientCaptureVitalsPage();
        patientCaptureVitalsPage.setHeightField("185");
        patientCaptureVitalsPage.setWeightField("78");
        patientCaptureVitalsPage.setTemperatureField("36.6");
        patientCaptureVitalsPage.setPulseField("120");
        patientCaptureVitalsPage.waitForPage();
        patientCaptureVitalsPage.setRespiratoryField("99");
        patientCaptureVitalsPage.setBloodPressureFields("120", "70");
        patientCaptureVitalsPage.setBloodOxygenSaturationField("50");
        patientCaptureVitalsPage.confirm();
        patientCaptureVitalsPage.save();
    }

    private void createTestVisit() {
        new TestData.TestVisit(patient.uuid, TestData.getAVisitType(), getLocationUuid(homePage)).create();
    }
}
