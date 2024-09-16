package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.PartitionAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.PartitionDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.TotalPartitionShareExceededException;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
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

    public String addPartition(String user, CreatePartition create) throws PartitionAlreadyExistsException, TotalPartitionShareExceededException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        Partition partition = new Partition(
                uuid,
                now,
                now,
                user,
                create.getName(),
                create.getShare(),
                0
        );
        partitionDAO.addPartition(partition);
        return uuid;
    }

    public void removePartition(String user, String id) throws PartitionDoesNotExistException {
        partitionDAO.removePartition(user, id);
    }

    public void updatePartition(String user, String id, UpdatePartition update) throws PartitionDoesNotExistException {
        LocalDateTime now = Now.get();
        Optional<Partition> maybe = partitionDAO.getPartition(user, id);
        if (maybe.isEmpty()) {
            throw new PartitionDoesNotExistException("update", id);
        }
        Partition partition = maybe.get();
        partition = new Partition(partition, update, now);
        partitionDAO.updatePartition(partition);
    }

    public List<Partition> getPartitions(String user) {
        List<Partition> partitions = partitionDAO.getPartitions(user);
        List<Income> incomeRecords = financeRecordDAO.getAllIncome(user);
        incomeRecords.forEach(income -> {
            Split split = Splitter.get(partitions, income.amount);
            for (int i = 0; i < partitions.size(); i++) {
                long amount = split.getAmounts()[i];
                Partition partition = partitions.get(i);
                partition.amount += amount;
            }
        });
        Map<String, Partition> map = new HashMap<>();
        partitions.forEach(partition -> map.put(partition.uuid, partition));
        List<Expense> expenseRecords = financeRecordDAO.getAllExpenses(user);
        expenseRecords.forEach(expense -> {
            Partition partition = map.get(expense.partition);
            partition.amount -= expense.amount;
        });
        return partitions;
    }
}
