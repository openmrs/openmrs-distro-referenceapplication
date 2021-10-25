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

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openmrs.reference.groups.BuildTests;
import org.openmrs.reference.page.AdministrationPage;
import org.openmrs.reference.page.VisitTypeListPage;
import org.openmrs.reference.page.VisitTypePage;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

public class AddVisitTypeTest extends ReferenceApplicationTestBase {

    private String visitTypeName;
    private String visitTypeDesc;

    @Before
    public void setUp() {
        visitTypeName = RandomStringUtils.randomAlphanumeric(8);
        visitTypeDesc = RandomStringUtils.randomAlphanumeric(16);
    }

    @Test
    @Category(BuildTests.class)
    public void addVisitTypeTest() {
        AdministrationPage administrationPage = homePage.goToAdministration();
        VisitTypeListPage visitTypeListPage = administrationPage.goToVisitTypePage();
        VisitTypePage visitTypePage = visitTypeListPage.addVisitType();
        visitTypePage.save();
        assertThat(visitTypePage.getValidationErrors(), hasItem("Invalid name"));
        visitTypePage.setName(visitTypeName);
        visitTypePage.setDescription(visitTypeDesc);
        visitTypeListPage = visitTypePage.save();
        assertThat(visitTypeListPage.getVisitTypeList(), hasItem(visitTypeName));
        assertThat(visitTypeListPage.getVisitTypeList(), hasItem(visitTypeDesc));
        visitTypePage = visitTypeListPage.goToVisitType(visitTypeName);
        visitTypePage.delete();
    }
}
