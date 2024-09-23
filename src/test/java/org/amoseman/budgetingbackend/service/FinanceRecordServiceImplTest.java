package org.amoseman.budgetingbackend.service;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingbackend.application.BudgetingConfiguration;
import org.amoseman.budgetingbackend.application.auth.hashing.ArgonHash;
import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.dao.impl.sql.AccountDAOImpl;
import org.amoseman.budgetingbackend.dao.impl.sql.BucketDAOImpl;
import org.amoseman.budgetingbackend.dao.impl.sql.FinanceRecordDAOImpl;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingbackend.exception.*;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.model.bucket.BucketInfo;
import org.amoseman.budgetingbackend.model.record.Expense;
import org.amoseman.budgetingbackend.model.record.Income;
import org.amoseman.budgetingbackend.model.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.model.record.info.IncomeInfo;
import org.amoseman.budgetingbackend.service.impl.AccountServiceImpl;
import org.amoseman.budgetingbackend.service.impl.BucketServiceImpl;
import org.amoseman.budgetingbackend.service.impl.FinanceRecordServiceImpl;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinanceRecordServiceImplTest {
    private static final String databaseURL = "jdbc:h2:mem:test";
    private static FinanceRecordServiceImpl<DSLContext> financeRecordService;
    static FinanceRecordDAO<DSLContext> financeRecordDAO;
    static DatabaseConnection<DSLContext> connection;

    @BeforeEach
    void setup() {
        InitTestDatabase.init(databaseURL, "schema.sql");
        connection = new DatabaseConnectionImpl(databaseURL);
        financeRecordDAO = new FinanceRecordDAOImpl(connection);
        financeRecordService = new FinanceRecordServiceImpl<>(financeRecordDAO);
    }

    @AfterEach
    void cleanup() {
        InitTestDatabase.clean(databaseURL);
    }

    void addAlice() {
        try {
            new AccountServiceImpl<>(new BudgetingConfiguration().setMaxUsernameLength(64), new AccountDAOImpl(connection), new ArgonHash(new SecureRandom(), 16, 16, 2, 8000, 1)).addAccount(new CreateAccount("alice", "password"));
        }
        catch (AccountAlreadyExistsException | UsernameExceedsMaxLengthException e) {
            fail(e);
        }
    }

    @Test
    void testIncomeCRUD() {
        addAlice();
        try {
            financeRecordService.addIncome("alice", new IncomeInfo(
                    100,
                    2024,
                    1,
                    1,
                    "category",
                    "description"
            ));
        }
        catch (FinanceRecordAlreadyExistsException | IllegalArgumentException e) {
            fail(e);
        }

        List<Income> records = financeRecordService.getIncome(
                "alice",
                2023,
                1,
                1,
                2024,
                1,
                2
        );
        assertEquals(1, records.size());
        Income income = records.get(0);
        assertEquals(100, income.amount);
        assertEquals("alice", income.owner);
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), income.occurred);
        assertEquals("category", income.category);
        assertEquals("description", income.description);

        try {
            financeRecordService.updateIncome("alice", income.uuid, new IncomeInfo(0, 2024, 1, 1, "", ""));
        }
        catch (IllegalArgumentException | FinanceRecordDoesNotExistException e) {
            fail(e);
        }
        records = financeRecordService.getIncome(
                "alice",
                2023,
                1,
                1,
                2024,
                1,
                2
        );
        if (records.isEmpty()) {
            fail("Unable to retrieve income after update");
        }
        Income updated = records.get(0);
        assertEquals(0, updated.amount);

        boolean deleted = financeRecordService.removeIncome("alice", income.uuid);
        if (!deleted) {
            fail("Unable to deleted added income");
        }
        records = financeRecordService.getIncome(
                "alice",
                2023,
                1,
                1,
                2024,
                1,
                2
        );
        assertEquals(0, records.size());
    }

    @Test
    void testExpenseCRUD() {
        addAlice();
        String bucket = null;
        try {
            bucket = new BucketServiceImpl<>(new BucketDAOImpl(connection), financeRecordDAO).addBucket("alice", new BucketInfo("bucket", 0.5));
        }
        catch (TotalBucketShareExceededException | BucketAlreadyExistsException e) {
            fail(e);
        }
        try {
            financeRecordService.addExpense("alice", new ExpenseInfo(
                    100,
                    2024,
                    1,
                    1,
                    "category",
                    "description",
                    bucket
            ));
        }
        catch (FinanceRecordAlreadyExistsException | IllegalArgumentException e) {
            fail(e);
        }

        List<Expense> records = financeRecordService.getExpenses(
                "alice",
                2023,
                1,
                1,
                2024,
                1,
                2
        );
        assertEquals(1, records.size());
        Expense expense = records.get(0);
        assertEquals(100, expense.amount);
        assertEquals("alice", expense.owner);
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0), expense.occurred);
        assertEquals("category", expense.category);
        assertEquals("description", expense.description);
        assertEquals(bucket, expense.bucket);

        try {
            financeRecordService.updateExpense("alice", expense.uuid, new ExpenseInfo(0, 2024, 1, 1, "", "", bucket));
        }
        catch (IllegalArgumentException | FinanceRecordDoesNotExistException e) {
            fail(e);
        }
        records = financeRecordService.getExpenses(
                "alice",
                2023,
                1,
                1,
                2024,
                1,
                2
        );
        if (records.isEmpty()) {
            fail("Unable to retrieve expense after update");
        }
        Expense updated = records.get(0);
        assertEquals(0, updated.amount);

        boolean deleted = financeRecordService.removeExpense("alice", expense.uuid);
        if (!deleted) {
            fail("Unable to deleted added expense");
        }
        records = financeRecordService.getExpenses(
                "alice",
                2023,
                1,
                1,
                2024,
                1,
                2
        );
        assertEquals(0, records.size());
    }
}