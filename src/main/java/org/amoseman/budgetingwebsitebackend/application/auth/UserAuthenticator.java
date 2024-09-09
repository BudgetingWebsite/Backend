package org.amoseman.budgetingwebsitebackend.application.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Account;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.checkerframework.checker.units.qual.A;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final AccountDAO<?> accountDAO;

    public UserAuthenticator(AccountDAO<?> accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Account account;
        try {
            account = accountDAO.getAccount(credentials.getUsername());
        }
        catch (UserDoesNotExistException e) {
            return Optional.empty();
        }
        if (!validate(account, credentials.getPassword())) {
            return Optional.empty();
        }
        return Optional.of(new User(account));
    }

    private boolean validate(Account account, String attempt) {
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withIterations(2)
                .withMemoryAsKB(8192)
                .withParallelism(1)
                .withSalt(Base64.getDecoder().decode(account.getPasswordSalt()));
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(builder.build());
        byte[] attemptHashBytes = new byte[16];
        generator.generateBytes(attempt.getBytes(StandardCharsets.UTF_8), attemptHashBytes, 0, attemptHashBytes.length);
        String attemptHash = Base64.getEncoder().encodeToString(attemptHashBytes);
        return account.getPasswordHash().equals(attemptHash);
    }
}
