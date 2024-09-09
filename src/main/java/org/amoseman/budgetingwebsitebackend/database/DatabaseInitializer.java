package org.amoseman.budgetingwebsitebackend.database;

/**
 * Represents a database initializer.
 * @param <C> the client type.
 */
public abstract class DatabaseInitializer<C> {
    protected final DatabaseConnection<C> connection;

    /**
     * Instantiate a database initializer.
     * @param connection the connection to the database.
     */
    public DatabaseInitializer(DatabaseConnection<C> connection) {
        this.connection = connection;
    }

    /**
     * Initialize the database.
     */
    public abstract void initialize();
}
