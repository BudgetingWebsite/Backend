package org.amoseman.budgetingbackend.service;


import org.amoseman.budgetingbackend.dao.FinanceRecordDAO;
import org.amoseman.budgetingbackend.exception.FinanceRecordAlreadyExistsException;
import org.amoseman.budgetingbackend.exception.FinanceRecordDoesNotExistException;
import org.amoseman.budgetingbackend.exception.NegativeValueException;
import org.amoseman.budgetingbackend.pojo.record.Expense;
import org.amoseman.budgetingbackend.pojo.record.Income;
import org.amoseman.budgetingbackend.pojo.record.info.ExpenseInfo;
import org.amoseman.budgetingbackend.pojo.record.info.IncomeInfo;

import java.util.List;

/**
 * A service for finance record logic.
 * @param <C> the client type.
 */
public abstract class FinanceRecordService<C> {
    protected final FinanceRecordDAO<C> financeRecordDAO;

    /**
     * Instantiate a finance record service.
     * @param financeRecordDAO the finance record data access object to use.
     */
    public FinanceRecordService(FinanceRecordDAO<C> financeRecordDAO) {
        this.financeRecordDAO = financeRecordDAO;
    }

    /**
     * Add an income record.
     * @param user the UUID of the owning user.
     * @param create the income info.
     * @return the UUID of the new record.
     * @throws FinanceRecordAlreadyExistsException if the record already exists.
     * @throws NegativeValueException if the amount provided in the record is negative.
     */
    public abstract String addIncome(String user, IncomeInfo create) throws FinanceRecordAlreadyExistsException, NegativeValueException;

    /**
     * Add an expense record.
     * @param user the UUID of the owning user.
     * @param create the expense info.
     * @return the UUID of the new record.
     * @throws FinanceRecordAlreadyExistsException if the record already exists.
     * @throws NegativeValueException if the amount provided in the record is negative.
     */
    public abstract String addExpense(String user, ExpenseInfo create) throws FinanceRecordAlreadyExistsException, NegativeValueException;

    /**
     * Remove an income record.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the record.
     * @return true if successful, false otherwise.
     */
    public abstract boolean removeIncome(String user, String uuid);

    /**
     * Remove an expense record.
     * @param user the UUID of the owning user.
     * @param uuid the UUID of the record.
     * @return true if successful, false otherwise.
     */
    public abstract boolean removeExpense(String user, String uuid);

    /**
     * Get all income records within the provided dates.
     * @param user the UUID of the owning user.
     * @param yearStart the start year of the range.
     * @param monthStart the start month of the range.
     * @param dayStart the start day of the range.
     * @param yearEnd the end year of the range.
     * @param monthEnd the end month of the range.
     * @param dayEnd the end day of the range.
     * @return the list of matching records.
     * @throws NumberFormatException if one of the year, month, or day values is invalid.
     */
    public abstract List<Income> getIncome(
            String user,
            int yearStart,
            int monthStart,
            int dayStart,
            int yearEnd,
            int monthEnd,
            int dayEnd) throws NumberFormatException;

    /**
     * Get all expense records within the provided dates.
     * @param user the UUID of the owning user.
     * @param yearStart the start year of the range.
     * @param monthStart the start month of the range.
     * @param dayStart the start day of the range.
     * @param yearEnd the end year of the range.
     * @param monthEnd the end month of the range.
     * @param dayEnd the end day of the range.
     * @return the list of matching records.
     * @throws NumberFormatException if one of the year, month, or day values is invalid.
     */
    public abstract List<Expense> getExpenses(
            String user,
            int yearStart,
            int monthStart,
            int dayStart,
            int yearEnd,
            int monthEnd,
            int dayEnd);

    /**
     * Update an income record.
     * @param user the UUID of the user.
     * @param uuid the UUID of the record.
     * @param update the updated record information.
     * @throws NegativeValueException if amount in the updated is negative.
     * @throws FinanceRecordDoesNotExistException if the record does not exist.
     */
    public abstract void updateIncome(String user, String uuid, IncomeInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException;

    /**
     * Update an expense record.
     * @param user the UUID of the user.
     * @param uuid the UUID of the record.
     * @param update the updated record information.
     * @throws NegativeValueException if amount in the updated is negative.
     * @throws FinanceRecordDoesNotExistException if the record does not exist.
     */
    public abstract void updateExpense(String user, String uuid, ExpenseInfo update) throws NegativeValueException, FinanceRecordDoesNotExistException;
}
