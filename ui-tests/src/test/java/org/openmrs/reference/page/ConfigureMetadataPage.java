/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.reference.page;

import org.openmrs.uitestframework.page.Page;
import org.openqa.selenium.By;

public class ConfigureMetadataPage extends Page {

    protected static final String PAGE_URL = "adminui/metadata/configureMetadata.page";
    private static final By MANAGE_LOCATION_ATTRIBUTE_TYPE_LINK = By.id("org-openmrs-module-adminui-manageLocationAttributeTypes-link-org-openmrs-module-adminui-manageLocationAttributeTypes-link-extension");
    private static final By MANAGE_LOCATIONS_LINK = By.id("org-openmrs-module-adminui-manageLocations-link-org-openmrs-module-adminui-manageLocations-link-extension");
    private static final By MANAGE_LOCATION_TAGS_LINK = By.id("org-openmrs-module-adminui-manageLocationTags-link-org-openmrs-module-adminui-manageLocationTags-link-extension");
    private static final By MANAGE_OCL_LINK = By.id("owa-openconceptlab-adminLink-owa-openconceptlab-adminLink-extension");

    public ConfigureMetadataPage(Page parent) {
        super(parent);
    }

    @Override
    public String getPageUrl() {
        return PAGE_URL;
    }

    public ManageLocationsPage goToManageLocations() {
        clickOn(MANAGE_LOCATIONS_LINK);
        return new ManageLocationsPage(this);
    }
    
    public ManageLocationTagsPage goToManageLocationTagPage() {
        clickOn(MANAGE_LOCATION_TAGS_LINK);
        return new ManageLocationTagsPage(this);
    }
    
    public ManageLocationAttributeTypesPage goToManageLocationAttributeTypesPage() {
        clickOn(MANAGE_LOCATION_ATTRIBUTE_TYPE_LINK);
        return new ManageLocationAttributeTypesPage(this);
    }   

    public OpenConceptLabPage goToOpenConceptLabPage() {
        clickOn(MANAGE_OCL_LINK);
        return new OpenConceptLabPage(this);
    }
}
