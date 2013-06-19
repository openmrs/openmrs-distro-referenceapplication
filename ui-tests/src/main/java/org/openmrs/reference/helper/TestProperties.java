package org.openmrs.reference.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;
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
        return properties.getProperty("webapp.url");
    }

    public String getUserName(){
        return properties.getProperty("login.username");
    }

    public String getPassword(){
        return properties.getProperty("login.password");
    }
}
