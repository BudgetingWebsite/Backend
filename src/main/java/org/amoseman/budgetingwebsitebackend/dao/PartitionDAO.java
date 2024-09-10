package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Partition;

import java.util.List;
import java.util.Optional;

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
    public abstract void addPartition(Partition partition) throws PartitionAlreadyExistsException;

    /**
     * Remove a partition.
     * @param owner the owner of the partition.
     * @param id the ID of the partition.
     */
    public abstract void removePartition(String owner, String id) throws PartitionDoesNotExistException;

    /**
     * Update a partition.
     * @param partition the partition.
     */
    public abstract void updatePartition(Partition partition) throws PartitionDoesNotExistException;

    /**
     * Get a partition.
     * @param owner the owner of the partition.
     * @param id the ID of the partition.
     * @return the partition.
     */
    public abstract Optional<Partition> getPartition(String owner, String id);

    /**
     * Get all partitions.
     * @param owner the owner of the partitions.
     * @return the partitions.
     */
    public abstract List<Partition> listPartitions(String owner);
}
