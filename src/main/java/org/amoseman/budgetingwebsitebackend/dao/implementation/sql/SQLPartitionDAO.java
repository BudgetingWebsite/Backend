package org.amoseman.budgetingwebsitebackend.dao.implementation.sql;

import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.Partition;
import org.jooq.DSLContext;

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
    public void removePartition(String id) throws PartitionDoesNotExistException {
        int result = connection.get()
                .deleteFrom(table("partitions"))
                .where(field("id").eq(id))
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
}
