package org.amoseman;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public static void close(String databaseURL) {
        try {
            connection.createStatement().execute("SHUTDOWN");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        new File(databaseURL.split(":")[2]).delete();

    }
}
