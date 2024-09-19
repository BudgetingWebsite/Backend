package org.amoseman.budgetingbackend.pojo.record.info;

/**
 * Represents an income record creation operation.
 */
public class IncomeInfo extends FinanceRecordInfo {

    public IncomeInfo() {
    }

    /**
     * Instantiate an income record creation operation.
     * @param amount the amount in dollars associated with the new income.
     * @param year what year the income occurred.
     * @param month what month the income occurred.
     * @param day what day the income occurred.
     * @param category the category of the new income.
     * @param description the description of the new income.
     */
    public IncomeInfo(long amount, int year, int month, int day, String category, String description) {
        super(amount, year, month, day, category, description);
    }
}
