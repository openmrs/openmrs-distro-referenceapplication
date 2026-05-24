package org.openmrs.module.openmrswebhook;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class WebhookOutboxEntryTest {
    @Test
    void encodeDecodeRoundTrips() {
        var entry = new WebhookOutboxEntry("evt-1", "CREATED", "{\"encounterId\":\"enc-1\"}", Instant.parse("2026-05-23T10:00:00Z"));

        var decoded = WebhookOutboxEntry.decode(entry.encode());

        assertEquals(entry, decoded);
    }
}
