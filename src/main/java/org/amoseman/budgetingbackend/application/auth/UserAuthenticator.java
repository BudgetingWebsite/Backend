package org.amoseman.budgetingbackend.application.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.model.account.Account;

import java.util.Base64;
import java.util.Optional;

public class UserAuthenticator implements Authenticator<BasicCredentials, User> {
    private final AccountDAO<?> accountDAO;
    private final Hash hash;

    public UserAuthenticator(AccountDAO<?> accountDAO, Hash hash) {
        this.accountDAO = accountDAO;
        this.hash = hash;
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        Optional<Account> maybe = accountDAO.getAccount(credentials.getUsername());
        if (maybe.isEmpty()) {
            return Optional.empty();
        }
        Account account = maybe.get();
        if (!validate(account, credentials.getPassword())) {
            return Optional.empty();
        }
        return Optional.of(new User(account));
    }

    private boolean validate(Account account, String attemptedPassword) {
        byte[] salt = Base64.getDecoder().decode(account.salt);
        String hash = this.hash.hash(attemptedPassword, salt);
        return hash.equals(account.hash);
    }
}
