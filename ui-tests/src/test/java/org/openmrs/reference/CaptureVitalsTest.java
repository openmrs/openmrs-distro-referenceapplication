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

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.ActiveVisitsPage;
import org.openmrs.reference.page.ClinicianFacingPatientDashboardPage;
import org.openmrs.reference.page.PatientCaptureVitalsPage;
import org.openmrs.uitestframework.test.RestClient;
import org.openmrs.uitestframework.test.TestData;


public class CaptureVitalsTest  extends ReferenceApplicationTestBase {

    private static final String VISIT_TYPE_UUID = "7b0f5697-27e3-40c4-8bae-f4049abfb4ed";
    private static final String LOCATION_UUID = "8d6c993e-c2cc-11de-8d13-0010c6dffd0f";

    private ActiveVisitsPage activeVisitsPage;
    private ClinicianFacingPatientDashboardPage patientDashboardPage;
    private PatientCaptureVitalsPage patientCaptureVitalsPage;
    private TestData.PatientInfo patient;


    @Before
    public void setup(){
        patient = createTestPatient();
        createTestVisit();
    }

    @After
    public void tearDown() throws Exception {
        deletePatient(patient.uuid);
    }

    @Test
    @Category(BuildTests.class)
    public void captureVital() throws InterruptedException {

        activeVisitsPage = homePage.goToActiveVisitsSearch();
        activeVisitsPage.search(patient.identifier);

        patientDashboardPage = activeVisitsPage.goToPatientDashboardOfLastActiveVisit();
        patientCaptureVitalsPage = patientDashboardPage.goToPatientCaptureVitalsPage();
        patientCaptureVitalsPage.setHeightField("185");
        patientCaptureVitalsPage.setWeightField("78");
        patientCaptureVitalsPage.setTemperatureField("36.6");
        patientCaptureVitalsPage.setPulseField("120");
        patientCaptureVitalsPage.setRespiratoryField("110");
        patientCaptureVitalsPage.setBloodPressureFields("120", "70");
        patientCaptureVitalsPage.setBloodOxygenSaturationField("50");
        patientCaptureVitalsPage.confirm();
        patientCaptureVitalsPage.save();
    }

    private void createTestVisit(){
        JsonNode visit = RestClient.post("visit", new TestData.TestVisit(patient.uuid, VISIT_TYPE_UUID, LOCATION_UUID));
    }
}
