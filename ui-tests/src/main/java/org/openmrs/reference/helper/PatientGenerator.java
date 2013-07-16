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

    private static final String[] PATIENT_BIRTH_DAY = {"1","2","3","4","5","6","7","8",
            "9","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
            "24","25","26","27","28"};

    private static final String[] PATIENT_BIRTH_YEAR = {"1980","1981","1982","1983",
            "1990","1991","1995"};

    private static final String[] PATIENT_GENDER = {"M","F"};

    private static String randomGenerator(String[] fieldName){
        return fieldName[(int)(Math.random()*fieldName.length)];
    }

	public static String getPatientGivenName() {
        return randomGenerator(PATIENT_GIVEN_NAMES);
	}

	public static String getPatientFamilyName() {
        return randomGenerator(PATIENT_FAMILY_NAMES);
	}

    public static String getPatientGender(){
        return randomGenerator(PATIENT_GENDER);
    }

    public static String getPatientBirthDay(){
        return randomGenerator(PATIENT_BIRTH_DAY);
    }

    public static String getPatientBirthMonth(){
        return randomGenerator(PATIENT_BIRTH_MONTH);
    }

    public static String getPatientBirthYear(){
         return randomGenerator(PATIENT_BIRTH_YEAR);
    }

    private static String timeGenerator(){
        return String.valueOf(Calendar.getInstance().getTimeInMillis()).substring(0,5);
    }

    public static String getPatientAddress1(){
        return "Address1"+ timeGenerator();
    }

    public static String getPatientAddress2(){
        return "Address2"+ timeGenerator();
    }

    public static String getPatientCity(){
        return "City"+ timeGenerator();
    }

    public static String getPatientState(){
        return "State"+ timeGenerator();
    }

    public static String getPatientCountry(){
        return "Country"+ timeGenerator();
    }

    public static String getPhoneNumber(){
        return String.valueOf(Calendar.getInstance().getTime());
    }

}
