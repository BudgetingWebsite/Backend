package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingwebsitebackend.dao.BucketDAO;
import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.impl.sql.BucketDAOImpl;
import org.amoseman.budgetingwebsitebackend.dao.impl.sql.FinanceRecordDAOImpl;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingwebsitebackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.TotalBucketShareExceededException;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.Bucket;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.CreateBucket;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.op.UpdateBucket;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BucketServiceTest {
    private static final String databaseURL = "jdbc:sqlite:test.db";
    private static BucketService<DSLContext> bucketService;

    @BeforeEach
    void setup() {
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        BucketDAO<DSLContext> bucketDAO = new BucketDAOImpl(connection);
        FinanceRecordDAO<DSLContext> financeRecordDAO = new FinanceRecordDAOImpl(connection);
        bucketService = new BucketService<>(bucketDAO, financeRecordDAO);
    }

    @Test
    void testCRUD() {
        String uuid = null;
        try {
            uuid = bucketService.addBucket("alice", new CreateBucket("savings", 0.2));
        }
        catch (BucketAlreadyExistsException | TotalBucketShareExceededException e) {
            fail(e);
        }
        List<Bucket> buckets = bucketService.getBuckets("alice");
        assertEquals(1, buckets.size());
        Bucket bucket = buckets.get(0);
        assertEquals(uuid, bucket.uuid);
        assertEquals("alice", bucket.owner);
        assertEquals("savings", bucket.name);
        assertEquals(0.2, bucket.share);
        assertEquals(0, bucket.amount);

        try {
            bucketService.updateBucket("alice", uuid, new UpdateBucket("expenses", 0.5));
        }
        catch (BucketDoesNotExistException e) {
            fail(e);
        }
        buckets = bucketService.getBuckets("alice");
        assertEquals(1, buckets.size());
        bucket = buckets.get(0);
        assertEquals(uuid, bucket.uuid);
        assertEquals("alice", bucket.owner);
        assertEquals("expenses", bucket.name);
        assertEquals(0.5, bucket.share);
        assertEquals(0, bucket.amount);

        try {
            bucketService.removeBucket("alice", uuid);
        }
        catch (BucketDoesNotExistException e) {
            fail(e);
        }
        buckets = bucketService.getBuckets("alice");
        assertEquals(0, buckets.size());

        InitTestDatabase.close(databaseURL);
    }
}