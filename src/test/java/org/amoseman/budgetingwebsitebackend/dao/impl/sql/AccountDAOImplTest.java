package org.amoseman.budgetingwebsitebackend.dao.impl.sql;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.UpdateAccount;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountDAOImplTest {

    @Test
    void testCRUD() {
        String databaseURL = "jdbc:sqlite:test.db";
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        AccountDAO<DSLContext> accountDAO = new AccountDAOImpl(connection);
        LocalDateTime now = Now.get();
        Account account = new Account(
                "12345",
                now,
                now,
                "password_hash",
                "password_salt",
                "USER"
        );
        try {
            accountDAO.addAccount(account);
        }
        catch (UserAlreadyExistsException e) {
            fail(e);
        }
        Optional<Account> maybe = accountDAO.getAccount("12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve added account");
        }
        Account retrieved = maybe.get();
        assertEquals("password_hash", retrieved.hash);
        assertEquals("password_salt", retrieved.salt);
        assertEquals("USER", retrieved.roles);
        Account update = new Account(account, new UpdateAccount("uuid", "new_password_hash", "new_password_salt", "USER,ADMIN"), Now.get());
        try {
            accountDAO.updateAccount(update);
        }
        catch (UserDoesNotExistException e) {
            fail(e);
        }
        maybe = accountDAO.getAccount("12345");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve updated account");
        }
        retrieved = maybe.get();
        assertEquals("new_password_hash", retrieved.hash);
        assertEquals("new_password_salt", retrieved.salt);
        assertEquals("USER,ADMIN", retrieved.roles);
        try {
            accountDAO.removeAccount("12345");
        }
        catch (UserDoesNotExistException e) {
            fail(e);
        }
        maybe = accountDAO.getAccount("12345");
        if (maybe.isPresent()) {
            fail("Retrieved account when it should have been deleted");
        }
    }
}