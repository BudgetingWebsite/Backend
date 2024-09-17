package org.amoseman.budgetingwebsitebackend.pojo.bucket.op;

/**
 * Represents a bucket creation operation.
 */
public class CreateBucket {
    private String name;
    private double share;

    public CreateBucket() {
    }

    /**
     * Instantiate a bucket creation operation.
     * @param name the name of the new bucket.
     * @param share the share of the new bucket.
     */
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
