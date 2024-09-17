package org.amoseman.budgetingbackend.dao.impl.sql;

import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.exception.*;
import org.amoseman.budgetingbackend.pojo.TimeRange;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.jooq.*;

import java.util.List;
import java.util.Optional;

import static org.jooq.codegen.Tables.*;
import org.jooq.codegen.tables.records.*;

public class FinanceRecordDAOImpl extends FinanceRecordDAO<DSLContext> {

    public FinanceRecordDAOImpl(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addIncome(Income income) throws FinanceRecordAlreadyExistsException {
        try {
            IncomeRecord record = connection.get().newRecord(INCOME, income);
            connection.get().executeInsert(record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FinanceRecordAlreadyExistsException("add", income.uuid);
        }
    }

    @Override
    public void addExpense(Expense expense) throws FinanceRecordAlreadyExistsException {
        try {
            ExpenseRecord record = connection.get().newRecord(EXPENSE, expense);
            connection.get().executeInsert(record);
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
                    .deleteFrom(INCOME)
                    .where(
                            INCOME.UUID.eq(uuid).and(INCOME.OWNER.eq(user))
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
                    .deleteFrom(EXPENSE)
                    .where(
                            EXPENSE.UUID.eq(uuid).and(EXPENSE.OWNER.eq(user))
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
                    .selectFrom(INCOME)
                    .where(INCOME.OWNER.eq(user))
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
                    .selectFrom(EXPENSE)
                    .where(EXPENSE.OWNER.eq(user))
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
                    .selectFrom(INCOME)
                    .where(
                            INCOME.OWNER.eq(user)
                                    .and(
                                            INCOME.OCCURRED.greaterOrEqual(range.getStart())
                                                    .and(INCOME.OCCURRED.lessOrEqual(range.getEnd()))
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
                    .selectFrom(EXPENSE)
                    .where(
                            EXPENSE.OWNER.eq(user)
                                    .and(
                                            EXPENSE.OCCURRED.greaterOrEqual(range.getStart())
                                                    .and(EXPENSE.OCCURRED.lessOrEqual(range.getEnd()))
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
                    .selectFrom(INCOME)
                    .where(
                            INCOME.OWNER.eq(user).and(INCOME.UUID.eq(uuid))
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
                    .selectFrom(EXPENSE)
                    .where(
                            EXPENSE.OWNER.eq(user).and(EXPENSE.UUID.eq(uuid))
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
