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

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CreatePersonTest extends ReferenceApplicationTestBase {

    private String personName;
    private String personAge;
    private String personFamilyName;

    @Before
    public void setUp() {
        personName = RandomStringUtils.randomAlphanumeric(8);
        personAge = RandomStringUtils.randomNumeric(2);
        personFamilyName = RandomStringUtils.randomAlphanumeric(8);
    }

    @Test
    @Category(BuildTests.class)
    public void createPersonTest() throws InterruptedException {
        AdministrationPage administrationPage = homePage.goToAdministration();
        ManagePersonPage managePersonPage = administrationPage.clickOnManagePersons();
        AddPersonPage personPage = managePersonPage.createPerson();
        personPage.createPerson();
        assertThat(personPage.getValidationErrors().size(), is(3));
        personPage.setPersonName(personName);
        personPage.setAge(personAge);
        personPage.clickGenderMale();
        PersonFormPage personFormPage = personPage.createPerson();
        personFormPage.setFamilyNameField(personFamilyName);
        personFormPage = personFormPage.savePerson();
        assertThat(personFormPage.getActionMessage(), is("Person saved"));
        personFormPage.deletePersonForever();
    }
}
