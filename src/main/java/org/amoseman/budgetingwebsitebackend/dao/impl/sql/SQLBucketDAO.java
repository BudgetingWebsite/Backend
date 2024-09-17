package org.amoseman.budgetingwebsitebackend.dao.impl.sql;

import org.amoseman.budgetingwebsitebackend.dao.BucketDAO;
import org.amoseman.budgetingwebsitebackend.database.DatabaseConnection;
import org.amoseman.budgetingwebsitebackend.exception.BucketAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.BucketDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.bucket.Bucket;
import org.jooq.*;

import java.util.List;
import java.util.Optional;

import static org.jooq.codegen.Tables.*;
import org.jooq.codegen.tables.records.*;

public class SQLBucketDAO extends BucketDAO<DSLContext> {

    public SQLBucketDAO(DatabaseConnection<DSLContext> connection) {
        super(connection);
    }

    @Override
    public void addBucket(Bucket bucket) throws BucketAlreadyExistsException {
        try {
            BucketRecord record = connection.get().newRecord(BUCKET, bucket);
            connection.get().executeInsert(record);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new BucketAlreadyExistsException("add", bucket.uuid);
        }
    }

    @Override
    public void removeBucket(String user, String uuid) throws BucketDoesNotExistException {
        int result = connection.get()
                .deleteFrom(BUCKET)
                .where(BUCKET.UUID.eq(uuid).and(BUCKET.OWNER.eq(user)))
                .execute();
        if (0 == result) {
            throw new BucketDoesNotExistException("remove", uuid);
        }
    }

    @Override
    public void updateBucket(Bucket bucket) throws BucketDoesNotExistException {
        BucketRecord record = connection.get().newRecord(BUCKET, bucket);
        int result = connection.get().executeUpdate(record);
        if (0 == result) {
            throw new BucketDoesNotExistException("update", bucket.uuid);
        }
    }

    @Override
    public Optional<Bucket> getBucket(String owner, String uuid) {
        try {
            List<Bucket> list = connection.get()
                    .selectFrom(BUCKET)
                    .where(BUCKET.UUID.eq(uuid))
                    .fetch()
                    .into(Bucket.class);
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
    public List<Bucket> getBuckets(String user) {
        try {
            return connection.get()
                    .selectFrom(BUCKET)
                    .where(BUCKET.OWNER.eq(user))
                    .fetch()
                    .into(Bucket.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
