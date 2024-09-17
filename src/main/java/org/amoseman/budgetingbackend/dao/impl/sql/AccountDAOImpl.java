package org.amoseman.budgetingbackend.dao.impl.sql;

import org.amoseman.budgetingbackend.dao.AccountDAO;
import org.amoseman.budgetingbackend.database.DatabaseConnection;
import org.amoseman.budgetingbackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingbackend.pojo.account.Account;
import org.jooq.*;
import java.util.List;
import java.util.Optional;

import static org.jooq.codegen.Tables.*;
import org.jooq.codegen.tables.records.*;

public class AccountDAOImpl extends AccountDAO<DSLContext> {
    public AccountDAOImpl(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addAccount(Account account) throws UserAlreadyExistsException {
        try {
            AccountRecord record = connection.get().newRecord(ACCOUNT, account);
            connection.get().executeInsert(record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new UserAlreadyExistsException("add", account.uuid);
        }
    }

    @Override
    public void removeAccount(String uuid) throws UserDoesNotExistException {
        int result = connection.get()
                .deleteFrom(ACCOUNT)
                .where(ACCOUNT.UUID.eq(uuid))
                .execute();
        if (0 == result) {
            throw new UserDoesNotExistException("remove", uuid);
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
    public void updateAccount(Account account) throws UserDoesNotExistException{
        AccountRecord record = connection.get().newRecord(ACCOUNT, account);
        int result = connection.get().executeUpdate(record);
        if (0 == result) {
            throw new UserDoesNotExistException("update", account.uuid);
        }
    }
}
