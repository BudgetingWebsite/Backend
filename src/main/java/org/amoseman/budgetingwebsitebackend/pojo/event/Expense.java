package org.amoseman.budgetingwebsitebackend.pojo.event;

import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class Expense extends FinanceRecord {
    public final String partition;

    @ConstructorProperties({"uuid", "created", "updated", "owner", "amount", "occurred", "category", "description", "partition"})
    public Expense(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description, String partition) throws NegativeValueException, InvalidFinanceEventTypeException {
        super(uuid, created, updated, owner, amount, occurred, category, description);
        this.partition = partition;
    }
}
