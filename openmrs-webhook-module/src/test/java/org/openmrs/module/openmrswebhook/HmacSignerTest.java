package org.openmrs.module.openmrswebhook;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HmacSignerTest {
    @Test
    void signsTimestampDotBodyWithSha256() {
        var signature = new HmacSigner().sign("2026-05-23T10:00:00Z", "{\"encounterId\":\"enc-1\"}", "secret");

        assertEquals("d6755d8bcbca62cb177940b96f00d8aa4ebc83992e91dcb796df6c6064691204", signature);
    }
}
