package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.FinanceRecordAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.FinanceRecordDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;

import java.util.List;
import java.util.Optional;

/**
 * Represents a data access object for income and expense records.
 * @param <C> the client type.
 */
public abstract class FinanceRecordDAO<C> extends DAO<C> {
    /**
     * Instantiate a data access object for income and expense records.
     * @param connection the database connection to use.
     */
    public FinanceRecordDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Add an income record.
     * @param income the income record.
     * @throws FinanceRecordAlreadyExistsException if the record already exists.
     */
    public abstract void addIncome(Income income) throws FinanceRecordAlreadyExistsException;

    /**
     * Add an expense record.
     * @param expense the expense record.
     * @throws FinanceRecordAlreadyExistsException if the record already exists.
     */
    public abstract void addExpense(Expense expense) throws FinanceRecordAlreadyExistsException;

    /**
     * Remove an income record.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the record.
     * @return true if the record was removed, false if it does not exist.
     */
    public abstract boolean removeIncome(String user, String uuid) ;

    /**
     * Remove an expense record.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the record.
     * @return true if the record was removed, false if it does not exist.
     */
    public abstract boolean removeExpense(String user, String uuid) ;

    /**
     * Get all of a user's income records.
     * @param user the UUID of the owning user.
     * @return the list of records.
     */
    public abstract List<Income> getAllIncome(String user);

    /**
     * Get all of a user's expense records.
     * @param user the UUID of the owning user.
     * @return the list of records.
     */
    public abstract List<Expense> getAllExpenses(String user);

    /**
     * Get the user's income records in the provided time range.
     * @param user the UUID of the owning user.
     * @param range the time range.
     * @return the list of records.
     */
    public abstract List<Income> getIncomeInRange(String user, TimeRange range);

    /**
     * Get the user's expense records in the provided time range.
     * @param user the UUID of the owning user.
     * @param range the time range.
     * @return the list of records.
     */
    public abstract List<Expense> getExpensesInRange(String user, TimeRange range);

    /**
     * Get an income record.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the record.
     * @return the record.
     */
    public abstract Optional<Income> getIncome(String user, String uuid);

    /**
     * Get an expense record.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the record.
     * @return the record.
     */
    public abstract Optional<Expense> getExpense(String user, String uuid);
}