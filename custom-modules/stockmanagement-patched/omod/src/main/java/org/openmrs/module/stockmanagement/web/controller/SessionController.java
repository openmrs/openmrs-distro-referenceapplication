/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.stockmanagement.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.PrivilegeScope;
import org.openmrs.module.stockmanagement.api.dto.SessionInfo;
import org.openmrs.module.stockmanagement.web.resource.PrivilegeScopeResource;
import org.openmrs.module.webservices.rest.SimpleObject;
import org.openmrs.module.webservices.rest.web.ConversionUtil;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.openmrs.module.webservices.rest.web.representation.CustomRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller that lets a client check the status of their session, and log out. (Authenticating is
 * handled through a filter, and may happen through this or any other resource.
 */
@Controller("${rootrootArtifactId}.SessionController")
@RequestMapping("/rest/" + RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/session")
public class SessionController {
	
	private static final Logger log = LoggerFactory.getLogger(SessionController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object get(WebRequest request) {
		boolean authenticated = Context.isAuthenticated();
		SimpleObject session = new SimpleObject();
		if (authenticated) {
			StockManagementService stockManagementService = Context.getService(StockManagementService.class);
			SessionInfo sessionInfo = stockManagementService.getCurrentUserSessionInfo();
			session.add("privileges", ConversionUtil.convertToRepresentation(sessionInfo.getPrivileges(),
			    Representation.DEFAULT, new PrivilegeScopeResource()));
		}
		return session;
	}
}
