package org.amoseman.budgetingbackend.service;

import org.amoseman.budgetingbackend.application.BudgetingConfiguration;
import org.amoseman.budgetingbackend.application.auth.hashing.Hash;
import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.exception.UsernameExceedsMaxLengthException;
import org.amoseman.budgetingbackend.exception.InvalidPasswordException;
import org.amoseman.budgetingbackend.model.account.Account;
import org.amoseman.budgetingbackend.model.account.op.CreateAccount;
import org.amoseman.budgetingbackend.password.PasswordChecker;

import java.util.Optional;

/**
 * A service for account logic.
 * @param <C> the client type.
 */
public abstract class AccountService<C> {
    protected final BudgetingConfiguration configuration;
    protected final AccountDAO<C> accountDAO;
    protected final Hash hash;
    protected final PasswordChecker passwordChecker;

    /**
     * Instantiate a new account service.
     * @param configuration the configuration of the budgeting service.
     * @param accountDAO the account data access object to use.
     * @param hash what to use for password hashing.
     */
    public AccountService(BudgetingConfiguration configuration, AccountDAO<C> accountDAO, Hash hash) {
        this.configuration = configuration;
        this.accountDAO = accountDAO;
        this.hash = hash;
        this.passwordChecker = new PasswordChecker(configuration);
    }

    /**
     * Add a new account.
     * @param usernamePassword the username and password of the new account.
     * @throws AccountAlreadyExistsException if the username is already in use.
     * @throws InvalidPasswordException if the password is too weak.
     */
    public abstract void addAccount(CreateAccount usernamePassword) throws AccountAlreadyExistsException, UsernameExceedsMaxLengthException, InvalidPasswordException;

    /**
     * Remove an account.
     * @param username the username of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     */
    public abstract void removeAccount(String username) throws AccountDoesNotExistException;
    /**
     * Get an account.
     * @param username the username of the account.
     * @return the account.
     */
    public abstract Optional<Account> getAccount(String username);

    /**
     * Change the password of an account.
     * @param username the username of the account.
     * @param password the new password of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     * @throws InvalidPasswordException if the password is too weak.
     */
    public abstract void changePassword(String username, String password) throws AccountDoesNotExistException, InvalidPasswordException;

    /**
     * Change the roles of an account.
     * The roles should be comma-delimited with no whitespaces and are case-sensitive.
     * Valid roles are ADMIN and USER.
     * @param username the username of the account.
     * @param roles the new roles of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     */
    public abstract void changeRoles(String username, String roles) throws AccountDoesNotExistException;
}
