package org.openmrs.reference;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.uitestframework.test.TestBase;

/**
 * Created by tomasz on 23.07.15.
 */
public class MergeVisitsTest extends TestBase {

    private HomePage homePage;
    private HeaderPage headerPage;

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void mergeVisitsTest() {
        homePage.goToActiveVisitPatient();

    }
}
