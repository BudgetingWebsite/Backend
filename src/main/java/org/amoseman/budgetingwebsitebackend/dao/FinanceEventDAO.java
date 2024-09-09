package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.FinanceEventAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.FinanceEventDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;

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
     * Add a finance event.
     * @param event the finance event.
     */
    public abstract void addEvent(FinanceEvent event) throws FinanceEventAlreadyExistsException;

    /**
     * Remove a finance event.
     * @param id the ID of the event.
     * @param type the type of the event.
     * @throws FinanceEventDoesNotExistException if the event does not exist.
     */
    public abstract void removeEvent(String id, String type) throws FinanceEventDoesNotExistException;

    /**
     * Get all finance events of a user.
     * @param user the ID of the user.
     * @param type the type of events.
     * @return the finance events.
     */
    public abstract List<FinanceEvent> getEvents(String user, String type) ;

    /**
     * Get all finance events of a user in a time range.
     * @param user the ID of the user.
     * @param type the type of events.
     * @param range the time range of use.
     * @return the finance events.
     */
    public abstract List<FinanceEvent> getEvents(String user, String type, TimeRange range) ;
}
