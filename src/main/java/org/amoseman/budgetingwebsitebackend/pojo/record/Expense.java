package org.amoseman.budgetingwebsitebackend.pojo.record;

import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents an expense record.
 */
public class Expense extends FinanceRecord {
    public final String bucket;

    /**
     * Instantiate an expense record.
     * @param uuid the UUID of the expense.
     * @param created when the expense was created.
     * @param updated when the expense was last updated.
     * @param owner the UUID of the account with this expense.
     * @param amount the dollar amount of the expense.
     * @param occurred when the expense occurred in reality.
     * @param category the category of the expense.
     * @param description the description of the expense.
     * @param bucket the bucket associated with the expense.
     * @throws NegativeValueException if the dollar amount provided is negative.
     */
    @ConstructorProperties({"uuid", "created", "updated", "owner", "amount", "occurred", "category", "description", "bucket"})
    public Expense(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description, String bucket) throws NegativeValueException {
        super(uuid, created, updated, owner, amount, occurred, category, description);
        this.bucket = bucket;
    }
}
