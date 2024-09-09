package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents an object with an identifier and a timestamp for when it was created.
 */
public class Creatable {
    private final String identifier;
    private final LocalDateTime created;

    public Creatable() {
        this.identifier = UUID.randomUUID().toString();
        this.created = LocalDateTime.now(ZoneId.of("UCT"));
    }

    public Creatable(String identifier, LocalDateTime timestamp) {
        this.identifier = identifier;
        this.created = timestamp;
    }

    public String getIdentifier() {
        return identifier;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getCreatedFormatted() {
        return created.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}