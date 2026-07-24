package org.openmrs.module.labtestreport.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Loads the native SQL backing the lab test report from src/main/resources/queries so both the
 * Hibernate DAO and the registered {@link org.openmrs.module.reporting.report.manager.ReportManager}
 * evaluate the exact same query.
 */
public class SqlResources {

	private SqlResources() {
	}

	public static String load(String resourceName) {
		String path = "queries/" + resourceName;
		try (InputStream in = SqlResources.class.getClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new IllegalStateException("Could not find classpath resource " + path);
			}
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				return reader.lines().collect(Collectors.joining("\n"));
			}
		}
		catch (IOException e) {
			throw new IllegalStateException("Failed to load SQL resource " + path, e);
		}
	}
}
