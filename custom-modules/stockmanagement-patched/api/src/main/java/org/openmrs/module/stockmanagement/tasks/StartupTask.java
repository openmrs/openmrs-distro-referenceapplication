package org.openmrs.module.stockmanagement.tasks;

public interface StartupTask {
	
	void execute();
	
	int getPriority();
}
