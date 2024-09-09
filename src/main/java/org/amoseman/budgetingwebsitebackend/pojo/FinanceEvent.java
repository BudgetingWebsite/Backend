package org.amoseman.budgetingwebsitebackend.pojo;

import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.time.LocalDateTime;

/**
 * Represents a income or expense event.
 */
public class FinanceEvent extends Creatable {
    private final long amount;

    /**
     * Instantiate a finance event.
     * @param created when the event occurred.
     * @param amount the amount of the event in cents.
     */
    public FinanceEvent(LocalDateTime created, long amount) throws NegativeValueException {
        super(created);
        if (amount < 0) {
            throw new NegativeValueException(amount);
        }
        this.amount = amount;
    }

    /**
     * Get the amount.
     * @return the amount.
     */
    public long getAmount() {
        return amount;
    }
}
