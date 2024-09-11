package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

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
                            field("uuid"),
                            field("owner"),
                            field("share"),
                            field("amount"),
                            field("name"),
                            field("created"),
                            field("updated")
                    )
                    .values(
                            partition.getUuid(),
                            partition.getOwner(),
                            partition.getShare(),
                            partition.getAmount(),
                            partition.getName(),
                            partition.getCreated(),
                            partition.getUpdated()
                    )
                    .execute();
        }
        catch (Exception e) {
            throw new PartitionAlreadyExistsException("add", partition.getUuid());
        }
    }

    @Override
    public void removePartition(String owner, String id) throws PartitionDoesNotExistException {
        int result = connection.get()
                .deleteFrom(table("partitions"))
                .where(field("uuid").eq(id).and(field("owner").eq(owner)))
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
                .set(field("name"), partition.getName())
                .set(field("updated"), partition.getUpdated())
                .where(field("uuid").eq(partition.getUuid()))
                .execute();
        if (0 == result) {
            throw new PartitionDoesNotExistException("update", partition.getUuid());
        }
    }

    @Override
    public Optional<Partition> getPartition(String owner, String id) {
        Result<Record> result = connection.get()
                .selectFrom(table("partitions"))
                .where(field("uuid").eq(id).and(field("owner").eq(owner)))
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

    @Override
    public double totalShare(String owner) {
        Result<Record1<BigDecimal>> results = connection.get()
                .select(sum(field("share", Double.class)))
                .from(table("partitions"))
                .where(field("owner").eq(owner))
                .fetch();
        Record1<BigDecimal> record = results.get(0);
        BigDecimal component =  record.component1();
        if (null == component) {
            // because the owner has no partitions
            return 0;
        }
        return component.doubleValue();
    }

    private Partition asPartition(Record record) {
        return new Partition(
                record.get(field("uuid"), String.class),
                record.get(field("created"), Timestamp.class).toLocalDateTime(),
                record.get(field("updated"), Timestamp.class).toLocalDateTime(),
                record.get(field("owner"), String.class),
                record.get(field("name"), String.class),
                record.get(field("share"), Double.class),
                record.get(field("amount"), Long.class)
        );
    }
}
