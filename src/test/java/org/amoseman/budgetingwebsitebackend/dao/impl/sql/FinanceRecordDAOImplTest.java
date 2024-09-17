package org.amoseman.budgetingwebsitebackend.dao.impl.sql;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingwebsitebackend.exception.FinanceRecordAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
import org.amoseman.budgetingwebsitebackend.pojo.record.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.record.Income;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FinanceRecordDAOImplTest {

    private static FinanceRecordDAO<DSLContext> financeRecordDAO;

    @BeforeAll
    static void setup() {
        String databaseURL = "jdbc:sqlite:test.db";
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        financeRecordDAO = new FinanceRecordDAOImpl(connection);
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

        financeRecordDAO.removeIncome("person", "12345");
        maybe = financeRecordDAO.getIncome("person", "12345");
        if (maybe.isPresent()) {
            fail("Able retrieve income that should have been deleted");
        }
        records = financeRecordDAO.getAllIncome("person");
        assertEquals(0, records.size());
        records = financeRecordDAO.getIncomeInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(0, records.size());
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
                    "bucket"
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
        assertEquals("bucket", retrieved.bucket);

        List<Expense> records = financeRecordDAO.getAllExpenses("person");
        assertEquals(1, records.size());

        records = financeRecordDAO.getExpensesInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(1, records.size());

        financeRecordDAO.removeExpense("person", "12345");
        maybe = financeRecordDAO.getExpense("person", "12345");
        if (maybe.isPresent()) {
            fail("Able retrieve expense that should have been deleted");
        }
        records = financeRecordDAO.getAllExpenses("person");
        assertEquals(0, records.size());
        records = financeRecordDAO.getExpensesInRange("person", new TimeRange(LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2024, 1, 2, 0, 0)));
        assertEquals(0, records.size());
    }
}