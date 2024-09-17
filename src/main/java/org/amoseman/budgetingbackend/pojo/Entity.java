package org.amoseman.budgetingbackend.pojo;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents a generic entity.
 * It is the most basic form of a record in the system.
 */
public class Entity {
    public final String uuid;
    public final LocalDateTime created;
    public final LocalDateTime updated;

    /**
     * Instantiate an entity.
     * @param uuid the UUID of the entity.
     * @param created when the entity was created.
     * @param updated when the entity was last updated.
     */
    @ConstructorProperties({"uuid", "created", "updated"})
    public Entity(String uuid, LocalDateTime created, LocalDateTime updated) {
        this.uuid = uuid;
        this.created = created;
        this.updated = updated;
    }
}
