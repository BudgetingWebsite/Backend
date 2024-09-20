package org.amoseman.budgetingbackend.dao.impl.sql;

import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.exception.AccountAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.AccountDoesNotExistException;
import org.amoseman.budgetingbackend.pojo.account.Account;
import org.jooq.*;
import java.util.List;
import java.util.Optional;

import static org.jooq.codegen.Tables.*;
import org.jooq.codegen.tables.records.*;
import org.jooq.exception.DataAccessException;

public class AccountDAOImpl extends AccountDAO<DSLContext> {
    public AccountDAOImpl(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addAccount(Account account) throws AccountAlreadyExistsException, DataAccessException {
        if (getAccount(account.uuid).isPresent()) {
            throw new AccountAlreadyExistsException("add", account.uuid);
        }
        AccountRecord record = connection.get().newRecord(ACCOUNT, account);
        connection.get().executeInsert(record);
    }

    @Override
    public void removeAccount(String uuid) throws AccountDoesNotExistException {
        int result = connection.get()
                .deleteFrom(ACCOUNT)
                .where(ACCOUNT.UUID.eq(uuid))
                .execute();
        if (0 == result) {
            throw new AccountDoesNotExistException("remove", uuid);
        }
    }

    @Override
    public Optional<Account> getAccount(String uuid) {
        List<Account> list = connection.get()
                .selectFrom(ACCOUNT)
                .where(ACCOUNT.UUID.eq(uuid))
                .fetch()
                .into(Account.class);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public void updateAccount(Account account) throws AccountDoesNotExistException {
        AccountRecord record = connection.get().newRecord(ACCOUNT, account);
        int result = connection.get().executeUpdate(record, ACCOUNT.UUID.eq(account.uuid));
        if (0 == result) {
            throw new AccountDoesNotExistException("update", account.uuid);
        }
    }
}
