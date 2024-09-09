package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents an object with a timestamp for when it was created.
 */
public class Creatable {
    private final LocalDateTime created;

    public Creatable() {
        this.created = LocalDateTime.now(ZoneId.of("UCT"));
    }

    public Creatable(LocalDateTime timestamp) {
        this.created = timestamp;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getCreatedFormatted() {
        return created.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
