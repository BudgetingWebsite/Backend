package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;

/**
 * Represents a data access object.
 * @param <C> the client type.
 */
public class DAO<C> {
    protected final DatabaseConnection<C> connection;

    /**
     * Instantiate a data access object.
     * @param connection the database connection to use.
     */
    public DAO(DatabaseConnection<C> connection) {
        this.connection = connection;
    }
}
