package org.amoseman.budgetingbackend.database;

/**
 * Represents a connection to the database.
 * @param <C> the type of the client.
 */
public abstract class DatabaseConnection<C> {
    protected final C client;

    /**
     * Instantiate a connection to a database.
     * @param url the URL of the database.
     */
    public DatabaseConnection(String url) {
        this.client = initialize(url);
    }

    /**
     * Initialize the client for the database.
     * @param url the URL of the database.
     * @return the client.
     */
    protected abstract C initialize(String url);

    /**
     * Close the connection to the database.
     */
    public abstract void close();

    /**
     * Get the client for the database.
     * @return C the client.
     */
    public C get() {
        return client;
    }
}
