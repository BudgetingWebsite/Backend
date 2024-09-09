package org.amoseman.budgetingwebsitebackend.application.auth;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Hasher {
    private final SecureRandom random;
    private final int hashLength;
    private final int saltLength;
    private final Argon2Parameters.Builder builder;

    public Hasher(SecureRandom random, int hashLength, int saltLength, int iterations, int memory, int parallelism) {
        this.random = random;
        this.hashLength = hashLength;
        this.saltLength = saltLength;
        builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(iterations)
                .withMemoryAsKB(memory)
                .withParallelism(parallelism);
    }

    public String hash(String password, byte[] salt) {
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.withSalt(salt).build());
        byte[] hashBytes = new byte[hashLength];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hashBytes, 0, hashLength);
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    public byte[] salt() {
        byte[] salt = new byte[saltLength];
        random.nextBytes(salt);
        return salt;
    }
}
