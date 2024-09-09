package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Updatable extends Creatable {
    private final LocalDateTime updated;

    public Updatable() {
        this.updated = LocalDateTime.now(ZoneId.of("UCT"));
    }

    public Updatable(LocalDateTime timestamp, LocalDateTime updated) {
        super(timestamp);
        this.updated = updated;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public String getUpdatedFormatted() {
        return updated.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
