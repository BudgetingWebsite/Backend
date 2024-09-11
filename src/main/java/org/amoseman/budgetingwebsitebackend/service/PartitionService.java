package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.pojo.event.ExpenseEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.IncomeEvent;
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
    private final FinanceEventDAO<C> financeEventDAO;

    public PartitionService(PartitionDAO<C> partitionDAO, FinanceEventDAO<C> financeEventDAO) {
        this.partitionDAO = partitionDAO;
        this.financeEventDAO = financeEventDAO;
    }

    public String addPartition(String owner, CreatePartition create) throws PartitionAlreadyExistsException {
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
        recalculate(owner);
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
        List<FinanceEvent> incomeEvents = financeEventDAO.getEvents(owner, "income");
        List<FinanceEvent> expenseEvents = financeEventDAO.getEvents(owner, "expense");
        List<IncomeEvent> income = new ArrayList<>();
        List<ExpenseEvent> expenses = new ArrayList<>();
        incomeEvents.forEach(event -> income.add((IncomeEvent) event));
        expenseEvents.forEach(event -> expenses.add((ExpenseEvent) event));
        for (IncomeEvent event : income) {
            Split split = Splitter.get(shares, event.getAmount());
            for (int i = 0; i < partitions.length; i++) {
                Partition partition = partitions[i];
                partitions[i] = partition.add(split.getAmounts()[i]);
            }
        }
        for (ExpenseEvent event : expenses) {
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
}
