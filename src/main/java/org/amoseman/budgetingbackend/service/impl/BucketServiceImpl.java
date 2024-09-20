package org.amoseman.budgetingbackend.service.impl;

import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingbackend.exception.TotalBucketShareExceededException;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.amoseman.budgetingbackend.pojo.bucket.Bucket;
import org.amoseman.budgetingbackend.pojo.bucket.BucketInfo;
import org.amoseman.budgetingbackend.service.BucketService;
import org.amoseman.budgetingbackend.util.Now;
import org.amoseman.budgetingbackend.util.Split;
import org.amoseman.budgetingbackend.util.Splitter;

import java.time.LocalDateTime;
import java.util.*;

public class BucketServiceImpl<C> extends BucketService<C> {
    public BucketServiceImpl(BucketDAO<C> bucketDAO, FinanceRecordDAO<C> financeRecordDAO) {
        super(bucketDAO, financeRecordDAO);
    }

    @Override
    public String addBucket(String user, BucketInfo create) throws BucketAlreadyExistsException, TotalBucketShareExceededException {
        List<Bucket> buckets = getBuckets(user);
        double totalShare = 0;
        for (Bucket bucket : buckets) {
            totalShare += bucket.share;
        }        if (totalShare + create.getShare() > 1) {
            throw new TotalBucketShareExceededException(totalShare, create.getShare());
        }
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

    @Override
    public void removeBucket(String user, String uuid) throws BucketDoesNotExistException {
        bucketDAO.removeBucket(user, uuid);
    }

    @Override
    public void updateBucket(String user, String uuid, BucketInfo update) throws BucketDoesNotExistException, TotalBucketShareExceededException {
        List<Bucket> buckets = getBuckets(user);
        double totalShare = 0;
        for (Bucket bucket : buckets) {
            if (bucket.uuid.equals(uuid)) {
                continue;
            }
            totalShare += bucket.share;
        }
        if (totalShare + update.getShare() > 1.0) {
            throw new TotalBucketShareExceededException(totalShare, update.getShare());
        }
        LocalDateTime now = Now.get();
        Optional<Bucket> maybe = bucketDAO.getBucket(user, uuid);
        if (maybe.isEmpty()) {
            throw new BucketDoesNotExistException("update", uuid);
        }
        Bucket bucket = maybe.get();
        bucket = new Bucket(bucket, update, now);
        bucketDAO.updateBucket(bucket);
    }

    @Override
    public List<Bucket> getBuckets(String user) {
        List<Bucket> buckets = bucketDAO.getBuckets(user);
        List<Income> incomeRecords = financeRecordDAO.getAllIncome(user);
        List<Expense> expenseRecords = financeRecordDAO.getAllExpenses(user);
        applyIncome(buckets, incomeRecords);
        applyExpenses(buckets, expenseRecords);
        return buckets;
    }

    @Override
    protected void applyIncome(List<Bucket> buckets, List<Income> incomeRecords) {
        incomeRecords.forEach(income -> {
            Split split = Splitter.get(buckets, income.amount);
            for (int i = 0; i < buckets.size(); i++) {
                long amount = split.getAmounts()[i];
                Bucket bucket = buckets.get(i);
                bucket.amount += amount;
            }
        });
    }

    @Override
    protected void applyExpenses(List<Bucket> buckets, List<Expense> expenseRecords) {
        Map<String, Bucket> map = new HashMap<>();
        buckets.forEach(bucket -> map.put(bucket.uuid, bucket));
        expenseRecords.forEach(expense -> {
            Bucket bucket = map.get(expense.bucket);
            bucket.amount -= expense.amount;
        });
    }
}
