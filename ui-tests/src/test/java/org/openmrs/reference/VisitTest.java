package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.uitestframework.test.TestBase;


public class VisitTest extends TestBase {

	String patientUuid;
	
	@Before
	public void before() {
		patientUuid = createTestPatient();
	}
	
	@After
    public void tearDown() throws Exception {
		deletePatientUuid(patientUuid);
		dbUnitTearDown();
    }
	
	/**
	 * Obviously, not really a test yet -- just demonstrating the createTestPatient feature.
	 */
	@Test
	public void test() {
		System.out.println("test patient uuid: " + patientUuid);
	}
}
