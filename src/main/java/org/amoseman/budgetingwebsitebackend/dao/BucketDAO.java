package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.Bucket;

import java.util.List;
import java.util.Optional;

/**
 * Represents a bucket data access object.
 * @param <C> the client type.
 */
public abstract class BucketDAO<C> extends DAO<C> {
    /**
     * Instantiate a bucket data access object.
     * @param connection the database connection to use.
     */
    public BucketDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Add a bucket.
     * @param bucket the bucket.
     */
    public abstract void addBucket(Bucket bucket) throws BucketAlreadyExistsException;

    /**
     * Remove a bucket.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the bucket.
     */
    public abstract void removeBucket(String user, String uuid) throws BucketDoesNotExistException;

    /**
     * Update a bucket.
     * @param bucket the bucket.
     */
    public abstract void updateBucket(Bucket bucket) throws BucketDoesNotExistException;

    /**
     * Get a bucket.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the bucket.
     * @return the bucket.
     */
    public abstract Optional<Bucket> getBucket(String user, String uuid);

    /**
     * Get all buckets.
     * @param user the UUID of the owning user.
     * @return the buckets.
     */
    public abstract List<Bucket> getBuckets(String user);
}
