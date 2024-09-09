package org.amoseman.budgetingwebsitebackend.pojo.update;

import java.time.LocalDateTime;

public class PartitionUpdate extends Update {
    private final double share;
    private final long amount;
    public PartitionUpdate(LocalDateTime updated, double share, long amount) {
        super(updated);
        this.share = share;
        this.amount = amount;
    }

    public double getShare() {
        return share;
    }

    public long getAmount() {
        return amount;
    }
}
