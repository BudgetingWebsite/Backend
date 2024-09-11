package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
import org.amoseman.budgetingwebsitebackend.pojo.event.ExpenseEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.IncomeEvent;
import org.jooq.*;
import org.jooq.Record;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLFinanceEventDAO extends FinanceEventDAO<DSLContext> {
    public SQLFinanceEventDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    private Table<Record> getTable(String type) {
        return table(String.format("%s_events", type));
    }

    private FinanceEvent fromRecord(String type, Record record) {
        try {
            if ("income".equals(type)) {
                return new IncomeEvent(
                        record.get(field("uuid"), String.class),
                        record.get(field("created"), Timestamp.class).toLocalDateTime(),
                        record.get(field("updated"), Timestamp.class).toLocalDateTime(),
                        record.get(field("owner"), String.class),
                        record.get(field("amount"), Long.class),
                        record.get(field("occurred"), Timestamp.class).toLocalDateTime(),
                        record.get(field("category"), String.class),
                        record.get(field("description"), String.class)
                );
            }
            else {
                return new ExpenseEvent(
                        record.get(field("uuid"), String.class),
                        record.get(field("created"), Timestamp.class).toLocalDateTime(),
                        record.get(field("updated"), Timestamp.class).toLocalDateTime(),
                        record.get(field("owner"), String.class),
                        record.get(field("amount"), Long.class),
                        record.get(field("occurred"), Timestamp.class).toLocalDateTime(),
                        record.get(field("category"), String.class),
                        record.get(field("description"), String.class),
                        record.get(field("partition"), String.class)
                        );
            }


        }
        catch (NegativeValueException | InvalidFinanceEventTypeException e) {
            throw new RuntimeException(e);
        }
    }

    private List<FinanceEvent> fromRecords(String type, Result<Record> results) {
        List<FinanceEvent> events = new ArrayList<>();
        results.forEach(record -> events.add(fromRecord(type, record)));
        return events;
    }

    @Override
    public void addEvent(IncomeEvent event) throws FinanceEventAlreadyExistsException {
        try {
            connection.get()
                    .insertInto(
                            getTable(event.getType()),
                            field("uuid"),
                            field("owner"),
                            field("amount"),
                            field("occurred"),
                            field("category"),
                            field("description"),
                            field("created"),
                            field("updated")
                    )
                    .values(
                            event.getUuid(),
                            event.getOwner(),
                            event.getAmount(),
                            event.getOccurred(),
                            event.getCategory(),
                            event.getDescription(),
                            event.getCreated(),
                            event.getUpdated()
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new FinanceEventAlreadyExistsException("add", event.getUuid());
        }
    }

    @Override
    public void addEvent(ExpenseEvent event) throws FinanceEventAlreadyExistsException {
        try {
            connection.get()
                    .insertInto(
                            getTable(event.getType()),
                            field("uuid"),
                            field("owner"),
                            field("amount"),
                            field("occurred"),
                            field("category"),
                            field("description"),
                            field("partition"),
                            field("created"),
                            field("updated")
                    )
                    .values(
                            event.getUuid(),
                            event.getOwner(),
                            event.getAmount(),
                            event.getOccurred(),
                            event.getCategory(),
                            event.getDescription(),
                            event.getPartition(),
                            event.getCreated(),
                            event.getUpdated()
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new FinanceEventAlreadyExistsException("add", event.getUuid());
        }
    }

    @Override
    public void removeEvent(String user, String id, String type) throws FinanceEventDoesNotExistException {
        int result = connection.get()
                .deleteFrom(getTable(type))
                .where(field("uuid").eq(id).and(field("owner").eq(user)))
                .execute();
        if (0 == result) {
            throw new FinanceEventDoesNotExistException("remove", id);
        }
    }

    @Override
    public List<FinanceEvent> getEvents(String user, String type) {
        Result<Record> result = connection.get()
                .selectFrom(getTable(type))
                .where(field("owner").eq(user))
                .fetch();
        return fromRecords(type, result);
    }

    @Override
    public List<FinanceEvent> getEvents(String user, String type, TimeRange range) {
        Condition rangeCondition = field("occurred").greaterOrEqual(range.getStart()).and(field("occurred").lessOrEqual(range.getEnd()));
        Result<Record> result = connection.get()
                .selectFrom(getTable(type))
                .where(field("owner").eq(user).and(rangeCondition))
                .fetch();
        return fromRecords(type, result);
    }
}
