package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;

public class Entity {
    protected final String uuid;
    protected final LocalDateTime created;
    protected final LocalDateTime updated;

    public Entity(String uuid, LocalDateTime created, LocalDateTime updated) {
        this.uuid = uuid;
        this.created = created;
        this.updated = updated;
    }

    public String getUuid() {
        return uuid;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }
}
