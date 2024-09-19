package org.amoseman.budgetingbackend.application.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.budgetingbackend.application.auth.hashing.Hasher;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.pojo.account.Account;

import java.util.Base64;
import java.util.Optional;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final AccountDAO<?> accountDAO;
    private final Hasher hasher;

    public UserAuthenticator(AccountDAO<?> accountDAO, Hasher hasher) {
        this.accountDAO = accountDAO;
        this.hasher = hasher;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        System.out.println(credentials.getUsername());
        Optional<Account> maybe = accountDAO.getAccount(credentials.getUsername());
        if (maybe.isEmpty()) {
            System.out.println("NO ACCOUNT");
            return Optional.empty();
        }
        Account account = maybe.get();
        if (!validate(account, credentials.getPassword())) {
            System.out.println("BAD PASSWORD: " + credentials.getPassword());
            return Optional.empty();
        }
        return Optional.of(new User(account));
    }

    private boolean validate(Account account, String attemptedPassword) {
        byte[] salt = Base64.getDecoder().decode(account.salt);
        String hash = hasher.hash(attemptedPassword, salt);

        System.out.printf("%s : %s\n", account.hash, hash);

        return hash.equals(account.hash);
    }
}
