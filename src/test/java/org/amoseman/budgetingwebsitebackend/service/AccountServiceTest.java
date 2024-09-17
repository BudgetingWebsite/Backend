package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.InitTestDatabase;
import org.amoseman.budgetingwebsitebackend.application.auth.hashing.ArgonHasher;
import org.amoseman.budgetingwebsitebackend.application.auth.hashing.Hasher;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.dao.impl.sql.AccountDAOImpl;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.impl.sql.sqlite.DatabaseConnectionImpl;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;
import org.amoseman.budgetingwebsitebackend.pojo.account.op.CreateAccount;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {
    private static AccountService<DSLContext> accountService;

    @BeforeEach
    void setup() {
        SecureRandom random = new SecureRandom();
        Hasher hasher = new ArgonHasher(random, 16, 16, 2, 8000, 1);
        String databaseURL = "jdbc:sqlite:test.db";
        InitTestDatabase.init(databaseURL, "schema.sql");
        DatabaseConnection<DSLContext> connection = new DatabaseConnectionImpl(databaseURL);
        AccountDAO<DSLContext> accountDAO = new AccountDAOImpl(connection);
        accountService = new AccountService<>(accountDAO, hasher);
    }

    @Test
    void testCRUD() {
        try {
            accountService.addAccount(new CreateAccount("alice", "12345"));
        }
        catch (UserAlreadyExistsException e) {
            fail(e);
        }
        Optional<Account> maybe =  accountService.getAccount("alice");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve account after it was added");
        }
        Account account = maybe.get();
        assertEquals("alice", account.uuid);
        assertEquals("USER", account.roles);

        try {
            accountService.changePassword("alice", "54321");
        }
        catch (UserDoesNotExistException e) {
            fail(e);
        }
        maybe = accountService.getAccount("alice");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve account after it's password was changed");
        }
        Account updated = maybe.get();
        assertNotEquals(account.hash, updated.hash);
        assertNotEquals(account.salt, updated.salt);

        try {
            accountService.changeRoles("alice", "USER,ADMIN");
        }
        catch (UserDoesNotExistException e) {
            fail(e);
        }
        maybe = accountService.getAccount("alice");
        if (maybe.isEmpty()) {
            fail("Unable to retrieve account after it's roles were changed");
        }
        updated = maybe.get();
        assertNotEquals(account.roles, updated.roles);

        try {
            accountService.removeAccount("alice");
        }
        catch (UserDoesNotExistException e) {
            fail(e);
        }
        maybe = accountService.getAccount("alice");
        if (maybe.isPresent()) {
            fail("Able to retrieve account after it should have been deleted");
        }

        InitTestDatabase.close("jdbc:sqlite:test.db");
    }
}