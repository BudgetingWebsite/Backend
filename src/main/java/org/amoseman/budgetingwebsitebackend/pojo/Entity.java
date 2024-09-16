package org.amoseman.budgetingwebsitebackend.pojo;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class Entity {
    public final String uuid;
    public final LocalDateTime created;
    public final LocalDateTime updated;

    @ConstructorProperties({"uuid", "created", "updated"})
    public Entity(String uuid, LocalDateTime created, LocalDateTime updated) {
        this.uuid = uuid;
        this.created = created;
        this.updated = updated;
    }
}
