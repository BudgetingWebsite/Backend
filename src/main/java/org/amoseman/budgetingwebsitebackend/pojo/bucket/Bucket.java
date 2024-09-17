package org.amoseman.budgetingwebsitebackend.pojo.bucket;

import org.amoseman.budgetingwebsitebackend.pojo.OwnedEntity;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.UpdateBucket;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents a bucket for budgeting.
 */
public class Bucket extends OwnedEntity {
    public final String name;
    public final double share;
    public long amount;

    /**
     * Instantiate a bucket.
     * @param uuid the UUID of the bucket.
     * @param created when the bucket was created.
     * @param updated when the bucked was last updated.
     * @param owner the UUID of the user with the bucket.
     * @param name the name of the bucket.
     * @param share the share of the bucket.
     * @param amount the total amount in dollars associated with the bucket.
     */
    @ConstructorProperties({"uuid", "created", "updated", "owner", "name", "share", "amount"})
    public Bucket(String uuid, LocalDateTime created, LocalDateTime updated, String owner, String name, double share, long amount) {
        super(uuid, created, updated, owner);
        this.name = name;
        this.share = share;
        this.amount = amount;
    }

    /**
     * Update the bucket.
     * @param bucket the bucket.
     * @param update the update information.
     * @param updated when the update occurred.
     */
    public Bucket(Bucket bucket, UpdateBucket update, LocalDateTime updated) {
        super(bucket.uuid, bucket.created, updated, bucket.owner);
        this.name = update.getName();
        this.share = update.getShare();
        this.amount = 0;
    }
}
