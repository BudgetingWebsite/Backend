package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

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

public class SQLPartitionDAO extends PartitionDAO<DSLContext> {
    private static final Table<Record> PARTITION_TABLE = table("partition");
    private static final Field<String> UUID_FIELD = field("uuid", String.class);
    private static final Field<Double> SHARE_FIELD = field("share", Double.class);
    private static final Field<String> OWNER_FIELD = field("owner", String.class);
    private static final Field<String> NAME_FIELD = field("name", String.class);
    private static final Field<LocalDateTime> UPDATED_FIELD = field("updated", LocalDateTime.class);

    public SQLPartitionDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addPartition(Partition partition) throws PartitionAlreadyExistsException {
        try {
            Record record = connection.get().newRecord(PARTITION_TABLE, partition);
            connection.get().executeInsert((TableRecord<?>) record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new PartitionAlreadyExistsException("add", partition.uuid);
        }
    }

    @Override
    public void removePartition(String user, String uuid) throws PartitionDoesNotExistException {
        int result = connection.get()
                .deleteFrom(PARTITION_TABLE)
                .where(UUID_FIELD.eq(uuid).and(OWNER_FIELD.eq(user)))
                .execute();
        if (0 == result) {
            throw new PartitionDoesNotExistException("remove", uuid);
        }
    }

    @Override
    public void updatePartition(Partition partition) throws PartitionDoesNotExistException {
        Record record = connection.get().newRecord(PARTITION_TABLE, partition);
        int result = connection.get().executeUpdate((UpdatableRecord<?>) record);
        if (0 == result) {
            throw new PartitionDoesNotExistException("update", partition.uuid);
        }
    }

    @Override
    public Optional<Partition> getPartition(String owner, String uuid) {
        try {
            List<Partition> list = connection.get()
                    .selectFrom(PARTITION_TABLE)
                    .where(UUID_FIELD.eq(uuid))
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
                    .selectFrom(PARTITION_TABLE)
                    .where(OWNER_FIELD.eq(user))
                    .fetch()
                    .into(Partition.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
