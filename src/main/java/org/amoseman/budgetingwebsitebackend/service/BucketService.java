package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.BucketDAO;
import org.amoseman.budgetingwebsitebackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.TotalBucketShareExceededException;
import org.amoseman.budgetingwebsitebackend.pojo.record.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.record.Income;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.Bucket;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.CreateBucket;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.UpdateBucket;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.amoseman.budgetingwebsitebackend.util.Split;
import org.amoseman.budgetingwebsitebackend.util.Splitter;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A service for bucket logic.
 * @param <C> the client type.
 */
public class BucketService<C> {
    private final BucketDAO<C> bucketDAO;
    private final FinanceRecordDAO<C> financeRecordDAO;

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
    public String addBucket(String user, CreateBucket create) throws BucketAlreadyExistsException, TotalBucketShareExceededException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        Bucket bucket = new Bucket(
                uuid,
                now,
                now,
                user,
                create.getName(),
                create.getShare(),
                0
        );
        bucketDAO.addBucket(bucket);
        return uuid;
    }

    /**
     * Remove a user's bucket.
     * This does not delete the expense records associated with the bucket, but instead leaves them orphaned.
     * @param user the UUID of the user.
     * @param uuid the UUID of the bucket.
     * @throws BucketDoesNotExistException if the bucket does not exist.
     */
    public void removeBucket(String user, String uuid) throws BucketDoesNotExistException {
        bucketDAO.removeBucket(user, uuid);
    }

    /**
     * Update a user's bucket.
     * @param user the UUID of the user.
     * @param uuid the UUID of the bucket.
     * @param update the update information.
     * @throws BucketDoesNotExistException if the bucket does not exist.
     */
    public void updateBucket(String user, String uuid, UpdateBucket update) throws BucketDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Bucket> maybe = bucketDAO.getBucket(user, uuid);
        if (maybe.isEmpty()) {
            throw new BucketDoesNotExistException("update", uuid);
        }
        Bucket bucket = maybe.get();
        bucket = new Bucket(bucket, update, now);
        bucketDAO.updateBucket(bucket);
    }

    /**
     * Get the user's buckets.
     * This will calculate the total funds associated with each bucket.
     * @param user the UUID of the user.
     * @return the buckets.
     */
    public List<Bucket> getBuckets(String user) {
        List<Bucket> buckets = bucketDAO.getBuckets(user);
        List<Income> incomeRecords = financeRecordDAO.getAllIncome(user);
        List<Expense> expenseRecords = financeRecordDAO.getAllExpenses(user);
        applyIncome(buckets, incomeRecords);
        applyExpenses(buckets, expenseRecords);
        return buckets;
    }

    /**
     * Apply the income records to the amounts of the buckets.
     * @param buckets the buckets.
     * @param incomeRecords the income records.
     */
    private void applyIncome(List<Bucket> buckets, List<Income> incomeRecords) {
        incomeRecords.forEach(income -> {
            Split split = Splitter.get(buckets, income.amount);
            for (int i = 0; i < buckets.size(); i++) {
                long amount = split.getAmounts()[i];
                Bucket bucket = buckets.get(i);
                bucket.amount += amount;
            }
        });
    }

    /**
     * Apply the expense records to the amounts of the buckets.
     * @param buckets the buckets.
     * @param expenseRecords the expense records.
     */
    private void applyExpenses(List<Bucket> buckets, List<Expense> expenseRecords) {
        Map<String, Bucket> map = new HashMap<>();
        buckets.forEach(bucket -> map.put(bucket.uuid, bucket));
        expenseRecords.forEach(expense -> {
            Bucket bucket = map.get(expense.bucket);
            bucket.amount -= expense.amount;
        });
    }
}
