package org.openmrs.module.openmrswebhook;

import java.net.http.HttpClient;
import java.nio.file.Path;
import org.openmrs.module.BaseModuleActivator;

public class OpenmrsWebhookActivator extends BaseModuleActivator {
    @Override
    public void started() {
        var properties = AppointmentWebhookProperties.fromOpenMrsGlobalProperties();
        var outbox = new FileWebhookOutbox(Path.of(properties.outboxPath()));
        var dispatcher = new AppointmentWebhookDispatcher(
            properties,
            new HmacSigner(),
            outbox,
            HttpClient.newHttpClient());

        new EventSubscriptionRegistrar()
            .registerEncounterListener(new AppointmentEventListener(dispatcher));
    }
}
