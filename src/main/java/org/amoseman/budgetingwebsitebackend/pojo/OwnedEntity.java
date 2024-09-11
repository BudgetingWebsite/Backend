package org.amoseman.budgetingwebsitebackend.pojo;

import java.time.LocalDateTime;

public class OwnedEntity extends Entity {
    protected final String owner;

    public OwnedEntity(String uuid, LocalDateTime created, LocalDateTime updated, String owner) {
        super(uuid, created, updated);
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}
