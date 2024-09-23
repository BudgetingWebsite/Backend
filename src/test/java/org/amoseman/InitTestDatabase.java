package org.amoseman;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.jooq.impl.DSL.table;

import static org.jooq.codegen.Tables.*;

public class InitTestDatabase {
    static Connection connection;

    public static void init(String databaseURL, String schemaLocation) {
        try {
            if (connection != null) {
                connection.close();
            }
            String location = databaseURL.split(":")[2];
            new File(location).deleteOnExit();
            connection = DriverManager.getConnection(databaseURL);
            String path = new File(InitTestDatabase.class.getClassLoader().getResource(schemaLocation).getFile()).toPath().toString();
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setSendFullScript(false);
            runner.setStopOnError(true);
            runner.runScript(new FileReader(path));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void clean(String databaseURL) {
        DSLContext context;
        try {
            context = DSL.using(DriverManager.getConnection(databaseURL), SQLDialect.H2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        context.deleteFrom(INCOME).where(DSL.trueCondition()).execute();
        context.deleteFrom(EXPENSE).where(DSL.trueCondition()).execute();
        context.deleteFrom(BUCKET).where(DSL.trueCondition()).execute();
        context.deleteFrom(ACCOUNT).where(DSL.trueCondition()).execute();
    }
}
