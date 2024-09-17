package org.amoseman.budgetingwebsitebackend.pojo.bucket.op;

/**
 * Represents a bucket update operation.
 */
public class UpdateBucket {
    private String name;
    private double share;

    public UpdateBucket() {
    }

    /**
     * Instantiate a bucket update.
     * @param name the updated name.
     * @param share the updated share.
     */
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
