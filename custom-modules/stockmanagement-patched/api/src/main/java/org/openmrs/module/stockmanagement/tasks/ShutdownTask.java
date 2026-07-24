package org.openmrs.module.stockmanagement.tasks;

public interface ShutdownTask {
	
	void execute();
	
	int getPriority();
}
