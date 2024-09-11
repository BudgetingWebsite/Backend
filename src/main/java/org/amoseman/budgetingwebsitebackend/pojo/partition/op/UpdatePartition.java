package org.amoseman.budgetingwebsitebackend.pojo.partition.op;

public class UpdatePartition {
    private String name;
    private double share;
    private long amount;

    public UpdatePartition(String name, double share, long amount) {
        this.name = name;
        this.share = share;
        this.amount = amount;
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
