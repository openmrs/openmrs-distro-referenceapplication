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
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.ManagePersonPage;
import org.openmrs.reference.page.PersonFormPage;
import org.openmrs.uitestframework.test.RestClient;
import org.openmrs.uitestframework.test.TestData;

public class DeletePersonTest extends ReferenceApplicationTestBase {

    private String personUuid;
    private TestData.PersonInfo personInfo;

    @Before
    public void setup() {
        personInfo = TestData.generateRandomPerson();
        personUuid = TestData.createPerson(personInfo);
    }

    @Test
    @Category(BuildTests.class)
    public void deletePersonTest() {
        AdministrationPage administrationPage = homePage.goToAdministration();
        ManagePersonPage managePersonPage = administrationPage.clickOnManagePersons();
        managePersonPage.setPersonName(personInfo.givenName);
        PersonFormPage personFormPage = managePersonPage.clickFirstFoundPerson();
        personFormPage.deletePersonForever();
    }

    @After
    public void teardown() {
        RestClient.delete("person/" + personUuid, true);
    }
}
