package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.application.auth.Hasher;
import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Account;
import org.amoseman.budgetingwebsitebackend.pojo.CreateAccount;
import org.amoseman.budgetingwebsitebackend.pojo.update.AccountUpdate;
import org.amoseman.budgetingwebsitebackend.time.Now;
import org.bouncycastle.util.encoders.Base64;

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
        Account account = new Account(usernamePassword.getUsername(), Now.get(), Now.get(), hash64, salt64, Set.of(Roles.USER));
        accountDAO.addAccount(account);
    }

    public void removeAccount(String username) throws UserDoesNotExistException {
        accountDAO.removeAccount(username);
    }

    public Optional<Account> getAccount(String username) {
        return accountDAO.getAccount(username);
    }

    public void changePassword(String username, String password) throws UserDoesNotExistException {
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new UserDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        byte[] salt = hasher.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hasher.hash(password, salt);
        account.update(new AccountUpdate(Now.get(), hash64, salt64, account.getRoles()));
        accountDAO.updateAccount(account);
    }

    public void changeRoles(String username, Set<String> roles) throws UserDoesNotExistException {
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new UserDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        account.update(new AccountUpdate(Now.get(), account.getPasswordHash(), account.getPasswordSalt(), roles));
        accountDAO.updateAccount(account);
    }
}
