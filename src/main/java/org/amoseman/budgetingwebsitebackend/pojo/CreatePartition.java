package org.amoseman.budgetingwebsitebackend.pojo;

public class CreatePartition {
    private final double share;

    public CreatePartition(double share) {
        this.share = share;
    }

    public double getShare() {
        return share;
    }
}
