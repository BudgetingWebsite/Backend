package org.amoseman.budgetingwebsitebackend.pojo.record.op;

public class CreateIncome extends CreateFinanceRecord {

    public CreateIncome() {
    }

    public CreateIncome(long amount, int year, int month, int day, String category, String description) {
        super(amount, year, month, day, category, description);
    }
}
