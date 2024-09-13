package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;

/**
 * Represents a statistics data access object.
 * @param <C> the client type.
 */
public abstract class StatisticsDAO<C> extends DAO<C> {
    public StatisticsDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Get the total funds of the user.
     * @param user the username of the user.
     * @return the total funds in cents.
     */
    public abstract long totalFunds(String user);
}
