package org.amoseman.budgetingwebsitebackend.application.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;
import org.bouncycastle.util.encoders.Base64;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserAuthenticatorTest {

    @Test
    void authenticate() {
        Hasher hasher = new Hasher(new SecureRandom(), 16, 16, 2, 8192, 1);
        String password = "password";
        byte[] salt = hasher.salt();
        String hash64 = hasher.hash(password, salt);
        String salt64 = Base64.toBase64String(salt);

        AccountDAO<?> accountDAO = Mockito.mock(AccountDAO.class);
        Mockito.when(accountDAO.getAccount("alice")).thenReturn(Optional.of(new Account("alice", null, null, hash64, salt64, Roles.USER)));
        UserAuthenticator authenticator = new UserAuthenticator(accountDAO, hasher);
        try {
            Optional<User> user = authenticator.authenticate(new BasicCredentials("alice", "password"));
            if (user.isEmpty()) {
                fail("Failed to authenticate");
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }
}