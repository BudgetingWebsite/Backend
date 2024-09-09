package org.amoseman.budgetingwebsitebackend.database.implementation;

import org.amoseman.budgetingwebsitebackend.application.BudgetingConfiguration;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.database.DatabaseInitializer;
import org.jooq.DSLContext;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.primaryKey;
import static org.jooq.impl.SQLDataType.*;

public class SQLDatabaseInitializer extends DatabaseInitializer<DSLContext> {
    public SQLDatabaseInitializer(DatabaseConnection<DSLContext> connection, BudgetingConfiguration configuration) {
        super(connection, configuration);
    }

    @Override
    public void initialize() {
        initAccountsTable();
        initIncomeEventsTable();
        initExpenseEventsTable();
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
                .createTableIfNotExists("income")
                .column(field("id"), VARCHAR)
                .column(field("user"), VARCHAR)
                .column(field("amount"), VARCHAR)
                .column(field("created"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("id"))
                )
                .execute();
    }

    private void initExpenseEventsTable() {
        connection.get()
                .createTableIfNotExists("expenses")
                .column(field("id"), VARCHAR)
                .column(field("user"), VARCHAR)
                .column(field("amount"), VARCHAR)
                .column(field("created"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("id"))
                )
                .execute();
    }

    private void initPartitionsTable() {
        connection.get()
                .createTableIfNotExists("partitions")
                .column(field("id"), VARCHAR)
                .column(field("user"), VARCHAR)
                .column(field("share"), DOUBLE)
                .column(field("amount"), BIGINT)
                .column(field("created"), LOCALDATETIME)
                .column(field("updated"), LOCALDATETIME)
                .constraints(
                        primaryKey(field("id"))
                )
                .execute();
    }
}
