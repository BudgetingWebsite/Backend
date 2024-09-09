package org.amoseman.budgetingwebsitebackend.pojo;

import org.amoseman.budgetingwebsitebackend.pojo.update.PartitionUpdate;

import java.time.LocalDateTime;

/**
 * Represents a partition of a user's funds.
 */
public class Partition extends Updatable<PartitionUpdate> {
    private String owner;
    private double share;
    private long amount;

    /**
     * Instantiate a partition.
     * @param identifier the identifier.
     * @param created when the partition was created.
     * @param updated when the partition was last updated.
     * @param owner the owner of the partition.
     * @param share the fractional share of the partition.
     * @param amount the amount in the partition.
     */
    public Partition(String identifier, LocalDateTime created, LocalDateTime updated, String owner, double share, long amount) {
        super(identifier, created, updated);
        this.owner = owner;
        this.share = share;
        this.amount = amount;
    }

    @Override
    public void updateData(PartitionUpdate update) {
        this.share = update.getShare();
        this.amount = update.getAmount();
    }

    /**
     * Get the owner of the partition.
     * @return the owner.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get the partition's share.
     * @return the fractional share.
     */
    public double getShare() {
        return share;
    }

    /**
     * Get the amount in the partition.
     * @return the amount.
     */
    public long getAmount() {
        return amount;
    }
}
