package org.amoseman.budgetingwebsitebackend.pojo;

import org.amoseman.budgetingwebsitebackend.pojo.update.PartitionUpdate;
import org.amoseman.budgetingwebsitebackend.pojo.update.Update;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class Updatable<U extends Update> extends Creatable {
    private LocalDateTime updated;

    public Updatable() {
        super();
        this.updated = LocalDateTime.now(ZoneId.of("UCT"));
    }

    public Updatable(String identifier, LocalDateTime created, LocalDateTime updated) {
        super(identifier, created);
        this.updated = updated;
    }

    public void update(U update) {
        this.updated = update.getUpdated();
        updateData(update);
    }

    public abstract void updateData(U update);

    public LocalDateTime getUpdated() {
        return updated;
    }

    public String getUpdatedFormatted() {
        return updated.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
