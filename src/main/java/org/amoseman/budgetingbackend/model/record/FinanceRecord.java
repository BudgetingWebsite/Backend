package org.amoseman.budgetingbackend.model.record;


import org.amoseman.budgetingbackend.exception.NegativeValueException;
import org.amoseman.budgetingbackend.model.OwnedEntity;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

/**
 * Represents a financial record.
 */
public abstract class FinanceRecord extends OwnedEntity {
    public final long amount;
    public final LocalDateTime occurred;
    public final String category;
    public final String description;

    /**
     * Instantiate a financial record.
     * @param uuid the UUID of the record.
     * @param created when the record was created.
     * @param updated when the record was last updated.
     * @param owner the UUID of the account with this record.
     * @param amount the dollar amount of the record.
     * @param occurred when the record occurred in reality.
     * @param category the category of the record.
     * @param description the description of the record.
     * @throws NegativeValueException if the dollar amount provided is negative.
     */
    @ConstructorProperties({"uuid", "created", "updated", "owner", "amount", "occurred", "category", "description"})
    public FinanceRecord(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description) throws NegativeValueException {
        super(uuid, created, updated, owner);
        if (amount < 0) {
            throw new NegativeValueException(amount);
        }
        this.amount = amount;
        this.occurred = occurred;
        this.category = category;
        this.description = description;
    }
}
