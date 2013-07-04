package org.openmrs.reference;

import org.openmrs.reference.helper.TestPatient;


public class PatientGenerator {
	
	public static TestPatient generateTestPatient() {
		TestPatient p = new TestPatient();
		p.givenName = randomArrayEntry(PATIENT_GIVEN_NAMES);
		p.familyName = randomArrayEntry(PATIENT_FAMILY_NAMES);
		p.gender = randomArrayEntry(PATIENT_GENDER);
		String suffix = randomSuffix();
		p.address1 = "Address1" + suffix;
		p.address2 = "Address2" + suffix;
		p.city = "City" + suffix;
		p.state = "State" + suffix;	// TODO shorter string for State perhaps?
		p.country = "Country" + suffix;	// TODO shorter string for Country perhaps?
		p.phone = randomSuffix(9);
		p.postalCode = "345234";	// hardwired for now
		p.latitude = "12";	// hardwired for now
		p.longitude = "47"; // hardwired for now
		p.startDate = "01-01-2000"; // hardwired for now
		p.endDate = "01-01-2010"; // hardwired for now
		return p;
	}
	
	private static final String[] PATIENT_GIVEN_NAMES = { "Alexandre", "Achint", "Brittany", "Burke", "Cesar", "Cosmin",
	        "Daniel", "Darius", "David", "Ellen", "Émerson", "Evan", "Fernando", "Gabou", "Glauber", "Hamish", "Louise",
	        "Mário", "Mark", "Natália", "Neil", "Neissi", "Nelice", "Rafal", "Renee", "Wyclif" };
	
	private static final String[] PATIENT_FAMILY_NAMES = { "Barbosa", "Sethi", "Eddy", "Mamlin", "Vortmann", "Ioan",
	        "Kayiwa", "Jazayeri", "Walton", "Ball", "Hernandez", "Waters", "Freire", "Mandy", "Ramos", "Fraser", "Sécordel",
	        "Areias", "Goodrich", "Arsand", "Craven", "Lima", "Heck", "Korytkowski", "Orser", "Luyima" };
	
	private static final String[] PATIENT_BIRTH_MONTH = { "January", "February", "March", "April", "May", "June", "July",
	        "August", "September", "October", "November", "December" };
	
	private static final String[] PATIENT_GENDER = { "M", "F" };
	
	static String randomArrayEntry(String[] array) {
		return array[(int) (Math.random() * array.length)];
	}
	
	public static String getPatientGivenName() {
		return randomArrayEntry(PATIENT_GIVEN_NAMES);
	}
	
	public static String getPatientFamilyName() {
		return randomArrayEntry(PATIENT_FAMILY_NAMES);
	}
	
	public static String getPatientGender() {
		return randomArrayEntry(PATIENT_GENDER);
	}
	
	public static String getPatientBirthMonth() {
		return randomArrayEntry(PATIENT_BIRTH_MONTH);
	}
	
    static String randomSuffix() {
    	return randomSuffix(6);
    }

    static String randomSuffix(int digits) {
    	// First n digits of the current time.
    	return String.valueOf(System.currentTimeMillis()).substring(0, digits-1);
    }
    
    public static String getPatientAddress1(){
        return "Address1" + randomSuffix();
    }

    public static String getPatientAddress2(){
        return "Address2" + randomSuffix();
    }
	
	public static String getPatientCity() {
		return "City" + randomSuffix();
	}
	
	public static String getPatientState() {
		return "State" + randomSuffix();
	}
	
	public static String getPatientCountry() {
		return "Country" + randomSuffix();
	}
	
	public static String getPhoneNumber() {
		return String.valueOf(System.currentTimeMillis());
	}
	
}
