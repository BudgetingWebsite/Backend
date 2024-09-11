package org.amoseman.budgetingwebsitebackend.pojo.partition;

import org.amoseman.budgetingwebsitebackend.pojo.OwnedEntity;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.UpdatePartition;
import org.amoseman.budgetingwebsitebackend.time.Now;

import java.time.LocalDateTime;

public class Partition extends OwnedEntity {
    private final String name;
    private final double share;
    private final long amount;

    public Partition(String uuid, LocalDateTime created, LocalDateTime updated, String owner, String name, double share, long amount) {
        super(uuid, created, updated, owner);
        this.name = name;
        this.share = share;
        this.amount = amount;
    }

    public Partition update(UpdatePartition update) {
        return new Partition(
                uuid,
                created,
                Now.get(),
                owner,
                update.getName(),
                update.getShare(),
                update.getAmount()
        );
    }

    public String getName() {
        return name;
    }

    public double getShare() {
        return share;
    }

    public long getAmount() {
        return amount;
    }
}
