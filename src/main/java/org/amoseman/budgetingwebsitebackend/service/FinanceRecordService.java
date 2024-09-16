package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.*;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceRecord;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateExpense;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateIncome;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.amoseman.budgetingwebsitebackend.util.Split;
import org.amoseman.budgetingwebsitebackend.util.Splitter;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FinanceRecordService<C> {
    private final FinanceRecordDAO<C> financeRecordDAO;
    private final PartitionDAO<C> partitionDAO;

    public FinanceRecordService(FinanceRecordDAO<C> financeRecordDAO, PartitionDAO<C> partitionDAO) {
        this.financeRecordDAO = financeRecordDAO;
        this.partitionDAO = partitionDAO;
    }

    public String addEvent(String user, CreateIncome create) throws NegativeValueException, InvalidFinanceEventTypeException, FinanceRecordAlreadyExistsException, DateTimeException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime when = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        Income event = new Income(
                uuid,
                now,
                now,
                user,
                create.getAmount(),
                when,
                create.getCategory(),
                create.getDescription()
        );
        financeRecordDAO.addEvent(event);
        addToPartitions(user, event.getAmount());
        return uuid;
    }

    public String addEvent(String user, CreateExpense create) throws NegativeValueException, InvalidFinanceEventTypeException, FinanceRecordAlreadyExistsException, DateTimeException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime when = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        Expense event = new Expense(
                uuid,
                now,
                now,
                user,
                create.getAmount(),
                when,
                create.getCategory(),
                create.getDescription(),
                create.getPartition()
        );
        financeRecordDAO.addEvent(event);
        addToPartition(user, create.getPartition(), -create.getAmount());
        return uuid;
    }

    private void addToPartitions(String owner, long amount) {
        List<Partition> partitions = partitionDAO.listPartitions(owner);
        double[] shares = new double[partitions.size()];
        for (int i = 0; i < partitions.size(); i++) {
            shares[i] = partitions.get(i).getShare();
        }
        Split split = Splitter.get(shares, amount);
        for (int i = 0; i < partitions.size(); i++) {
            long x = split.getAmounts()[i];
            try {
                partitionDAO.updatePartition(partitions.get(i).add(x));
            }
            catch (PartitionDoesNotExistException e) {
                // IMPOSSIBLE
            }
        }
    }

    private void addToPartition(String owner, String uuid, long amount) {
        Optional<Partition> maybe =  partitionDAO.getPartition(owner, uuid);
        if (maybe.isEmpty()) {
            return;
        }
        Partition partition = maybe.get();
        partition = partition.add(amount);
        try {
            partitionDAO.updatePartition(partition);
        }
        catch (PartitionDoesNotExistException e) {
            // IS NOT POSSIBLE
        }
    }

    public void removeEvent(String user, String id, String type) throws FinanceRecordDoesNotExistException {
        FinanceRecord event = financeRecordDAO.removeEvent(user, id, type);
        if ("income".equals(type)) {
            addToPartitions(user, -event.getAmount());
        }
        if ("expense".equals(type)) {
            Expense expense = (Expense) event;
            addToPartition(user, expense.getPartition(), expense.getAmount());
        }
    }

    public List<FinanceRecord> getEvents(
            String user, String type,
            String yearStartString, String monthStartString, String dayStartString,
            String yearEndString, String monthEndString, String dayEndString) throws NumberFormatException {

        int yearStart = Integer.parseInt(yearStartString);
        int monthStart = Integer.parseInt(monthStartString);
        int dayStart = Integer.parseInt(dayStartString);
        int yearEnd = Integer.parseInt(yearEndString);
        int monthEnd = Integer.parseInt(monthEndString);
        int dayEnd = Integer.parseInt(dayEndString);

        LocalDateTime start = LocalDateTime.of(yearStart, monthStart, dayStart, 0, 0);
        LocalDateTime end = LocalDateTime.of(yearEnd, monthEnd, dayEnd, 0, 0);
        TimeRange range = new TimeRange(start, end);
        return financeRecordDAO.getEvents(user, type, range);
    }

    public List<FinanceRecord> getEvents(String user, String type) {
        return financeRecordDAO.getEvents(user, type);
    }
}
