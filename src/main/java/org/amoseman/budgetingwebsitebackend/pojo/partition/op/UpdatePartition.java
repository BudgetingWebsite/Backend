package org.amoseman.budgetingwebsitebackend.pojo.partition.op;

public class UpdatePartition {
    private String name;
    private double share;

    public UpdatePartition() {
    }

    public UpdatePartition(String name, double share) {
        this.name = name;
        this.share = share;
    }

    public String getName() {
        return name;
    }

    public double getShare() {
        return share;
    }
}
