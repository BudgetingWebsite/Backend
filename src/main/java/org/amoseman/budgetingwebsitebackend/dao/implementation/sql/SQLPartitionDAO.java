package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Partition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class SQLPartitionDAO extends PartitionDAO<DSLContext> {
    public SQLPartitionDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addPartition(Partition partition) throws PartitionAlreadyExistsException {
        try {
            connection.get()
                    .insertInto(
                            table("partitions"),
                            field("id"),
                            field("owner"),
                            field("share"),
                            field("amount"),
                            field("created"),
                            field("updated")
                    )
                    .values(
                            partition.getIdentifier(),
                            partition.getOwner(),
                            partition.getShare(),
                            partition.getAmount(),
                            partition.getCreated(),
                            partition.getUpdated()
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new PartitionAlreadyExistsException("add", partition.getIdentifier());
        }
    }

    @Override
    public void removePartition(String owner, String id) throws PartitionDoesNotExistException {
        int result = connection.get()
                .deleteFrom(table("partitions"))
                .where(field("id").eq(id).and(field("owner").eq(owner)))
                .execute();
        if (0 == result) {
            throw new PartitionDoesNotExistException("remove", id);
        }
    }

    @Override
    public void updatePartition(Partition partition) throws PartitionDoesNotExistException {
        int result = connection.get()
                .update(table("partitions"))
                .set(field("share"), partition.getShare())
                .set(field("amount"), partition.getAmount())
                .set(field("updated"), partition.getUpdated())
                .where(field("id").eq(partition.getIdentifier()))
                .execute();
        if (0 == result) {
            throw new PartitionDoesNotExistException("update", partition.getIdentifier());
        }
    }

    @Override
    public Optional<Partition> getPartition(String owner, String id) {
        Result<Record> result = connection.get()
                .selectFrom(table("partitions"))
                .where(field("id").eq(id).and(field("owner").eq(owner)))
                .fetch();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        Record record = result.get(0);
        return Optional.of(asPartition(record));
    }

    @Override
    public List<Partition> listPartitions(String owner) {
        Result<Record> result = connection.get()
                .selectFrom(table("partitions"))
                .where(field("owner").eq(owner))
                .fetch();
        List<Partition> partitions = new ArrayList<>();
        result.forEach(record -> partitions.add(asPartition(record)));
        return partitions;
    }

    private Partition asPartition(Record record) {
        return new Partition(
                record.get(field("id"), String.class),
                record.get(field("created"), LocalDateTime.class),
                record.get(field("updated"), LocalDateTime.class),
                record.get(field("owner"), String.class),
                record.get(field("share"), Double.class),
                record.get(field("amount"), Long.class)
        );
    }
}
