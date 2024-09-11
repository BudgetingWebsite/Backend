package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.CreatePartition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.UpdatePartition;
import org.amoseman.budgetingwebsitebackend.util.Now;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PartitionService<C> {
    private final PartitionDAO<C> partitionDAO;

    public PartitionService(PartitionDAO<C> partitionDAO) {
        this.partitionDAO = partitionDAO;
    }

    public void addPartition(String owner, CreatePartition create) throws PartitionAlreadyExistsException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        Partition partition = new Partition(
                uuid,
                now,
                now,
                owner,
                create.getName(),
                create.getShare(),
                0
        );
        partitionDAO.addPartition(partition);
    }

    public void removePartition(String owner, String id) throws PartitionDoesNotExistException {
        partitionDAO.removePartition(owner, id);
    }

    public void updatePartition(String owner, String id, UpdatePartition update) throws PartitionDoesNotExistException {
        Optional<Partition> maybe = partitionDAO.getPartition(owner, id);
        if (maybe.isEmpty()) {
            throw new PartitionDoesNotExistException("update", id);
        }
        Partition partition = maybe.get();
        partition = partition.update(update);
        partitionDAO.updatePartition(partition);
    }

    public List<Partition> listPartitions(String owner) {
        return partitionDAO.listPartitions(owner);
    }
}
