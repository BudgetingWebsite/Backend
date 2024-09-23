package org.amoseman.budgetingbackend.model.record.info;

/**
 * Represents an expense record creation operation.
 */
public class ExpenseInfo extends FinanceRecordInfo {
    private String bucket;

    public ExpenseInfo() {
    }

    /**
     * Instantiate an expense record creation operation.
     * @param amount the amount in dollars associated with the new expense.
     * @param year what year the expense occurred.
     * @param month what month the expense occurred.
     * @param day what day the expense occurred.
     * @param category the category of the new expense.
     * @param description the description of the new expense.
     * @param bucket the bucket associate with the expense.
     */
    public ExpenseInfo(long amount, int year, int month, int day, String category, String description, String bucket) {
        super(amount, year, month, day, category, description);
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }
}
