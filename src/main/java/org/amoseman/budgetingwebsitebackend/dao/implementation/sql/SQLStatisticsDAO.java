package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.StatisticsDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLStatisticsDAO extends StatisticsDAO<DSLContext> {
    public SQLStatisticsDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public long totalFunds(String user) {
        Result<Record> result = connection.get()
                .selectFrom(table("partitions"))
                .where(field("owner").eq(user))
                .fetch();
        long total = 0;
        for (Record record : result) {
            long amount = record.get(field("amount"), Long.class);
            total += amount;
        }
        return total;
    }
}
