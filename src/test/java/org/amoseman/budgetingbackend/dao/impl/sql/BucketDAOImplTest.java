package org.amoseman.budgetingbackend.dao.impl.sql;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingbackend.application.auth.hashing.ArgonHasher;
import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingbackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingbackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingbackend.pojo.account.op.CreateAccount;
import org.amoseman.budgetingbackend.pojo.bucket.Bucket;
import org.amoseman.budgetingbackend.pojo.bucket.op.BucketInfo;
import org.amoseman.budgetingbackend.service.AccountService;
import org.amoseman.budgetingbackend.util.Now;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BucketDAOImplTest {

    @Test
    void textCRUD() {
        String databaseURL = "jdbc:h2:mem:test";
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        BucketDAO<DSLContext> bucketDAO = new BucketDAOImpl(connection);

        try {
            new AccountService<>(new AccountDAOImpl(connection), new ArgonHasher(new SecureRandom(), 16, 16, 2, 8000, 1)).addAccount(new CreateAccount("alice", "password"));
        } catch (UserAlreadyExistsException e) {
            fail(e);
        }

        LocalDateTime now = Now.get();
        Bucket bucket = new Bucket(
                "12345",
                now,
                now,
                "alice",
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
        Optional<Bucket> maybe = bucketDAO.getBucket("alice", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve added bucket");
        }
        Bucket retrieved = maybe.get();
        assertEquals("12345", retrieved.uuid);
        assertEquals("alice", retrieved.owner);
        assertEquals("person_bucket", retrieved.name);
        assertEquals(0.5, retrieved.share);
        assertEquals(0, retrieved.amount);

        List<Bucket> buckets = bucketDAO.getBuckets("alice");
        assertEquals(1, buckets.size());
        Bucket inList = buckets.get(0);
        assertEquals(retrieved.uuid, inList.uuid);
        assertEquals(retrieved.owner, inList.owner);
        assertEquals(retrieved.name, inList.name);
        assertEquals(retrieved.share, inList.share);
        assertEquals(retrieved.amount, inList.amount);

        Bucket update = new Bucket(bucket, new BucketInfo("new_name", 0.3), Now.get());
        try {
            bucketDAO.updateBucket(update);
        }
        catch (BucketDoesNotExistException e) {
            fail(e);
        }
        maybe = bucketDAO.getBucket("alice", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve updated bucket");
        }
        retrieved = maybe.get();
        assertEquals("new_name", retrieved.name);
        assertEquals(0.3, retrieved.share);
        assertEquals(0, retrieved.amount);

        try {
            bucketDAO.removeBucket("alice", "12345");
        }
        catch (BucketDoesNotExistException e) {
            fail(e);
        }
        maybe = bucketDAO.getBucket("person", "12345");
        if (maybe.isPresent()) {
            fail("Able to retrieve bucket that should have be deleted");
        }
        buckets = bucketDAO.getBuckets("alice");
        assertEquals(0, buckets.size());
        InitTestDatabase.close(databaseURL);
    }
}