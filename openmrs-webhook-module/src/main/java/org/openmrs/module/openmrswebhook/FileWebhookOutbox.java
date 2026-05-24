package org.openmrs.module.openmrswebhook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileWebhookOutbox implements WebhookOutbox {
    private final Path path;

    public FileWebhookOutbox(Path path) {
        this.path = path;
    }

    @Override
    public synchronized void enqueue(WebhookOutboxEntry entry) {
        try {
            Files.createDirectories(path.toAbsolutePath().getParent());
            Files.writeString(
                path,
                entry.encode() + System.lineSeparator(),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not persist OpenMRS webhook outbox entry.", ex);
        }
    }

    @Override
    public synchronized List<WebhookOutboxEntry> readAll() {
        if (!Files.exists(path)) {
            return List.of();
        }

        try {
            var entries = new ArrayList<WebhookOutboxEntry>();
            for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
                if (!line.isBlank()) entries.add(WebhookOutboxEntry.decode(line));
            }
            return entries;
        } catch (IOException ex) {
            throw new IllegalStateException("Could not read OpenMRS webhook outbox.", ex);
        }
    }

    @Override
    public synchronized void replaceAll(List<WebhookOutboxEntry> entries) {
        try {
            Files.createDirectories(path.toAbsolutePath().getParent());
            var lines = entries.stream().map(WebhookOutboxEntry::encode).toList();
            Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not rewrite OpenMRS webhook outbox.", ex);
        }
    }

    public static WebhookOutboxEntry entry(String eventId, String eventType, String body) {
        return new WebhookOutboxEntry(eventId, eventType, body, Instant.now());
    }
}
