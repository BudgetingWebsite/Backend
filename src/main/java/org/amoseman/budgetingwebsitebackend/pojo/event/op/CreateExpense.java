package org.amoseman.budgetingwebsitebackend.pojo.event.op;

public class CreateExpense extends CreateFinanceRecord {
    private String partition;

    public CreateExpense() {
    }

    public CreateExpense(long amount, int year, int month, int day, String category, String description, String partition) {
        super(amount, year, month, day, category, description);
        this.partition = partition;
    }

    public String getPartition() {
        return partition;
    }
}
