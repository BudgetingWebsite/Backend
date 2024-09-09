package org.amoseman.budgetingwebsitebackend.dao;

import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Account;

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
     * @throws UserAlreadyExistsException if the username is already in use.
     */
    public abstract void addAccount(Account account) throws UserAlreadyExistsException;

    /**
     * Remove a user account.
     * @param username the username of the account.
     * @throws UserDoesNotExistException if the account does not exist.
     */
    public abstract void removeAccount(String username) throws UserDoesNotExistException;

    /**
     * Get the account of a user.
     * @param username the username of the account.
     * @return the account.
     * @throws UserDoesNotExistException if the account does not exist.
     */
    public abstract Account getAccount(String username) throws UserDoesNotExistException;
}
