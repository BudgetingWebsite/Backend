package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.*;
import org.amoseman.budgetingwebsitebackend.pojo.event.ExpenseEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.FinanceEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.IncomeEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateExpenseEvent;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateIncomeEvent;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.amoseman.budgetingwebsitebackend.util.Split;
import org.amoseman.budgetingwebsitebackend.util.Splitter;
import org.jooq.impl.QOM;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FinanceEventService<C> {
    private final FinanceEventDAO<C> financeEventDAO;
    private final PartitionDAO<C> partitionDAO;

    public FinanceEventService(FinanceEventDAO<C> financeEventDAO, PartitionDAO<C> partitionDAO) {
        this.financeEventDAO = financeEventDAO;
        this.partitionDAO = partitionDAO;
    }

    public String addEvent(String user, CreateIncomeEvent create) throws NegativeValueException, InvalidFinanceEventTypeException, FinanceEventAlreadyExistsException, DateTimeException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime when = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        IncomeEvent event = new IncomeEvent(
                uuid,
                now,
                now,
                user,
                create.getAmount(),
                when,
                create.getCategory(),
                create.getDescription()
        );
        financeEventDAO.addEvent(event);
        addToPartitions(user, event.getAmount());
        return uuid;
    }

    public String addEvent(String user, CreateExpenseEvent create) throws NegativeValueException, InvalidFinanceEventTypeException, FinanceEventAlreadyExistsException, DateTimeException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime when = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        ExpenseEvent event = new ExpenseEvent(
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
        financeEventDAO.addEvent(event);
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

    public void removeEvent(String user, String id, String type) throws FinanceEventDoesNotExistException {
        FinanceEvent event = financeEventDAO.removeEvent(user, id, type);
        if ("income".equals(type)) {
            addToPartitions(user, -event.getAmount());
        }
        if ("expense".equals(type)) {
            ExpenseEvent expense = (ExpenseEvent) event;
            addToPartition(user, expense.getPartition(), expense.getAmount());
        }
    }

    public List<FinanceEvent> getEvents(
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
        return financeEventDAO.getEvents(user, type, range);
    }

    public List<FinanceEvent> getEvents(String user, String type) {
        return financeEventDAO.getEvents(user, type);
    }
}
