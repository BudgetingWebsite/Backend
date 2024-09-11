package org.amoseman.budgetingwebsitebackend.application.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;

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

    private boolean validate(Account account, String attempt) {
        byte[] salt = Base64.getDecoder().decode(account.getSalt());
        String hash = hasher.hash(attempt, salt);
        return hash.equals(account.getHash());
    }
}
