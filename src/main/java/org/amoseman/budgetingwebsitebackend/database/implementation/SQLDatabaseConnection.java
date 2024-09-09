package org.amoseman.budgetingwebsitebackend.database.implementation;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
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
        SQLDialect dialect = getDialect(url);
        return DSL.using(connection, dialect);
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

    private SQLDialect getDialect(String url) {
        String[] parts = url.split(":");
        String type = parts[1];
        switch (type) {
            case "sqlite" -> {
                return SQLDialect.SQLITE;
            }
            default -> throw new RuntimeException(String.format("Dialect not supported for %s", type));
        }
    }
}
