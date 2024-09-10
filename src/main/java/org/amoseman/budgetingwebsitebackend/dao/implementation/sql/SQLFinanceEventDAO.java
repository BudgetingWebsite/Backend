package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
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

    private FinanceEvent fromRecord(Record record) {
        try {
            return new FinanceEvent(
                    record.get(field("uuid"), String.class),
                    record.get(field("created"), Timestamp.class).toLocalDateTime(),
                    record.get(field("username"), String.class),
                    record.get(field("amount"), Long.class),
                    record.get(field("type"), String.class),
                    record.get(field("occurred"), Timestamp.class).toLocalDateTime()
            );
        }
        catch (NegativeValueException | InvalidFinanceEventTypeException e) {
            throw new RuntimeException(e);
        }
    }

    private List<FinanceEvent> fromRecords(Result<Record> results) {
        List<FinanceEvent> events = new ArrayList<>();
        results.forEach(record -> events.add(fromRecord(record)));
        return events;
    }

    @Override
    public void addEvent(FinanceEvent event) throws FinanceEventAlreadyExistsException {
        try {
            connection.get()
                    .insertInto(
                            getTable(event.getType()),
                            field("uuid"),
                            field("username"),
                            field("amount"),
                            field("occurred"),
                            field("created")
                    )
                    .values(
                            event.getIdentifier(),
                            event.getUser(),
                            event.getAmount(),
                            event.getWhen(),
                            event.getCreated()
                    )
                    .execute();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FinanceEventAlreadyExistsException("add", event.getIdentifier());
        }
    }

    @Override
    public void removeEvent(String user, String id, String type) throws FinanceEventDoesNotExistException {
        int result = connection.get()
                .deleteFrom(getTable(type))
                .where(field("uuid").eq(id).and(field("username").eq(user)))
                .execute();
        if (0 == result) {
            throw new FinanceEventDoesNotExistException("remove", id);
        }
    }

    @Override
    public List<FinanceEvent> getEvents(String user, String type) {
        Result<Record> result = connection.get()
                .selectFrom(getTable(type))
                .where(field("username").eq(user))
                .fetch();
        return fromRecords(result);
    }

    @Override
    public List<FinanceEvent> getEvents(String user, String type, TimeRange range) {
        Condition rangeCondition = field("occurred").greaterOrEqual(range.getStart()).and(field("occurred").lessOrEqual(range));
        Result<Record> result = connection.get()
                .selectFrom(getTable(type))
                .where(field("username").eq(user).and(rangeCondition))
                .fetch();
        return fromRecords(result);
    }
}
