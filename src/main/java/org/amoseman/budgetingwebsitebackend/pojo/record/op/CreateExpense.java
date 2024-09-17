package org.amoseman.budgetingwebsitebackend.pojo.record.op;

public class CreateExpense extends CreateFinanceRecord {
    private String bucket;

    public CreateExpense() {
    }

    public CreateExpense(long amount, int year, int month, int day, String category, String description, String bucket) {
        super(amount, year, month, day, category, description);
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }
}
