/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.reference.helper.TestPatient;
import org.openmrs.reference.page.AddressTemplatePage;
import org.openmrs.reference.page.HeaderPage;
import org.openmrs.reference.page.HomePage;
import org.openmrs.reference.page.RegistrationPage;
import org.openmrs.uitestframework.test.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class AddressTemplateTest extends TestBase {
	private HeaderPage headerPage;
	private HomePage homePage;
	private RegistrationPage registrationPage;
	private AddressTemplatePage addressTemplatePage;
	private String oldCode;
	
	@Before
	public void before() {
		headerPage = new HeaderPage(driver);
        homePage = new HomePage(driver);
        registrationPage = new RegistrationPage(driver);
        addressTemplatePage = new AddressTemplatePage(driver);
        
        // Login
        login();
        assertPage(homePage);
        
        // Go to address template page
        currentPage().gotoPage("/admin/locations/addressTemplate.form");
        assertPage(addressTemplatePage);
        
        // Get old code
        oldCode = addressTemplatePage.getXmlCode();
	}
	
	@After
	public void after() {
		// Return old code
		addressTemplatePage.go();
		addressTemplatePage.setXmlCode(oldCode);
		
		// Logout
		homePage.go();
		assertPage(homePage);
		
		headerPage.logOut();
		assertPage(loginPage);
	}
	
	@Test
	public void countyDistrictTest() throws Exception {
		// Replace xml code
		Element root = replaceXmlCode("<property name=\"countyDistrict\" value=\"Location.district\"/>",
				"<property name=\"countyDistrict\" value=\"24\"/>", "<string>countyDistrict</string>");
		
		// Set xml code
		addressTemplatePage.setXmlCode(nodeToString(root));
		
		// Go to patient contact info
		fillPatientInfo();
		
		// Check country district
		assertTrue(driver.findElements(By.id("countyDistrict")).size() != 0);
		WebElement element = driver.findElement(By.id("countyDistrict"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "countyDistrict");
		assertEquals(element.getAttribute("size"), "24");
	}
	
	@Test
	public void longitudeTest() throws Exception {
		// Replace xml code
		Element root = replaceXmlCode("<property name=\"longitude\" value=\"Location.longitude\"/>",
				"<property name=\"longitude\" value=\"10\"/>", "<string>longitude</string>");
		
		// Set xml code
		addressTemplatePage.setXmlCode(nodeToString(root));
		
		// Go to patient contact info
		fillPatientInfo();
		
		// Check longitude
		assertTrue(driver.findElements(By.id("longitude")).size() != 0);
		WebElement element = driver.findElement(By.id("longitude"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "longitude");
		assertEquals(element.getAttribute("size"), "10");
		assertEquals(element.getAttribute("class"), "number numeric-range");
		assertEquals(element.getAttribute("min"), "-180");
		assertEquals(element.getAttribute("max"), "180");
	}
	
	@Test
	public void latitudeTest() throws Exception {
		// Replace xml code
		Element root = replaceXmlCode("<property name=\"latitude\" value=\"Location.latitude\"/>",
				"<property name=\"latitude\" value=\"10\"/>", "<string>latitude</string>");
		
		// Set xml code
		addressTemplatePage.setXmlCode(nodeToString(root));
		
		// Go to patient contact info
		fillPatientInfo();
		
		// Check latitude
		assertTrue(driver.findElements(By.id("latitude")).size() != 0);
		WebElement element = driver.findElement(By.id("latitude"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "latitude");
		assertEquals(element.getAttribute("size"), "10");
		assertEquals(element.getAttribute("class"), "number numeric-range");
		assertEquals(element.getAttribute("min"), "-90");
		assertEquals(element.getAttribute("max"), "90");
	}
	
	@Test
	public void startDateTest() throws Exception {
		// Replace xml code
		Element root = replaceXmlCode("<property name=\"startDate\" value=\"PersonAddress.startDate\"/>",
				"<property name=\"startDate\" value=\"10\"/>", "<string>startDate</string>");
		
		// Set xml code
		addressTemplatePage.setXmlCode(nodeToString(root));
		
		// Go to patient contact info
		fillPatientInfo();
		
		// Check start date
		assertTrue(driver.findElements(By.id("startDate")).size() != 0);
		assertTrue(driver.findElements(By.id("startDate-wrapper")).size() != 0);
		assertTrue(driver.findElements(By.id("startDate-display")).size() != 0);
		assertTrue(driver.findElements(By.id("startDate-field")).size() != 0);
		
		WebElement element = driver.findElement(By.id("startDate-wrapper"));
		assertEquals(element.getTagName(), "span");
		assertEquals(element.getAttribute("class"), "date");
		
		element = driver.findElement(By.id("startDate-display"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("size"), "10");
	}
	
	@Test
	public void endDateTest() throws Exception {
		// Replace xml code
		Element root = replaceXmlCode("<property name=\"endDate\" value=\"personAddress.endDate\"/>",
				"<property name=\"endDate\" value=\"10\"/>", "<string>endDate</string>");
		
		// Set xml code
		addressTemplatePage.setXmlCode(nodeToString(root));
		
		// Go to patient contact info
		fillPatientInfo();
		
		// Check end date
		assertTrue(driver.findElements(By.id("endDate")).size() != 0);
		assertTrue(driver.findElements(By.id("endDate-wrapper")).size() != 0);
		assertTrue(driver.findElements(By.id("endDate-display")).size() != 0);
		assertTrue(driver.findElements(By.id("endDate-field")).size() != 0);
		
		WebElement element = driver.findElement(By.id("endDate-wrapper"));
		assertEquals(element.getTagName(), "span");
		assertEquals(element.getAttribute("class"), "date");
		
		element = driver.findElement(By.id("endDate-display"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("size"), "10");
	}
	
	@Test
	public void additionalAddressesTest() throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(addressTemplatePage.getXmlCode())));
		Element root = doc.getDocumentElement();
		
		// Replace xml code
		root = replaceXmlCodeRoot(db, doc, root,
				"<property name=\"address3\" value=\"Location.neighborhood\"/>",
				"<property name=\"address3\" value=\"24\"/>",
				"<string>address3</string>");
		
		root = replaceXmlCodeRoot(db, doc, root,
				"<property name=\"address4\" value=\"Location.division\"/>",
				"<property name=\"address4\" value=\"24\"/>",
				"<string>address4</string>");
		
		root = replaceXmlCodeRoot(db, doc, root,
				"<property name=\"address5\" value=\"Location.sublocation\"/>",
				"<property name=\"address5\" value=\"24\"/>",
				"<string>address5</string>");
		
		root = replaceXmlCodeRoot(db, doc, root,
				"<property name=\"address6\" value=\"Location.location\"/>",
				"<property name=\"address6\" value=\"24\"/>",
				"<string>address6</string>");
		
		// Set xml code
		addressTemplatePage.setXmlCode(nodeToString(root));
		
		// Go to patient contact info
		fillPatientInfo();
		
		// Check addresses
		WebElement element;
		
		assertTrue(driver.findElements(By.id("address3")).size() != 0);
		assertEquals(driver.findElements(By.name("address3")).get(0).getText(), "Neighborhood");
		element = driver.findElement(By.id("address3"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "address3");
		assertEquals(element.getAttribute("size"), "24");
		
		assertTrue(driver.findElements(By.id("address4")).size() != 0);
		assertEquals(driver.findElements(By.name("address4")).get(0).getText(), "Division");
		element = driver.findElement(By.id("address4"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "address4");
		assertEquals(element.getAttribute("size"), "24");
		
		assertTrue(driver.findElements(By.id("address5")).size() != 0);
		assertEquals(driver.findElements(By.name("address5")).get(0).getText(), "Sublocation");
		element = driver.findElement(By.id("address5"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "address5");
		assertEquals(element.getAttribute("size"), "24");
		
		assertTrue(driver.findElements(By.id("address6")).size() != 0);
		assertEquals(driver.findElements(By.name("address6")).get(0).getText(), "Location");
		element = driver.findElement(By.id("address6"));
		assertEquals(element.getTagName(), "input");
		assertEquals(element.getAttribute("type"), "text");
		assertEquals(element.getAttribute("name"), "address6");
		assertEquals(element.getAttribute("size"), "24");
	}
	
	private static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		return sw.toString();
	}
	
	private Element replaceXmlCode(String nameMapping, String sizeMapping, String lineByLineFormat) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new InputSource(new StringReader(addressTemplatePage.getXmlCode())));
		Element root = doc.getDocumentElement();
		return replaceXmlCodeRoot(db, doc, root, nameMapping, sizeMapping, lineByLineFormat);
	}
	
	private Element replaceXmlCodeRoot(DocumentBuilder db, Document doc, Element root,
			String nameMapping, String sizeMapping, String lineByLineFormat) throws Exception {
		Node fragmentNode;
		
		// nameMappings
		Element nameMappings = (Element) root.getElementsByTagName("nameMappings").item(0);
		Element oldNameMappings = (Element) root.getElementsByTagName("nameMappings").item(0);
		
		fragmentNode = db.parse(
		        new InputSource(new StringReader(nameMapping)))
		        .getDocumentElement();
		fragmentNode = doc.importNode(fragmentNode, true);
		nameMappings.appendChild(fragmentNode);
		
		root.replaceChild(nameMappings, oldNameMappings);
		
		// sizeMappings
		Element sizeMappings = (Element) root.getElementsByTagName("sizeMappings").item(0);
		Element oldSizeMappings = (Element) root.getElementsByTagName("sizeMappings").item(0);
				
		fragmentNode = db.parse(
		        new InputSource(new StringReader(sizeMapping)))
		        .getDocumentElement();
		fragmentNode = doc.importNode(fragmentNode, true);
		sizeMappings.appendChild(fragmentNode);
				
		root.replaceChild(sizeMappings, oldSizeMappings);
		
		// lineByLineFormat
		Element lineByLine = (Element) root.getElementsByTagName("lineByLineFormat").item(0);
		Element oldLineByLine = (Element) root.getElementsByTagName("lineByLineFormat").item(0);
		
		fragmentNode = db.parse(
		        new InputSource(new StringReader(lineByLineFormat)))
		        .getDocumentElement();
		fragmentNode = doc.importNode(fragmentNode, true);
		lineByLine.appendChild(fragmentNode);
		
		root.replaceChild(lineByLine, oldLineByLine);
		
		return root;
	}
	
	private void fillPatientInfo() {
		TestPatient patient = new TestPatient();
		patient.birthDay = "1";
		patient.birthMonth = "January";
		patient.birthYear = "1980";
		
		registrationPage.go();
		registrationPage.enterPatientGivenName("TestGivenName");
		registrationPage.enterPatientFamilyName("TestFamilyName");
		registrationPage.clickOnGenderLink();
		registrationPage.selectPatientGender("Male");
		registrationPage.clickOnBirthDateLink();
		registrationPage.enterPatientBirthDate(patient);
		registrationPage.clickOnContactInfo();
	}
}
