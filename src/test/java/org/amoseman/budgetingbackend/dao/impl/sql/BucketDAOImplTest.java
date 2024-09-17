package org.amoseman.budgetingbackend.dao.impl.sql;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingbackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingbackend.pojo.bucket.Bucket;
import org.amoseman.budgetingbackend.pojo.bucket.op.UpdateBucket;
import org.amoseman.budgetingbackend.util.Now;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BucketDAOImplTest {

    @Test
    void textCRUD() {
        String databaseURL = "jdbc:sqlite:test.db";
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        BucketDAO<DSLContext> bucketDAO = new BucketDAOImpl(connection);

        LocalDateTime now = Now.get();
        Bucket bucket = new Bucket(
                "12345",
                now,
                now,
                "person",
                "person_bucket",
                0.5,
                0
        );
        try {
            bucketDAO.addBucket(bucket);
        }
        catch (BucketAlreadyExistsException e) {
            fail(e);
        }
        Optional<Bucket> maybe = bucketDAO.getBucket("person", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve added bucket");
        }
        Bucket retrieved = maybe.get();
        assertEquals("12345", retrieved.uuid);
        assertEquals("person", retrieved.owner);
        assertEquals("person_bucket", retrieved.name);
        assertEquals(0.5, retrieved.share);
        assertEquals(0, retrieved.amount);

        List<Bucket> buckets = bucketDAO.getBuckets("person");
        assertEquals(1, buckets.size());
        Bucket inList = buckets.get(0);
        assertEquals(retrieved.uuid, inList.uuid);
        assertEquals(retrieved.owner, inList.owner);
        assertEquals(retrieved.name, inList.name);
        assertEquals(retrieved.share, inList.share);
        assertEquals(retrieved.amount, inList.amount);

        Bucket update = new Bucket(bucket, new UpdateBucket("new_name", 0.3), Now.get());
        try {
            bucketDAO.updateBucket(update);
        }
        catch (BucketDoesNotExistException e) {
            fail(e);
        }
        maybe = bucketDAO.getBucket("person", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve updated bucket");
        }
        retrieved = maybe.get();
        assertEquals("new_name", retrieved.name);
        assertEquals(0.3, retrieved.share);
        assertEquals(0, retrieved.amount);

        try {
            bucketDAO.removeBucket("person", "12345");
        }
        catch (BucketDoesNotExistException e) {
            fail(e);
        }
        maybe = bucketDAO.getBucket("person", "12345");
        if (maybe.isPresent()) {
            fail("Able to retrieve bucket that should have be deleted");
        }
        buckets = bucketDAO.getBuckets("person");
        assertEquals(0, buckets.size());
        InitTestDatabase.close(databaseURL);
    }
}