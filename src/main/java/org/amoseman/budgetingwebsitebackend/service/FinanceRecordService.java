package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingwebsitebackend.dao.PartitionDAO;
import org.amoseman.budgetingwebsitebackend.exception.*;
import org.amoseman.budgetingwebsitebackend.pojo.*;
import org.amoseman.budgetingwebsitebackend.pojo.event.Expense;
import org.amoseman.budgetingwebsitebackend.pojo.event.Income;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateExpense;
import org.amoseman.budgetingwebsitebackend.pojo.event.op.CreateIncome;
import org.amoseman.budgetingwebsitebackend.pojo.partition.Partition;
import org.amoseman.budgetingwebsitebackend.util.Now;
import org.amoseman.budgetingwebsitebackend.util.Split;
import org.amoseman.budgetingwebsitebackend.util.Splitter;

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

    public String addIncome(String user, CreateIncome create) throws FinanceRecordAlreadyExistsException, NegativeValueException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime occurred = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        Income income = new Income(
                uuid,
                now,
                now,
                user,
                create.getAmount(),
                occurred,
                create.getCategory(),
                create.getDescription()
        );
        financeRecordDAO.addIncome(income);
        addToPartitions(user, income.amount);
        return uuid;
    }

    public String addExpense(String user, CreateExpense create) throws FinanceRecordAlreadyExistsException, NegativeValueException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime occurred = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        Expense expense = new Expense(
                uuid,
                now,
                now,
                user,
                create.getAmount(),
                occurred,
                create.getCategory(),
                create.getDescription(),
                create.getPartition()
        );
        financeRecordDAO.addExpense(expense);
        addToPartition(user, expense.partition, -expense.amount);
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

    public boolean removeIncome(String user, String uuid) {
        Optional<Income> maybe = financeRecordDAO.getIncome(user, uuid);
        if (maybe.isEmpty()) {
            return false;
        }
        Income income = maybe.get();
        if (financeRecordDAO.removeIncome(user, uuid)) {
            addToPartitions(user, -income.amount);
            return true;
        }
        return false;
    }

    public boolean removeExpense(String user, String uuid) {
        Optional<Expense> maybe = financeRecordDAO.getExpense(user, uuid);
        if (maybe.isEmpty()) {
            return false;
        }
        Expense expense = maybe.get();
        if (financeRecordDAO.removeExpense(user, uuid)) {
            addToPartition(user, expense.partition, expense.amount);
            return true;
        }
        return false;
    }

    private LocalDateTime toLocalDateTime(String yearString, String monthString, String dayString) throws NumberFormatException {
        int year = Integer.parseInt(yearString);
        int month = Integer.parseInt(monthString);
        int day = Integer.parseInt(dayString);
        return LocalDateTime.of(year, month, day, 0, 0);
    }

    public List<Income> getIncome(
            String user,
            String yearStartString,
            String monthStartString,
            String dayStartString,
            String yearEndString,
            String monthEndString,
            String dayEndString) throws NumberFormatException {
        LocalDateTime start = toLocalDateTime(yearStartString, monthStartString, dayStartString);
        LocalDateTime end = toLocalDateTime(yearEndString, monthEndString, dayEndString);
        TimeRange range = new TimeRange(start, end);
        return financeRecordDAO.getIncomeInRange(user, range);
    }

    public List<Expense> getExpenses(
            String user,
            String yearStartString,
            String monthStartString,
            String dayStartString,
            String yearEndString,
            String monthEndString,
            String dayEndString) {
        LocalDateTime start = toLocalDateTime(yearStartString, monthStartString, dayStartString);
        LocalDateTime end = toLocalDateTime(yearEndString, monthEndString, dayEndString);
        TimeRange range = new TimeRange(start, end);
        return financeRecordDAO.getExpensesInRange(user, range);
    }
}
