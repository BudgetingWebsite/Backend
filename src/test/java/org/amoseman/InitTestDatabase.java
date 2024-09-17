package org.amoseman;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;

public class InitTestDatabase {
    public static void init(String databaseURL, String schemaLocation) {
        try {
            String location = databaseURL.split(":")[2];
            new File(location).deleteOnExit();
            Connection connection = DriverManager.getConnection(databaseURL);
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

}
