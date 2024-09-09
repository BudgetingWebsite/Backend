package org.amoseman.budgetingwebsitebackend.application.auth;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Hashing {
    public static String hash(int length, String password, byte[] salt) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(2)
                .withMemoryAsKB(8192)
                .withParallelism(1)
                .withSalt(salt);
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        byte[] hashBytes = new byte[length];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hashBytes, 0, length);
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
