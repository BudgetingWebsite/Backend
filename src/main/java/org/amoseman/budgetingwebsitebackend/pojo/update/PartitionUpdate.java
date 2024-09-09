package org.amoseman.budgetingwebsitebackend.pojo.update;

import java.time.LocalDateTime;

public class PartitionUpdate extends Update {
    private final double share;
    public PartitionUpdate(LocalDateTime updated, double share) {
        super(updated);
        this.share = share;
    }

    public double getShare() {
        return share;
    }
}
