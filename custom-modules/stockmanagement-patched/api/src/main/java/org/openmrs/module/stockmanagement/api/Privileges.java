package org.openmrs.module.stockmanagement.api;

import java.util.Arrays;
import java.util.List;

public class Privileges {
	
	/**
	 * App: stockmanagement.dashboard: Able to view stock management application dashboard
	 */
	public static final String APP_STOCKMANAGEMENT_DASHBOARD = "App: stockmanagement.dashboard";
	
	/**
	 * App: stockmanagement.stockItems: Able to view stock items
	 */
	public static final String APP_STOCKMANAGEMENT_STOCKITEMS = "App: stockmanagement.stockItems";
	
	/**
	 * Task: stockmanagement.stockItems.mutate: Able to create and update stock items
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE = "Task: stockmanagement.stockItems.mutate";
	
	/**
	 * Task: stockmanagement.stockItems.dispense.qty: Able to view stock item quantities at
	 * dispensing locations
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY = "Task: stockmanagement.stockItems.dispense.qty";
	
	/**
	 * Task: stockmanagement.stockItems.dispense: Able to dispense stock items
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE = "Task: stockmanagement.stockItems.dispense";
	
	/**
	 * App: stockmanagement.userRoleScopes: Able to view stock management user role scope
	 */
	public static final String APP_STOCKMANAGEMENT_USERROLESCOPES = "App: stockmanagement.userRoleScopes";
	
	/**
	 * Task: stockmanagement.userRoleScopes.mutate: Able to create and update user role scopes
	 */
	public static final String TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE = "Task: stockmanagement.userRoleScopes.mutate";
	
	/**
	 * App: stockmanagement.stockoperations: Able to view stock operations
	 */
	public static final String APP_STOCKMANAGEMENT_STOCKOPERATIONS = "App: stockmanagement.stockoperations";
	
	/**
	 * Task: stockmanagement.stockoperations.mutate: Able to create and update stock operations
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE = "Task: stockmanagement.stockoperations.mutate";
	
	/**
	 * Task: stockmanagement.stockoperations.approve: Able to aprove stock operations
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE = "Task: stockmanagement.stockoperations.approve";
	
	/**
	 * Task: stockmanagement.stockoperations.receiveitems: Able to receive dispatched stock items
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS = "Task: stockmanagement.stockoperations.receiveitems";
	
	/**
	 * App: stockmanagement.stockSources: Able to view stock sources
	 */
	public static final String APP_STOCKMANAGEMENT_STOCKSOURCES = "App: stockmanagement.stockSources";
	
	/**
	 * Task: stockmanagement.stockSources.mutate: Able to create and update stock sources
	 */
	public static final String TASK_STOCKMANAGEMENT_STOCKSOURCES_MUTATE = "Task: stockmanagement.stockSources.mutate";
	
	/**
	 * App: stockmanagement.stockOperationType: Able to view stock operation types
	 */
	public static final String APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE = "App: stockmanagement.stockOperationType";
	
	/**
	 * Task: stockmanagement.party.read: Able to read party information
	 */
	public static final String TASK_STOCKMANAGEMENT_PARTY_READ = "Task: stockmanagement.party.read";
	
	/**
	 * App: stockmanagement.reports: Able to view stock reports
	 */
	public static final String APP_STOCKMANAGEMENT_REPORTS = "App: stockmanagement.reports";
	
	/**
	 * Task: stockmanagement.reports.mutate: Able to create stock reports
	 */
	public static final String TASK_STOCKMANAGEMENT_REPORTS_MUTATE = "Task: stockmanagement.reports.mutate";
	
	public static final List<String> ALL = Arrays.asList(APP_STOCKMANAGEMENT_DASHBOARD, APP_STOCKMANAGEMENT_STOCKITEMS,
	    TASK_STOCKMANAGEMENT_STOCKITEMS_MUTATE, TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE_QTY,
	    TASK_STOCKMANAGEMENT_STOCKITEMS_DISPENSE, APP_STOCKMANAGEMENT_USERROLESCOPES,
	    TASK_STOCKMANAGEMENT_USERROLESCOPES_MUTATE, APP_STOCKMANAGEMENT_STOCKOPERATIONS,
	    TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE, TASK_STOCKMANAGEMENT_STOCKOPERATIONS_APPROVE,
	    TASK_STOCKMANAGEMENT_STOCKOPERATIONS_RECEIVEITEMS, APP_STOCKMANAGEMENT_STOCKSOURCES,
	    TASK_STOCKMANAGEMENT_STOCKSOURCES_MUTATE, APP_STOCKMANAGEMENT_STOCKOPERATIONTYPE, TASK_STOCKMANAGEMENT_PARTY_READ,
	    APP_STOCKMANAGEMENT_REPORTS, TASK_STOCKMANAGEMENT_REPORTS_MUTATE);
}
