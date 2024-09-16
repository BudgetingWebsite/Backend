package org.amoseman.budgetingwebsitebackend.pojo.event;


import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;
import org.amoseman.budgetingwebsitebackend.pojo.OwnedEntity;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public abstract class FinanceRecord extends OwnedEntity {
    public final long amount;
    public final LocalDateTime occurred;
    public final String category;
    public final String description;

    @ConstructorProperties({"uuid", "created", "updated", "owner", "amount", "occurred", "category", "description"})
    public FinanceRecord(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description) throws NegativeValueException, InvalidFinanceEventTypeException {
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
