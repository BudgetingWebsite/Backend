package org.amoseman.budgetingwebsitebackend.pojo.partition;

import org.amoseman.budgetingwebsitebackend.pojo.OwnedEntity;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.UpdatePartition;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class Partition extends OwnedEntity {
    public final String name;
    public final double share;
    public long amount;

    @ConstructorProperties({"uuid", "created", "updated", "owner", "name", "share", "amount"})
    public Partition(String uuid, LocalDateTime created, LocalDateTime updated, String owner, String name, double share, long amount) {
        super(uuid, created, updated, owner);
        this.name = name;
        this.share = share;
        this.amount = amount;
    }

    public Partition(Partition partition, UpdatePartition update, LocalDateTime updated) {
        super(partition.uuid, partition.created, updated, partition.owner);
        this.name = update.getName();
        this.share = update.getShare();
        this.amount = 0;
    }
}
