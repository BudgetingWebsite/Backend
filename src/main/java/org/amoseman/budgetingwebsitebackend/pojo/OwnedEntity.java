package org.amoseman.budgetingwebsitebackend.pojo;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class OwnedEntity extends Entity {
    public final String owner;

    @ConstructorProperties({"uuid", "created", "updated", "owner"})
    public OwnedEntity(String uuid, LocalDateTime created, LocalDateTime updated, String owner) {
        super(uuid, created, updated);
        this.owner = owner;
    }
}
