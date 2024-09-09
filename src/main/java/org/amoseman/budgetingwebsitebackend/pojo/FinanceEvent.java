package org.amoseman.budgetingwebsitebackend.pojo;

import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.time.LocalDateTime;

/**
 * Represents a income or expense event.
 */
public class FinanceEvent extends Creatable {
    public static final String INCOME = "income";
    public static final String EXPENSE = "expense";
    private final String user;
    private final long amount;
    private final String type;
    private final LocalDateTime when;

    /**
     * Instantiate a finance event.
     * @param identifier the identifier of the event.
     * @param created when this event was created.
     * @param user the user for the event.
     * @param amount the amount of the event in cents.
     * @param type the type of the event.
     * @param when when the event occurred.
     */
    public FinanceEvent(String identifier, LocalDateTime created, String user, long amount, String type, LocalDateTime when) throws NegativeValueException, InvalidFinanceEventTypeException {
        super(identifier, created);
        if (amount < 0) {
            throw new NegativeValueException(amount);
        }
        if (!(type.equals(INCOME) || type.equals(EXPENSE))) {
            throw new InvalidFinanceEventTypeException(type);
        }
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.when = when;
    }

    /**
     * Get the user for the event.
     * @return the user.
     */
    public String getUser() {
        return user;
    }

    /**
     * Get the amount.
     * @return the amount.
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Get the type.
     * @return the type.
     */
    public String getType() {
        return type;
    }

    /**
     * Get when the event occurred.
     * @return when the event occurred.
     */
    public LocalDateTime getWhen() {
        return when;
    }
}
