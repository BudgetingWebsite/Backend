package org.amoseman.budgetingwebsitebackend.pojo.record;

import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents an income record.
 */
public class Income extends FinanceRecord {

    /**
     * Instantiate a income record.
     * @param uuid the UUID of the income.
     * @param created when the income was created.
     * @param updated when the income was last updated.
     * @param owner the UUID of the account with this income.
     * @param amount the dollar amount of the income.
     * @param occurred when the income occurred in reality.
     * @param category the category of the income.
     * @param description the description of the income.
     * @throws NegativeValueException if the dollar amount provided is negative.
     */
    @ConstructorProperties({"uuid", "created", "updated", "owner", "amount", "occurred", "category", "description"})
    public Income(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description) throws NegativeValueException {
        super(uuid, created, updated, owner, amount, occurred, category, description);
    }
}
