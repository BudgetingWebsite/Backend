package org.amoseman.budgetingbackend.service;

import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.exception.*;
import org.amoseman.budgetingbackend.pojo.*;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.amoseman.budgetingbackend.pojo.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.pojo.record.info.IncomeInfo;
import org.amoseman.budgetingbackend.util.Now;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class FinanceRecordService<C> {
    private final FinanceRecordDAO<C> financeRecordDAO;

    public FinanceRecordService(FinanceRecordDAO<C> financeRecordDAO) {
        this.financeRecordDAO = financeRecordDAO;
    }

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

    public boolean removeIncome(String user, String uuid) {
        return financeRecordDAO.removeIncome(user, uuid);
    }

    public boolean removeExpense(String user, String uuid) {
        return financeRecordDAO.removeExpense(user, uuid);
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

    public void updateIncome(String user, String uuid, IncomeInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException {
        financeRecordDAO.updateIncome(user, uuid, update);
    }

    public void updateExpense(String user, String uuid, ExpenseInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException {
        financeRecordDAO.updateExpense(user, uuid, update);
    }
}
