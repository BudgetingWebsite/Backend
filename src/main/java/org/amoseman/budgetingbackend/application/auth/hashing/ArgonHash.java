package org.amoseman.budgetingbackend.application.auth.hashing;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class ArgonHash extends Hash {
    private final int hashLength;
    private final Argon2Parameters.Builder builder;

    public ArgonHash(SecureRandom random, int saltLength, int hashLength, int iterations, int memory, int parallelism) {
        super(random, saltLength);
        this.hashLength = hashLength;
        builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(iterations)
                .withMemoryAsKB(memory)
                .withParallelism(parallelism);
    }

    @Override
    public String hash(String password, byte[] salt) {
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.withSalt(salt).build());
        byte[] hashBytes = new byte[hashLength];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8), hashBytes, 0, hashLength);
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
