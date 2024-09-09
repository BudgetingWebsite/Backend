package org.amoseman.budgetingwebsitebackend.database;

import org.amoseman.budgetingwebsitebackend.application.BudgetingConfiguration;

import java.util.concurrent.locks.Condition;

/**
 * Represents a database initializer.
 * @param <C> the client type.
 */
public abstract class DatabaseInitializer<C> {
    protected final DatabaseConnection<C> connection;
    protected final BudgetingConfiguration configuration;

    /**
     * Instantiate a database initializer.
     * @param connection the connection to the database.
     * @param configuration the configuration to use.
     */
    public DatabaseInitializer(DatabaseConnection<C> connection, BudgetingConfiguration configuration) {
        this.connection = connection;
        this.configuration = configuration;
    }

    /**
     * Initialize the database.
     */
    public abstract void initialize();
}
