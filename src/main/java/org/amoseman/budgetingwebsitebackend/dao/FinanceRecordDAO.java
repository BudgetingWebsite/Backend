package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.FinanceRecordAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.FinanceRecordDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceRecord;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;

import java.util.List;

/**
 * Represents a data access object for income and expense events.
 * @param <C> the client type.
 */
public abstract class FinanceRecordDAO<C> extends DAO<C> {
    /**
     * Instantiate a data access object for income and expense events.
     * @param connection the database connection to use.
     */
    public FinanceRecordDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Add a finance event.
     * @param event the finance event.
     */
    public abstract void addEvent(Income event) throws FinanceRecordAlreadyExistsException;
    public abstract void addEvent(Expense event) throws FinanceRecordAlreadyExistsException;

    /**
     * Remove a finance event.
     *
     * @param user the user of the event.
     * @param id   the ID of the event.
     * @param type the type of the event.
     * @return
     * @throws FinanceRecordDoesNotExistException if the event does not exist.
     */
    public abstract FinanceRecord removeEvent(String user, String id, String type) throws FinanceRecordDoesNotExistException;

    /**
     * Get all finance events of a user.
     * @param user the ID of the user.
     * @param type the type of events.
     * @return the finance events.
     */
    public abstract List<FinanceRecord> getEvents(String user, String type) ;

    /**
     * Get all finance events of a user in a time range.
     * @param user the ID of the user.
     * @param type the type of events.
     * @param range the time range of use.
     * @return the finance events.
     */
    public abstract List<FinanceRecord> getEvents(String user, String type, TimeRange range);
}
