package org.openmrs.module.openmrswebhook;

import java.util.ArrayList;

public class WebhookRetryTask implements Runnable {
    private final AppointmentWebhookDispatcher dispatcher;
    private final WebhookOutbox outbox;

    public WebhookRetryTask(AppointmentWebhookDispatcher dispatcher, WebhookOutbox outbox) {
        this.dispatcher = dispatcher;
        this.outbox = outbox;
    }

    @Override
    public void run() {
        runOnce();
    }

    public void runOnce() {
        var failed = new ArrayList<WebhookOutboxEntry>();
        for (WebhookOutboxEntry entry : outbox.readAll()) {
            try {
                dispatcher.send(entry.eventId(), entry.eventType(), entry.body());
            } catch (Exception ex) {
                failed.add(entry);
            }
        }
        outbox.replaceAll(failed);
    }
}
