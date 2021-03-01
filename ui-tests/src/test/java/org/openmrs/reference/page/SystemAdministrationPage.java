/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class SystemAdministrationPage extends Page {

    private static final By  ADVANCED_ADMINISTRATION = By.id("referenceapplication-legacyAdmin-app");

    private static By STYLES_GUIDE_LINK = By.className("icon-magic");

    public SystemAdministrationPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return "coreapps/systemadministration/systemAdministration.page";
    }

    public AdministrationPage goToAdvancedAdministration(){
        clickOn(ADVANCED_ADMINISTRATION);
        return new AdministrationPage(this);
    }

 


    public void clickOnStylesGuideAppLink() {
        clickOn(STYLES_GUIDE_LINK);
    }


}
