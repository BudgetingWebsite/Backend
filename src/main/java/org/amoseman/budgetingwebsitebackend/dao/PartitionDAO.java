package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;

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
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the partition.
     */
    public abstract void removePartition(String user, String uuid) throws PartitionDoesNotExistException;

    /**
     * Update a partition.
     * @param partition the partition.
     */
    public abstract void updatePartition(Partition partition) throws PartitionDoesNotExistException;

    /**
     * Get a partition.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the partition.
     * @return the partition.
     */
    public abstract Optional<Partition> getPartition(String user, String uuid);

    /**
     * Get all partitions.
     * @param user the UUID of the owning user.
     * @return the partitions.
     */
    public abstract List<Partition> getPartitions(String user);
}
