package org.amoseman.budgetingbackend.service.impl;

import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.exception.*;
import org.amoseman.budgetingbackend.pojo.*;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.amoseman.budgetingbackend.pojo.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.pojo.record.info.IncomeInfo;
import org.amoseman.budgetingbackend.service.FinanceRecordService;
import org.amoseman.budgetingbackend.util.Now;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FinanceRecordServiceImpl<C> extends FinanceRecordService<C> {
    public FinanceRecordServiceImpl(FinanceRecordDAO<C> financeRecordDAO) {
        super(financeRecordDAO);
    }

    @Override
    public String addIncome(String user, IncomeInfo create) throws FinanceRecordAlreadyExistsException, NegativeValueException {
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
        return uuid;
    }

    @Override
    public String addExpense(String user, ExpenseInfo create) throws FinanceRecordAlreadyExistsException, NegativeValueException {
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
                create.getBucket()
        );
        financeRecordDAO.addExpense(expense);
        return uuid;
    }

    @Override
    public boolean removeIncome(String user, String uuid) {
        return financeRecordDAO.removeIncome(user, uuid);
    }

    @Override
    public boolean removeExpense(String user, String uuid) {
        return financeRecordDAO.removeExpense(user, uuid);
    }

    @Override
    public List<Income> getIncome(
            String user,
            int yearStart,
            int monthStart,
            int dayStart,
            int yearEnd,
            int monthEnd,
            int dayEnd) throws NumberFormatException {
        LocalDateTime start = LocalDateTime.of(yearStart, monthStart, dayStart, 0, 0);
        LocalDateTime end = LocalDateTime.of(yearEnd, monthEnd, dayEnd, 0, 0);
        TimeRange range = new TimeRange(start, end);
        return financeRecordDAO.getIncomeInRange(user, range);
    }

    @Override
    public List<Expense> getExpenses(
            String user,
            int yearStart,
            int monthStart,
            int dayStart,
            int yearEnd,
            int monthEnd,
            int dayEnd) {
        LocalDateTime start = LocalDateTime.of(yearStart, monthStart, dayStart, 0, 0);
        LocalDateTime end = LocalDateTime.of(yearEnd, monthEnd, dayEnd, 0, 0);
        TimeRange range = new TimeRange(start, end);
        return financeRecordDAO.getExpensesInRange(user, range);
    }

    @Override
    public void updateIncome(String user, String uuid, IncomeInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException {
        financeRecordDAO.updateIncome(user, uuid, update);
    }

    @Override
    public void updateExpense(String user, String uuid, ExpenseInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException {
        financeRecordDAO.updateExpense(user, uuid, update);
    }
}
