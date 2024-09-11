package org.amoseman.budgetingwebsitebackend.database.implementation;

import org.amoseman.budgetingwebsitebackend.application.BudgetingConfiguration;
import org.amoseman.budgetingwebsitebackend.application.auth.Hasher;
import org.amoseman.budgetingwebsitebackend.application.auth.Roles;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.DatabaseInitializer;
import org.amoseman.budgetingwebsitebackend.time.Now;
import org.bouncycastle.util.encoders.Base64;
import org.jooq.DSLContext;

import java.time.LocalDateTime;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

public class SQLDatabaseInitializer extends DatabaseInitializer<DSLContext> {
    private final Hasher hasher;

    public SQLDatabaseInitializer(DatabaseConnection<DSLContext> connection, BudgetingConfiguration configuration, Hasher hasher) {
        super(connection, configuration);
        this.hasher = hasher;
    }

    @Override
    public void initialize() {
        initAccountsTable();
        initIncomeEventsTable();
        initExpenseEventsTable();
        initPartitionsTable();
        initAdminAccount();
    }

    private void initAccountsTable() {
        connection.get()
                .createTableIfNotExists("accounts")
                .column(field("username"), VARCHAR(configuration.getMaxUsernameLength()))
                .column(field("roles"), VARCHAR)
                .column(field("hash"), VARCHAR)
                .column(field("salt"), VARCHAR)
                .column(field("created"), LOCALDATETIME)
                .column(field("updated"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("username"))
                )
                .execute();
    }

    private void initIncomeEventsTable() {
        connection.get()
                .createTableIfNotExists("income_events")
                .column(field("uuid"), VARCHAR)
                .column(field("owner"), VARCHAR)
                .column(field("amount"), VARCHAR)
                .column(field("occurred"), LOCALDATETIME)
                .column(field("category"), VARCHAR)
                .column(field("description"), VARCHAR)
                .column(field("created"), LOCALDATETIME)
                .column(field("updated"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("uuid"))
                )
                .execute();
    }

    private void initExpenseEventsTable() {
        connection.get()
                .createTableIfNotExists("expense_events")
                .column(field("uuid"), VARCHAR)
                .column(field("owner"), VARCHAR)
                .column(field("amount"), VARCHAR)
                .column(field("occurred"), VARCHAR)
                .column(field("category"), VARCHAR)
                .column(field("description"), VARCHAR)
                .column(field("partition"), VARCHAR)
                .column(field("created"), LOCALDATETIME)
                .column(field("updated"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("uuid")),
                        foreignKey(field("partition")).references(table("partitions"))
                )
                .execute();
    }

    private void initPartitionsTable() {
        connection.get()
                .createTableIfNotExists("partitions")
                .column(field("uuid"), VARCHAR)
                .column(field("owner"), VARCHAR)
                .column(field("share"), DOUBLE)
                .column(field("amount"), BIGINT)
                .column(field("created"), LOCALDATETIME)
                .column(field("updated"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("uuid"))
                )
                .execute();
    }

    private void initAdminAccount() {
        String username = configuration.getAdminUsername();
        boolean exists = connection.get()
                .selectFrom(table("accounts"))
                .where(field("username").eq(username))
                .fetch()
                .isNotEmpty();
        if (exists) {
            return;
        }
        String password = configuration.getAdminPassword();
        byte[] salt = hasher.salt();
        String hash64 = hasher.hash(password, salt);
        String salt64 = Base64.toBase64String(salt);
        LocalDateTime now = Now.get();
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
                        username,
                        Roles.ADMIN,
                        hash64,
                        salt64,
                        now,
                        now
                )
                .execute();
    }
}
