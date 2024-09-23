package org.amoseman.budgetingbackend.service;

import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingbackend.exception.TotalBucketShareExceededException;
import org.amoseman.budgetingbackend.model.bucket.Bucket;
import org.amoseman.budgetingbackend.model.bucket.BucketInfo;
import org.amoseman.budgetingbackend.model.record.Expense;
import org.amoseman.budgetingbackend.model.record.Income;

import java.util.*;

/**
 * A service for bucket logic.
 * @param <C> the client type.
 */
public abstract class BucketService<C> {
    protected final BucketDAO<C> bucketDAO;
    protected final FinanceRecordDAO<C> financeRecordDAO;

    /**
     * Instantiate a new bucket service.
     * @param bucketDAO the bucket data access object to use.
     * @param financeRecordDAO the finance record data access object to use.
     */
    public BucketService(BucketDAO<C> bucketDAO, FinanceRecordDAO<C> financeRecordDAO) {
        this.bucketDAO = bucketDAO;
        this.financeRecordDAO = financeRecordDAO;
    }

    /**
     * Add a new bucket.
     * @param user the UUID of the user.
     * @param create the information of the bucket.
     * @return the UUID of the new bucket.
     * @throws BucketAlreadyExistsException if the bucket already exists.
     * @throws TotalBucketShareExceededException if this would result in the user's total bucket share to exceed 1.
     */
    public abstract String addBucket(String user, BucketInfo create) throws BucketAlreadyExistsException, TotalBucketShareExceededException;

    /**
     * Remove a user's bucket.
     * This does not delete the expense records associated with the bucket, but instead leaves them orphaned.
     * @param user the UUID of the user.
     * @param uuid the UUID of the bucket.
     * @throws BucketDoesNotExistException if the bucket does not exist.
     */
    public abstract void removeBucket(String user, String uuid) throws BucketDoesNotExistException;

    /**
     * Update a user's bucket.
     * @param user the UUID of the user.
     * @param uuid the UUID of the bucket.
     * @param update the update information.
     * @throws BucketDoesNotExistException if the bucket does not exist.
     */
    public abstract void updateBucket(String user, String uuid, BucketInfo update) throws BucketDoesNotExistException, TotalBucketShareExceededException;

    /**
     * Get the user's buckets.
     * This will calculate the total funds associated with each bucket.
     * @param user the UUID of the user.
     * @return the buckets.
     */
    public abstract List<Bucket> getBuckets(String user);

    /**
     * Apply the income records to the amounts of the buckets.
     * @param buckets the buckets.
     * @param incomeRecords the income records.
     */
    protected abstract void applyIncome(List<Bucket> buckets, List<Income> incomeRecords);

    /**
     * Apply the expense records to the amounts of the buckets.
     * @param buckets the buckets.
     * @param expenseRecords the expense records.
     */
    protected abstract void applyExpenses(List<Bucket> buckets, List<Expense> expenseRecords);
}
