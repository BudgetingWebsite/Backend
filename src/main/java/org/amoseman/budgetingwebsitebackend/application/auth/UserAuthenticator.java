package org.amoseman.budgetingwebsitebackend.application.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Account;

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
        byte[] salt = Base64.getDecoder().decode(account.getPasswordSalt());
        String hash = hasher.hash(attempt, salt);
        return hash.equals(account.getPasswordHash());
    }
}
