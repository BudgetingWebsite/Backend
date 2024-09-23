package org.amoseman.budgetingbackend.model.record.info;

/**
 * Represents a financial record creation operation.
 */
public class FinanceRecordInfo {
    private long amount;
    private int year;
    private int month;
    private int day;
    private String category;
    private String description;

    public FinanceRecordInfo() {

    }

    /**
     * Instantiate a financial record creation operation.
     * @param amount the amount in dollars associated with the new record.
     * @param year what year the record occurred.
     * @param month what month the record occurred.
     * @param day what day the record occurred.
     * @param category the category of the new record.
     * @param description the description of the new record.
     */
    public FinanceRecordInfo(long amount, int year, int month, int day, String category, String description) {
        this.amount = amount;
        this.year = year;
        this.month = month;
        this.day = day;
        this.category = category;
        this.description = description;
    }

    public long getAmount() {
        return amount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
