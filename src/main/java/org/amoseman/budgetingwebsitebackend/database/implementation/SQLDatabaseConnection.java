package org.amoseman.budgetingwebsitebackend.database.implementation;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseConnection extends DatabaseConnection<DSLContext> {
    private Connection connection;

    /**
     * Instantiate a connection to an SQL database.
     * @param url the URL of the database.
     */
    public SQLDatabaseConnection(String url) {
        super(url);
    }

    @Override
    protected DSLContext getClient(String url) {
        try {
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return DSL.using(connection);
    }

    @Override
    protected void close() {
        try {
            connection.close();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
