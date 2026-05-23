package org.openmrs.module.openmrswebhook;

public record AppointmentWebhookPayload(
        String encounterId,
        String patientId,
        String start,
        String end,
        String status,
        String patientDisplay,
        String serviceType,
        String location,
        String instructions) {

    public String toJson() {
        return "{"
            + "\"encounterId\":\"" + escape(encounterId) + "\","
            + "\"patientId\":\"" + escape(patientId) + "\","
            + "\"start\":\"" + escape(start) + "\","
            + nullable("end", end) + ","
            + "\"status\":\"" + escape(status) + "\","
            + nullable("patientDisplay", patientDisplay) + ","
            + nullable("serviceType", serviceType) + ","
            + nullable("location", location) + ","
            + nullable("instructions", instructions)
            + "}";
    }

    private static String nullable(String name, String value) {
        return "\"" + name + "\":" + (value == null ? "null" : "\"" + escape(value) + "\"");
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
