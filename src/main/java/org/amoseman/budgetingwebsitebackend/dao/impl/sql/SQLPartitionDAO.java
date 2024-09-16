package org.amoseman.budgetingwebsitebackend.dao.impl.sql;

import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.jooq.*;
import org.jooq.Record;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

import static org.jooq.codegen.Tables.*;
import org.jooq.codegen.tables.records.*;

public class SQLPartitionDAO extends PartitionDAO<DSLContext> {

    public SQLPartitionDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addPartition(Partition partition) throws PartitionAlreadyExistsException {
        try {
            PartitionRecord record = connection.get().newRecord(PARTITION, partition);
            connection.get().executeInsert(record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new PartitionAlreadyExistsException("add", partition.uuid);
        }
    }

    @Override
    public void removePartition(String user, String uuid) throws PartitionDoesNotExistException {
        int result = connection.get()
                .deleteFrom(PARTITION)
                .where(PARTITION.UUID.eq(uuid).and(PARTITION.OWNER.eq(user)))
                .execute();
        if (0 == result) {
            throw new PartitionDoesNotExistException("remove", uuid);
        }
    }

    @Override
    public void updatePartition(Partition partition) throws PartitionDoesNotExistException {
        PartitionRecord record = connection.get().newRecord(PARTITION, partition);
        int result = connection.get().executeUpdate(record);
        if (0 == result) {
            throw new PartitionDoesNotExistException("update", partition.uuid);
        }
    }

    @Override
    public Optional<Partition> getPartition(String owner, String uuid) {
        try {
            List<Partition> list = connection.get()
                    .selectFrom(PARTITION)
                    .where(PARTITION.UUID.eq(uuid))
                    .fetch()
                    .into(Partition.class);
            if (list.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(list.get(0));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Partition> getPartitions(String user) {
        try {
            return connection.get()
                    .selectFrom(PARTITION)
                    .where(PARTITION.OWNER.eq(user))
                    .fetch()
                    .into(Partition.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
