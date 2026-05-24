package org.openmrs.module.openmrswebhook;

import java.time.Instant;
import java.util.UUID;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class AppointmentEventListener implements MessageListener {
    private final AppointmentWebhookDispatcher dispatcher;

    public AppointmentEventListener(AppointmentWebhookDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void onMessage(Message message) {
        if (!(message instanceof MapMessage mapMessage)) {
            return;
        }

        try {
            String eventType = valueOrDefault(mapMessage, "action", "UPDATED");
            String encounterId = firstPresent(mapMessage, "uuid", "encounterUuid", "resourceUuid");
            String patientId = firstPresent(mapMessage, "patientUuid", "patientId", "subjectUuid");
            String status = valueOrDefault(mapMessage, "status", "planned");
            String start = valueOrDefault(mapMessage, "start", Instant.now().toString());
            String eventId = valueOrDefault(mapMessage, "eventId", UUID.randomUUID().toString());

            if (encounterId == null || patientId == null) {
                return;
            }

            var payload = new AppointmentWebhookPayload(
                encounterId,
                patientId,
                start,
                optional(mapMessage, "end"),
                status,
                optional(mapMessage, "patientDisplay"),
                optional(mapMessage, "serviceType"),
                optional(mapMessage, "location"),
                optional(mapMessage, "instructions")
            );

            dispatcher.dispatch(eventId, eventType, payload);
        } catch (Exception ex) {
            dispatcher.queueFailure("listener-error", "UPDATED", "{}");
        }
    }

    private static String firstPresent(MapMessage message, String... keys) throws JMSException {
        for (String key : keys) {
            var value = optional(message, key);
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private static String valueOrDefault(MapMessage message, String key, String defaultValue) throws JMSException {
        var value = optional(message, key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static String optional(MapMessage message, String key) throws JMSException {
        return message.itemExists(key) ? message.getString(key) : null;
    }
}
