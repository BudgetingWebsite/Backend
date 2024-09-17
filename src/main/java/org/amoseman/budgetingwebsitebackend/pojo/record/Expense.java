package org.amoseman.budgetingwebsitebackend.pojo.record;

import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class Expense extends FinanceRecord {
    public final String bucket;

    @ConstructorProperties({"uuid", "created", "updated", "owner", "amount", "occurred", "category", "description", "bucket"})
    public Expense(String uuid, LocalDateTime created, LocalDateTime updated, String owner, long amount, LocalDateTime occurred, String category, String description, String bucket) throws NegativeValueException {
        super(uuid, created, updated, owner, amount, occurred, category, description);
        this.bucket = bucket;
    }
}
