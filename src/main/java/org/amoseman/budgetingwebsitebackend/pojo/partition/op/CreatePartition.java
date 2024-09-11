package org.amoseman.budgetingwebsitebackend.pojo.partition.op;

public class CreatePartition {
    private String name;
    private double share;

    public CreatePartition() {
    }

    public CreatePartition(String name, double share) {
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
