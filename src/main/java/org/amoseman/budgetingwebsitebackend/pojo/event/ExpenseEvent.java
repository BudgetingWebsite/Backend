package org.amoseman.budgetingwebsitebackend.pojo.event;

import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.time.LocalDateTime;

public class ExpenseEvent extends FinanceEvent {
    private final String partition;

    public ExpenseEvent(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description, String partition) throws NegativeValueException, InvalidFinanceEventTypeException {
        super(uuid, created, updated, owner, "expense", amount, occurred, category, description);
        this.partition = partition;
    }

    public String getPartition() {
        return partition;
    }
}
