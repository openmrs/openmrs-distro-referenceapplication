package org.openmrs.reference.helper;


import java.util.Calendar;

public class PatientGenerator {

	private static final String[] PATIENT_GIVEN_NAMES = { "Alexandre", "Achint",
			"Brittany", "Burke", "Cesar", "Cosmin", "Daniel", "Darius", "David", "Ellen",
			"Émerson", "Evan", "Fernando", "Gabou", "Glauber", "Hamish", "Louise", "Mário",
			"Mark", "Natália", "Neil", "Neissi", "Nelice", "Rafal", "Renee", "Wyclif" };
	
	private static final String[] PATIENT_FAMILY_NAMES = { "Barbosa", "Sethi", "Eddy",
			"Mamlin", "Vortmann", "Ioan", "Kayiwa", "Jazayeri", "Walton", "Ball", "Hernandez",
			"Waters", "Freire", "Mandy", "Ramos", "Fraser", "Sécordel", "Areias", "Goodrich",
			"Arsand", "Craven", "Lima", "Heck", "Korytkowski", "Orser", "Luyima" };

	private static final String[] PATIENT_BIRTH_MONTH = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October", "November",
			"December"};

    private static final String[] PATIENT_GENDER = {"","Male","Female"};

	public static String getPatientGivenName() {
		return PATIENT_GIVEN_NAMES[(int) (Math.random() * PATIENT_GIVEN_NAMES.length)];
	}

	public static String getPatientFamilyName() {
		return PATIENT_FAMILY_NAMES[(int) (Math.random() * PATIENT_FAMILY_NAMES.length)];
	}

    public static String getPatientGender(){
        return PATIENT_GENDER[(int) (Math.random() * PATIENT_GENDER.length)];
    }

    public static String getPatientBirthMonth(){
        return PATIENT_BIRTH_MONTH[(int) (Math.random() * PATIENT_BIRTH_MONTH.length)];
    }

    public static String getPatientAddress1(){
        return "Address1"+ String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getPatientAddress2(){
        return "Address2"+ String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getPatientCity(){
        return "City"+ String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getPatientState(){
        return "State"+ String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getPatientCountry(){
        return "Country"+ String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static String getPhoneNumber(){
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

}
