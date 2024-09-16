package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.TotalPartitionShareExceededException;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceRecord;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.CreatePartition;
import org.amoseman.budgetingwebsitebackend.pojo.partition.op.UpdatePartition;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.amoseman.budgetingwebsitebackend.util.Split;
import org.amoseman.budgetingwebsitebackend.util.Splitter;

import java.time.LocalDateTime;
import java.util.*;

public class PartitionService<C> {
    private final PartitionDAO<C> partitionDAO;
    private final FinanceRecordDAO<C> financeRecordDAO;

    public PartitionService(PartitionDAO<C> partitionDAO, FinanceRecordDAO<C> financeRecordDAO) {
        this.partitionDAO = partitionDAO;
        this.financeRecordDAO = financeRecordDAO;
    }

    public String addPartition(String owner, CreatePartition create) throws PartitionAlreadyExistsException, TotalPartitionShareExceededException {
        double totalShare = partitionDAO.totalShare(owner);
        if (totalShare + create.getShare() > 1) {
            throw new TotalPartitionShareExceededException(totalShare, create.getShare());
        }
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
        try {
            recalculate(owner, uuid);
        }
        catch (PartitionDoesNotExistException e) {
            throw new RuntimeException(e); // SHOULD NEVER happen
        }
        return uuid;
    }

    public void removePartition(String owner, String id) throws PartitionDoesNotExistException {
        partitionDAO.removePartition(owner, id);
        recalculate(owner);
    }

    public void updatePartition(String owner, String id, UpdatePartition update) throws PartitionDoesNotExistException {
        Optional<Partition> maybe = partitionDAO.getPartition(owner, id);
        if (maybe.isEmpty()) {
            throw new PartitionDoesNotExistException("update", id);
        }
        Partition partition = maybe.get();
        boolean changedShare = update.getShare() != partition.getShare();
        partition = partition.update(update);
        partitionDAO.updatePartition(partition);
        if (changedShare) {
            recalculate(owner);
        }
    }

    public List<Partition> listPartitions(String owner) {
        return partitionDAO.listPartitions(owner);
    }

    private void recalculate(String owner) {
        Partition[] partitions = partitionDAO.listPartitions(owner).toArray(new Partition[0]);
        double[] shares = new double[partitions.length];
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < partitions.length; i++) {
            Partition partition = partitions[i];
            partitions[i] = partition.add(-partition.getAmount());
            shares[i] = partition.getShare();
            map.put(partition.getUuid(), i);
        }
        List<FinanceRecord> incomeEvents = financeRecordDAO.getEvents(owner, "income");
        List<FinanceRecord> expenseEvents = financeRecordDAO.getEvents(owner, "expense");
        List<Income> income = new ArrayList<>();
        List<Expense> expenses = new ArrayList<>();
        incomeEvents.forEach(event -> income.add((Income) event));
        expenseEvents.forEach(event -> expenses.add((Expense) event));
        for (Income event : income) {
            Split split = Splitter.get(shares, event.getAmount());
            for (int i = 0; i < partitions.length; i++) {
                Partition partition = partitions[i];
                partitions[i] = partition.add(split.getAmounts()[i]);
            }
        }
        for (Expense event : expenses) {
            int i = map .get(event.getPartition());
            Partition partition = partitions[i];
            partitions[i] = partition.add(-event.getAmount());
        }
        for (Partition partition : partitions) {
            try {
                partitionDAO.updatePartition(partition);
            } catch (PartitionDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void recalculate(String owner, String uuid) throws PartitionDoesNotExistException {
        Optional<Partition> maybe = partitionDAO.getPartition(owner, uuid);
        if (maybe.isEmpty()) {
            throw new PartitionDoesNotExistException("recalculate", uuid);
        }
        Partition partition = maybe.get();
        List<FinanceRecord> income = financeRecordDAO.getEvents(owner, "income");
        List<FinanceRecord> expenses = financeRecordDAO.getEvents(owner, "expense");
        for (FinanceRecord e : income) {
            Income event = (Income) e;
            Split split = Splitter.get(new double[]{partition.getShare()}, event.getAmount());
            partition = partition.add(split.getAmounts()[0]);
        }
        for (FinanceRecord e : expenses) {
            Expense event = (Expense) e;
            if (!event.getPartition().equals(partition.getUuid())) {
                continue;
            }
            partition = partition.add(-event.getAmount());
        }
        partitionDAO.updatePartition(partition);
    }
}
