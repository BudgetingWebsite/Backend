package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.application.auth.Hashing;
import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Account;
import org.amoseman.budgetingwebsitebackend.pojo.CreateAccount;
import org.amoseman.budgetingwebsitebackend.time.Now;
import org.bouncycastle.util.encoders.Base64;

import java.security.SecureRandom;
import java.util.Set;

public class AccountService<C> {
    private final AccountDAO<C> accountDAO;
    private final SecureRandom random;

    public AccountService(AccountDAO<C> accountDAO, SecureRandom random) {
        this.accountDAO = accountDAO;
        this.random = random;
    }

    public void addAccount(CreateAccount usernamePassword) throws UserAlreadyExistsException {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String salt64 = Base64.toBase64String(salt);
        String hash64 = Hashing.hash(16, usernamePassword.getPassword(), salt);
        Account account = new Account(usernamePassword.getUsername(), Now.get(), Now.get(), hash64, salt64, Set.of(Roles.USER));
        accountDAO.addAccount(account);
    }

    public void removeAccount(String username) throws UserDoesNotExistException {
        accountDAO.removeAccount(username);
    }
}
