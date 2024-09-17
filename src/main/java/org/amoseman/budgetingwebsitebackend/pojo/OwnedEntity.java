package org.amoseman.budgetingwebsitebackend.pojo;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents an owned entity.
 */
public class OwnedEntity extends Entity {
    public final String owner;

    /**
     * Instantiate an owned entity.
     * @param uuid the UUID of the entity.
     * @param created when the entity was created.
     * @param updated when the entity was last updated.
     * @param owner the UUID of the account that owns this entity.
     */
    @ConstructorProperties({"uuid", "created", "updated", "owner"})
    public OwnedEntity(String uuid, LocalDateTime created, LocalDateTime updated, String owner) {
        super(uuid, created, updated);
        this.owner = owner;
    }
}
