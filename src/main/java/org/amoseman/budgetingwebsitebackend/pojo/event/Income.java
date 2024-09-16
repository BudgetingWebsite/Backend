package org.amoseman.budgetingwebsitebackend.pojo.event;

import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.time.LocalDateTime;

public class Income extends FinanceRecord {
    public Income(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description) throws NegativeValueException, InvalidFinanceEventTypeException {
        super(uuid, created, updated, owner, "income", amount, occurred, category, description);
    }
}
