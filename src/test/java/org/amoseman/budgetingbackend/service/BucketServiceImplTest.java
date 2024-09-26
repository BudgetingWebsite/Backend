package org.amoseman.budgetingbackend.service;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingbackend.application.BudgetingConfiguration;
import org.amoseman.budgetingbackend.application.auth.hashing.ArgonHash;
import org.amoseman.budgetingbackend.dao.BucketDAO;
import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.dao.impl.sql.AccountDAOImpl;
import org.amoseman.budgetingbackend.dao.impl.sql.BucketDAOImpl;
import org.amoseman.budgetingbackend.dao.impl.sql.FinanceRecordDAOImpl;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingbackend.exception.*;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.model.bucket.Bucket;
import org.amoseman.budgetingbackend.model.bucket.BucketInfo;
import org.amoseman.budgetingbackend.model.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.model.record.info.IncomeInfo;
import org.amoseman.budgetingbackend.service.impl.AccountServiceImpl;
import org.amoseman.budgetingbackend.service.impl.BucketServiceImpl;
import org.amoseman.budgetingbackend.service.impl.FinanceRecordServiceImpl;
import org.jooq.DSLContext;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BucketServiceImplTest {
    private static final String databaseURL = "jdbc:h2:mem:test";
    private static BucketServiceImpl<DSLContext> bucketService;
    private static FinanceRecordServiceImpl<DSLContext> financeRecordService;
    static DatabaseConnection<DSLContext> connection;

    @AfterEach
    void cleanup() {
        InitTestDatabase.clean(databaseURL);
    }

    @BeforeEach
    void setup() {
        InitTestDatabase.init(databaseURL, "schema.sql");
        connection = new DatabaseConnectionImpl(databaseURL);
        BucketDAO<DSLContext> bucketDAO = new BucketDAOImpl(connection);
        FinanceRecordDAO<DSLContext> financeRecordDAO = new FinanceRecordDAOImpl(connection);
        bucketService = new BucketServiceImpl<>(bucketDAO, financeRecordDAO);
        financeRecordService = new FinanceRecordServiceImpl<>(financeRecordDAO);
        try {
            new AccountServiceImpl<>(
                    new BudgetingConfiguration().setMinPasswordLength(0).setMinPasswordEntropy(0).setMinPasswordScore(0).setPasswordRequiresUppercase(false).setPasswordRequiresSpecial(false),
                    new AccountDAOImpl(connection),
                    new ArgonHash(new SecureRandom(), 16, 16, 2, 8000, 1))
                    .addAccount(new CreateAccount("alice", "password"));
        } catch (AccountAlreadyExistsException | UsernameExceedsMaxLengthException | InvalidPasswordException e) {
            fail(e);
        }
    }

    @Test
    @Order(1)
    void testCRUD() {
        String uuid = null;
        try {
            uuid = bucketService.addBucket("alice", new BucketInfo("savings", 0.2));
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
            bucketService.updateBucket("alice", uuid, new BucketInfo("expenses", 0.5));
        }
        catch (BucketDoesNotExistException | TotalBucketShareExceededException e) {
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
    }

    @Test
    @Order(1)
    void testRecords() {
        try {
            bucketService.addBucket("alice", new BucketInfo("savings", 0.2));
            bucketService.addBucket("alice", new BucketInfo("expenses", 0.5));
            bucketService.addBucket("alice", new BucketInfo("other", 0.3));
        }
        catch (BucketAlreadyExistsException | TotalBucketShareExceededException e) {
            fail(e);
        }
        try {
            financeRecordService.addIncome("alice", new IncomeInfo(200, 2024, 1, 1, "", ""));
        }
        catch (FinanceRecordAlreadyExistsException | IllegalArgumentException e) {
            fail(e);
        }

        List<Bucket> buckets = bucketService.getBuckets("alice");
        Bucket savings = buckets.get(0);
        Bucket expenses = buckets.get(1);
        Bucket other = buckets.get(2);
        assertEquals(40, savings.amount);
        assertEquals(100, expenses.amount);
        assertEquals(60, other.amount);

        try {
            bucketService.updateBucket("alice", expenses.uuid, new BucketInfo("expenses", 0.2));
            bucketService.updateBucket("alice", savings.uuid, new BucketInfo("savings", 0.5));
        }
        catch (BucketDoesNotExistException | TotalBucketShareExceededException e) {
            fail(e);
        }
        buckets = bucketService.getBuckets("alice");
        savings = buckets.get(0);
        expenses = buckets.get(1);
        other = buckets.get(2);
        assertEquals(100, savings.amount);
        assertEquals(40, expenses.amount);
        assertEquals(60, other.amount);

        try {
            financeRecordService.addExpense("alice", new ExpenseInfo(100, 2024, 1, 1, "", "", savings.uuid));
        }
        catch (IllegalArgumentException | FinanceRecordAlreadyExistsException e) {
            fail(e);
        }
        buckets = bucketService.getBuckets("alice");
        savings = buckets.get(0);
        expenses = buckets.get(1);
        other = buckets.get(2);
        assertEquals(0, savings.amount);
        assertEquals(40, expenses.amount);
        assertEquals(60, other.amount);
    }
}