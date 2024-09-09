package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.TimeRange;
import org.jooq.*;
import org.jooq.Record;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLFinanceEventDAO extends FinanceEventDAO<DSLContext> {
    public SQLFinanceEventDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    private Table<Record> getTable(String type) {
        return table(String.format("%s-events", type));
    }

    private FinanceEvent fromRecord(Record record) {
        try {
            return new FinanceEvent(
                    record.get(field("id"), String.class),
                    record.get(field("created"), LocalDateTime.class),
                    record.get(field("user"), String.class),
                    record.get(field("amount"), Long.class),
                    record.get(field("type"), String.class),
                    record.get(field("when"), LocalDateTime.class)
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
                            field("id"),
                            field("user"),
                            field("amount"),
                            field("when"),
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
            throw new FinanceEventAlreadyExistsException("add", event.getIdentifier());
        }
    }

    @Override
    public void removeEvent(String id, String type) throws FinanceEventDoesNotExistException {
        int result = connection.get()
                .deleteFrom(getTable(type))
                .where(field("id").eq(id))
                .execute();
        if (0 == result) {
            throw new FinanceEventDoesNotExistException("remove", id);
        }
    }

    @Override
    public List<FinanceEvent> getEvents(String user, String type) {
        Result<Record> result = connection.get()
                .selectFrom(getTable(type))
                .where(field("user").eq(user))
                .fetch();
        return fromRecords(result);
    }

    @Override
    public List<FinanceEvent> getEvents(String user, String type, TimeRange range) {
        Condition rangeCondition = field("when").greaterOrEqual(range.getStart()).and(field("when").lessOrEqual(range));
        Result<Record> result = connection.get()
                .selectFrom(getTable(type))
                .where(field("user").eq(user).and(rangeCondition))
                .fetch();
        return fromRecords(result);
    }
}
