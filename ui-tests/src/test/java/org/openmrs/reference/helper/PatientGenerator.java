package org.openmrs.reference.helper;

public class PatientGenerator {

    private static final String[] PATIENT_GIVEN_NAMES = {"Alexandre", "Achint", "Brittany", "Burke", "Cesar", "Cosmin",
            "Daniel", "Darius", "David", "Ellen", "Émerson", "Evan", "Fernando", "Gabou", "Glauber", "Hamish", "Louise",
            "Mário", "Mark", "Natália", "Neil", "Neissi", "Nelice", "Rafal", "Renee", "Wyclif"};
    private static final String[] PATIENT_FAMILY_NAMES = {"Barbosa", "Sethi", "Eddy", "Mamlin", "Vortmann", "Ioan",
            "Kayiwa", "Jazayeri", "Walton", "Ball", "Hernandez", "Waters", "Freire", "Mandy", "Ramos", "Fraser", "Sécordel",
            "Areias", "Goodrich", "Arsand", "Craven", "Lima", "Heck", "Korytkowski", "Orser", "Luyima"};
    private static final String[] PATIENT_BIRTH_MONTH = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    private static final String[] PATIENT_BIRTH_DAY = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
            "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
    private static final String[] PATIENT_BIRTH_YEAR = {"1980", "1981", "1982", "1983", "1990", "1991", "1995"};
    private static final String[] PATIENT_GENDER = {"Male", "Female"};

    public static TestPatient generateTestPatient() {
        TestPatient p = new TestPatient();
        p.givenName = randomArrayEntry(PATIENT_GIVEN_NAMES);
        p.middleName = "";
        p.familyName = randomArrayEntry(PATIENT_FAMILY_NAMES);
        p.gender = randomArrayEntry(PATIENT_GENDER);
        p.birthDay = randomArrayEntry(PATIENT_BIRTH_DAY);
        p.birthMonth = randomArrayEntry(PATIENT_BIRTH_MONTH);
        p.birthYear = randomArrayEntry(PATIENT_BIRTH_YEAR);
        String suffix = randomSuffix();
        p.address1 = "Address1" + suffix;
        p.address2 = "Address2" + suffix;
        p.city = "City" + suffix;
        p.state = "State" + suffix; // TODO shorter string for State perhaps?
        p.country = "Country" + suffix; // TODO shorter string for Country perhaps?
        p.phone = randomSuffix(9);
        p.postalCode = "345234"; // hardwired for now
        p.latitude = "12"; // hardwired for now
        p.longitude = "47"; // hardwired for now
        p.startDate = "01-01-2000"; // hardwired for now
        p.endDate = "01-01-2010"; // hardwired for now
        return p;
    }

    static String randomArrayEntry(String[] array) {
        return array[(int) (Math.random() * array.length)];
    }

    static String randomSuffix() {
        return randomSuffix(6);
    }

    static String randomSuffix(int digits) {
        // First n digits of the current time.
        return String.valueOf(System.currentTimeMillis()).substring(0, digits - 1);
    }

}
