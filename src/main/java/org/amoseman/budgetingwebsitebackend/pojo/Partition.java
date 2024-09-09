package org.amoseman.budgetingwebsitebackend.pojo;

import org.amoseman.budgetingwebsitebackend.pojo.update.PartitionUpdate;

import java.time.LocalDateTime;

/**
 * Represents a partition of a user's funds.
 */
public class Partition extends Updatable<PartitionUpdate> {
    private double share;

    /**
     * Instantiate a partition.
     * @param identifier the identifier.
     * @param created when the partition was created.
     * @param updated when the partition was last updated.
     * @param share the fractional share of the partition.
     */
    public Partition(String identifier, LocalDateTime created, LocalDateTime updated, double share) {
        super(identifier, created, updated);
        this.share = share;
    }

    @Override
    public void updateData(PartitionUpdate update) {
        this.share = update.getShare();
    }

    /**
     * Get the partition's share.
     * @return the fractional share.
     */
    public double getShare() {
        return share;
    }
}
