package org.openmrs.module.openmrswebhook;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

public record WebhookOutboxEntry(String eventId, String eventType, String body, Instant queuedAt) {
    public String encode() {
        return encode(eventId) + "\t" + encode(eventType) + "\t" + encode(body) + "\t" + queuedAt;
    }

    public static WebhookOutboxEntry decode(String line) {
        var parts = line.split("\t", -1);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Malformed outbox line.");
        }
        return new WebhookOutboxEntry(
            decodePart(parts[0]),
            decodePart(parts[1]),
            decodePart(parts[2]),
            Instant.parse(parts[3]));
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decodePart(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
