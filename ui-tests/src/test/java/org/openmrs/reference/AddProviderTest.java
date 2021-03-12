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
import org.junit.Ignore;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.ManageProviderPage;
import org.openmrs.reference.page.ProviderPage;
import org.openmrs.uitestframework.test.RestClient;
import org.openmrs.uitestframework.test.TestData;

public class AddProviderTest extends ReferenceApplicationTestBase {

    private TestData.PersonInfo person;
    private String personUuid;

    @Before
    public void setUp() {
        person = TestData.generateRandomPerson();
        personUuid = TestData.createPerson(this.person);
    }

    @Ignore
    @Category(BuildTests.class)
    public void addProviderTest() throws InterruptedException {
        AdministrationPage administrationPage = homePage.goToAdministration();
        ManageProviderPage manageProviderPage = administrationPage.clickOnManageProviders();
        ProviderPage providerPage = manageProviderPage.clickOnAddProvider();

        providerPage.setIdentifier(personUuid);
        providerPage.setPerson(person.getName());
        manageProviderPage = providerPage.clickOnSave();

        manageProviderPage.setProviderNameOrId(person.getName());
        providerPage = manageProviderPage.clickOnProvider(person.getName());
        providerPage.deleteForever();
    }

    @After
    public void tearDown() throws InterruptedException {
        RestClient.delete("person/" + personUuid, true);
    }
}
