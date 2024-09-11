package org.amoseman.budgetingwebsitebackend.pojo.event;


import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;
import org.amoseman.budgetingwebsitebackend.pojo.Entity;
import org.amoseman.budgetingwebsitebackend.pojo.OwnedEntity;

import java.time.LocalDateTime;

public abstract class FinanceEvent extends OwnedEntity {
    private final String type;
    private final long amount;
    private final LocalDateTime occurred;
    private final String category;
    private final String description;

    public FinanceEvent(String uuid, LocalDateTime created, LocalDateTime updated, String owner, String type, long amount, LocalDateTime occurred, String category, String description) throws NegativeValueException, InvalidFinanceEventTypeException {
        super(uuid, created, updated, owner);
        if (amount < 0) {
            throw new NegativeValueException(amount);
        }
        if (!("income".equals(type) || "expense".equals(type))) {
            throw new InvalidFinanceEventTypeException(type);
        }
        this.type = type;
        this.amount = amount;
        this.occurred = occurred;
        this.category = category;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getOccurred() {
        return occurred;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }
}
