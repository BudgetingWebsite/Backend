package org.amoseman.budgetingbackend.service;

import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.amoseman.budgetingbackend.application.auth.Roles;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.pojo.account.Account;
import org.amoseman.budgetingbackend.pojo.account.op.CreateAccount;
import org.amoseman.budgetingbackend.pojo.account.op.UpdateAccount;
import org.amoseman.budgetingbackend.util.Now;
import org.bouncycastle.util.encoders.Base64;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * A service for account logic.
 * @param <C> the client type.
 */
public class AccountService<C> {
    private final AccountDAO<C> accountDAO;
    private final Hash hash;

    /**
     * Instantiate a new account service.
     * @param accountDAO the account data access object to use.
     * @param hash what to use for password hashing.
     */
    public AccountService(AccountDAO<C> accountDAO, Hash hash) {
        this.accountDAO = accountDAO;
        this.hash = hash;
    }

    /**
     * Add a new account.
     * @param usernamePassword the username and password of the new account.
     * @throws AccountAlreadyExistsException if the username is already in use.
     */
    public void addAccount(CreateAccount usernamePassword) throws AccountAlreadyExistsException {
        byte[] salt = hash.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hash.hash(usernamePassword.getPassword(), salt);
        Account account = new Account(usernamePassword.getUsername(), Now.get(), Now.get(), hash64, salt64, Roles.USER);
        accountDAO.addAccount(account);
    }

    /**
     * Remove an account.
     * @param username the username of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     */
    public void removeAccount(String username) throws AccountDoesNotExistException {
        accountDAO.removeAccount(username);
    }

    /**
     * Get an account.
     * @param username the username of the account.
     * @return the account.
     */
    public Optional<Account> getAccount(String username) {
        return accountDAO.getAccount(username);
    }

    /**
     * Change the password of an account.
     * @param username the username of the account.
     * @param password the new password of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     */
    public void changePassword(String username, String password) throws AccountDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new AccountDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        byte[] salt = hash.salt();
        String salt64 = Base64.toBase64String(salt);
        String hash64 = hash.hash(password, salt);
        UpdateAccount update = new UpdateAccount(username, hash64, salt64, account.roles);
        account = new Account(account, update, now);
        accountDAO.updateAccount(account);
    }

    /**
     * Change the roles of an account.
     * The roles should be comma-delimited with no whitespaces and are case-sensitive.
     * Valid roles are ADMIN and USER.
     * @param username the username of the account.
     * @param roles the new roles of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     */
    public void changeRoles(String username, String roles) throws AccountDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Account> maybe = accountDAO.getAccount(username);
        if (maybe.isEmpty()) {
            throw new AccountDoesNotExistException("change password", username);
        }
        Account account = maybe.get();
        UpdateAccount update = new UpdateAccount(username, account.hash, account.salt, roles);
        account = new Account(account, update, now);
        accountDAO.updateAccount(account);
    }
}
