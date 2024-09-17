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

public class BucketService<C> {
    private final BucketDAO<C> bucketDAO;
    private final FinanceRecordDAO<C> financeRecordDAO;

    public BucketService(BucketDAO<C> bucketDAO, FinanceRecordDAO<C> financeRecordDAO) {
        this.bucketDAO = bucketDAO;
        this.financeRecordDAO = financeRecordDAO;
    }

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

    public void removeBucket(String user, String uuid) throws BucketDoesNotExistException {
        bucketDAO.removeBucket(user, uuid);
    }

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

    public List<Bucket> getBuckets(String user) {
        List<Bucket> buckets = bucketDAO.getBuckets(user);
        List<Income> incomeRecords = financeRecordDAO.getAllIncome(user);
        incomeRecords.forEach(income -> {
            Split split = Splitter.get(buckets, income.amount);
            for (int i = 0; i < buckets.size(); i++) {
                long amount = split.getAmounts()[i];
                Bucket bucket = buckets.get(i);
                bucket.amount += amount;
            }
        });
        Map<String, Bucket> map = new HashMap<>();
        buckets.forEach(bucket -> map.put(bucket.uuid, bucket));
        List<Expense> expenseRecords = financeRecordDAO.getAllExpenses(user);
        expenseRecords.forEach(expense -> {
            Bucket bucket = map.get(expense.bucket);
            bucket.amount -= expense.amount;
        });
        return buckets;
    }
}
