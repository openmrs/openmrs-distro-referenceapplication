package org.openmrs.module.stockmanagement.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.ModuleConstants;
import org.openmrs.module.stockmanagement.tasks.LocationTreeSynchronize;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller("${rootrootArtifactId}.LocationTreeSynchronizeController")
@RequestMapping("/rest/" + RestConstants.VERSION_1 + "/" + ModuleConstants.MODULE_ID + "/locationtreesynchronize")
public class LocationTreeSynchronizeController {
	
	@RequestMapping(method = RequestMethod.POST)
	public void locationTreeSyncronize(HttpServletResponse response) throws IOException {
		boolean authenticated = Context.isAuthenticated();
		if (!authenticated) {
			response.setContentType("text/plain");
			response.getOutputStream().print(
			    Context.getMessageSourceService().getMessage("stockmanagement.stockoperation.authrequired"));
			return;
		}
		
		if (!Context.hasPrivilege(PrivilegeConstants.MANAGE_LOCATIONS)) {
			response.setContentType("text/plain");
			response.getOutputStream().print(Context.getMessageSourceService().getMessage("stockmanagement.notauthorised"));
			return;
		}
		
		LocationTreeSynchronize locationTreeSynchronize = new LocationTreeSynchronize();
		locationTreeSynchronize.execute();
		
		response.setStatus(200);
		response.setContentType("text/plain");
		response.getOutputStream().print("Done");
		return;
	}
}
