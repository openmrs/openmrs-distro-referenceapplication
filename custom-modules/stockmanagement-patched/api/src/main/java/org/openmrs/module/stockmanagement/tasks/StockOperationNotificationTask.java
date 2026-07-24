package org.openmrs.module.stockmanagement.tasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockOperationAction;
import org.openmrs.util.PrivilegeConstants;

public class StockOperationNotificationTask implements Runnable {
	
	private Thread thread;
	
	private String operationUuid;
	
	private StockOperationAction.Action action;
	
	private String actionReason;
	
	private Integer actionByUserId;
	
	public StockOperationNotificationTask(String operationUuid, StockOperationAction.Action action, String actionReason,
	    Integer actionByUserId) {
		this.operationUuid = operationUuid;
		this.action = action;
		this.actionReason = actionReason;
		this.actionByUserId = actionByUserId;
	}
	
	@Override
	public void run() {
		try {
			StockManagementService stockManagementService = Context.getService(StockManagementService.class);
			Context.openSession();
			Context.addProxyPrivilege(PrivilegeConstants.GET_USERS);
			Context.addProxyPrivilege(PrivilegeConstants.GET_ROLES);
			Context.addProxyPrivilege(PrivilegeConstants.MANAGE_ALERTS);
			Context.addProxyPrivilege(PrivilegeConstants.GET_LOCATIONS);
			stockManagementService.sendStockOperationNotification(operationUuid, action, actionReason, actionByUserId);
		}
		catch (Exception exception) {
			Log log = LogFactory.getLog(this.getClass());
			log.error(exception);
		}
		finally {
			Context.closeSession();
		}
	}
	
	public void fireAndForget() {
		thread = new Thread(this);
		thread.start();
	}
}
