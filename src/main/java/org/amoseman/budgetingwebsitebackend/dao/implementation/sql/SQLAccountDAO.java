package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.dao.AccountDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.UserAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.UserDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Account;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLAccountDAO extends AccountDAO<DSLContext> {
    public SQLAccountDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addAccount(Account account) throws UserAlreadyExistsException {
        try {
            connection.get()
                    .insertInto(
                            table("accounts"),
                            field("username"),
                            field("roles"),
                            field("hash"),
                            field("salt"),
                            field("created"),
                            field("updated")
                    )
                    .values(
                            account.getIdentifier(),
                            Roles.asString(account.getRoles()),
                            account.getPasswordHash(),
                            account.getPasswordSalt(),
                            account.getCreated(),
                            account.getUpdated()
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new UserAlreadyExistsException("add", account.getIdentifier());
        }
    }

    @Override
    public void removeAccount(String username) throws UserDoesNotExistException {
        int result = connection.get()
                .deleteFrom(table("accounts"))
                .where(field("username").eq(username))
                .execute();
        if (0 == result) {
            throw new UserDoesNotExistException("remove", username);
        }
    }

    @Override
    public Optional<Account> getAccount(String username) {
        Result<Record> result = connection.get()
                .selectFrom(table("accounts"))
                .where(field("username").eq(username))
                .fetch();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Record record = result.get(0);
        return Optional.of(new Account(
                record.get(field("username"), String.class),
                record.get(field("created"), LocalDateTime.class),
                record.get(field("updated"), LocalDateTime.class),
                record.get(field("hash"), String.class),
                record.get(field("salt"), String.class),
                Roles.fromString(record.get(field("roles"), String.class))
                ));
    }

    @Override
    public void updateAccount(Account account) throws UserDoesNotExistException{
        int result = connection.get()
                .update(table("accounts"))
                .set(field("roles"), Roles.asString(account.getRoles()))
                .set(field("hash"), account.getPasswordHash())
                .set(field("salt"), account.getPasswordSalt())
                .set(field("updated"), account.getUpdated())
                .where(field("username").eq(account.getIdentifier()))
                .execute();
        if (0 == result) {
            throw new UserDoesNotExistException("update", account.getIdentifier());
        }
    }
}
