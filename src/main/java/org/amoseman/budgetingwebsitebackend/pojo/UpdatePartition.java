package org.amoseman.budgetingwebsitebackend.pojo;

public class UpdatePartition {
    private final double share;
    private final long amount;

    public UpdatePartition(double share, long amount) {
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
