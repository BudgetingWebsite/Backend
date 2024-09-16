package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.account.Account;
import org.jooq.*;
import org.jooq.Record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLAccountDAO extends AccountDAO<DSLContext> {
    private static final Table<Record> ACCOUNT_TABLE = table("account");
    private static final Field<String> UUID_FIELD = field("uuid", String.class);
    private static final Field<String> ROLES_FIELD = field("roles", String.class);
    private static final Field<String> HASH_FIELD = field("hash", String.class);
    private static final Field<String> SALT_FIELD = field("salt", String.class);
    private static final Field<LocalDateTime> CREATED_FIELD = field("created", LocalDateTime.class);
    private static final Field<LocalDateTime> UPDATED_FIELD = field("updated", LocalDateTime.class);

    public SQLAccountDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addAccount(Account account) throws UserAlreadyExistsException {
        try {
            Record record = connection.get().newRecord(ACCOUNT_TABLE, account);
            connection.get().executeInsert((TableRecord<?>) record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new UserAlreadyExistsException("add", account.uuid);
        }
    }

    @Override
    public void removeAccount(String uuid) throws UserDoesNotExistException {
        int result = connection.get()
                .deleteFrom(ACCOUNT_TABLE)
                .where(UUID_FIELD.eq(uuid))
                .execute();
        if (0 == result) {
            throw new UserDoesNotExistException("remove", uuid);
        }
    }

    @Override
    public Optional<Account> getAccount(String uuid) {
        List<Account> list = connection.get()
                .selectFrom(ACCOUNT_TABLE)
                .where(UUID_FIELD.eq(uuid))
                .fetch()
                .into(Account.class);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public void updateAccount(Account account) throws UserDoesNotExistException{
        Record record = connection.get().newRecord(ACCOUNT_TABLE, account);
        int result = connection.get().executeUpdate((UpdatableRecord<?>) record);
        if (0 == result) {
            throw new UserDoesNotExistException("update", account.uuid);
        }
    }
}
