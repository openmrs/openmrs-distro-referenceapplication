package org.openmrs.module.stockmanagement.api.dto;

import java.util.HashSet;

public class SessionInfo {
	
	private HashSet<PrivilegeScope> privileges;
	
	public HashSet<PrivilegeScope> getPrivileges() {
		return privileges;
	}
	
	public void setPrivileges(HashSet<PrivilegeScope> privileges) {
		this.privileges = privileges;
	}
}
