package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.application.auth.Hasher;
import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.CreateAccount;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.UpdateAccount;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.bouncycastle.util.encoders.Base64;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class AccountService<C> {
    private final AccountDAO<C> accountDAO;
    private final Hasher hasher;

    public AccountService(AccountDAO<C> accountDAO, Hasher hasher) {
        this.accountDAO = accountDAO;
        this.hasher = hasher;
    }

    public void addAccount(CreateAccount usernamePassword) throws UserAlreadyExistsException {
        byte[] salt = hasher.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hasher.hash(usernamePassword.getPassword(), salt);
        Account account = new Account(usernamePassword.getUsername(), Now.get(), Now.get(), hash64, salt64, Roles.USER);
        accountDAO.addAccount(account);
    }

    public void removeAccount(String username) throws UserDoesNotExistException {
        accountDAO.removeAccount(username);
    }

    public Optional<Account> getAccount(String username) {
        return accountDAO.getAccount(username);
    }

    public void changePassword(String username, String password) throws UserDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new UserDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        byte[] salt = hasher.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hasher.hash(password, salt);
        UpdateAccount update = new UpdateAccount(username, hash64, salt64, account.roles);
        account = new Account(account, update, now);
        accountDAO.updateAccount(account);
    }

    public void changeRoles(String username, String roles) throws UserDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new UserDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        UpdateAccount update = new UpdateAccount(username, account.hash, account.salt, roles);
        account = new Account(account, update, now);
        accountDAO.updateAccount(account);
    }
}
