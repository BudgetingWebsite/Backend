package org.amoseman.budgetingbackend.dao.impl.sql;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingbackend.application.auth.hashing.ArgonHasher;
import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingbackend.exception.*;
import org.amoseman.budgetingbackend.pojo.TimeRange;
import org.amoseman.budgetingbackend.pojo.account.op.CreateAccount;
import org.amoseman.budgetingbackend.pojo.bucket.op.CreateBucket;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.amoseman.budgetingbackend.pojo.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.pojo.record.info.IncomeInfo;
import org.amoseman.budgetingbackend.service.AccountService;
import org.amoseman.budgetingbackend.service.BucketService;
import org.amoseman.budgetingbackend.util.Now;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FinanceRecordDAOImplTest {

    private static FinanceRecordDAO<DSLContext> financeRecordDAO;
    static String databaseURL = "jdbc:h2:mem:test";
    static String bucket;


    @BeforeEach
    void setup() {
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        financeRecordDAO = new FinanceRecordDAOImpl(connection);
        try {
            new AccountService<>(new AccountDAOImpl(connection), new ArgonHasher(new SecureRandom(), 16, 16, 2, 8000, 1)).addAccount(new CreateAccount("person", "password"));
        } catch (UserAlreadyExistsException e) {
            fail(e);
        }
        try {
            bucket = new BucketService<>(new BucketDAOImpl(connection), financeRecordDAO).addBucket("person", new CreateBucket("bucket", 0.5));
        }
        catch (TotalBucketShareExceededException | BucketAlreadyExistsException e) {
            fail(e);
        }
    }

    @Test
    void testIncomeCRUD() {
        LocalDateTime now = Now.get();
        LocalDateTime occurred = LocalDateTime.of(2024, 1, 1, 0, 0);
        Income income = null;
        try {
            income = new Income(
                    "12345",
                    now,
                    now,
                    "person",
                    100,
                    occurred,
                    "category",
                    "description"
            );
        }
        catch (NegativeValueException e) {
            fail(e);
        }
        try {
            financeRecordDAO.addIncome(income);
        }
        catch (FinanceRecordAlreadyExistsException e) {
            fail(e);
        }
        Optional<Income> maybe = financeRecordDAO.getIncome("person", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve income that was added");
        }
        Income retrieved = maybe.get();
        assertEquals("12345", retrieved.uuid);
        assertEquals("person", retrieved.owner);
        assertEquals(100, retrieved.amount);
        assertEquals("category", retrieved.category);
        assertEquals("description", retrieved.description);

        List<Income> records = financeRecordDAO.getAllIncome("person");
        assertEquals(1, records.size());

        records = financeRecordDAO.getIncomeInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(1, records.size());

        try {
            financeRecordDAO.updateIncome("person", "12345", new IncomeInfo(0, 2024, 1, 1, "", ""));
        }
        catch (FinanceRecordDoesNotExistException | NegativeValueException e) {
            fail(e);
        }
        maybe = financeRecordDAO.getIncome("person", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve income after it was updated");
        }
        Income updated = maybe.get();
        assertEquals(0, updated.amount);


        financeRecordDAO.removeIncome("person", "12345");
        maybe = financeRecordDAO.getIncome("person", "12345");
        if (maybe.isPresent()) {
            fail("Able retrieve income that should have been deleted");
        }
        records = financeRecordDAO.getAllIncome("person");
        assertEquals(0, records.size());
        records = financeRecordDAO.getIncomeInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(0, records.size());
        InitTestDatabase.close(databaseURL);
    }

    @Test
    void testExpenseCRUD() {
        LocalDateTime now = Now.get();
        LocalDateTime occurred = LocalDateTime.of(2024, 1, 1, 0, 0);
        Expense expense = null;
        try {
            expense = new Expense(
                    "12345",
                    now,
                    now,
                    "person",
                    100,
                    occurred,
                    "category",
                    "description",
                    bucket
            );
        }
        catch (NegativeValueException e) {
            fail(e);
        }
        try {
            financeRecordDAO.addExpense(expense);
        }
        catch (FinanceRecordAlreadyExistsException e) {
            fail(e);
        }
        Optional<Expense> maybe = financeRecordDAO.getExpense("person", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve expense that was added");
        }
        Expense retrieved = maybe.get();
        assertEquals("12345", retrieved.uuid);
        assertEquals("person", retrieved.owner);
        assertEquals(100, retrieved.amount);
        assertEquals("category", retrieved.category);
        assertEquals("description", retrieved.description);
        assertEquals(bucket, retrieved.bucket);

        List<Expense> records = financeRecordDAO.getAllExpenses("person");
        assertEquals(1, records.size());

        records = financeRecordDAO.getExpensesInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(1, records.size());

        try {
            financeRecordDAO.updateExpense("person", "12345", new ExpenseInfo(0, 2024, 1, 1, "", "", bucket));
        }
        catch (FinanceRecordDoesNotExistException | NegativeValueException e) {
            fail(e);
        }
        maybe = financeRecordDAO.getExpense("person", "12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve expense after it was updated");
        }
        Expense updated = maybe.get();
        assertEquals(0, updated.amount);

        financeRecordDAO.removeExpense("person", "12345");
        maybe = financeRecordDAO.getExpense("person", "12345");
        if (maybe.isPresent()) {
            fail("Able retrieve expense that should have been deleted");
        }
        records = financeRecordDAO.getAllExpenses("person");
        assertEquals(0, records.size());
        records = financeRecordDAO.getExpensesInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(0, records.size());
        InitTestDatabase.close(databaseURL);
    }
}