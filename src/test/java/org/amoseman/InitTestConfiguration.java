package org.amoseman;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class InitTestConfiguration {
    public static void init(String configurationLocation, String databaseLocation, String username, String password) {
        File file = new File(configurationLocation);
        file.deleteOnExit();
        try {
            if (!file.createNewFile()) {
                return;
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String contents =   "database-url: " + databaseLocation + "\n" +
                    "admin-username: " + username + "\n" +
                    "admin-password: " + password + "\n" +
                    "min-password-length: 0\n" +
                    "min-password-entropy: 0\n" +
                    "min-password-score: 0\n" +
                    "password-requires-uppercase: false\n" +
                    "password-requires-special: false";
            writer.write(contents);
            writer.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
