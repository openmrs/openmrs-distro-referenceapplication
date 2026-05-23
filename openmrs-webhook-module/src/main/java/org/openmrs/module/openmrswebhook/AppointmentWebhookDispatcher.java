package org.openmrs.module.openmrswebhook;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;

public class AppointmentWebhookDispatcher {
    private final AppointmentWebhookProperties properties;
    private final HmacSigner signer;
    private final WebhookOutbox outbox;
    private final HttpClient httpClient;

    public AppointmentWebhookDispatcher(
            AppointmentWebhookProperties properties,
            HmacSigner signer,
            WebhookOutbox outbox,
            HttpClient httpClient) {
        this.properties = properties;
        this.signer = signer;
        this.outbox = outbox;
        this.httpClient = httpClient;
    }

    public void dispatch(String eventId, String eventType, AppointmentWebhookPayload payload) {
        String body = payload.toJson();
        try {
            send(eventId, eventType, body);
        } catch (Exception ex) {
            outbox.enqueue(new WebhookOutboxEntry(eventId, eventType, body, Instant.now()));
        }
    }

    public void queueFailure(String eventId, String eventType, String body) {
        outbox.enqueue(new WebhookOutboxEntry(eventId, eventType, body, Instant.now()));
    }

    public boolean send(String eventId, String eventType, String body) throws IOException, InterruptedException {
        var timestamp = Instant.now().toString();
        var signature = signer.sign(timestamp, body, properties.secret());
        var request = HttpRequest.newBuilder(URI.create(properties.backendUrl()))
            .header("Content-Type", "application/json")
            .header("X-OpenMRS-Event-Id", eventId)
            .header("X-OpenMRS-Event-Type", eventType)
            .header("X-OpenMRS-Timestamp", timestamp)
            .header("X-OpenMRS-Organization-Id", properties.organizationId())
            .header("X-OpenMRS-Signature", "sha256=" + signature)
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        if (response.statusCode() < 200 || response.statusCode() > 299) {
            throw new IOException("Backend webhook returned HTTP " + response.statusCode());
        }
        return true;
    }
}
