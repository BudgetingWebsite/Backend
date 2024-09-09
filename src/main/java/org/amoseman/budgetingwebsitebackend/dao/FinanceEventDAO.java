package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.ExpenseEventDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.IncomeEventDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a data access object for income and expense events.
 * @param <C> the client type.
 */
public abstract class FinanceEventDAO<C> extends DAO<C> {
    /**
     * Instantiate a data access object for income and expense events.
     * @param connection the database connection to use.
     */
    public FinanceEventDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Add an income event.
     * @param event the income event.
     */
    public abstract void addIncomeEvent(FinanceEvent event);

    /**
     * Add an expense event.
     * @param event the expense event.
     */
    public abstract void addExpenseEvent(FinanceEvent event);

    /**
     * Remove an income event.
     * @param id the ID of the income event.
     * @throws IncomeEventDoesNotExistException if the income event does not exist.
     */
    public abstract void removeIncomeEvent(String id) throws IncomeEventDoesNotExistException;

    /**
     * Remove an expense event.
     * @param id the ID of the expense event.
     * @throws ExpenseEventDoesNotExistException if the expense event does not exist.
     */
    public abstract void removeExpenseEvent(String id) throws ExpenseEventDoesNotExistException;

    /**
     * Get all income events of a user.
     * @param user the ID of the user.
     * @return the income events.
     * @throws UserDoesNotExistException if the user does not exist.
     */
    public abstract List<FinanceEvent> getIncomeEvents(String user) throws UserDoesNotExistException;

    /**
     * Get all expense events of a user.
     * @param user the ID of the user.
     * @return the expense events.
     * @throws UserDoesNotExistException if the user does not exist.
     */
    public abstract List<FinanceEvent> getExpenseEvents(String user) throws UserDoesNotExistException;

    /**
     * Get all income events of a user in a time range.
     * @param user the ID of the user.
     * @param range the time range of use.
     * @return the income events.
     * @throws UserDoesNotExistException if the user does not exist.
     */
    public abstract List<FinanceEvent> getIncomeEvents(String user, TimeRange range) throws UserDoesNotExistException;

    /**
     * Get all expense events of a user in a time range.
     * @param user the ID of the user.
     * @param range the time range of use.
     * @return the expense events.
     * @throws UserDoesNotExistException if the user does not exist.
     */
    public abstract List<FinanceEvent> getExpenseEvents(String user, TimeRange range) throws UserDoesNotExistException;

}
