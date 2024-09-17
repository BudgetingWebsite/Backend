package org.amoseman.budgetingwebsitebackend.pojo.bucket.op;

public class CreateBucket {
    private String name;
    private double share;

    public CreateBucket() {
    }

    public CreateBucket(String name, double share) {
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
