package org.openmrs.reference.page;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class TestProperties {
    private Properties properties;

    public TestProperties() {
        properties = new Properties();
        try {
            InputStream input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("org/openmrs/reference/test.properties");
            properties.load(new InputStreamReader(input, "UTF-8"));
        }
        catch (IOException ioException) {
            throw new RuntimeException("test.properties not found. Error: ", ioException);
        }
    }

    public String getWebAppUrl() {
        return properties.getProperty("webapp.url", "http://localhost:8080/openmrs");
    }

    public String getDatabaseUrl() {
        return properties.getProperty("database.url", "jdbc:mysql://localhost:3306/openmrs");
    }

    public String getDatabaseUsername() {
        return properties.getProperty("database.username", "");
    }

    public String getDatabasePassword() {
        return properties.getProperty("database.password", "");
    }

}
