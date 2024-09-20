package org.amoseman.budgetingbackend.pojo.bucket;

/**
 * Represents the information of a bucket.
 */
public class BucketInfo {
    private String name;
    private double share;

    public BucketInfo() {
    }

    /**
     * Instantiate a bucket information object.
     * @param name the name of the new bucket.
     * @param share the share of the new bucket.
     */
    public BucketInfo(String name, double share) {
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
