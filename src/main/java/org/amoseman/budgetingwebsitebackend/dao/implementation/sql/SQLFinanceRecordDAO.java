package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;
import org.jooq.*;
import org.jooq.Record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLFinanceRecordDAO extends FinanceRecordDAO<DSLContext> {

    private static final Table<Record> INCOME_TABLE = table("income");
    private static final Table<Record> EXPENSE_TABLE = table("expense");
    private static final Field<String> UUID_FIELD = field("uuid", String.class);
    private static final Field<String> OWNER_FIELD = field("owner", String.class);
    private static final Field<LocalDateTime> OCCURRED_FIELD = field("occurred", LocalDateTime.class);

    public SQLFinanceRecordDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addIncome(Income income) throws FinanceRecordAlreadyExistsException {
        try {
            Record record = connection.get().newRecord(INCOME_TABLE, income);
            connection.get().executeInsert((TableRecord<?>) record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FinanceRecordAlreadyExistsException("add", income.uuid);
        }
    }

    @Override
    public void addExpense(Expense expense) throws FinanceRecordAlreadyExistsException {
        try {
            Record record = connection.get().newRecord(EXPENSE_TABLE, expense);
            connection.get().executeInsert((TableRecord<?>) record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FinanceRecordAlreadyExistsException("add", expense.uuid);
        }
    }

    @Override
    public boolean removeIncome(String user, String uuid) {
        try {
            return 1 == connection.get()
                    .deleteFrom(INCOME_TABLE)
                    .where(
                            UUID_FIELD.eq(uuid).and(OWNER_FIELD.eq(user))
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeExpense(String user, String uuid) {
        try {
            return 1 == connection.get()
                    .deleteFrom(EXPENSE_TABLE)
                    .where(
                            UUID_FIELD.eq(uuid).and(OWNER_FIELD.eq(user))
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Income> getAllIncome(String user) {
        try {
            return connection.get()
                    .selectFrom(INCOME_TABLE)
                    .where(OWNER_FIELD.eq(user))
                    .fetch()
                    .into(Income.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Expense> getAllExpenses(String user) {
        try {
            return connection.get()
                    .selectFrom(EXPENSE_TABLE)
                    .where(OWNER_FIELD.eq(user))
                    .fetch()
                    .into(Expense.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Income> getIncomeInRange(String user, TimeRange range) {
        try {
            return connection.get()
                    .selectFrom(INCOME_TABLE)
                    .where(
                            OWNER_FIELD.eq(user)
                                    .and(
                                            OCCURRED_FIELD.greaterOrEqual(range.getStart())
                                                    .and(OCCURRED_FIELD.lessOrEqual(range.getEnd()))
                                    )
                    )
                    .fetch()
                    .into(Income.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Expense> getExpensesInRange(String user, TimeRange range) {
        try {
            return connection.get()
                    .selectFrom(EXPENSE_TABLE)
                    .where(
                            OWNER_FIELD.eq(user)
                                    .and(
                                            OCCURRED_FIELD.greaterOrEqual(range.getStart())
                                                    .and(OCCURRED_FIELD.lessOrEqual(range.getEnd()))
                                    )
                    )
                    .fetch()
                    .into(Expense.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Income> getIncome(String user, String uuid) {
        try {
            List<Income> list = connection.get()
                    .selectFrom(INCOME_TABLE)
                    .where(
                            OWNER_FIELD.eq(user).and(UUID_FIELD.eq(uuid))
                    )
                    .fetch()
                    .into(Income.class);
            if (list.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(list.get(0));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Expense> getExpense(String user, String uuid) {
        try {
            List<Expense> list = connection.get()
                    .selectFrom(EXPENSE_TABLE)
                    .where(
                            OWNER_FIELD.eq(user).and(UUID_FIELD.eq(uuid))
                    )
                    .fetch()
                    .into(Expense.class);
            if (list.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(list.get(0));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
