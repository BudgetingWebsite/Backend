package org.amoseman.budgetingbackend.database.impl.sql.sqlite;

import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.jooq.*;
import org.jooq.conf.ExecuteWithoutWhere;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultRecordMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionImpl extends DatabaseConnection<DSLContext> {
    private Connection connection;

    /**
     * Instantiate a connection to an SQL database.
     * @param url the URL of the database.
     */
    public DatabaseConnectionImpl(String url) {
        super(url);
    }

    @Override
    protected DSLContext initialize(String url) {
        try {
            connection = DriverManager.getConnection(url);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        SQLDialect dialect = getDialect(url);
        Configuration configuration = new DefaultConfiguration()
                .set(connection)
                .set(dialect)
                .set(new Settings().withExecuteUpdateWithoutWhere(ExecuteWithoutWhere.THROW))
                .set(new Settings().withExecuteDeleteWithoutWhere(ExecuteWithoutWhere.THROW))
                .derive((RecordMapperProvider) DefaultRecordMapper::new);
        return DSL.using(configuration);
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

    /**
     * Determine the proper SQL dialect based on a given JDBC connection URL.
     * @param url the JDBC URL.
     * @return the SQL dialect.
     */
    private SQLDialect getDialect(String url) {
        String[] parts = url.split(":");
        String type = parts[1];
        switch (type) {
            case "h2" -> {
                return SQLDialect.H2;
            }
            case "sqlite" -> {
                return SQLDialect.SQLITE;
            }
            case "postgresql" -> {
                return SQLDialect.POSTGRES;
            }
            case "mariadb" -> {
                return SQLDialect.MARIADB;
            }
            case "mysql" -> {
                return SQLDialect.MYSQL;
            }
            default -> throw new RuntimeException(String.format("Dialect not supported for %s", type));
        }
    }
}
