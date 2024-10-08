package org.amoseman.budgetingbackend.dao;

import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.model.account.Account;

import java.util.Optional;

/**
 * Represents a data access object for a user account.
 * @param <C> the client type.
 */
public abstract class AccountDAO<C> extends DAO<C> {
    /**
     * Instantiate an account data access object.
     * @param connection the database connection to use.
     */
    public AccountDAO(DatabaseConnection<C> connection) {
        super(connection);
    }

    /**
     * Add a new user account.
     * @param account the user account.
     * @throws AccountAlreadyExistsException if the username is already in use.
     */
    public abstract void addAccount(Account account) throws AccountAlreadyExistsException;

    /**
     * Remove a user account.
     * @param uuid the UUID of the account.
     * @throws AccountDoesNotExistException if the account does not exist.
     */
    public abstract void removeAccount(String uuid) throws AccountDoesNotExistException;

    /**
     * Get the account of a user.
     * @param uuid the UUID of the account.
     * @return the account.
     */
    public abstract Optional<Account> getAccount(String uuid) ;

    /**
     * Update the account of a user.
     * @param account the account.
     */
    public abstract void updateAccount(Account account) throws AccountDoesNotExistException;
}
