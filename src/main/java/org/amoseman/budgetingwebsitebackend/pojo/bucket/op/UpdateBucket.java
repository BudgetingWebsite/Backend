package org.amoseman.budgetingwebsitebackend.pojo.bucket.op;

public class UpdateBucket {
    private String name;
    private double share;

    public UpdateBucket() {
    }

    public UpdateBucket(String name, double share) {
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
