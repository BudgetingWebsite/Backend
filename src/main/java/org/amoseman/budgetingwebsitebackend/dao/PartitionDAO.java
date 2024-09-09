package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.pojo.Partition;

/**
 * Represents a partition data access object.
 * @param <C> the client type.
 */
public abstract class PartitionDAO<C> extends DAO<C> {
    /**
     * Instantiate a partition data access object.
     * @param connection the database connection to use.
     */
    public PartitionDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Add a partition.
     * @param partition the partition.
     */
    public abstract void addPartition(Partition partition);

    /**
     * Remove a partition.
     * @param id the ID of the partition.
     */
    public abstract void removePartition(String id);

    /**
     * Update a partition.
     * @param partition the partition.
     */
    public abstract void updatePartition(Partition partition);
}
