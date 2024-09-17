package org.amoseman.budgetingwebsitebackend.pojo.bucket;

import org.amoseman.budgetingwebsitebackend.pojo.OwnedEntity;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.UpdateBucket;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class Bucket extends OwnedEntity {
    public final String name;
    public final double share;
    public long amount;

    @ConstructorProperties({"uuid", "created", "updated", "owner", "name", "share", "amount"})
    public Bucket(String uuid, LocalDateTime created, LocalDateTime updated, String owner, String name, double share, long amount) {
        super(uuid, created, updated, owner);
        this.name = name;
        this.share = share;
        this.amount = amount;
    }

    public Bucket(Bucket bucket, UpdateBucket update, LocalDateTime updated) {
        super(bucket.uuid, bucket.created, updated, bucket.owner);
        this.name = update.getName();
        this.share = update.getShare();
        this.amount = 0;
    }
}
