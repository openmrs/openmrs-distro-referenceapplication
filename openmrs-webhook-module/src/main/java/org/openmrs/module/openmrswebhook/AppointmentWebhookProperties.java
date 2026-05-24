package org.openmrs.module.openmrswebhook;

import org.openmrs.api.context.Context;

public record AppointmentWebhookProperties(
        String backendUrl,
        String secret,
        String organizationId,
        String outboxPath) {

    public static AppointmentWebhookProperties fromOpenMrsGlobalProperties() {
        var admin = Context.getAdministrationService();
        return new AppointmentWebhookProperties(
            admin.getGlobalProperty("openmrswebhook.backendUrl", ""),
            admin.getGlobalProperty("openmrswebhook.secret", ""),
            admin.getGlobalProperty("openmrswebhook.organizationId", "default"),
            admin.getGlobalProperty("openmrswebhook.outboxPath", "openmrs-webhook-outbox.jsonl")
        );
    }
}
